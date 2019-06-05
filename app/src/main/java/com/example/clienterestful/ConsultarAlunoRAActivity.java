package com.example.clienterestful;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConsultarAlunoRAActivity extends AppCompatActivity {
    TextView txtNome, txtRA, txtEmail;
    Button btnConsultar;
    EditText edtRA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_aluno_ra);

        btnConsultar = (Button) findViewById(R.id.btnConsultarRA);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtRA = (TextView) findViewById(R.id.txtRA);
        txtNome = (TextView) findViewById(R.id.txtNome);
        edtRA = (EditText) findViewById(R.id.edtRA);

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtRA.getText().toString().isEmpty()) {
                    Toast.makeText(ConsultarAlunoRAActivity.this, "Insira um RA", Toast.LENGTH_SHORT).show();
                }
                else {
                    GetAlunoByRATask getAlunoByRATask = new GetAlunoByRATask();
                    getAlunoByRATask.execute();
                }
            }
        });
    }

    public class GetAlunoByRATask extends AsyncTask<Void, Void, Aluno> {
        String URL_CONSULTAR_TODOS_ALUNOS;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Aluno aluno) {
            if (aluno != null) {
                txtEmail.setText(aluno.getEmail());
                txtNome.setText(aluno.getNome());
                txtRA.setText(aluno.getRA());
                Toast.makeText(ConsultarAlunoRAActivity.this, "Consulta feita com sucesso!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(ConsultarAlunoRAActivity.this, "Erro na consulta de aluno", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Aluno doInBackground(Void... params) {
            URL_CONSULTAR_TODOS_ALUNOS = "http://" + MainActivity.IP + ":8080/Projeto_Restful_AD/webresources/aluno/getAlunoRA/"
            + edtRA.getText().toString();

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(URL_CONSULTAR_TODOS_ALUNOS);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                Gson gson = new Gson();
                return gson.fromJson(reader, Aluno.class);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("ERROR", e.getMessage());
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
