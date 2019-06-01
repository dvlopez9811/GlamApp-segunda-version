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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;
import proyectohastafinal.almac.myapplication.model.Servicio;

public class InformacionSalonActivity extends AppCompatActivity {


    private RecyclerView listaServicios;
    private AdapterServiciosInformacionSalon adapterServicios;
    private Button btn_agendar_cita,btn_anadir_favoritos,btn_volver;
    private TextView txt_titulo_salon,txt_descripcion_servicios,txt_informacion_direccion_salon;
    private ImageView imagen_perfil_info_salon_activity,estrella_calificacion1,estrella_calificacion2
            ,estrella_calificacion3,estrella_calificacion4,estrella_calificacion5;

    FirebaseDatabase rtdb;
    FirebaseAuth auth;
    FirebaseStorage storage;
    private boolean favoritoMarcado;

    private ExpandableHeightGridView gridCatalogo;
    private AdapterCatalogo adapterCatalogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_salon);

        btn_anadir_favoritos = findViewById(R.id.btn_anadir_favoritos_informacion_salon_activity);
        btn_volver = findViewById(R.id.btn_atras_informacion_salon_activity);
        btn_agendar_cita = findViewById(R.id.btn_agendar_cita_info_salon_activity);
        txt_titulo_salon = findViewById(R.id.titulo_salon_informacion_salon_activity);
        txt_informacion_direccion_salon = findViewById(R.id.txt_informacion_direccion_salon);
        //txt_descripcion_servicios = findViewById(R.id.txt_resumen_servicios_info_salon_activity);
        imagen_perfil_info_salon_activity = findViewById(R.id.imagen_perfil_info_salon_activity);
        estrella_calificacion1 = findViewById(R.id.calificacion_1_informacion_salon);
        estrella_calificacion2 = findViewById(R.id.calificacion_2_informacion_salon);
        estrella_calificacion3 = findViewById(R.id.calificacion_3_informacion_salon);
        estrella_calificacion4 = findViewById(R.id.calificacion_4_informacion_salon);
        estrella_calificacion5 = findViewById(R.id.calificacion_5_informacion_salon);

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
        gridCatalogo =  findViewById(R.id.grid_Catalogo_informacion_salon_activity);
        adapterCatalogo= new AdapterCatalogo(InformacionSalonActivity.this,uris);
        gridCatalogo.setAdapter(adapterCatalogo);
        gridCatalogo.setExpanded(true);


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
                Log.e("entra", uris.size()+"");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btn_anadir_favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO        Implementar cambio de favoritos en base de datos
                if(favoritoMarcado){
                    btn_anadir_favoritos.setBackgroundResource(R.drawable.fav_128_sin_seleccionar);
                    favoritoMarcado=false;
                    Toast.makeText(InformacionSalonActivity.this,nombreSalon+" se ha eliminado de tus favoritos",Toast.LENGTH_SHORT).show();
                }
                else {
                    btn_anadir_favoritos.setBackgroundResource(R.drawable.fav_seleccionado_128);
                    favoritoMarcado=true;
                    Toast.makeText(InformacionSalonActivity.this,"Añadiste a "+nombreSalon+" a tus favoritos",Toast.LENGTH_SHORT).show();
                }
            }
        });


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

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
