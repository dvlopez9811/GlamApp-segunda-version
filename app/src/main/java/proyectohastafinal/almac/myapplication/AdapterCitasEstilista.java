package proyectohastafinal.almac.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import proyectohastafinal.almac.myapplication.model.Estilista;
import proyectohastafinal.almac.myapplication.model.Servicio;

public class AdapterCitasEstilista extends RecyclerView.Adapter<AdapterCitasEstilista.CustomViewHolder>{

    ArrayList<Cita> citas;
    FirebaseDatabase rtdb;


//    public void agregarcita(Cita cita){
//        citas.add(cita);
//        notifyDataSetChanged();
//    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;
        }
    }

    public AdapterCitasEstilista(){
        rtdb = FirebaseDatabase.getInstance();
        citas = new ArrayList<>();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.renglon_cita_estilista, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        Cita aux = citas.get(position);

        Log.d("CITASSS", aux.getIdUsuario() + "");

        rtdb.getReference().child("usuario").child(citas.get(position).getIdUsuario()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Cliente cliente = dataSnapshot.getValue(Cliente.class);

                ((TextView) holder.root.findViewById(R.id.usuario_renglon_cita_estilista)).setText(cliente.getUsuario());
                ((TextView) holder.root.findViewById(R.id.servicio_renglon_cita_estilista)).setText(citas.get(position).getServicio());

                //Hora inicio
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

                ((TextView) holder.root.findViewById(R.id.horainicio_renglon_cita_estilista)).setText(horarioinicio);
                final Button btn_cancelar_cita_estilista = holder.root.findViewById(R.id.btn_cancelar_renglon_cita_estilista);
                btn_cancelar_cita_estilista.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(citas.get(position));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
    public interface OnItemClickListener{
        void onItemClick(Cita cita);
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
