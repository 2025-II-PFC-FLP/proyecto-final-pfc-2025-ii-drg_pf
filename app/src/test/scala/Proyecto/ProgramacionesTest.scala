package Proyecto

// Importa las librerías necesarias de ScalaTest
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

// Declaramos la clase del test, que extiende AnyFunSuite
// y Matchers para usar "shouldBe", "shouldEqual", etc.
class ProgramacionesTest extends AnyFunSuite with Matchers {

  // Finca pequeña que usaremos para las pruebas de Programaciones
  val f: Finca = Vector(
    (6, 2, 3),  // Tablón 0: ts=6, tr=2, p=3
    (4, 1, 1),  // Tablón 1: ts=4, tr=1, p=1
    (7, 3, 4)   // Tablón 2: ts=7, tr=3, p=4
  )

  // Matriz de distancias entre los tablones.
  // Es simétrica y la diagonal es 0, como lo indica el pdf.
  val d: Distancia = Vector(
    Vector(0, 2, 4),
    Vector(2, 0, 3),
    Vector(4, 3, 0)
  )

  // 1. PRUEBA DE GENERACIÓN DE PROGRAMACIONES
  test("generarProgramacionesRiego debe generar todas las permutaciones") {

    // Llamamos a la función que genera todas las permutaciones posibles.
    val programaciones = Programaciones.generarProgramacionesRiego(f)

    // Para 3 tablones deben existir 3! = 6 permutaciones posibles.
    programaciones.length shouldBe 6

    // Verificamos que no haya duplicados.
    programaciones.toSet.size shouldBe 6
  }

  // 2. PRUEBA DE VALIDACIÓN DE PROGRAMACIONES
  test("esProgramacionValida debe aceptar permutaciones correctas") {

    // Esta es una permutación válida de {0,1,2}
    val pi = Vector(2, 0, 1)

    // La función debe devolver true
    Programaciones.esProgramacionValida(pi, 3) shouldBe true
  }

  test("esProgramacionValida debe rechazar permutaciones incorrectas") {

    // Esta NO es válida porque el 2 se repite y falta el 0
    val pi = Vector(2, 2, 1)

    // La función debe detectarlo y devolver false
    Programaciones.esProgramacionValida(pi, 3) shouldBe false
  }

  // 3. PRUEBA DE COSTO TOTAL
  test("costoTotal debe calcular correctamente un caso simple") {

    val pi = Vector(0, 1, 2)  // Programación simple y secuencial

    // Calculamos el costo total usando nuestra función
    val costo = Programaciones.costoTotal(f, pi, d)

    // Verificamos que sea igual a:
    // costo de riego total + costo de movilidad total
    costo shouldBe (Costos.costoRiegoFinca(f, pi) + Costos.costoMovilidad(f, pi, d))
  }
}