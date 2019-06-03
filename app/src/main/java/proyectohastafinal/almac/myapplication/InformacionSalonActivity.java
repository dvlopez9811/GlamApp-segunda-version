package proyectohastafinal.almac.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import proyectohastafinal.almac.myapplication.model.FotoCatalogo;
import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;
import proyectohastafinal.almac.myapplication.model.Servicio;

public class InformacionSalonActivity extends AppCompatActivity {


    private RecyclerView listaServicios;
    private AdapterServiciosInformacionSalon adapterServicios;
    private Button btn_agendar_cita,btn_anadir_favoritos,btn_volver,btn_como_llegar;
    private TextView txt_titulo_salon,txt_descripcion_servicios,txt_informacion_direccion_salon,txt_calificacion,txt_calificadores;
    private ImageView imagen_perfil_info_salon_activity,estrella_calificacion1,estrella_calificacion2
            ,estrella_calificacion3,estrella_calificacion4,estrella_calificacion5;

    private boolean favoritoMarcado;
    private ExpandableHeightGridView gridCatalogo;
    private AdapterCatalogo adapterCatalogo;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_salon);

        btn_anadir_favoritos = findViewById(R.id.btn_anadir_favoritos_informacion_salon_activity);
        btn_volver = findViewById(R.id.btn_atras_informacion_salon_activity);
        btn_agendar_cita = findViewById(R.id.btn_agendar_cita_info_salon_activity);
        btn_como_llegar = findViewById(R.id.btn_ver_ubicacion_maps_intent);
        txt_titulo_salon = findViewById(R.id.titulo_salon_informacion_salon_activity);
        txt_informacion_direccion_salon = findViewById(R.id.txt_informacion_direccion_salon);
        //txt_descripcion_servicios = findViewById(R.id.txt_resumen_servicios_info_salon_activity);
        imagen_perfil_info_salon_activity = findViewById(R.id.imagen_perfil_info_salon_activity);
        estrella_calificacion1 = findViewById(R.id.calificacion_1_informacion_salon);
        estrella_calificacion2 = findViewById(R.id.calificacion_2_informacion_salon);
        estrella_calificacion3 = findViewById(R.id.calificacion_3_informacion_salon);
        estrella_calificacion4 = findViewById(R.id.calificacion_4_informacion_salon);
        estrella_calificacion5 = findViewById(R.id.calificacion_5_informacion_salon);
        txt_calificacion=findViewById(R.id.txt_valor_calificacion_informacion_salon);
        txt_calificadores=findViewById(R.id.tv_personas_calificacion);

        rtdb=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();

        adapterServicios=new AdapterServiciosInformacionSalon();
        listaServicios=findViewById(R.id.listado_servicios_informacion_salon_activity);
        listaServicios.setLayoutManager(new LinearLayoutManager(this));

         String nombreSalon= getIntent().getExtras().get("salon").toString();
        txt_titulo_salon.setText(nombreSalon);

        //Mostrar foto
        StorageReference ref = storage.getReference().child("salones de belleza").child(nombreSalon).child("profile");
        if(ref == null){
            Log.e("hola","es nulllllllllll");
        }
        runOnUiThread( ()->{
            ref.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(InformacionSalonActivity.this).load(uri).into(imagen_perfil_info_salon_activity));
        });

        final long[] fotos = new long[1];
        final ArrayList<Uri> uris = new ArrayList<>();
//        gridCatalogo =  findViewById(R.id.grid_Catalogo_informacion_salon_activity);
//        adapterCatalogo= new AdapterCatalogo(InformacionSalonActivity.this,uris);
//        gridCatalogo.setAdapter(adapterCatalogo);
//        gridCatalogo.setExpanded(true);
        // Catálogo
        rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("fotos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long fotos = (Long) dataSnapshot.getValue();
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

                //Catalogo

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //FAVORITOS
        if(auth.getCurrentUser()!=null) {
            rtdb.getReference().child("favoritos").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.equals(null) && dataSnapshot.hasChild(nombreSalon)) {
                        btn_anadir_favoritos.setBackgroundResource(R.drawable.fav_seleccionado_128);
                        favoritoMarcado = true;
                    } else {
                        btn_anadir_favoritos.setBackgroundResource(R.drawable.fav_128_sin_seleccionar);
                        favoritoMarcado = false;
                    }
                    btn_anadir_favoritos.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            btn_anadir_favoritos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (favoritoMarcado) {
                        btn_anadir_favoritos.setBackgroundResource(R.drawable.fav_128_sin_seleccionar);
                        favoritoMarcado = false;
                        rtdb.getReference().child("favoritos").child(auth.getCurrentUser().getUid()).child(nombreSalon).removeValue();
                        Toast.makeText(InformacionSalonActivity.this, nombreSalon + " se ha eliminado de tus favoritos", Toast.LENGTH_SHORT).show();
                    } else {
                        btn_anadir_favoritos.setBackgroundResource(R.drawable.fav_seleccionado_128);
                        favoritoMarcado = true;
                        rtdb.getReference().child("favoritos").child(auth.getCurrentUser().getUid()).child(nombreSalon).setValue(nombreSalon);
                        Toast.makeText(InformacionSalonActivity.this, "Añadiste a " + nombreSalon + " a tus favoritos", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        //Mostrar servicios en adapter
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

        //Mostrar dirección
        rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("direccion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        txt_informacion_direccion_salon.setText(dataSnapshot.getValue()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btn_agendar_cita.setOnClickListener(v -> {
            if(auth.getCurrentUser()==null){
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(v.getContext());
                dialogo1.setTitle("Registro");
                dialogo1.setMessage("Por favor inicia sesión");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Aceptar", (dialogo11, id) -> {
                    Intent i = new Intent(InformacionSalonActivity.this,InicioActivity.class);
                    startActivity(i);
                    finish();
                });
                dialogo1.setNegativeButton("Cancelar", (dialogo112, id) -> {

                });
                dialogo1.show();

            }
            else{
                Intent i = new Intent(InformacionSalonActivity.this, AgendarCitaActivity.class);
                i.putExtra("salon", getIntent().getExtras().get("salon").toString());
                startActivity(i);
            }

        });


        rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("latitud").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double latitud = (double)dataSnapshot.getValue();
                rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("longitud").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        double longitud = (double)dataSnapshot.getValue();

                        btn_como_llegar.setOnClickListener(v -> {
                            Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitud+","+longitud);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(mapIntent);
                            }
                        });

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });



        ////Calificación

        rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("calificacion").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String cal=  dataSnapshot.getValue(String.class);
                double calificacion = Double.parseDouble(cal);
                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.CEILING);
                txt_calificacion.setText(df.format(calificacion)+"");
                mostrarEstrellas(calificacion);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        rtdb.getReference().child("Salon de belleza").child(nombreSalon).child("numeroCalificaciones").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numero = (Long) dataSnapshot.getValue();
                txt_calificadores.setText("("+numero+") calificaciones");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // CATALAGO

        // Depilación

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

        // Maquillaje

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

        // Masaje
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

        // Peluqueria
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

        // Uñas
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void mostrarEstrellas(double calificacion){
        if(calificacion>=0.5){
            if(calificacion>=1){
                estrella_calificacion1.setImageResource(R.drawable.ic_estrella_llena);
                if(calificacion>=1.5){
                    if(calificacion>=2){
                        estrella_calificacion2.setImageResource(R.drawable.ic_estrella_llena);
                        if(calificacion>=2.5){
                            if(calificacion>=3){
                                estrella_calificacion3.setImageResource(R.drawable.ic_estrella_llena);
                                if(calificacion>=3.5){
                                    if(calificacion>=4.0){
                                        estrella_calificacion4.setImageResource(R.drawable.ic_estrella_llena);
                                        if(calificacion>=4.5){
                                            if(calificacion==5)
                                                estrella_calificacion5.setImageResource(R.drawable.ic_estrella_llena);
                                            else
                                                estrella_calificacion5.setImageResource(R.drawable.ic_estrella_mitad);
                                        }else{
                                            estrella_calificacion5.setImageResource(R.drawable.ic_estrella_vacia);
                                        }
                                    }else{
                                        estrella_calificacion4.setImageResource(R.drawable.ic_estrella_mitad);
                                        estrella_calificacion5.setImageResource(R.drawable.ic_estrella_vacia);
                                    }
                                }else{
                                    estrella_calificacion4.setImageResource(R.drawable.ic_estrella_vacia);
                                    estrella_calificacion5.setImageResource(R.drawable.ic_estrella_vacia);
                                }
                            }else{
                                estrella_calificacion3.setImageResource(R.drawable.ic_estrella_mitad);
                                estrella_calificacion4.setImageResource(R.drawable.ic_estrella_vacia);
                                estrella_calificacion5.setImageResource(R.drawable.ic_estrella_vacia);
                            }
                        }else{
                            estrella_calificacion3.setImageResource(R.drawable.ic_estrella_vacia);
                            estrella_calificacion4.setImageResource(R.drawable.ic_estrella_vacia);
                            estrella_calificacion5.setImageResource(R.drawable.ic_estrella_vacia);
                        }
                    }else{
                        estrella_calificacion2.setImageResource(R.drawable.ic_estrella_mitad);
                        estrella_calificacion3.setImageResource(R.drawable.ic_estrella_vacia);
                        estrella_calificacion4.setImageResource(R.drawable.ic_estrella_vacia);
                        estrella_calificacion5.setImageResource(R.drawable.ic_estrella_vacia);

                    }

                }else{
                    estrella_calificacion2.setImageResource(R.drawable.ic_estrella_vacia);
                    estrella_calificacion3.setImageResource(R.drawable.ic_estrella_vacia);
                    estrella_calificacion4.setImageResource(R.drawable.ic_estrella_vacia);
                    estrella_calificacion5.setImageResource(R.drawable.ic_estrella_vacia);

                }
            }else{
                estrella_calificacion1.setImageResource(R.drawable.ic_estrella_mitad);
                estrella_calificacion2.setImageResource(R.drawable.ic_estrella_vacia);
                estrella_calificacion3.setImageResource(R.drawable.ic_estrella_vacia);
                estrella_calificacion4.setImageResource(R.drawable.ic_estrella_vacia);
                estrella_calificacion5.setImageResource(R.drawable.ic_estrella_vacia);

            }
        }else{
            estrella_calificacion1.setImageResource(R.drawable.ic_estrella_vacia);
            estrella_calificacion2.setImageResource(R.drawable.ic_estrella_vacia);
            estrella_calificacion3.setImageResource(R.drawable.ic_estrella_vacia);
            estrella_calificacion4.setImageResource(R.drawable.ic_estrella_vacia);
            estrella_calificacion5.setImageResource(R.drawable.ic_estrella_vacia);

        }
    }

    public void adapterDepilacion(ArrayList<FotoCatalogo> fotos) {
        gridCatalogoDepilacion = findViewById(R.id.grid_catalogo_depilacion_activity_informacion_salon);
        adapterCatalogoDepilacion = new AdapterCatalogoPerfilSalon(this, fotos, "User");
        adapterCatalogoDepilacion.notifyDataSetChanged();
        gridCatalogoDepilacion.setAdapter(adapterCatalogoDepilacion);
        gridCatalogoDepilacion.setExpanded(true);
    }

    public void adapterMaquillaje(ArrayList<FotoCatalogo> fotos) {
        gridCatalogoMaquillaje = findViewById(R.id.grid_catalogo_maquillaje_activity_informacion_salon);
        adapterCatalogoMaquillaje = new AdapterCatalogoPerfilSalon(this, fotos, "User");
        gridCatalogoMaquillaje.setAdapter(adapterCatalogoMaquillaje);
        gridCatalogoMaquillaje.setExpanded(true);
    }

    public void adapterMasaje(ArrayList<FotoCatalogo> fotos) {
        gridCatalogoMasaje = findViewById(R.id.grid_catalogo_masaje_activity_informacion_salon);
        adapterCatalogoMasaje = new AdapterCatalogoPerfilSalon(this, fotos, "User");
        gridCatalogoMasaje.setAdapter(adapterCatalogoMasaje);
        gridCatalogoMasaje.setExpanded(true);
    }

    public void adapterPeluqueria(ArrayList<FotoCatalogo> fotos) {
        gridCatalogoPeluqueria = findViewById(R.id.grid_catalogo_peluqueria_activity_informacion_salon);
        adapterCatalogoPeluqueria = new AdapterCatalogoPerfilSalon(this, fotos, "User");
        gridCatalogoPeluqueria.setAdapter(adapterCatalogoPeluqueria);
        gridCatalogoPeluqueria.setExpanded(true);
    }

    public void adapterUnhas(ArrayList<FotoCatalogo> fotos) {
        gridCatalogoUnhas = findViewById(R.id.grid_catalogo_unhas_activity_informacion_salon);
        adapterCatalogoUnhas = new AdapterCatalogoPerfilSalon(this, fotos, "User");
        gridCatalogoUnhas.setAdapter(adapterCatalogoUnhas);
        gridCatalogoUnhas.setExpanded(true);
    }

}
