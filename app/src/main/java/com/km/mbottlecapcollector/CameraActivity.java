package com.km.mbottlecapcollector;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.km.mbottlecapcollector.util.ScreenRatioHelper;
import com.km.mbottlecapcollector.view.AdjustedTextureView;
import com.km.mbottlecapcollector.view.CameraOverlay;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.km.mbottlecapcollector.util.ScreenRatioHelper.FULL_HD_HEIGHT;
import static com.km.mbottlecapcollector.util.ScreenRatioHelper.FULL_HD_WIDTH;
import static com.km.mbottlecapcollector.util.ScreenRatioHelper.IMAGE_RATIO;

public abstract class CameraActivity extends Activity {

    private static final String TAG = CameraActivity.class.getSimpleName();
    private Button takePictureButton;
    private AdjustedTextureView textureView;
    private RelativeLayout rootLayout;
    public CameraOverlay screenSquare;
    public static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    public String cameraId;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest captureRequest;
    private CaptureRequest.Builder captureRequestBuilder;
    private Size cameraPreviewResolution;
    private Size imageResolution;
    private ImageReader imageReader;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    public int deviceWidth;
    private int cameraPreviewWidth;
    public String capturedImageURI;
    private ProgressDialog progressBar;

    /**
     * Fixed values to ensure that preview don't have too high resolution which may cause
     * slow performance
     */

    private double cameraImageRatio = 1;
    public CameraManager manager;
    private StreamConfigurationMap map;

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            Log.i(TAG, "Opening camera");
            cameraDevice = camera;
            createCameraPreview();
            imageResolution = calculateImageResolution();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    final CameraCaptureSession.CaptureCallback captureCallbackListener =
            new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request,
                                               TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    createCameraPreview();
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initializeTextureView();
        initializeButton();
        initializeSurface();
        initializeProgressBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraDevice.close();
    }

    public void initializeTextureView() {
        textureView = (AdjustedTextureView) findViewById(R.id.texture);
        textureView.setSurfaceTextureListener(textureListener);
        deviceWidth = ScreenRatioHelper.getScreenWidth();
        Log.i(TAG, "Device Width: " + deviceWidth);
    }

    public void initializeButton() {
        takePictureButton = findViewById(R.id.btn_takepicture);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        takePictureButton.setEnabled(false);
    }

    public void initializeSurface() {
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        screenSquare = initializeOverlay();
        rootLayout.addView(screenSquare);
    }

    public void initializeProgressBar() {
        progressBar = new ProgressDialog(this);
        progressBar.setTitle(R.string.loading);
        progressBar.setMessage(getString(R.string.loading_picture));
        progressBar.setCancelable(false);
        progressBar.dismiss();
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void takePicture() {
        progressBar.show();
        if (null == cameraDevice) {
            Log.i(TAG, "There is no camera available");
            return;
        }
        try {
            ImageReader reader = ImageReader.newInstance(imageResolution.getWidth(),
                    imageResolution.getHeight(), ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder =
                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            final File fileCircle = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    getFileSuffix() + System.currentTimeMillis() + ".jpg");
            cameraImageRatio = imageResolution.getHeight() / (double) cameraPreviewWidth;
            ImageReader.OnImageAvailableListener readerListener =
                    new ImageReader.OnImageAvailableListener() {
                        @Override
                        public void onImageAvailable(ImageReader reader) {
                            try (Image image = reader.acquireLatestImage()) {
                                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                                byte[] bytes = new byte[buffer.capacity()];
                                buffer.get(bytes);
                                Bitmap originalBitmap = BitmapFactory.decodeByteArray(bytes,
                                        0, bytes.length, null);
                                if (ScreenRatioHelper.isDeviceSMA50()) {
                                    Matrix matrix = new Matrix();
                                    matrix.postRotate(90);
                                    originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0,
                                            originalBitmap.getWidth(), originalBitmap.getHeight(), matrix,
                                            true);
                                }
                                originalBitmap = rotateImage(originalBitmap);
                                Bitmap squareBitmap = Bitmap.createBitmap(originalBitmap,
                                        (int) (cameraImageRatio * deviceWidth * (1 - getCircleScreenRatio()) / 2),
                                        (int) (cameraImageRatio * deviceWidth * (1 - getCircleScreenRatio()) / 2),
                                        (int) ((deviceWidth * getCircleScreenRatio() * cameraImageRatio)),
                                        (int) ((deviceWidth * getCircleScreenRatio() * cameraImageRatio)));
                                Bitmap circleBitmap = getCircledBitmap(squareBitmap);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                squareBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                ByteArrayOutputStream streamCircle = new ByteArrayOutputStream();
                                circleBitmap.compress(Bitmap.CompressFormat.PNG, 100, streamCircle);
                                byte[] outputArrayCircle = streamCircle.toByteArray();
                                save(outputArrayCircle, fileCircle);
                                capturedImageURI = fileCircle.getPath();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        private void save(byte[] bytes, File file) throws IOException {
                            OutputStream output = null;
                            try {
                                output = new FileOutputStream(file);
                                output.write(bytes);
                            } finally {
                                if (null != output) {
                                    output.close();
                                }
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                        Uri.fromFile(file)));
                            }
                        }
                    };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener =
                    new CameraCaptureSession.CaptureCallback() {
                        @Override
                        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request,
                                                       TotalCaptureResult result) {
                            super.onCaptureCompleted(session, request, result);
                            progressBar.dismiss();
                            goToChoiceActivity();
                        }
                    };
            cameraDevice.createCaptureSession(outputSurfaces,
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(CameraCaptureSession session) {
                            try {
                                session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession session) {
                        }
                    }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(cameraPreviewResolution.getWidth(),
                    cameraPreviewResolution.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            takePictureButton.setEnabled(true);
                            //The camera is already closed
                            if (null == cameraDevice) {
                                return;
                            }
                            // When the session is ready, we start displaying the preview.
                            cameraCaptureSessions = cameraCaptureSession;
                            updatePreview();
                            cameraPreviewWidth = textureView.getWidth();
                            Log.i(TAG, "cameraPreviewWidth: " + cameraPreviewWidth);
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                            Toast.makeText(CameraActivity.this, "Configuration change",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        initializeCharacteristics();
        cameraPreviewResolution = calculateCameraPreviewResolution();
        Log.i(TAG, "Camera preview resolution " + cameraPreviewResolution);
        Log.i(TAG, "Opening camera");
        try {
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CameraActivity.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Camera " + cameraId + " opened");
    }

    private void updatePreview() {
        if (null == cameraDevice) {
            Log.e(TAG, "Error in updating preview");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(),
                    null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(CameraActivity.this,
                        getText(R.string.you_must_grant_permissions), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    protected void onPause() {
        stopBackgroundThread();
        super.onPause();
    }

    private Bitmap getCircledBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private void initializeCharacteristics() {
        try {
            cameraId = getCameraNumber();
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private Size calculateImageResolution() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        CameraCharacteristics characteristics = null;
        Log.i(TAG, "Image ratio " + IMAGE_RATIO);
        try {
            characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Size[] jpegSizes = null;
        if (characteristics != null) {
            jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    .getOutputSizes(ImageFormat.JPEG);
        }
        if (jpegSizes != null && 0 < jpegSizes.length) {
            Size result = Arrays.stream(jpegSizes).filter(size -> (compareRatio(size, IMAGE_RATIO))).
                    findFirst().orElse(jpegSizes[0]);
            Log.i(TAG, "Adjusted image resolution " + result);
            return result;
        } else {
            Log.i(TAG, "Adjusted image resolution " + cameraPreviewResolution);
            return cameraPreviewResolution;
        }
    }

    private Size calculateCameraPreviewResolution() {
        return Arrays.stream(map.getOutputSizes(SurfaceTexture.class))
                .filter(s -> s.getWidth() == FULL_HD_WIDTH && s.getHeight() == FULL_HD_HEIGHT)
                .findFirst().orElse(new Size(FULL_HD_WIDTH, FULL_HD_HEIGHT));
    }

    private boolean compareRatio(Size size, double ratio) {
        return ScreenRatioHelper.compareDouble((size.getWidth() / (double) size.getHeight()), (ratio));
    }

    private void goToChoiceActivity() {
        Intent intent = new Intent(this, ChoiceActivity.class);
        intent.putExtra("URI", capturedImageURI);
        intent.putExtra("className", getClass().getName());
        startActivity(intent);
    }

    public abstract CameraOverlay initializeOverlay();

    public abstract String getCameraNumber() throws CameraAccessException;

    public abstract Bitmap rotateImage(Bitmap originalBitmap);

    public abstract double getCircleScreenRatio();

    public abstract String getFileSuffix();

}
