package proyectohastafinal.almac.myapplication;

import android.Manifest;
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
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    // Codigo de los permisos
    private static final int REQUEST_CODE = 11;

    private Fragment inicio_fragment;
    private Fragment buscar_fragment;
    private Fragment citas_fragment;
    private Fragment favortios_fragment;
    private Fragment configuracion_fragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Permisos
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, REQUEST_CODE);

        BottomNavigationView navigationView = findViewById(R.id.navigation);

        navigationView.setOnNavigationItemSelectedListener(this);

        cargarFragmento(BuscarFragment.getInstance());
        navigationView.setSelectedItemId(R.id.navigation_buscar);



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                if ( inicio_fragment == null )
                    inicio_fragment = InicioFragment.getInstance();
                fragment = inicio_fragment;
                break;

            case R.id.navigation_buscar: ;
                buscar_fragment = BuscarFragment.getInstance();
                fragment = buscar_fragment;
                break;

            case R.id.navigation_citas:
                if ( citas_fragment == null)
                    citas_fragment = CitasFragment.getInstance();
                fragment = citas_fragment;
                break;

            case R.id.navigation_favoritos:
                if ( favortios_fragment == null)
                    favortios_fragment = FavoritosFragment.getInstance();
                fragment = favortios_fragment;
                break;

            case R.id.navigation_configurarion:
              /*  if ( configuracion_fragment == null)
                    configuracion_fragment = RegistroSalonDeBelleza.ConfiguracionFragment.getInstance();
                fragment = configuracion_fragment;*/
                break;

        }

        cargarFragmento(fragment);
        return true;
    }

    public void cargarFragmento(Fragment fragment) {

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
