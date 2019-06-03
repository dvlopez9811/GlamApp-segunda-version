package proyectohastafinal.almac.myapplication.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import proyectohastafinal.almac.myapplication.MainActivity;
import proyectohastafinal.almac.myapplication.R;

public class NotificationService extends Service {

    public static final String CHANNEL_ID = "Glam191";
    public static final String CHANNEL_NAME = "Glam19-1";
    public static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
    private NotificationManager manager;
    FirebaseDatabase rtdb;
    FirebaseAuth auth;

    @Override
    public void onCreate() {

        rtdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        Log.e(">>>",auth.getCurrentUser().getUid());
        rtdb.getReference().child("Alerta").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp: dataSnapshot.getChildren()) {
                    String alerta = dsp.getValue().toString();
                    crearNotificacion(alerta);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void crearNotificacion(String mensaje) {
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel canal = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE);
            manager.createNotificationChannel(canal);
        }
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setContentTitle("Alerta")
                .setContentText(mensaje)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        manager.notify(1, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

}
