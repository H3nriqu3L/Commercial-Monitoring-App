package com.example.commercial_monitoring_app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import android.widget.ImageView;
import android.util.DisplayMetrics;





public class MainActivity extends AppCompatActivity {
    private TextView txtSelectedMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onMenuClick(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_menu, null);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        int width = (int) (screenWidth * 0.75); // 75% da largura da tela
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

        popupWindow.showAtLocation(view, Gravity.START, 0, 0); // como um drawer da esquerda

        // Botão de fechar
        ImageView closeButton = popupView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> popupWindow.dismiss());

        // Exemplo de clique
        LinearLayout item1 = popupView.findViewById(R.id.menu_item1);
        item1.setOnClickListener(v -> {
            Toast.makeText(this, "Item 1 clicado", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        LinearLayout item2 = popupView.findViewById(R.id.menu_item2);
        item2.setOnClickListener(v -> {
            Toast.makeText(this, "Item 2 clicado", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        LinearLayout item3 = popupView.findViewById(R.id.menu_item3);
        item3.setOnClickListener(v -> {
            Toast.makeText(this, "Item 3 clicado", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        LinearLayout item4 = popupView.findViewById(R.id.menu_item4);
        item4.setOnClickListener(v -> {
            Toast.makeText(this, "Item 4 clicado", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        LinearLayout item5 = popupView.findViewById(R.id.menu_item5);
        item5.setOnClickListener(v -> {
            Toast.makeText(this, "Item 5 clicado", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
    }




}