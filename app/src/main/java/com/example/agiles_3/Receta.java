package com.example.agiles_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

    private static final String API_URL = "https://test-es.edamam.com/api/nutrition-data?app_id=8af55b45&app_key=130850566c8c9ac00a4c406797e0378a";
    private static final String API_APP_ID = "8af55b45";
    private static final String API_APP_KEY = "130850566c8c9ac00a4c406797e0378a";
    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private Button btnAgregar, btnEliminar;
    private List<String> editTextDataList;

    private List<String> getAllEditTextValues() {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null) {
                String editTextData = viewHolder.editText.getText().toString();
                values.add(editTextData);
            }
        }
        return values;
    }

    private EditText titulo;
    private EditText descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            Log.d(LOG_TAG, "Eliminado: " + editTextDataList);
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
                        // User clicked the OK button.
                        Toast.makeText(getApplicationContext(), "Receta guardada correctamente.",
                                Toast.LENGTH_SHORT).show();

                        editTextDataList = getAllEditTextValues();
                        String textoTitulo = titulo.getText().toString();
                        String textoDescripcion = descripcion.getText().toString();

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("title", textoTitulo);
                            jsonObject.put("summary", textoDescripcion);
                            jsonObject.put("ingr", editTextDataList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Imprimir el JSON
                        Log.d("JSON", jsonObject.toString());

                        // Enviar la solicitud HTTP para agregar la receta a la API de Edamam
                        try {
                            OkHttpClient client = new OkHttpClient();

                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

                            Request request = new Request.Builder()
                                    .url(API_URL)
                                    .addHeader("Content-Type", "application/json")
                                    .post(requestBody)
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                    // Manejar la falla de la solicitud
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        // La solicitud fue exitosa, puedes procesar la respuesta aquí
                                        String responseBody = response.body().string();
                                        Log.d("API Response", responseBody);
                                        // Procesa la respuesta según sea necesario
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