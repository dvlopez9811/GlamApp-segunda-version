package proyectohastafinal.almac.myapplication;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class AdapterCatalogo extends BaseAdapter {

    private Context context;

    ArrayList data;


    public AdapterCatalogo(Context context) {
        this.context = context;
        data = new ArrayList();

        data.add("a");
        data.add("b");
        data.add("c");
        data.add("d");
        data.add("e");
        data.add("f");
        data.add("g");
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_cuadricula_catalogo, viewGroup, false);
        }

        ImageView imagen = view.findViewById(R.id.imagen_catalogo_informacion_salon_activity);
        TextView descripcion = view.findViewById(R.id.descripcion_imagen_catalogo_informacion_activity);

        Glide.with(imagen.getContext()).load(R.drawable.ejemplo_catalogo).into(imagen);
        descripcion.setText("Betty");
        return view;
    }

    public void showCatalogo(String nombreSalon){

    }
}
