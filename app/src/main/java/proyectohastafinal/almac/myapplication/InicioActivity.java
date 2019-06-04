package proyectohastafinal.almac.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import proyectohastafinal.almac.myapplication.model.Cliente;

public class InicioActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 11;
    private static final String TAG = "FacebookLogin";
    private static final int RC_SIGN_IN_GOOGLE = 9001;

    private ImageButton btn_salir_inicio_activity;

    private Button btn_inicio_sesion_facebook;
    private Button btn_inicio_sesion_google;
    private Button btn_inicio_sesion_email;

    private TextView txt_crear_cuenta;

    private LoginButton loginButton;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    FirebaseAuth auth;
    FirebaseDatabase rtdb;
    String volverACargar;

    private Intent intentMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        Bundle extras = getIntent().getExtras();
        volverACargar = "";
        if (extras != null ){
            volverACargar = extras.getString("NoIniciar");
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verificando usuario");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Lo primero que se realiza es solicitar todos los permisos necesarios para ejecutar la aplicación.
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, REQUEST_CODE);

        // Se obtiene la referencia de Firebase de autenticación y la base de datos.
        auth = FirebaseAuth.getInstance();
        rtdb = FirebaseDatabase.getInstance();

        /*
        Se inicializar la clase principal. No se coloca como "Launch" debido a que el primer fragment
        que se carga necesita de los permisos de localización.
         */
        intentMain = new Intent(InicioActivity.this, MainActivity.class);

        // Si ya hay una sesión iniciada, esta pantalla no se muestra.
        if (auth.getCurrentUser() != null) {
            // LO NUEVO - se muestra por poco tiempo porque hay que verificar de quien es
            rtdb.getReference().child("identificador").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String tipo = dataSnapshot.getValue(String.class);
                    if(tipo.equals("cliente")){
                        Intent i = new Intent(InicioActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }else if(tipo.equals("estilista")){
                        Intent i = new Intent(InicioActivity.this, MainEstilistaActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Intent i = new Intent(InicioActivity.this, MainSalonActivity.class);
                        startActivity(i);
                        finish();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        // Se inicializan los componentes gráficos necesarios de la actividad.
        btn_salir_inicio_activity = findViewById(R.id.btn_salir_inicio_activity);
        btn_inicio_sesion_email = findViewById(R.id.btn_correo_inicio_activity);
        btn_inicio_sesion_google = findViewById(R.id.btn_google_inicio_activity);
        btn_inicio_sesion_facebook=findViewById(R.id.btn_facebook_inicio_activity);
        txt_crear_cuenta=findViewById(R.id.txt_crear_cuenta_inicio_activity);

        // Tratamiento del inicio de sesión de Facebook.
        mCallbackManager = CallbackManager.Factory.create();

        loginButton = findViewById(R.id.btn_login_facebook_inicio_activity);
        loginButton.setReadPermissions("email", "public_profile", "user_friends");

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }
            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

        btn_inicio_sesion_facebook.setOnClickListener(v -> {
            loginButton.callOnClick();
            loginButton.performClick();
        });

        // Tratamiento del inicio de sesión de Google.
        btn_inicio_sesion_google.setOnClickListener(v -> signInGoogle());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btn_inicio_sesion_email.setOnClickListener(v -> {
            Intent intentLogin = new Intent(InicioActivity.this,LoginActivity.class);
            startActivity(intentLogin);
            finish();
        });

        // Listener del botón crear cuenta, este botón manda a la actividad de registro.
        txt_crear_cuenta.setOnClickListener(v -> {
            Intent intentRegistro = new Intent(InicioActivity.this,RegistroActivity.class);
            startActivity(intentRegistro);
        });

        // Listener del botón salir, aquí se manda a la clase principal.
        btn_salir_inicio_activity.setOnClickListener(v -> {
            if(volverACargar.equals("")) {
                startActivity(intentMain);
                finish();
            } else
                onBackPressed();
        });

        progressDialog.dismiss();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            //updateUI(user);
                            AccessToken accessToken = AccessToken.getCurrentAccessToken();
                            verificarExistenciaUsuarioFacebook(task,accessToken);
                            startActivity(intentMain);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void verificarExistenciaUsuarioFacebook(@NonNull Task<AuthResult> task,AccessToken token){
        boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
        if(isNew){
            anadirNuevoUsuarioFacebook(token);
        }
    }

    private void verificarExistenciaUsuarioGoogle(@NonNull Task<AuthResult> task){
        boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
        if(isNew){
            anadirNuevoUsuarioGoogle();
        }
    }

    private void anadirNuevoUsuarioFacebook(AccessToken token){
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String name = object.getString("name");
                    String email = object.getString("email");
                    String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    crearUsuarioDeFacebookOGoogle(name,email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);

        // Initiate the GraphRequest
        request.executeAsync();
    }

    private void anadirNuevoUsuarioGoogle(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            crearUsuarioDeFacebookOGoogle(personName,personEmail);
        }
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                            verificarExistenciaUsuarioGoogle(task);
                            startActivity(intentMain);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            findViewById(R.id.btn_login_facebook_inicio_activity).setVisibility(View.GONE);
        } else {
            findViewById(R.id.btn_login_facebook_inicio_activity).setVisibility(View.VISIBLE);
        }
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

    private void crearUsuarioDeFacebookOGoogle(String nombre,String email){
        Cliente cl = new Cliente(email, "", nombre, "","");
        rtdb.getReference().child("usuario").child(auth.getCurrentUser().getUid()).setValue(cl);
        rtdb.getReference().child("identificador").child(auth.getCurrentUser().getUid()).setValue("cliente");
    }
}