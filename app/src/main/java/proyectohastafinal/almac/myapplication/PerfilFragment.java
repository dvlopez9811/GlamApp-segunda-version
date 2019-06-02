package proyectohastafinal.almac.myapplication;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Permission;

public class PerfilFragment extends Fragment {

    private static PerfilFragment instance;
    private Button btn_cerrar_sesion;
    private Button btn_cambiar_contrasenha;

    private TextView nombre;
    private TextView correo;

    FirebaseDatabase rtdb;
    FirebaseAuth auth;

    public static PerfilFragment getInstance(){
        instance = instance == null ? new PerfilFragment() : instance;
        return instance;
    }

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View mView = inflater.inflate(R.layout.fragment_perfil, container, false);

        correo = mView.findViewById(R.id.perfil_correo);
        nombre = mView.findViewById(R.id.perfil_nombre);
        btn_cambiar_contrasenha = mView.findViewById(R.id.btn_cambiar_contraseña_perfil_fragment);
        btn_cerrar_sesion = mView.findViewById(R.id.btn_cerrar_sesion);

        rtdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


            if ( auth.getCurrentUser() != null) {
                rtdb.getReference().child("usuario").child(auth.getCurrentUser().getUid()).child("correo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String tuCorreo = dataSnapshot.getValue(String.class);
                        correo.setText(tuCorreo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                rtdb.getReference().child("usuario").child(auth.getCurrentUser().getUid()).child("nombreYApellido").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String tuNombre = dataSnapshot.getValue(String.class);
                        nombre.setText(tuNombre);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            // Inflate the layout for this fragment
            btn_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthUI.getInstance()
                            .signOut(inflater.getContext())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent i = new Intent(PerfilFragment.this.getContext(), InicioActivity.class);
                                    startActivity(i);
                                    getActivity().finish();
                                }
                            });
                }
            });


            btn_cambiar_contrasenha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), CambiarContrasenhaActivity.class);
                    startActivity(i);
                }
            });

                for (UserInfo user: auth.getCurrentUser().getProviderData()) {
                    if (user.getProviderId().equals("google.com")) {
                        btn_cambiar_contrasenha.setVisibility(View.GONE);
                        ((ImageView)mView.findViewById(R.id.imagen_boton_cambiar_contrasena_cliente)).setVisibility(View.GONE);

                    }
                    else if(user.getProviderId().equals("facebook.com")){
                        btn_cambiar_contrasenha.setVisibility(View.GONE);
                        ((ImageView)mView.findViewById(R.id.imagen_boton_cambiar_contrasena_cliente)).setVisibility(View.GONE);
                    }
                }
            }else{
                btn_cambiar_contrasenha.setVisibility(View.GONE);
                correo.setVisibility(View.GONE);
                nombre.setVisibility(View.GONE);
                ((ImageView)mView.findViewById(R.id.imagen_boton_cambiar_contrasena_cliente)).setVisibility(View.GONE);
            }

        return mView;
    }



}
