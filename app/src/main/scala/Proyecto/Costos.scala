package Proyecto

import Utilidades.tsup
import Utilidades.treg
import Utilidades.prio
import TiempoRiego.tIR
import common._

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


  def costoMovilidad(f: Finca, pi: ProRiego, d: Distancia): Int = {
    val parejas = pi.sliding(2).map{case Vector(a,b) => (a,b)}.toVector
    (0 until parejas.length).map(i => d(parejas(i)._1)(parejas(i)._2)).sum
  }




  // VERSIONES PARALELAS


  def costoRiegoFincaPar(f: Finca, pi: ProRiego): Int = {
    val indices = (0 until f.length).toVector

    // Para problemas grandes paralelizamos, para pequeños no (evitar overhead)
    if (indices.length > 1000) {

      val mitad = indices.length / 2

      // Paralelizamos igual que en Programaciones
      val (izq, der) = parallel(
        indices.take(mitad).map(i => costoRiegoTablon(i, f, pi)),
        indices.drop(mitad).map(i => costoRiegoTablon(i, f, pi))
      )

      izq.sum + der.sum

    } else {
      indices.map(i => costoRiegoTablon(i, f, pi)).sum
    }
  }



  def costoMovilidadPar(f: Finca, pi: ProRiego, d: Distancia): Int = {
    val parejas = pi.sliding(2).map{case Vector(a,b) => (a,b)}.toVector

    // También igual que Programaciones: paralelizamos si hay suficiente trabajo
    if (parejas.length > 1000) {

      val mitad = parejas.length / 2

      val (izq, der) = parallel(
        parejas.take(mitad).map{ case (a,b) => d(a)(b) },
        parejas.drop(mitad).map{ case (a,b) => d(a)(b) }
      )

      izq.sum + der.sum

    } else {
      parejas.map{ case (a,b) => d(a)(b) }.sum
    }
  }


}
