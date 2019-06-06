package com.example.clienterestful;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;

public class ConsultarTodosAlunosActivity extends AppCompatActivity {
    Button btnConsultar;
    ListView listView;
    AlunoAdapter alunoAdapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_todos_alunos);


        btnConsultar = this.findViewById(R.id.btnConsultarTA);
        listView = this.findViewById(R.id.lstAlunos);
        progressBar = (ProgressBar) findViewById(R.id.pbTAlunos);

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

    public class GetAlunosTask extends AsyncTask<Void, Void, Aluno[]> {
        String URL_CONSULTAR_TODOS_ALUNOS;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(Aluno[] alunos) {
            if (alunos != null) {
                alunoAdapter.setAlunos(Arrays.asList(alunos));
                alunoAdapter.notifyDataSetChanged();
                listView.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(ConsultarTodosAlunosActivity.this, "Erro na busca de alunos", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Aluno[] doInBackground(Void... params) {
            /*String ip = "";
            try(final DatagramSocket socket = new DatagramSocket()){
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                ip = socket.getLocalAddress().getHostAddress();
            } catch (UnknownHostException | SocketException e) {
                e.printStackTrace();
                Toast.makeText(ConsultarTodosAlunosActivity.this, "Erro ao recuperar ip do celular", Toast.LENGTH_LONG).show();
                ConsultarTodosAlunosActivity.this.finish();
            }*/

            /*try {
                Enumeration e = NetworkInterface.getNetworkInterfaces();
                while(e.hasMoreElements())
                {
                    NetworkInterface n = (NetworkInterface) e.nextElement();
                    Enumeration ee = n.getInetAddresses();
                    while (ee.hasMoreElements())
                    {
                        ip = ((InetAddress) ee.nextElement()).getHostAddress();
                        if (ip.startsWith("177")) {
                            break;
                        }
                    }
                    if (ip.startsWith("177"))
                        break;
                }
            } catch (SocketException e) {
                e.printStackTrace();
                Toast.makeText(ConsultarTodosAlunosActivity.this, "Erro ao recuperar ip do celular", Toast.LENGTH_LONG).show();
                ConsultarTodosAlunosActivity.this.finish();
            }*/

            URL_CONSULTAR_TODOS_ALUNOS = "http://" + MainActivity.IP + ":8080/Projeto_Restful_AD/webresources/aluno/getAllAlunos";

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
