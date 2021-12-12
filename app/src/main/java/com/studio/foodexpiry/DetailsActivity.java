package com.studio.foodexpiry;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import foodexpiry.R;


public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String strName = getIntent().getStringExtra("name").toString();
        String strBreed = getIntent().getStringExtra("type").toString();
        String strVac = getIntent().getStringExtra("expiry").toString();
        String strAge = getIntent().getStringExtra("expected").toString();
        byte[]  image = getIntent().getByteArrayExtra("image");


        ImageView img = findViewById(R.id.imageView);
        TextView name = findViewById(R.id.name);
        TextView breed = findViewById(R.id.breed);
        TextView vaccination = findViewById(R.id.vaccination);
        TextView age = findViewById(R.id.age);


        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        img.setImageBitmap(bitmap);
        name.setText(strName);
        breed.setText(strBreed);
        vaccination.setText(strVac);
        age.setText(strAge);



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
