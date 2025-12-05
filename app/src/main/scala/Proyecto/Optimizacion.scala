package Proyecto

import common._

object Optimizacion {

  /**
   * Genera todas las programaciones posibles de tamaño n.
   * Corresponde a las permutaciones de 0..n-1.
   *
   * @param n número de tablones
   * @return todas las programaciones posibles
   */
  def todasLasProgramaciones(n: Int): Vector[ProRiego] = {
    (0 until n).toVector.permutations.map(_.toVector).toVector
  }

  /**
   * Versión paralela de todasLasProgramaciones.
   */
  def todasLasProgramacionesPar(n: Int): Vector[ProRiego] = {
    val base = (0 until n).toVector
    val perms = base.permutations.toVector

    if (perms.length > 1000) {
      val mitad = perms.length / 2
      val (izq, der) = parallel(
        perms.take(mitad).map(_.toVector),
        perms.drop(mitad).map(_.toVector)
      )
      izq ++ der
    } else {
      perms.map(_.toVector)
    }
  }

  /**
   * Calcula la programación óptima de una finca (versión secuencial interna).
   */
  def programacionOptima(f: Finca, d: Distancia): (ProRiego, Int) = {
    val n = f.length
    val todas = todasLasProgramaciones(n)
    Programaciones.mejorProgramacion(f, d, todas)
  }

  /**
   * Versión paralela interna.
   */
  def programacionOptimaPar(f: Finca, d: Distancia): (ProRiego, Int) = {
    val n = f.length
    val todasPar = todasLasProgramacionesPar(n)
    Programaciones.mejorProgramacion(f, d, todasPar)
  }

  /**
   * Alias público requerido por el taller (versión secuencial).
   */
  def ProgramacionRiegoOptimo(f: Finca, d: Distancia): (ProRiego, Int) =
    programacionOptima(f, d)

  /**
   * Alias público requerido por el taller (versión paralela).
   */
  def ProgramacionRiegoOptimoPar(f: Finca, d: Distancia): (ProRiego, Int) =
    programacionOptimaPar(f, d)

  /**
   * Devuelve solo el costo óptimo de la finca.
   */
  def costoOptimo(f: Finca, d: Distancia): Int = {
    val (_, costo) = programacionOptima(f, d)
    costo
  }
}
