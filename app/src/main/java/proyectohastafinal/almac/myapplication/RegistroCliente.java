package proyectohastafinal.almac.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        registroEstilistaClienteBtnRegistrarme = findViewById(R.id.registro_estilista_cliente_btn_registrarme);

        registroEstilistaClienteBtnRegistrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String pass = registroEstilistaClienteEtContrasenha.getText().toString();
                String passConfirm = registroEstilistaClienteEtContrasenhaConfirmar.getText().toString();

                if (pass.length() < 6) {
                    Toast.makeText(RegistroCliente.this, "La contraseña debe tener por lo menos 6 caracteres", Toast.LENGTH_LONG).show();
                } else {
                    if (pass.equals(passConfirm)) {

                        final String correo = registroEstilistaClienteEtCorreo.getText().toString();
                        final String usuario = registroEstilistaClienteEtUsuario.getText().toString();
                        final String nombre = registroEstilistaClienteEtNombreYApellido.getText().toString();
                        final String telefono = registroEstilistaClienteEtTelefono.getText().toString();

                        final Cliente cl = new Cliente(correo, usuario,nombre,telefono,pass);

                        if (registroEstilistaClienteCheckBoxEstilista.isChecked()) {
                            Intent i = new Intent(RegistroCliente.this, RegistroEstilista.class);
                            i.putExtra("correo",correo).putExtra("usuario", usuario).putExtra("nombre",nombre).putExtra("telefono",telefono).putExtra("pass",pass);
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
                    } else {
                        Toast.makeText(RegistroCliente.this, "Las contraseñas no son iguales", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }
}
