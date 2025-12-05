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
}
