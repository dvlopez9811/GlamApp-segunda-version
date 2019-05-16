package proyectohastafinal.almac.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class CambiarContrasenhaActivity extends AppCompatActivity {

    private EditText editTextCorreo;
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

        editTextCorreo = findViewById(R.id.et_cambiar_contrasenha_correo);

        editTextPassAnterior = findViewById(R.id.et_cambiar_contrasenha_pass_anterior);

        editTextPassNueva = findViewById(R.id.et_cambiar_contrasenha_pass_nueva);

        btnCambiarPass = findViewById(R.id.btn_cambiar_contraseña_ejecutar);

        btnCambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FirebaseUser user = auth.getCurrentUser();

                String correo = editTextCorreo.getText().toString();
                String passAnterior = editTextPassAnterior.getText().toString();
                final String nuevaPass = editTextPassNueva.getText().toString();

                final AuthCredential authCredential = EmailAuthProvider.getCredential(correo, passAnterior);

                user.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            user.updatePassword(nuevaPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        rtdb.getReference().child("usuario").child(user.getUid()).child("contraseña").setValue(nuevaPass);
                                        Toast.makeText(CambiarContrasenhaActivity.this, "Contraseña actualizada", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(CambiarContrasenhaActivity.this, "Contraseña no actualizada", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(CambiarContrasenhaActivity.this, "Error de autenticación", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });

    }
}
