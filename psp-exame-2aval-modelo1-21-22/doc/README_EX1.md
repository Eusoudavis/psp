#PSP - Exame da primeira avaliación no curso 21-22

####Introdución
Aquí están as instruccións para o **modelo 1** do exame 1 da 1ª avaliación de PSP no curso 2021/2022. Temos varias aplicacións implementedas:
* **Servidor:** Unha aplicación servidor adicado a **procesar expresións aritméticas**, que estará dispoñible para procesar peticións a través do porto **1234**. As mensaxes que acepta deben seguir o seguinte protocolo de comunicación:
   	
    * **Expresión aritmética**: Mensaxe recibido para realizar o procesamento dunha expresión aritmética. Debe ter o seguinte formato 'CALC::EXPRESION', por exemplo 'CALC::23*5-22'.
    
    * **Número total de cálculos procesados**: Mensaxe para solicitar o número total de peticións de procesamento de cálculo foron executadas polo servidor. Debe ter o seguinte formato 'GET::TOTAL'.
    
    * **Fin da comunicación**: Mensaxe para solicitar o número total de peticións de procesamento de cálculo foron executadas polo servidor. Debe ter o seguinte formato 'GET::TOTAL'.


* **Cliente:** Unha aplicación cliente que permite establecer e comunicar cun servidor. Ademais terá un método main no que, usando fíos, lanza **múltiples** clientes de xeito concorrente e para probar o servidor.

