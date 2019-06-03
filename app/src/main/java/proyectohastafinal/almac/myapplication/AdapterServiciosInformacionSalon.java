package proyectohastafinal.almac.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.Servicio;

public class AdapterServiciosInformacionSalon extends RecyclerView.Adapter<AdapterServiciosInformacionSalon.CustomViewHolder>{

    ArrayList<Servicio> servicios;


    public AdapterServiciosInformacionSalon(){
        servicios = new ArrayList<>();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.renglon_servicio_informacion_salon_activity, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        String tipo=servicios.get(position).getTipo();
        ((TextView) holder.root.findViewById(R.id.nombre_servicio_renglon_servicio_informacion)).setText(tipo);
        if(tipo.equals("Maquillaje"))
            ((ImageView)holder.root.findViewById(R.id.imagen_servicio_renglon_informacion_activity)).setImageResource(R.drawable.ic_new_maquillaje_morado);
        else if(tipo.equals("Depilación"))
            ((ImageView)holder.root.findViewById(R.id.imagen_servicio_renglon_informacion_activity)).setImageResource(R.drawable.ic_new_depilacion_morado);
        else if(tipo.equals("Masaje"))
            ((ImageView)holder.root.findViewById(R.id.imagen_servicio_renglon_informacion_activity)).setImageResource(R.drawable.ic_new_masaje_morado);
        else if(tipo.equals("Peluquería"))
            ((ImageView)holder.root.findViewById(R.id.imagen_servicio_renglon_informacion_activity)).setImageResource(R.drawable.ic_new_peluqueria_morado);
        else if(tipo.equals("Uñas"))
            ((ImageView)holder.root.findViewById(R.id.imagen_servicio_renglon_informacion_activity)).setImageResource(R.drawable.uc_new_unas_morado);



        //TODO implementar mostrar precio cuando haya sido hecho tambien en el registro
    }

    @Override
    public int getItemCount() {
        return servicios.size();
    }

    public void showAllServicios(ArrayList<Servicio> allservicios) {
        for(int i = 0 ; i<allservicios.size() ; i++){
            if(!servicios.contains(allservicios.get(i))) servicios.add(allservicios.get(i));
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
