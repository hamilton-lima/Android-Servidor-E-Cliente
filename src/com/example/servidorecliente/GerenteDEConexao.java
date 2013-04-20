package com.example.servidorecliente;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import android.util.Log;

public class GerenteDEConexao implements Runnable {

	private static final String TAG = "gerente de conexao";
	private Thread ouvinte;

	public GerenteDEConexao(int porta) {
		this.porta = porta;
		conexoes = new ArrayList<Conexao>();
	}

	private int porta;

	public int getPorta() {
		return porta;
	}

	private boolean ativo = true;
	private ServerSocket servidor;
	private ArrayList<Conexao> conexoes;
	private Conexao cliente;

	public ArrayList<Conexao> getConexoes() {
		return conexoes;
	}

	public void iniciar() {

		try {
			servidor = new ServerSocket(porta);
			Log.i(TAG, "--- endereco do servidor : " + getLocalIpAddress());

			ouvinte = new Thread(this);
			ouvinte.start();

		} catch (IOException e) {
			ativo = false;
			Log.e(TAG, "erro ao iniciar servidor", e);
		}

	}

	private String getLocalIpAddress() {
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();

			while (en.hasMoreElements()) {
				NetworkInterface intf = en.nextElement();
				Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();

				while (enumIpAddr.hasMoreElements()) {

					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e(TAG, "erro recuperando IP atual", ex);
		}
		return null;
	}

	public void run() {
		while (ativo) {

			try {
				Socket conexao = servidor.accept();
				Log.i(TAG, "!! nova conexao : "
						+ conexao.getInetAddress().getHostAddress().toString());

				conexoes.add(new Conexao(conexao));

			} catch (IOException e) {
				Log.e(TAG, "erro ao aguardar nova conexao", e);
				ativo = false;
			}

		}
	}

	public void adeus() {
		if (servidor != null) {
			try {
				servidor.close();
			} catch (IOException e) {
				Log.e(TAG, "erro ao fechar conexao", e);
			}
		}

		// interrompe auto conexao
		cliente.adeus();

		// interrompe conexoes dos clientes
		ArrayList<Conexao> conexoes = getConexoes();
		for (Conexao conexao : conexoes) {
			conexao.adeus();
		}

		ativo = false;
		try {
			Thread.sleep(200);
			ouvinte.interrupt();
		} catch (InterruptedException e) {
			Log.e(TAG, "erro interrompendo conexao", e);
		}

		Log.i(TAG, "adeus() -- conexao encerrada.");
	}

	public void setAutoConexao(Conexao cliente) {
		this.cliente = cliente;
	}

}
