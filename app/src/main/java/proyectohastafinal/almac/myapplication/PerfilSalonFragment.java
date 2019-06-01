package proyectohastafinal.almac.myapplication;

import android.content.Context;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import proyectohastafinal.almac.myapplication.model.Marcador;
import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;
import proyectohastafinal.almac.myapplication.model.Servicio;


public class PerfilSalonFragment extends Fragment {


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

                //Mostrar foto
                StorageReference ref = storage.getReference().child("salones de belleza").child(nombreSalon).child("profile");
                if(ref == null){
                    Log.e("hola","es nulllllllllll");
                }

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
                        ArrayList<Servicio> servicios=new ArrayList<>();
                        for (DataSnapshot dsp: dataSnapshot.getChildren()){
                            if(dsp.getValue(Boolean.class)) {
                                SalonDeBelleza s = new SalonDeBelleza();
                                s.setNombreSalonDeBelleza(nombreSalon);
                                Servicio servicioNuevo = new Servicio(dsp.getKey(),s);
                                servicios.add(servicioNuevo);
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







}
