package com.example.clienterestful;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AlunoAdapter extends BaseAdapter {
    private List<Aluno> alunos;
    private Context context;

    public AlunoAdapter(Context context, List<Aluno> objects) {
        super();
        this.alunos = objects;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    @Override
    public int getCount() {
        return alunos.size();
    }

    @Override
    public Object getItem(int position) {
        return alunos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.aluno_item, parent, false);
        }

        Aluno aluno = this.alunos.get(position);
        TextView nome, email, ra;
        nome = (TextView) convertView.findViewById(R.id.txtNome);
        email = (TextView) convertView.findViewById(R.id.txtEmail);
        ra = (TextView) convertView.findViewById(R.id.txtRA);

        nome.setText(aluno.getNome());
        email.setText(aluno.getEmail());
        ra.setText(aluno.getRA());
        return convertView;
    }

    @Override
    public boolean isEmpty() {
        return (alunos.isEmpty());
    }
}
