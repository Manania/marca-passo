package com.marcacorrida.model;


import java.util.Date;

public class Corrida {
	private String nome;
	private int numPassos;
	private long duracao;
	private Date data; //TODO: Substituir por Instant ou LocalDateTime

	/**
	 *
	 * @param nome
	 * @param numPassos
	 * @param duracao em milissegundos
	 * @param data milissegundos desde a epoch
	 */
	public Corrida(String nome, int numPassos, long duracao, Date data) {
		this.nome = nome;
		this.numPassos = numPassos;
		this.duracao = duracao;
		this.data = data;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getNumPassos() {
		return numPassos;
	}

	public void setNumPassos(int numPassos) {
		this.numPassos = numPassos;
	}

	public long getDuracao() {
		return duracao;
	}

	public void setDuracao(long duracao) {
		this.duracao = duracao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "{ " +
				"nome\"=\"" + nome + '\"' +
				", \"numPassos\"=" + numPassos +
				", \"duracao\"=" + duracao +
				", \"data\"=" + '\"' + data + '\"' +
				" }";
	}
}
