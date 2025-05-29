package com.example.commercial_monitoring_app;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.MotionEvent;

import com.example.commercial_monitoring_app.model.Client;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.view.Gravity;
import android.util.DisplayMetrics;


import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView headerTitle;
    private FloatingActionButton mainFab;
    private ActivityResultLauncher<Intent> addClientLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        Log.d("MainActivity", "Result received: " + result.getResultCode());
                        Log.d("MainActivity", "RESULT_OK is: " + Activity.RESULT_OK);
                        Log.d("MainActivity", "RESULT_CANCELED is: " + Activity.RESULT_CANCELED);
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                            Log.d("MainActivity", "Current fragment: " + currentFragment.getClass().getSimpleName());
                            if (currentFragment instanceof ClientsFragment) {
                                Log.d("MainActivity", "Calling refresh on ClientsFragment");
                                ((ClientsFragment) currentFragment).refreshClientsList();
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        headerTitle = findViewById(R.id.header);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        mainFab = findViewById(R.id.main_fab);

        // Home default
        replaceFragment(new HomeFragment());
        headerTitle.setText(R.string.home);

        bottomNavigation.setOnItemSelectedListener(item ->{
            int id = item.getItemId();

            if (id == R.id.page_home) {
                replaceFragment(new HomeFragment());
                headerTitle.setText(R.string.home);
                mainFab.setVisibility(View.GONE);
                return true;
            } else if (id == R.id.page_clients) {
                replaceFragment(new ClientsFragment());
                headerTitle.setText(R.string.oportunidades);
                mainFab.setVisibility(View.VISIBLE);
                return true;
            } else if (id == R.id.page_insights) {
                replaceFragment(new LeadsFragment());
                headerTitle.setText(R.string.insights);
                mainFab.setVisibility(View.GONE);
                return true;
            } else if (id == R.id.page_profile) {
                replaceFragment(new PerfilFragment());
                headerTitle.setText(R.string.perfil);
                mainFab.setVisibility(View.GONE);
                return true;
            }
            return false;
        });

        // FAB click listener
        mainFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddClientActivity.class);
            addClientLauncher.launch(intent);
        });


    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }


    public void onMenuClick(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_menu, null);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;


        int width = (int) (screenWidth * 0.65); // x% of screen width
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        //popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        View headerView = findViewById(R.id.header_include);

        popupWindow.showAsDropDown(headerView, 0, 0, Gravity.START);

        View rootView = popupView.findViewById(R.id.popup_container);

        // Track touch movement for manual swipe detection
        final float[] startX = new float[1];
        final float[] startY = new float[1];

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Record initial touch position
                        startX[0] = event.getX();
                        startY[0] = event.getY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        // Calculate distance moved
                        float currentX = event.getX();
                        float currentY = event.getY();
                        float deltaX = currentX - startX[0];
                        float deltaY = currentY - startY[0];

                        // If horizontal movement is greater than vertical and is leftward
                        if (Math.abs(deltaX) > Math.abs(deltaY) && deltaX < -50) {
                            // User is swiping left, dismiss the popup
                            popupWindow.dismiss();
                            return true;
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                        // Reset values
                        startX[0] = 0;
                        startY[0] = 0;
                        return true;
                }
                return false;
            }
        });



        // Click examples
        LinearLayout item1 = popupView.findViewById(R.id.menu_item1);
        item1.setOnClickListener(v -> {
            popupWindow.dismiss();

            BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
            bottomNavigation.setSelectedItemId(R.id.page_profile);
        });


        LinearLayout item2 = popupView.findViewById(R.id.menu_item2);
        item2.setOnClickListener(v -> {
            popupWindow.dismiss();

            BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
            bottomNavigation.setSelectedItemId(R.id.page_clients);

        });

        LinearLayout item3 = popupView.findViewById(R.id.menu_item3);
        item3.setOnClickListener(v -> {
            popupWindow.dismiss();

            BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
            bottomNavigation.setSelectedItemId(R.id.page_insights);
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
