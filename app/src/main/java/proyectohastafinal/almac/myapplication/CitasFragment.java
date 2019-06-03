package proyectohastafinal.almac.myapplication;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import proyectohastafinal.almac.myapplication.model.Cliente;
import proyectohastafinal.almac.myapplication.model.Estilista;

public class CitasFragment extends Fragment implements AdapterCitas.OnItemClickListener {

    private static CitasFragment instance;
    FirebaseDatabase rtdb;
    FirebaseAuth auth;

    private AdapterCitas adapterCitas;
    private RecyclerView lista_citas;

    private TextView txt_iniciar_sesion_fragment_citas;
    private Button btn_iniciar_sesion_fragment_citas;

    private Cita citaseleccionada;

    Calendar calendario;

    ArrayList<Cita> citasCliente;

    public static CitasFragment getInstance() {
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
                Intent i = new Intent(getContext(), InicioActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        if (auth.getCurrentUser() == null) {
            return v;
        }

        txt_iniciar_sesion_fragment_citas.setVisibility(TextView.INVISIBLE);
        btn_iniciar_sesion_fragment_citas.setVisibility(TextView.INVISIBLE);

        rtdb = FirebaseDatabase.getInstance();

        lista_citas = v.findViewById(R.id.lista_citas);

        citasCliente = new ArrayList<>();
        rtdb.getReference().child("usuario").child(auth.getCurrentUser().getUid()).child("citas").addListenerForSingleValueEvent(new ValueEventListener() {

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
                                citasCliente.add(cita);
                            if (citasCliente.size() == size[0]) adapter();
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
        Collections.sort(citasCliente);

        Calendar calendarioActual = Calendar.getInstance();

        for (int i = 0; i < citasCliente.size(); i++) {
            Cita cita = citasCliente.get(i);
            String[] fechaCita = cita.getFecha().split("-");
            Calendar calendarioCita = new GregorianCalendar(Integer.parseInt(fechaCita[0]), Integer.parseInt(fechaCita[1]) - 1, Integer.parseInt(fechaCita[2]), cita.getHorainicio(), 0, 0);

            if (calendarioCita.getTimeInMillis() < calendarioActual.getTimeInMillis()) {
                cita.setEstado(Cita.FINALIZADA);
                if (i == 0) {
                    cita.setInformacion("CITAS POR CALIFICAR");
                }
            } else {
                if (calendarioActual.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(fechaCita[2])) {
                    if ( i == 0) {
                        cita.setInformacion("HOY");
                        cita.setCabecera(cita.getDia().toUpperCase());
                    } else if ( !citasCliente.get(i - 1).getInformacion().equals("HOY")) {
                        cita.setInformacion("HOY");
                        cita.setCabecera(cita.getDia().toUpperCase());
                    }
                } else {
                    long diff = calendarioCita.getTime().getTime() - calendarioActual.getTime().getTime();
                    long diferencia = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    if ( i == 0){
                        cita.setInformacion("PRÓXIMO");
                        cita.setCabecera(cita.getDia().toUpperCase()+" "+diferencia);
                    } else if (  Integer.parseInt((citasCliente.get(i - 1).getFecha().split("-"))[2]) != Integer.parseInt((cita.getFecha().split("-"))[2])){
                        cita.setInformacion("PRÓXIMO");
                        cita.setCabecera(cita.getDia().toUpperCase()+" "+diferencia);
                    }
                }
            }
        }

        adapterCitas = new AdapterCitas(getContext(), citasCliente);
        adapterCitas.setListener(CitasFragment.this);
        lista_citas.setLayoutManager(new LinearLayoutManager(getContext()));
        lista_citas.setAdapter(adapterCitas);
        lista_citas.setHasFixedSize(true);
    }


    @Override
    public void onItemClick(View v,Cita cita) {
        getActivity().registerForContextMenu(v);
        getActivity().openContextMenu(v);
        citaseleccionada = cita;
        Log.d("Seleccionada", citaseleccionada+"");
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

            case R.id.cambio_hora_cita:
                break;

            case R.id.cancelar_cita:
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

                    }
                });
                dialogo1.show();


                break;

        }

        return super.onContextItemSelected(item);
    }

}
