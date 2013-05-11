package com.example.servidorecliente.rede;

import com.example.servidorecliente.Conexao;

public interface DepoisDeReceberDados {

	void execute(Conexao origem, String linha);

}
