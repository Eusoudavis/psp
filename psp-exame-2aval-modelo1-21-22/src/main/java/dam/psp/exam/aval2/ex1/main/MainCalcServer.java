package dam.psp.exam.aval2.ex1.main;

import dam.psp.exam.aval2.ex1.CalcServer;
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
public class MainCalcServer {
	
	/**
	 * Número máximo de peticións concorrentes que é capaz de manexar o servidor, no caso de superalas, este pecharase
	 *
	 * Exercicio 5 Modificacion do num max de request
	 */
	protected static final int __MAX_REQUESTS = 30;


	/**
	 * Método 'main' desde o que se arranca o noso servidor de cálculo para probalo
	 * 
	 * @param args argumentos pasados (neste caso aínda que se pasaran, non se fai nada con eles)
	 */
	public static void main(String[] args) {
		try {
			CalcServer server = new CalcServer(1234, __MAX_REQUESTS);
			server.start();
			
		} catch (Exception e) {
			log.error("O servidor fallou");
			log.error(e);
		}
	}
}