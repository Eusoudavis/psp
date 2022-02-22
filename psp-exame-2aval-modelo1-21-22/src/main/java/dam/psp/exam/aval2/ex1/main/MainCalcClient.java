package dam.psp.exam.aval2.ex1.main;

import java.util.ArrayList;
import java.util.List;

import dam.psp.exam.aval2.ex1.CalcClient;
import dam.psp.exam.aval2.ex1.CalcRequest;
import dam.psp.exam.aval2.ex1.CalcRequest.MessageType;
import dam.psp.exam.aval2.ex1.comm.Utils;
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
public class MainCalcClient {
	
	/**
	 * Número máximo de clientes para executar de xeito concorrente
	 */
	public static final int __MAX_CLIENTS = 200;
	
	/**
	 * Método 'main' da clase que usa a clase 'Client'
	 *
	 * Exercicio 5 Uso do join para facer que todos os fíos creados finalicen a vez.
	 *
	 * @param args argumentos pasados (neste caso aínda que se pasaran, non se fai nada con eles)
	 */
	public static void main(String[] args) {
		try {		
			List<CalcClient> clients = new ArrayList<CalcClient>();
			String[] expr_values = {"2+3-10*3", "23+33*111-3", "45+313*111-3", "23+13*101-5", "3+3*20-75"};

			for (int i = 0; i < __MAX_CLIENTS; i++) {
				CalcClient calcClient = new CalcClient("localhost", 1234);
				if (!calcClient.isConnected()) {
					log.info(Format.PREFIX_1.getValue() + "Finaliza a execución despois de ter executado '" + i + "' fíos de clientes, pois o servidor deixou de responder");
					break;
				}
				calcClient.setName("Fío do cliente " + i);				
				calcClient.setMessageType(CalcRequest.MessageType.getRandomMessageType());
				if  (calcClient.getMessageType().equals(MessageType.CALC)) {
					Integer index = Utils.getRandomNumberInRange(1, 5);					
					calcClient.setBody(expr_values[index-1]);
				}
				calcClient.start();
				clients.add(calcClient);
			}
			for (CalcClient calcClient: clients
				 ) {
				calcClient.join();
			}

			log.info(Format.PREFIX_1.getValue() + "Pecháronse todas as conexións establecidas co servidor e o noso MainCalcClient finalizou");

		} catch (Exception e) {
			log.error(Format.PREFIX_1.getValue() + "O cliente fallou ao tentar establecer a conexión: " + e.getMessage());
		}
	}
}