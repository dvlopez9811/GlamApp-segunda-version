package proyectohastafinal.almac.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import proyectohastafinal.almac.myapplication.model.Cita;
import proyectohastafinal.almac.myapplication.model.Horario;
import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;
import proyectohastafinal.almac.myapplication.model.Servicio;


public class AgendarCitaActivity extends AppCompatActivity{


    FirebaseDatabase rtdb;
    FirebaseAuth auth;

    private TextView tv_nombreSalon;
    private RecyclerView listaServicios;
    private AdapterItemsAgendarCita adapterServicios;

    private Button btn_aceptar_agendar_cita_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_cita);
        rtdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        String salon = getIntent().getExtras().get("salon").toString();
        tv_nombreSalon = findViewById(R.id.nombre_salon_agendar_cita_activity);
        tv_nombreSalon.setText(salon);


        listaServicios=findViewById(R.id.lista_servicios_disponibles_agendar_cita_activity);
        listaServicios.setLayoutManager(new LinearLayoutManager(this));
        adapterServicios = new AdapterItemsAgendarCita(salon);

        btn_aceptar_agendar_cita_activity=findViewById(R.id.btn_aceptar_agendar_cita_activity);

        rtdb.getReference().child("Salon de belleza").child(salon).child("servicios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Servicio> servicios=new ArrayList<>();

                for (DataSnapshot dsp: dataSnapshot.getChildren()){
                    if(dsp.getValue(Boolean.class)) {
                        SalonDeBelleza s = new SalonDeBelleza();
                        s.setNombreSalonDeBelleza(salon);
                        Servicio servicioNuevo = new Servicio(dsp.getKey(),s);
                        servicios.add(servicioNuevo);
                    }
                }

                listaServicios.setAdapter(adapterServicios);
                adapterServicios.showAllServicios(servicios);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_aceptar_agendar_cita_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }
}
