package proyectohastafinal.almac.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import proyectohastafinal.almac.myapplication.model.NotificationService;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Fragment buscar_fragment;
    private Fragment citas_fragment;
    private Fragment favortios_fragment;
    private Fragment perfil_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationService notificationService = new NotificationService();
        Intent ser = new Intent(this, NotificationService.class);
        startService(ser);

        BottomNavigationView navigationView = findViewById(R.id.navigation);

        navigationView.setOnNavigationItemSelectedListener(this);

        cargarFragmento(BuscarFragment.getInstance());
        navigationView.setSelectedItemId(R.id.navigation_buscar);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_buscar: ;
                buscar_fragment = BuscarFragment.getInstance();
                fragment = buscar_fragment;
                break;

            case R.id.navigation_citas:
                citas_fragment = CitasFragment.getInstance();
                fragment = citas_fragment;
                break;

            case R.id.navigation_favoritos:
                favortios_fragment = FavoritosFragment.getInstance();
                fragment = favortios_fragment;
                break;

            case R.id.navigation_perfil:
                perfil_fragment = PerfilFragment.getInstance();
                fragment = perfil_fragment;
                break;
        }

        cargarFragmento(fragment);
        return true;
    }

    public void cargarFragmento(Fragment fragment) {

        if(fragment != null ) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            String tag = fragment.getClass().getName();

            Fragment parentFragment;

            if (fm.findFragmentByTag(tag) != null)
                parentFragment = fm.findFragmentByTag(tag);
            else
                parentFragment = fragment;

             parentFragment.setArguments(null);

            ft.addToBackStack(parentFragment.getClass().getName() + "");

            ft.replace(R.id.fragment_container, parentFragment, tag);
            ft.commit();
            fm.executePendingTransactions();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_citas,menu);
    }



}
