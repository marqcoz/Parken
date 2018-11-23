package com.parken.parken;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;

public class SancionPagoActivity extends AppCompatActivity {

    private ConstraintLayout ep;
    private ConstraintLayout zp;
    private ConstraintLayout vehiculo;
    private ConstraintLayout monto;
    private ConstraintLayout fecha;
    private ConstraintLayout location;

    private TextView txtEspacioParken;
    private TextView txtZonaParken;
    private TextView textVehiculo;
    private TextView txtMonto;
    private TextView txtFecha;
    private TextView txtUbicacion;

    private Button pay;
    private Button cancel;

    private String tiempo;
    private String placaVehiculo;
    private String modeloVehiculo;
    private String zonaParken;
    private String ubicacion;
    private String origin;

    private int idSancion;
    private int espacioParken;

    private float montoSancion;

    private String nombreAutomovilista;
    private String apellidoAutomovilista;
    private String emailAutomovilista;
    private String celAutomovilista;

    ShPref session;

    SancionActivity activitySancion;
    SesionActivity activitySesion;

    private VolleySingleton volley;
    protected RequestQueue fRequestQueue;
    public JsonObjectRequest jsArrayRequest;

    private static final String PAYPAL_CLIENT = "Ae5YnMlrCaIo7Gl622Cvyb7r8eOEhQweVjxuvvxxVhJCbfZgtH4LLDOshoDQe_LO2iClRoMQID_YcOwW";
    private static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PAYPAL_CLIENT);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sancion_pago);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        session = new ShPref(this);

        activitySancion = new SancionActivity();
        activitySesion = new SesionActivity();

        volley = VolleySingleton.getInstance(getApplicationContext());
        fRequestQueue = volley.getRequestQueue();


        ep = findViewById(R.id.constraintEP);
        zp = findViewById(R.id.constraintZP);
        vehiculo = findViewById(R.id.constraintVehiculo);
        monto = findViewById(R.id.constraintMonto);
        fecha = findViewById(R.id.constraintFecha);
        location = findViewById(R.id.constraintUbicacion);

        txtEspacioParken = findViewById(R.id.textViewEspacioParken);
        txtZonaParken = findViewById(R.id.textViewZonaParken);
        txtUbicacion = findViewById(R.id.textViewLocation);
        textVehiculo = findViewById(R.id.textViewCar);
        txtMonto = findViewById(R.id.textViewAmount);
        txtFecha = findViewById(R.id.textViewDate);

        pay = findViewById(R.id.btnPayPal);
        cancel = findViewById(R.id.btnCancelar);


        Intent intent = getIntent();
        if (null != intent) {
            espacioParken = intent.getIntExtra("espacioParken", 0);
            idSancion = intent.getIntExtra("idSancion", 0);
            zonaParken = intent.getStringExtra("zonaParken");
            ubicacion = intent.getStringExtra("ubicacion");
            modeloVehiculo = intent.getStringExtra("modeloVehiculo");
            placaVehiculo = intent.getStringExtra("placaVehiculo");
            tiempo = intent.getStringExtra("tiempo");
            montoSancion = intent.getFloatExtra("monto",0f);
            origin = intent.getStringExtra("origin");
        }

        nombreAutomovilista = session.infoNombre();
        apellidoAutomovilista = session.infoApellido();
        emailAutomovilista = session.infoEmail();
        celAutomovilista = session.infoCelular();

        txtEspacioParken.setText(String.valueOf(espacioParken));
        txtZonaParken.setText(zonaParken);
        txtUbicacion.setText(ubicacion);
        txtFecha.setText(tiempo + " hrs");
        String carro = modeloVehiculo + " - " + placaVehiculo;
        textVehiculo.setText(carro);
        String precio = "$ " + String.valueOf(montoSancion) + "0 MXN";
        txtMonto.setText(precio);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                processPayment();
                //dialogConfirmPayTicket().show();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Solo cerramos el activity
                finish();

            }
        });

    }

    private void processPayment() {

        PayPalPayment paypalPayment = new PayPalPayment(new BigDecimal(montoSancion),
                SesionParkenActivity.CURRENCY, "Parken Test", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, paypalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PAYPAL_REQUEST_CODE) {
            if(resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation != null){
                    try {

                        String paymentDetails = confirmation.toJSONObject().toString(4);

                        /*
                        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Pago exitoso", Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                        snackbar.show();
                        */
                        payReceiptSuccesful(idSancion);
                        //Intent intent = new Intent(SancionPagoActivity.this, ParkenActivity.class);
                        //startActivity(intent);
                        //dialogPaySuccessful1().show();



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Error al procesar el pago", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                snackbar.show();
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Pago no válido", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
            snackbar.show();
        }
    }

    private void payReceiptSuccesful(int idSancion) throws JSONException {


            HashMap<String, String> parametros = new HashMap();
            parametros.put("idSancion", String.valueOf(idSancion));

            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    Jeison.URL_DRIVER_UPDATE_TICKET,
                    new JSONObject(parametros),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try{
                                if(response.getString("success").equals("1")){

                                    Log.d("PagarSancion", response.toString());
                                    //showProgress(false);
                                    if(origin.equals("SancionActivity")){
                                        //activitySancion.adapterSancion.notifyDataSetChanged();
                                        dialogPaySuccessful().show();
                                    }
                                    if(origin.equals("SesionActivity")){
                                        //activitySesion.adapterSesion.notifyDataSetChanged();
                                        dialogPaySuccessful().show();
                                    }
                                    if(origin.equals("Parken")){

                                        Intent dialogIntent = new Intent(getApplicationContext(), ParkenActivity.class);
                                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        dialogIntent.putExtra("Activity", ParkenActivity.ACTIVITY_PAY_RECEIPT);
                                        startActivity(dialogIntent);
                                        finish();

                                    }

                                }else{
                                    //showProgress(false);
                                    Log.d("PagarSancion", response.toString());
                                    dialogFailed().show();
                                    //Mostrar dialog

                                }

                                return;

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //showProgress(false);
                                dialogFailed().show();
                                return;
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //showProgress(false);
                            Log.d("PagarSancion", "Error Respuesta en JSON: " + error.getMessage());
                            dialogFailed().show();
                            return;

                        }
                    });

            fRequestQueue.add(jsArrayRequest);
        }
        //Update la tabla sancion
        //Crear un registro en la tabla reportes
        //Actualizar la sesión parken como finalizada
        //Actualizar el estatus del vehiculo

        public AlertDialog dialogFailed() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Error")
                    .setMessage("No se puede realizar la conexión con el servidor. Intenta de nuevo.")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
            return builder.create();
        }

    public AlertDialog dialogPaySuccessful() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Pago exitoso")
                .setMessage("En unos minutos un supervisor retirará el inmovilizador de tu vehículo.")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })

                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if(origin.equals("SancionActivity")){
                            //activitySancion.adapterSancion.notifyDataSetChanged();
                            startActivity(new Intent(getApplicationContext(), SancionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                        }
                        if(origin.equals("SesionActivity")){
                            //activitySesion.adapterSesion.notifyDataSetChanged();
                            startActivity(new Intent(getApplicationContext(), SesionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                        }

                        finish();

                    }
                });

        return builder.create();
    }

    public AlertDialog dialogPaySuccessful1() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Pago exitoso")
                .setMessage("Transacción correcta")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

        return builder.create();
    }

}
