package proyectohastafinal.almac.myapplication;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import proyectohastafinal.almac.myapplication.model.Cita;

import static proyectohastafinal.almac.myapplication.model.Constantes.*;

public class CitasSalonFragment extends Fragment {

    private static  CitasSalonFragment instance;
    private ArrayList<Cita> citas;
    private AdapterCitas adapterCitas;
    private RecyclerView recyclerView;

    private String nombreSalon;

    FirebaseAuth auth;
    FirebaseDatabase rtdb;

    public static CitasSalonFragment getInstance(){
        instance = instance == null ? new CitasSalonFragment():instance;
        return instance;
    }


    public CitasSalonFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rtdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        citas = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_citas_salon, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_citas_salones);
        citas = new ArrayList<>();
        rtdb.getReference().child(RAMA_IDENTIFICADORES).child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nombreSalon = dataSnapshot.getValue(String.class);

                rtdb.getReference().child(RAMA_CITAS).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long numeroCitas = dataSnapshot.getChildrenCount();
                        int contador = 0;
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            Cita cita = childDataSnapshot.getValue(Cita.class);
                            if(cita.getNombreSalon().equals(nombreSalon))
                                citas.add(cita);
                            contador++;
                            if(numeroCitas == contador) adapter();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    public void adapter(){

        Collections.sort(citas);

        Calendar calendarioActual = Calendar.getInstance();
        ArrayList<Cita> citasAux = new ArrayList<>();

        for (int i = 0; i < citas.size(); i++) {
            Cita cita = citas.get(i);
            String[] fechaCita = cita.getFecha().split("-");
            Calendar calendarioCita = new GregorianCalendar(Integer.parseInt(fechaCita[0]), Integer.parseInt(fechaCita[1]) - 1, Integer.parseInt(fechaCita[2]), cita.getHorainicio(), 0, 0);
            if (calendarioCita.getTimeInMillis() < calendarioActual.getTimeInMillis()) {

            } else {
                if (calendarioActual.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(fechaCita[2])) {
                } else {
                    long diff = calendarioCita.getTime().getTime() - calendarioActual.getTime().getTime();
                    long diferencia = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    if (i == 0) {
                        cita.setInformacion("PRÓXIMO");
                        cita.setCabecera(cita.getDia() + " " + diferencia);
                    } else if (Integer.parseInt((citas.get(i - 1).getFecha().split("-"))[2]) != Integer.parseInt((cita.getFecha().split("-"))[2])) {
                        cita.setInformacion("PRÓXIMO");
                        cita.setCabecera(cita.getDia() + " " + diferencia);
                    }
                    citasAux.add(citas.get(i));
                }
            }
        }

        adapterCitas = new AdapterCitas(getContext(),citasAux, AdapterCitas.SALON);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterCitas);
        recyclerView.setHasFixedSize(true);
    }
}
