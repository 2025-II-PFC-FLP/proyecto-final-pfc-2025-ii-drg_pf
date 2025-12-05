package Proyecto

/**
 * Objeto que contiene la función para calcular el tiempo de inicio
 * de riego de cada tablón según una programación dada.
 *
 * Corresponde a la Sección 2.3 del documento del proyecto.
 */
object TiempoRiego {

  /**
   * Calcula el tiempo de inicio de riego para cada tablón
   * según la programación pi.
   *
   * Dada una finca f y una programación de riego pi,
   * y f.length == n, tIR(f, pi) devuelve t: TiempoInicioRiego
   * tal que t(i) es el tiempo en que inicia el riego del
   * tablón i de la finca f según pi.
   *
   * Fórmula (Sección 1.2.1 del PDF):
   * - t^Π_π₀ = 0
   * - t^Π_πⱼ = t^Π_π(j-1) + tr^F_π(j-1),  para j = 1, ..., n-1
   *
   * @param f Finca con n tablones
   * @param pi Programación de riego (permutación de {0, ..., n-1})
   * @return Vector donde resultado(i) es el tiempo de inicio del tablón i
   */
  def tIR(f: Finca, pi: ProRiego): TiempoInicioRiego = {
    val n = f.length

    // Función auxiliar recursiva de cola
    def calcularTiempos(turno: Int, acumulador: TiempoInicioRiego): TiempoInicioRiego = {
      if (turno >= n) {
        // Caso base: procesamos todos los turnos
        acumulador
      } else {
        // Caso recursivo: procesar turno actual
        val tablonActual = pi(turno)

        val tiempoInicio = if (turno == 0) {
          // t^Π_π₀ = 0
          0
        } else {
          // t^Π_πⱼ = t^Π_π(j-1) + tr^F_π(j-1)
          val tablonAnterior = pi(turno - 1)
          val tiempoInicioAnterior = acumulador(tablonAnterior)
          val tiempoRiegoAnterior = Utilidades.treg(f, tablonAnterior)

          tiempoInicioAnterior + tiempoRiegoAnterior
        }

        // Actualizar acumulador y continuar
        calcularTiempos(turno + 1, acumulador.updated(tablonActual, tiempoInicio))
      }
    }

    // Iniciar recursión con vector de ceros
    calcularTiempos(0, Vector.fill(n)(0))
  }
}