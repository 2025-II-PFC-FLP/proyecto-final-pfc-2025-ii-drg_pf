package Proyecto

import scala.util.Random

object Generadores {

  /**
   * Genera una finca aleatoria con 'long' tablones
   * Los rangos escalan con el tamaño de la finca
   * @param long Número de tablones a generar
   * @return Finca con tablones aleatorios
   */

  def fincaAlAzar(long: Int): Finca = {
    // Crea un Vector de tamaño 'long', donde cada posición es un tablón aleatorio.
    Vector.fill(long)(
      (Random.between(1, long * 2 + 1),    // ts: tiempo de supervivencia, entre 1 y long*2
        Random.between(1, long + 1),        // tr: tiempo de riego, entre 1 y long
        Random.between(1, 5))               // p: prioridad entre 1 y 4
    )
  }

  /**
   * Genera un tablón aleatorio con rangos fijos
   * ts: 5-20 días, tr: 1-5 días, p: 1-4
   * @return Tablón con valores en rangos realistas
   */
  def generarTablon(): Tablon = {
    // Crea una tupla (ts, tr, p) con valores dentro de rangos constantes
    (Random.between(5, 21),   // ts entre 5 y 20
      Random.between(1, 6),    // tr entre 1 y 5
      Random.between(1, 5))    // p entre 1 y 4
  }

  /**
   * Genera una finca con n tablones usando rangos fijos
   * @param n Cantidad de tablones
   * @return Finca con n tablones
   */
  def generarFinca(n: Int): Finca = {
    // Genera un Vector de tamaño n usando generarTablon() para cada elemento
    Vector.fill(n)(generarTablon())
  }

  /**
   * Genera una matriz de distancias simétrica con diagonal en 0
   * d(i)(j) = d(j)(i) y d(i)(i) = 0
   * @param long Tamaño de la matriz (número de tablones)
   * @return Matriz de distancias
   */
  def distanciaAlAzar(long: Int): Distancia = {
    // Matriz base con valores aleatorios entre 1 y long*3
    val v = Vector.fill(long, long)(Random.between(1, long * 3 + 1))

    // Construye una matriz simétrica copiando el triángulo superior al inferior,
    // y poniendo 0 en la diagonal principal.
    Vector.tabulate(long, long)((i, j) =>
      if (i < j) v(i)(j)       // Triángulo superior: toma el valor original
      else if (i == j) 0      // Diagonal: distancia consigo mismo es 0
      else v(j)(i))           // Triángulo inferior: copia simétrica del superior
  }

  /**
   * Genera una programación de riego aleatoria (permutación de 0 a n-1)
   * @param n Número de tablones
   * @return Programación de riego aleatoria
   */
  def generarProRiego(n: Int): ProRiego = {
    // Mezcla los números de 0 a n-1 para formar una permutación válida
    Random.shuffle((0 until n).toVector)
  }

  /**
   * Genera múltiples fincas de diferentes tamaños
   * Útil para benchmarks y pruebas
   * @param tamanos Vector con los tamaños deseados
   * @return Vector de fincas
   */
  def generarFincasVarias(tamanos: Vector[Int]): Vector[Finca] = {
    // Genera una finca por cada tamaño en el vector 'tamanos'
    tamanos.map(fincaAlAzar)  // Usa la versión escalable
  }
}
