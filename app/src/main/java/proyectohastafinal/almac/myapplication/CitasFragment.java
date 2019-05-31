package proyectohastafinal.almac.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import proyectohastafinal.almac.myapplication.model.Cita;
import proyectohastafinal.almac.myapplication.model.Cliente;
import proyectohastafinal.almac.myapplication.model.Estilista;

public class CitasFragment extends Fragment implements AdapterCitas.OnItemClickListener{

    private static CitasFragment instance;
    FirebaseDatabase rtdb;
    FirebaseAuth auth;

    private AdapterCitas adapterCitas;
    private RecyclerView lista_citas;

    private TextView txt_iniciar_sesion_fragment_citas;
    private Button btn_iniciar_sesion_fragment_citas;

    private Cita citaseleccionada;

    Calendar calendario;

    public static CitasFragment getInstance(){
        instance = instance == null ? new CitasFragment() : instance;
        return instance;
    }

    public CitasFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_citas, container, false);

        auth = FirebaseAuth.getInstance();

        calendario = Calendar.getInstance();

        txt_iniciar_sesion_fragment_citas = v.findViewById(R.id.txt_iniciar_sesion_fragment_citas);
        btn_iniciar_sesion_fragment_citas = v.findViewById(R.id.btn_iniciar_sesion_fragment_citas);



        btn_iniciar_sesion_fragment_citas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),InicioActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        if(auth.getCurrentUser() == null){
            return v;
        }

        txt_iniciar_sesion_fragment_citas.setVisibility(TextView.INVISIBLE);
        btn_iniciar_sesion_fragment_citas.setVisibility(TextView.INVISIBLE);

        rtdb = FirebaseDatabase.getInstance();

        lista_citas = v.findViewById(R.id.lista_citas);
        adapterCitas = new AdapterCitas();
        adapterCitas.setListener(this);
        lista_citas.setLayoutManager(new LinearLayoutManager(v.getContext()));
        lista_citas.setAdapter(adapterCitas);
        lista_citas.setHasFixedSize(true);

        rtdb.getReference().child("usuario").child(auth.getCurrentUser().getUid()).child("citas").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<String> idcitas = new ArrayList<>();

                for (DataSnapshot hijo:dataSnapshot.getChildren())
                    idcitas.add(hijo.getValue().toString());

                int dia = calendario.get(Calendar.DAY_OF_MONTH)-1;
                int mes = calendario.get(Calendar.MONTH) + 1;
                int anio = calendario.get(Calendar.YEAR);
                int hora = calendario.get(Calendar.HOUR_OF_DAY);

                for (int i=0;i<idcitas.size();i++) {

                    rtdb.getReference().child("Citas").child(idcitas.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Cita cita = (dataSnapshot.getValue(Cita.class));

                            String[] fecha = cita.getFecha().split("-");
                            int diacita = Integer.parseInt(fecha[2]);
                            int mescita = Integer.parseInt(fecha[1]);
                            int anocita = Integer.parseInt(fecha[0]);
                            int horacita = cita.getHorainicio();


                            if (anio > anocita || (anio == anocita && mes > mescita) || (anio == anocita && mes == mescita && dia > diacita) ||
                                    (anio == anocita && mes == mescita && dia == diacita && hora >= horacita)) {

                                /*
                               new Thread(() -> {

                                    new ServiceManager.BorrarCitas(idcita, new ServiceManager.BorrarCitas.OnResponseListener() {
                                        @Override
                                        public void onResponse(String response) {

                                        }
                                    });

                                    new ServiceManager.BorrarCitasUsuario(cita.getIdUsuario(),idcita, new ServiceManager.BorrarCitasUsuario.OnResponseListener() {
                                        @Override
                                        public void onResponse(String response) {

                                        }
                                    });

                                    new ServiceManager.BorrarCitasEstilista(cita.getIdEstilista(),idcita, new ServiceManager.BorrarCitasEstilista.OnResponseListener() {
                                        @Override
                                        public void onResponse(String response) {

                                        }
                                    });

                                    new ServiceManager.BorrarHorarioEstilista(cita.getIdEstilista(),cita.getFecha(),cita.getHorainicio(), new ServiceManager.BorrarHorarioEstilista.OnResponseListener() {
                                        @Override
                                        public void onResponse(String response) {

                                        }
                                    });

                                }).start();
                                */

                            } else
                                adapterCitas.agregarcita(cita);

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

        return v;
    }

    @Override
    public void onItemClick(View v,Cita cita) {
        getActivity().registerForContextMenu(v);
        getActivity().openContextMenu(v);
        citaseleccionada = cita;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.context_menu_citas,menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.envio_mensaje_estilista_cita:

                rtdb.getReference().child("Estilista").child(citaseleccionada.getIdEstilista()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Estilista estilista = dataSnapshot.getValue(Estilista.class);

                        //Vamos a abrir la ventana de chat
                        Intent i = new Intent(getActivity(),ChatActivity.class);
                        i.putExtra("telEstilista", estilista.getTelefono());
                        startActivity(i);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




                break;

        }

        return super.onContextItemSelected(item);
    }

}
