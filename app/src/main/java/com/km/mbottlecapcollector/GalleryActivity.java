package com.km.mbottlecapcollector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.km.mbottlecapcollector.api.model.PictureWrapper;
import com.km.mbottlecapcollector.util.ScreenRatioHelper;
import com.km.mbottlecapcollector.view.GalleryAdapter;

import java.util.ArrayList;


public class GalleryActivity extends Activity {

    private static final String TAG = GalleryActivity.class.getSimpleName();
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PictureWrapper> capList = new ArrayList<>();
    private Button nextButton;
    private Button previousButton;
    private TextView currentPageTextView;
    private TextView lastPageTextView;
    private int numberOfRows;
    private int numberOfItems;
    private int currentStartNumber = 0;
    private int currentLastNumber = 0;
    private int itemsOnPage = 0;
    private int numberOfPages = 0;
    private int currentPage = 0;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        RecyclerView recyclerView = findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true);
        currentPageTextView = findViewById(R.id.textViewCurrentPage);
        currentPageTextView.setText(String.valueOf(currentPage + 1));
        lastPageTextView = findViewById(R.id.textViewLastPage);
        capList = getIntent().getParcelableArrayListExtra("caps");
        capList.sort((cap1, cap2) -> (int) (cap1.getId() - cap2.getId()));

        numberOfRows = ScreenRatioHelper.getNumberOfRows();
        numberOfItems = capList.size();
        currentLastNumber = numberOfRows * ScreenRatioHelper.CAP_IN_ROW_AMOUNT;
        itemsOnPage = currentLastNumber;
        numberOfPages = (int) Math.ceil(numberOfItems / (double) itemsOnPage);
        lastPageTextView.setText(String.valueOf(numberOfPages));

        Log.i(TAG, "Number of rows :" + numberOfRows);
        Log.i(TAG, "Number of pages :" + numberOfPages);
        Log.i(TAG, "Number of items on page :" + itemsOnPage);
        Log.i(TAG, "Number of items :" + numberOfItems);
        layoutManager = new GridLayoutManager(getApplicationContext(), ScreenRatioHelper.CAP_IN_ROW_AMOUNT);
        recyclerView.setLayoutManager(layoutManager);

        progressBar = new ProgressDialog(this);
        progressBar.setTitle(R.string.loading);
        progressBar.setMessage(getString(R.string.loading_cap));
        progressBar.setCancelable(false);
        progressBar.dismiss();

        GalleryAdapter adapter = new GalleryAdapter(new ArrayList<>(capList.subList(currentStartNumber, currentLastNumber)), progressBar);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        nextButton = findViewById(R.id.buttonNext);
        nextButton.setOnClickListener(view -> {
                    adapter.setGalleryList(getNextItems());
                    adapter.notifyDataSetChanged();
                }
        );
        previousButton = findViewById(R.id.buttonPrevious);
        previousButton.setOnClickListener(view -> {
            adapter.setGalleryList(getPreviousItems());
            adapter.notifyDataSetChanged();
        });

    }

    private ArrayList<PictureWrapper> getNextItems() {
        if (currentLastNumber != numberOfItems) {
            if (currentLastNumber + itemsOnPage > numberOfItems) {
                currentLastNumber = numberOfItems;
                currentStartNumber += (itemsOnPage);
            } else {
                currentStartNumber += itemsOnPage;
                currentLastNumber += itemsOnPage;
            }
            incrementCurrentPage(true);
        }
        return new ArrayList<>(capList.subList(currentStartNumber, currentLastNumber));
    }

    private ArrayList<PictureWrapper> getPreviousItems() {
        if (currentStartNumber != 0) {
            currentStartNumber -= itemsOnPage;
            currentLastNumber = currentStartNumber + itemsOnPage;
            incrementCurrentPage(false);
        }
        return new ArrayList<>(capList.subList(currentStartNumber, currentLastNumber));
    }

    private void incrementCurrentPage(boolean increment) {
        if (increment) {
            currentPage++;
        } else {
            currentPage--;
        }
        currentPageTextView.setText(String.valueOf(currentPage + 1));
    }
}