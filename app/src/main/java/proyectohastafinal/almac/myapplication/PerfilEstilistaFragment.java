package proyectohastafinal.almac.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import proyectohastafinal.almac.myapplication.util.UtilDomi;


public class PerfilEstilistaFragment extends Fragment {

    private static PerfilEstilistaFragment instance;
    private static final int GALLERY_CALLBACK_ID = 101;

    private Button btn_cerrar_sesion,btn_cambiar_contrasena;

    FirebaseAuth auth;
    FirebaseDatabase rtdb;
    FirebaseStorage storage;
    private File photoFile;
    private ImageView auxFotoPerfilEstilista;

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
        storage=FirebaseStorage.getInstance();
        btn_cerrar_sesion = mView.findViewById(R.id.btn_cerrar_sesion_estilista);
        btn_cambiar_contrasena=mView.findViewById(R.id.btn_cambiar_contraseña_perfil_estilista_fragment);

        auxFotoPerfilEstilista = mView.findViewById(R.id.foto_perfil_estilista_fragment_perfil);
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

        btn_cambiar_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mView.getContext(),CambiarContrasenhaActivity.class);
                startActivity(i);
            }
        });

        StorageReference ref = storage.getReference().child("estilistas").child(auth.getCurrentUser().getUid());
        if(ref == null){
            Glide.with(mView.getContext())
                    .load(R.drawable.ic_foto_perfil_defecto_estilista).apply(RequestOptions.circleCropTransform())
                    .into((ImageView) mView.findViewById(R.id.foto_perfil_estilista_fragment_perfil));}
        else
        ref.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(mView.getContext())
                .load(uri).apply(RequestOptions.circleCropTransform()).into((ImageView) mView.findViewById(R.id.foto_perfil_estilista_fragment_perfil)));


        mView.findViewById(R.id.txt_cambiar_foto_perfil_estilista).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, GALLERY_CALLBACK_ID);
            }
        });

        return mView;
        }



    private void subirImagen(){
        StorageReference ref = storage.getReference().child("estilistas").child(auth.getCurrentUser().getUid());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(photoFile);
            ref.putStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_CALLBACK_ID && resultCode == RegistroSalonDeBelleza.RESULT_OK) {
            final Uri uri = data.getData();
            photoFile = new File(  UtilDomi.getPath(this.getContext(), uri)  );
            getActivity().runOnUiThread( () -> {
                //auxFotoPerfilEstilista.setBackground(null);
                //auxFotoPerfilEstilista.setImageURI(uri);
                Glide.with(getActivity())
                        .load(uri).apply(RequestOptions.circleCropTransform()).into((ImageView) getActivity().findViewById(R.id.foto_perfil_estilista_fragment_perfil));

            });
            subirImagen();
        }
    }
}
