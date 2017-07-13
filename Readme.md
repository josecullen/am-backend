# Almundo Backend
## Clases Principales
### Dispatcher
La clase **Dispatcher** fue realizada como singleton, 
 ya que la finalidad de la misma es que maneje el pool de threads para toda la aplicación.

 Esta clase es central en la funcionalidad, ya que maneja las prioridades de empleados, las llamadas y los threads.
 
### Modelos
Los modelos de empleados implementan la Interface **Employee**, para poder manejarse por polimorfismo.

## Solución cuando no hay empleados libres
La solución tiene su test *testLessEmployeesThanCalls* y fue resuelta con una cola de *waitingCalls* que se va llenando 
en el método *dispatchCall*. Cuando se desocupa un thread, verifica si dicha cola tiene objetos, y en caso afirmativo, 
vuelve a despachar la call.

## Solución cuando entran más de 10 llamada concurrentes.
La solución tiene su test *testDispatcherMoreCallsThanThreads*.
En *dispatchCall* se agrega la llamada al **ExecutorService**, que maneja sola el encolado de runnables cuando superan 
el pool de threads. 
Un problema que ocurrió haciendo esto fue que asignaba el Empleado *antes* de ponerlo en el pool, pero, debido a la 
demora, cuando éste realmente corría, a veces ya había un empleado de menor rango disponible. Por eso se cambió para que
la asignación del empleado se de cuando está corriendo el runnable.

