package com.km.mbottlecapcollector;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.km.mbottlecapcollector.api.model.Cap;
import com.km.mbottlecapcollector.api.model.PictureWrapper;
import com.km.mbottlecapcollector.api.rest.API;
import com.km.mbottlecapcollector.util.ScreenRatioHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private ArrayList<PictureWrapper> galleryList;
    private ProgressDialog progressBar;

    public GalleryAdapter(ArrayList<PictureWrapper> galleryList, ProgressDialog bar) {
        this.galleryList = galleryList;
        this.progressBar = bar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_cell_layout,
                parent, false);
        return new ViewHolder(view, progressBar);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PictureWrapper wrapper = galleryList.get(position);
        Picasso.get().load(wrapper.getUrl(ScreenRatioHelper.getCapInRowWidth())).into(holder.img);
        holder.id = wrapper.getId();
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private long id;

        public ViewHolder(View view, ProgressDialog progressDialog) {
            super(view);
            img = view.findViewById(R.id.galleryImage);
            img.setOnClickListener(view1 -> {
                progressDialog.show();
                API.bottleCaps().cap(id).enqueue(new Callback<Cap>() {
                    @Override
                    public void onResponse(Call<Cap> call, Response<Cap> response) {
                        progressDialog.dismiss();
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            Cap cap = response.body();
                            Intent intent = new Intent(view.getContext().getApplicationContext(), ReadCapActivity.class);
                            intent.putExtra("id", cap.getId());
                            intent.putExtra("url", cap.getFileLocation(ScreenRatioHelper.getStandaloneCapWidth()));
                            intent.putExtra("capName", cap.getCapName());
                            intent.putExtra("googleDriveName", cap.getGoogleDriveID());
                            intent.putExtra("creationDate", cap.getCreationDate());
                            view.getContext().startActivity(intent);
                        } else if (responseCode == 404) {
                            Toast.makeText(view.getContext(), "Cap was not found",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Cap> call, Throwable t) {
                        Toast.makeText(view.getContext(), "Failure", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            });
        }
    }

    public ArrayList<PictureWrapper> getGalleryList() {
        return galleryList;
    }

    public void setGalleryList(ArrayList<PictureWrapper> galleryList) {
        this.galleryList = galleryList;
    }
}
