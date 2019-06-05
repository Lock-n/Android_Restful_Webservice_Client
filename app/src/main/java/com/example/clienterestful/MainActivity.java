package com.example.clienterestful;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final String IP = "177.220.18.78";
    Button btnConsultarTodosAlunos, btnConsultarAlunoRA, btnIncluirAluno, btnDeletarAluno, btnAlterarAluno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConsultarAlunoRA = findViewById(R.id.btnConsultarAlunoPorRA);
        btnIncluirAluno = findViewById(R.id.btnIncluirAluno);
        btnDeletarAluno = findViewById(R.id.btnDeletarAluno);
        btnConsultarTodosAlunos = findViewById(R.id.btnConsultarAlunos);
        btnAlterarAluno = findViewById(R.id.btnAlterarAluno);

        btnIncluirAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IncluirAlunoActivity.class);
                startActivity(intent);
            }
        });

        btnConsultarAlunoRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConsultarAlunoRAActivity.class);
                startActivity(intent);
            }
        });

        btnConsultarTodosAlunos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConsultarTodosAlunosActivity.class);
                startActivity(intent);
            }
        });

        btnDeletarAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeletarAlunoActivity.class);
                startActivity(intent);
            }
        });

        btnAlterarAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlterarAlunoActivity.class);
                startActivity(intent);
            }
        });
    }
}
