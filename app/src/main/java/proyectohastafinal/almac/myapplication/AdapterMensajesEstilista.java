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

public class AdapterMensajesEstilista extends RecyclerView.Adapter<AdapterMensajesEstilista.CustomViewHolder>{

    private ArrayList<Cliente> usuarios;
    private ArrayList<String> idusuarios;
    ArrayList<Cita> citas;
    FirebaseDatabase rtdb;
    Context context;

    public void agregarusuario(Cliente usuario,String idusuario){
        usuarios.add(usuario);
        idusuarios.add(idusuario);
        notifyDataSetChanged();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relative_layout_mensaje_estilista, relative_renglon_mensaje_estilista;
        TextView renglon_cita_dia_semana_mensaje_estilista, renglon_cita_fecha_mensaje, renglon_cita_dias_restantes_mensaje,
                nombre_usuario_cita_mensaje_estilista, servicio_renglon_cita_mensaje_estilista, horainicio_renglon_cita_mensaje_estilista;
        ImageView iv_llamar_renglon_mensaje_estilista;

        public CustomViewHolder(View itemView) {
            super(itemView);
            renglon_cita_dia_semana_mensaje_estilista = itemView.findViewById(R.id.renglon_cita_dia_semana_mensaje_estilista);
            renglon_cita_fecha_mensaje = itemView.findViewById(R.id.renglon_cita_fecha_mensaje);
            renglon_cita_dias_restantes_mensaje = itemView.findViewById(R.id.renglon_cita_dias_restantes_mensaje);
            nombre_usuario_cita_mensaje_estilista = itemView.findViewById(R.id.nombre_usuario_cita_mensaje_estilista);
            servicio_renglon_cita_mensaje_estilista = itemView.findViewById(R.id.servicio_renglon_cita_mensaje_estilista);
            horainicio_renglon_cita_mensaje_estilista = itemView.findViewById(R.id.horainicio_renglon_cita_mensaje_estilista);

            iv_llamar_renglon_mensaje_estilista = itemView.findViewById(R.id.iv_llamar_renglon_mensaje_estilista);

            relative_layout_mensaje_estilista = itemView.findViewById(R.id.relative_layout_mensaje_estilista);
            relative_renglon_mensaje_estilista = itemView.findViewById(R.id.relative_renglon_mensaje_estilista);
        }
    }

    public AdapterMensajesEstilista(Context context, ArrayList<Cliente> clientes,ArrayList<String> idusuarios, ArrayList<Cita> citas){
        this.context = context;
        this.usuarios = clientes;
        this.citas = citas;
        this.idusuarios = idusuarios;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.renglon_mensaje_estilista, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
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
        holder.servicio_renglon_cita_mensaje_estilista.setText(citas.get(position).getServicio());
        holder.horainicio_renglon_cita_mensaje_estilista.setText(horarioinicio);
        holder.nombre_usuario_cita_mensaje_estilista.setText(usuarios.get(position).getUsuario());
        holder.relative_renglon_mensaje_estilista.setVisibility(View.GONE);

        if(citas.get(position).getInformacion().equals("CITAS POR CALIFICAR")) {
            holder.relative_renglon_mensaje_estilista.setVisibility(View.VISIBLE);
            holder.renglon_cita_fecha_mensaje.setVisibility(TextView.GONE);
            holder.renglon_cita_dias_restantes_mensaje.setVisibility(TextView.GONE);
            holder.renglon_cita_dia_semana_mensaje_estilista.setText("CITAS POR CALIFICAR");
        } else if(citas.get(position).getInformacion().equals("HOY")) {
            holder.relative_renglon_mensaje_estilista.setVisibility(View.VISIBLE);
            holder.renglon_cita_fecha_mensaje.setText(citas.get(position).getFecha());
            holder.renglon_cita_dias_restantes_mensaje.setText("HOY");
            holder.renglon_cita_dia_semana_mensaje_estilista.setText(citas.get(position).getCabecera());
        }else if(citas.get(position).getInformacion().equals("PRÓXIMO")) {
            String[] datos = citas.get(position).getCabecera().split(" ");
            holder.relative_renglon_mensaje_estilista.setVisibility(View.VISIBLE);
            holder.renglon_cita_fecha_mensaje.setText(citas.get(position).getFecha());
            holder.renglon_cita_dias_restantes_mensaje.setText(datos[1] + " dias");
            holder.renglon_cita_dia_semana_mensaje_estilista.setText(datos[0]);
        }

        holder.iv_llamar_renglon_mensaje_estilista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemCall(usuarios.get(position).getTelefono());
            }
        });

        holder.relative_layout_mensaje_estilista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PASANDOOOOO", "PASANDO");
                listener.onItemClick(v,usuarios.get(position),idusuarios.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    //OBSERVER
    public interface OnItemClickListener{
        void onItemClick(View v,Cliente usario,String idusuario);
        void onItemCall(String telefono);
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
