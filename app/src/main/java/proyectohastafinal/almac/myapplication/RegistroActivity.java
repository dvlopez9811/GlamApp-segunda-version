package proyectohastafinal.almac.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegistroActivity extends AppCompatActivity {

    private Button btn_registro_cliente_estilista;
    private  Button btn_registro_salon_belleza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        btn_registro_cliente_estilista = findViewById(R.id.btn_registro_cliente_estilista);
        btn_registro_salon_belleza = findViewById(R.id.btn_registro_salon_belleza);

        btn_registro_cliente_estilista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistroActivity.this, RegistroCliente.class);
                startActivity(i);
                finish();
            }
        });

        btn_registro_salon_belleza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistroActivity.this, RegistroSalonDeBelleza.class);
                startActivity(i);
                finish();
            }
        });
    }
}
