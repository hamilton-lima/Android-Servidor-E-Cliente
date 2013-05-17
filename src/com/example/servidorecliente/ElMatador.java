package com.example.servidorecliente;

import java.util.ArrayList;

public class ElMatador {

	private static ElMatador instance;
	private ArrayList<Thread> threads2Kill;

	private ElMatador() {
		threads2Kill = new ArrayList<Thread>();
	}

	public static ElMatador getInstance() {
		if (instance == null) {
			instance = new ElMatador();
		}
		return instance;
	}

	public Thread newThread(Runnable runner) {
		Thread t = new Thread(runner);
		t.setPriority(Thread.MIN_PRIORITY);
		threads2Kill.add(t);
		return t;
	}

	public void killThenAll() {
		int n = 0;
		while (threads2Kill.size() < n) {
			threads2Kill.get(n).interrupt();
			n++;
		}
	}

}
