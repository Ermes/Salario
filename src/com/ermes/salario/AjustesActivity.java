package com.ermes.salario;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.ListPreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;

public class AjustesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.ajustes);
		estableceSummary(getPreferenceScreen());
	}
	
	private void estableceSummary(PreferenceScreen pScreen) {
		String sum = "";
		for ( int i=0; i < pScreen.getPreferenceCount(); i++) {
			Preference pref = pScreen.getPreference(i);
			if (pref instanceof ListPreference) {
				ListPreference listPref = (ListPreference) pref;
				sum =  listPref.getEntry()!= null ? (pref.getSummary() + ": " + listPref.getEntry()) : (pref.getSummary() + ": ");
				pref.setSummary(sum);
			}
		}
	}
	
	//------------------------------------------------------
	// paro y arranco el listener cuando no se necesite 
	//------------------------------------------------------
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}
	//-------------------------------------------------------
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		actualizaPreferencias(key);
	}

	private void actualizaPreferencias(String key) {
		// TODO Auto-generated method stub
		// podria comprobar si la key es una u otra
		// if (key.equals("hijos1") ...
		String sum = "";
		Preference preferencia = findPreference(key);
		if (preferencia instanceof ListPreference) {
			ListPreference listPref = (ListPreference) preferencia;
			String [] enunciado = ((String) preferencia.getSummary()).split(":");
			sum = enunciado[0] + ": " + listPref.getEntry();
			preferencia.setSummary(sum);
			
		}

		
	}



}