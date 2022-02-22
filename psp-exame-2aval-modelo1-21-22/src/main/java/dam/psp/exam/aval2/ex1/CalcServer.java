package dam.psp.exam.aval2.ex1;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import dam.psp.exam.aval2.ex1.comm.SocketPlus;
import dam.psp.exam.aval2.ex1.comm.Utils.Format;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Clase principal que fará uso da clase 'Server' para executala
 * 
 * @author <a href="mailto:manuelpacior@gmail.com">Manuel Pacior Pérez</a>
 */
@Log4j2
@Getter
@Setter
public class CalcServer {	
	/**
	 * Número máximo de peticións concorrentes que é capaz de manexar o servidor, no caso de superalas, este pecharase
	 */
	protected Integer max_requests = null;
	
	/**
	 * Porto no que escoita o servidor
	 */
	protected final Integer port;
	
	/**
	 * Indica se o servidor continua escoitando (TRUE escoita, e FALSE para)
	 *
	 * Exercicio 5 convertílo en volátil
	 */	
	protected volatile Boolean listen = true;

	/**
	 * Clase
	 */
	protected ServerSocket serverSocket = null;

	/**
	 * Listado que contén o conxunto de sockets que manexa o servidor
	 */
	protected final List<CalcRequest> calcRequests = new ArrayList<CalcRequest>();

	/**
	 * Número de peticións procesadas en total
	 */
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private static volatile Integer number_calculations_processed = 0;

	/**
	 * Exercicio 4 Getter sincronizado para acceder aos datos dun atributo volatil
	 *
	 * @return devolve o valor do atributo
	 */
	public synchronized static Integer getNumber_calculations_processed() {
		return number_calculations_processed;
	}

	/**
	 * Exercicio 4 Setter sincronizado para persistir un valor nun atributo volátil
	 *
	 * @param number_calculations_processed parámetro que recibe o cal apunta ao propio atributo
	 */
	public synchronized static void setNumber_calculations_processed(Integer number_calculations_processed) {
		CalcServer.number_calculations_processed = number_calculations_processed;
	}


	/**
	 * Exercicio 5 Método getter para acceder a atributo volátil
	 *
	 * @return Devolve o atributo
	 */
	public synchronized Boolean getListen() {
		return listen;
	}

	/**
	 * Exercicio 5 Método setter para a persistencia de valores un atributo volátir
	 *
	 * @param listen parámetro que precisa o método. O propio atributo
	 */
	public synchronized void setListen(Boolean listen) {
		this.listen = listen;
	}

	/**
	 * Construtor do servidor
	 * 
	 * @param port porto no que escoitará o servidor
	 * @param max_requests número máximo de peticións que o servidor é capaz de procesar
	 */
	public CalcServer(Integer port, int max_requests) {
		this.port = port;
		this.max_requests = max_requests;
		try {
			this.serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			log.error("[ERRO] O servidor fallou ao tentar establecer a conexión no porto '" + this.port + "': " + e.getMessage());
		}
	}
	
	/**
	 * Construtor do servidor
	 * 
	 * @param port porto no que escoitará o servidor
	 */
	public CalcServer(Integer port) {
		this(port, 2000);
	}
	
	/**
	 * Método que arranca o servidor
	 *
	 * Exercicio 5 Uso do join para finalizar todos os fíos das request a vez
	 *
	 * Exercicio 5 Cambiar método de inicio dos fíos e asignación de nome ao fío
	 *
	 */
	public void start() {
		try {			
			Integer i = 1;
			while (this.getListen()) {
				// O método 'accept()' inicia o socket e espera unha conexión desde un cliente
				log.info(Format.PREFIX_1.getValue() + "Esperando peticións desde un cliente no porto '" + this.getPort() + "' ... [OK]");
				CalcRequest calcRequest = new CalcRequest(this.getServerSocket().accept());
				calcRequest.setName("Fio CalcRequest " + i + " do dgonzalez");
				this.getCalcRequests().add(calcRequest);
				calcRequest.start();
				if (CalcServer.getNumberCalculationsProcessed() > this.max_requests) {
					this.setListen(false);
				}
				i++;
			}
			for (CalcRequest calcRequest: calcRequests
				 ) {
				calcRequest.join();
			}

			// Finalizamos a conexión co cliente
			this.getServerSocket().close();
			log.info(Format.PREFIX_1.getValue() + "Pechouse o servidor");

		} catch (Exception e) {
			log.error("O servidor fallou");
			log.error(e);
		}
	}
	
	/**
	 * Incrementa o número de elementos que foron procesados.
	 *
	 * Exercicio 4 Modificación do método para o acceso e persistencia de datos dun atributo volatil
	 */
	public static void increaseNumberCalculationsProcessed() {
		setNumber_calculations_processed(getNumber_calculations_processed()+1);
	}

	/**
	 * Devolve o número de peticións de cálculo procesadas.
	 * 
	 * @return número de cálculos realizados
	 */
	public static Integer getNumberCalculationsProcessed() {
		return CalcServer.number_calculations_processed;
	}
}