package proyectohastafinal.almac.myapplication;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import proyectohastafinal.almac.myapplication.model.FotoCatalogo;
import proyectohastafinal.almac.myapplication.model.Marcador;
import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;
import proyectohastafinal.almac.myapplication.model.Servicio;
import proyectohastafinal.almac.myapplication.util.UtilDomi;

public class PerfilSalonFragment extends Fragment {

    private static final int GALLERY_CALLBACK_ID = 101;
    private static final int GALLERY_CALLBACK_ID_CATALOGO = 102;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView listaServicios;
    private AdapterServiciosPerfilSalon adapterServicios;

    // Catálogo de depilación
    private ExpandableHeightGridView gridCatalogoDepilacion;
    private AdapterCatalogoPerfilSalon adapterCatalogoDepilacion;

    // Catálogo de maquillaje
    private ExpandableHeightGridView gridCatalogoMaquillaje;
    private AdapterCatalogoPerfilSalon adapterCatalogoMaquillaje;

    // Catálogo de masaje
    private ExpandableHeightGridView gridCatalogoMasaje;
    private AdapterCatalogoPerfilSalon adapterCatalogoMasaje;

    // Catálogo de peluqueria
    private ExpandableHeightGridView gridCatalogoPeluqueria;
    private AdapterCatalogoPerfilSalon adapterCatalogoPeluqueria;

    // Catálogo de uñas
    private ExpandableHeightGridView gridCatalogoUnhas;
    private AdapterCatalogoPerfilSalon adapterCatalogoUnhas;

    FirebaseDatabase rtdb;
    FirebaseAuth auth;
    FirebaseStorage storage;

    private String nombreSalon;
    private String direccionActual;
    private static PerfilSalonFragment instance;
    private ArrayList<String> serviciosViejos;
    private ArrayList<String> serviciosNuevos;
    private File photoFile;
    private File photoFileCatalogo;
    private ImageButton btn_volver_popup;
    private ImageView image_popup_window_agregar;
    private EditText et_popup_window_descripcion;
    private EditText et_popup_window_precio;
    private Button btn_popup_windows_subir;

    View popUpView;
    PopupWindow popupWindow;
    LayoutInflater layoutInflater;

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
        rtdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

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
                        ((EditText) mView.findViewById(R.id.et_uibacion_editable_perfil_salon_fragment)).setText(direccion);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                (mView.findViewById(R.id.boton_editar_servicios_perfil_salon_fragment)).setOnClickListener(v -> {
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
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            HashMap<String, Boolean> aux = (HashMap<String, Boolean>) dataSnapshot1.getValue();
                            for (int i = 0; i < serviciosNuevos.size(); i++) {
                                aux.put(serviciosNuevos.get(i), true);
                            }
                            rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("servicios").setValue(aux);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                });

                (mView.findViewById(R.id.boton_editar_ubicacion_perfil_salon_fragment)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Habilita edicion
                        if (!mView.findViewById(R.id.et_uibacion_editable_perfil_salon_fragment).isEnabled()) {
                            direccionActual = ((EditText) mView.findViewById(R.id.et_uibacion_editable_perfil_salon_fragment)).getText().toString();
                            (mView.findViewById(R.id.et_uibacion_editable_perfil_salon_fragment)).setEnabled(true);
                            (mView.findViewById(R.id.boton_editar_ubicacion_perfil_salon_fragment)).setBackgroundResource(R.drawable.ic_chulo);
                            //Deshabilita edicion
                        } else {
                            (mView.findViewById(R.id.et_uibacion_editable_perfil_salon_fragment)).setEnabled(false);
                            (mView.findViewById(R.id.boton_editar_ubicacion_perfil_salon_fragment)).setBackgroundResource(R.drawable.edit);
                            String direccionIngresada = ((EditText) mView.findViewById(R.id.et_uibacion_editable_perfil_salon_fragment)).getText().toString();
                            if (!direccionIngresada.equals(direccionActual)) {
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
                                Marcador marcador = new Marcador(latitud, longitud, nombreSalon);
                                rtdb.getReference().child("Marcador").child(nombreSalon).setValue(marcador);
                                rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("direccion").setValue(direccionIngresada);
                                rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("latitud").setValue(latitud);
                                rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("longitud").setValue(longitud);

                                Toast.makeText(inflater.getContext(), "La dirección ha sido cambiada exitosamente", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                });

                (mView.findViewById(R.id.cambiar_imagen_perfil_salon_fragment)).setOnClickListener(v -> {

                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    i.setType("image/*");
                    startActivityForResult(i, GALLERY_CALLBACK_ID);

                });

                //Mostrar foto
                StorageReference ref = storage.getReference().child("salones de belleza").child(nombreSalon).child("profile");
                if (ref != null) {
                    auxFotoPerfilSalon = mView.findViewById(R.id.imagen_perfil_perfil_salon_fragment);

                    ref.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(mView.getContext())
                            .load(uri).into((ImageView) mView.findViewById(R.id.imagen_perfil_perfil_salon_fragment)));

                }

                // Catálogo

                // Obtener de depilación

                rtdb.getReference().child("fotos").child(nombreSalon).child("depilacion").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<FotoCatalogo> fotosDepilacion = new ArrayList<>();
                        long fotos = dataSnapshot.getChildrenCount();
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                            FotoCatalogo foto = childDataSnapshot.getValue(FotoCatalogo.class);
                            fotosDepilacion.add(foto);
                            if (fotos == fotosDepilacion.size()) adapterDepilacion(fotosDepilacion);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                // Obtener de maquillaje

                rtdb.getReference().child("fotos").child(nombreSalon).child("maquillaje").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<FotoCatalogo> fotosMaquillaje = new ArrayList<>();
                        long fotos = dataSnapshot.getChildrenCount();
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                            FotoCatalogo foto = childDataSnapshot.getValue(FotoCatalogo.class);
                            fotosMaquillaje.add(foto);
                            if (fotos == fotosMaquillaje.size()) {
                                adapterMaquillaje(fotosMaquillaje);
                            }
                            ;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                // Obtener de masaje
                rtdb.getReference().child("fotos").child(nombreSalon).child("masaje").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<FotoCatalogo> fotosMasaje = new ArrayList<>();
                        long fotos = dataSnapshot.getChildrenCount();
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                            FotoCatalogo foto = childDataSnapshot.getValue(FotoCatalogo.class);
                            fotosMasaje.add(foto);
                            if (fotos == fotosMasaje.size()) {
                                adapterMasaje(fotosMasaje);
                            }
                            ;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                // Obtener de peluqueria
                rtdb.getReference().child("fotos").child(nombreSalon).child("peluqueria").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<FotoCatalogo> fotosPeluqueria = new ArrayList<>();
                        long fotos = dataSnapshot.getChildrenCount();
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                            FotoCatalogo foto = childDataSnapshot.getValue(FotoCatalogo.class);
                            fotosPeluqueria.add(foto);
                            if (fotos == fotosPeluqueria.size()) {
                                adapterPeluqueria(fotosPeluqueria);
                            }
                            ;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                // Obtener de uñas
                rtdb.getReference().child("fotos").child(nombreSalon).child("uñas").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<FotoCatalogo> fotosUhnas = new ArrayList<>();
                        long fotos = dataSnapshot.getChildrenCount();
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                            FotoCatalogo foto = childDataSnapshot.getValue(FotoCatalogo.class);
                            fotosUhnas.add(foto);
                            if (fotos == fotosUhnas.size()) {
                                adapterUnhas(fotosUhnas);
                            }
                            ;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                //Mostrar los servicios en adapter
                adapterServicios = new AdapterServiciosPerfilSalon();
                listaServicios = mView.findViewById(R.id.listado_servicios_perfil_salon_fragment);
                listaServicios.setLayoutManager(new LinearLayoutManager(mView.getContext()));

                rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("servicios").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        serviciosViejos = new ArrayList<>();
                        ArrayList<Servicio> servicios = new ArrayList<>();
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            if (dsp.getValue(Boolean.class)) {
                                SalonDeBelleza s = new SalonDeBelleza();
                                s.setNombreSalonDeBelleza(nombreSalon);
                                Servicio servicioNuevo = new Servicio(dsp.getKey(), s);
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


                final String[] servicio = new String[1];
                mView.findViewById(R.id.btn_agregar_depilacion_catalogo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        servicio[0] = "depilacion";
                        popupWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
                    }
                });

                mView.findViewById(R.id.btn_agregar_maquillaje_catalogo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        servicio[0] = "maquillaje";
                        popupWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
                    }
                });

                mView.findViewById(R.id.btn_agregar_masaje_catalogo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        servicio[0] = "masaje";
                        popupWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
                    }
                });

                mView.findViewById(R.id.btn_agregar_peluqueria_catalogo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        servicio[0] = "peluqueria";
                        popupWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
                    }
                });

                mView.findViewById(R.id.btn_agregar_unhas_catalogo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        servicio[0] = "uñas";
                        popupWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
                    }
                });

                layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                popUpView = layoutInflater.inflate(R.layout.popup_window_agregar_foto_catalogo, null);
                popupWindow = new PopupWindow(popUpView, RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);

                btn_volver_popup = popUpView.findViewById(R.id.btn_volver_popup);
                et_popup_window_descripcion = popUpView.findViewById(R.id.et_popup_window_descripcion);
                et_popup_window_precio = popUpView.findViewById(R.id.et_popup_window_precio);
                btn_popup_windows_subir = popUpView.findViewById(R.id.btn_popup_windows_subir);
                image_popup_window_agregar = popUpView.findViewById(R.id.image_popup_window_agregar);

                popupWindow.setFocusable(true);
                popupWindow.update();

                btn_volver_popup.setOnClickListener(v1 -> popupWindow.dismiss());

                image_popup_window_agregar.setOnClickListener(v12 -> {
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    i.setType("image/*");
                    startActivityForResult(i, GALLERY_CALLBACK_ID_CATALOGO);
                });
                btn_popup_windows_subir.setOnClickListener(v13 -> {
                    subirImagenCatalogo(nombreSalon,servicio[0], et_popup_window_descripcion.getText().toString(),et_popup_window_precio.getText().toString());
                    popupWindow.dismiss();
                });


                ////////////////////////////FUERA DE AQUI NO HAY ACCESO A EL NOMBRE DEL SALON
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return mView;
    }

    public void adapterDepilacion(ArrayList<FotoCatalogo> fotos) {
        gridCatalogoDepilacion = getView().findViewById(R.id.grid_catalogo_depilacion);
        adapterCatalogoDepilacion = new AdapterCatalogoPerfilSalon(getView().getContext(), fotos, "Salon");
        adapterCatalogoDepilacion.notifyDataSetChanged();
        gridCatalogoDepilacion.setAdapter(adapterCatalogoDepilacion);
        gridCatalogoDepilacion.setExpanded(true);
    }

    public void adapterMaquillaje(ArrayList<FotoCatalogo> fotos) {
        gridCatalogoMaquillaje = getView().findViewById(R.id.grid_catalogo_maquillaje);
        adapterCatalogoMaquillaje = new AdapterCatalogoPerfilSalon(getView().getContext(), fotos, "Salon");
        gridCatalogoMaquillaje.setAdapter(adapterCatalogoMaquillaje);
        gridCatalogoMaquillaje.setExpanded(true);
    }

    public void adapterMasaje(ArrayList<FotoCatalogo> fotos) {
        gridCatalogoMasaje = getView().findViewById(R.id.grid_catalogo_masaje);
        adapterCatalogoMasaje = new AdapterCatalogoPerfilSalon(getView().getContext(), fotos, "Salon");
        gridCatalogoMasaje.setAdapter(adapterCatalogoMasaje);
        gridCatalogoMasaje.setExpanded(true);
    }

    public void adapterPeluqueria(ArrayList<FotoCatalogo> fotos) {
        gridCatalogoPeluqueria = getView().findViewById(R.id.grid_catalogo_peluqueria);
        adapterCatalogoPeluqueria = new AdapterCatalogoPerfilSalon(getView().getContext(), fotos, "Salon");
        gridCatalogoPeluqueria.setAdapter(adapterCatalogoPeluqueria);
        gridCatalogoPeluqueria.setExpanded(true);
    }

    public void adapterUnhas(ArrayList<FotoCatalogo> fotos) {
        gridCatalogoUnhas = getView().findViewById(R.id.grid_catalogo_unhas);
        adapterCatalogoUnhas = new AdapterCatalogoPerfilSalon(getView().getContext(), fotos, "Salon");
        gridCatalogoUnhas.setAdapter(adapterCatalogoUnhas);
        gridCatalogoUnhas.setExpanded(true);
    }


    public ArrayList<String> compararListasServicios() {
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

                if (!auxVieja.contains(auxTotal.get(i))) {
                    result.add(auxTotal.get(i));
                }
            }
        }

        return result;
    }

    public ArrayList<String> ordenar(List<String> aOrdenar) {

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
        if (aOrdenar.contains("Depilación")) {
            ordenada.add("Depilación");
        }
        if (aOrdenar.contains("Peluquería")) {
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
            photoFile = new File(UtilDomi.getPath(this.getContext(), uri));
            getActivity().runOnUiThread(() -> {
                auxFotoPerfilSalon.setBackground(null);
                auxFotoPerfilSalon.setImageURI(uri);
            });
            subirImagen();
        }
        if (requestCode == GALLERY_CALLBACK_ID_CATALOGO && resultCode == RegistroSalonDeBelleza.RESULT_OK) {
            final Uri uri = data.getData();
            photoFileCatalogo = new File(UtilDomi.getPath(this.getContext(), uri));

            final Uri uri2 = data.getData();
            photoFileCatalogo = new File(UtilDomi.getPath(getContext(), uri2));
            getActivity().runOnUiThread(() -> {
                image_popup_window_agregar.setBackground(null);
                image_popup_window_agregar.setImageURI(uri);
            });
        }
    }

    //TODO sube a storage
    private void subirImagen() {
        StorageReference ref = storage.getReference().child("salones de belleza").child(nombreSalon).child("profile");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(photoFile);
            ref.putStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void subirImagenCatalogo() {

        rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("fotos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long fotos = (long) dataSnapshot.getValue();
                long nuevoTamaño = fotos + 1;
                StorageReference ref = storage.getReference().child("salones de belleza").child(nombreSalon).child(nuevoTamaño + ".png");
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(photoFileCatalogo);
                    ref.putStream(fis);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("fotos").setValue(nuevoTamaño);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void subirImagenCatalogo(String nombreSalon, String servicio, String descripcion, String precio) {
        String nombreFoto = UUID.randomUUID().toString();
        FotoCatalogo fotoCatalogo = new FotoCatalogo(nombreFoto, descripcion, precio, servicio, nombreSalon);
        rtdb.getReference().child("fotos").child(nombreSalon).child(servicio).child(nombreFoto).setValue(fotoCatalogo);

        StorageReference ref = storage.getReference().child("salones de belleza").child(nombreSalon).child(servicio).child(nombreFoto);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(photoFileCatalogo);
            ref.putStream(fis);
            Thread.sleep(2000);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
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
