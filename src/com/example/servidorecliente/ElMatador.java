package com.example.servidorecliente;

import java.util.Stack;

import android.util.Log;

import com.example.servidorecliente.rede.Killable;

public class ElMatador {

	private static final String TAG = "elmatador";
	private static ElMatador instance;
	private Stack<Killable> targets;

	private ElMatador() {
		targets = new Stack<Killable>();
	}

	public static ElMatador getInstance() {
		if (instance == null) {
			instance = new ElMatador();
		}
		return instance;
	}

	/**
	 * adiciona objeto para eliminacao futura
	 * 
	 * @param target
	 */
	public void add(Killable target) {
		Log.i(TAG, "---- adicionado alvo para eliminar no fim : "
				+ targetAsString(target));
		targets.add(target);
	}

	/**
	 * cria representacao como string do objeto para fins de log
	 * 
	 * @param target
	 * @return
	 */
	private String targetAsString(Killable target) {
		if (target != null) {
			return target.getClass().getCanonicalName();
		}

		return null;
	}

	/**
	 * chama metodo de remover recursos em uso de cada objeto da lista
	 */
	public void killThenAll() {
		Log.i(TAG, "START kill the all");

		while (!targets.empty()) {
			Killable target = targets.pop();
			kill(target, targetAsString(target));
		}

		Log.i(TAG, "END kill the all");
	}

	/**
	 * elimina um objeto
	 * 
	 * @param target
	 * @param id
	 */
	private void kill(Killable target, String id) {
		Log.i(TAG, "eliminando processo : " + id);

		try {
			target.killMeSoftly();
		} catch (Throwable t) {
			Log.e(TAG, "ERRO eliminando processo : " + id);
		}
	}

}
