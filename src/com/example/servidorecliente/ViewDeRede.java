package com.example.servidorecliente;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.servidorecliente.bean.Jogador;
import com.example.servidorecliente.rede.ControleDeUsuariosCliente;
import com.example.servidorecliente.rede.DadosDoCliente;
import com.example.servidorecliente.rede.Protocolo;

public class ViewDeRede extends View implements Runnable {
	private static final String TAG = "view-rede";
	private static final int UPDATE_TIME = 100;
	private Paint paint;
	private long time = 30;

	private ControleDeUsuariosCliente tratadorDeDadosDoCliente;
	private DadosDoCliente dadosDoCliente;

	private float raio = 20;
	private int margem = 5;
	private int fontSize = 20;

	public ViewDeRede(Context context, Conexao cliente,
			ControleDeUsuariosCliente tratadorDeDadosDoCliente) {

		super(context);

		// envia estado atual do cliente para o servidor
		dadosDoCliente = new DadosDoCliente(cliente, UPDATE_TIME);
		Thread threadDados = ElMatador.getInstance().newThread(dadosDoCliente);
		threadDados.start();

		this.tratadorDeDadosDoCliente = tratadorDeDadosDoCliente;

		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(fontSize);

		cliente.write(Protocolo.PROTOCOL_ID + "," + cliente.getId() + ",0,0");

		setFocusableInTouchMode(true);
		setClickable(true);
		setLongClickable(true);

		Thread thread = ElMatador.getInstance().newThread(this);
		thread.start();
	}

	public void draw(Canvas canvas) {
		super.draw(canvas);

		ConcurrentHashMap<String, Jogador> jogadores = tratadorDeDadosDoCliente
				.getJogadores();

		Iterator<String> iterator = jogadores.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Jogador jogador = jogadores.get(key);

			canvas.drawCircle(jogador.getX(), jogador.getY(), raio, paint);
			canvas.drawText("<" + jogador.getNome() + ">", jogador.getX()
					- raio, jogador.getY() + raio + margem + fontSize, paint);
		}

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

	}

	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		Log.i(TAG, "ontouch: " + action);

		int id = event.getPointerId(event.getActionIndex());
		dadosDoCliente.setX((int) event.getX(id));
		dadosDoCliente.setY((int) event.getY(id));

		return super.onTouchEvent(event);
	}

}
