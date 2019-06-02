package proyectohastafinal.almac.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import proyectohastafinal.almac.myapplication.model.Cita;
import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;
import proyectohastafinal.almac.myapplication.model.ServiceManager;
import proyectohastafinal.almac.myapplication.model.Servicio;

public class CitasEstilistaFragment extends Fragment implements AdapterCitasEstilista.OnItemClickListener{

    private static CitasEstilistaFragment instance;

    private AdapterCitasEstilista adapterCitasEstilista;
    private RecyclerView lista_citas_estilista;
    private ArrayList <Cita> citas;

    FirebaseDatabase rtdb;
    FirebaseAuth auth;

    Calendar calendario;

    public static CitasEstilistaFragment getInstance(){
        instance = instance == null ? new CitasEstilistaFragment() : instance;
        return instance;
    }

    public CitasEstilistaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rtdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        calendario = Calendar.getInstance();

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_citas_estilista, container, false);

        lista_citas_estilista = v.findViewById(R.id.lista_citas_estilista);
        adapterCitasEstilista = new AdapterCitasEstilista();
        adapterCitasEstilista.setListener(this);
        lista_citas_estilista.setLayoutManager(new LinearLayoutManager(v.getContext()));
        //lista_citas_estilista.setAdapter(adapterCitasEstilista);
        //lista_citas_estilista.setHasFixedSize(true);

        rtdb.getReference().child("Estilista").child(auth.getCurrentUser().getUid()).child("citas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                citas = new ArrayList<>();
                int dia = calendario.get(Calendar.DAY_OF_MONTH);
                int mes = calendario.get(Calendar.MONTH) + 1;
                int anio = calendario.get(Calendar.YEAR);
                int hora = calendario.get(Calendar.HOUR_OF_DAY);

                for (DataSnapshot dsp: dataSnapshot.getChildren()){
                    String idcita  = dsp.getValue(String.class);
                    rtdb.getReference().child("Citas").child(idcita).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Cita cita = dataSnapshot.getValue(Cita.class);

                            String[] fecha = cita.getFecha().split("-");
                            int diacita = Integer.parseInt(fecha[2]);
                            int mescita = Integer.parseInt(fecha[1]);
                            int anocita = Integer.parseInt(fecha[0]);
                            int horacita = cita.getHorainicio();

                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                Log.d("CITA", diacita + " " + mescita + " " + anocita);
                                if (dia == diacita && mes == mescita && anio == anocita) {
                                    Log.d("PASO", "PASO");
                                    citas.add(cita);
                                }
                            }

                            lista_citas_estilista.setAdapter(adapterCitasEstilista);
                            lista_citas_estilista.setHasFixedSize(true);
                            adapterCitasEstilista.showAllCitas(citas);


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
    public void onItemClick(Cita cita) {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
        dialogo1.setTitle("Cita");
        dialogo1.setMessage("¿ Esta seguro de cancelar la cita ?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Toast.makeText(getActivity(),"Está en desarrollo",Toast.LENGTH_LONG).show();

            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
    }
}
