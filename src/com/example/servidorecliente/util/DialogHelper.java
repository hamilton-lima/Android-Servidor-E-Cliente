package com.example.servidorecliente.util;

import java.net.UnknownHostException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class DialogHelper {

	/*
	 * exibe mensagem com botao de OK
	 */
	public static void message(Context context, String message) {

		AlertDialog.Builder b = new AlertDialog.Builder(context);
		AlertDialog dialog = b.create();
		dialog.setMessage(message);

		dialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		dialog.show();
	}

	public static void error(Context context, String message, String tag,
			Exception e) {

		Log.e(tag, message, e);
		message(context, message);
	}
}
