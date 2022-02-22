package dam.psp.exam.aval2.ex1.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Clase que se usa para manter unha conexión a máis alto nivel que un socket, de xeito que, a partires dun socket,  
 * se inicialicen os elementos de alto nivel para comunicar e enviar texto.
 * 
 * @author manuel
 *
 */
@Log4j2
@Getter
@Setter
public class SocketPlus {

	/**
	 * Socket que mantén a conexión
	 */
	private final Socket socket;

	/**
	 * Exercicio 1
	 *
	 * Fluxo de entrada(recepción de mensaxes)
	 */
	protected InputStreamReader inputStreamReader;

	/**
	 * Fluxo de saída (envío de mensaxes)
	 */
	protected PrintWriter printWriter = null; // Usado para os datos de saída (datos comunicados)

	/**
	 * Buffer de entrada, vinculado ao fluxo de entrada de mensaxes desde o cliente)
	 */
	protected BufferedReader bufferedReader = null; // Buffer de lecturas dos datos de entrada (datos recibidos)

	/**
	 * Construtor da clase SocketPlus
	 *
	 * Exercicio 1 Chamada a método initStream para inicialización de fluxos
	 * @param socket socket que contén a conexión que imos manexar
	 */
	public SocketPlus(Socket socket) {
		this.socket = socket;
		initStream();
	}

	/**
	 * Exercicio 1
	 *
	 * Método para a inicialización de fluxos de comunicación cliente-servidor
	 */
	public void initStream(){
		try {
			inputStreamReader = new InputStreamReader(socket.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);
			printWriter = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método xenérico que envia unha mensaxe a un destino, a través do socket de conexión establecido. 
	 * 
	 * @param message	mensaxe que queremos enviar
	 */
	public void sendData(String message) {
		try {
			if (message == null)
				throw new Exception("Non se recibiu ningunha mensaxe para enviar");

			// Fluxo de datos a enviar
			if (printWriter == null)
				throw new Exception("Non se poden comunicar os datos a través de 'printWriter' porque é NULL");

			// Se escribe en el servidor usando su flujo de datos
			printWriter.println(message);
			printWriter.flush();

		} catch (Exception e) {
			log.error("O envío de datos fallou '" + e.getMessage() + "' ...[ERRO]");
		}
	}

	/**
	 * Lee unha liña datos recibidos desde o servidor
	 * 
	 * @return lista de mensaxes lidos
	 */
	public String readData() {
		String msg = "";
		try {
			msg = bufferedReader.readLine();			
			/**
			 * Se msg é nulo entón devolve a cadea baleira
			 */
			msg = (msg == null) ? "" : msg;
			
		} catch (Exception e) {
			log.error("A lectura de datos recibidos fallou: '" + e.getMessage() + "' ...[ERRO]");
		}
		return msg;
	}


}
