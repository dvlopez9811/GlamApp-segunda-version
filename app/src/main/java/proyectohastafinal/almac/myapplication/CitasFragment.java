package proyectohastafinal.almac.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewAnimator;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.Cita;
import proyectohastafinal.almac.myapplication.model.SalonDeBelleza;

public class CitasFragment extends Fragment{

    private static CitasFragment instance;
    FirebaseDatabase rtdb;
    FirebaseAuth auth;
    private ArrayList<Cita> citas;

    private AdapterCitas adapterCitas;
    private RecyclerView lista_citas;

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

        rtdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        citas = new ArrayList<>();

        lista_citas = v.findViewById(R.id.lista_citas);
        adapterCitas = new AdapterCitas();
        lista_citas.setLayoutManager(new LinearLayoutManager(v.getContext()));
        lista_citas.setAdapter(adapterCitas);
        lista_citas.setHasFixedSize(true);


        rtdb.getReference().child("usuario").child(auth.getCurrentUser().getUid()).child("citas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<String> idcitas = new ArrayList<>();

                for (DataSnapshot hijo:dataSnapshot.getChildren())
                    idcitas.add(hijo.getValue().toString());

                for (int i=0;i<idcitas.size();i++) {

                    rtdb.getReference().child("citas").child(idcitas.get(i)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Cita cita = (Cita) dataSnapshot.getValue();
                            citas.add(cita);
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
    public void onResume() {
        super.onResume();
        adapterCitas.mostrarcitas(citas);
    }
}
