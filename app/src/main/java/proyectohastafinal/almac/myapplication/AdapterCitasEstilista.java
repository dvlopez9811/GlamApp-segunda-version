package proyectohastafinal.almac.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.Cita;
import proyectohastafinal.almac.myapplication.model.Cliente;
import proyectohastafinal.almac.myapplication.model.Estilista;
import proyectohastafinal.almac.myapplication.model.Servicio;

public class AdapterCitasEstilista extends RecyclerView.Adapter<AdapterCitasEstilista.CustomViewHolder>{

    ArrayList<Cita> citas;
    FirebaseDatabase rtdb;
    Context context;


//    public void agregarcita(Cita cita){
//        citas.add(cita);
//        notifyDataSetChanged();
//    }

//    public static class CustomViewHolder extends RecyclerView.ViewHolder {
//        public LinearLayout root;
//        public CustomViewHolder(LinearLayout v) {
//            super(v);
//            root = v;
//        }
//
//    }

//    public AdapterCitasEstilista(){
//        rtdb = FirebaseDatabase.getInstance();
//        citas = new ArrayList<>();
//    }

    public AdapterCitasEstilista(Context context, ArrayList<Cita> citas){
        this.context = context;
        this.citas = citas;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.renglon_cita_estilista, parent, false));
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

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
        holder.salon_renglon_cita_fragment_estilista_hoy.setText(citas.get(position).getNombreSalon());
        holder.servicio_renglon_cita_fragment_estilista_hoy.setText(citas.get(position).getServicio());
        holder.horainicio_renglon_cita_fragment_estilista_hoy.setText(horarioinicio);
        holder.nombre_estilista_cita_fragment_estilista_hoy.setText("arreglar estilista");
        holder.relative_renglon_cita_header_fragment_estilista_hoy.setVisibility(View.GONE);

        if(citas.get(position).getInformacion().equals("CITAS POR CALIFICAR")) {
            holder.relative_renglon_cita_header_fragment_estilista_hoy.setVisibility(View.VISIBLE);
            holder.renglon_cita_fecha_fragment_estilista_hoy.setVisibility(TextView.GONE);
            holder.renglon_cita_dias_restantes_fragment_estilista_hoy.setVisibility(TextView.GONE);
            holder.renglon_cita_dia_de_la_semana_fragment_estilista_hoy.setText("CITAS POR CALIFICAR");
        } else if(citas.get(position).getInformacion().equals("HOY")) {
            holder.relative_renglon_cita_header_fragment_estilista_hoy.setVisibility(View.VISIBLE);
            holder.renglon_cita_fecha_fragment_estilista_hoy.setText(citas.get(position).getFecha());
            holder.renglon_cita_dias_restantes_fragment_estilista_hoy.setText("HOY");
            holder.renglon_cita_dia_de_la_semana_fragment_estilista_hoy.setText(citas.get(position).getCabecera());
        }else if(citas.get(position).getInformacion().equals("PRÓXIMO")) {
            String[] datos = citas.get(position).getCabecera().split(" ");
            holder.relative_renglon_cita_header_fragment_estilista_hoy.setVisibility(View.VISIBLE);
            holder.renglon_cita_fecha_fragment_estilista_hoy.setText(citas.get(position).getFecha());
            holder.renglon_cita_dias_restantes_fragment_estilista_hoy.setText(datos[1] + " dias");
            holder.renglon_cita_dia_de_la_semana_fragment_estilista_hoy.setText(datos[0]);
        }

        holder.iv_menu_cita_renglon_cita_fragment_estilista_hoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PASANDOOOOO", "PASANDO");
                listener.onItemClick(v,citas.get(position));
            }
        });

//        Cita aux = citas.get(position);
//
//        Log.d("CITASSS", aux.getIdUsuario() + "");
//
//        rtdb.getReference().child("Estilista").child(citas.get(position).getIdEstilista()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                /*Cliente cliente = dataSnapshot.getValue(Cliente.class);
//
//                ((TextView) holder.root.findViewById(R.id.usuario_renglon_cita_estilista)).setText(cliente.getUsuario());
//                ((TextView) holder.root.findViewById(R.id.servicio_renglon_cita_estilista)).setText(citas.get(position).getServicio());
//
//                //Hora inicio
//                int horarioinic = citas.get(position).getHorainicio();
//                String horarioinicio = "";
//                if(citas.get(position).getHorainicio()<12){
//                    horarioinicio=horarioinic+"a.m";
//                }
//                else {
//                    if(horarioinic!=12)
//                        horarioinic-=12;
//                    horarioinicio=horarioinic+" p.m";
//                }
//
//                ((TextView) holder.root.findViewById(R.id.horainicio_renglon_cita_estilista)).setText(horarioinicio);
//                final Button btn_cancelar_cita_estilista = holder.root.findViewById(R.id.btn_cancelar_renglon_cita_estilista);
//                btn_cancelar_cita_estilista.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        listener.onItemClick(citas.get(position));
//                    }
//                });*/
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relative_renglon_cita_header_fragment_estilista_hoy;
        TextView salon_renglon_cita_fragment_estilista_hoy, nombre_estilista_cita_fragment_estilista_hoy, servicio_renglon_cita_fragment_estilista_hoy, horainicio_renglon_cita_fragment_estilista_hoy,
                renglon_cita_dia_de_la_semana_fragment_estilista_hoy,renglon_cita_fecha_fragment_estilista_hoy,renglon_cita_dias_restantes_fragment_estilista_hoy;
        ImageView iv_menu_cita_renglon_cita_fragment_estilista_hoy;

        public CustomViewHolder(View itemView) {
            super(itemView);
            salon_renglon_cita_fragment_estilista_hoy = itemView.findViewById(R.id.salon_renglon_cita_fragment_estilista_hoy);
            nombre_estilista_cita_fragment_estilista_hoy = itemView.findViewById(R.id.nombre_estilista_cita_fragment_estilista_hoy);
            servicio_renglon_cita_fragment_estilista_hoy = itemView.findViewById(R.id.servicio_renglon_cita_fragment_estilista_hoy);
            horainicio_renglon_cita_fragment_estilista_hoy = itemView.findViewById(R.id.horainicio_renglon_cita_fragment_estilista_hoy);
            iv_menu_cita_renglon_cita_fragment_estilista_hoy = itemView.findViewById(R.id.iv_menu_cita_renglon_cita_fragment_estilista_hoy);
            relative_renglon_cita_header_fragment_estilista_hoy = itemView.findViewById(R.id.relative_renglon_cita_header_fragment_estilista_hoy);
            renglon_cita_dia_de_la_semana_fragment_estilista_hoy = itemView.findViewById(R.id.renglon_cita_dia_de_la_semana_fragment_estilista_hoy);
            renglon_cita_fecha_fragment_estilista_hoy = itemView.findViewById(R.id.renglon_cita_fecha_fragment_estilista_hoy);
            renglon_cita_dias_restantes_fragment_estilista_hoy = itemView.findViewById(R.id.renglon_cita_dias_restantes_fragment_estilista_hoy);

        }
    }

    @Override
    public int getItemCount() {
        return citas.size();
    }

    public void showAllCitas(ArrayList<Cita> allCitas) {
        for (int i = 0; i < allCitas.size(); i++) {
            if (!citas.contains(allCitas.get(i))) citas.add(allCitas.get(i));
        }
        notifyDataSetChanged();
    }
    //OBSERVER
    public interface OnItemClickListenerHoy{
        void onItemClick(View v,Cita cita);
    }

    private OnItemClickListenerHoy listener;

    public void setListener(OnItemClickListenerHoy listener){
        this.listener = listener;
    }

//    //OBSERVER
//    public interface OnItemClickListener{
//        void onItemClick(Cita cita);
//    }
//
//    private OnItemClickListener listener;
//
//    public void setListener(OnItemClickListener listener){
//        this.listener = listener;
//    }

}
