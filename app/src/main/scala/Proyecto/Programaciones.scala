package Proyecto

import common._

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
    lista.map(pi => (pi, costoTotal(f, pi, d))).minBy(_._2)
  }

  /**
   * Genera todas las posibles programaciones de riego de la finca f.
   * Cada programación es una permutación de los índices 0..n-1.
   *
   * VERSIÓN SECUENCIAL
   *
   * @param f finca
   * @return Vector con todas las permutaciones posibles
   */
  def generarProgramacionesRiego(f: Finca): Vector[ProRiego] = {
    val n = f.length
    // Genera todas las permutaciones del vector 0..n-1
    (0 until n).toVector.permutations.map(_.toVector).toVector
  }

  /**
   * Genera todas las posibles programaciones de riego de la finca f.
   * Cada programación es una permutación de los índices 0..n-1.
   *
   * VERSIÓN PARALELA - Utiliza paralelización de tareas con common.parallel
   *
   * @param f finca
   * @return Vector con todas las permutaciones posibles
   */
  def generarProgramacionesRiegoPar(f: Finca): Vector[ProRiego] = {
    val n = f.length
    val indices = (0 until n).toVector

    // Genera todas las permutaciones y las convierte a Vector
    val todasPermutaciones = indices.permutations.toVector

    // Si hay suficientes permutaciones, las procesa en paralelo
    if (todasPermutaciones.length > 1000) {
      // Divide el trabajo en dos mitades y las procesa en paralelo usando common.parallel
      val mitad = todasPermutaciones.length / 2
      val (izq, der) = parallel(
        todasPermutaciones.take(mitad).map(_.toVector),
        todasPermutaciones.drop(mitad).map(_.toVector)
      )
      // Combina los resultados de ambas tareas paralelas
      izq ++ der
    } else {
      // Para casos pequeños, la versión secuencial es más eficiente
      // debido a la sobrecarga (overhead) de crear tareas paralelas
      todasPermutaciones.map(_.toVector)
    }
  }

}