package com.example.servidorecliente;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	public static final String TAG = "rede";
	private static final int PORTA_PADRAO = 2121;
	private GerenteDEConexao gerente;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gerente = new GerenteDEConexao(PORTA_PADRAO);
		
		setContentView(new ViewDeRede(this, gerente));
	}

	@Override
	protected void onDestroy() {
		gerente.adeus();
		super.onDestroy();
	}
	

}
