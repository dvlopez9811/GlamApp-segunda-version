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


public class AgendarCitaActivity extends AppCompatActivity implements AdapterHorarios.OnItemClickListener{

    private static final String[] DIAS = new String[]{"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};

    private RecyclerView listaHorarios;
    private AdapterHorarios adapterHorarios;
    private TextView tv_nombreSalon;
    private Spinner sp_servicio_agendarcita;
    private Spinner sp_estilista_agendarcita;
    private RadioGroup rg_hoyomanana_agendarcita;
    private RadioButton rb_hoy_agendarcita;

    FirebaseDatabase rtdb;
    FirebaseAuth auth;

    private Calendar calendario;

    private ArrayList<String> servicios;
    private ArrayList<String> idestilistas;
    private String salon;
    private String idestilista;
    private String dia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_cita);

        rtdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        calendario = Calendar.getInstance();

        adapterHorarios = new AdapterHorarios();
        adapterHorarios.setListener(this);
        listaHorarios = findViewById(R.id.lista_horarios_disponibles_item_agendar_cita);
        listaHorarios.setLayoutManager(new LinearLayoutManager(this));
        listaHorarios.setAdapter(adapterHorarios);
        listaHorarios.setHasFixedSize(true);

        tv_nombreSalon = findViewById(R.id.nombre_salon_agendar_cita_activity);
        sp_servicio_agendarcita = findViewById(R.id.sp_servicio_agendar_cita);
        sp_estilista_agendarcita = findViewById(R.id.sp_estilista_agendar_cita);
        rg_hoyomanana_agendarcita = findViewById(R.id.rg_hoyomanana_agendarcita);
        rb_hoy_agendarcita = findViewById(R.id.rb_hoy_agendarcita);


        listaHorarios.setAdapter(adapterHorarios);
        listaHorarios.setHasFixedSize(true);

        salon = getIntent().getExtras().get("salon").toString();
        tv_nombreSalon.setText(salon);


        rtdb.getReference().child("Salon de belleza").child(salon).child("servicios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                servicios = new ArrayList<>();
                for (DataSnapshot dsp: dataSnapshot.getChildren()){
                    if(dsp.getValue(Boolean.class)) {
                        servicios.add(dsp.getKey());
                    }

                }

                darEstilistas(servicios.get(0));

                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (AgendarCitaActivity.this, android.R.layout.simple_spinner_item, servicios);
                sp_servicio_agendarcita.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sp_servicio_agendarcita.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    darEstilistas(servicios.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        sp_estilista_agendarcita.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                idestilista = idestilistas.get(position);

                if(rb_hoy_agendarcita.isChecked()) {
                    dia = DIAS[calendario.get(Calendar.DAY_OF_WEEK) - 1];
                    darHorarios(true);
                }else{
                    if(DIAS[calendario.get(Calendar.DAY_OF_WEEK)-1].equals(DIAS[6]))
                        dia = DIAS[0];
                    else
                        dia = DIAS[calendario.get(Calendar.DAY_OF_WEEK)];

                    darHorarios(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rg_hoyomanana_agendarcita.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){

                    case R.id.rb_hoy_agendarcita:

                    dia = DIAS[calendario.get(Calendar.DAY_OF_WEEK) - 1];
                    darHorarios(true);


                        break;

                    case R.id.rb_manana_agendarcita:

                        if(DIAS[calendario.get(Calendar.DAY_OF_WEEK)-1].equals(DIAS[6]))
                            dia = DIAS[0];
                        else
                            dia = DIAS[calendario.get(Calendar.DAY_OF_WEEK)];

                        darHorarios(false);

                        break;
                }


            }
        });

    }


    public void darEstilistas(String servicio){

        rtdb.getReference().child("Salon de belleza").child(salon).child("Estilistas").child(servicio).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                idestilista = "";
                idestilistas = new ArrayList<>();
                final ArrayList<String> nombreestilistas = new ArrayList<>();

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    String id = dsp.getValue(String.class);
                    idestilistas.add(id);

                    rtdb.getReference().child("Estilista").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Estilista estilista = dataSnapshot.getValue(Estilista.class);
                            nombreestilistas.add(estilista.getNombreYApellido());

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (AgendarCitaActivity.this, android.R.layout.simple_spinner_item, nombreestilistas);
                            sp_estilista_agendarcita.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void darHorarios(final boolean hoy){


        rtdb.getReference().child("Estilista").child(idestilista).child("horarios").child(dia).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                final Horario horario = dataSnapshot.getValue(Horario.class);


                if (horario != null) {

                    rtdb.getReference().child("Estilista").child(idestilista).child("agenda").child(dia).child("horas").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            ArrayList<Integer> horasagenda = new ArrayList<>();
                            ArrayList<Horario> horarios = new ArrayList<>();

                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                horasagenda.add(Integer.parseInt(dsp.getKey()));
                            }

                            int horainicial = horario.getHoraInicio();

                            if(hoy) {

                                final int hora = calendario.get(Calendar.HOUR_OF_DAY);

                                if (hora < (horario.getHoraFinal() - 1) && hora > horainicial) {
                                    horainicial = hora + 1;
                                    mostrarHorariosHoyoManana(horasagenda,horainicial,horarios,horario);
                                }
                                else
                                    Toast.makeText(AgendarCitaActivity.this, "No hay horarios disponibles hoy", Toast.LENGTH_LONG).show();
                            }else
                                     mostrarHorariosHoyoManana(horasagenda,horainicial,horarios,horario);


                            adapterHorarios.showAllHorarios(horarios);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {

                    adapterHorarios.showAllHorarios(new ArrayList<Horario>());

                    if (!idestilista.isEmpty()) {

                        if (hoy)
                            Toast.makeText(AgendarCitaActivity.this, "El estilista no trabaja hoy", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(AgendarCitaActivity.this, "El estilista no trabaja mañana", Toast.LENGTH_LONG).show();

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void mostrarHorariosHoyoManana(ArrayList<Integer> horasagenda, int horainicial,ArrayList<Horario> horarios,Horario horario){
        if (horasagenda.size() != 0) {

            boolean cont = true;

            for (int j = horainicial; j < horario.getHoraFinal() && cont; j++) {

                for (int k = 0; k < horasagenda.size(); k++) {

                    if (j == horasagenda.get(k))
                        break;
                    else if (j < horasagenda.get(k)) {
                        horarios.add(new Horario(j, j + 1));
                        break;
                    } else if (k == horasagenda.size() - 1) {
                        while (j < horario.getHoraFinal()) {
                            horarios.add(new Horario(j, j + 1));
                            j++;
                        }
                        cont = false;
                        break;
                    }

                }


            }
        }else {
            for (int j = horainicial; j < horario.getHoraFinal(); j++) {
                horarios.add(new Horario(j, j + 1));
            }
        }
    }

    @Override
    public void onItemClick(final Horario horario) {

        if(auth.getCurrentUser()==null){

            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
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
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Confirmación");
            dialogo1.setMessage("¿ Desea crear la cita ?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                    String idcita = UUID.randomUUID().toString();


            Cita cita = new Cita(idcita,Cita.RESERVADA,dia,horario.getHoraFinal(),horario.getHoraInicio(),"",salon,
                    sp_servicio_agendarcita.getSelectedItem().toString(), idestilista, auth.getCurrentUser().getUid());


                    rtdb.getReference().child("Citas").child(idcita).setValue(cita);
                    rtdb.getReference().child("usuario").child(auth.getCurrentUser().getUid()).child("citas").child(idcita).setValue(idcita);
                    rtdb.getReference().child("Estilista").child(idestilista).child("citas").child(idcita).setValue(idcita);
                    rtdb.getReference().child("Estilista").child(idestilista).child("agenda").child(dia).child("horas").child(horario.getHoraInicio() + "").setValue(true);
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
}
