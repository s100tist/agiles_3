package com.example.agiles_3;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;
import java.net.URL;
import java.util.zip.Inflater;

public class guardarReceta extends Activity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.save_recipe);
        Recipe selectedObject = (Recipe) getIntent().getSerializableExtra("recipe");
        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView caloriesTextView = findViewById(R.id.caloriesTextView);
        ImageView imageView = findViewById(R.id.imageView);
        TextView ingredientsTextView = findViewById(R.id.ingredientsTextView);


        nameTextView.setText(selectedObject.getName());
        caloriesTextView.setText("Calorias: "+selectedObject.getCalories());
        ingredientsTextView.setText(TextUtils.join("\n", selectedObject.getIngredientLines()));
        new RecipeAdapter.DownloadImageTask(imageView).execute(selectedObject.getImageUrl());





        Log.d(TAG, String.valueOf(selectedObject.getName()));
        super.onCreate(savedInstanceState);
        Button cancelButton = findViewById(R.id.cancelButton);
        Button saveButton = findViewById(R.id.saveButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Guardado");
                //Aqui va el codigo de guardar
            }
        });
    }



    void DownloadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }
}
