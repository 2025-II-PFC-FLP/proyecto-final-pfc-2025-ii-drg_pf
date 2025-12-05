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
}