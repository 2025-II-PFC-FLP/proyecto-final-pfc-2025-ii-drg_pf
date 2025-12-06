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

  // GENERACIÓN DE TABLAS COMPARATIVAS
  // Genera tabla para costoRiegoFinca
  def tablaCostoRiegoFinca(tamanos: Vector[Int]): Unit = {
    println("\n" + "="*80)
    println("BENCHMARK: costoRiegoFinca vs costoRiegoFincaPar")
    println("="*80)
    println(f"${"Tamaño"}%-15s ${"Secuencial (ms)"}%-20s ${"Paralelo (ms)"}%-20s ${"Aceleración (%)"}%-20s")
    println("-"*80)

    tamanos.foreach { tamano =>
      val (tSeq, tPar, acel) = benchmarkCostoRiegoFinca(tamano)
      println(f"$tamano%-15d ${tSeq}%-20.4f ${tPar}%-20.4f ${acel}%-20.2f")
    }

    println("="*80 + "\n")
  }

  // Genera tabla para costoMovilidad
  def tablaCostoMovilidad(tamanos: Vector[Int]): Unit = {
    println("\n" + "="*80)
    println("BENCHMARK: costoMovilidad vs costoMovilidadPar")
    println("="*80)
    println(f"${"Tamaño"}%-15s ${"Secuencial (ms)"}%-20s ${"Paralelo (ms)"}%-20s ${"Aceleración (%)"}%-20s")
    println("-"*80)

    tamanos.foreach { tamano =>
      val (tSeq, tPar, acel) = benchmarkCostoMovilidad(tamano)
      println(f"$tamano%-15d ${tSeq}%-20.4f ${tPar}%-20.4f ${acel}%-20.2f")
    }

    println("="*80 + "\n")
  }

  // Genera tabla para generación de programaciones
  def tablaGenerarProgramaciones(tamanos: Vector[Int]): Unit = {
    println("\n" + "="*80)
    println("BENCHMARK: generarProgramacionesRiego vs generarProgramacionesRiegoPar")
    println("="*80)
    println(f"${"Tamaño"}%-15s ${"Secuencial (ms)"}%-20s ${"Paralelo (ms)"}%-20s ${"Aceleración (%)"}%-20s")
    println("-"*80)

    tamanos.foreach { tamano =>
      if (tamano <= 10) {
        val (tSeq, tPar, acel) = benchmarkGenerarProgramaciones(tamano)
        println(f"$tamano%-15d ${tSeq}%-20.4f ${tPar}%-20.4f ${acel}%-20.2f")
      } else {
        println(f"$tamano%-15d ${"N/A (muy grande)"}%-20s ${"N/A"}%-20s ${"N/A"}%-20s")
      }
    }

    println("="*80 + "\n")
  }

  // Genera tabla para programación óptima
  def tablaProgramacionOptima(tamanos: Vector[Int]): Unit = {
    println("\n" + "="*80)
    println("BENCHMARK: ProgramacionRiegoOptimo vs ProgramacionRiegoOptimoPar")
    println("="*80)
    println(f"${"Tamaño"}%-15s ${"Secuencial (ms)"}%-20s ${"Paralelo (ms)"}%-20s ${"Aceleración (%)"}%-20s")
    println("-"*80)

    tamanos.foreach { tamano =>
      if (tamano <= 9) {
        val (tSeq, tPar, acel) = benchmarkProgramacionOptima(tamano)
        println(f"$tamano%-15d ${tSeq}%-20.4f ${tPar}%-20.4f ${acel}%-20.2f")
      } else {
        println(f"$tamano%-15d ${"N/A (muy grande)"}%-20s ${"N/A"}%-20s ${"N/A"}%-20s")
      }
    }

    println("="*80 + "\n")
  }

  // EJECUCIÓN COMPLETA DE BENCHMARKS
  // Ejecuta todos los benchmarks y muestra todas las tablas
  def ejecutarBenchmarkCompleto(): Unit = {
    println("\n")
    println("╔" + "="*78 + "╗")
    println("║" + " "*20 + "REPORTE DE BENCHMARK COMPLETO" + " "*28 + "║")
    println("╚" + "="*78 + "╝")

    // Tamaños grandes para funciones de costo (no son factoriales)
    val tamanosGrandes = Vector(100, 500, 1000, 2000, 5000)

    // Tamaños pequeños para permutaciones (complejidad factorial)
    val tamanosPequenos = Vector(5, 6, 7, 8, 9)

    // Ejecutar todos los benchmarks
    tablaCostoRiegoFinca(tamanosGrandes)
    tablaCostoMovilidad(tamanosGrandes)
    tablaGenerarProgramaciones(tamanosPequenos)
    tablaProgramacionOptima(tamanosPequenos)

    println("\n" + "="*80)
    println("BENCHMARK COMPLETADO")
    println("="*80 + "\n")
  }

  // Benchmark con tamaños personalizados
  def ejecutarBenchmarkPersonalizado(
                                      tamanosGrandes: Vector[Int],
                                      tamanosPequenos: Vector[Int]
                                    ): Unit = {
    println("\n")
    println("╔" + "="*78 + "╗")
    println("║" + " "*18 + "REPORTE DE BENCHMARK PERSONALIZADO" + " "*26 + "║")
    println("╚" + "="*78 + "╝")

    tablaCostoRiegoFinca(tamanosGrandes)
    tablaCostoMovilidad(tamanosGrandes)
    tablaGenerarProgramaciones(tamanosPequenos)
    tablaProgramacionOptima(tamanosPequenos)

    println("\n" + "="*80)
    println("BENCHMARK COMPLETADO")
    println("="*80 + "\n")
  }

  // Analiza si vale la pena paralelizar
  def analizarParalelizacion(
                              nombreFuncion: String,
                              tamano: Int,
                              tiempoSeq: Double,
                              tiempoPar: Double
                            ): Unit = {
    val speedup = calcularSpeedup(tiempoSeq, tiempoPar)
    val aceleracion = calcularAceleracion(tiempoSeq, tiempoPar)

    println(s"\nAnálisis: $nombreFuncion con tamaño $tamano")
    println(s"  Tiempo secuencial: ${tiempoSeq} ms")
    println(s"  Tiempo paralelo: ${tiempoPar} ms")
    println(s"  Speedup: ${speedup}x")
    println(s"  Aceleración: ${aceleracion}%")

    if (speedup > 1.2) {
      println(s"  ✅ La paralelización es BENEFICIOSA (speedup > 1.2x)")
    } else if (speedup > 1.0) {
      println(s"  ⚠️  La paralelización es MARGINAL (1.0x < speedup < 1.2x)")
    } else {
      println(s"  ❌ La paralelización NO es beneficiosa (speedup < 1.0x)")
    }
  }

  // ===============================================
  // MAIN - PUNTO DE ENTRADA
  // ===============================================

  def main(args: Array[String]): Unit = {
    println("\n╔" + "="*78 + "╗")
    println("║" + " "*15 + "PROYECTO - BENCHMARKS DE PARALELIZACIÓN" + " "*22 + "║")
    println("╚" + "="*78 + "╝\n")

    ejecutarBenchmarkCompleto()

    println("\n✅ Benchmark completado exitosamente\n")
  }
}