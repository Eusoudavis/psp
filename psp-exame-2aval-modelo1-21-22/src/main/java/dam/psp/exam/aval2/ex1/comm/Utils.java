package dam.psp.exam.aval2.ex1.comm;

import java.util.Random;

/**
 * Clase que contén unha serie de utilidades e recursos xerais usados nos dous modelos de exame
 * 
 * @author manuel
 *
 */
public final class Utils {

	/**
	 * Enumerado usado para dar formato aos datos que sacamos por consola e aos ficheiros de logs.
	 * 
	 * @author manuel
	 *
	 */
	public static enum Format {
		/**
		 * Prefixo xenérico para amosar a información
		 */
		PREFIX_1("[*] "),

		/**
		 * Prefixo usado para amosar a información enviada
		 */
		PREFIX_1_SEND(" ( ...=> ) "),

		/**
		 * Prefixo usado para amosar a información recibida
		 */
		PREFIX_1_RECEIVED(" ( <=... ) ");
		
		/**
		 * Valor do tipo de mensaxe enumerado
		 */
		private final String value;

		/**
		 * Devolve o valor para o tipo de formato
		 * 
		 * @return valor do tipo de formato
		 */		
		public String getValue() {
			return value;
		}

		/**
		 * Configura o valor do formato. Fíxate que este método faise privado para non poder modificalo
		 * 
		 * @param value valor asignado ao formato
		 */
		private Format(String value) {
			this.value = value;
		}
		
		/**
		 * Devolve o texto formatado co sufixo indicado
		 * 
		 * @param name nome que queremos formatar na cadea
		 * @param format tipo de format que imos engadir como sufixo
		 * @return mensaxe formatado
		 */
		public static String getFormattedMessage(String name, Format format) {
			return "[" + name + "] " + format.getValue(); 
		}
	}

	/**
	 * Obtén un número enteiro entre un valor máximo e mínimo permitidos
	 * 
	 * @param min Número mínimo permitido
	 * @param max Número máximo permitido
	 * @return número aleatorio xerado
	 */
	public static int getRandomNumberInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	/**
	 * Conta o número total de palabras dunha cadea facendo uso do método split
	 * 
	 * @param input Texto de entrada para o que imos contar as palabras	 * 
	 * @return número enteiro co total de palabras contadas
	 */
	public static int countWordsUsingSplit(String input) {
		if (input == null || input.isEmpty()) {
			return 0;
		}
		String[] words = input.split("\\s+");
		return words.length;
	}
}