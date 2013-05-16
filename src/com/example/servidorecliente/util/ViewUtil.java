package com.example.servidorecliente.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class ViewUtil {

	/**
	 * fecha teclado 
	 */
	public static void closeKeyboard(Activity activity) {

		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(activity.getWindow().getCurrentFocus()
				.getWindowToken(), 0);

	}

}
