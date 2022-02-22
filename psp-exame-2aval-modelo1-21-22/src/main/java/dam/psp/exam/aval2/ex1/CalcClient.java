package dam.psp.exam.aval2.ex1;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import dam.psp.exam.aval2.ex1.CalcRequest.MessageType;
import dam.psp.exam.aval2.ex1.comm.SocketPlus;
import dam.psp.exam.aval2.ex1.comm.Utils.Format;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Clase principal do cliente que enviará peticións ao servidor 'CalcServer'. O cliente debe poder executarse como un fío.
 * 
 * Esta clase ten definido un main no que executará un conxunto de clientes en múltiples fíos co obxectivo de probar de xeito
 * concorrente o noso servidor.
 * 
 * @author <a href="mailto:manuelpacior@gmail.com">Manuel Pacior Pérez</a>
 *
 */
@Log4j2
@Getter
@Setter
public class CalcClient extends Thread {

	/**
	 * Socket que mantén a conexión
	 */
	protected SocketPlus socket = null;

	/**
	 * Exercicio 2 Instanciación de clase Enum
	 */
	protected MessageType messageType;

	/**
	 * Contén o corpo da mensaxe enviada. Por exemplo en 'CALC::23+45' o corpo sería '23+45'
	 */
	protected String body = null;

	/**
	 * Exercicio 2 Construtor de clase
	 * @param host parámetro que representa a ip que precisa o socket
	 * @param port parámetro que representa o porto que precisa o socket
	 */
	public CalcClient(String host, int port) {
		try {
			socket = new SocketPlus(new Socket(host, port));
		} catch (UnknownHostException e) {
			log.error(e.getMessage());
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			log.error(e.getMessage());
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}

	/**
	 * Método que se encarga de comunicar unha petición ao servidor e recibir a resposta
	 *
	 * Exercicio 2 Non se lanza ningunha excepción
	 */
	public void process() {
		if (this.socket == null) {
			log.error(Format.getFormattedMessage(Thread.currentThread().getName(),Format.PREFIX_1) + "O socket é nulo e non se pode comunicar a mensaxe [ERRO]");
			return;
		}
		/**
		 * Enviamos a nosa mensaxe no formato indicado ao servidor
		 */
		String msg = this.getMessage();
		this.socket.sendData(this.getMessage());
		log.info(Format.getFormattedMessage(Thread.currentThread().getName(), Format.PREFIX_1_SEND) + " Mensaxe enviada ao servidor: '" + msg + "'");

		/**
		 * Lemos do servidor, a resposta recibida para a petición solicitada
		 */
		String response = this.socket.readData();
		if ((response != null) && (!response.isEmpty()))
			log.info(Format.getFormattedMessage(Thread.currentThread().getName(),Format.PREFIX_1_RECEIVED) + " O resultado recibido do servidor para '" + msg + "': '" + response + "' ...[OK]");
		else
			log.error(Format.getFormattedMessage(Thread.currentThread().getName(), Format.PREFIX_1_RECEIVED) + " Non se procesou a petición '" + msg + "' o servidor ...[ERRO]");
	}

	/**
	 * Método a executar cando se inicia o fío e obtén asignación da CPU para executarse
	 */
	public void run() {
		this.process();
	}

	/**
	 * Comproba que hai un socket de conexión co servidor
	 * 
	 * @return TRUE se hai conexón e FALSE en caso contrario
	 *
	 * Exercicio 2 Non se lanza ningunha excepción
	 */
	public boolean isConnected() {
		return ((this.socket != null) && (this.socket.getSocket() != null) && (this.socket.getSocket().isConnected())) ? true : false;
	}

	/**
	 * Devolve a mensaxe que imos a enviar ao servidor
	 * 
	 * @return mensaxe que enviamos ao servidor
	 */
	public String getMessage() {
		String msg = "";
		if (messageType == MessageType.CALC) {
			msg = messageType.getValue() + this.getBody();
		} else if (messageType == MessageType.GET_TOTAL) {
			msg = messageType.getValue();
		}
		return msg;
	}

}