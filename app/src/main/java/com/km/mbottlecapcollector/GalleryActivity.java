package com.km.mbottlecapcollector;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.km.mbottlecapcollector.api.model.PictureWrapper;

import java.util.ArrayList;


public class GalleryActivity extends AppCompatActivity {

    private ArrayList<PictureWrapper> capList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        RecyclerView recyclerView = findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true);
        capList = getIntent().getParcelableArrayListExtra("caps");
        capList.sort((cap1, cap2) -> (int) (cap1.getId() - cap2.getId()));

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 4);
        recyclerView.setLayoutManager(layoutManager);
        GalleryAdapter adapter = new GalleryAdapter(capList);
        recyclerView.setAdapter(adapter);
    }


}