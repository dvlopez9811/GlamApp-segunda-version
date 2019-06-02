package proyectohastafinal.almac.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import proyectohastafinal.almac.myapplication.model.Marcador;
import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;
import proyectohastafinal.almac.myapplication.model.Servicio;
import proyectohastafinal.almac.myapplication.util.UtilDomi;


public class PerfilSalonFragment extends Fragment {

    private static final int GALLERY_CALLBACK_ID = 101;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private ExpandableHeightGridView gridCatalogo;
    private AdapterCatalogoPerfilSalon adapterCatalogo;
    private RecyclerView listaServicios;
    private AdapterServiciosPerfilSalon adapterServicios;

    FirebaseDatabase rtdb;
    FirebaseAuth auth;
    FirebaseStorage storage;

    private String nombreSalon;
    private String direccionActual;
    private static PerfilSalonFragment instance;
    private ArrayList<String> serviciosViejos;
    private ArrayList<String> serviciosNuevos;
    private File photoFile;

    private ImageView auxFotoPerfilSalon;

    public static PerfilSalonFragment getInstance() {
        instance = instance == null ? new PerfilSalonFragment() : instance;
        return instance;
    }


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public PerfilSalonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilSalonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilSalonFragment newInstance(String param1, String param2) {
        PerfilSalonFragment fragment = new PerfilSalonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        rtdb=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View mView = inflater.inflate(R.layout.fragment_perfil_salon, container, false);
        rtdb.getReference().child("identificador").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nombreSalon = dataSnapshot.getValue(String.class);
                ((TextView) mView.findViewById(R.id.titulo_nombre_salon_perfil_fragment_)).setText(nombreSalon);

                //Aqui ya esta disponible el nombre
                rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("direccion").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String direccion = dataSnapshot.getValue(String.class);
                        ((EditText)mView.findViewById(R.id.et_uibacion_editable_perfil_salon_fragment)).setText(direccion);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                ( mView.findViewById(R.id.boton_editar_servicios_perfil_salon_fragment)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        serviciosNuevos = new ArrayList<>();

                        HashMap<String, Boolean> checked = adapterServicios.getChecked();

                        for (Map.Entry<String, Boolean> entry : checked.entrySet()) {
                            if (entry.getValue()) {
                                serviciosNuevos.add(entry.getKey());
                            }
                        }

                        ArrayList<String> nuevosServicios = compararListasServicios();

                        for (int i = 0; i < nuevosServicios.size(); i++) {
                            rtdb.getReference().child("Buscar servicios salon de belleza").child(nuevosServicios.get(i)).child(nombreSalon).push().setValue(nombreSalon);
                        }

                        rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("servicios").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                HashMap<String, Boolean> aux = (HashMap<String, Boolean>) dataSnapshot.getValue();
                                for (int i = 0; i < serviciosNuevos.size(); i++) {
                                    aux.put(serviciosNuevos.get(i), true);
                                }
                                rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("servicios").setValue(aux);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });

                ( mView.findViewById(R.id.boton_editar_ubicacion_perfil_salon_fragment)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Habilita edicion
                        if(!mView.findViewById(R.id.et_uibacion_editable_perfil_salon_fragment).isEnabled()) {
                            direccionActual=((EditText)mView.findViewById(R.id.et_uibacion_editable_perfil_salon_fragment)).getText().toString();
                            (mView.findViewById(R.id.et_uibacion_editable_perfil_salon_fragment)).setEnabled(true);
                            (mView.findViewById(R.id.boton_editar_ubicacion_perfil_salon_fragment)).setBackgroundResource(R.drawable.ic_chulo);
                            //Deshabilita edicion
                        }else{
                            (mView.findViewById(R.id.et_uibacion_editable_perfil_salon_fragment)).setEnabled(false);
                            (mView.findViewById(R.id.boton_editar_ubicacion_perfil_salon_fragment)).setBackgroundResource(R.drawable.edit);
                            String direccionIngresada=((EditText)mView.findViewById(R.id.et_uibacion_editable_perfil_salon_fragment)).getText().toString();
                            if(!direccionIngresada.equals(direccionActual)){
                                Geocoder gc = new Geocoder(mView.getContext());
                                double latitud = 0, longitud = 0;
                                try {
                                    List<Address> list = gc.getFromLocationName(direccionIngresada + "Cali CO", 1);
                                    Address add = list.get(0);
                                    String locality = add.getLocality();
                                    latitud = add.getLatitude();
                                    longitud = add.getLongitude();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Marcador marcador = new Marcador(latitud,longitud,nombreSalon);
                                rtdb.getReference().child("Marcador").child(nombreSalon).setValue(marcador);
                                rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("direccion").setValue(direccionIngresada);
                                rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("latitud").setValue(latitud);
                                rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("longitud").setValue(longitud);

                                Toast.makeText(inflater.getContext(),"La dirección ha sido cambiada exitosamente",Toast.LENGTH_SHORT);
                            }
                        }
                    }
                });

                // TODO para varela, esto se encarga de estar pendiente del texto para cambiar la foto y abrir la galeria y a ver el onActivityResult
                ( mView.findViewById(R.id.cambiar_imagen_perfil_salon_fragment)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        i.setType("image/*");
                        startActivityForResult(i, GALLERY_CALLBACK_ID);

                    }
                });

                //Mostrar foto
                StorageReference ref = storage.getReference().child("salones de belleza").child(nombreSalon).child("profile");
                if(ref == null){
                    Log.e("hola","es nulllllllllll");
                }

                auxFotoPerfilSalon = mView.findViewById(R.id.imagen_perfil_perfil_salon_fragment);

                ref.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(mView.getContext())
                            .load(uri).into((ImageView) mView.findViewById(R.id.imagen_perfil_perfil_salon_fragment)));

                final long[] fotos = new long[1];
                final ArrayList<Uri> uris = new ArrayList<>();
                //Catalogo
                gridCatalogo =  mView.findViewById(R.id.grid_Catalogo_informacion_salon_activity);
                adapterCatalogo= new AdapterCatalogoPerfilSalon(mView.getContext(),uris);
                gridCatalogo.setAdapter(adapterCatalogo);
                gridCatalogo.setExpanded(true);
                // Catálogo
                Log.d("NOMBRE SALIN", nombreSalon);
                rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("fotos").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.e("entra>>>>>>>>>>>>>>", nombreSalon);
                        long fotos = (long) dataSnapshot.getValue();
                        for (int i = 1; i <= fotos; i++){
                            StorageReference ref2 = storage.getReference().child("salones de belleza").child(nombreSalon).child(i+".png");
                            ref2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    uris.add(uri);
                                    adapterCatalogo.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


                //Mostrar servicios en adapter
                adapterServicios= new AdapterServiciosPerfilSalon();
                listaServicios=mView.findViewById(R.id.listado_servicios_perfil_salon_fragment);
                listaServicios.setLayoutManager(new LinearLayoutManager(mView.getContext()));

                rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("servicios").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        serviciosViejos = new ArrayList<>();
                        ArrayList<Servicio> servicios=new ArrayList<>();
                        for (DataSnapshot dsp: dataSnapshot.getChildren()){
                            if(dsp.getValue(Boolean.class)) {
                                SalonDeBelleza s = new SalonDeBelleza();
                                s.setNombreSalonDeBelleza(nombreSalon);
                                Servicio servicioNuevo = new Servicio(dsp.getKey(),s);
                                servicios.add(servicioNuevo);
                                serviciosViejos.add(servicioNuevo.getTipo());
                            }
                        }

                        ArrayList<String> serviciosNombre = new ArrayList<>();

                        serviciosNombre.add("Maquillaje");
                        serviciosNombre.add("Depilación");
                        serviciosNombre.add("Masaje");
                        serviciosNombre.add("Peluquería");
                        serviciosNombre.add("Uñas");

                        for (int i = 0; i < serviciosNombre.size(); i++) {
                            if (!servicios.contains(serviciosNombre.get(i))) {
                                Servicio auxTemp = new Servicio(serviciosNombre.get(i), null);
                                servicios.add(auxTemp);
                                auxTemp = null;
                            }
                        }

                        listaServicios.setAdapter(adapterServicios);
                        listaServicios.setHasFixedSize(true);
                        adapterServicios.showAllServicios(servicios);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });










           ////////////////////////////FUERA DE AQUI NO HAY ACCESO A EL NOMBRE DEL SALON
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return mView;
    }

    public void modificarServicios (SalonDeBelleza salonDeBelleza) {

        for (int i = 0; i < serviciosViejos.size(); i++) {
            salonDeBelleza.getServicios().put(serviciosViejos.get(i), true);
        }

    }

    public ArrayList<String> compararListasServicios () {
        ArrayList<String> result = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            List<String> total = Stream.of(serviciosNuevos, serviciosViejos)
                    .flatMap(x -> x.stream())
                    .collect(Collectors.toList());

            ArrayList<String> totalOrdenada = ordenar(total);
            ArrayList<String> listaViejaOrdenada = ordenar(serviciosViejos);

            ArrayList<String> auxTotal = combinaciones(totalOrdenada);
            ArrayList<String> auxVieja = combinaciones(listaViejaOrdenada);


            for (int i = 0; i < auxTotal.size(); i++) {

                if (!auxVieja.contains(auxTotal.get(i))){
                    result.add(auxTotal.get(i));
                }
            }
        }

        return  result;
    }

    public ArrayList<String> ordenar (List<String> aOrdenar) {

        ArrayList<String> ordenada = new ArrayList<>();

        if (aOrdenar.contains("Uñas")) {
            ordenada.add("Uñas");
        }
        if (aOrdenar.contains("Maquillaje")) {
            ordenada.add("Maquillaje");
        }
        if (aOrdenar.contains("Masaje")) {
            ordenada.add("Masaje");
        }
        if (aOrdenar.contains("Depilación")){
            ordenada.add("Depilación");
        }
        if (aOrdenar.contains("Peluquería")){
            ordenada.add("Peluquería");
        }

        return ordenada;
    }

    //TODO por aca lo que hace es recibir una imagen de la galeria y ponerla de fondo con un hilito y ademas llamar a subirImagen que lo que hace es
    //TODO subir la imagen a la storage
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_CALLBACK_ID && resultCode == RegistroSalonDeBelleza.RESULT_OK) {
            final Uri uri = data.getData();
            photoFile = new File(  UtilDomi.getPath(this.getContext(), uri)  );
            getActivity().runOnUiThread( () -> {
                auxFotoPerfilSalon.setBackground(null);
                auxFotoPerfilSalon.setImageURI(uri);
            });
            subirImagen();
        }
    }

    //TODO sube a storage
    private void subirImagen(){
        StorageReference ref = storage.getReference().child("salones de belleza").child(nombreSalon).child("profile");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(photoFile);
            ref.putStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> combinaciones (ArrayList<String> serviciosParaCombinar) {
        String[] res = new String[(1 << serviciosParaCombinar.size()) - 1];
        int k = 0;
        int x = 1;
        for (int i = serviciosParaCombinar.size() - 1; i >= 0; --i) {
            res[k++] = serviciosParaCombinar.get(i);
            for (int j = 1; j < x; ++j) {
                res[k++] = serviciosParaCombinar.get(i) + "-" + res[j - 1];
            }
            x *= 2;
        }
        ArrayList<String> result = new ArrayList<>(Arrays.asList(res));
        return result;
    }

}
