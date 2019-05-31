package proyectohastafinal.almac.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.Estilista;

public class MensajesEstilistaFragment extends Fragment implements AdapterMensajesEstilista.OnItemClickListener{

    private static MensajesEstilistaFragment instance;

    FirebaseDatabase rtdb;
    FirebaseAuth auth;

    private String telefonopropio;
    private String usuario;

    private AdapterMensajesEstilista adapterMensajesEstilista;
    private RecyclerView listamensajesEstilista;

    public static MensajesEstilistaFragment getInstance(){
        instance = instance == null ? new MensajesEstilistaFragment() : instance;
        return instance;
    }

    public MensajesEstilistaFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_mensajes_estilista, container, false);

        auth = FirebaseAuth.getInstance();
        rtdb = FirebaseDatabase.getInstance();

        listamensajesEstilista = v.findViewById(R.id.lista_mensajes_estilista);
        adapterMensajesEstilista = new AdapterMensajesEstilista();
        adapterMensajesEstilista.setListener(this);
        listamensajesEstilista.setLayoutManager(new LinearLayoutManager(v.getContext()));
        listamensajesEstilista.setAdapter(adapterMensajesEstilista);
        listamensajesEstilista.setHasFixedSize(true);

        rtdb.getReference().child("Estilista").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Estilista estilista = dataSnapshot.getValue(Estilista.class);

                telefonopropio = estilista.getTelefono();
                usuario = estilista.getUsuario();
                rtdb.getReference().child("chat").child(telefonopropio).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot dsp: dataSnapshot.getChildren()) {
                            adapterMensajesEstilista.agregarusuario(dsp.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        return v;



    }

    @Override
    public void onItemClick(String telefono) {

        Intent i = new Intent(getActivity(),ChatActivity.class);
        i.putExtra("telUsuario", telefono);
        i.putExtra("telEstilista",telefonopropio);
        i.putExtra("usEstilista",usuario);
        startActivity(i);


    }
}
