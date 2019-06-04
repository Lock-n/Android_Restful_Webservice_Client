package com.example.clienterestful;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnConsultarTodosAlunos, btnConsultarAlunoRA, btnIncluirAluno, btnDeletarAluno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConsultarAlunoRA = findViewById(R.id.btnConsultarAlunoPorRA);
        btnIncluirAluno = findViewById(R.id.btnIncluirAluno);
        btnDeletarAluno = findViewById(R.id.btnDeletarAluno);
        btnConsultarTodosAlunos = findViewById(R.id.btnConsultarAlunos);

        btnConsultarTodosAlunos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConsultarTodosAlunosActivity.class);
                startActivity(intent);
            }
        });
    }
}
