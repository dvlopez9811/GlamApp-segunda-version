package proyectohastafinal.almac.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    public void agregarusuario(Cliente usuario,String idusuario){
        usuarios.add(usuario);
        idusuarios.add(idusuario);
        notifyDataSetChanged();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;


        }

    }

    public AdapterMensajesEstilista(){
        usuarios = new ArrayList<>();
        idusuarios = new ArrayList<>();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.renglon_mensaje_estilista, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        ((TextView)holder.root.findViewById(R.id.tv_usuario_mensaje_estilista)).setText(usuarios.get(position).getUsuario());
        ((ImageView)holder.root.findViewById(R.id.llamar_mensaje_estilista)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemCall(usuarios.get(position).getTelefono());
            }
        });

        holder.root.findViewById(R.id.renglon_mensaje_estilista).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(usuarios.get(position),idusuarios.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    //OBSERVER
    public interface OnItemClickListener{
        void onItemClick(Cliente usuario,String idusuario);
        void onItemCall(String telefono);
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
