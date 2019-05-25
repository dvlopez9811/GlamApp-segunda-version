package proyectohastafinal.almac.myapplication;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import proyectohastafinal.almac.myapplication.model.Estilista;
import proyectohastafinal.almac.myapplication.model.Horario;
import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;

public class RegistroEstilista extends AppCompatActivity {

    private static final String [] DIAS_SEMANA  ={"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";

    private CheckBox registroEstilistaCheckBoxUñas;
    private CheckBox registroEstilistaCheckBoxMasaje;
    private CheckBox registroEstilistaCheckBoxMaquillaje;
    private CheckBox registroEstilistaCheckBoxDepilacion;
    private CheckBox registroEstilistaCheckBoxPeluqueria;

    private Spinner spinnerSalonesDeBelleza;
    private Spinner spinnerFechaIncio;
    private Spinner spinnerFechaFinal;

    private Button registro_estilista_btn_listo;

    private Button ibObtenerHoraInicio;
    private Button ibObtenerHoraFinal;

    public EditText etObtenerHoraInicio;
    public EditText etObtenerHoraFinal;

    private String servicios;
    private int dia1;

    private int horainicio;
    private int horafin;



    FirebaseAuth auth;
    FirebaseDatabase rtdb;

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_estilista);

        spinnerSalonesDeBelleza = findViewById(R.id.registro_estilista_spinner_salones_belleza);
        spinnerFechaFinal = findViewById(R.id.registro_estilista_spinner_fecha_final);
        spinnerFechaIncio = findViewById(R.id.registro_estilista_spinner_fecha_inicio);

        registro_estilista_btn_listo = findViewById(R.id.registro_estilista_btn_listo);

        ibObtenerHoraInicio = findViewById(R.id.ib_obtener_hora_inicio);
        etObtenerHoraInicio = findViewById(R.id.et_obtener_hora_inicio);

        ibObtenerHoraInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerHoraInicio();
            }
        });

        ibObtenerHoraFinal = findViewById(R.id.ib_obtener_hora_final);
        etObtenerHoraFinal = findViewById(R.id.et_obtener_hora_final);

        ibObtenerHoraFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerHoraFinal();
            }
        });

        auth = FirebaseAuth.getInstance();
        rtdb = FirebaseDatabase.getInstance();

        registroEstilistaCheckBoxUñas = findViewById(R.id.registro_estilista_check_servicio_uñas);
        registroEstilistaCheckBoxMasaje = findViewById(R.id.registro_estilista_check_servicio_masaje);
        registroEstilistaCheckBoxMaquillaje = findViewById(R.id.registro_estilista_check_servicio_maquillaje);
        registroEstilistaCheckBoxDepilacion = findViewById(R.id.registro_estilista_check_servicio_depilacion);
        registroEstilistaCheckBoxPeluqueria = findViewById(R.id.registro_estilista_check_servicio_peluqueria);


        final String correoEstilista = getIntent().getExtras().getString("correo");
        final String usuarioEstilista = getIntent().getExtras().getString("usuario");
        final String nombreEstilista = getIntent().getExtras().getString("nombre");
        final String telefonoEstilista = getIntent().getExtras().getString("telefono");
        final String passEstilista = getIntent().getExtras().getString("pass");

        final ArrayList<CharSequence> salonesDeBelleza = new ArrayList<>();
        servicios = "";

        rtdb.getReference().child("Salon de belleza").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    salonesDeBelleza.add(childDataSnapshot.getKey());
                    ArrayAdapter<CharSequence> salonesDeBellezaAdapter = new ArrayAdapter<>(RegistroEstilista.this, R.layout.support_simple_spinner_dropdown_item, salonesDeBelleza);
                    spinnerSalonesDeBelleza.setAdapter(salonesDeBellezaAdapter);

                   /* rtdb.getReference().child("Salon de belleza").child(spinnerSalonesDeBelleza.getSelectedItem().toString()).child("servicios").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            HashMap<String, Booleazn> nombreSalonDeBelleza =  (HashMap<String, Boolean>) dataSnapshot.getValue();
                            Log.d("nombreSalonDeBelleza", nombreSalonDeBelleza.keySet().toString() + " ");

                            servicios.addAll(nombreSalonDeBelleza.keySet());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ArrayAdapter<CharSequence> diasDeLaSemana = ArrayAdapter.createFromResource(this, R.array.dias_semana, android.R.layout.simple_spinner_item);
        spinnerFechaIncio.setAdapter(diasDeLaSemana);
        spinnerFechaFinal.setAdapter(diasDeLaSemana);

        registro_estilista_btn_listo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Estilista estilista = new Estilista(correoEstilista,nombreEstilista,usuarioEstilista,telefonoEstilista,passEstilista);

                String diaUno = spinnerFechaIncio.getSelectedItem().toString();
                String diaDos = spinnerFechaFinal.getSelectedItem().toString();

                dia1 = 0;
                int dia2 = 0;

                if (diaUno.equals(diaDos)) {

                } else {
                    for (int i = 0; i < DIAS_SEMANA.length; i++) {

                        if (diaUno.equals(DIAS_SEMANA[i])) {
                            dia1 = (i);
                        }

                        if (diaDos.equals(DIAS_SEMANA[i])){
                            dia2 = (i+1);
                        }
                    }
                }

                final int tamanhoHorarios = dia2-dia1;





                rtdb.getReference().child("Salon de belleza").child(spinnerSalonesDeBelleza.getSelectedItem().toString()).child("nombreSalonDeBelleza").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String nombreSalonDeBelleza = dataSnapshot.getValue(String.class);
                        estilista.setNombreSalonDeBelleza(nombreSalonDeBelleza);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                comprobarServiciosEscogidos();

                auth.createUserWithEmailAndPassword(correoEstilista, passEstilista).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        rtdb.getReference().child("Estilista").child(auth.getCurrentUser().getUid()).setValue(estilista);

                        rtdb.getReference().child("identificador").child(auth.getCurrentUser().getUid()).setValue("estilista");

                        for (int i = 0; i< tamanhoHorarios; i++, dia1++) {
                            Horario ho = new Horario(horainicio,horafin);
                            rtdb.getReference().child("Estilista").child(auth.getCurrentUser().getUid()).child("horarios").child(DIAS_SEMANA[dia1]).setValue(ho);
                        }


                        Log.e(">>>",servicios);
                        String[] serv = servicios.split(" ");

                        for (int i=0;i<serv.length;i++)
                            rtdb.getReference().child("Salon de belleza").child(spinnerSalonesDeBelleza.getSelectedItem().toString()).child("Estilistas").child(serv[i]).child(auth.getCurrentUser().getUid()).setValue(auth.getCurrentUser().getUid());


                        Intent i = new Intent(RegistroEstilista.this,MainEstilistaActivity.class);
                        startActivity(i);
                        finish();

                    }
                });

            }
        });

    }

    public void comprobarServiciosEscogidos () {

        if (registroEstilistaCheckBoxUñas.isChecked()) {
            servicios+="Uñas";
        }

        if (registroEstilistaCheckBoxMaquillaje.isChecked()) {
            servicios+=" Maquillaje";
        }

        if (registroEstilistaCheckBoxMasaje.isChecked()) {
            servicios+=" Masaje";
        }

        if (registroEstilistaCheckBoxDepilacion.isChecked()) {
            servicios+=" Depilación";
        }

        if (registroEstilistaCheckBoxPeluqueria.isChecked()) {
            servicios+=" Peluquería";
        }

        servicios = servicios.trim();

    }

    private void obtenerHoraInicio(){
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                String AM_PM;

                horainicio = hourOfDay;

                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                etObtenerHoraInicio.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
        }, hora, minuto, false);

        recogerHora.show();
    }

    public void obtenerHoraFinal(){
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                String AM_PM;

                horafin = hourOfDay;

                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                etObtenerHoraFinal.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
        }, hora, minuto, false);

        recogerHora.show();
    }
}
