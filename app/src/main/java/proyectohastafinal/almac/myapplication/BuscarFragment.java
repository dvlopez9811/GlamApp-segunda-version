package proyectohastafinal.almac.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import proyectohastafinal.almac.myapplication.model.BusquedaSalonDeBelleza;
import proyectohastafinal.almac.myapplication.model.Marcador;
import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;


public class BuscarFragment extends Fragment implements View.OnClickListener {

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        et_buscar_salon_fragment_buscar = mView.findViewById(R.id.et_buscar_salon_fragment_buscar);
        btn_cancelar_busqueda_fragment_buscar = mView.findViewById(R.id.btn_cancelar_busqueda_fragment_buscar);
        image_filtro_peluqueria_fragment_buscar = mView.findViewById(R.id.image_filtro_peluqueria_fragment_buscar);
        image_filtro_depilacion_fragment_buscar = mView.findViewById(R.id.image_filtro_depilacion_fragment_buscar);
        image_filtro_unas_fragment_buscar = mView.findViewById(R.id.image_filtro_unas_fragment_buscar);
        image_filtro_maquillaje_fragment_buscar = mView.findViewById(R.id.image_filtro_maquillaje_fragment_buscar);
        image_filtro_masaje_fragment_buscar = mView.findViewById(R.id.image_filtro_masaje_fragment_buscar);

        image_filtro_peluqueria_fragment_buscar.setOnClickListener(this);
        image_filtro_depilacion_fragment_buscar.setOnClickListener(this);
        image_filtro_unas_fragment_buscar.setOnClickListener(this);
        image_filtro_maquillaje_fragment_buscar.setOnClickListener(this);
        image_filtro_masaje_fragment_buscar.setOnClickListener(this);

        recyclerView = mView.findViewById(R.id.recycler_view_salones);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapater = new AdapterSalones();
        recyclerView.setAdapter(mAdapater);

        rtdb.getReference().child("Salon de belleza").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    SalonDeBelleza salonDeBelleza = dsp.getValue(SalonDeBelleza.class);
                    Location locationDestino = new Location("Location destino");
                    locationDestino.setLatitude(salonDeBelleza.getLatitud());
                    locationDestino.setLongitude(salonDeBelleza.getLongitud());
                    double distancia = location.distanceTo(locationDestino) / 1000.0;
                    DecimalFormat twoDForm = new DecimalFormat("#.#");
                    BusquedaSalonDeBelleza busquedaSalonDeBelleza = new BusquedaSalonDeBelleza(salonDeBelleza.getNombreSalonDeBelleza(), salonDeBelleza.getDireccion(), twoDForm.format(distancia) + " km");
                    mAdapater.agregarSalon(busquedaSalonDeBelleza);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        rtdb.getReference().child("Salon de belleza").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SalonDeBelleza salonDeBelleza = dataSnapshot.getValue(SalonDeBelleza.class);
                Location locationDestino = new Location("Location destino");
                locationDestino.setLatitude(salonDeBelleza.getLatitud());
                locationDestino.setLongitude(salonDeBelleza.getLongitud());
                double distancia = location.distanceTo(locationDestino) / 1000.0;
                DecimalFormat twoDForm = new DecimalFormat("#.#");
                BusquedaSalonDeBelleza busquedaSalonDeBelleza = new BusquedaSalonDeBelleza(salonDeBelleza.getNombreSalonDeBelleza(), salonDeBelleza.getDireccion(), twoDForm.format(distancia) + " km");
                mAdapater.agregarSalon(busquedaSalonDeBelleza);
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

    private void actualizarUbicacion(Location location) {
        this.location = location;

        rtdb.getReference().child("Salon de belleza").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    SalonDeBelleza salonDeBelleza = dsp.getValue(SalonDeBelleza.class);
                    Location locationDestino = new Location("Location destino");
                    locationDestino.setLatitude(salonDeBelleza.getLatitud());
                    locationDestino.setLongitude(salonDeBelleza.getLongitud());
                    double distancia = distanciaA(locationDestino);
                    DecimalFormat twoDForm = new DecimalFormat("#.#");
                    BusquedaSalonDeBelleza busquedaSalonDeBelleza = new BusquedaSalonDeBelleza(salonDeBelleza.getNombreSalonDeBelleza(), salonDeBelleza.getDireccion(), twoDForm.format(distancia) + " km");
                    mAdapater.actualizarDistancia(busquedaSalonDeBelleza);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public double distanciaA(Location dest) {
        return location.distanceTo(dest) / 1000.0;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.image_filtro_peluqueria_fragment_buscar:
                v.setActivated(!v.isActivated());
                break;
            case R.id.image_filtro_depilacion_fragment_buscar:
                v.setActivated(!v.isActivated());
                break;
            case R.id.image_filtro_unas_fragment_buscar:
                v.setActivated(!v.isActivated());
                break;
            case R.id.image_filtro_maquillaje_fragment_buscar:
                v.setActivated(!v.isActivated());
                break;
            case R.id.image_filtro_masaje_fragment_buscar:
                v.setActivated(!v.isActivated());
        }
    }
}