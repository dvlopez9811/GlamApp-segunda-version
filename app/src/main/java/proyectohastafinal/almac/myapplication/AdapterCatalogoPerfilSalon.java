package proyectohastafinal.almac.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.FotoCatalogo;

public class AdapterCatalogoPerfilSalon extends BaseAdapter {


    private Context context;
    ArrayList<FotoCatalogo> fotoCatalogos;
    FirebaseStorage storage;
    FirebaseDatabase rtdb;
    private String type;

    String contenidoDescripcion="";
    String contenidoPrecio="";

    public AdapterCatalogoPerfilSalon(Context cont,ArrayList<FotoCatalogo> fotoCatalogos, String type){
        context=cont;
        storage = FirebaseStorage.getInstance();
        rtdb=FirebaseDatabase.getInstance();
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
            view.findViewById(R.id.iv_eliminar_foto).setVisibility(View.INVISIBLE);
        } else {
            btn_editar_descripcion_catalogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    if (!descripcion.isEnabled()) {
                        btn_editar_descripcion_catalogo.setBackgroundResource(R.drawable.ic_chulo_blanco);
                        descripcion.setEnabled(true);
                        descripcion.performClick();
                        contenidoDescripcion = descripcion.getText().toString();
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        descripcion.setSelection(descripcion.getText().length());
                        descripcion.requestFocus();
                        imm.showSoftInput(descripcion, InputMethodManager.SHOW_IMPLICIT);
                    } else {
                        String nuevaDescripcion = descripcion.getText().toString();
                        if (!nuevaDescripcion.equals(contenidoDescripcion)) {
                            rtdb.getReference().child("fotos").child(fotoCatalogos.get(position).getNombreSalon()).
                                    child(fotoCatalogos.get(position).getServicio()).child(fotoCatalogos.get(position)
                                    .getNombreFoto()).child("descripcionFoto").setValue(nuevaDescripcion);
                        }
                        btn_editar_descripcion_catalogo.setBackgroundResource(R.drawable.edit_blanco);
                        descripcion.setEnabled(false);
                    }


                }
            });

            btn_editar_precio_catalogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!precio.isEnabled()) {
                        btn_editar_precio_catalogo.setBackgroundResource(R.drawable.ic_chulo_blanco);
                        precio.setEnabled(true);
                        contenidoPrecio = precio.getText().toString();
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        precio.setSelection(precio.getText().length());
                        precio.requestFocus();
                        imm.showSoftInput(precio, InputMethodManager.SHOW_IMPLICIT);
                    } else {
                        String nuevoPrecio = precio.getText().toString();
                        if (!nuevoPrecio.equals(contenidoPrecio)) {
                            rtdb.getReference().child("fotos").child(fotoCatalogos.get(position).getNombreSalon()).
                                    child(fotoCatalogos.get(position).getServicio()).child(fotoCatalogos.get(position)
                                    .getNombreFoto()).child("precioFoto").setValue(nuevoPrecio);
                        }
                        btn_editar_precio_catalogo.setBackgroundResource(R.drawable.edit_blanco);
                        precio.setEnabled(false);
                    }
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



        view.findViewById(R.id.iv_eliminar_foto).setOnClickListener(v -> {

            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(v.getContext());
            dialogo1.setTitle("Eliminar Foto");
            dialogo1.setMessage("¿ Deseas eliminar esta foto ?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ref.delete();
                    rtdb.getReference().child("fotos").child(fotoCatalogos.get(position).getNombreSalon())
                            .child(fotoCatalogos.get(position).getServicio()).child(fotoCatalogos.get(position).getNombreFoto())
                            .removeValue();
                }
            });
            dialogo1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialogo1.show();

        });


        return view;
    }

}
