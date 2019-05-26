package proyectohastafinal.almac.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import proyectohastafinal.almac.myapplication.model.Estilista;
import proyectohastafinal.almac.myapplication.model.Horario;
import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;
import proyectohastafinal.almac.myapplication.model.Servicio;


public class AgendarCitaActivity extends AppCompatActivity implements AdapterHorarios.OnItemClickListener{


    private TextView nombreSalon;
    private Spinner sp_estilista_agendarcita;


    FirebaseDatabase rtdb;
    FirebaseAuth auth;

    private RecyclerView listaServicios;
    private AdapterItemsAgendarCita adapterServicios;

    private Calendar calendario;
    private ArrayList<String> idestilistas;
    private String salon;
    private String idestilista;
    private String dia;
    private Button btn_aceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_cita);
        rtdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        calendario = Calendar.getInstance();


        adapterServicios = new AdapterItemsAgendarCita();
        listaServicios=findViewById(R.id.lista_servicios_disponibles_agendar_cita_activity);
        listaServicios.setLayoutManager(new LinearLayoutManager(this));
        btn_aceptar=findViewById(R.id.btn_aceptar_agendar_cita_activity);
        nombreSalon = findViewById(R.id.nombre_salon_agendar_cita_activity);


        salon = getIntent().getExtras().get("salon").toString();
        nombreSalon.setText(salon);


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
                listaServicios.setHasFixedSize(true);
                adapterServicios.showAllServicios(servicios);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth.getCurrentUser()==null){

                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(v.getContext());
                    dialogo1.setTitle("Registro");
                    dialogo1.setMessage("Por favor inicia sesión");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            Intent i = new Intent(AgendarCitaActivity.this,LoginActivity.class);
                            startActivity(i);
                            finish();

                        }
                    });
                    dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    dialogo1.show();

                }
                else {
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(v.getContext());
                    dialogo1.setTitle("Confirmación");
                    dialogo1.setMessage("¿ Desea crear la cita ?");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                            String idcita = UUID.randomUUID().toString();
                            Cita cita = new Cita();
                          //  Cita cita = new Cita(idcita,Cita.RESERVADA,dia,horario.getHoraFinal(),horario.getHoraInicio(),"",salon,
                            //        sp_servicio_agendarcita.getSelectedItem().toString(), idestilista, auth.getCurrentUser().getUid());
                            rtdb.getReference().child("Citas").child(idcita).setValue(cita);
                            rtdb.getReference().child("usuario").child(auth.getCurrentUser().getUid()).child("citas").child(idcita).setValue(idcita);
                            rtdb.getReference().child("Estilista").child(idestilista).child("citas").child(idcita).setValue(idcita);
                   //         rtdb.getReference().child("Estilista").child(idestilista).child("agenda").child(dia).child("horas").child(horario.getHoraInicio() + "").setValue(true);
                            Toast.makeText(AgendarCitaActivity.this, "Cita enviada al estilista", Toast.LENGTH_LONG).show();
                        }
                    });
                    dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    dialogo1.show();
                }
            }
        });
    }




    @Override
    public void onItemClick(final Horario horario) {


    }
}
