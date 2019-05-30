package proyectohastafinal.almac.myapplication;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import proyectohastafinal.almac.myapplication.model.Marcador;
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
    private Button btn_registro_salon_volver;

    private ArrayList<String> servicios;

    FirebaseDatabase rtdb;
    FirebaseAuth auth;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_salon_de_belleza);

        registroSalonDeBellezaEtNombre = findViewById(R.id.registro_salon_belleza_et_nombre_salon_belleza);
        registroSalonDeBellezaEtCorreo = findViewById(R.id.registro_salon_belleza_et_nombre_correo);
        registroSalonDeBellezaEtNombreDueño = findViewById(R.id.registro_salon_belleza_et_nombre_duenho);
        registroSalonDeBellezaEtContrasenha = findViewById(R.id.registro_salon_belleza_et_contrasenha);
        registroSalonDeBellezaEtContrasenhaConfirmar = findViewById(R.id.registro_salon_belleza_et_contrasenha_confimar);
        registroSalonDeBellezaEtDireccion = findViewById(R.id.registro_salon_belleza_et_direccion);

        Typeface typeface = ResourcesCompat.getFont(RegistroSalonDeBelleza.this,R.font.josefin_sans);
        registroSalonDeBellezaCheckBoxUñas = findViewById(R.id.registro_salon_belleza_servicio_uñas);
        registroSalonDeBellezaCheckBoxUñas.setTypeface(typeface);
        registroSalonDeBellezaCheckBoxMasaje = findViewById(R.id.registro_salon_belleza_servicio_masaje);
        registroSalonDeBellezaCheckBoxMasaje.setTypeface(typeface);
        registroSalonDeBellezaCheckBoxMaquillaje = findViewById(R.id.registro_salon_belleza_servicio_maquillaje);
        registroSalonDeBellezaCheckBoxMaquillaje.setTypeface(typeface);
        registroSalonDeBellezaCheckBoxDepilacion = findViewById(R.id.registro_salon_belleza_servicio_depilacion);
        registroSalonDeBellezaCheckBoxDepilacion.setTypeface(typeface);
        registroSalonDeBellezaCheckBoxPeluqueria = findViewById(R.id.registro_salon_belleza_servicio_peluqueria);
        registroSalonDeBellezaCheckBoxPeluqueria.setTypeface(typeface);

        ivSalonBelleza = findViewById(R.id.registro_salon_belleza_iv_foto);

        registroSalonDeBellezaBtnListo = findViewById(R.id.registro_salon_belleza_btn_listo);

        registroSsalonBellezaEtCamara = findViewById(R.id.registro_salon_belleza_et_camara);

        btn_registro_salon_volver = findViewById(R.id.btn_registro_salon_volver);

        auth = FirebaseAuth.getInstance();
        rtdb = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        servicios = new ArrayList<>();

        registroSsalonBellezaEtCamara.setOnClickListener(v -> {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i, GALLERY_CALLBACK_ID);
        });

        registroSalonDeBellezaBtnListo.setOnClickListener(v -> {
            String nombreDuenho = registroSalonDeBellezaEtNombreDueño.getText().toString();
            final String nombreSalonBelleza = registroSalonDeBellezaEtNombre.getText().toString();
            String pass = registroSalonDeBellezaEtContrasenha.getText().toString();
            String passConfirm = registroSalonDeBellezaEtContrasenhaConfirmar.getText().toString();
            String direccion = registroSalonDeBellezaEtDireccion.getText().toString();
            final String correo = registroSalonDeBellezaEtCorreo.getText().toString();

            boolean correcto = true;

            if (nombreSalonBelleza.trim().equalsIgnoreCase("")) {
                SpannableString s = new SpannableString("Por favor ingrese el nombre del salón de belleza");
                s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                registroSalonDeBellezaEtNombre.setError(s);
                correcto = false;
            }

            if (correo.trim().equalsIgnoreCase("")) {
                SpannableString s = new SpannableString("Por favor ingrese un correo electrónico");
                s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                registroSalonDeBellezaEtCorreo.setError(s);
                correcto = false;
            } else if (!correo.trim().contains("@") | !correo.trim().contains(".")) {
                SpannableString s = new SpannableString("Debe ser un correo electrónico válido");
                s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                registroSalonDeBellezaEtCorreo.setError(s);
                correcto = false;
            }

            if (pass.trim().equalsIgnoreCase("")) {
                SpannableString s = new SpannableString("Por favor ingresa una contraseña");
                s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                registroSalonDeBellezaEtContrasenha.setError(s);
                correcto = false;
            } else if (pass.length() < 6) {
                SpannableString s = new SpannableString("La contraseña debe tener por lo menos 6 caracteres");
                s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                registroSalonDeBellezaEtContrasenha.setError(s);
                correcto = false;
            }

            if (passConfirm.equalsIgnoreCase("")) {
                SpannableString s = new SpannableString("Por favor confirma tu contraseña");
                s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                registroSalonDeBellezaEtContrasenhaConfirmar.setError(s);
                correcto = false;
            } else {
                if (!pass.equals(passConfirm)) {
                    SpannableString s = new SpannableString("Las contraseñas no coinciden");
                    s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    registroSalonDeBellezaEtContrasenhaConfirmar.setError(s);
                    correcto = false;
                }
            }

            if (direccion.trim().equalsIgnoreCase("")) {
                SpannableString s = new SpannableString("Por favor ingrese una dirección");
                s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                registroSalonDeBellezaEtDireccion.setError(s);
                correcto = false;
            }

            if (correcto) {

                Geocoder gc = new Geocoder(RegistroSalonDeBelleza.this);

                double latitud = 0, longitud = 0;

                try {
                    List<Address> list = gc.getFromLocationName(direccion + "Cali CO", 1);

                    Address add = list.get(0);

                    String locality = add.getLocality();

                    latitud = add.getLatitude();
                    longitud = add.getLongitude();

                } catch (IOException e) {
                    SpannableString s = new SpannableString("Ingrese una dirección válida");
                    s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    registroSalonDeBellezaEtDireccion.setError(s);
                    correcto = false;
                }

                if (correcto) {
                    final SalonDeBelleza salonDeBelleza = new SalonDeBelleza(nombreDuenho, nombreSalonBelleza, correo, pass, direccion, latitud, longitud);
                    comprobarServiciosEscogidos();
                    modificarServicios(salonDeBelleza);
                    auth.createUserWithEmailAndPassword(correo, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            String serviciosCambinados[] = combinaciones(servicios);
                            rtdb.getReference().child("Salon de belleza").child(nombreSalonBelleza).setValue(salonDeBelleza);
                            subirImagen();
                            rtdb.getReference().child("identificador").child(auth.getCurrentUser().getUid()).setValue("salón de belleza");

                            Marcador marcador = new Marcador(salonDeBelleza.getLatitud(), salonDeBelleza.getLongitud(), salonDeBelleza.getNombreSalonDeBelleza());
                            rtdb.getReference().child("Marcador").child(salonDeBelleza.getNombreSalonDeBelleza()).setValue(marcador);

                            for (int i = 0; i < serviciosCambinados.length; i++) {
                                rtdb.getReference().child("Buscar servicios salon de belleza").child(serviciosCambinados[i]).child(salonDeBelleza.getNombreSalonDeBelleza()).push().setValue(salonDeBelleza.getNombreSalonDeBelleza());
                            }

                            finish();
                        }
                    }).addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException weakPassword) {
                                SpannableString s = new SpannableString("Por favor ingrese otra contraseña");
                                s.setSpan(new RegistroSalonDeBelleza.TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                registroSalonDeBellezaEtContrasenha.setError(s);
                            } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                SpannableString s = new SpannableString("Por favor ingrese otro correo electrónico");
                                s.setSpan(new RegistroSalonDeBelleza.TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                registroSalonDeBellezaEtCorreo.setError(s);
                            } catch (FirebaseAuthUserCollisionException existEmail)  {
                                SpannableString s = new SpannableString("Ya existe un usuario con este correo electrónico");
                                s.setSpan(new RegistroSalonDeBelleza.TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                registroSalonDeBellezaEtCorreo.setError(s);
                            } catch (Exception e) {
                            }
                        }
                    });
                    finish();
                }
            }
        }
        );

        btn_registro_salon_volver.setOnClickListener(v -> finish());
    }

    public void modificarServicios (SalonDeBelleza salonDeBelleza) {

        for (int i = 0; i < servicios.size(); i++) {
            salonDeBelleza.getServicios().put(servicios.get(i), true);
        }

    }

    public void comprobarServiciosEscogidos () {

        if (registroSalonDeBellezaCheckBoxUñas.isChecked())
            servicios.add(registroSalonDeBellezaCheckBoxUñas.getText().toString());

        if (registroSalonDeBellezaCheckBoxMaquillaje.isChecked())
            servicios.add(registroSalonDeBellezaCheckBoxMaquillaje.getText().toString());

        if (registroSalonDeBellezaCheckBoxMasaje.isChecked())
            servicios.add(registroSalonDeBellezaCheckBoxMasaje.getText().toString());

        if (registroSalonDeBellezaCheckBoxDepilacion.isChecked())
            servicios.add(registroSalonDeBellezaCheckBoxDepilacion.getText().toString());

        if (registroSalonDeBellezaCheckBoxPeluqueria.isChecked())
            servicios.add(registroSalonDeBellezaCheckBoxPeluqueria.getText().toString());
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
            final Uri uri = data.getData();
            photoFile = new File(  UtilDomi.getPath(this, uri)  );
            runOnUiThread( () -> {
                ivSalonBelleza.setBackground(null);
                ivSalonBelleza.setImageURI(uri);
            });
        }

    }

    private void subirImagen(){
        StorageReference ref = storage.getReference().child("salones de belleza").child(registroSalonDeBellezaEtNombre.getText().toString()).child("profile");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(photoFile);
            ref.putStream(fis).addOnSuccessListener(taskSnapshot -> cargarFotoPerfil());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void cargarFotoPerfil() {
        StorageReference ref = storage.getReference().child("estilistas").child(auth.getCurrentUser().getUid());
        ref.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(RegistroSalonDeBelleza.this).load(uri).into(ivSalonBelleza));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private class TypefaceSpan extends MetricAffectingSpan {
        private Typeface mTypeface;
        public TypefaceSpan(Typeface typeface) {
            mTypeface = typeface;
        }

        @Override
        public void updateMeasureState(TextPaint p) {
            p.setTypeface(mTypeface);
            p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setTypeface(mTypeface);
            tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }
}
