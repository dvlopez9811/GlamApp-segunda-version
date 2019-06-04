package proyectohastafinal.almac.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import proyectohastafinal.almac.myapplication.model.BusquedaSalonDeBelleza;
import proyectohastafinal.almac.myapplication.model.Servicio;

public class AdapterFavoritos extends RecyclerView.Adapter<AdapterFavoritos.CustomViewHolder> {


    ArrayList<BusquedaSalonDeBelleza> salones;
    FirebaseDatabase rtdb;
    FirebaseAuth auth;
    FirebaseStorage storage;
    private AdapterFavoritos.OnItemClickListener listener;
    public interface OnItemClickListener{
        void onItemClick(BusquedaSalonDeBelleza salonDeBelleza);
    }
    public void setListener(AdapterFavoritos.OnItemClickListener listener){
        this.listener = listener;
    }

    public AdapterFavoritos(){
        salones = new ArrayList<>();
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.renglon_favoritos, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        rtdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        BusquedaSalonDeBelleza busquedaSalonDeBelleza = salones.get(position);
        ((TextView)holder.root.findViewById(R.id.txt_nombre_salon_favoritos)).setText(busquedaSalonDeBelleza.getNombreSalonDeBelleza());

        StorageReference ref = storage.getReference().child("salones de belleza").child(busquedaSalonDeBelleza.getNombreSalonDeBelleza()).child("profile");
        if(ref == null){

        }else {
            Glide.with(holder.root.getContext()).load(R.drawable.default_salon_fav).apply(RequestOptions.circleCropTransform())
                    .into((ImageView) holder.root.findViewById(R.id.foto_perfil_salon_renglon_favoritos));
            ref.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(holder.root.getContext())
                    .load(uri).apply(RequestOptions.circleCropTransform()).into((ImageView) holder.root.findViewById(R.id.foto_perfil_salon_renglon_favoritos)));
        }

        holder.root.findViewById(R.id.renglon_favorito).setOnClickListener(v1 -> listener.onItemClick(salones.get(position)));

        rtdb.getReference().child("Salon de belleza").child(salones.get(position).getNombreSalonDeBelleza()).child("direccion").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String direccion = dataSnapshot.getValue(String.class);
                ((TextView)holder.root.findViewById(R.id.txt_direccion_renglon_favoritos)).setText(direccion);
                rtdb.getReference().child("Salon de belleza").child(salones.get(position).getNombreSalonDeBelleza()).child("calificacion").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String cal=  dataSnapshot.getValue(String.class);
                        double calificacion = Double.parseDouble(cal);
                        DecimalFormat df = new DecimalFormat("#.##");
                        df.setRoundingMode(RoundingMode.CEILING);
                        ((TextView)holder.root.findViewById(R.id.txt_calificacion_renglon_favoritos)).setText(df.format(calificacion)+"");
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
    }



    public void showAllFavoritos(ArrayList<BusquedaSalonDeBelleza> allsalones) {
        salones = new ArrayList<>();
        for(int i = 0 ; i<allsalones.size() ; i++){
            salones.add(allsalones.get(i));
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return salones.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;
        }
    }

}
