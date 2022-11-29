package com.marcacorrida.datasource;

import android.provider.BaseColumns;

public class DatabaseContract {
	private DatabaseContract() { }
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "marcapasso.db";

	public static class CorridaEntry implements BaseColumns {
		private CorridaEntry() { }
		public static final String TABLE_NAME = "Corrida";
		public static final String NOME = "nome";
		public static final String DURACAO = "duracao"; /**Em milissegundos*/
		public static final String NUM_PASSOS = "numero_passos";
		public static final String DATA = "data";
	}

	public static class MetaEntry implements  BaseColumns {
		private MetaEntry() { }
		public static final String TABLE_NAME = "Meta";
		public static final String NUM_PASSOS = "numero_passos";
		public static final String DATA = "data";
	}

}