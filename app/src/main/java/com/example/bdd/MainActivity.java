package com.example.bdd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bdd.dialog.SimpleDialogFragment;
import com.example.bdd.dialog.SimpleDialogListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DetecteurDeClicSurRecycler, SimpleDialogListener {

    private RecyclerView mRecyclerView;
    private MonRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    CoordinatorLayout mcoordinatorLayout;
    FloatingActionButton frag_simple_btn;

    final String PREFS_NAME = "preferences_file";
    ArrayList<Planete> pla = new ArrayList<Planete>();
    PlaneteDao planeteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        //BTN
        frag_simple_btn = findViewById(R.id.frag_simple_btn);

        //BDD
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "planetesDB").build();
        planeteDao = db.planeteDao();
        loadData(planeteDao);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MonRecyclerViewAdapter(getDataSource());
        mRecyclerView.setAdapter(mAdapter);
        mcoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, R.drawable.divider);
        mRecyclerView.addItemDecoration(itemDecoration);

        frag_simple_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSimpleDialog();
            }
        });


    }

    private void loadData(PlaneteDao planeteDao) {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (settings.getBoolean("is_data_loaded", true)) {
                    initData(planeteDao);
                    settings.edit().putBoolean("is_data_loaded", false).commit();
                }

                List<Planete> planetes = planeteDao.getAll();
                for (int i =0; i< planetes.size();i++) {

                    Planete plan = new Planete(planetes.get(i).getUid(), planetes.get(i).getNom(), planetes.get(i).getTaille(), planetes.get(i).getImage());
                    pla.add(plan);
                }
            }
        }).start();


    }

    private void initData(PlaneteDao planeteDao) {

        ArrayList<Planete> planetes = new ArrayList<>();

        planetes.add(new Planete(1,"Mercure","4900", R.drawable.mercure));
        planetes.add(new Planete(2,"Venus","12000", R.drawable.venus));
        planetes.add(new Planete(3,"Terre","12800", R.drawable.terre));
        planetes.add(new Planete(4,"Mars","6800", R.drawable.mars));
        planetes.add(new Planete(5,"Jupiter","144000", R.drawable.jupiter));
        planetes.add(new Planete(6,"Saturne","120000", R.drawable.saturne));
        planetes.add(new Planete(7,"Uranus","52000", R.drawable.uranus));
        planetes.add(new Planete(8,"Neptune","50000", R.drawable.neptune));
        planetes.add(new Planete(9,"Pluton","2300", R.drawable.pluton));

        for (int index = 0; index < planetes.size(); index++) {
            Planete planete = planetes.get(index);
            planeteDao.insert(planete);
        }

    }

    private ArrayList<Planete> getDataSource() {

        return pla;
    }

    @Override
    protected void onResume() {
        super.onResume();
       mAdapter.setDetecteurDeClicSurRecycler(this);
    }

    public void clicSurRecyclerItem(int position, View v) {
        Snackbar.make(mcoordinatorLayout, " Clic sur l'item " + position, Snackbar.LENGTH_LONG).show();
    }

    private void showSimpleDialog() {

        FragmentManager fm = getSupportFragmentManager();
        SimpleDialogFragment simpleDialogFragment = SimpleDialogFragment.newInstance("Titre");
        simpleDialogFragment.show(fm, "fragment_simple_dialog");

    }

    // Cette méthode est appellée dans l'activité quand le listener est déclanché
    // Les données sont passées en paramétres
    @Override
    public void onOkClickDialog(String nom, String taille)
    {

        loadData(planeteDao);

        if(nom.isEmpty() || taille.isEmpty())
        {
            Toast.makeText(this, "Probleme" + nom + taille, Toast.LENGTH_SHORT).show();
        }
        else
        {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    Planete planete = new Planete(pla.size() + 3, nom, taille, R.drawable.neptune);
                    planeteDao.insert(planete);
                    pla.add(planete);

                }
            }).start();
            Toast.makeText(this, "Enregistrement Planete reussi", Toast.LENGTH_SHORT).show();

        }
    }

}