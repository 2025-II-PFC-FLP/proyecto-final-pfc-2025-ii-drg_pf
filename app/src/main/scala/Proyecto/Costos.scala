package Proyecto
import Utilidades.tsup
import Utilidades.treg
import Utilidades.prio
import TiempoRiego.tIR

object Costos {

  // Costo Riego Tablon recibe una finca f,la posicion i de un tablon en el vector de la Finca f
  // y una programacion de riego pi
  def costoRiegoTablon(i: Int, f: Finca, pi: ProRiego): Int = {
    // Se obtienen los "atributos" del tablon i de la finca f
    // Tiempo de supervivencia
    val ts = tsup(f,i)
    // Tiempo de regado
    val tr = treg(f,i)
    // Prioridad del tablon
    val tp = prio(f,i)
    // Tiempo de inicio de riego (1.2.1 Calculo del tiempo de inicio de riego)
    val t = tIR(f,pi)(i)
    // Formula del costo de Riego de un tablon (1.2.2 Costo de riego de un tablon)
    if(ts - tr >= t){
      ts - (t + tr)
    }
    else{
      tp * ((t + tr)-ts)
    }
  }



  def costoRiegoFinca(f: Finca, pi: ProRiego): Int = {
    //Recorre el vector Finca f de 0 a n-1 (n es el tamaño del vector o bien el numero de tablones)
    // i va tomando los valores de 0 a n-1 lo cual representa la posicion del tablon en el vector
    (0 until f.length).map(i => costoRiegoTablon(i,f,pi)).sum
  }



}
