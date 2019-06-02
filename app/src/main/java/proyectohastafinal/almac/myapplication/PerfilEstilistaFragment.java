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
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class PerfilEstilistaFragment extends Fragment {

    private static PerfilEstilistaFragment instance;

    private Button btn_cerrar_sesion;

    FirebaseAuth auth;
    FirebaseDatabase rtdb;

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
        final View mView = inflater.inflate(R.layout.fragment_perfil_estilist, container, false);
        rtdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        btn_cerrar_sesion = mView.findViewById(R.id.btn_cerrar_sesion);


        if (auth.getCurrentUser() != null) {
            rtdb.getReference().child("Estilista").child(auth.getCurrentUser().getUid()).child("correo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String tuCorreo = dataSnapshot.getValue(String.class);
                    ((TextView) mView.findViewById(R.id.txt_correo_estilista_fragment_perfil)).setText(tuCorreo);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            rtdb.getReference().child("Estilista").child(auth.getCurrentUser().getUid()).child("nombreYApellido").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String tuNombre = dataSnapshot.getValue(String.class);
                    ((TextView) mView.findViewById(R.id.txt_nombre_estilista_fragment_perfil)).setText(tuNombre);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
            btn_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthUI.getInstance()
                            .signOut(inflater.getContext())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent i = new Intent(PerfilEstilistaFragment.this.getContext(), InicioActivity.class);
                                    startActivity(i);
                                    getActivity().finish();
                                }
                            });
                }
            });

            return mView;
        }


}
