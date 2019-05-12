package proyectohastafinal.almac.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class CatalogoActivity extends AppCompatActivity {

    private ImageView iv_servicio_catalogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        Intent datos = getIntent();
        int ref = datos.getExtras().getInt("servicio");
        iv_servicio_catalogo = findViewById(R.id.iv_servicio_catalogo);
        iv_servicio_catalogo.setImageResource(ref);
    }
}
