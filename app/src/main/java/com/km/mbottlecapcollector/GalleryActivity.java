package com.km.mbottlecapcollector;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.km.mbottlecapcollector.api.model.PictureWrapper;
import com.km.mbottlecapcollector.util.ScreenRatioHelper;

import java.util.ArrayList;


public class GalleryActivity extends Activity {

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

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), ScreenRatioHelper.capsInRowAmount);
        recyclerView.setLayoutManager(layoutManager);
        GalleryAdapter adapter = new GalleryAdapter(capList);
        recyclerView.setAdapter(adapter);
    }


}