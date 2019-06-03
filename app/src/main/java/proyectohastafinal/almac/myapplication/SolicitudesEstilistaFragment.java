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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import proyectohastafinal.almac.myapplication.model.Cita;
import proyectohastafinal.almac.myapplication.model.Estilista;


public class SolicitudesEstilistaFragment extends Fragment implements AdapterCitasEstilista.OnItemClickListenerHoy{

    private AdapterCitasEstilista adapterCitasEstilista;
    private RecyclerView lista_citas_estilista;
    private ArrayList<Cita> citas;
    private Cita citaseleccionada;

    FirebaseDatabase rtdb;
    FirebaseAuth auth;
    ArrayList<Cita> citasEstilista;
    Calendar calendario;

    private static SolicitudesEstilistaFragment instance;

    public static SolicitudesEstilistaFragment getInstance(){
        instance = instance == null ? new SolicitudesEstilistaFragment() : instance;
        return instance;
    }

    public SolicitudesEstilistaFragment() {
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
        View v = inflater.inflate(R.layout.fragment_solicitudes_estilista, container, false);
        auth = FirebaseAuth.getInstance();

        calendario = Calendar.getInstance();

        if (auth.getCurrentUser() == null) {
            return v;
        }

        rtdb = FirebaseDatabase.getInstance();

        lista_citas_estilista = v.findViewById(R.id.lista_citas_estilista_solicitudes);

        citasEstilista = new ArrayList<>();
        rtdb.getReference().child("Estilista").child(auth.getCurrentUser().getUid()).child("citas").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long[] size = new Long[1];
                size[0] = dataSnapshot.getChildrenCount();
                for (DataSnapshot hijo : dataSnapshot.getChildren()) {
                    Query query = rtdb.getReference().child("Citas").child(hijo.getValue().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Cita cita = (dataSnapshot.getValue(Cita.class));
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
                            if (citasEstilista.size() == size[0]) adapter();
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

    private void adapter() {
        Collections.sort(citasEstilista);

        Calendar calendarioActual = Calendar.getInstance();
        ArrayList<Cita> citasAux = new ArrayList<>();

        for (int i = 0; i < citasEstilista.size(); i++) {
            Cita cita = citasEstilista.get(i);
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
                        cita.setCabecera(cita.getDia().toUpperCase() + " " + diferencia);
                    } else if (Integer.parseInt((citasEstilista.get(i - 1).getFecha().split("-"))[2]) != Integer.parseInt((cita.getFecha().split("-"))[2])) {
                        cita.setInformacion("PRÓXIMO");
                        cita.setCabecera(cita.getDia().toUpperCase() + " " + diferencia);
                    }
                    citasAux.add(citasEstilista.get(i));
                }
            }
        }

        adapterCitasEstilista = new AdapterCitasEstilista(getContext(), citasAux);
        adapterCitasEstilista.setListener(SolicitudesEstilistaFragment.this);
        lista_citas_estilista.setLayoutManager(new LinearLayoutManager(getContext()));
        lista_citas_estilista.setAdapter(adapterCitasEstilista);
        lista_citas_estilista.setHasFixedSize(true);
    }

    @Override
    public void onItemClick(View v, Cita cita) {
        this.registerForContextMenu(v);
        getActivity().openContextMenu(v);
        citaseleccionada = cita;
        Log.d("Seleccionada", citaseleccionada+"");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.context_menu_citas_estilista,menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.envio_mensaje_usuario_cita:

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

            case R.id.cancelar_cita_desde_estilista:
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
                dialogo1.setTitle("Cita");
                dialogo1.setMessage("¿ Esta seguro de cancelar la cita ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                        String idEstilista = citaseleccionada.getIdEstilista();
                        String idUsuario = citaseleccionada.getIdUsuario();
                        String idCita = citaseleccionada.getIdcita();
                        String fecha = citaseleccionada.getFecha();
                        String inicio = citaseleccionada.getHorainicio()+"";
                        Log.e("idCIta", idCita +"");
                        final boolean[] seElimino = {false};
                        rtdb.getReference().child("Citas").child(idCita).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.e("SIRVE", "elimino " + idCita);
                            }
                        });
                        rtdb.getReference().child("Estilista").child(idEstilista).child("citas").child(idCita).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.e("SIRVE2", "elimino " + idCita);
                            }
                        });

                        rtdb.getReference().child("Estilista").child(idEstilista).child("agenda").child(fecha).child("horas").child(inicio).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.e("SIRVE3", "elimino " + idCita);
                            }
                        });

                        rtdb.getReference().child("usuario").child(idUsuario).child("citas").child(idCita).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.e("SIRVE4", "elimino " + idCita);
                            }
                        });

                        Toast.makeText(getActivity(),"Se cancelo esta cita",Toast.LENGTH_LONG).show();
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Toast.makeText(getContext(), "ESTE fue el elegido pero de solicitudes", Toast.LENGTH_LONG).show();

                    }
                });
                dialogo1.show();


                break;

        }

        return super.onContextItemSelected(item);
    }

}
