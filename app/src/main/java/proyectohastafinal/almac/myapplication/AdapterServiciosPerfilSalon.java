package proyectohastafinal.almac.myapplication;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import proyectohastafinal.almac.myapplication.model.Servicio;

public class AdapterServiciosPerfilSalon extends RecyclerView.Adapter<AdapterServiciosPerfilSalon.CustomViewHolder> {


    ArrayList<Servicio> data;
    HashMap<String, Boolean> checked;

    public AdapterServiciosPerfilSalon(){
        data = new ArrayList();
        checked = new HashMap<String, Boolean>();
        checked.put("Maquillaje", false);
        checked.put("Depilación", false);
        checked.put("Masaje", false);
        checked.put("Peluquería", false);
        checked.put("Uñas", false);

    }


    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.renglon_servicio_perfil_salon_fragment, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        String tipo = data.get(position).getTipo();
        Object aux = data.get(position).getSalonDeBelleza();

        ((TextView) holder.root.findViewById(R.id.checkbox_servicio_renglon_servicio_perfil_fragmen)).setText(tipo);
        if (tipo.equals("Maquillaje")) {
            ((ImageView) holder.root.findViewById(R.id.imagen_servicio_renglon_perfil_fragment)).setImageResource(R.drawable.ic_new_maquillaje_morado);
            if (aux != null) {
                ((CheckBox) holder.root.findViewById(R.id.checkbox_servicio_renglon_servicio_perfil_fragmen)).setEnabled(false);
                ((CheckBox) holder.root.findViewById(R.id.checkbox_servicio_renglon_servicio_perfil_fragmen)).setOnClickListener(new CompoundButton.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        if (!checked.get("Maquillaje")){
                            checked.replace("Maquillaje", true);
                        } else {
                            checked.replace("Maquillaje", false);
                        }
                    }
                });
            }
        } else if (tipo.equals("Depilación")) {
            ((ImageView) holder.root.findViewById(R.id.imagen_servicio_renglon_perfil_fragment)).setImageResource(R.drawable.ic_new_depilacion_morado);
            if (aux != null) {
                ((CheckBox) holder.root.findViewById(R.id.checkbox_servicio_renglon_servicio_perfil_fragmen)).setEnabled(false);
            }
            ((CheckBox) holder.root.findViewById(R.id.checkbox_servicio_renglon_servicio_perfil_fragmen)).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    if (!checked.get("Depilación")){
                        checked.replace("Depilación", true);
                    } else {
                        checked.replace("Depilación", false);
                    }
                }
            });
        } else if (tipo.equals("Masaje")) {
            ((ImageView) holder.root.findViewById(R.id.imagen_servicio_renglon_perfil_fragment)).setImageResource(R.drawable.ic_new_masaje_morado);
            if (aux != null) {
                ((CheckBox) holder.root.findViewById(R.id.checkbox_servicio_renglon_servicio_perfil_fragmen)).setEnabled(false);
            }
            ((CheckBox) holder.root.findViewById(R.id.checkbox_servicio_renglon_servicio_perfil_fragmen)).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    if (!checked.get("Masaje")){
                        checked.replace("Masaje", true);
                    } else {
                        checked.replace("Masaje", false);
                    }
                }
            });
        } else if (tipo.equals("Peluquería")) {
            ((ImageView) holder.root.findViewById(R.id.imagen_servicio_renglon_perfil_fragment)).setImageResource(R.drawable.ic_new_peluqueria_morado);
            if (aux != null) {
                ((CheckBox) holder.root.findViewById(R.id.checkbox_servicio_renglon_servicio_perfil_fragmen)).setEnabled(false);
            }
            ((CheckBox) holder.root.findViewById(R.id.checkbox_servicio_renglon_servicio_perfil_fragmen)).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    if (!checked.get("Peluquería")){
                        checked.replace("Peluquería", true);
                    } else {
                        checked.replace("Peluquería", false);
                    }
                }
            });
        } else if (tipo.equals("Uñas")) {
            ((ImageView) holder.root.findViewById(R.id.imagen_servicio_renglon_perfil_fragment)).setImageResource(R.drawable.uc_new_unas_morado);
            if (aux != null) {
                ((CheckBox) holder.root.findViewById(R.id.checkbox_servicio_renglon_servicio_perfil_fragmen)).setEnabled(false);
            }
            ((CheckBox) holder.root.findViewById(R.id.checkbox_servicio_renglon_servicio_perfil_fragmen)).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    String a = data.get(position).getTipo();
                    if (!checked.get("Uñas")){
                        checked.replace("Uñas", true);
                    } else {
                        checked.replace("Uñas", false);
                    }
                }
            });
        }


    }

    public HashMap<String, Boolean> getChecked () {
        return checked;
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
