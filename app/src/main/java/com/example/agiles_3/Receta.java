package com.example.agiles_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Receta extends AppCompatActivity {
    String apiUrl = "https://api.edamam.com/api/nutrition-details?app_id=5d40c77a&app_key=b083f3c286a2f50bc8ea7808c6c676e5";

    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private Button btnAgregar, btnEliminar;
    private List<String> editTextDataList;
    private JSONArray listaIngredientes;
    JSONObject jsonResponse;

    private JSONArray getAllEditTextValues() {
        JSONArray values = new JSONArray();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null) {
                String editTextData = viewHolder.editText.getText().toString();
                values.put(editTextData);
            }
        }
        return values;
    }

    private EditText titulo;
    private EditText descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ReceipeViewModel mReceipeViewModel;
        mReceipeViewModel = new ViewModelProvider(this).get(ReceipeViewModel.class);
        mReceipeViewModel.getAllReceipes().observe(this, receipes -> {
            Log.d("ajiji",receipes.toString());
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receta);

        editTextDataList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerview);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAgregar = findViewById(R.id.agregarIngrediente);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.agregarEditText();
            }
        });

        btnEliminar = findViewById(R.id.eliminarIngrediente);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.eliminarEditText();
            }
        });

        titulo = findViewById(R.id.editTextTextPersonName);
        descripcion = findViewById(R.id.editTextTextPersonName2);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        public void agregarEditText() {
            editTextDataList.add("");
            notifyItemInserted(editTextDataList.size() - 1);
            recyclerView.scrollToPosition(editTextDataList.size() - 1);
        }

        public void eliminarEditText() {
            int position = editTextDataList.size() - 1;
            // si quita la posición pero truena
            if (position >= 0 && position < editTextDataList.size()) {
                editTextDataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, editTextDataList.size());
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_ingredientes, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String editTextData = editTextDataList.get(position);
            holder.editText.setText(editTextData);
            holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        int position = holder.getAdapterPosition();
                        String editTextData = holder.editText.getText().toString();
                        editTextDataList.set(position, editTextData);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return editTextDataList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            EditText editText;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                editText = itemView.findViewById(R.id.editText);
            }
        }
    }

    public void agregarReceta(View view) {

        AlertDialog.Builder myAlertBuilder = new
                AlertDialog.Builder(Receta.this);

        myAlertBuilder.setTitle("Guardar receta");
        myAlertBuilder.setMessage("Presiona 'SI' para confirmar:");

        myAlertBuilder.setPositiveButton("SI", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        listaIngredientes = getAllEditTextValues();
                        String textoTitulo = titulo.getText().toString();
                        String textoDescripcion = descripcion.getText().toString();

                        JSONObject jsonObject = new JSONObject();
                        try {
//                            jsonObject.put("title", textoTitulo);
//                            jsonObject.put("summary", textoDescripcion);
                            jsonObject.put("ingr", listaIngredientes);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Enviar la solicitud HTTP para agregar la receta a la API de Edamam
                        try {
                            OkHttpClient client = new OkHttpClient();

                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

                            Request request = new Request.Builder()
                                    .url(apiUrl)
                                    .addHeader("Content-Type", "application/json")
                                    .post(requestBody)
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

                                    if (response.isSuccessful()) {
                                        // La solicitud fue exitosa, puedes procesar la respuesta aquí
                                        String responseBody = response.body().string();
                                        try {
                                            jsonResponse = new JSONObject(responseBody);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                        Log.d("API Response", responseBody);

                                        // Crear un intent para pasar a la siguiente actividad
                                        Intent intent = new Intent(Receta.this, MainActivity.class);
                                        intent.putExtra("jsonObject", jsonResponse.toString());
                                        startActivity(intent);

                                    } else {
                                        // La solicitud no fue exitosa, maneja el error aquí
                                        Log.d("API Error", "Código de error: " + response.code() + ", Mensaje: " + response.message());
                                    }
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            // Manejar cualquier excepción que pueda ocurrir durante la solicitud HTTP
                        }

                        // User clicked the OK button
                        Toast.makeText(getApplicationContext(), "Receta guardada correctamente.",
                                Toast.LENGTH_SHORT).show();

                    }
                });
        myAlertBuilder.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User cancelled the dialog.
                    }
                });

        myAlertBuilder.show();

    }

    public void cancelarReceta(View view) {

        AlertDialog.Builder myAlertBuilder = new
                AlertDialog.Builder(Receta.this);

        myAlertBuilder.setTitle("Cancelar receta");
        myAlertBuilder.setMessage("Presiona 'SI' para confirmar:");

        myAlertBuilder.setPositiveButton("SI", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked the OK button.
                        Toast.makeText(getApplicationContext(), "Cancelación correcta.",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Receta.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
        myAlertBuilder.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User cancelled the dialog.
                    }
                });

        myAlertBuilder.show();

    }
}