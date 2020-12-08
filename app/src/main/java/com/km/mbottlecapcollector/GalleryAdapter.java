package com.km.mbottlecapcollector;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.km.mbottlecapcollector.api.model.PictureWrapper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private ArrayList<PictureWrapper> galleryList;

    public GalleryAdapter(ArrayList<PictureWrapper> galleryList) {
        this.galleryList = galleryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_cell_layout,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(galleryList.get(position).getUrl()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.galleryImage);
        }
    }
}
