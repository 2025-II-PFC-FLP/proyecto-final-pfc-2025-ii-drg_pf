package Proyecto

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ProgramacionesTest extends AnyFunSuite with Matchers {

  // Finca pequeña para pruebas
  val f: Finca = Vector(
    (6, 2, 3),  // Tablón 0: ts=6, tr=2, p=3
    (4, 1, 1),  // Tablón 1: ts=4, tr=1, p=1
    (7, 3, 4)   // Tablón 2: ts=7, tr=3, p=4
  )

  val d: Distancia = Vector(
    Vector(0, 2, 4),
    Vector(2, 0, 3),
    Vector(4, 3, 0)
  )


  // ------------------------------------------------------------
  // 1. PRUEBA DE GENERACIÓN SECUENCIAL
  // ------------------------------------------------------------
  test("generarProgramacionesRiego debe generar todas las permutaciones") {
    val programaciones = Programaciones.generarProgramacionesRiego(f)
    programaciones.length shouldBe 6
    programaciones.toSet.size shouldBe 6
  }

  // ------------------------------------------------------------
  // 2. PRUEBA DE VALIDACIÓN
  // ------------------------------------------------------------
  test("esProgramacionValida debe aceptar permutaciones correctas") {
    val pi = Vector(2, 0, 1)
    Programaciones.esProgramacionValida(pi, 3) shouldBe true
  }

  test("esProgramacionValida debe rechazar permutaciones incorrectas") {
    val pi = Vector(2, 2, 1)
    Programaciones.esProgramacionValida(pi, 3) shouldBe false
  }

  // ------------------------------------------------------------
  // 3. COSTO TOTAL (consistencia)
  // ------------------------------------------------------------
  test("costoTotal debe calcular correctamente un caso simple") {
    val pi = Vector(0, 1, 2)
    val costo = Programaciones.costoTotal(f, pi, d)
    costo shouldBe (Costos.costoRiegoFinca(f, pi) + Costos.costoMovilidad(f, pi, d))
  }

  // ------------------------------------------------------------
  // 4. MEJOR PROGRAMACIÓN (secuencial)
  // ------------------------------------------------------------
  test("mejorProgramacion debe retornar la programación óptima") {
    val programaciones = Programaciones.generarProgramacionesRiego(f)
    val (mejor, costo) = Programaciones.mejorProgramacion(f, d, programaciones)
    val todosLosCostos = programaciones.map(pi => Programaciones.costoTotal(f, pi, d))
    costo shouldBe todosLosCostos.min
    programaciones.contains(mejor) shouldBe true
  }


  // PRUEBAS PARALELAS

  // 5.1 Todas las programaciones paralelas deben coincidir con la versión secuencial
  test("generarProgramacionesRiegoPar debe generar exactamente las mismas programaciones que la versión secuencial") {
    val sec = Programaciones.generarProgramacionesRiego(f).toSet
    val par = Programaciones.generarProgramacionesRiegoPar(f).toSet
    par shouldBe sec
  }

  // 5.2 mejorProgramacion también debe funcionar con programaciones paralelas
  test("mejorProgramacion debe funcionar igual usando las programaciones paralelas") {
    val programacionesPar = Programaciones.generarProgramacionesRiegoPar(f)
    val (mejor, costo) = Programaciones.mejorProgramacion(f, d, programacionesPar)

    val costosPar = programacionesPar.map(pi => Programaciones.costoTotal(f, pi, d))

    costo shouldBe costosPar.min
    programacionesPar.contains(mejor) shouldBe true
  }

  // 5.3 Las versiones paralela y secuencial de Optimizacion deben concordar
  test("ProgramacionRiegoOptimoPar debe producir el mismo resultado que ProgramacionRiegoOptimo") {

    val (piSec, costoSec) = Optimizacion.ProgramacionRiegoOptimo(f, d)
    val (piPar, costoPar) = Optimizacion.ProgramacionRiegoOptimoPar(f, d)

    costoPar shouldBe costoSec

    Programaciones.costoTotal(f, piPar, d) shouldBe
      Programaciones.costoTotal(f, piSec, d)
  }
}
