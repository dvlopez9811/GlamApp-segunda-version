package proyectohastafinal.almac.myapplication;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainSalonActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


    private Fragment perfil_fragment;
    private Fragment citas_fragment;
    private Fragment ajustes_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_salon);

        BottomNavigationView navigationView = findViewById(R.id.bnv_navigation_salon);
        navigationView.setOnNavigationItemSelectedListener(this);
        cargarFragmento(PerfilSalonFragment.getInstance());
        navigationView.setSelectedItemId(R.id.navigation_perfil_salon);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_perfil_salon:
                 perfil_fragment= PerfilSalonFragment.getInstance();
                fragment = perfil_fragment;
                break;

            case R.id.navigation_citas_salon:
                citas_fragment = CitasSalonFragment.getInstance();
                fragment = citas_fragment;
                break;


            case R.id.navigation_ajustes_salon:
                ajustes_fragment = AjustesSalonFragment.getInstance();
                fragment = ajustes_fragment;
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

            ft.replace(R.id.fragment_container_salon, parentFragment, tag);
            ft.commit();
            fm.executePendingTransactions();
        }
    }
}
