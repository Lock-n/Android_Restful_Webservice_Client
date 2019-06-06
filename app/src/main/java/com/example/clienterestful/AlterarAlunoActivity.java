package com.example.clienterestful;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class AlterarAlunoActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private Aluno aluno_to_be_updated;
    Button btnAlterar, btnSelecionar;
    EditText edtNome, edtEmail;
    TextView txtRA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_aluno);

        btnAlterar = (Button) findViewById(R.id.btnAlterar);
        btnSelecionar = (Button) findViewById(R.id.btnSelecionar);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtNome = (EditText) findViewById(R.id.edtNome);
        txtRA = (TextView) findViewById(R.id.txtRA);

        btnSelecionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlterarAlunoActivity.this, SelecionarAlunoActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aluno_to_be_updated == null) {
                    Toast.makeText(AlterarAlunoActivity.this, "Selecione um aluno antes", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (edtEmail.getText().toString().isEmpty() || edtNome.getText().toString().isEmpty()) {
                    Toast.makeText(AlterarAlunoActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                UpdateAlunoTask updateAlunoTask = new UpdateAlunoTask();
                updateAlunoTask.execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (data != null) {
                aluno_to_be_updated = (Aluno)data.getSerializableExtra("ALUNO");
                edtNome.setText(aluno_to_be_updated.getNome());
                edtEmail.setText(aluno_to_be_updated.getEmail());
                Log.d("Debug", "aluno_to_be_updated: " + aluno_to_be_updated.toString());

                txtRA.setText(aluno_to_be_updated.getRA());
            }
        }
    }

    public class UpdateAlunoTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(AlterarAlunoActivity.this, "Aluno alterado com sucesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(AlterarAlunoActivity.this, "Erro na alteração do aluno", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String URL_ALTERAR_TODOS_ALUNOS = "http://" + MainActivity.IP + ":8080/Projeto_Restful_AD/webresources/aluno/putAluno";

            HttpURLConnection connection = null;
            //BufferedReader reader = null;

            try {
                URL url = new URL(URL_ALTERAR_TODOS_ALUNOS);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.addRequestProperty("Content-Type", "application/json");
                OutputStream out = null;

                out = new BufferedOutputStream(connection.getOutputStream());

                Gson gson = new Gson();
                aluno_to_be_updated.setEmail(edtEmail.getText().toString());
                aluno_to_be_updated.setNome(edtNome.getText().toString());
                //String aluno_json = gson.toJson(aluno_a_ser_deletado, Aluno.class);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(gson.toJson(aluno_to_be_updated, Aluno.class));
                writer.flush();
                writer.close();
                out.close();

                connection.connect();

                int responseCode = connection.getResponseCode();

                return (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT);

            } catch (MalformedURLException e) {
                Log.e("ERROR", e.getMessage());
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
