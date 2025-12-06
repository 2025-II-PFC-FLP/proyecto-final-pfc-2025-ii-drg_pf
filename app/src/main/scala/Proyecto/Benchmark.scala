package Proyecto

import org.scalameter._

object Benchmark {

  // Configuración para las mediciones de tiempo
  val standardConfig = config(
    Key.exec.minWarmupRuns := 10,
    Key.exec.maxWarmupRuns := 20,
    Key.exec.benchRuns := 20,
    Key.verbose := false
  ) withWarmer (new Warmer.Default)

  // Mide cuánto tarda en ejecutarse un bloque de código
  def medirTiempo[T](block: => T): Double = {
    val tiempo = standardConfig measure {
      block
    }
    tiempo.value
  }

  // Calcula qué tan rápido fue en comparación (en porcentaje)
  def calcularAceleracion(tiempoSeq: Double, tiempoPar: Double): Double = {
    if (tiempoPar > 0) {
      ((tiempoSeq - tiempoPar) / tiempoSeq) * 100.0
    } else {
      0.0
    }
  }

  // Calcula cuántas veces más rápido es la versión paralela
  def calcularSpeedup(tiempoSeq: Double, tiempoPar: Double): Double = {
    if (tiempoPar > 0) tiempoSeq / tiempoPar else 0.0
  }

  // BENCHMARKS PARA CADA FUNCIÓN

  // Compara costoRiegoFinca normal vs paralela
  def benchmarkCostoRiegoFinca(tamano: Int): (Double, Double, Double) = {
    val finca = Generadores.fincaAlAzar(tamano)
    val pi = Generadores.generarProRiego(tamano)

    val tiempoSeq = medirTiempo {
      Costos.costoRiegoFinca(finca, pi)
    }

    val tiempoPar = medirTiempo {
      Costos.costoRiegoFincaPar(finca, pi)
    }

    val aceleracion = calcularAceleracion(tiempoSeq, tiempoPar)

    (tiempoSeq, tiempoPar, aceleracion)
  }

  // Compara costoMovilidad normal vs paralela
  def benchmarkCostoMovilidad(tamano: Int): (Double, Double, Double) = {
    val finca = Generadores.fincaAlAzar(tamano)
    val pi = Generadores.generarProRiego(tamano)
    val distancia = Generadores.distanciaAlAzar(tamano)

    val tiempoSeq = medirTiempo {
      Costos.costoMovilidad(finca, pi, distancia)
    }

    val tiempoPar = medirTiempo {
      Costos.costoMovilidadPar(finca, pi, distancia)
    }

    val aceleracion = calcularAceleracion(tiempoSeq, tiempoPar)

    (tiempoSeq, tiempoPar, aceleracion)
  }

  // Compara generación de programaciones (solo para fincas pequeñas)
  def benchmarkGenerarProgramaciones(tamano: Int): (Double, Double, Double) = {
    val finca = Generadores.fincaAlAzar(tamano)

    val tiempoSeq = medirTiempo {
      Programaciones.generarProgramacionesRiego(finca)
    }

    val tiempoPar = medirTiempo {
      Programaciones.generarProgramacionesRiegoPar(finca)
    }

    val aceleracion = calcularAceleracion(tiempoSeq, tiempoPar)

    (tiempoSeq, tiempoPar, aceleracion)
  }

  // Compara búsqueda de programación óptima (solo fincas muy pequeñas)
  def benchmarkProgramacionOptima(tamano: Int): (Double, Double, Double) = {
    val finca = Generadores.fincaAlAzar(tamano)
    val distancia = Generadores.distanciaAlAzar(tamano)

    val tiempoSeq = medirTiempo {
      Optimizacion.ProgramacionRiegoOptimo(finca, distancia)
    }

    val tiempoPar = medirTiempo {
      Optimizacion.ProgramacionRiegoOptimoPar(finca, distancia)
    }

    val aceleracion = calcularAceleracion(tiempoSeq, tiempoPar)

    (tiempoSeq, tiempoPar, aceleracion)
  }

}