package com.example.servidorecliente.rede;

import android.util.Log;

import com.example.servidorecliente.Conexao;
import com.example.servidorecliente.MainActivity;

public class DadosDoCliente implements Runnable {

	private Conexao cliente;
	private int updateTime;

	private int x;
	private int y;

	public DadosDoCliente(Conexao cliente, int updateTime) {
		this.cliente = cliente;
		this.updateTime = updateTime;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(updateTime);
			} catch (InterruptedException e) {
				Log.e(MainActivity.TAG, "interrupcao do run()");
			}

			cliente.write(Protocolo.PROTOCOL_MOVE + "," + x + "," + y);

		}

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
