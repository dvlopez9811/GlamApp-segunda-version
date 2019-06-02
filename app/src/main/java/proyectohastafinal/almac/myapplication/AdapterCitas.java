package proyectohastafinal.almac.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
        holder.nombre_estilista_cita.setText("arreglar estilista");
        holder.relative_renglon_cita_header.setVisibility(View.GONE);

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
    }

    @Override
    public int getItemCount() {
        return citas.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relative_renglon_cita_header;
        TextView salon_renglon_cita, nombre_estilista_cita, servicio_renglon_cita, horainicio_renglon_cita,renglon_cita_dia_de_la_semana,renglon_cita_fecha,renglon_cita_dias_restantes;
        ImageView iv_menu_cita_renglon_cita;

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

        }
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
