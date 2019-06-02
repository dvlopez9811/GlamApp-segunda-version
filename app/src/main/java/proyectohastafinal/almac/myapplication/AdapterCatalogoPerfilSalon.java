package proyectohastafinal.almac.myapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterCatalogoPerfilSalon extends BaseAdapter {


    private Context context;
    ArrayList<Uri> uris;


    public AdapterCatalogoPerfilSalon(Context cont,ArrayList<Uri> nUris){
        context=cont;
        uris = nUris;
    }
    @Override
    public int getCount() {
        return uris.size();
    }

    @Override
    public Object getItem(int position) {
        return uris.get(position);
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
            view = inflater.inflate(R.layout.item_catalogo_perfil_salon, viewGroup, false);
        }

        ImageView imagen = view.findViewById(R.id.imagen_catalogo_perfil_salon_activity);
        TextView descripcion = view.findViewById(R.id.descripcion_imagen_catalogo_perfil_activity);

        Glide.with(imagen.getContext()).load(uris.get(position)).into(imagen);
        return view;
    }

    public void showCatalogo(String nombreSalon){

    }
}
