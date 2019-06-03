package proyectohastafinal.almac.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import static proyectohastafinal.almac.myapplication.model.Constantes.*;

import proyectohastafinal.almac.myapplication.model.BusquedaSalonDeBelleza;

public class AdapterSalones extends BaseAdapter {

    protected Context context;
    LayoutInflater inflater;
    ArrayList<BusquedaSalonDeBelleza> salones;
    ArrayList<BusquedaSalonDeBelleza> copiaSalones;

    public AdapterSalones(Context context, ArrayList<BusquedaSalonDeBelleza> salones){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.salones = salones;
        copiaSalones = new ArrayList<>();
        copiaSalones.addAll(salones);
    }

    @Override
    public int getCount() {
        return salones.size();
    }

    @Override
    public Object getItem(int arg0) {
        return salones.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_salones, null);
        }

        BusquedaSalonDeBelleza busquedaSalonDeBelleza = salones.get(position);
        TextView txt_item_nombre_salon = v.findViewById(R.id.txt_item_nombre_salon);
        TextView txt_item_direccion_salon = v.findViewById(R.id.txt_item_direccion_salon);
        TextView txt_item_distancia_a_salon = v.findViewById(R.id.txt_item_distancia_a_salon);
        v.findViewById(R.id.item_salon).setOnClickListener(v1 -> listener.onItemClick(salones.get(position)));

        txt_item_nombre_salon.setText(busquedaSalonDeBelleza.getNombreSalonDeBelleza());
        txt_item_direccion_salon.setText(busquedaSalonDeBelleza.getDireccionSalonDeBelleza());
        txt_item_distancia_a_salon.setText(busquedaSalonDeBelleza.getDistanciaASalonDeBelleza());
        return v;
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

    public void filtrar(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        Log.e("fdfd",copiaSalones.size()+"");
        salones.clear();
        if (charText.length() == 0)
            salones.addAll(copiaSalones);
        else {
            for (BusquedaSalonDeBelleza busquedaSalonDeBelleza : copiaSalones)
                if(busquedaSalonDeBelleza.getNombreSalonDeBelleza().toLowerCase(Locale.getDefault()).contains(charText))
                    salones.add(busquedaSalonDeBelleza);
        }
        notifyDataSetChanged();
    }
}