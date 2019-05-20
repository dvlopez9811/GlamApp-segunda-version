package proyectohastafinal.almac.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.Estilista;
import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;
import proyectohastafinal.almac.myapplication.model.Servicio;

public class AgendarCitaActivity extends AppCompatActivity {

    private RecyclerView listaServicios;
    private AdapterItemsAgendarCita adapterServicios;
    private Button btn_aceptar;
    private TextView nombreSalon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_cita);


        adapterServicios = new AdapterItemsAgendarCita();
        listaServicios=findViewById(R.id.lista_servicios_disponibles_agendar_cita_activity);
        listaServicios.setLayoutManager(new LinearLayoutManager(this));
        btn_aceptar=findViewById(R.id.btn_aceptar_agendar_cita_activity);
        nombreSalon=findViewById(R.id.nombre_salon_agendar_cita_activity);

        // TODO Estas instancias son para prueba
        SalonDeBelleza prueba = new SalonDeBelleza("Piedrahita","Icesi",
                "icesi@gmail.com","123456","Cl 18 #122-55",3.342045,-76.53098489999999);
        Estilista estprueba= new Estilista();
        estprueba.setNombreYApellido("Manuel Coral");
        ArrayList<Estilista> estilistas = new ArrayList<>();
        estilistas.add(estprueba);
        prueba.setEstilistasArrayList(estilistas);
        Servicio maquillaje = new Servicio("Maquillaje",prueba);
        maquillaje.setEstilistasQueLoPrestan(estilistas);
        ArrayList<Servicio> servicios = new ArrayList<>();
        servicios.add(maquillaje);

        listaServicios.setAdapter(adapterServicios);
        listaServicios.setHasFixedSize(true);
        adapterServicios.showAllServicios(servicios);

        nombreSalon.setText(prueba.getNombreSalonDeBelleza());

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AgendarCitaActivity.this, "Implementar", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
