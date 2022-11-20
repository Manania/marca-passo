package com.marcacorrida.historico;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.marcacorrida.R;

public class CorridaViewHolder extends RecyclerView.ViewHolder {
    private TextView txtVwNome;
    private TextView txtVwDuracao;
    private TextView txtVwPassos;
    private TextView txtVwData;

    public CorridaViewHolder(View view) {
        super(view);

        txtVwNome = view.findViewById(R.id.txtVwNome);
        txtVwDuracao = view.findViewById(R.id.txtVwDuracao);
        txtVwPassos = view.findViewById(R.id.txtVwPassos);
        txtVwData = view.findViewById(R.id.txtVwData);
    }

    public TextView getTxtVwNome() {
        return txtVwNome;
    }

    public TextView getTxtVwDuracao() {
        return txtVwDuracao;
    }

    public TextView getTxtVwPassos() {
        return txtVwPassos;
    }

    public TextView getTxtVwData() {
        return txtVwData;
    }
}

