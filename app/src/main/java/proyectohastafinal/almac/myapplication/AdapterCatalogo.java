package proyectohastafinal.almac.myapplication;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
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
    ArrayList<Uri> uris;


    public AdapterCatalogo(Context context,ArrayList<Uri> nUris) {
        this.context = context;
        this.uris = nUris;
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
            view = inflater.inflate(R.layout.item_cuadricula_catalogo, viewGroup, false);
        }

        ImageView imagen = view.findViewById(R.id.imagen_catalogo_informacion_salon_activity);
        TextView descripcion = view.findViewById(R.id.descripcion_imagen_catalogo_informacion_activity);

        Glide.with(imagen.getContext()).load(uris.get(position)).into(imagen);


        //     ((InformacionSalonActivity)context).runOnUiThread( ()-> {
        //       for (int i = 0; i < uris.size(); i++) {

        //           Glide.with(imagen.getContext()).load(uris.get(i)).into(imagen);
        //           descripcion.setText("Betty");
        //        }
        //      });

        return view;
    }



    public void showCatalogo(ArrayList<Uri> nUris){
        for(int i = 0 ; i<nUris.size() ; i++){
            if(!uris.contains(nUris.get(i))) uris.add(nUris.get(i));
        }
        notifyDataSetChanged();
    }
}
