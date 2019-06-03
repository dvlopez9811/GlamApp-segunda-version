package proyectohastafinal.almac.myapplication;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

import proyectohastafinal.almac.myapplication.model.Cita;
import proyectohastafinal.almac.myapplication.model.Cliente;
import proyectohastafinal.almac.myapplication.model.Estilista;

public class MensajesEstilistaFragment extends Fragment implements AdapterMensajesEstilista.OnItemClickListener{

    private static MensajesEstilistaFragment instance;

    FirebaseDatabase rtdb;
    FirebaseAuth auth;

    private String telefonopropio;
    private String usuarioEstilista;
    ArrayList<Cita> citasEstilista;

    private AdapterMensajesEstilista adapterMensajesEstilista;
    private RecyclerView listamensajesEstilista;

    public static MensajesEstilistaFragment getInstance(){
        instance = instance == null ? new MensajesEstilistaFragment() : instance;
        return instance;
    }

    public MensajesEstilistaFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_mensajes_estilista, container, false);

        ActivityCompat.requestPermissions(getActivity(),new String[]{
                Manifest.permission.CALL_PHONE,
        },0);

        auth = FirebaseAuth.getInstance();
        rtdb = FirebaseDatabase.getInstance();

        listamensajesEstilista = v.findViewById(R.id.lista_mensajes_estilista);


        citasEstilista = new ArrayList<>();
        rtdb.getReference().child("Estilista").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Estilista estilista = dataSnapshot.getValue(Estilista.class);
                Long[] size = new Long[1];
                size[0] = dataSnapshot.getChildrenCount();

                for (Map.Entry<String, String> idcita : estilista.getCitas().entrySet()) {
                    rtdb.getReference().child("Citas").child(idcita.getValue()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Cita cita = dataSnapshot.getValue(Cita.class);
                            String[] fechaCita = cita.getFecha().split("-");
                            Calendar calendarioCita = new GregorianCalendar(Integer.parseInt(fechaCita[0]), Integer.parseInt(fechaCita[1]) - 1, Integer.parseInt(fechaCita[2]), cita.getHorainicio(), 0, 0);

                            Calendar calendarioDiaAnterior = Calendar.getInstance();
                            calendarioDiaAnterior.add(Calendar.DATE, -1);


                            if (calendarioCita.getTimeInMillis() <= calendarioDiaAnterior.getTimeInMillis()) {
                                //Eliminar la cita
                                Log.e("CITA", calendarioCita.getTimeInMillis() + "");
                                Log.e("ANTERIOR", calendarioDiaAnterior.getTimeInMillis() + "");
                                size[0]--;
                            } else
                                citasEstilista.add(cita);
                            adapter();

//                            rtdb.getReference().child("usuario").child(cita.getIdUsuario()).addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    Cliente usuario = dataSnapshot.getValue(Cliente.class);
//                                    usuarios.add(usuario);
//                                    //adapterMensajesEstilista.agregarusuario(usuario);
//                                    adapter();
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                telefonopropio = estilista.getTelefono();
                usuarioEstilista = estilista.getUsuario();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

    private void adapter() {
        Collections.sort(citasEstilista);

        Calendar calendarioActual = Calendar.getInstance();

        for (int i = 0; i < citasEstilista.size(); i++) {
            Cita cita = citasEstilista.get(i);
            String[] fechaCita = cita.getFecha().split("-");
            Calendar calendarioCita = new GregorianCalendar(Integer.parseInt(fechaCita[0]), Integer.parseInt(fechaCita[1]) - 1, Integer.parseInt(fechaCita[2]), cita.getHorainicio(), 0, 0);

            if (calendarioCita.getTimeInMillis() < calendarioActual.getTimeInMillis()) {

            } else {
                if (calendarioActual.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(fechaCita[2])) {
                    if ( i == 0) {
                        cita.setInformacion("HOY");
                        cita.setCabecera(cita.getDia().toUpperCase());
                    } else if ( !citasEstilista.get(i - 1).getInformacion().equals("HOY")) {
                        cita.setInformacion("HOY");
                        cita.setCabecera(cita.getDia().toUpperCase());
                    }
                } else {
                    long diff = calendarioCita.getTime().getTime() - calendarioActual.getTime().getTime();
                    long diferencia = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    if ( i == 0){
                        cita.setInformacion("PRÓXIMO");
                        cita.setCabecera(cita.getDia().toUpperCase()+" "+diferencia);
                    } else if (  Integer.parseInt((citasEstilista.get(i - 1).getFecha().split("-"))[2]) != Integer.parseInt((cita.getFecha().split("-"))[2])){
                        cita.setInformacion("PRÓXIMO");
                        cita.setCabecera(cita.getDia().toUpperCase()+" "+diferencia);
                    }
                }
            }
        }

        adapterMensajesEstilista = new AdapterMensajesEstilista(getContext(), citasEstilista);
        adapterMensajesEstilista.setListener(this);
        listamensajesEstilista.setLayoutManager(new LinearLayoutManager(getContext()));
        listamensajesEstilista.setAdapter(adapterMensajesEstilista);
        listamensajesEstilista.setHasFixedSize(true);

    }


    @Override
    public void onItemClick(View v, Cliente usario) {
        Intent i = new Intent(getActivity(),ChatActivity.class);
        i.putExtra("telUsuario", usario.getTelefono());
        i.putExtra("telEstilista",telefonopropio);
        i.putExtra("usEstilista",usuarioEstilista);
        startActivity(i);
    }

    @Override
    public void onItemCall(String telefono) {
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:"+telefono));
        startActivity(i);
    }
}
