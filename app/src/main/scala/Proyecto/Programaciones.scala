package Proyecto

object Programaciones {

  /**
   * Verifica si una programación pi es válida.
   * Una programación válida es una permutación de 0..n-1.
   *
   * @param pi programación de riego
   * @param n cantidad de tablones en la finca f
   * @return true si pi es una permutación válida
   */
  def esProgramacionValida(pi: ProRiego, n: Int): Boolean = {
    // Verifica que tenga tamaño n y que al ordenarse coincida con 0..n-1
    pi.length == n && pi.sorted == (0 until n).toVector
  }

  /**
   * Calcula el costo total de una programación.
   * Costo total = costo de riego + costo de movilidad.
   *
   * @param f finca
   * @param pi programación de riego
   * @param d matriz de distancias
   * @return costo total de la programación
   */
  def costoTotal(f: Finca, pi: ProRiego, d: Distancia): Int = {
    // Suma costo de riego y costo de movilidad
    Costos.costoRiegoFinca(f, pi) + Costos.costoMovilidad(f, pi, d)
  }

  /**
   * Encuentra la mejor (mínima) programación dentro de una lista dada.
   *
   * @param f finca
   * @param d matriz de distancias
   * @param lista lista de programaciones candidatas
   * @return tupla (programación, costo)
   */
  def mejorProgramacion(f: Finca, d: Distancia, lista: Vector[ProRiego]): (ProRiego, Int) = {
    // Evalúa cada programación y toma la de menor costo total
    lista.map(pi => (pi, costoTotal(f, pi, d))).minBy(_._2) // Devuelve la tupla (programación, costo) tomando el segundo elemento.
  }

  /**
   * Genera todas las posibles programaciones de riego de la finca f.
   * Cada programación es una permutación de los índices 0..n-1.
   *
   * @param f finca
   * @return Vector con todas las permutaciones posibles
   */
  def generarProgramacionesRiego(f: Finca): Vector[ProRiego] = {
    val n = f.length

    (0 until n).toVector.permutations.map(_.toVector).toVector // Genera todas las permutaciones del vector 0..n-1
  }

}
