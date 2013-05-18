package com.example.servidorecliente;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.servidorecliente.rede.ControleDeUsuariosCliente;
import com.example.servidorecliente.rede.ControleDeUsuariosServidor;
import com.example.servidorecliente.rede.DepoisDeReceberDados;
import com.example.servidorecliente.rede.Killable;
import com.example.servidorecliente.util.DialogHelper;
import com.example.servidorecliente.util.RedeUtil;
import com.example.servidorecliente.util.ViewUtil;

public class MainActivity extends Activity implements Killable {

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

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	public void criarServidor(View sender) {

		try {
			ViewUtil.closeKeyboard(this);

			if (gerente != null) {
				gerente.killMeSoftly();
			}

			gerente = new GerenteDEConexao(PORTA_PADRAO);

			// gerente.iniciarServidor(new TratadorDeRedeECO());
			gerente.iniciarServidor(new ControleDeUsuariosServidor());

			DepoisDeReceberDados tratadorDeDadosDoCliente = new ControleDeUsuariosCliente();

			Socket s = new Socket("127.0.0.1", PORTA_PADRAO);
			conexao = new Conexao(s, usuario, tratadorDeDadosDoCliente);

			String serverIp = RedeUtil.getLocalIpAddress();
			DialogHelper.message(this, "endereco do servidor : " + serverIp);
			setTitle("servidor : " + serverIp);

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
		ViewUtil.closeKeyboard(this);
		usuario = editUsuario.getText().toString();
		Log.i(TAG, "usuario salvo:" + usuario);
	}

	public void conectar(View sender) {

		String ip = editIP.getText().toString();

		if (ip.trim().length() == 0) {
			DialogHelper.message(this,
					"endereço do servidor não pode ser vazio");

		} else {
			ViewUtil.closeKeyboard(this);

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

	/**
	 * @see http 
	 *      ://stackoverflow.com/questions/2257963/how-to-show-a-dialog-to-confirm
	 *      -that-the-user-wishes-to-exit-an-android-activity
	 */
	public void onBackPressed() {
		Log.i(TAG,"--------- back pressed");

		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("abandonando o barco")
				.setMessage(
						"Tem certeza que vai embora ? vou sentir sua falta ...")
				.setPositiveButton("Adeus",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								killMeSoftly();
							}

						}).setNegativeButton("Então tá, fico + um pouco", null)
				.show();
	}

	/**
	 * realiza limpeza dos threads em funcionamento
	 */
	public void killMeSoftly() {
		ElMatador.getInstance().killThenAll();
		finish();
	}

}
