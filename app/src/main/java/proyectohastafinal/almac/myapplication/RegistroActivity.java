package proyectohastafinal.almac.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegistroActivity extends AppCompatActivity {

    private Button btn_registro_cliente_estilista;
    private Button btn_registro_salon_belleza;
    private Button btn_volver_registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        btn_registro_cliente_estilista = findViewById(R.id.btn_registro_cliente_estilista);
        btn_registro_salon_belleza = findViewById(R.id.btn_registro_salon_belleza);
        btn_volver_registro = findViewById(R.id.btn_volver_registro);

        btn_registro_cliente_estilista.setOnClickListener(v -> {
            Intent i = new Intent(RegistroActivity.this, RegistroCliente.class);
            startActivity(i);
        });

        btn_registro_salon_belleza.setOnClickListener(v -> {
            Intent i = new Intent(RegistroActivity.this, RegistroSalonDeBelleza.class);
            startActivity(i);
        });

        btn_volver_registro.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
