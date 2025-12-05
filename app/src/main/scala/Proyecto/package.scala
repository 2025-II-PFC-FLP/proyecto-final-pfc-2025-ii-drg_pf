package object Proyecto {
  /**
  Se definen los tipos del dominio

  *El tablon es una tupla (ts,tr,p) donde:
  *ts: tiempo de supervivencia (dias maximos sin regar)
  *tr: tiempo de riego (dias necesarios para regar)
  *p: prioridad (1-4, en donde el 4 es la mas alta)
   */

  type Tablon = (Int, Int, Int)

  /**
  La finca es un vector de tablones
*/

  type Finca = Vector[Tablon]

/**
  *Matriz es la distancia entre los tablones
  *d(i)(j) = distancia entre tablon i al j
*/

  type Distancia = Vector[Vector[Int]]

  /**
 *Programacion de riego: vector que representa la permutacion e indica el orden en que se riegan los tablones
 */

  type ProRiego= Vector[Int]

  /**
   * Tiempo de inicio para cada tablon
   * tir(i) = el momento en donde inicia el riego de tablon i
   */

  type TiempoInicioRiego = Vector[Int]

}
