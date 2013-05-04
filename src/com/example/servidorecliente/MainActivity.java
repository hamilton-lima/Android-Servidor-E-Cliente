package com.example.servidorecliente;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MainActivity extends Activity {

	public static final String TAG = "rede";
	private static final int PORTA_PADRAO = 2121;
	private GerenteDEConexao gerente;

	private EditText editUsuario;
	private EditText editIP;
	private String usuario;
	private ViewDeRede viewDoJogo;
	private Conexao conexao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		viewDoJogo = new ViewDeRede(this, gerente);
		editIP = (EditText) findViewById(R.id.editText1);
		editUsuario = (EditText) findViewById(R.id.editText2);
	}

	public void criarServidor(View sender) {

		try {

			if (gerente != null) {
				gerente.adeus();
			}

			gerente = new GerenteDEConexao(PORTA_PADRAO);
			gerente.iniciarServidor();
			Socket s = new Socket("127.0.0.1", gerente.getPorta());
			conexao = new Conexao(s);

			DialogHelper.message(this,
					"endereco do servidor : " + RedeUtil.getLocalIpAddress());

			setContentView(viewDoJogo);

		} catch (UnknownHostException e) {
			DialogHelper.error(this, "Erro ao conectar com o servidor",
					MainActivity.TAG, e);

		} catch (IOException e) {
			DialogHelper.error(this, "Erro ao comunicar com o servidor",
					MainActivity.TAG, e);
		}

	}

	public void salvarUsuario(View sender) {
		usuario = editUsuario.getText().toString();
	}

	public void conectar(View sender) {

		String ip = editIP.getText().toString();

		if (ip.trim().length() == 0) {
			DialogHelper.message(this,
					"endereço do servidor não pode ser vazio");

		} else {
			// fecha teclado
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
					.getWindowToken(), 0);

			try {
				Socket s = new Socket(ip, gerente.getPorta());
				conexao = new Conexao(s);
				setContentView(viewDoJogo);

			} catch (UnknownHostException e) {
				DialogHelper.error(this, "Erro ao conectar com o servidor",
						MainActivity.TAG, e);

			} catch (IOException e) {
				DialogHelper.error(this, "Erro ao comunicar com o servidor",
						MainActivity.TAG, e);
			}

		}

	}

	@Override
	protected void onDestroy() {

		if (gerente != null) {
			gerente.adeus();
		}

		if (conexao != null) {
			conexao.adeus();
		}

		super.onDestroy();
	}

}
