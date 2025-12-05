package Proyecto

object Utilidades {

  // i sirve para identificar al tablon en el vector de la Finca
  // tspu obtiene el tiempo de supervivencia de un tablon en la Finca f
  def tsup(f: Finca, i: Int): Int = {
    f(i)._1
  }


  // treg obtiene el tiempo de regado de un tablon en la Finca f
  def treg(f: Finca, i: Int): Int = {
    f(i)._2
  }


  // prio obtiene el la prioridad de un tablon en la finca f
  def prio(f: Finca, i: Int): Int = {
    f(i)._3
  }



}
