package com.km.mbottlecapcollector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;


public class GalleryActivity extends AppCompatActivity {

    private ImageView capImage0;
    private ImageView capImage1;
    private ImageView capImage2;
    private ImageView capImage3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        capImage0 = findViewById(R.id.imageCap0);
        capImage1 = findViewById(R.id.imageCap1);
        capImage2 = findViewById(R.id.imageCap2);
        capImage3 = findViewById(R.id.imageCap3);


        Picasso.get().load("https://lh3.googleusercontent.com/BVw2ZH7k_-YVEW6Cj6AiW2ORtOvBw-0zNTDU6Z56fbNuKdCPMUzltJzxA0kRqQhArJs2X1K1GIlT9t8").into(capImage0);
        Picasso.get().load("https://lh3.googleusercontent.com/wTGnn-QpQG7c_2SoNOqodO6IK7kDaU7yMh7tcPAq7l-B23tNXXeneBAjmo3xHx6Qi6sScFgbFpljim8").into(capImage1);
        Picasso.get().load("https://lh3.googleusercontent.com/TLT_jt22fakTWao_3_mR0O2hnzUP7Uq5jEwA7yrukatEoFpM3oeFbzzDG9_D_-WqO6s8YNg5nUYNwnY").into(capImage2);
        Picasso.get().load("https://lh3.googleusercontent.com/UBXnX001S0UtATmU7p98OxyW4HX0aTgs7q2X1nI43cj2C6w45JAwRostwaTbwaJchnYQJaHMzUrzUUI").into(capImage3);
    }


}