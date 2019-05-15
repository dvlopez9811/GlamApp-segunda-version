package proyectohastafinal.almac.myapplication;

import android.annotation.SuppressLint;
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

    private Button btn_fragment_buscar_filtros;
    private ImageButton btn_cancelar_busqueda;

    private boolean estaBuscando;

    private PopupWindow popup_filtros;
    private ImageView image_filtro_peluqueria;
    private ImageView image_filtro_depilacion;
    private ImageView image_filtro_unas;
    private ImageView image_filtro_maquillaje;
    private ImageView image_filtro_masaje;
    private Button bnt_filtros_listos;
    private EditText et_fragment_buscar_buscar_salon;
    private TextView txt_fragment_buscar_resultados_salon;
    private ImageButton btn_actualizar_ubicacion;

    private TextView popup_nombre_salon;
    private ImageView popup_imagen_salon;
    private TextView popup_servicio_peluqueria;
    private TextView popup_servicio_depilacion;
    private TextView popup_servicio_unas;
    private TextView popup_servicio_maquillaje;
    private TextView popup_servicio_masaje;


    // Location Manager
    private LocationManager locationManager;

    // Location
    private Location location;

    private PopupWindow popup_informacion;

    private HashMap<String, Marker> marcadores;

    FirebaseDatabase rtdb;
    FirebaseStorage storage;


    // Marcador de la ubicación actual
    private Marker marcador_actual;

    GoogleMap mGoogleMap;
    MapView mMapView;
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

        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view_salones);
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
                    double distancia = location.distanceTo(locationDestino)/1000.0;
                    DecimalFormat twoDForm = new DecimalFormat("#.#");
                    BusquedaSalonDeBelleza busquedaSalonDeBelleza = new BusquedaSalonDeBelleza(salonDeBelleza.getNombreSalonDeBelleza(),salonDeBelleza.getDireccion(), twoDForm.format(distancia)+" km");
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
                double distancia = location.distanceTo(locationDestino)/1000.0;
                DecimalFormat twoDForm = new DecimalFormat("#.#");
                BusquedaSalonDeBelleza busquedaSalonDeBelleza = new BusquedaSalonDeBelleza(salonDeBelleza.getNombreSalonDeBelleza(),salonDeBelleza.getDireccion(), twoDForm.format(distancia)+" km");
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
            Log.e("HOLA","cambia");
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

    // Obtiene la latitud y longitud de la ubicacion actual y agrega el marcador
    private void miUbicacion(Location location) {

        this.location = location;

        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            // Latitud y longitud de la posicion actual
            LatLng posicion_actual = new LatLng(lat, lng);

            // Método para añadir el marcador
            agregarMarcadorUbicacion(posicion_actual);
        }
    }

    private void actualizarUbicacion (Location location){
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
                    BusquedaSalonDeBelleza busquedaSalonDeBelleza = new BusquedaSalonDeBelleza(salonDeBelleza.getNombreSalonDeBelleza(),salonDeBelleza.getDireccion(), twoDForm.format(distancia)+" km");
                    mAdapater.actualizarDistancia(busquedaSalonDeBelleza);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public double distanciaA(Location dest){
        Log.e("goa","calcula");
        return location.distanceTo(dest)/1000.0;
    }
    // Agrega el marcador de la ubicación actual
    private void agregarMarcadorUbicacion(LatLng posicion_actual) {

        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(posicion_actual, 16);

        if (marcador_actual != null) {
            marcador_actual.remove();
        }

        MarkerOptions option = new MarkerOptions()
                .position(posicion_actual)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marcador_femenino));

        marcador_actual = mGoogleMap.addMarker(option);

        mGoogleMap.animateCamera(miUbicacion);

    }


    public void agregarMarcador(GoogleMap googleMap, LatLng latLng) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marcador_salon));

        mGoogleMap.addMarker(markerOptions);
    }

    private void mostrarPopUpWindowFiltros() {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_window_filtros, null);

        popup_filtros = new PopupWindow(layout, 700, 920, true);

        popup_imagen_salon = layout.findViewById(R.id.popup_imagen_salon);
        image_filtro_peluqueria = layout.findViewById(R.id.image_filtro_peluqueria);
        image_filtro_depilacion = layout.findViewById(R.id.image_filtro_depilacion);
        image_filtro_unas = layout.findViewById(R.id.image_filtro_unas);
        image_filtro_maquillaje = layout.findViewById(R.id.image_filtro_maquillaje);
        image_filtro_masaje = layout.findViewById(R.id.image_filtro_masaje);
        bnt_filtros_listos = layout.findViewById(R.id.btn_filtros_listos);

        image_filtro_peluqueria.setOnClickListener(this);
        image_filtro_depilacion.setOnClickListener(this);
        image_filtro_unas.setOnClickListener(this);
        image_filtro_maquillaje.setOnClickListener(this);
        image_filtro_masaje.setOnClickListener(this);
        bnt_filtros_listos.setOnClickListener(this);

        popup_filtros.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    private void mostrarPopUpWindowInformacion() {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_window_informacion, null);

        popup_informacion = new PopupWindow(layout, 1000, 720, true);
        popup_nombre_salon = layout.findViewById(R.id.popup_nombre_salon);
        popup_imagen_salon = layout.findViewById(R.id.popup_imagen_salon);
        popup_servicio_peluqueria = layout.findViewById(R.id.popup_servicio_peluqueria);
        popup_servicio_depilacion = layout.findViewById(R.id.popup_servicio_depilacion);
        popup_servicio_unas = layout.findViewById(R.id.popup_servicio_unas);
        popup_servicio_maquillaje = layout.findViewById(R.id.popup_servicio_maquillaje);
        popup_servicio_masaje = layout.findViewById(R.id.popup_servicio_masaje);

        Display display = mView.getDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        popup_informacion.showAtLocation(layout, Gravity.CENTER, 0, (int) (height * 0.18));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.image_filtro_depilacion | R.id.image_filtro_peluqueria | R.id.image_filtro_unas | R.id.image_filtro_maquillaje | R.id.image_filtro_maquillaje:
                ImageView imageView = (ImageView) v;

                if (imageView.isSelected()) {
                    imageView.setColorFilter(Color.rgb(255, 255, 255), android.graphics.PorterDuff.Mode.MULTIPLY);
                    imageView.setSelected(false);
                } else {
                    imageView.setColorFilter(Color.rgb(155, 155, 155), android.graphics.PorterDuff.Mode.MULTIPLY);
                    imageView.setSelected(true);
                }
                break;

            case R.id.btn_cancelar_busqueda:
                et_fragment_buscar_buscar_salon.setText(null);
                estaBuscando = false;
                miUbicacion(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
                InputMethodManager imm2 = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                et_fragment_buscar_buscar_salon.requestFocus();
                et_fragment_buscar_buscar_salon.requestFocus(EditText.FOCUS_DOWN);
                imm2.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                break;
        }


    }


    public void agregarMarcador(Marcador marcador) {
        Log.e("HOLA", marcador.getIDSalonDeBelleza() + "ee");
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(marcador.getLatitud(), marcador.getLongitud());
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marcador_salon));
        markerOptions.title(marcador.getIDSalonDeBelleza());
        if (!marcadores.containsKey(marcador.getIDSalonDeBelleza())) {
            Marker marker = mGoogleMap.addMarker(markerOptions);
            marcadores.put(marcador.getIDSalonDeBelleza(), marker);
            CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 16);
            mGoogleMap.animateCamera(miUbicacion);
        }
    }

    public void mostrarMarcadorSalon (String IDSalonDeBelleza) {
        CameraUpdate ubicacionSalon = CameraUpdateFactory.newLatLngZoom(marcadores.get(IDSalonDeBelleza).getPosition(),16);
        mGoogleMap.animateCamera(ubicacionSalon);
    }
}


