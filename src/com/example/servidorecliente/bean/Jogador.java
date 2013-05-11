package com.example.servidorecliente.bean;

public class Jogador {
	private String nome;
	private int x;
	private int y;

	public Jogador(String nome, int x, int y) {
		this.nome = nome;
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return "Jogador [nome=" + nome + ", x=" + x + ", y=" + y + "]";
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String toStringCSV() {
		return nome + "," + x + "," + y + ";";
	}

}
