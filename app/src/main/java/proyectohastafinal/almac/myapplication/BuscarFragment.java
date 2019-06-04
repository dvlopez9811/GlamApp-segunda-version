package proyectohastafinal.almac.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.BusquedaSalonDeBelleza;
import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;


public class BuscarFragment extends Fragment implements View.OnClickListener, AdapterSalones.OnItemClickListener, SearchView.OnQueryTextListener{

    public final static String[] SERVICIOS_SALON = {"Uñas", "Maquillaje", "Masaje", "Depilación", "Peluquería"};

    private static BuscarFragment instance;

    public static BuscarFragment getInstance() {
        instance = instance == null ? new BuscarFragment() : instance;
        return instance;
    }

    private ListView recyclerView;
    private AdapterSalones mAdapater;

    private ImageView image_filtro_peluqueria_fragment_buscar;
    private ImageView image_filtro_depilacion_fragment_buscar;
    private ImageView image_filtro_unas_fragment_buscar;
    private ImageView image_filtro_maquillaje_fragment_buscar;
    private ImageView image_filtro_masaje_fragment_buscar;
    private SearchView searchView;

    // Location
    private Location location;

    ProgressDialog progressDialogo;
    private ArrayList<BusquedaSalonDeBelleza> salonesDeBelleza;

    FirebaseDatabase rtdb;

    View mView;

    private boolean[] servicios;

    public BuscarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        servicios = new boolean[5];
        rtdb = FirebaseDatabase.getInstance();
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        salonesDeBelleza = new ArrayList<>();

        progressDialogo = new ProgressDialog(getContext());
        progressDialogo.setMessage("Buscando los mejores salones para ti");
        progressDialogo.show();

        mView = inflater.inflate(R.layout.fragment_buscar, container, false);

        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(provider);

        if(location == null){
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location==null)
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        image_filtro_peluqueria_fragment_buscar = mView.findViewById(R.id.image_filtro_peluqueria_fragment_buscar);
        image_filtro_depilacion_fragment_buscar = mView.findViewById(R.id.image_filtro_depilacion_fragment_buscar);
        image_filtro_unas_fragment_buscar = mView.findViewById(R.id.image_filtro_unas_fragment_buscar);
        image_filtro_maquillaje_fragment_buscar = mView.findViewById(R.id.image_filtro_maquillaje_fragment_buscar);
        image_filtro_masaje_fragment_buscar = mView.findViewById(R.id.image_filtro_masaje_fragment_buscar);
        recyclerView = mView.findViewById(R.id.recycler_view_salones);
        searchView = mView.findViewById(R.id.searchview);

        image_filtro_peluqueria_fragment_buscar.setOnClickListener(this);
        image_filtro_depilacion_fragment_buscar.setOnClickListener(this);
        image_filtro_unas_fragment_buscar.setOnClickListener(this);
        image_filtro_maquillaje_fragment_buscar.setOnClickListener(this);
        image_filtro_masaje_fragment_buscar.setOnClickListener(this);

        rtdb.getReference().child("Salon de belleza").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long numeroDeSalones = dataSnapshot.getChildrenCount();

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    SalonDeBelleza salonDeBelleza = dsp.getValue(SalonDeBelleza.class);
                    agregarSalonDeBeleza(salonDeBelleza,salonesDeBelleza);
                } if(salonesDeBelleza.size() == numeroDeSalones) listarSalones(salonesDeBelleza);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return mView;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {
        progressDialogo.show();
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
        TextView searchText = searchView.findViewById(id);
        searchText.setText(null);

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

    @Override
    public void onItemClick(BusquedaSalonDeBelleza salonDeBelleza) {

        Intent i = new Intent(getActivity(),InformacionSalonActivity.class);
        i.putExtra("salon",salonDeBelleza.getNombreSalonDeBelleza());

        startActivity(i);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(TextUtils.isEmpty(query)){
            mAdapater.filtrar("");
            recyclerView.clearTextFilter();
        } else {
            mAdapater.filtrar(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(TextUtils.isEmpty(newText)){
            mAdapater.filtrar("");
            recyclerView.clearTextFilter();
        } else {
            mAdapater.filtrar(newText);
        }
        return true;
    }

    private void listarSalones ( ArrayList<BusquedaSalonDeBelleza> salonesDeBelleza) {
        mAdapater = new AdapterSalones(getContext(),salonesDeBelleza);
        mAdapater.setListener(this);
        recyclerView.setAdapter(mAdapater);

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
        TextView searchText = searchView.findViewById(id);
        Typeface typeface = ResourcesCompat.getFont(getContext(),R.font.josefin_sans);
        searchText.setTypeface(typeface);

        recyclerView.setTextFilterEnabled(true);
        setupSearchView();
        progressDialogo.dismiss();
    }

    /**
     * Este método calcula la distancia en kilómetros a una latitud y longitud de una ubicación dada desde la ubicación actual.
     * @param latitud Latitud de la ubicación.
     * @param longitud Longitud de la ubicación.
     * @return Cadena de texto cona la distancia en kilómetros con el formato: ##,# km
     */
    private String calcularDistancia(double latitud, double longitud){
        Location locationDestino = new Location("Location destino");
        locationDestino.setLatitude(latitud);
        locationDestino.setLongitude(longitud);

        double distanciaADestino = location.distanceTo(locationDestino) / 1000.0;
        DecimalFormat twoDForm = new DecimalFormat("#.#");

        String distancia = twoDForm.format(distanciaADestino);

        if(distancia.length() == 2 & !distancia.contains(","))
            distancia += ",0";
        else if (distancia.length() == 2)
            distancia = "0" + distancia;

        return distancia + " km";
    }

    private void agregarSalonDeBeleza(SalonDeBelleza salonDeBelleza,ArrayList<BusquedaSalonDeBelleza> busquedaSalonDeBellezas){
        String distancia = calcularDistancia( salonDeBelleza.getLatitud(),salonDeBelleza.getLongitud() );
        BusquedaSalonDeBelleza busquedaSalonDeBelleza = new BusquedaSalonDeBelleza(salonDeBelleza.getNombreSalonDeBelleza(), salonDeBelleza.getDireccion(),  distancia);
        busquedaSalonDeBellezas.add(busquedaSalonDeBelleza);
    }

    private void buscarSalon(){

        String busqueda = "";

        for (int i = 0;i<servicios.length;i++)
            if(servicios[i])
                if(busqueda.equals(""))
                    busqueda = SERVICIOS_SALON[i];
                else
                    busqueda += "-" + SERVICIOS_SALON[i];

        if( !busqueda.equals("") ) {
            ArrayList<BusquedaSalonDeBelleza> salonesFiltrados = new ArrayList();
            rtdb.getReference().child("Buscar servicios salon de belleza").child(busqueda).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long datos = dataSnapshot.getChildrenCount();

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        String salonDeBelleza = childDataSnapshot.getKey();

                        rtdb.getReference().child("Salon de belleza").child(salonDeBelleza).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                SalonDeBelleza salonDeBelleza = dataSnapshot.getValue(SalonDeBelleza.class);
                                agregarSalonDeBeleza(salonDeBelleza,salonesFiltrados);
                                if ( datos == salonesFiltrados.size() )
                                    listarSalones(salonesFiltrados);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        } else {
            listarSalones(salonesDeBelleza);
        }
    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Buscar salón");
    }
}