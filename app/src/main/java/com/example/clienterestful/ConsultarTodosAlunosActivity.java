package com.example.clienterestful;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import com.google.gson.Gson;

public class ConsultarTodosAlunosActivity extends AppCompatActivity {
    Button btnConsultar;
    ListView listView;
    AlunoAdapter alunoAdapter;
    public static final String URL_CONSULTAR_TODOS_ALUNOS = "http://127.0.0.1:8080/Projeto_Restful_AD/webresources/aluno/getAllAlunos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_todos_alunos);

        btnConsultar = this.findViewById(R.id.btnConsultarTA);
        listView = this.findViewById(R.id.lstAlunos);

        if (alunoAdapter == null) {
            alunoAdapter = new AlunoAdapter(this.getBaseContext(), new LinkedList<Aluno>());
        }

        listView.setAdapter(alunoAdapter);

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAlunosTask getAlunosTask = new GetAlunosTask();
                getAlunosTask.execute();
            }
        });
    }

    private class GetAlunosTask extends AsyncTask<Void, Void, Aluno[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Aluno[] alunos) {
            if (alunos != null) {
                LinkedList<Aluno> l = new LinkedList<>();

                for (Aluno a : alunos) {
                    l.add(a);
                }
                alunoAdapter.setAlunos(l);
                alunoAdapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(ConsultarTodosAlunosActivity.this, "Erro na busca de alunos", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Aluno[] doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(URL_CONSULTAR_TODOS_ALUNOS);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                /*while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);

                }*/

                Gson gson = new Gson();
                Aluno[] alunos = gson.fromJson(reader, Aluno[].class);
                return alunos;


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
