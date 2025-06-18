package com.example.commercial_monitoring_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PerfilFragment extends Fragment {
    public PerfilFragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);


        TextView vendedorEmail = view.findViewById(R.id.vendedor_email);
        TextView vendedorNome = view.findViewById(R.id.vendedor_nome);

        UserSession session = UserSession.getInstance(getContext());
        String userEmail = session.getUserEmail();
        String userNome = session.getUserName();

        // Definir o email no TextView
        if (userEmail != null) {
            vendedorEmail.setText(userEmail);
        } else {
            vendedorEmail.setText("Email não disponível");
        }
        if(userNome!=null){
            vendedorNome.setText(userNome);
        }else{
            vendedorNome.setText("Nome não disponível");
        }

        return view;
    }
}

