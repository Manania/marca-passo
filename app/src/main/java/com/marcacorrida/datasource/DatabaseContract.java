package com.marcacorrida.datasource;

import android.provider.BaseColumns;

public class DatabaseContract {
	private DatabaseContract() { }
	public static final String DATABASE_NAME = "passometro.db";

	public static class CorridaEntry implements BaseColumns {
		private CorridaEntry() { }
		public static final String TABLE_NAME = "Corrida";
		public static final String NOME = "nome";
		public static final String DURACAO = "duracao"; /**Em milissegundos*/
		public static final String NUM_PASSOS = "numero_passos";
		public static final String DATA = "data";
	}
}