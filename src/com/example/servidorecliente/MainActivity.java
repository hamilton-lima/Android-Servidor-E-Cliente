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

import com.example.servidorecliente.rede.ControleDeUsuariosCliente;
import com.example.servidorecliente.rede.DepoisDeReceberDados;
import com.example.servidorecliente.rede.TratadorDeRedeECO;
import com.example.servidorecliente.util.DialogHelper;
import com.example.servidorecliente.util.RedeUtil;

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

		editIP = (EditText) findViewById(R.id.editText1);
		editUsuario = (EditText) findViewById(R.id.editText2);
	}

	public void criarServidor(View sender) {

		try {

			if (gerente != null) {
				gerente.adeus();
			}

			gerente = new GerenteDEConexao(PORTA_PADRAO);
			gerente.iniciarServidor(new TratadorDeRedeECO());

			DepoisDeReceberDados tratadorDeDadosDoCliente = new ControleDeUsuariosCliente();

			Socket s = new Socket("127.0.0.1", gerente.getPorta());
			conexao = new Conexao(s, usuario, tratadorDeDadosDoCliente);

			DialogHelper.message(this,
					"endereco do servidor : " + RedeUtil.getLocalIpAddress());

			// garante que view possa recuperar a lista de usuarios atual e
			// enviar dados pela rede
			viewDoJogo = new ViewDeRede(this, conexao,
					(ControleDeUsuariosCliente) tratadorDeDadosDoCliente);

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
				DepoisDeReceberDados tratadorDeDadosDoCliente = new ControleDeUsuariosCliente();

				Socket s = new Socket(ip, PORTA_PADRAO);
				conexao = new Conexao(s, usuario, tratadorDeDadosDoCliente);

				// garante que view possa recuperar a lista de usuarios atual e
				// enviar dados pela rede
				viewDoJogo = new ViewDeRede(this, conexao,
						(ControleDeUsuariosCliente) tratadorDeDadosDoCliente);

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
