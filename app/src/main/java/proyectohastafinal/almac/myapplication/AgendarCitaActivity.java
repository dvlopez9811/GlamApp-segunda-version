package proyectohastafinal.almac.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.Estilista;
import proyectohastafinal.almac.myapplication.model.Horario;
import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;
import proyectohastafinal.almac.myapplication.model.Servicio;

public class AgendarCitaActivity extends AppCompatActivity implements AdapterHorarios.OnItemClickListener{

    private RecyclerView listaHorarios;
    private AdapterHorarios adapterHorarios;
    private TextView nombreSalon;
    private Spinner sp_estilista_agendarcita;
    private LinearLayout ll_servicios_agendar_cita;
    private Button btn_ver_estilistas_agendar_cita;

    private int indice;

    private ArrayList<String> idestilistasaptos;
    private ArrayList<String> nombreestilistas;

    FirebaseDatabase rtdb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_cita);

        rtdb = FirebaseDatabase.getInstance();


        adapterHorarios = new AdapterHorarios();
        listaHorarios = findViewById(R.id.lista_horarios_disponibles_item_agendar_cita);
        listaHorarios.setLayoutManager(new LinearLayoutManager(this));
        nombreSalon = findViewById(R.id.nombre_salon_agendar_cita_activity);
        sp_estilista_agendarcita = findViewById(R.id.item_agendar_cita_spinner_estilista);
        ll_servicios_agendar_cita = findViewById(R.id.ll_servicios_agendar_cita);

        listaHorarios.setAdapter(adapterHorarios);
        listaHorarios.setHasFixedSize(true);

        final String salon = getIntent().getExtras().get("salon").toString();
        nombreSalon.setText(salon);

        String[] servicios = getIntent().getExtras().get("servicios").toString().split(" ");
        final CheckBox[] checkBoxes = new CheckBox[servicios.length];

        for (int g = 0; g < servicios.length; g++) {

            checkBoxes[g] = new CheckBox(this);
            checkBoxes[g].setText(servicios[g]);
            checkBoxes[g].setChecked(true);
            ll_servicios_agendar_cita.addView(checkBoxes[g]);

        }

        btn_ver_estilistas_agendar_cita = findViewById(R.id.btn_ver_estilistas_agendar_cita);
        btn_ver_estilistas_agendar_cita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String servicios = "";
                for (int j=0;j<checkBoxes.length;j++){
                    if(checkBoxes[j].isChecked())
                        servicios+=checkBoxes[j].getText().toString()+" ";

                }

                servicios = servicios.trim();
                Log.e("<<<",servicios);
                nombreestilistas = new ArrayList<>();


                rtdb.getReference().child("Salon de belleza").child(salon).child("Estilistas").child(servicios).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp: dataSnapshot.getChildren()){

                            String id = dsp.getValue(String.class);
                            Log.e("<<<",id);

                            rtdb.getReference().child("Estilista").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    Estilista estilista = dataSnapshot.getValue(Estilista.class);
                                    nombreestilistas.add(estilista.getNombreYApellido());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (AgendarCitaActivity.this, android.R.layout.simple_spinner_item, nombreestilistas);
                        sp_estilista_agendarcita.setAdapter(adapter);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }



        });


    }
    @Override
    public void onItemClick(Horario horario) {



    }
}
