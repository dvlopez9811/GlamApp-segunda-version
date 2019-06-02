package proyectohastafinal.almac.myapplication;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CambiarContrasenhaActivity extends AppCompatActivity {

    private EditText editTextConfirmarPass;
    private EditText editTextPassAnterior;
    private EditText editTextPassNueva;

    private Button btnCambiarPass;

    FirebaseAuth auth;
    FirebaseDatabase rtdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasenha);

        auth = FirebaseAuth.getInstance();
        rtdb = FirebaseDatabase.getInstance();

        editTextConfirmarPass = findViewById(R.id.et_confirmar_nueva_cambiar_activity);
        editTextPassAnterior = findViewById(R.id.et_contrasena_actual_cambiar_activity);
        editTextPassNueva = findViewById(R.id.et_nueva_contrasena_cambiar_activity);
        btnCambiarPass = findViewById(R.id.btn_cambiar_contrasena_aceptar);
        btnCambiarPass.setOnClickListener(v -> {
            final FirebaseUser user = auth.getCurrentUser();
            String pass_actual = editTextPassAnterior.getText().toString();
            String pass_nueva = editTextPassNueva.getText().toString();
            String confirmacion_pass = editTextConfirmarPass.getText().toString();
            boolean correcto = true;

            if (pass_actual.trim().equalsIgnoreCase("")) {
                SpannableString s = new SpannableString("Por favor ingrese su contraseña actual");
                s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                editTextPassAnterior.setError(s);
                correcto = false;
            }

            if (pass_nueva.trim().equalsIgnoreCase("")) {
                SpannableString s = new SpannableString("Por favor ingrese nueva contraseña");
                s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                editTextPassNueva.setError(s);
                correcto = false;
            }else if(pass_nueva.trim().length()<6){
                SpannableString s = new SpannableString("La contraseña debe tener al menos 6 caracteres");
                s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                editTextPassNueva.setError(s);
                correcto = false;
            }
            if (pass_actual.trim().equalsIgnoreCase("")) {
                SpannableString s = new SpannableString("Por favor confirme nueva contraseña");
                s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                editTextConfirmarPass.setError(s);
                correcto = false;
            }

            if (!pass_nueva.equals(confirmacion_pass)) {
                SpannableString s = new SpannableString("Las contraseñas no coinciden");
                s.setSpan(new TypefaceSpan(ResourcesCompat.getFont(v.getContext(), R.font.josefin_sans)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                editTextConfirmarPass.setError(s);
                correcto = false;
            }

            if(correcto) {

                rtdb.getReference().child("identificador").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String tipo= dataSnapshot.getValue(String.class);
                        String nTipo="";
                        String identificadorBase="";

                        if(tipo.equals("estilista")){nTipo="Estilista";identificadorBase=user.getUid();}
                        else if(tipo.equals("cliente")){ nTipo="usuario";identificadorBase=user.getUid();}
                        else{ nTipo="Salon de belleza";identificadorBase=tipo;}

                        final String nTipofinal=nTipo;
                        final String identificador=identificadorBase;

                            rtdb.getReference().child(nTipo).child(identificadorBase).child("correo").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String correo = dataSnapshot.getValue(String.class);
                                    final AuthCredential authCredential = EmailAuthProvider.getCredential(correo, pass_actual);
                                    user.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                user.updatePassword(pass_nueva).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                                rtdb.getReference().child(nTipofinal).child(identificador).child("contrasenha").setValue(pass_nueva);
                                                            Toast.makeText(CambiarContrasenhaActivity.this, "Contraseña actualizada", Toast.LENGTH_LONG).show();
                                                            editTextConfirmarPass.setText("");
                                                            editTextPassAnterior.setText("");
                                                            editTextPassNueva.setText("");
                                                        } else {
                                                            try {
                                                                throw task.getException();
                                                            } catch (FirebaseAuthWeakPasswordException weakPassword) {
                                                                Toast.makeText(CambiarContrasenhaActivity.this, "Su contraseña es muy débil", Toast.LENGTH_LONG).show();
                                                            } catch (Exception e) {
                                                                Toast.makeText(CambiarContrasenhaActivity.this, "Error cambiando contraseña", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(CambiarContrasenhaActivity.this, "Error de autenticación", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });





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

