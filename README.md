# LAB2_ARSW 🚀
**Integrantes:**


* _Angie Daniela Ruiz Alfonso_
* _Juan Sebastian Díaz Salamanca_ 
## JAVA IMMORTALS 💥
### _Part I Before finishing clas._
**_Verifique el funcionamiento del programa y ejecútelo. Mientras esto ocurre, ejecute jVisualVM y verifique el consumo de CPU del proceso correspondiente. ¿Por qué es este consumo? ¿Cuál es la clase responsable?_**


_Al ejecutar el programa vemos en el JVM:_


![alt text](https://raw.githubusercontent.com/angiedanielar/LAB2_ARSW/master/img/1.png)


_Este fue el resultado obtenido luego de esperar 30 minutos, decidimos detener el programa a la fuerza para inspeccionar su respectivo código, y fue cuando vimos que dentro del programa hay un while true que nunca acabará. Este alto consumo corresponde a la clase Consumidor y se debe a que hay hilos activos aún cuando no hay elementos en la lista para "consumir"._


**_Realice los ajustes necesarios para que la solución utilice la CPU de manera más eficiente, teniendo en cuenta que, por ahora, la producción es lenta y el consumo rápido_**


_Lo primero que hicimos fue que el hilo consumidor espere (.wait()) cuando no hayan elementos en su lista para consumir. Luego de ello hicimos que el hilo productor notificara a todos los demás hilos (.notifyAll()) para que  supieran cuando se inserte un nuevo elemento en la lista para consunmir, así estos dejarán de esperar activamente. Con estos cambios realziados se obtiene un consumo más eficiente de CPU como vemos en la siguiente imagen:_


![alt text](https://raw.githubusercontent.com/angiedanielar/LAB2_ARSW/master/img/2.png)


**_Haz que el productor ahora produzca muy rápido y que el consumidor consuma lento. Teniendo en cuenta que el productor conoce un límite de Stock (cuántos elementos debe tener, como máximo en la cola), haga que se respete ese límite_**


_Para hacer que el productor ahora produzca muy rápido y que el consumidor consuma lento procedimos a retirar la espera del productor para que el pueda producir rápidamente. Teniendo en cuenta el Stock del productor, hicimos que se respetara este límite de la manera anterior, haciendo que solo consuma lo que indica el límite. Y verificamnos que  estableciendo un pequeño límite para el 'stock' de 5, no hay un alto consumo de CPU ni errores:_


![alt text](https://raw.githubusercontent.com/angiedanielar/LAB2_ARSW/master/img/3.png)


![alt text](https://raw.githubusercontent.com/angiedanielar/LAB2_ARSW/master/img/4.png)
### _Part II Synchronization and Dead-Locks_ 👻
**_Dada la intención del juego, un invariante debe ser que la suma de los puntos de vida de todos los jugadores sea siempre la misma (por supuesto, en un instante de tiempo en el que no se esté realizando una operación de aumento / reducción de tiempo). En este caso, para N jugadores, ¿cuál debería ser este valor? Ejecute la aplicación y verifique cómo funciona la opción "pausar y comprobar". ¿Se cumple la invariante?_**


_Para N jugadores el valor total de la suma de sus puntos de vida, se calcula con la siguiente formula: Valor total de la suma de puntos de vida de todos los jugadores = la cantidad inicial de vida de cada inmortal * la cantidad de inmortales. El DEFAULT_IMMORTAL_HEALTH (cantidad inicial de vida de cada inmortal) en el programa es igual a 100, y al correr el programa con 3 inmortales (N), debería dar como resultado 300. Pero como vemos, el invariante no se cumple:_


![alt text](https://raw.githubusercontent.com/angiedanielar/LAB2_ARSW/master/img/5.png)


**_Una primera hipótesis de que se presenta la condición de carrera para esta función (pausa y verificación) es que el programa consulta la lista cuyos valores imprimirá, mientras que otros hilos modifican sus valores. Para corregir esto, haga lo que sea necesario para que, antes de imprimir los resultados actuales, se pausen todos los demás hilos. Además, implemente la opción "reanudar". Vuelva a comprobar el funcionamiento (haga clic en el botón varias veces). ¿Se cumple o no el invariante ?._**


_Al pausar todos los demás hilos antes de imprimir los resultados actuales, e implementar la función para reanudar, la cual se encarga de que los hilos dormidos se despierten, vemos que el invariante sigue se cumple:_


![alt text](https://raw.githubusercontent.com/angiedanielar/LAB2_ARSW/master/img/6.png)


**_Identifica posibles regiones críticas en lo que respecta a la lucha de los inmortales._**


  *_Puede pasar que durante la pelea de jugadorX contra jugadorY, el jugadorX mate al jugadorY, pero que simultáneamente el jugadorY esté en una pelea y la gane, donde se presentaria que el jugadorY moriría, pero a la vez viviría. Acá es cuando ocurre el fenómeno de que los jugadores "reviven"._
  
  
  *_Puede pasar que al momento de seleccionar a mi rival, hayan matado a este justo en ese momento, entonces se presentaría el fenómeno de que pelearía con un jugador que ya se encuentra muerto._
  

**_Una vez que se corrige el problema, rectifique que el programa continúa funcionando de manera consistente cuando se ejecutan 100, 1000 o 10000 inmortales._**

_El programa nunca se detuvo y siguió cumpliendo el invariante._


**100 inmortales:**
![alt text](https://raw.githubusercontent.com/angiedanielar/LAB2_ARSW/master/img/7.png)


**1000 inmortales:**
![alt text](https://raw.githubusercontent.com/angiedanielar/LAB2_ARSW/master/img/8.png)


**10000 inmortales:**
![alt text](https://raw.githubusercontent.com/angiedanielar/LAB2_ARSW/master/img/9.png)


**_Analizando el esquema de operación de simulación, ¿podría esto crear una condición de carrera? Implemente la funcionalidad, ejecute la simulación y vea qué problema surge cuando hay muchos 'inmortales' en ella._**

_Esto no crearía una condición de carrera en el programa pero sí una falla en el modelo de juego en el que se basa el programa, solucionamos esto retirado de la lista de jugadores a los que mueren. Cuando se ejecuta el programa con 100000 se demora un poco pensando, pero es casi es instantánea su ejecución, aproximadamente de 5 a 10 segundos, pero al ejecutarlo con 1000000 se demora mucho más, aproximadamente de 4 a 5 minutos. El problema es que la ejecución se va haciendo cada vez más lenta, entre más inmortales, mayor tiempo de espera para su ejecución, y como podremos ver a continuación un consumo cada vez más exagerado de CPU y una cantidad cada vez más alta de hilos activos:_


**100000 inmortales:**
![alt text](https://raw.githubusercontent.com/angiedanielar/LAB2_ARSW/master/img/10.png)


**1000000 inmortales:**
![alt text](https://raw.githubusercontent.com/angiedanielar/LAB2_ARSW/master/img/11.png)


**_Para finalizar, implemente la opción STOP._**


_Implementamos la función STOP, con una bandera para saber si el juego se encuentra detenido, y cuando se encuentra la bandera de detenido verdadera, detenemos al método  run()._
### Pruebas ✅ 
![alt text](https://raw.githubusercontent.com/angiedanielar/LAB2_ARSW/master/img/12.png)
## ¡¡¡Compile and run instructions!!! 🛠️
**_Para compilar:_**
_maven package_


**_Para ejecutar las pruebas:_**
_mvn test_


**_Para ejecutar las clases principales:_** 
 * _mvn exec:java -Dexec.mainClass="edu.eci.arsw.concprg.prodcons.StartProduction"_
 
 
 * _mvn exec:java -Dexec.mainClass="edu.eci.arsw.highlandersim.ControlFrame"_
 
 
