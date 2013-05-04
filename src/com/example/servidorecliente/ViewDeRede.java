package com.example.servidorecliente;

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
	private Context context;

	public ViewDeRede(Context context, GerenteDEConexao gerente) {
		super(context);

		this.gerente = gerente;
		this.context = context;

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

	}

	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();

		if (action == MotionEvent.ACTION_DOWN) {


		}
		return super.onTouchEvent(event);
	}

}
