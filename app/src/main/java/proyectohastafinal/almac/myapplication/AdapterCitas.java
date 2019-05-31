package proyectohastafinal.almac.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.Cita;

public class AdapterCitas extends RecyclerView.Adapter<AdapterCitas.CustomViewHolder>{

    ArrayList<Cita> citas;


    public void agregarcita(Cita cita){
        citas.add(cita);
        notifyDataSetChanged();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;


        }

    }

    public AdapterCitas(){
        citas = new ArrayList<>();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.renglon_cita, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

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

       ((TextView) holder.root.findViewById(R.id.salon_renglon_cita)).setText(citas.get(position).getNombreSalon());
        ((TextView) holder.root.findViewById(R.id.servicio_renglon_cita)).setText(citas.get(position).getServicio());
        ((TextView) holder.root.findViewById(R.id.horainicio_renglon_cita)).setText(horarioinicio);
        final ImageView iv_menu_cita_renglon_cita = holder.root.findViewById(R.id.iv_menu_cita_renglon_cita);
        iv_menu_cita_renglon_cita.setOnClickListener(new View.OnClickListener() {
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

    //OBSERVER
    public interface OnItemClickListener{
        void onItemClick(View v,Cita cita);
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
