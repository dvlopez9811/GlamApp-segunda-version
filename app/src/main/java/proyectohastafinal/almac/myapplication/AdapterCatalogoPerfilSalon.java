package proyectohastafinal.almac.myapplication;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.FotoCatalogo;

public class AdapterCatalogoPerfilSalon extends BaseAdapter {


    private Context context;
    ArrayList<FotoCatalogo> fotoCatalogos;
    FirebaseStorage storage;
    private String type;

    public AdapterCatalogoPerfilSalon(Context cont,ArrayList<FotoCatalogo> fotoCatalogos, String type){
        context=cont;
        storage = FirebaseStorage.getInstance();
        this.fotoCatalogos = fotoCatalogos;
        this.type = type;
    }


    @Override
    public int getCount() {
        return fotoCatalogos.size();
    }

    @Override
    public Object getItem(int position) {
        return fotoCatalogos.get(position);
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

        EditText descripcion = view.findViewById(R.id.descripcion_imagen_catalogo_perfil_activity);
        EditText precio = view.findViewById(R.id.precio_imagen_catalogo_perfil_activity);

        ImageButton btn_editar_descripcion_catalogo = view.findViewById(R.id.btn_editar_descripcion_catalogo);
        ImageButton btn_editar_precio_catalogo = view.findViewById(R.id.btn_editar_precio_catalogo);

        if (type.equals("User")) {
            btn_editar_descripcion_catalogo.setVisibility(View.INVISIBLE);
            btn_editar_precio_catalogo.setVisibility(View.INVISIBLE);
        } else {
            btn_editar_descripcion_catalogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                }
            });

            btn_editar_precio_catalogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                }
            });
        }

        StorageReference ref = storage.getReference().child("salones de belleza").child(fotoCatalogos.get(position).getNombreSalon()).child(fotoCatalogos.get(position).getServicio()).child(fotoCatalogos.get(position).getNombreFoto());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(imagen.getContext()).load(uri).into(imagen);
            }
        });

        descripcion.setText(fotoCatalogos.get(position).getDescripcionFoto());
        precio.setText(fotoCatalogos.get(position).getPrecioFoto());


        return view;
    }

}
