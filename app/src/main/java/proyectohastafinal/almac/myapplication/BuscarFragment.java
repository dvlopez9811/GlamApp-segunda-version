package proyectohastafinal.almac.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import proyectohastafinal.almac.myapplication.model.BusquedaSalonDeBelleza;
import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;


public class BuscarFragment extends Fragment implements View.OnClickListener, AdapterSalones.OnItemClickListener{

    public final static String[] SERVICIOS_SALON = {"Uñas", "Maquillaje", "Masaje", "Depilación", "Peluquería"};

    private static BuscarFragment instance;

    public static BuscarFragment getInstance() {
        instance = instance == null ? new BuscarFragment() : instance;
        return instance;
    }

    private RecyclerView recyclerView;
    private AdapterSalones mAdapater;
    private RecyclerView.LayoutManager layoutManager;

    private EditText et_buscar_salon_fragment_buscar;
    private ImageButton btn_cancelar_busqueda_fragment_buscar;
    private ImageView image_filtro_peluqueria_fragment_buscar;
    private ImageView image_filtro_depilacion_fragment_buscar;
    private ImageView image_filtro_unas_fragment_buscar;
    private ImageView image_filtro_maquillaje_fragment_buscar;
    private ImageView image_filtro_masaje_fragment_buscar;

    // Location Manager
    private LocationManager locationManager;

    // Location
    private Location location;

    private PopupWindow popup_informacion;

    private HashMap<String, Marker> marcadores;

    FirebaseDatabase rtdb;

    View mView;

    private boolean[] servicios;

    public BuscarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marcadores = new HashMap<>();
        rtdb = FirebaseDatabase.getInstance();
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_buscar, container, false);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, locationListener);

        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location==null)
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        et_buscar_salon_fragment_buscar = mView.findViewById(R.id.et_buscar_salon_fragment_buscar);
        btn_cancelar_busqueda_fragment_buscar = mView.findViewById(R.id.btn_cancelar_busqueda_fragment_buscar);
        image_filtro_peluqueria_fragment_buscar = mView.findViewById(R.id.image_filtro_peluqueria_fragment_buscar);
        image_filtro_depilacion_fragment_buscar = mView.findViewById(R.id.image_filtro_depilacion_fragment_buscar);
        image_filtro_unas_fragment_buscar = mView.findViewById(R.id.image_filtro_unas_fragment_buscar);
        image_filtro_maquillaje_fragment_buscar = mView.findViewById(R.id.image_filtro_maquillaje_fragment_buscar);
        image_filtro_masaje_fragment_buscar = mView.findViewById(R.id.image_filtro_masaje_fragment_buscar);
        servicios = new boolean[5];
        image_filtro_peluqueria_fragment_buscar.setOnClickListener(this);
        image_filtro_depilacion_fragment_buscar.setOnClickListener(this);
        image_filtro_unas_fragment_buscar.setOnClickListener(this);
        image_filtro_maquillaje_fragment_buscar.setOnClickListener(this);
        image_filtro_masaje_fragment_buscar.setOnClickListener(this);
        btn_cancelar_busqueda_fragment_buscar.setOnClickListener(this);

        btn_cancelar_busqueda_fragment_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_buscar_salon_fragment_buscar.setText(null);
            }
        });

        recyclerView = mView.findViewById(R.id.recycler_view_salones);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapater = new AdapterSalones();
        mAdapater.setListener(this);
        recyclerView.setAdapter(mAdapater);

        rtdb.getReference().child("Salon de belleza").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    mostrarSalonDeBellezaAdapter(dsp,mAdapater,true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        rtdb.getReference().child("Salon de belleza").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mostrarSalonDeBellezaAdapter(dataSnapshot,mAdapater,false);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return mView;
    }

    // Location Listener
    LocationListener locationListener = new LocationListener() {

        // Metodo para manejar cuando se cambie de ubicacion
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.image_filtro_unas_fragment_buscar:
                if(v.isActivated()) {
                    servicios[0] = false;
                }else {
                    servicios[0] = true;
                }
                v.setActivated(!v.isActivated());
                break;
            case R.id.image_filtro_maquillaje_fragment_buscar:
                if(v.isActivated())
                    servicios[1] = false;
                else
                    servicios[1] = true;
                v.setActivated(!v.isActivated());
                break;
            case R.id.image_filtro_masaje_fragment_buscar:
                if(v.isActivated())
                    servicios[2] = false;
                else
                    servicios[2] = true;
                v.setActivated(!v.isActivated());
                break;
            case R.id.image_filtro_depilacion_fragment_buscar:
                Log.e("ALAAAAAAAAAAAAAAAAAA",(image_filtro_depilacion_fragment_buscar.isActivated())+"");
                if(v.isActivated())
                    servicios[3] = false;
                else
                    servicios[3] = true;
                v.setActivated(!v.isActivated());
                break;
            case R.id.image_filtro_peluqueria_fragment_buscar:
                if(v.isActivated())
                    servicios[4] = false;
                else
                    servicios[4] = true;
                v.setActivated(!v.isActivated());
        }

        buscarSalon();
    }

    private void actualizarUbicacion(Location location) {

        this.location = location;

        rtdb.getReference().child("Salon de belleza").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    mostrarSalonDeBellezaAdapter(dsp,mAdapater,false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void mostrarSalonDeBellezaAdapter(@NonNull DataSnapshot dataSnapshot, @NonNull AdapterSalones mAdapater, boolean paraAgregar){

        SalonDeBelleza salonDeBelleza = dataSnapshot.getValue(SalonDeBelleza.class);

        Location locationDestino = new Location("Location destino");
        locationDestino.setLatitude(salonDeBelleza.getLatitud());
        locationDestino.setLongitude(salonDeBelleza.getLongitud());

        double distancia = location.distanceTo(locationDestino) / 1000.0;
        DecimalFormat twoDForm = new DecimalFormat("#.#");

        BusquedaSalonDeBelleza busquedaSalonDeBelleza = new BusquedaSalonDeBelleza(salonDeBelleza.getNombreSalonDeBelleza(), salonDeBelleza.getDireccion(), twoDForm.format(distancia) + " km");

        if(paraAgregar)
            mAdapater.agregarSalon(busquedaSalonDeBelleza);
        else
            mAdapater.actualizarDistancia(busquedaSalonDeBelleza);
    }

    private void buscarSalon(){
        String busqueda = "";
        for (int i = 0;i<servicios.length;i++){
            if(servicios[i]){
                if(busqueda.equals(""))
                    busqueda = SERVICIOS_SALON[i];
                else
                    busqueda += "-" + SERVICIOS_SALON[i];
            }
        }
        Log.e("estado", busqueda);
        if( !busqueda.equals("") ) {
            Log.e("hola", busqueda);
            mAdapater.limpiarSalones();
            rtdb.getReference().child("Buscar servicios salon de belleza").child(busqueda).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        String salonDeBelleza = childDataSnapshot.getKey();

                        Log.e("hola", salonDeBelleza);
                        rtdb.getReference().child("Salon de belleza").child(salonDeBelleza).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                mostrarSalonDeBellezaAdapter(dataSnapshot, mAdapater, true);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            rtdb.getReference().child("Salon de belleza").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        mostrarSalonDeBellezaAdapter(dsp,mAdapater,true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onItemClick(BusquedaSalonDeBelleza salonDeBelleza) {
        Toast.makeText(getContext(), salonDeBelleza.getNombreSalonDeBelleza(), Toast.LENGTH_SHORT).show();
    }
}