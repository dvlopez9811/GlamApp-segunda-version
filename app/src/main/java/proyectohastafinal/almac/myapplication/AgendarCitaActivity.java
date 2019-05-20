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

    private int indice;
    private ArrayList<String> idestilistasaptos;

    FirebaseDatabase rtdb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_cita);

        rtdb = FirebaseDatabase.getInstance();


        adapterHorarios = new AdapterHorarios();
        listaHorarios=findViewById(R.id.lista_horarios_disponibles_item_agendar_cita);
        listaHorarios.setLayoutManager(new LinearLayoutManager(this));
        nombreSalon=findViewById(R.id.nombre_salon_agendar_cita_activity);
        sp_estilista_agendarcita = findViewById(R.id.item_agendar_cita_spinner_estilista);


        listaHorarios.setAdapter(adapterHorarios);
        listaHorarios.setHasFixedSize(true);

        String salon = getIntent().getExtras().get("salon").toString();
        nombreSalon.setText(salon);

/*
        String[] arregloservicios = serv.split(" ");

        idestilistasaptos = new ArrayList<>();

        for(int j=0;j<arregloservicios.length;j++) {
            rtdb.getReference().child("Salon de belleza").child(salon).child("Estilistas").child(arregloservicios[j]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot dsp : dataSnapshot.getChildren()){
                        String[] datos = dsp.getValue().toString().split(",");
                        for (int j = 0;j<datos.length;j++){
                            if(!idestilistasaptos.contains(datos[j])) idestilistasaptos.add(datos[j]);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        indice = 0;
        for (indice=0;indice<idestilistas.length;indice++){

            rtdb.getReference().child("Estilista").child(idestilistas[indice]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Estilista estilista = dataSnapshot.getValue(Estilista.class);
                    nombreestilistas[indice] = estilista.getNombreYApellido();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this,android.R.layout.simple_spinner_item,nombreestilistas);


        }
*/


    }

    @Override
    public void onItemClick(Horario horario) {



    }
}
