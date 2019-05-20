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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

import proyectohastafinal.almac.myapplication.model.Horario;
import proyectohastafinal.almac.myapplication.model.Servicio;

public class AdapterItemsAgendarCita extends RecyclerView.Adapter<AdapterItemsAgendarCita.CustomViewHolder>{

    ArrayList<Servicio> data;
    FirebaseDatabase rtdb;

    Calendar c;
    DatePickerDialog dpd;

    private RecyclerView listaHorarios;
    private AdapterHorarios adapterHorarios;

    public AdapterItemsAgendarCita(){
        data = new ArrayList<>();
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agendar_cita, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        rtdb = FirebaseDatabase.getInstance();
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {

        ((TextView) holder.root.findViewById(R.id.txt_tipo_servicio_item_agendar_cita)).setText(data.get(position).getTipo());

        adapterHorarios=new AdapterHorarios();
        listaHorarios=holder.root.findViewById(R.id.lista_horarios_disponibles_item_agendar_cita);
        listaHorarios.setLayoutManager(new LinearLayoutManager(holder.root.getContext()));

        listaHorarios.setAdapter(adapterHorarios);
        listaHorarios.setHasFixedSize(true);

        Horario prueba = new Horario("Lunes","2:30 pm","3:00 pm");
        Horario prueba2 = new Horario("Lunes","3:00 pm","3:30 pm");

        ArrayList<Horario> horariosPrueba = new ArrayList<Horario>();
        horariosPrueba.add(prueba);
        horariosPrueba.add(prueba2);
        adapterHorarios.showAllHorarios(horariosPrueba);


        ( holder.root.findViewById(R.id.date_picker_agendar_item)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c=Calendar.getInstance();
                int dia = c.get(Calendar.DAY_OF_MONTH);
                 int mes = c.get(Calendar.MONTH);
                 int anio = c.get(Calendar.YEAR);


                dpd = new DatePickerDialog(holder.root.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ((TextView) holder.root.findViewById(R.id.mes_seleccionado_item_agendar_cita)).setText(month+1+"");
                        ((TextView) holder.root.findViewById(R.id.dia_seleccionado_item_agendar_cita)).setText(dayOfMonth+"");
                    }
                },anio,mes,dia);
                dpd.show();
            }
        });



        final ArrayList<CharSequence> estilistas = new ArrayList<>();
        rtdb.getReference().child("Salon de belleza").child(data.get(position).getSalonDeBelleza().getNombreSalonDeBelleza()).child("Estilistas")
                .child(data.get(position).getTipo()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    estilistas.add(childDataSnapshot.getKey());
                    ArrayAdapter<CharSequence> estilistasAdapter = new ArrayAdapter<>(holder.root.getContext(),
                            R.layout.support_simple_spinner_dropdown_item,estilistas);
                    ((Spinner)holder.root.findViewById(R.id.item_agendar_cita_spinner_estilista)).setAdapter(estilistasAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void showAllServicios(ArrayList<Servicio> allservicios) {
        for(int i = 0 ; i<allservicios.size() ; i++){
            if(!data.contains(allservicios.get(i))) data.add(allservicios.get(i));
        }
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
