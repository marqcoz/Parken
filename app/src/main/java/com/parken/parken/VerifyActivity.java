package com.parken.parken;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.google.firebase.auth.FirebaseAuth;

public class VerifyActivity extends AppCompatActivity{

    AutoCompleteTextView countryCode, cel;
    Button verify;
    String celular, codigoPais;
    String nombre, apellido, correo, password, origin;
    String id, column, value;
    View form;

    private FirebaseAuth mAuth;
    private VolleySingleton volley;
    protected RequestQueue fRequestQueue;
    FirebaseAuth.AuthStateListener mAuthListener;
    String phoneNumber, phoneNumberFormatted, code;
    public static VerifyActivity activityVerify;
    private View mProgressView;
    private View mVerifyFormView;
    private ShPref session;
    private LoginActivity loginAct = new LoginActivity();

    @Override
    public  void onBackPressed(){
        if(session != null){
            if(session.getVerifying()){ } else{ super.onBackPressed(); }
        }else{
            super.onBackPressed();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar(true);


        Intent intent = getIntent();
        origin = intent.getStringExtra("origin");
        if(origin.equals("createActivity")){
            nombre = intent.getStringExtra("nombre");
            apellido = intent.getStringExtra("apellido");
            correo = intent.getStringExtra("correo");
            password = intent.getStringExtra("password");
        }else{
            id = intent.getStringExtra("id");
            column = intent.getStringExtra("column");
            value = intent.getStringExtra("value");
        }




        volley = VolleySingleton.getInstance(getApplicationContext());
        fRequestQueue = volley.getRequestQueue();
        activityVerify = this;
        session = new ShPref(activityVerify);
        session.setVerifying(false);
        mVerifyFormView = findViewById(R.id.nestedScrollForm);
        mProgressView = findViewById(R.id.verifiy_progress);

        VerifyFragment verifyFragment = (VerifyFragment)
                getSupportFragmentManager().findFragmentById(R.id.nestedScrollForm);

        if (verifyFragment == null) {
            verifyFragment = VerifyFragment.newInstance();

            Bundle arguments = new Bundle();
            arguments.putString("origin",origin);

            if(origin.equals("createActivity")){
                arguments.putString("nombre", nombre);
                arguments.putString("apellido", apellido);
                arguments.putString("correo", correo);
                arguments.putString("password", password);

            }else{
                arguments.putString("id", id);
                arguments.putString("column", column);
                arguments.putString("value", value);
            }

            verifyFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.nestedScrollForm, verifyFragment)
                    .commit();
        }

    }

    public void setupActionBar(boolean estatus) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(estatus);
        }
    }



}

