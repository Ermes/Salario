package com.ermes.salario;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SalarioActivity extends Activity implements OnClickListener, OnKeyListener {

    EditText anual;
    TextView IRPF, netoMensual, pagaExtra, baseIRPF, deduccionFamiliar, deduccionSS, IRPFanual, lPagaExtra;
    Button   calcular;
    TextView np;

    public void accion() {
        // oculto el teclado
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        int n = Integer.parseInt(preferencias.getString("prefPagas", "12"));
        double a = Double.parseDouble(anual.getText().toString());
        int totH = Integer.parseInt(preferencias.getString("hijos1", "0"));
        int max = Integer.parseInt(preferencias.getString("hijos2", "0"));
        int min = Integer.parseInt(preferencias.getString("hijos3", "0"));
        int cat = Integer.parseInt(preferencias.getString("prefCategoria", "4"));
        int lab = Integer.parseInt(preferencias.getString("sitLaboral", "1"));
        // int n = Integer.parseInt(np.getSelectedItem().toString());
        if (totH >= min) {

            pagas miSueldo = new pagas(a, n, totH, max, min, cat, lab);

            IRPF.setText("IRPF : " + miSueldo.getIRPF() + "%");
            deduccionSS.setText(miSueldo.deduccionSS() + " €");
            baseIRPF.setText(miSueldo.BASE + " €");
            deduccionFamiliar.setText(miSueldo.deduccionFamiliar() + " €");
            IRPFanual.setText(miSueldo.aPagarIRPF() + " €");
            netoMensual.setText(miSueldo.netoMensual() + " €");
            if (n > 12) {
                lPagaExtra.setVisibility(View.VISIBLE);
                pagaExtra.setText(miSueldo.pagaExtra() + " €");
            } else {
                lPagaExtra.setVisibility(View.INVISIBLE);
                pagaExtra.setText("");
            }
        } else {

            Toast.makeText(this,
                    "Revisa las preferencias: el numero total de hijos no puede ser inferior a la suma de las otros",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void onClick(View v) {
        if (anual.getText().toString().equals("")) {
            Toast.makeText(this, "Debes rellenar el salario bruto anual", Toast.LENGTH_SHORT).show();
        } else {
            accion();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        anual = (EditText) findViewById(R.id.txtAnual);

        np = (TextView) findViewById(R.id.txtNPagas);
        IRPF = (TextView) findViewById(R.id.txtIRPF);
        netoMensual = (TextView) findViewById(R.id.txtNetoMensual);
        pagaExtra = (TextView) findViewById(R.id.txtPagaExtra);
        lPagaExtra = (TextView) findViewById(R.id.lblPagaExtra);
        lPagaExtra.setVisibility(View.INVISIBLE);
        baseIRPF = (TextView) findViewById(R.id.txtBaseIRPF);
        deduccionSS = (TextView) findViewById(R.id.txtDeduccionSS);
        deduccionFamiliar = (TextView) findViewById(R.id.txtDeduccionFamiliar);
        IRPFanual = (TextView) findViewById(R.id.txtIRPFanual);
        calcular = (Button) findViewById(R.id.btnCalcular);
        IRPF.setText("IRPF :");

        np.setText(Integer.parseInt(preferencias.getString("prefPagas", "12")) + " pagas");

        // anual.setText(preferencias.getString("pBruto", BRUTO_DEF));
        calcular.setOnClickListener(this);
        anual.setOnKeyListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            accion();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.acerca:
                startActivity(new Intent("com.ermes.salario.ACERCA"));
                return true;
            case R.id.ajustes:
                startActivity(new Intent("com.ermes.salario.AJUSTES"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        np.setText(Integer.parseInt(preferencias.getString("prefPagas", "12")) + " pagas");

    }
}