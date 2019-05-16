package proyectohastafinal.almac.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.Cita;

public class CitasFragment extends Fragment implements AdapterCitas.OnItemClickListener{

    private static CitasFragment instance;
    FirebaseDatabase rtdb;
    FirebaseAuth auth;

    private AdapterCitas adapterCitas;
    private RecyclerView lista_citas;

    private TextView txt_iniciar_sesion_fragment_citas;
    private Button btn_iniciar_sesion_fragment_citas;

    private Cita citaseleccionada;

    public static CitasFragment getInstance(){
        instance = instance == null ? new CitasFragment() : instance;
        return instance;
    }

    public CitasFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_citas, container, false);

        auth = FirebaseAuth.getInstance();

        txt_iniciar_sesion_fragment_citas = v.findViewById(R.id.txt_iniciar_sesion_fragment_citas);
        btn_iniciar_sesion_fragment_citas = v.findViewById(R.id.btn_iniciar_sesion_fragment_citas);

        btn_iniciar_sesion_fragment_citas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),InicioActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        if(auth.getCurrentUser() == null){
            return v;
        }

        txt_iniciar_sesion_fragment_citas.setVisibility(TextView.INVISIBLE);
        btn_iniciar_sesion_fragment_citas.setVisibility(TextView.INVISIBLE);

        rtdb = FirebaseDatabase.getInstance();

        lista_citas = v.findViewById(R.id.lista_citas);
        adapterCitas = new AdapterCitas();
        adapterCitas.setListener(this);
        lista_citas.setLayoutManager(new LinearLayoutManager(v.getContext()));
        lista_citas.setAdapter(adapterCitas);
        lista_citas.setHasFixedSize(true);

        registerForContextMenu(lista_citas);

        rtdb.getReference().child("usuario").child(auth.getCurrentUser().getUid()).child("citas").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<String> idcitas = new ArrayList<>();

                for (DataSnapshot hijo:dataSnapshot.getChildren())
                    idcitas.add(hijo.getValue().toString());


                for (int i=0;i<idcitas.size();i++) {

                    rtdb.getReference().child("citas").child(idcitas.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Cita cita = (dataSnapshot.getValue(Cita.class));
                                adapterCitas.agregarcita(cita);
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

        return v;
    }

    @Override
    public void onItemClick(Cita cita) {
            citaseleccionada = cita;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if(citaseleccionada!=null) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu_citas, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {

            case R.id.envio_mensaje_estilista_cita:

                //Vamos a abrir la ventana de chat
                Intent i = new Intent(getActivity(),ChatActivity.class);
                    i.putExtra("tel", citaseleccionada.getTelefonoEstilista());
                    startActivity(i);



                break;

        }

        return super.onContextItemSelected(item);
    }

}
