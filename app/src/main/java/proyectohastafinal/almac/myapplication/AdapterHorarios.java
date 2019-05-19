package proyectohastafinal.almac.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.Horario;
import proyectohastafinal.almac.myapplication.model.Servicio;

public class AdapterHorarios extends RecyclerView.Adapter<AdapterHorarios.CustomViewHolder> {

    FirebaseDatabase rtdb;
    ArrayList<Horario> data;

    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    public AdapterHorarios(){
        data = new ArrayList<>();
    }
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.renglon_horario_disponible, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        rtdb = FirebaseDatabase.getInstance();
        return vh;
    }

    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {
        ((TextView) holder.root.findViewById(R.id.hora_inicio_item_horarios)).setText(data.get(position).getHoraInicio());
        ((TextView) holder.root.findViewById(R.id.hora_fin_item_horarios)).setText(data.get(position).getHoraFinal());
        ((CheckBox) holder.root.findViewById(R.id.checked_horario)).setChecked(data.get(position).isSeleccionado());
        ((CheckBox) holder.root.findViewById(R.id.checked_horario)).setTag(new Integer(position));

        if(position == 0 && data.get(0).isSeleccionado() && ((CheckBox)holder.root.findViewById(R.id.checked_horario)).isChecked()) {
            lastChecked = holder.root.findViewById(R.id.checked_horario);
            lastCheckedPos = 0;}

        holder.root.findViewById(R.id.checked_horario).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                int clickedPos = ((Integer) cb.getTag()).intValue();
                if (cb.isChecked()) {
                    if (lastChecked != null) {
                        lastChecked.setChecked(false);
                        data.get(lastCheckedPos).setSeleccionado(false);
                    }
                    lastChecked = cb;
                    lastCheckedPos = clickedPos;
                } else
                    lastChecked = null;
                data.get(clickedPos).setSeleccionado(cb.isChecked());
            }
        });

    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;

        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;
        }
    }

    public void showAllHorarios(ArrayList<Horario> allhorarios) {
        for(int i = 0 ; i<allhorarios.size() ; i++){
            if(!data.contains(allhorarios.get(i))) data.add(allhorarios.get(i));
        }
        notifyDataSetChanged();
    }
    public int getItemCount() {
        return data.size();
    }
}
