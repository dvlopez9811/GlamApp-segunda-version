package proyectohastafinal.almac.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class InicioFragment extends Fragment implements View.OnClickListener {

    private Button btn_ingresar_inicio;
    private ImageView iv_peluqueria_inicio;
    private ImageView iv_unas_inicio;
    private ImageView iv_maquillaje_inicio;
    private ImageView iv_depilacion_inicio;
    private ImageView iv_masaje_inicio;
    private Button btn_buscar_inicio;

    FirebaseAuth auth;


    private static InicioFragment instance;

    public static InicioFragment getInstance(){
        instance = instance == null ? new InicioFragment() : instance;
        return instance;
    }

    public InicioFragment() {
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
        View v = inflater.inflate(R.layout.fragment_inicio, container, false);

        btn_ingresar_inicio = v.findViewById(R.id.btn_ingresar_inicio);
        iv_peluqueria_inicio = v.findViewById(R.id.iv_peluqueria_inicio);
        iv_unas_inicio = v.findViewById(R.id.iv_unas_inicio);
        iv_maquillaje_inicio = v.findViewById(R.id.iv_maquillaje_inicio);
        iv_depilacion_inicio = v.findViewById(R.id.iv_depilacion_inicio);
        iv_masaje_inicio = v.findViewById(R.id.iv_masaje_inicio);
        btn_buscar_inicio = v.findViewById(R.id.btn_buscar_inicio);

        btn_ingresar_inicio.setOnClickListener(this);
        iv_peluqueria_inicio.setOnClickListener(this);
        iv_unas_inicio.setOnClickListener(this);
        iv_maquillaje_inicio.setOnClickListener(this);
        iv_depilacion_inicio.setOnClickListener(this);
        iv_masaje_inicio.setOnClickListener(this);
        btn_buscar_inicio.setOnClickListener(this);

        if(auth != null)
            btn_ingresar_inicio.setVisibility(Button.INVISIBLE);

        return v;
    }

    @Override
    public void onClick(View v) {

        Intent i;

        switch(v.getId()){


            case R.id.iv_peluqueria_inicio | R.id.iv_unas_inicio | R.id.iv_maquillaje_inicio | R.id.iv_depilacion_inicio | R.id.iv_masaje_inicio:

                Toast.makeText(getContext(),"ENTRA",Toast.LENGTH_SHORT).show();
                ImageView imageView = (ImageView) v;

                if (imageView.isSelected()) {
                    imageView.setColorFilter(Color.rgb(255, 255, 255), android.graphics.PorterDuff.Mode.MULTIPLY);
                    imageView.setSelected(false);
                } else {
                    imageView.setColorFilter(Color.rgb(155, 155, 155), android.graphics.PorterDuff.Mode.MULTIPLY);
                    imageView.setSelected(true);
                }
                break;

            case R.id.btn_ingresar_inicio:

                Toast.makeText(getContext(),getView().getId()+"+" +R.id.btn_ingresar_inicio,Toast.LENGTH_SHORT).show();
                i = new Intent(getActivity(),LoginActivity.class);
                startActivity(i);
                getActivity().finish();
                break;

            case R.id.btn_buscar_inicio:
                i = new Intent(getActivity(),ResultadoBusquedaSalonActivity.class);
                startActivity(i);
                getActivity().finish();
                break;

        }

    }
}
