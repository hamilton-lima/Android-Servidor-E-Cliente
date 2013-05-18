package com.example.servidorecliente.rede;

import android.util.Log;

import com.example.servidorecliente.Conexao;
import com.example.servidorecliente.ElMatador;
import com.example.servidorecliente.MainActivity;

public class DadosDoCliente implements Runnable, Killable {

	private Conexao cliente;
	private int updateTime;

	private int x;
	private int y;
	private boolean ativo = true;

	public DadosDoCliente(Conexao cliente, int updateTime) {
		this.cliente = cliente;
		this.updateTime = updateTime;
		ElMatador.getInstance().add(this);
	}

	public void run() {
		while (ativo) {
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

	public void killMeSoftly() {
		ativo = false;
	}

}
