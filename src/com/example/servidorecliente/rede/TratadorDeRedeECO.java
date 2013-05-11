package com.example.servidorecliente.rede;

import com.example.servidorecliente.Conexao;
import com.example.servidorecliente.Const;

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
