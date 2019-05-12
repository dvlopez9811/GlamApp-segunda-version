package proyectohastafinal.almac.myapplication;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;

public class AdapterSalones extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<SalonDeBelleza> salones;

    public AdapterSalones (Activity activity, ArrayList<SalonDeBelleza> salones) {
        this.activity = activity;
        this.salones = salones;
    }

    public void agregarSalones(ArrayList<SalonDeBelleza> salones) {
        for (int i = 0; i < salones.size(); i++) {
            this.salones.add(salones.get(i));
        }
    }

    @Override
    public int getCount() {
        return salones.size();
    }

    @Override
    public SalonDeBelleza getItem(int position) {
        return salones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_salones,null);
        }

        SalonDeBelleza salonDeBelleza = salones.get(position);

        return v;
    }
}