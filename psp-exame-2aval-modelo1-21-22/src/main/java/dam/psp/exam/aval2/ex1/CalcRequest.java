package dam.psp.exam.aval2.ex1;

import com.fathzer.soft.javaluator.DoubleEvaluator;

import dam.psp.exam.aval2.ex1.comm.SocketPlus;
import dam.psp.exam.aval2.ex1.comm.Utils;
import dam.psp.exam.aval2.ex1.comm.Utils.Format;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.net.Socket;

/**
 * Clase usada para manexar as diferentes peticións que procesa un servidor.
 * Trátase dunha clase que estende a fío dado que cada petición executarase no seu propio fío
 * para permitir que o servidor siga escoitando e respostando a outras peticións.
 *
 * Exercicio 5 Facer extender a clase a Thread
 * 
 * @author manuel
 *
 */
@Log4j2
public class CalcRequest extends Thread{

	/**
	 * Exercicio 2 Instanciación da close SocketPlus como atributo de clase
	 */
	private final SocketPlus socket;
	
	/**
	 * Tipo de prefixo, usado nas mensaxes enviadas desde o cliente.
	 */
	public static enum MessageType {
		/**
		 * Tipo de mesaxe de cálculo. Indica que se realizará un cálculo aritmético.
		 */
		CALC("CALC::"), 
		/**
		 * Tipo de mesaxe de petición do total. Indica que se solicitará que se envíe o número de cálculos procesados.
		 */
		GET_TOTAL("GET::TOTAL");

		/**
		 * Valor do tipo de mensaxe enumerado
		 */
		private final String value;

		/**
		 * Devolve o valor para o tipo de mensaxe
		 * 
		 * @return valor do tipo de mensaxe
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Configura o valor. Fíxate que este método faise privado para non poder modificalo
		 * 
		 * @param value valor asignado
		 */
		private MessageType(String value) {
			this.value = value;
		}
		
		/**
		 * Método que devolve un tipo de operador de xeito aleatorio coa seguinte porcentaxe:
		 *    - 70% de probabilidades de obter unha mensaxe de tipo 'MessageType.CALC'
		 *    - 30% de probabilidades de obter unha mensaxe de tipo 'MessageType.GET_TOTAL'
		 * 
		 * @return tipo de mensaxe xerado de xeito aleatorio
		 */
		public static MessageType getRandomMessageType() {
			int num = Utils.getRandomNumberInRange(1, 10);
			
			if (num <= 7) {
				return MessageType.CALC;
			}
			else {
				return MessageType.GET_TOTAL;
			}
		}
	}
	
	/**
	 * Constructor da clase
	 *
	 * Exericio 5 Modificar o construtor
	 *
	 * @param socket socket que contén a conexión
	 * 
	 */
	public CalcRequest(Socket socket) {
		this.socket = new SocketPlus(socket);
	}
	
	/**
	 * Devolve o corpo dunha mensaxe de cálculo para poder procesala coa libraría javaluator.
	 * Por exemplo, se recibimos "CALC::23+12-1", devolve ""23+12-1"
	 * 
	 * @param msg texto que imos a procesar
	 */
	private String getCalcMessage(String msg) {
		if (msg == null )
			return null;
		if (!msg.contains(MessageType.CALC.getValue()))
			return null;
		
		return msg.replaceFirst(MessageType.CALC.getValue(), "");
	}
	
	/**
	 * Método que se encarga de procesar unha petición
	 *
	 * Exercicio 5 Modificar todas as chamadas ao nome dos fíos
	 */
	public void processRequest() throws InterruptedException {
		log.info(Format.getFormattedMessage(Thread.currentThread().getName(), Format.PREFIX_1_RECEIVED) + "A continuación imos a ler a información recibida desde o cliente [OK]");

		// Lemos os datos do servidor
		String msg = socket.readData();
		log.info(Format.getFormattedMessage(Thread.currentThread().getName(), Format.PREFIX_1_RECEIVED) + "Acabamos de recibir a mensaxe: '" + msg + "' ... [OK]");
		
		if (msg.contains(MessageType.CALC.getValue())) {
			Double calcResult = processCalc(getCalcMessage(msg));
			if (calcResult == null) {
				log.error(Format.getFormattedMessage(Thread.currentThread().getName(), Format.PREFIX_1_SEND) + "ERRO coa mensaxe recibida: '" + msg + "' ... [ERRO]");
				this.socket.sendData("ERRO coa mensaxe recibida: '" + msg + "'");
			}
			else {
				log.info(Format.getFormattedMessage(Thread.currentThread().getName(), Format.PREFIX_1_SEND) + "Comunicamos o resultado do cálculo  ao cliente, que é '" + calcResult.toString() + "' ... [OK]");
				CalcServer.increaseNumberCalculationsProcessed();
				this.socket.sendData(calcResult.toString());				
			}
		} else if (msg.contains(MessageType.GET_TOTAL.getValue())) {
			Integer total = CalcServer.getNumberCalculationsProcessed();
			log.info(Format.getFormattedMessage(Thread.currentThread().getName(), Format.PREFIX_1_SEND) + "Comunicamos ao cliente o total de cálculos realizados, que son '" + total + "' ... [OK]");
			this.socket.sendData(total.toString());
		} else {
			log.info(Format.getFormattedMessage(Thread.currentThread().getName(), Format.PREFIX_1_SEND) + "Comunicamos ao cliente o erro provocado pola solicitude '" + msg + "' ... [ERRO]");
			this.socket.sendData("ERRO coa mensaxe recibida: '" + msg + "'");
		}
		Thread.sleep(500);
	}
	
	/**
	 * Procesa a expresión coa libraría javaluator 
	 * 
	 * @param expr expresión aritmética a procesar
	 * @return devolve o resultado de procesar a expresión aritmética
	 */
	public Double processCalc(String expr) {
		Double result = new DoubleEvaluator().evaluate(expr);
		return result;
	}
	

	/**
	 * Método a executar cando se inicia o fío e obtén asignación da CPU para executarse
	 */
	@Override
	public void run() {
		try {
			processRequest();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
