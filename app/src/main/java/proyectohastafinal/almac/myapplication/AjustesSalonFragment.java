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
import android.widget.Toast;

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


public class AjustesSalonFragment extends Fragment {

    private static AjustesSalonFragment instance;


    private Button btn_cerrar_sesion;
    private Button btn_cambiar_contrasenha;

    private TextView nombre;
    private TextView correo;

    FirebaseDatabase rtdb;
    FirebaseAuth auth;
    FirebaseStorage storage;

    public static AjustesSalonFragment getInstance(){
        instance = instance == null ? new AjustesSalonFragment() :instance;
        return instance;
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AjustesSalonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AjustesSalonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AjustesSalonFragment newInstance(String param1, String param2) {
        AjustesSalonFragment fragment = new AjustesSalonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mView = inflater.inflate(R.layout.fragment_ajustes_salon, container, false);

        correo = mView.findViewById(R.id.txt_correo_ajustes_salon_fragment);
        nombre = mView.findViewById(R.id.txt_nombre_salon_ajustes_fragment);

        btn_cambiar_contrasenha=mView.findViewById(R.id.btn_cambiar_contraseña_ajustes_salon_fragment);
        btn_cerrar_sesion=mView.findViewById(R.id.btn_cerrar_sesion_ajustes_salon);

        rtdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();

        final String tuCorreo = "";

        if (auth.getCurrentUser() != null) {


            rtdb.getReference().child("identificador").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String nombreSalon = dataSnapshot.getValue(String.class);
                    nombre.setText(nombreSalon);

                    rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("correo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String tuCorreo = dataSnapshot.getValue(String.class);
                            correo.setText(tuCorreo);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    //Mostrar foto
                    StorageReference ref = storage.getReference().child("salones de belleza").child(nombreSalon).child("profile");
                    if(ref == null){
                        Log.e("hola","es nulllllllllll");
                    }
                    ref.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(mView.getContext())
                            .load(uri).apply(RequestOptions.circleCropTransform()).into((ImageView) mView.findViewById(R.id.foto_perfil_salon_fragment_ajustes)));



                    ///////////////
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });




            btn_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthUI.getInstance()
                            .signOut(inflater.getContext())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent i = new Intent(AjustesSalonFragment.this.getContext(), InicioActivity.class);
                                    startActivity(i);
                                    getActivity().finish();
                                }
                            });
                }
            });

            btn_cambiar_contrasenha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mView.getContext(),CambiarContrasenhaActivity.class);
                    startActivity(i);
                }
            });
        }







        return mView;
    }
}