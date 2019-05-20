package proyectohastafinal.almac.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InformacionSalonActivity extends AppCompatActivity {


    private Button btn_agendar_cita;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_salon);

        btn_agendar_cita=findViewById(R.id.btn_agendar_cita_info_salon_activity);

        btn_agendar_cita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InformacionSalonActivity.this,AgendarCitaActivity.class);
                startActivity(i);
            }
        });

    }
}
