package Proyecto

object Optimizacion {

  /**
   * Genera todas las programaciones posibles de tamaño n.
   * Corresponde a las permutaciones de 0..n-1.
   *
   * @param n número de tablones
   * @return todas las programaciones posibles
   */
  def todasLasProgramaciones(n: Int): Vector[ProRiego] = {
    (0 until n).toVector.permutations.map(_.toVector).toVector // Permutaciones del dominio 0..n-1 convertidas a Vector
  }
}
