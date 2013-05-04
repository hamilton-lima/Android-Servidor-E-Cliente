package com.example.servidorecliente;

import android.util.Log;

public class TratadorDeRedeECO implements DepoisDeReceberDados {

	@Override
	public void execute(Conexao origem, String linha) {

		Log.i(Const.TAG, "<<" + linha);
		if (linha != null) {
			origem.write("eco : " + linha);
		}

	}

}
