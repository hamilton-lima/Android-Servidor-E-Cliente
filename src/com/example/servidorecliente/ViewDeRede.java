package com.example.servidorecliente;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ViewDeRede extends View implements Runnable {
	private Paint paint;
	private int w;
	private int h;
	private long time = 30;
	private Conexao cliente;

	private GerenteDEConexao gerente;

	public ViewDeRede(Context context, GerenteDEConexao gerente) {
		super(context);

		this.gerente = gerente;

		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(20);

		Thread thread = new Thread(this);
		thread.start();
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		this.w = getWidth();
		this.h = getHeight();
	}

	public void draw(Canvas canvas) {
		super.draw(canvas);
		canvas.drawText("conexoes : " + gerente.getConexoes().size(), 10, 30,
				paint);
	}

	public void run() {

		while (true) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				Log.e(MainActivity.TAG, "interrupcao do run()");
			}
			update();
			postInvalidate();
		}

	}

	private void update() {
		Log.i(MainActivity.TAG, "lendo textos");
		ArrayList<Conexao> conexoes = gerente.getConexoes();
		for (Conexao conexao : conexoes) {
			String texto = conexao.eco();
			Log.i(MainActivity.TAG, conexao.getIP() + " << " + texto);
		}

	}

	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {

			gerente.iniciar();
			Log.i(MainActivity.TAG, "iniciado servidor de jogo");

			try {
				Socket s = new Socket("127.0.0.1", gerente.getPorta());
				cliente = new Conexao(s);
				gerente.setAutoConexao(cliente);
				
				Log.i(MainActivity.TAG, "primeiro cliente conectado");

			} catch (UnknownHostException e) {
				Log.e(MainActivity.TAG, "endereco de servidor invalido", e);

			} catch (IOException e) {
				Log.e(MainActivity.TAG, "erro ao comunicar com servidor", e);
			}

		}
		return super.onTouchEvent(event);
	}

}
