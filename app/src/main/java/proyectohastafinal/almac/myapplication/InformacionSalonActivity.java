package proyectohastafinal.almac.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class InformacionSalonActivity extends AppCompatActivity {


    private Button btn_agendar_cita;

    FirebaseDatabase rtdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_salon);


        rtdb = FirebaseDatabase.getInstance();



        btn_agendar_cita=findViewById(R.id.btn_agendar_cita_info_salon_activity);
        btn_agendar_cita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(InformacionSalonActivity.this,AgendarCitaActivity.class);
                i.putExtra("salon",getIntent().getExtras().get("salon").toString());
                startActivity(i);
            }
        });

    }
}
