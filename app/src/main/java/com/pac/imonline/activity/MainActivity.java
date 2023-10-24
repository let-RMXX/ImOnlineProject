package com.pac.imonline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pac.imonline.R;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ImageView imageBtnProfile;
    RecyclerView recyclerView;
    ArrayList<ModelFeed> modelFeedArrayList = new ArrayList<>();
    FeedAdapter feedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewMain);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        feedAdapter = new FeedAdapter(this, modelFeedArrayList);
        recyclerView.setAdapter(feedAdapter);

        populateRecyclerView();

        // Toolbar Config
        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        imageBtnProfile = findViewById(R.id.imageViewProfile);
        imageBtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ProfileActivityMain.class);
                startActivity(intent);

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.botton_communities:
                    startActivity(new Intent(getApplicationContext(), CommunityListActivity.class));
                    overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                    finish();
                    return true;

                case R.id.botton_profile:
                    startActivity(new Intent(getApplicationContext(),ProfileActivityMain.class));
                    overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.botton_chat:
                    startActivity(new Intent(getApplicationContext(), ContactMain.class));
                    overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

    }
    public void populateRecyclerView(){
        ModelFeed modelFeed = new ModelFeed(1, 100, 200, R.drawable.me, R.drawable.store, "Carlos Álvaro", "1 hr", "Vem fazer parte da SONAE MC!. Estamos á procura de operador(a) de loja e operador(a) de caixa. Se tens curiosidade, espírito crítico e elevado sentido de responsabilidade entra em contacto ");
        modelFeedArrayList.add(modelFeed);

        ModelFeed modelFeed2 = new ModelFeed(2, 20, 12, R.drawable.photo_2, R.drawable.programacao, "Sunny Romain", "10 hr", "Empresa XYZ - Desenvolvedor(a) de Software\n" +
                "\n" +
                "A Empresa XYZ está à procura de um(a) Desenvolvedor(a) de Software para se juntar à nossa equipa talentosa e inovadora.");
        modelFeedArrayList.add(modelFeed2);

        ModelFeed modelFeed3 = new ModelFeed(3, 55, 34, R.drawable.photo_1, R.drawable.food1, "Christal Seffora", "1 hr", "Fresh food!");
        modelFeedArrayList.add(modelFeed3);

        ModelFeed modelFeed4 = new ModelFeed(4, 43, 32, R.drawable.photo_3, R.drawable.travel1, "Rafael Tomás", "4 hr", "Sunset <3");
        modelFeedArrayList.add(modelFeed4);

        ModelFeed modelFeed5 = new ModelFeed(5, 76, 53, R.drawable.photo_4, R.drawable.travel2, "Malenia Miquella", "2 hr", "On the road");
        modelFeedArrayList.add(modelFeed5);

        ModelFeed modelFeed6 = new ModelFeed(5, 80, 60, R.drawable.foto1, R.drawable.advogado, "Zusman Thato", "2 hr", "Escritório de Advocacia Thato - Advogado(a) Especializado(a) em Direito Civil O Escritório de Advocacia Thato procura um(a) Advogado(a) Especializado(a) em Direito Civil para integrar nossa equipe dedicada e comprometida em fornecer serviços jurídicos excepcionais aos nossos clientes. Para mais informações contacte-me");
        modelFeedArrayList.add(modelFeed6);


        feedAdapter.notifyDataSetChanged();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);

    }
}