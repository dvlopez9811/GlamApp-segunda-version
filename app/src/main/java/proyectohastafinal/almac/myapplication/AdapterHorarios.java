package proyectohastafinal.almac.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.Horario;

public class AdapterHorarios extends RecyclerView.Adapter<AdapterHorarios.CustomViewHolder> {

    ArrayList<Horario> data;

    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    public AdapterHorarios(){
        data = new ArrayList<>();
    }


    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.renglon_horario_disponible, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;

        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;
        }
    }

    public void showAllHorarios(ArrayList<Horario> allhorarios) {
        data = new ArrayList<>();
        for(int i = 0 ; i<allhorarios.size() ; i++){
            data.add(allhorarios.get(i));
        }
        notifyDataSetChanged();
    }
    public int getItemCount() {
        return data.size();
    }

    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {


        //Hora inicio
        int horarioinic = data.get(position).getHoraInicio();
        String horarioinicio = "";
        if(data.get(position).getHoraInicio()<12){
            horarioinicio=horarioinic+"a.m";
        }
        else {
            if(horarioinic!=12)
            horarioinic-=12;
            horarioinicio=horarioinic+" p.m";
        }

        //Hora final
        int horariofin = data.get(position).getHoraFinal();
        String horariofinal = "";
        if(data.get(position).getHoraFinal()<12){
            horariofinal=horariofin+"a.m";
        }
        else {
            if(horariofin!=12)
            horariofin-=12;
            horariofinal=horariofin+" p.m";
        }



        ((TextView) holder.root.findViewById(R.id.hora_inicio_item_horarios)).setText(horarioinicio);
        ((TextView) holder.root.findViewById(R.id.hora_fin_item_horarios)).setText(horariofinal);

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

    //OBSERVER
    public interface OnItemClickListener{
        void onItemClick(Horario horario);
    }

    private AdapterHorarios.OnItemClickListener listener;

    public void setListener(AdapterHorarios.OnItemClickListener listener){
        this.listener = listener;
    }


}
