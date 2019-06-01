package proyectohastafinal.almac.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.Servicio;

public class AdapterServiciosPerfilSalon extends RecyclerView.Adapter<AdapterServiciosPerfilSalon.CustomViewHolder> {


    ArrayList<Servicio> data;

    public AdapterServiciosPerfilSalon(){
        data = new ArrayList();
    }


    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.renglon_servicio_perfil_salon_fragment, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        String tipo=data.get(position).getTipo();
        ((TextView) holder.root.findViewById(R.id.nombre_servicio_renglon_servicio_perfil_fragmen)).setText(tipo);
        if(tipo.equals("Maquillaje"))
            ((ImageView)holder.root.findViewById(R.id.imagen_servicio_renglon_perfil_fragment)).setImageResource(R.drawable.ic_new_maquillaje_morado);
        else if(tipo.equals("Depilación"))
            ((ImageView)holder.root.findViewById(R.id.imagen_servicio_renglon_perfil_fragment)).setImageResource(R.drawable.ic_new_depilacion_morado);
        else if(tipo.equals("Masaje"))
            ((ImageView)holder.root.findViewById(R.id.imagen_servicio_renglon_perfil_fragment)).setImageResource(R.drawable.ic_new_masaje_morado);
        else if(tipo.equals("Peluquería"))
            ((ImageView)holder.root.findViewById(R.id.imagen_servicio_renglon_perfil_fragment)).setImageResource(R.drawable.ic_new_peluqueria_morado);
        else if(tipo.equals("Uñas"))
            ((ImageView)holder.root.findViewById(R.id.imagen_servicio_renglon_perfil_fragment)).setImageResource(R.drawable.uc_new_unas_morado);

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
