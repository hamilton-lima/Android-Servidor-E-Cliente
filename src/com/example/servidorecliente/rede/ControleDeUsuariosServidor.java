package com.example.servidorecliente.rede;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import android.util.Log;

import com.example.servidorecliente.Conexao;
import com.example.servidorecliente.Const;
import com.example.servidorecliente.bean.Jogador;

public class ControleDeUsuariosServidor implements DepoisDeReceberDados {

	// notar que este tipo de hashmap eh sincronizado
	// suportando acessos de multiplos threads
	private ConcurrentHashMap<String, Jogador> jogadores;

	public ControleDeUsuariosServidor() {
		jogadores = new ConcurrentHashMap<String, Jogador>();
	}

	// comandos possiveis dos clientes
	// ID,nome do usuario,x,y
	// MOVE,x,y

	public void execute(Conexao origem, String linha) {

		Log.i(Const.TAG, "<<" + linha);

		if (linha.startsWith(Protocolo.PROTOCOL_ID)) {
			adicionaNovoUsuario(origem, linha);
		}

		if (linha.startsWith(Protocolo.PROTOCOL_MOVE)) {
			moveUsuario(origem, linha);
		}

		informaTodosUsuarios(origem);
	}

	private void informaTodosUsuarios(Conexao origem) {

		StringBuffer buffer = new StringBuffer();
		Iterator iterator = jogadores.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			Jogador jogador = jogadores.get(key);
			buffer.append(jogador.toStringCSV());
		}

		origem.write(buffer.toString());
	}

	private void moveUsuario(Conexao origem, String linha) {
		String[] array = linha.split(",");
		int x = Integer.parseInt(array[1]);
		int y = Integer.parseInt(array[2]);

		Jogador jogador = jogadores.get(origem.getId());
		jogador.setX(x);
		jogador.setY(y);
	}

	private void adicionaNovoUsuario(Conexao origem, String linha) {
		String[] array = linha.split(",");
		String nome = array[1];
		int x = Integer.parseInt(array[2]);
		int y = Integer.parseInt(array[3]);

		origem.setId(nome);
		Jogador jogador = new Jogador(nome, x, y);
		jogadores.put(nome, jogador);
	}
}
