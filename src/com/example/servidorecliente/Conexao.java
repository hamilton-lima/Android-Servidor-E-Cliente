package com.example.servidorecliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.example.servidorecliente.rede.DepoisDeReceberDados;

import android.util.Log;

public class Conexao implements Runnable {

	private static final String TAG = "conexao";
	private BufferedReader leitor;
	private BufferedWriter escritor;
	private String id;

	private Socket conexao;
	private boolean ativo = true;
	private Thread escutandoParaSempre;
	private ConcurrentLinkedQueue<String> linhas;
	private DepoisDeReceberDados depoisDeReceberDadosHandler;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * usado para criar objetos de conexao do lado servidor
	 * 
	 * @param conexao
	 * @param usuario
	 * @param depoisDeReceberDadosHandler
	 * @throws IOException
	 */
	public Conexao(Socket conexao, String id,
			DepoisDeReceberDados depoisDeReceberDadosHandler)
			throws IOException {

		this.conexao = conexao;
		this.depoisDeReceberDadosHandler = depoisDeReceberDadosHandler;
		this.id = id;

		leitor = new BufferedReader(new InputStreamReader(
				conexao.getInputStream()));
		escritor = new BufferedWriter(new OutputStreamWriter(
				conexao.getOutputStream()));

		linhas = new ConcurrentLinkedQueue<String>();
		escutandoParaSempre = new Thread(this);
		escutandoParaSempre.setPriority(Thread.MIN_PRIORITY);
		escutandoParaSempre.start();
	}

	public String getIP() {
		if (conexao != null) {
			return conexao.getInetAddress().getHostAddress().toString();
		}
		return null;
	}

	/**
	 * le continuamente da conexao
	 */
	public void run() {
		Log.i(TAG,"conexao esperando por dados : " + id);
		
		while (ativo) {
			try {
				// bloqueante !!
				String linha = leitor.readLine();

				// para cada linha nao nula chama o respectivo handler
				if (linha != null) {
					Log.i(TAG, "linha recebida: " + linha );
					if (depoisDeReceberDadosHandler != null) {
						depoisDeReceberDadosHandler.execute(this, linha);
					}
				}
			} catch (IOException e) {
				Log.e(TAG, "erro lendo da conexao");
			}
		}

	}

	public void adeus() {
		if (conexao != null) {
			try {
				conexao.close();
			} catch (IOException e) {
				Log.e(TAG, "erro ao fechar conexao", e);
			}
		}

		ativo = false;
		try {
			Thread.sleep(Const.ESPERA_CONEXAO_MORRER);
			escutandoParaSempre.interrupt();
		} catch (InterruptedException e) {
			Log.e(TAG, "erro interrompendo conexao", e);
		}

		Log.i(TAG, "adeus() -- conexao CLIENTE encerrada.");
	}

	public void write(String string) {
		Log.i(TAG, "cliente.write:" + string);
		try {
			escritor.write(string + "\n");
			escritor.flush();
		} catch (IOException e) {
			Log.e(Const.TAG, "erro escrevendo na conexao");
		}
	}

}
