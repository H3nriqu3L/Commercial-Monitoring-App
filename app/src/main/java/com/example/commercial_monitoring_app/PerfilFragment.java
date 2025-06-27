package com.example.commercial_monitoring_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.commercial_monitoring_app.model.Oportunidade;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerfilFragment extends Fragment {

    private static final int PICK_IMAGE = 100;
    private ImageView profileImage;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserProfile";
    private static final String PROFILE_IMAGE_KEY = "profile_image";
    private TextView tvDesempenhoPercentage,  vendedorEmail, vendedorNome;
    TextView tvRankingPosition;
    TextView tvRankingText;
    TextView tvRankingMotivational;
    UserSession session;
    String userEmail;
    String userNome;

    public PerfilFragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Inicializar SharedPreferences e dados usuario
        sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        initViews(view);

        loadUserData();
        loadDesempenho();
        loadRanking(view);

        loadSavedProfileImage();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        loadDesempenho();

        return view;
    }

    private void initViews(View view){
        profileImage = view.findViewById(R.id.profile_image);
        vendedorEmail = view.findViewById(R.id.vendedor_email);
        vendedorNome = view.findViewById(R.id.vendedor_nome);
        tvDesempenhoPercentage = view.findViewById(R.id.tvDesempenhoPercentage);

        tvRankingPosition = view.findViewById(R.id.ranking_position);
        tvRankingText = view.findViewById(R.id.ranking_text);
        tvRankingMotivational = view.findViewById(R.id.ranking_motivational);
    }


    private void loadDesempenho(){
        int ganhas = MyApp.getCountOportunidadesGanhas();
        int perdidas = MyApp.getCountOportunidadesPerdidas();

        int totalOportunidades = ganhas + perdidas;


        if (totalOportunidades > 0) {

            double porcentagem = ((double) ganhas / totalOportunidades) * 100;

            String porcentagemFormatada = String.format("%.1f%%", porcentagem);

            tvDesempenhoPercentage.setText(porcentagemFormatada);
        } else {
            // Caso não tenha oportunidades, exibe 0%
            tvDesempenhoPercentage.setText("0.0%");
        }
    }

    private void loadRanking(View view){
        List<Oportunidade> oportunidadeList = MyApp.getOportunidadeList();

        // HashMap para contar oportunidades por responsável
        HashMap<String, Integer> responsavelCount = new HashMap<>();

        for(int i = 0; i < oportunidadeList.size(); i++){
            Oportunidade oportunidade = oportunidadeList.get(i);
            String responsavel = oportunidade.getResponsavelNome();

            if(responsavel != null && !responsavel.isEmpty()){
                responsavelCount.put(responsavel, responsavelCount.getOrDefault(responsavel, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> rankingList = new ArrayList<>(responsavelCount.entrySet());

        Collections.sort(rankingList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return b.getValue().compareTo(a.getValue()); // Ordem decrescente
            }
        });

        int posicaoUsuario = -1;
        int qtdOportunidadesUsuario = 0;

        for(int i = 0; i < rankingList.size(); i++){
            if(rankingList.get(i).getKey().equals(userNome)){
                posicaoUsuario = i + 1;
                qtdOportunidadesUsuario = rankingList.get(i).getValue();
                break;
            }
        }

        if(posicaoUsuario != -1){

            String posicaoFormatada = formatarPosicao(posicaoUsuario);
            tvRankingPosition.setText(posicaoFormatada);

            tvRankingText.setText(posicaoUsuario + "º lugar entre " + rankingList.size() + " responsáveis");

            // Mensagem motivacion
            String mensagemMotivacional = obterMensagemMotivacional(posicaoUsuario, rankingList.size());
            tvRankingMotivational.setText(mensagemMotivacional);

        } else {
            // Caso o usuário não tenha oportunidades
            tvRankingPosition.setText("-");
            tvRankingText.setText("Sem oportunidades ainda");
            tvRankingMotivational.setText("Comece a trabalhar! 💪");
        }
    }

    private String formatarPosicao(int posicao) {
        switch (posicao) {
            case 1: return "1º";
            case 2: return "2º";
            case 3: return "3º";
            default: return posicao + "º";
        }
    }

    private String obterMensagemMotivacional(int posicao, int total) {
        if (posicao == 1) {
            return "Parabéns! Você é o líder! 🏆";
        } else if (posicao <= 3) {
            return "Ótimo trabalho! No top 3! 🥉";
        } else if (posicao <= total * 0.5) {
            return "Continue assim! 🚀";
        } else {
            return "Vamos subir no ranking! 💪";
        }
    }

    private void loadUserData(){
        session = UserSession.getInstance(getContext());
        userEmail = session.getUserEmail();
        userNome = session.getUserName();

        if (userEmail != null) {
            vendedorEmail.setText(userEmail);
        } else {
            vendedorEmail.setText("Email não disponível");
        }
        if(userNome != null){
            vendedorNome.setText(userNome);
        } else {
            vendedorNome.setText("Nome não disponível");
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecionar imagem"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE && data != null) {
            Uri imageUri = data.getData();

            try {
                // Converter imagem para bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        getActivity().getContentResolver(), imageUri);

                Bitmap resizedBitmap = resizeImage(bitmap, 200, 200);

                profileImage.setImageBitmap(resizedBitmap);

                saveImageToPreferences(resizedBitmap);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Bitmap resizeImage(Bitmap originalBitmap, int targetWidth, int targetHeight) {
        return Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, false);
    }

    private void saveImageToPreferences(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            // Salvar no SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PROFILE_IMAGE_KEY, encoded);
            editor.apply();

            Toast.makeText(getContext(), "Imagem salva com sucesso!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erro ao salvar imagem", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSavedProfileImage() {
        try {
            String encodedImage = sharedPreferences.getString(PROFILE_IMAGE_KEY, null);

            if (encodedImage != null) {
                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                profileImage.setImageBitmap(decodedByte);
            }
            // Se não houver imagem salva, a imagem padrã será mantida

        } catch (Exception e) {
            e.printStackTrace();
            // Em caso de erro, manter a imagem padrão
        }
    }
}