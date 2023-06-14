package com.example.agiles_3;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String API_URL = "https://api.edamam.com/search";
    private static final String API_APP_ID = "8af55b45";
    private static final String API_APP_KEY = "130850566c8c9ac00a4c406797e0378a";

    private ListView listView;
    private SearchView searchView;
    private RecipeAdapter adapter;
    private List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        searchView = findViewById(R.id.searchView);
        recipes = new ArrayList<>();
        adapter = new RecipeAdapter(this, recipes);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtén el dato seleccionado
                Recipe selectedData = (Recipe) parent.getItemAtPosition(position);
                //Log.d(TAG, String.valueOf(selectedData.getName()));

                // Muestra la información del dato seleccionado en una nueva actividad o en un diálogo, como desees
                Context context = view.getContext();
                Intent intent = new Intent(context, guardarReceta.class);
                intent.putExtra("recipe", selectedData);
                startActivity(intent);
            }
        });
        
        
        

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    searchRecipes(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // JSON de la receta con toda la información
        String jsonString = getIntent().getStringExtra("jsonObject");
        if (jsonString != null) {
            try {
                //jsonObject es el JSON con la información de los ingredientes
                JSONObject jsonObject = new JSONObject(jsonString);
                // Resto del código para trabajar con el objeto JSON
                Log.d("JSON recibido", String.valueOf(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("JSON", "JSON nulo");
        }
    }

    private void searchRecipes(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String urlStr = API_URL + "?q=" + encodedQuery + "&app_id=" + API_APP_ID + "&app_key=" + API_APP_KEY;

            RecipeSearchTask task = new RecipeSearchTask();
            task.execute(urlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class RecipeSearchTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection connection = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    response = convertInputStreamToString(inputStream);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            parseResponse(response);
        }
    }

    private String convertInputStreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private void parseResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray hitsArray = jsonObject.getJSONArray("hits");

            recipes.clear();
            for (int i = 0; i < hitsArray.length(); i++) {
                JSONObject hitObject = hitsArray.getJSONObject(i);
                JSONObject recipeObject = hitObject.getJSONObject("recipe");
                String recipeName = recipeObject.getString("label");
                String recipeImage = recipeObject.getString("image");
                String recipesCalories = recipeObject.getString("calories");
                JSONArray ingredientLinesArray = recipeObject.getJSONArray("ingredientLines");
                List<String> ingredientLines = new ArrayList<>();
                for (int j = 0; j < ingredientLinesArray.length(); j++) {
                    ingredientLines.add(ingredientLinesArray.getString(j));
                }


                Recipe recipe = new Recipe(recipeName, recipeImage, ingredientLines, recipesCalories);
                recipes.add(recipe);
            }

            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void crearReceta(View view) {
// Se manda a la pantalla para agregar ingredientes
        Intent intent = new Intent(this, Receta.class);
        startActivity(intent);
    }


}

class Recipe implements Serializable{
    private String name;
    private String imageUrl;
    private List<String> ingredientLines;
    private String calories;

    private String carbs;

    Recipe(String name, String imageUrl, List<String> ingredientLines, String calories) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.ingredientLines = ingredientLines;
        this.calories = calories;
    }

    String getName() {
        return name;
    }

    String getImageUrl() {
        return imageUrl;
    }

    List<String> getIngredientLines() {
        return ingredientLines;
    }

    String getCalories(){return  calories;}
}

class RecipeAdapter extends ArrayAdapter<Recipe> {
    private LayoutInflater inflater;

    RecipeAdapter(MainActivity context, List<Recipe> recipes) {
        super(context, 0, recipes);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.item_recipe, parent, false);
        }

        Recipe recipe = getItem(position);
        if (recipe != null) {
            TextView nameTextView = itemView.findViewById(R.id.nameTextView);
            ImageView imageView = itemView.findViewById(R.id.imageView);
            TextView caloriesTextView = itemView.findViewById(R.id.caloriesTextView);
           // TextView ingredientsTextView = itemView.findViewById(R.id.ingredientsTextView);
            //aki van las kalorias
            nameTextView.setText(recipe.getName());
            caloriesTextView.setText("Calorías: " + recipe.getCalories());
            //ingredientsTextView.setText(TextUtils.join("\n", recipe.getIngredientLines()));

            if (!TextUtils.isEmpty(recipe.getImageUrl())) {
                new DownloadImageTask(imageView).execute(recipe.getImageUrl());
            }
        }

        return itemView;
    }




    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(imageUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }
}