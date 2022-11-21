package com.marcacorrida.model;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

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
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append("nome\"=");
		builder.append('\"');
		builder.append(nome);
		builder.append('\"');
		builder.append(", \"numPassos\"=");
		builder.append(numPassos);
		builder.append(", \"duracao\"=");
		builder.append(duracao);
		builder.append(", \"data\"=");
		builder.append('\"');
		builder.append(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(
						ZonedDateTime.ofInstant( data.toInstant(), ZoneId.systemDefault() )));
		builder.append('\"');
		builder.append(" }");
		return builder.toString();
	}
}
