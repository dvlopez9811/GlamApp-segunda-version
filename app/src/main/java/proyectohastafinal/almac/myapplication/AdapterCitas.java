package proyectohastafinal.almac.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.Cita;

public class AdapterCitas extends RecyclerView.Adapter<AdapterCitas.CustomViewHolder> {

    ArrayList<Cita> citas;

    public void mostrarcitas(ArrayList<Cita> allcitas){
        for (int i=0;i<allcitas.size();i++) {
            if (!citas.contains(allcitas.get(i))) citas.add(allcitas.get(i));
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
       ((TextView) holder.root.findViewById(R.id.salon_renglon_cita)).setText(citas.get(position).getNombreSalon());
        ((TextView) holder.root.findViewById(R.id.servicio_renglon_cita)).setText(citas.get(position).getServicio());
        ((TextView) holder.root.findViewById(R.id.horainicio_renglon_cita)).setText(citas.get(position).getHorainicio());
    }

    @Override
    public int getItemCount() {
        return citas.size();
    }

}
