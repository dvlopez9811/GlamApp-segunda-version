package proyectohastafinal.almac.myapplication;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.BusquedaSalonDeBelleza;
import proyectohastafinal.almac.myapplication.model.Cita;
import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;

public class AdapterSalones extends RecyclerView.Adapter<AdapterSalones.CustomViewHolder> {

    protected ArrayList<BusquedaSalonDeBelleza> salones;
    protected Activity activity;

    // Renglon de la lista
    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        // Es un objeto tipo LinearLayout porque el padre es Layout
        public LinearLayout root;
        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;
        }
    }


    public AdapterSalones(){
        salones = new ArrayList<>();

    }
    // Crear el renglón. Generación.
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Un inflater transforma cualquier xml en un view.
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_salones, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    // Se pone información al renglón. Utilización.
    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        ((TextView) holder.root.findViewById(R.id.txt_item_nombre_salon)).setText(salones.get(position).getNombreSalonDeBelleza());
        ((TextView) holder.root.findViewById(R.id.txt_item_direccion_salon)).setText(salones.get(position).getDireccionSalonDeBelleza());
        ((TextView) holder.root.findViewById(R.id.txt_item_distancia_a_salon)).setText(salones.get(position).getDistanciaASalonDeBelleza());
        holder.root.findViewById(R.id.item_salon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(salones.get(position));
            }
        });
    }


    //OBSERVER
    public interface OnItemClickListener{
        void onItemClick(BusquedaSalonDeBelleza salonDeBelleza);
    }

    private AdapterSalones.OnItemClickListener listener;

    public void setListener(AdapterSalones.OnItemClickListener listener){
        this.listener = listener;
    }

    public void agregarSalon(BusquedaSalonDeBelleza salonDeBelleza){
        if(!salones.contains(salonDeBelleza))
            salones.add(salonDeBelleza);
        notifyDataSetChanged();
    }

    // Número de items que se tienen.
    @Override
    public int getItemCount() {
        return salones.size();
    }

    public void actualizarDistancia (BusquedaSalonDeBelleza busquedaSalonDeBelleza){
        for (int i = 0; i < salones.size(); i++){
            if( salones.get(i).equals(busquedaSalonDeBelleza) ) {
                salones.get(i).setDistanciaASalonDeBelleza(busquedaSalonDeBelleza.getDistanciaASalonDeBelleza());

            }
        }
        notifyDataSetChanged();
    }

    public BusquedaSalonDeBelleza darItem(int posicion){
        return salones.get(posicion);
    }

    public void limpiarSalones(){
        salones = new ArrayList<>();
        notifyDataSetChanged();
    }

}