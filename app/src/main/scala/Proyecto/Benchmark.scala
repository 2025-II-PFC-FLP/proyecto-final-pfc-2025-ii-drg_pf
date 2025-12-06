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
}