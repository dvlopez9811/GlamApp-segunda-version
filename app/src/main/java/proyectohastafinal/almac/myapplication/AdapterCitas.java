package proyectohastafinal.almac.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Stream;

import proyectohastafinal.almac.myapplication.model.BusquedaSalonDeBelleza;
import proyectohastafinal.almac.myapplication.model.Cita;

public class AdapterCitas extends RecyclerView.Adapter<AdapterCitas.CustomViewHolder>{

    Context context;
    ArrayList<Cita> citas;
    FirebaseDatabase rtdb;

    public AdapterCitas(Context context, ArrayList<Cita> citas){
        this.context = context;
        this.citas = citas;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.renglon_cita,parent,false));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        rtdb=FirebaseDatabase.getInstance();
        int horarioinic = citas.get(position).getHorainicio();
        String horarioinicio = "";
        if(citas.get(position).getHorainicio()<12){
            horarioinicio=horarioinic+"a.m";
        }
        else {
            if(horarioinic!=12)
                horarioinic-=12;
            horarioinicio=horarioinic+" p.m";
        }

        //StorageReference ref = storage.getReference().child("estilistas").child(citas.get(position).getIdEstilista());
        //ImageView image =  holder.root.findViewById(R.id.image_cita_estilista);
        //ref.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(image.getContext()).load(uri).into(image));
        holder.salon_renglon_cita.setText(citas.get(position).getNombreSalon());
        holder.servicio_renglon_cita.setText(citas.get(position).getServicio());
        holder.horainicio_renglon_cita.setText(horarioinicio);
        holder.nombre_estilista_cita.setText(citas.get(position).getNombreEstilista());
        holder.relative_renglon_cita_header.setVisibility(View.GONE);


        if(citas.get(position).getEstado().equals(Cita.FINALIZADA)){
            holder.relative_renglon_calificar_cita.setVisibility(View.VISIBLE);
            holder.iv_menu_cita_renglon_cita.setVisibility(View.GONE);
        }
        if(citas.get(position).getInformacion().equals("CITAS POR CALIFICAR")) {
            holder.relative_renglon_cita_header.setVisibility(View.VISIBLE);
            holder.renglon_cita_fecha.setVisibility(TextView.GONE);
            holder.renglon_cita_dias_restantes.setVisibility(TextView.GONE);
            holder.renglon_cita_dia_de_la_semana.setText("CITAS POR CALIFICAR");
        } else if(citas.get(position).getInformacion().equals("HOY")) {
            holder.relative_renglon_cita_header.setVisibility(View.VISIBLE);
            holder.renglon_cita_fecha.setText(citas.get(position).getFecha());
            holder.renglon_cita_dias_restantes.setText("HOY");
            holder.renglon_cita_dia_de_la_semana.setText(citas.get(position).getCabecera());
        }else if(citas.get(position).getInformacion().equals("PRÓXIMO")) {
            String[] datos = citas.get(position).getCabecera().split(" ");
            holder.relative_renglon_cita_header.setVisibility(View.VISIBLE);
            holder.renglon_cita_fecha.setText(citas.get(position).getFecha());
            holder.renglon_cita_dias_restantes.setText(datos[1] + " dias");
            holder.renglon_cita_dia_de_la_semana.setText(datos[0]);
        }

        holder.iv_menu_cita_renglon_cita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,citas.get(position));
            }
        });



        ///Calificacion
        String nombreSalon = citas.get(position).getNombreSalon();
        rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("calificacion").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String cal=  dataSnapshot.getValue(String.class);
                double calificacionActualSalon=Double.parseDouble(cal);
                rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("numeroCalificaciones").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       long numero = (Long) dataSnapshot.getValue();

                       holder.btn_calificacion1.setOnClickListener(v -> {
                           double calificacionDada=1.0;
                           String promedioNuevo= calificacionActualSalon+((calificacionDada-calificacionActualSalon)/(numero+1))+"";
                           rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("calificacion").setValue(promedioNuevo);
                           rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("numeroCalificaciones").setValue(numero+1);
                           holder.btn_calificacion1.setBackgroundResource(R.drawable.ic_estrella_llena);
                           bloquearBotonesCalificar(holder.btn_calificacion1,holder.btn_calificacion2,holder.btn_calificacion3,holder.btn_calificacion4,holder.btn_calificacion5);
                           eliminarCitaCalificada(citas.get(position));
                       });
                        holder.btn_calificacion2.setOnClickListener(v -> {
                            double calificacionDada=2.0;
                            String promedioNuevo= calificacionActualSalon+((calificacionDada-calificacionActualSalon)/(numero+1))+"";
                            rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("calificacion").setValue(promedioNuevo);
                            rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("numeroCalificaciones").setValue(numero+1);
                            holder.btn_calificacion1.setBackgroundResource(R.drawable.ic_estrella_llena);
                            holder.btn_calificacion2.setBackgroundResource(R.drawable.ic_estrella_llena);
                            bloquearBotonesCalificar(holder.btn_calificacion1,holder.btn_calificacion2,holder.btn_calificacion3,holder.btn_calificacion4,holder.btn_calificacion5);
                            eliminarCitaCalificada(citas.get(position));

                        });

                        holder.btn_calificacion3.setOnClickListener(v -> {
                            double calificacionDada=3.0;
                            String promedioNuevo= calificacionActualSalon+((calificacionDada-calificacionActualSalon)/(numero+1))+"";
                            rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("calificacion").setValue(promedioNuevo);
                            rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("numeroCalificaciones").setValue(numero+1);
                            holder.btn_calificacion1.setBackgroundResource(R.drawable.ic_estrella_llena);
                            holder.btn_calificacion2.setBackgroundResource(R.drawable.ic_estrella_llena);
                            holder.btn_calificacion3.setBackgroundResource(R.drawable.ic_estrella_llena);
                            bloquearBotonesCalificar(holder.btn_calificacion1,holder.btn_calificacion2,holder.btn_calificacion3,holder.btn_calificacion4,holder.btn_calificacion5);
                            eliminarCitaCalificada(citas.get(position));
                        });

                        holder.btn_calificacion4.setOnClickListener(v -> {
                            double calificacionDada=4.0;
                            String promedioNuevo= calificacionActualSalon+((calificacionDada-calificacionActualSalon)/(numero+1))+"";
                            rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("calificacion").setValue(promedioNuevo);
                            rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("numeroCalificaciones").setValue(numero+1);
                            holder.btn_calificacion1.setBackgroundResource(R.drawable.ic_estrella_llena);
                            holder.btn_calificacion2.setBackgroundResource(R.drawable.ic_estrella_llena);
                            holder.btn_calificacion3.setBackgroundResource(R.drawable.ic_estrella_llena);
                            holder.btn_calificacion4.setBackgroundResource(R.drawable.ic_estrella_llena);
                            bloquearBotonesCalificar(holder.btn_calificacion1,holder.btn_calificacion2,holder.btn_calificacion3,holder.btn_calificacion4,holder.btn_calificacion5);
                            eliminarCitaCalificada(citas.get(position));
                        });
                        holder.btn_calificacion5.setOnClickListener(v -> {
                            double calificacionDada=5.0;
                            String promedioNuevo= calificacionActualSalon+((calificacionDada-calificacionActualSalon)/(numero+1))+"";
                            rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("calificacion").setValue(promedioNuevo);
                            rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("numeroCalificaciones").setValue(numero+1);
                            holder.btn_calificacion1.setBackgroundResource(R.drawable.ic_estrella_llena);
                            holder.btn_calificacion2.setBackgroundResource(R.drawable.ic_estrella_llena);
                            holder.btn_calificacion3.setBackgroundResource(R.drawable.ic_estrella_llena);
                            holder.btn_calificacion4.setBackgroundResource(R.drawable.ic_estrella_llena);
                            holder.btn_calificacion5.setBackgroundResource(R.drawable.ic_estrella_llena);
                            bloquearBotonesCalificar(holder.btn_calificacion1,holder.btn_calificacion2,holder.btn_calificacion3,holder.btn_calificacion4,holder.btn_calificacion5);
                            eliminarCitaCalificada(citas.get(position));
                        });
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });



    }

    @Override
    public int getItemCount() {
        return citas.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relative_renglon_cita_header;
        TextView salon_renglon_cita, nombre_estilista_cita, servicio_renglon_cita, horainicio_renglon_cita,renglon_cita_dia_de_la_semana,renglon_cita_fecha,renglon_cita_dias_restantes;
        ImageView iv_menu_cita_renglon_cita;
        RelativeLayout relative_renglon_calificar_cita;
        Button btn_calificacion1,btn_calificacion2,btn_calificacion3,btn_calificacion4,btn_calificacion5;
        public CustomViewHolder(View itemView) {
            super(itemView);
            salon_renglon_cita = itemView.findViewById(R.id.salon_renglon_cita);
            nombre_estilista_cita = itemView.findViewById(R.id.nombre_estilista_cita);
            servicio_renglon_cita = itemView.findViewById(R.id.servicio_renglon_cita);
            horainicio_renglon_cita = itemView.findViewById(R.id.horainicio_renglon_cita);
            iv_menu_cita_renglon_cita = itemView.findViewById(R.id.iv_menu_cita_renglon_cita);
            relative_renglon_cita_header = itemView.findViewById(R.id.relative_renglon_cita_header);
            renglon_cita_dia_de_la_semana = itemView.findViewById(R.id.renglon_cita_dia_de_la_semana);
            renglon_cita_fecha = itemView.findViewById(R.id.renglon_cita_fecha);
            renglon_cita_dias_restantes = itemView.findViewById(R.id.renglon_cita_dias_restantes);
            relative_renglon_calificar_cita=itemView.findViewById(R.id.opcion_calificacion_renglon_cita);
            btn_calificacion1=itemView.findViewById(R.id.btn_estrella_calificacion_1);
            btn_calificacion2=itemView.findViewById(R.id.btn_estrella_calificacion_2);
            btn_calificacion3=itemView.findViewById(R.id.btn_estrella_calificacion_3);
            btn_calificacion4=itemView.findViewById(R.id.btn_estrella_calificacion_4);
            btn_calificacion5=itemView.findViewById(R.id.btn_estrella_calificacion_5);

        }
    }


    public void eliminarCitaCalificada(Cita cita){

        String idEstilista = cita.getIdEstilista();
        String idUsuario = cita.getIdUsuario();
        String idCita = cita.getIdcita();
        String fecha = cita.getFecha();
        String inicio = cita.getHorainicio()+"";
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

    }

   public void bloquearBotonesCalificar(Button c1,Button c2,Button c3,Button c4, Button c5){
        c1.setEnabled(false);
       c2.setEnabled(false);
       c3.setEnabled(false);
       c4.setEnabled(false);
       c5.setEnabled(false);

   }
    //OBSERVER
    public interface OnItemClickListener{
        void onItemClick(View v,Cita cita);
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
