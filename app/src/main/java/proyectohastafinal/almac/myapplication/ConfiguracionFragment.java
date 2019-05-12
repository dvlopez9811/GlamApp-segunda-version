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

public class ConfiguracionFragment extends Fragment {

    private static ConfiguracionFragment instance;

    public static ConfiguracionFragment getInstance(){
        instance = instance == null ? new ConfiguracionFragment() : instance;
        return instance;
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btn_cerrar_sesión;

    private static final int MY_REQUEST_CODE=7117; //Cualquier numero

    public ConfiguracionFragment() {
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
       final View mView= inflater.inflate(R.layout.fragment_configuracion, container, false);

       btn_cerrar_sesión = mView.findViewById(R.id.btn_cerrar_sesion);
        btn_cerrar_sesión.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(inflater.getContext())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent i = new Intent(ConfiguracionFragment.this.getContext(), LoginActivity.class);
                                startActivity(i);
                            }
                        });
            }
        });
    return mView;
    }

}
