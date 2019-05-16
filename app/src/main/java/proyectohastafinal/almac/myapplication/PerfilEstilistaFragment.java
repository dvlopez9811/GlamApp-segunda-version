package proyectohastafinal.almac.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class PerfilEstilistaFragment extends Fragment {

    private static PerfilEstilistaFragment instance;

    private Button btn_cerrar_sesion;

    public static PerfilEstilistaFragment getInstance(){
        instance = instance == null ? new PerfilEstilistaFragment() : instance;
        return instance;
    }

    public PerfilEstilistaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);

        btn_cerrar_sesion = v.findViewById(R.id.btn_cerrar_sesion);
        btn_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(inflater.getContext())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent i = new Intent(PerfilEstilistaFragment.this.getContext(), InicioActivity.class);
                                startActivity(i);
                            }
                        });
            }
        });

        return v;
    }


}
