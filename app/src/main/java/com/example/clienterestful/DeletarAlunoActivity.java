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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DeletarAlunoActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    /*private static final String EMAIL = "EMAIL";
    private static final String RA = "RA";
    private static final String NOME = "NOME";*/
    private Aluno aluno_a_ser_deletado;
    Button btnDeletar, btnSelecionar;
    RadioGroup rgpSelecionarDeletar;
    EditText edtRA;
    TextView txtRA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletar_aluno);

        btnDeletar = findViewById(R.id.btnDeletar);
        btnSelecionar = findViewById(R.id.btnSelecionarAlunoD);
        rgpSelecionarDeletar = (RadioGroup) findViewById(R.id.rdgrpSelecionarDigitar);
        edtRA = (EditText) findViewById(R.id.edtRA);
        txtRA = (TextView) findViewById(R.id.txtRA);

        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (rgpSelecionarDeletar.getCheckedRadioButtonId()) {
                    case R.id.rdnDigitar:
                        if (edtRA.getText().toString().isEmpty()) {
                            Toast.makeText(DeletarAlunoActivity.this, "Insira um RA", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            DeleteAlunoTask deleteAlunoTask = new DeleteAlunoTask();
                            deleteAlunoTask.execute(edtRA.getText().toString());
                        }
                        break;

                    case R.id.rdnSelecionar:
                        if (aluno_a_ser_deletado == null) {
                            Toast.makeText(DeletarAlunoActivity.this, "Selecione um aluno antes", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            DeleteAlunoTask deleteAlunoTask = new DeleteAlunoTask();
                            deleteAlunoTask.execute(aluno_a_ser_deletado.getRA());
                        }
                        break;
                    case -1:
                        Toast.makeText(DeletarAlunoActivity.this, "Selecione uma opção", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

        btnSelecionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeletarAlunoActivity.this, SelecionarAlunoActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            /*String nome = data.getStringExtra(NOME);
            String ra = data.getStringExtra(RA);
            String email = data.getStringExtra(EMAIL);*/
            if (data != null) {
                aluno_a_ser_deletado = (Aluno)data.getSerializableExtra("ALUNO");//new Aluno(ra, nome, email);;
                Log.d("Debug", "aluno_a_ser_deletado: " + aluno_a_ser_deletado.toString());

                txtRA.setText(aluno_a_ser_deletado.getRA());
            }
        }
    }

    public class DeleteAlunoTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(DeletarAlunoActivity.this, "Aluno deletado com sucesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(DeletarAlunoActivity.this, "Erro na deleção do aluno", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String URL_DELETAR_TODOS_ALUNOS = "http://" + MainActivity.IP + ":8080/Projeto_Restful_AD/webresources/aluno/deleteAluno";

            HttpURLConnection connection = null;
            //BufferedReader reader = null;

            try {
                URL url = new URL(URL_DELETAR_TODOS_ALUNOS);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.addRequestProperty("Content-Type", "application/json");
                OutputStream out = null;

                out = new BufferedOutputStream(connection.getOutputStream());

                //Gson gson = new Gson();
                //String aluno_json = gson.toJson(aluno_a_ser_deletado, Aluno.class);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(params[0]);
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
