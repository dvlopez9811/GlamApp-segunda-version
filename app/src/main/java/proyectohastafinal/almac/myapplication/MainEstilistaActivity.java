package proyectohastafinal.almac.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import proyectohastafinal.almac.myapplication.model.NotificationService;

public class MainEstilistaActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private Fragment citas_fragment;
    private Fragment solicitudes_fragment;
    private Fragment mensajes_fragment;
    private Fragment perfil_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estilista);

        NotificationService notificationService = new NotificationService();

        Intent ser = new Intent(this, NotificationService.class);
        startService(ser);

        BottomNavigationView navigationView = findViewById(R.id.navigation_estilista);

        navigationView.setOnNavigationItemSelectedListener(this);

        cargarFragmento(CitasEstilistaFragment.getInstance());
        navigationView.setSelectedItemId(R.id.navigation_citas_estilista);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_citas_estilista: ;
                citas_fragment = CitasEstilistaFragment.getInstance();
                fragment = citas_fragment;
                break;

            case R.id.navigation_solicitudes_estilista:
                solicitudes_fragment = SolicitudesEstilistaFragment.getInstance();
                fragment = solicitudes_fragment;
                break;

            case R.id.navigation_mensajes_estilista:
                mensajes_fragment = MensajesEstilistaFragment.getInstance();
                fragment = mensajes_fragment;
                break;

            case R.id.navigation_perfil_estilista:
                perfil_fragment = PerfilEstilistaFragment.getInstance();
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

            //parentFragment.setArguments(null);

            ft.addToBackStack(parentFragment.getClass().getName() + "");

            ft.replace(R.id.fragment_container_estilista, parentFragment, tag);
            ft.commit();
            fm.executePendingTransactions();
        }
    }
}
