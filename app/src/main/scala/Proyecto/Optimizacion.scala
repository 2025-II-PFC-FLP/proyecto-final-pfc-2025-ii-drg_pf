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

  /**
   * Calcula la programación óptima de una finca.
   * Revisa todas las programaciones posibles.
   *
   * @param f finca
   * @param d matriz de distancias
   * @return (mejor programación, costo mínimo)
   */
  def programacionOptima(f: Finca, d: Distancia): (ProRiego, Int) = {
    val n = f.length

    // Generar todas las posibles programaciones de riego
    val todas = todasLasProgramaciones(n)

    // Usar la función de Programaciones para elegir la mejor
    Programaciones.mejorProgramacion(f, d, todas)
  }


  /**
   * Devuelve solo el costo óptimo de la finca.
   * Trae programacionOptima pero solo entrega el costo.
   *
   * @param f finca
   * @param d matriz de distancias
   * @return costo mínimo
   */
  def costoOptimo(f: Finca, d: Distancia): Int = {
    val (_, costo) = programacionOptima(f, d)
    //Ejemplo: val (_, costo) = (Vector(0,2,1), 45), El _ ignora el primer valor y costo toma el valor 45
    costo
  }
}
