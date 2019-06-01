package proyectohastafinal.almac.myapplication;

import android.content.Context;
import android.graphics.Color;
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

    ArrayList<Integer> horasdisponibles;

    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    private LinearLayout ll;

    public AdapterHorarios(){
        horasdisponibles = new ArrayList<>();
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

    public void showAllHorasDisponibles(ArrayList<Integer> allhorasdisponibles) {
        horasdisponibles = new ArrayList<>();
        for(int i = 0 ; i<allhorasdisponibles.size() ; i++){
            horasdisponibles.add(allhorasdisponibles.get(i));
        }
        notifyDataSetChanged();
    }
    public int getItemCount() {
        return horasdisponibles.size();
    }

    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {


        //Hora inicio
        int horarioinic = horasdisponibles.get(position);
        String horarioinicio = "";
        if(horasdisponibles.get(position)<12){
            horarioinicio=horarioinic+"a.m";
        }
        else {
            if(horarioinic!=12)
            horarioinic-=12;
            horarioinicio=horarioinic+" p.m";
        }

        //Hora final
        int horariofin = horasdisponibles.get(position)+1;
        String horariofinal = "";
        if((horasdisponibles.get(position)+1)<12){
            horariofinal=horariofin+"a.m";
        }
        else {
            if(horariofin!=12)
            horariofin-=12;
            horariofinal=horariofin+" p.m";
        }



        ((TextView) holder.root.findViewById(R.id.hora_inicio_item_horarios)).setText(horarioinicio);
        ((TextView) holder.root.findViewById(R.id.hora_fin_item_horarios)).setText(horariofinal);


        holder.root.findViewById(R.id.renglon_horario_disponible).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ll!=null)
                    ll.setBackgroundColor(Color.TRANSPARENT);

                LinearLayout llay = holder.root.findViewById(R.id.renglon_horario_disponible);
                llay.setBackgroundColor(Color.GREEN);
                ll = llay;

                listener.onClickHorario(horasdisponibles.get(position),holder.root.getContext());
            }
        });

    }

    //OBSERVER
    public interface OnItemClickListener{
        void onClickHorario(int horaseleccionada, Context context);
    }

    private AdapterHorarios.OnItemClickListener listener;

    public void setListener(AdapterHorarios.OnItemClickListener listener){
        this.listener = listener;
    }


}
