package proyectohastafinal.almac.myapplication;

import android.app.DatePickerDialog;
import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import proyectohastafinal.almac.myapplication.model.Cita;
import proyectohastafinal.almac.myapplication.model.Horario;
import proyectohastafinal.almac.myapplication.model.Servicio;

public class AdapterItemsAgendarCita extends RecyclerView.Adapter<AdapterItemsAgendarCita.CustomViewHolder> implements AdapterHorarios.OnItemClickListener{

    private final static String[] DIASSEMANA = new String[]{"Domingo","Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};


    ArrayList<Servicio> servicios;
    FirebaseDatabase rtdb;
    FirebaseAuth auth;

    Calendar calendario;
    DatePickerDialog dpd;

    private ArrayList<AdapterHorarios> adapterHorarios;
    private ArrayList<RecyclerView> listaHorarios;

    private ArrayList<String> idestilistas;
    private String tiposervicio;
    private String idestilista;
    private String diaelegido;
    private String fechaelegida;
    private String salon;
    private int horaelegida;
    private boolean diaelegidoeshoy;
    private int posicionspinner;

    public AdapterItemsAgendarCita(String salon){
        idestilistas = new ArrayList<>();
        this.salon = salon;
        adapterHorarios = new ArrayList<>();
        listaHorarios = new ArrayList<>();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agendar_cita, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);

        rtdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        return vh;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {

        tiposervicio = servicios.get(position).getTipo();
        ((TextView) holder.root.findViewById(R.id.txt_tipo_servicio_item_agendar_cita)).setText(tiposervicio);

        listaHorarios.add(holder.root.findViewById(R.id.lista_horarios_disponibles_item_agendar_cita));

        AdapterHorarios adapterHorario = new AdapterHorarios();
        adapterHorario.setListener(this);
        adapterHorarios.add(adapterHorario);

        //Imagen de servicio

        if(tiposervicio.equals("Maquillaje"))
            ((ImageView)holder.root.findViewById(R.id.imagen_tipo_servicio_item_agendar_cita)).setImageResource(R.drawable.ic_new_maquillaje_morado);
        else if(tiposervicio.equals("Depilación"))
            ((ImageView)holder.root.findViewById(R.id.imagen_tipo_servicio_item_agendar_cita)).setImageResource(R.drawable.ic_new_depilacion_morado);
        else if(tiposervicio.equals("Masaje"))
            ((ImageView)holder.root.findViewById(R.id.imagen_tipo_servicio_item_agendar_cita)).setImageResource(R.drawable.ic_new_masaje_morado);
        else if(tiposervicio.equals("Peluquería"))
            ((ImageView)holder.root.findViewById(R.id.imagen_tipo_servicio_item_agendar_cita)).setImageResource(R.drawable.ic_new_peluqueria_morado);
        else if(tiposervicio.equals("Uñas"))
            ((ImageView)holder.root.findViewById(R.id.imagen_tipo_servicio_item_agendar_cita)).setImageResource(R.drawable.uc_new_unas_morado);

        rtdb.getReference().child("Salon de belleza").child(salon).child("Estilistas").child(servicios.get(position).getTipo())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        ArrayList<String> estilistas = new ArrayList<>();

                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                            estilistas.add(childDataSnapshot.getValue(String.class));
                            idestilistas.add(childDataSnapshot.getKey());
                            //Log.e(">>>>",childDataSnapshot.getKey());

                            ArrayAdapter<String> estilistasAdapter = new ArrayAdapter<>(holder.root.getContext(),
                                    R.layout.support_simple_spinner_dropdown_item, estilistas);


                            ((Spinner) holder.root.findViewById(R.id.item_agendar_cita_spinner_estilista)).setAdapter(estilistasAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        ((Spinner)holder.root.findViewById(R.id.item_agendar_cita_spinner_estilista)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                posicionspinner = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


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

                        idestilista = idestilistas.get(position+posicionspinner);
                        //Log.e(">>>",idestilista+" "+position+"-"+posicionspinner);

                        diaelegido = fechaelegida = "";
                        Calendar c = Calendar.getInstance();
                        c.set(year,month,dayOfMonth);
                        diaelegido = DIASSEMANA[c.get(Calendar.DAY_OF_WEEK)-1];
                        fechaelegida = year+"-"+(month+1)+"-"+dayOfMonth;


                        Log.e(">>>",dia+" "+dayOfMonth+"-"+mes+" "+month+"-"+anio+" "+year+"/"+diaelegido);

                        if(dia==dayOfMonth && mes==month && anio==year)
                            diaelegidoeshoy = true;
                        else
                            diaelegidoeshoy=false;

                        if(anio>year || (anio==year && mes>month) || (mes==month && dia>dayOfMonth)) {
                            Toast.makeText(holder.root.getContext(), "Fecha inválida", Toast.LENGTH_LONG).show();
                        }
                        else {

                                darHorarios(holder,position);
                        }

                    }


                }, anio, mes, dia);
                dpd.show();



            }
        });



        ((ImageButton)holder.root.findViewById(R.id.ib_aceptar_item_agendar_cita)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (horaelegida != 0) {
                    String idcita = UUID.randomUUID().toString();
                    Cita cita = new Cita(idcita, Cita.RESERVADA, diaelegido, fechaelegida, horaelegida + 1, horaelegida, "", salon,
                            tiposervicio, idestilista, auth.getCurrentUser().getUid());
                    rtdb.getReference().child("Citas").child(idcita).setValue(cita);
                    rtdb.getReference().child("usuario").child(auth.getCurrentUser().getUid()).child("citas").child(idcita).setValue(idcita);
                    rtdb.getReference().child("Estilista").child(idestilista).child("citas").child(idcita).setValue(idcita);
                    rtdb.getReference().child("Estilista").child(idestilista).child("agenda").child(fechaelegida).child("horas").child(horaelegida + "").setValue(horaelegida);
                    darHorarios(holder,position);
                    Toast.makeText(holder.root.getContext(), "Cita enviada al estilista", Toast.LENGTH_LONG).show();
                    horaelegida = 0;
                }else{
                    Toast.makeText(holder.root.getContext(), "Elige un horario", Toast.LENGTH_LONG).show();
                }
            }
        });

        ((ImageButton)holder.root.findViewById(R.id.ib_rechazar_item_agendar_cita)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterHorarios.get(position).showAllHorasDisponibles(new ArrayList<>());
            }
        });

    }



    @Override
    public int getItemCount() {
            return servicios.size();
    }

    public void showAllServicios(ArrayList<Servicio> allservicios) {

        servicios = new ArrayList<>();
        for(int i = 0 ; i<allservicios.size() ; i++){
            servicios.add(allservicios.get(i));
        }

        notifyDataSetChanged();
    }


    public void darHorarios(final CustomViewHolder holder,final int position){
        rtdb.getReference().child("Estilista").child(idestilista).child("horarios").child(diaelegido).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final Horario horario = dataSnapshot.getValue(Horario.class);

                if (horario != null) {


                    int hora = calendario.get(Calendar.HOUR_OF_DAY);
                    if(diaelegidoeshoy && hora >= horario.getHoraFinal()-1)
                        Toast.makeText(holder.root.getContext(), "Lo siento, no puedes agendar hoy", Toast.LENGTH_LONG).show();

                        else{

                        rtdb.getReference().child("Estilista").child(idestilista).child("agenda").child(fechaelegida).child("horas").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                ArrayList<Integer> horasocupadas = new ArrayList<>();
                                ArrayList<Integer> horasdisponibles = new ArrayList<>();


                                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                    horasocupadas.add(dsp.getValue(Integer.class));
                                }

                                int horainicio = horario.getHoraInicio();
                                if (diaelegidoeshoy)
                                    horainicio = hora+1;

                                mostrarHorarios(horasocupadas, horainicio, horasdisponibles, horario);

                                listaHorarios.get(position).setLayoutManager(new LinearLayoutManager(holder.root.getContext()));
                                listaHorarios.get(position).setAdapter(adapterHorarios.get(position));
                                adapterHorarios.get(position).showAllHorasDisponibles(horasdisponibles);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }else{
                    Toast.makeText(holder.root.getContext(), "El estilista no trabaja el día elegido",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {



            }
        });


    }

    public void mostrarHorarios(ArrayList<Integer> horasocupadas, int horainicial,ArrayList<Integer> horasdisponibles,Horario horario){
        if (!horasocupadas.isEmpty()) {
            boolean cont = true;
            for (int j = horainicial; j < horario.getHoraFinal() && cont; j++) {
                for (int k = 0; k < horasocupadas.size(); k++) {
                    if (j == horasocupadas.get(k))
                        break;
                    else if (j < horasocupadas.get(k)) {
                        horasdisponibles.add(j);
                        break;
                    } else if (k == horasocupadas.size() - 1) {
                        while (j < horario.getHoraFinal()) {
                            horasdisponibles.add(j);
                            j++;
                        }
                        cont = false;
                        break;
                    }
                }
            }
        }else {
            for (int j = horainicial; j < horario.getHoraFinal(); j++) {
                horasdisponibles.add(j);
            }
        }
    }



    @Override
    public void onClickHorario(int horaseleccionada, Context context) {
        horaelegida = horaseleccionada;
    }




}