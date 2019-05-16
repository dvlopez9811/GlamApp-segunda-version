package proyectohastafinal.almac.myapplication;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.Servicio;

public class AdapterItemsAgendarCita extends RecyclerView.Adapter<AdapterItemsAgendarCita.CustomViewHolder>{

    ArrayList<Servicio> data;

    FirebaseDatabase rtdb;

    public AdapterItemsAgendarCita(){
        data = new ArrayList<>();
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agendar_cita, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        rtdb = FirebaseDatabase.getInstance();

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {
        ((TextView) holder.root.findViewById(R.id.txt_tipo_servicio_item_agendar_cita)).setText(data.get(position).getTipo());
        ((TextView) holder.root.findViewById(R.id.txt_nombre_salon_item_agendar_cita)).setText(data.get(position).getSalonDeBelleza().getNombreSalonDeBelleza());


        final ArrayList<CharSequence> estilistas = new ArrayList<>();

        rtdb.getReference().child("Salon de belleza").child(data.get(position).getSalonDeBelleza().getNombreSalonDeBelleza()).child("Estilistas")
                .child(data.get(position).getTipo()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    estilistas.add(childDataSnapshot.getKey());
                    ArrayAdapter<CharSequence> estilistasAdapter = new ArrayAdapter<>(holder.root.getContext(),
                            R.layout.support_simple_spinner_dropdown_item,estilistas);
                    ((Spinner)holder.root.findViewById(R.id.item_agendar_cita_spinner_estilista)).setAdapter(estilistasAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;
        }
    }

}
