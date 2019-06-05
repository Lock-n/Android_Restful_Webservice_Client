package com.example.clienterestful;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class IncluirAlunoActivity extends AppCompatActivity {
    Button btnIncluir;
    EditText edtNome, edtRA, edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incluir_aluno);

        btnIncluir = (Button) findViewById(R.id.btnIncluir);
        edtNome = (EditText) findViewById(R.id.edtNome);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtRA = (EditText) findViewById(R.id.edtRA);

        btnIncluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNome.getText().toString().isEmpty() || edtEmail.getText().toString().isEmpty() || edtRA.getText().toString().isEmpty()) {
                    Toast.makeText(IncluirAlunoActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
                else {
                    IncluirAlunoTask incluirAlunoTask = new IncluirAlunoTask();
                    incluirAlunoTask.execute();
                }
            }
        });
    }

    public class IncluirAlunoTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(IncluirAlunoActivity.this, "Aluno incluído com sucesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(IncluirAlunoActivity.this, "Erro na inclusão do aluno, talvez este RA já esteja incluído?", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String URL_INCLUIR_ALUNO = "http://" + MainActivity.IP + ":8080/Projeto_Restful_AD/webresources/aluno/includeAluno";

            HttpURLConnection connection = null;
            //BufferedReader reader = null;

            try {
                URL url = new URL(URL_INCLUIR_ALUNO);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.addRequestProperty("Content-Type", "application/json");
                OutputStream out = null;

                out = new BufferedOutputStream(connection.getOutputStream());

                Gson gson = new Gson();
                String aluno_json = gson.toJson(new Aluno(edtRA.getText().toString(),
                        edtNome.getText().toString(), edtEmail.getText().toString()), Aluno.class);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(aluno_json);
                writer.flush();
                writer.close();
                out.close();

                connection.connect();

                int responseCode = connection.getResponseCode();

                return (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("ERROR", e.getMessage());
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return false;
        }
    }
}
