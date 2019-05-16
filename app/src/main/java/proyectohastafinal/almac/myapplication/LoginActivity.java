package proyectohastafinal.almac.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.Login;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
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

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    // Codigo de los permisos
    private static final int MY_REQUEST_CODE= 7117;

    private static final String TAG = "FacebookLogin";
    private static final int RC_SIGN_IN_GOOGLE = 9001;


    private Button btn_iniciar_sesion,btn_inicio_sesion_facebook,btn_inicio_sesion_google;
    private Button  btn_login_volver;
    private LoginButton loginButton;
    private EditText et_login_correo, et_login_password;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    List<AuthUI.IdpConfig> providers;
    FirebaseAuth auth;
    FirebaseDatabase rtdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        rtdb = FirebaseDatabase.getInstance();

        Log.e("INFORMACION", (auth == null ? "en realidad":"galso"));

        btn_iniciar_sesion= findViewById(R.id.btn_iniciarsesion);
        btn_inicio_sesion_google=findViewById(R.id.btn_login_google);
        et_login_correo = findViewById(R.id.et_usuario);
        et_login_password=findViewById(R.id.et_password);
        btn_inicio_sesion_facebook=findViewById(R.id.boton_facebook_personalizado);
        btn_login_volver = findViewById(R.id.btn_login_volver);

        mCallbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.btn_login_facebook);
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

        //GOOGLE
        btn_inicio_sesion_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //E-MAIL Y PASSWORD
        btn_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String correo = et_login_correo.getText().toString();
                String pass = et_login_password.getText().toString();
                if (!correo.equals("") && !pass.equals("")) {
                    auth.signInWithEmailAndPassword(correo, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            rtdb.getReference().child("identificador").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String tipo = dataSnapshot.getValue(String.class);
                                    if(tipo.equals("cliente")){
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }else{
                                        Intent i = new Intent(LoginActivity.this, MainEstilistaActivity.class);
                                        startActivity(i);
                                        finish();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();

                }
            }
        });

        btn_inicio_sesion_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.callOnClick();
                loginButton.performClick();
            }
        });



        btn_login_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,InicioActivity.class);
                startActivity(i);
                finish();
            }
        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
    }


    private void crearUsuarioNuevo(String nombre,String email){
        //*******************Crear usuario e ingresar a base de datos aqui


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
                        crearUsuarioNuevo(name,email);
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
            crearUsuarioNuevo(personName,personEmail);
        }
    }

    private void verificarExistenciaUsuarioFacebook(@NonNull Task<AuthResult> task,AccessToken token){
        boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
        if(isNew){
            Toast.makeText(LoginActivity.this, "Este usuario es nuevo",Toast.LENGTH_SHORT).show();
            anadirNuevoUsuarioFacebook(token);
        }
        else{
            Toast.makeText(LoginActivity.this, "Este usuario ya existe",Toast.LENGTH_SHORT).show();
        }
    }

    private void verificarExistenciaUsuarioGoogle(@NonNull Task<AuthResult> task){
        boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
        if(isNew){
            Toast.makeText(LoginActivity.this, "Este usuario es nuevo",Toast.LENGTH_SHORT).show();
            anadirNuevoUsuarioGoogle();
        }
        else{
            Toast.makeText(LoginActivity.this, "Este usuario ya existe",Toast.LENGTH_SHORT).show();
        }
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    @Override
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
                            Toast.makeText(LoginActivity.this,"Ingresaste con correo "+user.getEmail(),Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
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
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
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

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            findViewById(R.id.btn_login_facebook).setVisibility(View.GONE);
        } else {
            findViewById(R.id.btn_login_facebook).setVisibility(View.VISIBLE);
        }
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(LoginActivity.this, InicioActivity.class);
        startActivity(i);
        finish();
    }
}