package proyectohastafinal.almac.myapplication;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class EstilistaActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estilista);

        BottomNavigationView navigationView = findViewById(R.id.navigation_estilista);

        navigationView.setOnNavigationItemSelectedListener(this);

        cargarFragmento(BuscarFragment.getInstance());
        navigationView.setSelectedItemId(R.id.navigation_citas_estilista);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
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

            ft.replace(R.id.fragment_container_estilista, parentFragment, tag);
            ft.commit();
            fm.executePendingTransactions();
        }
    }
}
