package proyectohastafinal.almac.myapplication;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import proyectohastafinal.almac.myapplication.model.Cliente;

public class RegistroCliente extends AppCompatActivity {

    private EditText registroEstilistaClienteEtCorreo;
    private EditText registroEstilistaClienteEtUsuario;
    private EditText registroEstilistaClienteEtNombreYApellido;
    private EditText registroEstilistaClienteEtTelefono;
    private EditText registroEstilistaClienteEtContrasenha;
    private EditText registroEstilistaClienteEtContrasenhaConfirmar;
    private CheckBox registroEstilistaClienteCheckBoxEstilista;

    private Button registroEstilistaClienteBtnRegistrarme;
    private Button btn_registro_cliente_volver;

    FirebaseAuth auth;
    FirebaseDatabase rtdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);

        auth = FirebaseAuth.getInstance();
        rtdb = FirebaseDatabase.getInstance();

        registroEstilistaClienteEtCorreo = findViewById(R.id.registro_estilista_cliente_et_correo);
        registroEstilistaClienteEtUsuario = findViewById(R.id.registro_estilista_cliente_et_nombre);
        registroEstilistaClienteEtNombreYApellido = findViewById(R.id.registro_estilista_cliente_et_apellido);
        registroEstilistaClienteEtTelefono = findViewById(R.id.id_registro_estilista_cliente_et_telefono);
        registroEstilistaClienteEtContrasenha = findViewById(R.id.registro_estilista_cliente_et_contrasenha);
        registroEstilistaClienteEtContrasenhaConfirmar = findViewById(R.id.registro_estilista_cliente_et_confimar_contrasenha);
        registroEstilistaClienteCheckBoxEstilista = findViewById(R.id.registro_estilista_cliente_check_box_estilista);
        registroEstilistaClienteCheckBoxEstilista.setTypeface(ResourcesCompat.getFont(this, R.font.josefin_sans));
        registroEstilistaClienteBtnRegistrarme = findViewById(R.id.registro_estilista_cliente_btn_registrarme);
        btn_registro_cliente_volver = findViewById(R.id.btn_registro_cliente_volver);

        registroEstilistaClienteBtnRegistrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean correcto = true;

                String correo = registroEstilistaClienteEtCorreo.getText().toString();
                String usuario = registroEstilistaClienteEtUsuario.getText().toString();
                String nombre = registroEstilistaClienteEtNombreYApellido.getText().toString();
                String telefono = registroEstilistaClienteEtTelefono.getText().toString();
                String pass = registroEstilistaClienteEtContrasenha.getText().toString();
                String passConfirm = registroEstilistaClienteEtContrasenhaConfirmar.getText().toString();

                if (correo.trim().equalsIgnoreCase("")) {
                    SpannableString s = new SpannableString("Por favor ingrese un correo electrónico");
                    s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    registroEstilistaClienteEtCorreo.setError(s);
                    correcto = false;
                } else if (!correo.trim().contains("@") | !correo.trim().contains(".")) {
                    SpannableString s = new SpannableString("Debe ser un correo electrónico válido");
                    s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    registroEstilistaClienteEtCorreo.setError(s);
                    correcto = false;
                }

                if (usuario.trim().equalsIgnoreCase("")) {
                    SpannableString s = new SpannableString("Por favor ingrese un nombre de usuario");
                    s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    registroEstilistaClienteEtUsuario.setError(s);
                    correcto = false;
                } else if (usuario.contains(" ") | usuario.contains("/")) {
                    SpannableString s = new SpannableString("El nombre de usuario no puede contener espacios ni caracteres especiales");
                    s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    registroEstilistaClienteEtUsuario.setError(s);
                    correcto = false;
                }

                if (nombre.trim().equalsIgnoreCase("") | !nombre.contains("")) {
                    SpannableString s = new SpannableString("Por favor ingresa tu nombre y apellido");
                    s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    registroEstilistaClienteEtNombreYApellido.setError(s);
                    correcto = false;
                }

                if (!telefono.trim().equalsIgnoreCase("")) {
                    try {
                        int numero = Integer.parseInt(telefono);
                    } catch (NumberFormatException e) {
                        SpannableString s = new SpannableString("Por favor ingresa un número de teléfono válido");
                        s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        registroEstilistaClienteEtTelefono.setError(s);
                        correcto = false;
                    }

                }

                if (pass.trim().equalsIgnoreCase("")) {
                    SpannableString s = new SpannableString("Por favor ingresa una contraseña");
                    s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    registroEstilistaClienteEtContrasenha.setError(s);
                    correcto = false;
                } else if (pass.length() < 6) {
                    SpannableString s = new SpannableString("La contraseña debe tener por lo menos 6 caracteres");
                    s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    registroEstilistaClienteEtContrasenha.setError(s);
                    correcto = false;
                }

                if (passConfirm.equalsIgnoreCase("")) {
                    SpannableString s = new SpannableString("Por favor confirma tu contraseña");
                    s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    registroEstilistaClienteEtContrasenhaConfirmar.setError(s);
                    correcto = false;
                } else {
                    if (!pass.equals(passConfirm)) {
                        SpannableString s = new SpannableString("Las contraseñas no coinciden");
                        s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        registroEstilistaClienteEtContrasenhaConfirmar.setError(s);
                        correcto = false;
                    }
                }

                if (correcto) {

                    final Cliente cl = new Cliente(correo, usuario, nombre, telefono, pass);

                    if (registroEstilistaClienteCheckBoxEstilista.isChecked()) {
                        Intent i = new Intent(RegistroCliente.this, RegistroEstilista.class);
                        i.putExtra("correo", correo).putExtra("usuario", usuario).putExtra("nombre", nombre).putExtra("telefono", telefono).putExtra("pass", pass);
                        startActivity(i);
                    } else {
                        auth.createUserWithEmailAndPassword(correo, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                rtdb.getReference().child("usuario").child(auth.getCurrentUser().getUid()).setValue(cl);
                                Intent i = new Intent(RegistroCliente.this, MainActivity.class);
                                startActivity(i);
                                finish();

                                rtdb.getReference().child("identificador").child(auth.getCurrentUser().getUid()).setValue("cliente");
                            }
                        });
                    }
                }
            }
        });

        btn_registro_cliente_volver.setOnClickListener(v -> finish());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
