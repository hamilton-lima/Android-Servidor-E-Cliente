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
import com.example.servidorecliente.rede.Protocolo;

public class ViewDeRede extends View implements Runnable {
	private Paint paint;
	private long time = 30;

	private Conexao cliente;
	private Context context;

	private ControleDeUsuariosCliente tratadorDeDadosDoCliente;
	private int y;
	private int x;
	private float raio = 20;
	private int margem = 5;

	public ViewDeRede(Context context, Conexao cliente,
			ControleDeUsuariosCliente tratadorDeDadosDoCliente) {

		super(context);

		this.cliente = cliente;
		this.context = context;
		this.tratadorDeDadosDoCliente = tratadorDeDadosDoCliente;

		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(20);

		cliente.write(Protocolo.PROTOCOL_ID + "," + cliente.getId() + "," + x
				+ "," + y);

		Thread thread = new Thread(this);
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
					- raio, jogador.getY() + raio + margem  , paint);
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

		if (action == MotionEvent.ACTION_DOWN) {
			int id = event.getPointerId(event.getActionIndex());
			x = (int) event.getX(id);
			y = (int) event.getY(id);
			cliente.write(Protocolo.PROTOCOL_MOVE + "," + x + "," + y);
		}
		return super.onTouchEvent(event);
	}

}
