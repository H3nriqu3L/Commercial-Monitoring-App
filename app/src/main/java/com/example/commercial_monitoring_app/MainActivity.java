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
import android.widget.ScrollView;
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
    private HomeFragment homeFragment;
    private ActivityResultLauncher<Intent> addClientLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        Log.d("MainActivity", "Result received: " + result.getResultCode());
                        Log.d("MainActivity", "RESULT_OK is: " + Activity.RESULT_OK);
                        Log.d("MainActivity", "RESULT_CANCELED is: " + Activity.RESULT_CANCELED);
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                            Log.d("MainActivity", "Current fragment: " + currentFragment.getClass().getSimpleName());
                            if (currentFragment instanceof OportunidadesFragment) {
                                ((OportunidadesFragment) currentFragment).refreshOportunidades();
                            } else if (currentFragment instanceof ClientsFragment) {
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

        homeFragment = new HomeFragment();



        // Home default
        headerTitle.setText(R.string.home);

        addClientLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        if (currentFragment instanceof OportunidadesFragment) {
                            ((OportunidadesFragment) currentFragment).refreshOportunidades();
                        } else if (currentFragment instanceof ClientsFragment) {
                            ((ClientsFragment) currentFragment).refreshClientsList();
                        } else if (currentFragment instanceof HomeFragment) {
                            ((HomeFragment) currentFragment).refreshAtividades();
                        }
                    }
                });

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.page_home) {
                replaceFragment(homeFragment, "HOME_TAG");
                headerTitle.setText(R.string.home);
                mainFab.setVisibility(View.GONE);
                return true;
            } else if (id == R.id.page_clients) {
                replaceFragment(new OportunidadesFragment(), "OPO_TAG");
                headerTitle.setText(R.string.oportunidades);
                mainFab.setVisibility(View.GONE);
                return true;
            } else if (id == R.id.page_insights) {
                replaceFragment(new LeadsFragment(), "LEADS_TAG");
                headerTitle.setText(R.string.insights);
                mainFab.setVisibility(View.GONE);
                return true;
            } else if (id == R.id.page_profile) {
                replaceFragment(new PerfilFragment(), "PROFILE_TAG");
                headerTitle.setText(R.string.perfil);
                mainFab.setVisibility(View.GONE);
                return true;
            }

            return false;
        });


        mainFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddClientActivity.class);
            addClientLauncher.launch(intent);
        });


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment(), "HOME_TAG")
                .commit();


    }

    private void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .commit();
    }


    public void onMenuClick(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_menu, null);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        TextView vendedorNome = popupView.findViewById(R.id.cliente_nome);
        TextView vendedorEmail = popupView.findViewById(R.id.cliente_email);

        // Configurar os dados do usuário
        UserSession session = UserSession.getInstance(this);
        String userEmail = session.getUserEmail();
        String userNome = session.getUserName();

        if (userEmail != null) {
            vendedorEmail.setText(userEmail);
        } else {
            vendedorEmail.setText("Email não disponível");
        }

        if (userNome != null) {
            vendedorNome.setText(userNome);
        } else {
            vendedorNome.setText("Nome não disponível");
        }

        int width = (int) (screenWidth * 0.85); // x% of screen width
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        View headerView = findViewById(R.id.header_include);
        popupWindow.showAsDropDown(headerView, 0, 0, Gravity.START);

        View.OnTouchListener swipeListener = createSwipeListener(popupWindow);


        View rootView = popupView.findViewById(R.id.popup_container);
        rootView.setOnTouchListener(swipeListener);

        ScrollView scrollView = popupView.findViewById(R.id.menu_scroll_view); // Add this ID to ScrollView
        if (scrollView != null) {
            scrollView.setOnTouchListener(swipeListener);
        }

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
            popupWindow.dismiss();
            BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
            bottomNavigation.setSelectedItemId(R.id.page_home);
        });

        LinearLayout item5 = popupView.findViewById(R.id.menu_item5);
        item5.setOnClickListener(v -> {
            session.clearSession();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            popupWindow.dismiss();
        });
    }

    private View.OnTouchListener createSwipeListener(PopupWindow popupWindow) {
        return new View.OnTouchListener() {
            private float startX = 0;
            private float startY = 0;
            private boolean isSwipeDetected = false;
            private final float SWIPE_THRESHOLD = 100; // Minimum distance for swipe
            private final float SWIPE_VELOCITY_THRESHOLD = 50; // Minimum velocity

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        isSwipeDetected = false;
                        return false; // Don't consume the event yet

                    case MotionEvent.ACTION_MOVE:
                        float currentX = event.getX();
                        float currentY = event.getY();
                        float deltaX = currentX - startX;
                        float deltaY = currentY - startY;

                        // Check if this is a horizontal swipe to the left
                        if (Math.abs(deltaX) > Math.abs(deltaY) && deltaX < -SWIPE_THRESHOLD) {
                            if (!isSwipeDetected) {
                                isSwipeDetected = true;
                                popupWindow.dismiss();
                                return true; // Consume the event
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        startX = 0;
                        startY = 0;
                        isSwipeDetected = false;
                        break;
                }
                return false; // Let other views handle the event if not a swipe
            }
        };
    }

}
