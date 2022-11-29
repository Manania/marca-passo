package com.marcacorrida.historico;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.marcacorrida.R;
import com.marcacorrida.datasource.CorridaRepository;

import java.io.IOException;

public class HistoricoFragment extends Fragment {
    private RecyclerView recyclerView;
    public HistoricoFragment() {
    }

    public static HistoricoFragment newInstance() {
        HistoricoFragment fragment = new HistoricoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_historico, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recylcler_view_historico);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        try(CorridaRepository repo = new CorridaRepository(getContext())) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new CorridaAdapter(repo.listar(false)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}