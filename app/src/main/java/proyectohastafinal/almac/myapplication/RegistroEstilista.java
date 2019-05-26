package proyectohastafinal.almac.myapplication;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import proyectohastafinal.almac.myapplication.model.Estilista;
import proyectohastafinal.almac.myapplication.model.Horario;
import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;
import proyectohastafinal.almac.myapplication.util.UtilDomi;

public class RegistroEstilista extends AppCompatActivity {

    private static final String [] DIAS_SEMANA  ={"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    private static final int GALLERY_CALLBACK_ID = 101;

    private Spinner spinnerSalonesDeBelleza;
    private Spinner spinnerFechaIncio;
    private Spinner spinnerFechaFinal;

    private Button registro_estilista_btn_listo;
    private Button btn_registro_estilista_volver;

    public TextView etObtenerHoraInicio;
    public TextView etObtenerHoraFinal;

    private String servicios;
    private HashMap<String, Boolean> serviciosPrestados;

    private LinearLayout help_layout_linear;

    private ImageView registro_estilista_iv_foto;
    FirebaseAuth auth;
    FirebaseDatabase rtdb;
    FirebaseStorage storage;

    private File photoFile;

    ArrayList<CheckBox> checkBox;

    private int dia1;
    private int horainicio;
    private int horafin;


    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_estilista);

        spinnerSalonesDeBelleza = findViewById(R.id.registro_estilista_spinner_salones_belleza);
        spinnerFechaFinal = findViewById(R.id.registro_estilista_spinner_fecha_final);
        spinnerFechaIncio = findViewById(R.id.registro_estilista_spinner_fecha_inicio);

        registro_estilista_btn_listo = findViewById(R.id.registro_estilista_btn_listo);
        btn_registro_estilista_volver = findViewById(R.id.btn_registro_estilista_volver);

        etObtenerHoraInicio = findViewById(R.id.et_obtener_hora_inicio);
        etObtenerHoraFinal = findViewById(R.id.et_obtener_hora_final);


        etObtenerHoraInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarHoraInicio();
            }
        });

        etObtenerHoraFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarHoraFinal();
            }
        });

        auth = FirebaseAuth.getInstance();
        rtdb = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        registro_estilista_iv_foto = findViewById(R.id.registro_estilista_iv_foto);
        registro_estilista_iv_foto.setOnClickListener(v -> {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i, GALLERY_CALLBACK_ID);
        });

        checkBox = new ArrayList<>();
        final String correoEstilista = getIntent().getExtras().getString("correo");
        final String usuarioEstilista = getIntent().getExtras().getString("usuario");
        final String nombreEstilista = getIntent().getExtras().getString("nombre");
        final String telefonoEstilista = getIntent().getExtras().getString("telefono");
        final String passEstilista = getIntent().getExtras().getString("pass");

        final ArrayList<CharSequence> salonesDeBelleza = new ArrayList<>();

        rtdb.getReference().child("Salon de belleza").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    salonesDeBelleza.add(childDataSnapshot.getKey());
                    ArrayAdapter<CharSequence> salonesDeBellezaAdapter = new ArrayAdapter<>(RegistroEstilista.this, R.layout.spinner_item_salones, salonesDeBelleza);
                    spinnerSalonesDeBelleza.setAdapter(salonesDeBellezaAdapter);

                    spinnerSalonesDeBelleza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            String salon = (String) spinnerSalonesDeBelleza.getItemAtPosition(position);
                            help_layout_linear = findViewById(R.id.help_layout_linear);

                            rtdb.getReference().child("Salon de belleza").child(salon).child("servicios").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    help_layout_linear.removeAllViews();
                                    serviciosPrestados = new HashMap<>();

                                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){
                                        String servicio = childDataSnapshot.getKey();

                                        serviciosPrestados.put(servicio,false);
                                        Typeface typeface = ResourcesCompat.getFont(RegistroEstilista.this,R.font.josefin_sans);

                                        CheckBox checkBox = new CheckBox(RegistroEstilista.this);
                                        checkBox.setText(servicio);
                                        checkBox.setTypeface(typeface);
                                        checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                            if(isChecked){
                                                serviciosPrestados.put(buttonView.getText().toString(),true);
                                            } else {
                                                serviciosPrestados.put(buttonView.getText().toString(),false);
                                            }
                                        });
                                        help_layout_linear.addView(checkBox);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ArrayAdapter<CharSequence> diasDeLaSemana = ArrayAdapter.createFromResource(this, R.array.dias_semana, R.layout.spinner_item_salones);
        spinnerFechaIncio.setAdapter(diasDeLaSemana);
        spinnerFechaFinal.setAdapter(diasDeLaSemana);

        registro_estilista_btn_listo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Estilista estilista = new Estilista(correoEstilista,usuarioEstilista,nombreEstilista,passEstilista,passEstilista);

                ArrayList<Horario> horarios = new ArrayList<>();

                String diaUno = spinnerFechaIncio.getSelectedItem().toString();
                String diaDos = spinnerFechaFinal.getSelectedItem().toString();

                dia1 = 0;
                int dia2 = 0;

                if (diaUno.equals(diaDos)) {

                } else {
                    for (int i = 0; i < DIAS_SEMANA.length; i++) {

                        if (diaUno.equals(DIAS_SEMANA[i])) {
                            dia1 = (i);
                        }

                        if (diaDos.equals(DIAS_SEMANA[i])){
                            dia2 = (i+1);
                        }
                    }
                }

                int tamanhoHorarios = dia2-dia1;


                estilista.setHorarios(horarios);

                rtdb.getReference().child("Salon de belleza").child(spinnerSalonesDeBelleza.getSelectedItem().toString()).child("nombreSalonDeBelleza").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String nombreSalonDeBelleza = dataSnapshot.getValue(String.class);
                        estilista.setNombreSalonDeBelleza(nombreSalonDeBelleza);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                comprobarServiciosEscogidos();

                Toast.makeText(RegistroEstilista.this,servicios,Toast.LENGTH_SHORT).show();

                auth.createUserWithEmailAndPassword(correoEstilista, passEstilista).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        rtdb.getReference().child("Estilista").child(auth.getCurrentUser().getUid()).setValue(estilista);

                        rtdb.getReference().child("identificador").child(auth.getCurrentUser().getUid()).setValue("estilista");

                        for (int i = 0; i< tamanhoHorarios; i++, dia1++) {
                            Horario ho = new Horario(horainicio,horafin);
                            rtdb.getReference().child("Estilista").child(auth.getCurrentUser().getUid()).child("horarios").child(DIAS_SEMANA[dia1]).setValue(ho);
                        }

                        String[] serv = servicios.split(" ");

                        for (int i=0;i<serv.length;i++)
                            rtdb.getReference().child("Salon de belleza").child(spinnerSalonesDeBelleza.getSelectedItem().toString()).child("Estilistas").child(serv[i]).child(auth.getCurrentUser().getUid()).setValue(auth.getCurrentUser().getUid());


                        Intent i = new Intent(RegistroEstilista.this,MainEstilistaActivity.class);
                        startActivity(i);
                        finish();

                    }
                });

            }
        });

        btn_registro_estilista_volver.setOnClickListener(v -> {
            finish();
        });

    }

    public void comprobarServiciosEscogidos () {

        servicios = "";
        for (Map.Entry<String, Boolean> entry : serviciosPrestados.entrySet()) {
            if (entry.getValue())
                servicios += entry.getKey() + " ";
        }
        servicios.trim();
    }

    private void modificarHoraInicio(){
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                try {
                    String horaFormato24 = hourOfDay + DOS_PUNTOS + minute;

                    SimpleDateFormat formato24h = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat formato12h = new SimpleDateFormat("hh:mm a");

                    Date date24h = formato24h.parse(horaFormato24);
                    horainicio = hourOfDay;
                    etObtenerHoraInicio.setText(formato12h.format(date24h));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, hora, minuto, false);

        recogerHora.show();
    }

    private void modificarHoraFinal(){
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                try {
                    String horaFormato24 = hourOfDay + DOS_PUNTOS + minute;

                    SimpleDateFormat formato24h = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat formato12h = new SimpleDateFormat("hh:mm a");

                    Date date24h = formato24h.parse(horaFormato24);
                    horafin = hourOfDay;
                    etObtenerHoraFinal.setText(formato12h.format(date24h));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, hora, minuto, false);
        recogerHora.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == GALLERY_CALLBACK_ID && resultCode == RESULT_OK) {
            final Uri uri = data.getData();
            photoFile = new File(  UtilDomi.getPath(this, uri)  );
            runOnUiThread( () -> {
                registro_estilista_iv_foto.setBackground(null);
                registro_estilista_iv_foto.setImageURI(uri);
            });
        }
    }

    private void subirImagen(){
        StorageReference ref = storage.getReference().child("estilistas").child(auth.getCurrentUser().getUid());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(photoFile);
            ref.putStream(fis).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    cargarFotoPerfil();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void cargarFotoPerfil() {
        StorageReference ref = storage.getReference().child("estilistas").child(auth.getCurrentUser().getUid());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(RegistroEstilista.this).load(uri).into(registro_estilista_iv_foto);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}