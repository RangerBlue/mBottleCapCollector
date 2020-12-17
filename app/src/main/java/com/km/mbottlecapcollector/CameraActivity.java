package com.km.mbottlecapcollector;

import android.Manifest;
import android.app.Activity;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.km.mbottlecapcollector.util.ScreenRatioHelper.FULL_HD_HEIGHT;
import static com.km.mbottlecapcollector.util.ScreenRatioHelper.FULL_HD_WIDTH;
import static com.km.mbottlecapcollector.util.ScreenRatioHelper.IMAGE_RATIO;

public class CameraActivity extends Activity {

    private static final String TAG = "CameraActivity";
    private Button takePictureButton;
    private AdjustedTextureView textureView;
    private RelativeLayout rootLayout;
    private SquareOverlay screenSquare;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size cameraPreviewResolution;
    private Size imageResolution;
    private ImageReader imageReader;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private int deviceWidth;
    private int cameraPreviewWidth;
    private String capturedImageURI;
    /**
     * Fixed values to ensure that preview don't have too high resolution which may cause
     * slow performance
     */

    private double cameraImageRatio = 1;
    private CameraManager manager;
    private CameraCharacteristics characteristics;
    private StreamConfigurationMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        textureView = (AdjustedTextureView) findViewById(R.id.texture);
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        takePictureButton = (Button) findViewById(R.id.btn_takepicture);
        assert takePictureButton != null;
        deviceWidth = ScreenRatioHelper.getScreenWidth();
        Log.i(TAG, "Device Width: " + deviceWidth);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        screenSquare = new SquareOverlay(getApplicationContext(), deviceWidth);
        rootLayout.addView(screenSquare);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        cameraPreviewWidth = textureView.getWidth();
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
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
    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            createCameraPreview();
        }
    };

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void takePicture() {
        if (null == cameraDevice) {
            Log.i(TAG, "There is no camera available");
            return;
        }
        try {
            ImageReader reader = ImageReader.newInstance(imageResolution.getWidth(), imageResolution.getHeight(), ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            final File fileCircle = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + "circle.jpg");
            cameraImageRatio = imageResolution.getHeight() / (double) cameraPreviewWidth;
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        Bitmap originalBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
                        if (ScreenRatioHelper.isDeviceSMA50()) {
                            Matrix matrix = new Matrix();
                            matrix.postRotate(90);
                            originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0,
                                    originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
                        }
                        Bitmap squareBitmap = Bitmap.createBitmap(originalBitmap,
                                (int) (cameraImageRatio * deviceWidth * (1 - screenSquare.getRatio()) / 2),
                                (int) (cameraImageRatio * deviceWidth * (1 - screenSquare.getRatio()) / 2),
                                (int) ((deviceWidth * screenSquare.getRatio() * cameraImageRatio)),
                                (int) ((deviceWidth * screenSquare.getRatio() * cameraImageRatio)));
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
                    } finally {
                        if (image != null) {
                            image.close();
                        }
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
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    goToChoiceActivity();
                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
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

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(cameraPreviewResolution.getWidth(), cameraPreviewResolution.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                    cameraPreviewWidth = textureView.getWidth();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(CameraActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Camera " + cameraId + " opened");
    }

    protected void updatePreview() {
        if (null == cameraDevice) {
            Log.e(TAG, "Error in updating preview");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(CameraActivity.this, "Sorry!!!, you can't use this app without granting permission",
                        Toast.LENGTH_LONG).show();
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

    public static Bitmap getCircledBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private void goToChoiceActivity() {
        Intent intent = new Intent(this, ChoiceActivity.class);
        intent.putExtra("URI", capturedImageURI);
        Log.i(TAG, "Caputured image URI " + capturedImageURI);
        startActivity(intent);
    }

    private void initializeCharacteristics() {
        try {
            cameraId = manager.getCameraIdList()[0];
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
            jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
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
}
