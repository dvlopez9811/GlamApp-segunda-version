package proyectohastafinal.almac.myapplication;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;
import proyectohastafinal.almac.myapplication.util.UtilDomi;

public class RegistroSalonDeBelleza extends AppCompatActivity {

    private static final int GALLERY_CALLBACK_ID = 101;

    private File photoFile;

    private EditText registroSalonDeBellezaEtNombre;
    private EditText registroSalonDeBellezaEtCorreo;
    private EditText registroSalonDeBellezaEtNombreDueño;
    private EditText registroSalonDeBellezaEtContrasenha;
    private EditText registroSalonDeBellezaEtContrasenhaConfirmar;
    private EditText registroSalonDeBellezaEtDireccion;

    private CheckBox registroSalonDeBellezaCheckBoxUñas;
    private CheckBox registroSalonDeBellezaCheckBoxMasaje;
    private CheckBox registroSalonDeBellezaCheckBoxMaquillaje;
    private CheckBox registroSalonDeBellezaCheckBoxDepilacion;
    private CheckBox registroSalonDeBellezaCheckBoxPeluqueria;

    private ImageView ivSalonBelleza;

    private TextView registroSsalonBellezaEtCamara;

    private Button registroSalonDeBellezaBtnListo;

    private ArrayList<String> servicios;

    FirebaseDatabase rtdb;
    FirebaseAuth auth;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_salon_de_belleza);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, 0);

        registroSalonDeBellezaEtNombre = findViewById(R.id.registro_salon_belleza_et_nombre_salon_belleza);
        registroSalonDeBellezaEtCorreo = findViewById(R.id.registro_salon_belleza_et_nombre_correo);
        registroSalonDeBellezaEtNombreDueño = findViewById(R.id.registro_salon_belleza_et_nombre_duenho);
        registroSalonDeBellezaEtContrasenha = findViewById(R.id.registro_salon_belleza_et_contrasenha);
        registroSalonDeBellezaEtContrasenhaConfirmar = findViewById(R.id.registro_salon_belleza_et_contrasenha_confimar);
        registroSalonDeBellezaEtDireccion = findViewById(R.id.registro_salon_belleza_et_direccion);

        registroSalonDeBellezaCheckBoxUñas = findViewById(R.id.registro_salon_belleza_servicio_uñas);
        registroSalonDeBellezaCheckBoxMasaje = findViewById(R.id.registro_salon_belleza_servicio_masaje);
        registroSalonDeBellezaCheckBoxMaquillaje = findViewById(R.id.registro_salon_belleza_servicio_maquillaje);
        registroSalonDeBellezaCheckBoxDepilacion = findViewById(R.id.registro_salon_belleza_servicio_depilacion);
        registroSalonDeBellezaCheckBoxPeluqueria = findViewById(R.id.registro_salon_belleza_servicio_peluqueria);

        ivSalonBelleza = findViewById(R.id.registro_salon_belleza_iv_foto);

        registroSalonDeBellezaBtnListo = findViewById(R.id.registro_salon_belleza_btn_listo);

        registroSsalonBellezaEtCamara = findViewById(R.id.registro_salon_belleza_et_camara);

        auth = FirebaseAuth.getInstance();
        rtdb = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        servicios = new ArrayList<>();

        registroSsalonBellezaEtCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, GALLERY_CALLBACK_ID);
            }
        });

        registroSalonDeBellezaBtnListo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreDuenho = registroSalonDeBellezaEtNombreDueño.getText().toString();
                final String nombreSalonBelleza = registroSalonDeBellezaEtNombre.getText().toString();
                String pass = registroSalonDeBellezaEtContrasenha.getText().toString();
                String passConfirm = registroSalonDeBellezaEtContrasenhaConfirmar.getText().toString();
                String direccion = registroSalonDeBellezaEtDireccion.getText().toString();
                String correo = registroSalonDeBellezaEtCorreo.getText().toString();

                if (pass.equals(passConfirm)) {
                    final SalonDeBelleza salonDeBelleza = new SalonDeBelleza(nombreDuenho, nombreSalonBelleza, correo ,pass, direccion, 0, 0);
                    comprobarServiciosEscogidos();
                    modificarServicios(salonDeBelleza);
                    auth.createUserWithEmailAndPassword(correo, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            String serviciosCambinados [] = combinaciones(servicios);
                            rtdb.getReference().child("Salon de belleza").child(nombreSalonBelleza).setValue(salonDeBelleza);
                            rtdb.getReference().child("Marcador").child(auth.getCurrentUser().getUid()).push().setValue(salonDeBelleza.getLatitud()+"");
                            rtdb.getReference().child("Marcador").child(auth.getCurrentUser().getUid()).push().setValue(salonDeBelleza.getLongitud()+"");
                            rtdb.getReference().child("Marcador").child(auth.getCurrentUser().getUid()).push().setValue(salonDeBelleza.getNombreSalonDeBelleza()+"");


                            for (int i = 0; i < serviciosCambinados.length; i++) {
                                rtdb.getReference().child("Buscar servicios salon de belleza").child(serviciosCambinados[i]).push().setValue(salonDeBelleza.getNombreSalonDeBelleza());
                            }
                        }
                    });
                    finish();
                }
            }
        });
    }

    public void modificarServicios (SalonDeBelleza salonDeBelleza) {

        for (int i = 0; i < servicios.size(); i++) {
            salonDeBelleza.getServicios().put(servicios.get(i), true);
        }

    }

    public void comprobarServiciosEscogidos () {

        if (registroSalonDeBellezaCheckBoxUñas.isChecked()) {
            servicios.add(registroSalonDeBellezaCheckBoxUñas.getText().toString());
        }

        if (registroSalonDeBellezaCheckBoxMaquillaje.isChecked()) {
            servicios.add(registroSalonDeBellezaCheckBoxMaquillaje.getText().toString());
        }

        if (registroSalonDeBellezaCheckBoxMasaje.isChecked()) {
            servicios.add(registroSalonDeBellezaCheckBoxMasaje.getText().toString());
        }

        if (registroSalonDeBellezaCheckBoxDepilacion.isChecked()) {
            servicios.add(registroSalonDeBellezaCheckBoxDepilacion.getText().toString());
        }

        if (registroSalonDeBellezaCheckBoxPeluqueria.isChecked()) {
            servicios.add(registroSalonDeBellezaCheckBoxPeluqueria.getText().toString());
        }


    }

    public String[] combinaciones (ArrayList<String> serviciosParaCombinar) {
        String[] res = new String[(1 << serviciosParaCombinar.size()) - 1];
        int k = 0;
        int x = 1;
        for (int i = serviciosParaCombinar.size() - 1; i >= 0; --i) {
            res[k++] = serviciosParaCombinar.get(i);
            for (int j = 1; j < x; ++j) {
                res[k++] = serviciosParaCombinar.get(i) + "-" + res[j - 1];
            }
            x *= 2;
        }
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == GALLERY_CALLBACK_ID && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            photoFile = new File(  UtilDomi.getPath(this, uri)  );
            subirImagen();
        }

    }

    private void subirImagen(){
        StorageReference ref = storage.getReference().child("salones de belleza").child(registroSalonDeBellezaEtNombre.getText().toString());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(photoFile);
            ref.putStream(fis).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    cargarFotoPerfil();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void cargarFotoPerfil() {
        StorageReference ref = storage.getReference().child("salones de belleza").child(registroSalonDeBellezaEtNombre.getText().toString());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(RegistroSalonDeBelleza.this).load(uri).into(ivSalonBelleza);
            }
        });
    }


    public static class ConfiguracionFragment extends Fragment {

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
}
