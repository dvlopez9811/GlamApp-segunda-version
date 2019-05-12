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
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class BuscarFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private static BuscarFragment instance;

    public static BuscarFragment getInstance(){
        instance = instance == null ? new BuscarFragment() : instance;
        return instance;
    }

    private Button btn_fragment_buscar_filtros;

    private PopupWindow popup_filtros;
    private ImageView image_filtro_peluqueria;
    private ImageView image_filtro_depilacion;
    private ImageView image_filtro_unas;
    private ImageView image_filtro_maquillaje;
    private ImageView image_filtro_masaje;
    private Button bnt_filtros_listos;

    private PopupWindow popup_informacion;

    // Location Manager
    private LocationManager locationManager;

    // Location
    private Location location;

    // Marcador de la ubicación actual
    private Marker marcador_actual;

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public BuscarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_buscar, container, false);

        mMapView = mView.findViewById(R.id.map);

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        }

        btn_fragment_buscar_filtros = mView.findViewById(R.id.btn_fragment_buscar_filtros);

        btn_fragment_buscar_filtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopUpWindowFiltros();
            }
        });

        return mView;
    }

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

    // Location Listener
    LocationListener locationListener = new LocationListener() {

        // Metodo para manejar cuando se cambie de ubicacion
        @Override
        public void onLocationChanged(Location location) {
            miUbicacion(location);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // Actualiza la primera ubicacion
        miUbicacion(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                agregarMarcador(mGoogleMap, latLng);
            }
        });

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mostrarPopUpWindowInformacion();
                return true;
            }
        });
    }

    public void agregarMarcador(GoogleMap googleMap, LatLng latLng) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marcador_salon));

        mGoogleMap.addMarker(markerOptions);
    }

    private void mostrarPopUpWindowFiltros(){

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_window_filtros, null);

        popup_filtros = new PopupWindow(layout,700, 920, true);

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

        popup_filtros.showAtLocation(layout, Gravity.CENTER,0,0);
    }

    private void mostrarPopUpWindowInformacion(){

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_window_informacion, null);

        popup_informacion = new PopupWindow(layout,1000, 720, true);

        Display display = mView.getDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        popup_informacion.showAtLocation(layout, Gravity.CENTER,0  ,(int)(height*0.18));
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageView) {
            ImageView image = (ImageView) v;

            if (image.isSelected()) {
                image.setColorFilter(Color.rgb(255, 255, 255), android.graphics.PorterDuff.Mode.MULTIPLY);
                image.setSelected(false);
            } else {
                image.setColorFilter(Color.rgb(155, 155, 155), android.graphics.PorterDuff.Mode.MULTIPLY);
                image.setSelected(true);
            }

        } else if (v instanceof Button) {
            popup_filtros.dismiss();
            Intent i = new Intent(getContext(),ResultadoBusquedaSalonActivity.class);
            startActivity(i);

        }
    }


}
