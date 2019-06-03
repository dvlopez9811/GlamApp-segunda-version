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
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.BusquedaSalonDeBelleza;

public class FavoritosFragment extends Fragment implements View.OnClickListener, AdapterFavoritos.OnItemClickListener{

    private static FavoritosFragment instance;

    FirebaseAuth auth;
    FirebaseDatabase rtdb;

    private RecyclerView listaSalonesFavoritos;
    private AdapterFavoritos adapterFavoritos;

    public static FavoritosFragment getInstance(){
        instance = instance == null ? new FavoritosFragment() : instance;
        return instance;
    }

    public FavoritosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_favoritos, container, false);
        rtdb=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        listaSalonesFavoritos = mView.findViewById(R.id.listado_salones_favoritos);
        listaSalonesFavoritos.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        adapterFavoritos=new AdapterFavoritos();
        adapterFavoritos.setListener(this);
        ArrayList<BusquedaSalonDeBelleza> salones = new ArrayList<>();
        rtdb.getReference("favoritos").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String nombreSalon=dsp.getValue(String.class);
                    BusquedaSalonDeBelleza salon = new BusquedaSalonDeBelleza();
                    salon.setNombreSalonDeBelleza(nombreSalon);
                    salones.add(salon);
                }
                if(salones.size()==0) {
                    mView.findViewById(R.id.txt_no_hay_salones_favoritos).setVisibility(View.VISIBLE);
                }else mView.findViewById(R.id.txt_no_hay_salones_favoritos).setVisibility(View.GONE);

                listaSalonesFavoritos.setAdapter(adapterFavoritos);
                adapterFavoritos.showAllFavoritos(salones);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return mView;
    }


    public void onItemClick(BusquedaSalonDeBelleza salonDeBelleza) {
        Intent i = new Intent(getActivity(),InformacionSalonActivity.class);
        i.putExtra("salon",salonDeBelleza.getNombreSalonDeBelleza());
        startActivity(i);
       getActivity().recreate();
    }

    @Override
    public void onClick(View v) {

    }
}
