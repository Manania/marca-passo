package com.marcacorrida.historico;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.marcacorrida.R;
import com.marcacorrida.model.Corrida;

public class CorridaAdapter extends RecyclerView.Adapter<CorridaAdapter.CorridaViewHolder> {
    private List<Corrida> dataSet;
    private DateFormat DDMMYY_FORMATER = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());

    public CorridaAdapter(List<Corrida> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public CorridaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.corrida_row_item, parent, false);
        return new CorridaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CorridaViewHolder holder, int position) {
        if(position > 0 && getItemCount() > 1) {
            Corrida item = dataSet.get(position -1);
            holder.getTxtVwNome().setText( item.getNome() );
            holder.getTxtVwPassos().setText( String.valueOf( item.getNumPassos() ) );
            holder.getTxtVwDuracao().setText( String.valueOf( item.getDuracao() /  1000f ) );
            holder.getTxtVwData().setText(DDMMYY_FORMATER.format(item.getData()));
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size() + 1;
    }

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

}














