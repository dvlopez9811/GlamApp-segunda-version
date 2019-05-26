package proyectohastafinal.almac.myapplication;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.support.annotation.HalfFloat;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

import proyectohastafinal.almac.myapplication.model.Estilista;
import proyectohastafinal.almac.myapplication.model.Horario;
import proyectohastafinal.almac.myapplication.model.Servicio;

public class AdapterItemsAgendarCita extends RecyclerView.Adapter<AdapterItemsAgendarCita.CustomViewHolder>{

    private static final String[] DIAS = new String[]{"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};

    ArrayList<Servicio> data;
    FirebaseDatabase rtdb;
    FirebaseAuth auth;

    Calendar calendario;
    DatePickerDialog dpd;

    private RecyclerView listaHorarios;
    private AdapterHorarios adapterHorarios;

    private String salon;
    private String idestilista;
    private String dia;
    private ArrayList<String> idestilistas;



    public AdapterItemsAgendarCita(){
        data = new ArrayList<>();
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agendar_cita, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        rtdb = FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {

        ((TextView) holder.root.findViewById(R.id.txt_tipo_servicio_item_agendar_cita)).setText(data.get(position).getTipo());
        adapterHorarios = new AdapterHorarios();
        listaHorarios = holder.root.findViewById(R.id.lista_horarios_disponibles_item_agendar_cita);
        listaHorarios.setLayoutManager(new LinearLayoutManager(holder.root.getContext()));

        listaHorarios.setAdapter(adapterHorarios);
        listaHorarios.setHasFixedSize(true);

        Horario prueba = new Horario(12,13);
        Horario prueba2 = new Horario(13,14);

        ArrayList<Horario> horariosPrueba = new ArrayList<Horario>();
        horariosPrueba.add(prueba);
        horariosPrueba.add(prueba2);
     //   adapterHorarios.showAllHorarios(horariosPrueba);
darHorarios(true,holder);

        (holder.root.findViewById(R.id.date_picker_agendar_item)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendario = Calendar.getInstance();
                int dia = calendario.get(Calendar.DAY_OF_MONTH);
                int mes = calendario.get(Calendar.MONTH);
                int anio = calendario.get(Calendar.YEAR);


                dpd = new DatePickerDialog(holder.root.getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ((TextView) holder.root.findViewById(R.id.mes_seleccionado_item_agendar_cita)).setText(month + 1 + "");
                        ((TextView) holder.root.findViewById(R.id.dia_seleccionado_item_agendar_cita)).setText(dayOfMonth + "");
                    }
                }, anio, mes, dia);
                dpd.show();
            }
        });


        final ArrayList<CharSequence> estilistas = new ArrayList<>();

       rtdb.getReference().child("Salon de belleza").child(data.get(position).getSalonDeBelleza().getNombreSalonDeBelleza()).child("Estilistas")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    estilistas.add(childDataSnapshot.getKey());
                    ArrayAdapter<CharSequence> estilistasAdapter = new ArrayAdapter<>(holder.root.getContext(),
                            R.layout.support_simple_spinner_dropdown_item, estilistas);
                  //  darEstilistas(data.get(position).getTipo(),holder);
                }
            }

   //         @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ((Spinner)holder.root.findViewById(R.id.item_agendar_cita_spinner_estilista)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



    }


    public void darEstilistas(String servicio,CustomViewHolder holder){
        rtdb.getReference().child("Salon de belleza").child(salon).child("Estilistas").child(servicio).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idestilista = "";
                idestilistas = new ArrayList<>();
                final ArrayList<String> nombreestilistas = new ArrayList<>();

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String id = dsp.getValue(String.class);
                    idestilistas.add(id);

                    rtdb.getReference().child("Estilista").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           Estilista estilista = dataSnapshot.getValue(Estilista.class);
                            nombreestilistas.add(estilista.getNombreYApellido());
                            ArrayAdapter<String> adapter = new ArrayAdapter<String> (holder.root.getContext(), android.R.layout.simple_spinner_item, nombreestilistas);
                            ((Spinner) holder.root.findViewById(R.id.item_agendar_cita_spinner_estilista)).setAdapter(adapter);
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
    }

    public void darHorarios(final boolean hoy,CustomViewHolder holder){
        rtdb.getReference().child("Estilista").child(idestilista).child("horarios").child(dia).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final Horario horario = dataSnapshot.getValue(Horario.class);
                if (horario != null) {
                    rtdb.getReference().child("Estilista").child(idestilista).child("agenda").child(dia).child("horas").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            ArrayList<Integer> horasagenda = new ArrayList<>();
                            ArrayList<Horario> horarios = new ArrayList<>();

                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                horasagenda.add(Integer.parseInt(dsp.getKey()));
                            }
                            int horainicial = horario.getHoraInicio();
                            if(hoy) {
                                final int hora = calendario.get(Calendar.HOUR_OF_DAY);
                                if (hora < (horario.getHoraFinal() - 1) && hora > horainicial) {
                                    horainicial = hora + 1;
                                    mostrarHorariosHoyoManana(horasagenda,horainicial,horarios,horario);
                                }
                                else
                                    Toast.makeText(holder.root.getContext(), "No hay horarios disponibles hoy", Toast.LENGTH_LONG).show();
                            }else
                                mostrarHorariosHoyoManana(horasagenda,horainicial,horarios,horario);
                            adapterHorarios.showAllHorarios(horarios);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    adapterHorarios.showAllHorarios(new ArrayList<Horario>());
                    if (!idestilista.isEmpty()) {
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void mostrarHorariosHoyoManana(ArrayList<Integer> horasagenda, int horainicial,ArrayList<Horario> horarios,Horario horario){
        if (horasagenda.size() != 0) {
            boolean cont = true;
            for (int j = horainicial; j < horario.getHoraFinal() && cont; j++) {
                for (int k = 0; k < horasagenda.size(); k++) {
                    if (j == horasagenda.get(k))
                        break;
                    else if (j < horasagenda.get(k)) {
                        horarios.add(new Horario(j, j + 1));
                        break;
                    } else if (k == horasagenda.size() - 1) {
                        while (j < horario.getHoraFinal()) {
                            horarios.add(new Horario(j, j + 1));
                            j++;
                        }
                        cont = false;
                        break;
                    }
                }
            }
        }else {
            for (int j = horainicial; j < horario.getHoraFinal(); j++) {
                horarios.add(new Horario(j, j + 1));
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void showAllServicios(ArrayList<Servicio> allservicios) {
        for(int i = 0 ; i<allservicios.size() ; i++){
            if(!data.contains(allservicios.get(i))) data.add(allservicios.get(i));
        }
        salon = allservicios.get(0).getSalonDeBelleza().getNombreSalonDeBelleza();
        notifyDataSetChanged();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;
        }
    }

}