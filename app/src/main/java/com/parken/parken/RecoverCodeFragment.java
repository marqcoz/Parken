package com.parken.parken;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class RecoverCodeFragment extends Fragment{

    Button wrong;
    EditText code;
    TextView seconds;
    Button verify;
    Button resend;



    public RecoverCodeFragment() {
        // Required empty public constructor
    }

    public static RecoverCodeFragment newInstance(/*parámetros*/) {
        RecoverCodeFragment fragment = new RecoverCodeFragment();
        // Setup parámetros
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Gets parámetros
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_recover_code, container, false);

        wrong = root.findViewById(R.id.btnWrongNumberRecover);
        code = root.findViewById(R.id.editTextCodigoRecover);
        seconds = root.findViewById(R.id.textViewSecondsRecover);
        verify = root.findViewById(R.id.btnVerificarCodeRecover);
        resend = root.findViewById(R.id.btnResendCodeRecover);


        return root;


    }

}
