package Proyecto

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TiempoRiegoTest extends AnyFunSuite with Matchers {

  // Datos de prueba sacados del PDF

  // Finca del Ejemplo 1 (Sección 1.3.1)
  val f1: Finca = Vector(
    (10, 3, 4),  // T0
    (5, 3, 3),   // T1
    (2, 2, 1),   // T2
    (8, 1, 1),   // T3
    (6, 4, 2)    // T4
  )

  // Finca del Ejemplo 2 (Sección 1.3.2) - solo cambia el ts de T0
  val f2: Finca = Vector(
    (9, 3, 4),   // T0
    (5, 3, 3),   // T1
    (2, 2, 1),   // T2
    (8, 1, 1),   // T3
    (6, 4, 2)    // T4
  )

  // Probar con el Ejemplo 1, primera programación del PDF
  test("tIR - Ejemplo 1 del PDF, Programación Π₁ = ⟨0,1,4,2,3⟩") {
    val pi1: ProRiego = Vector(0, 1, 4, 2, 3)
    val resultado = TiempoRiego.tIR(f1, pi1)

    // El PDF dice que debería dar ⟨0, 3, 10, 12, 6⟩
    val esperado = Vector(0, 3, 10, 12, 6)

    assert(resultado == esperado,
      s"Los tiempos no coinciden con el PDF.\n" +
        s"Esperado: $esperado\n" +
        s"Obtenido: $resultado")

    // Verificar cada tablón individualmente
    assert(resultado(0) == 0, "T0 debe iniciar en t=0")
    assert(resultado(1) == 3, "T1 debe iniciar en t=3")
    assert(resultado(4) == 6, "T4 debe iniciar en t=6")
    assert(resultado(2) == 10, "T2 debe iniciar en t=10")
    assert(resultado(3) == 12, "T3 debe iniciar en t=12")
  }

  // Probar con el Ejemplo 1, segunda programación del PDF
  test("tIR - Ejemplo 1 del PDF, Programación Π₂ = ⟨2,1,4,3,0⟩") {
    val pi2: ProRiego = Vector(2, 1, 4, 3, 0)
    val resultado = TiempoRiego.tIR(f1, pi2)

    // Según el PDF debería dar ⟨10, 2, 0, 9, 5⟩
    val esperado = Vector(10, 2, 0, 9, 5)

    assert(resultado == esperado,
      s"Los tiempos no coinciden con el PDF.\n" +
        s"Esperado: $esperado\n" +
        s"Obtenido: $resultado")

    // Aquí el orden cambia: T2 va primero, luego T1, etc.
    assert(resultado(2) == 0, "T2 debe iniciar en t=0 (es el primero)")
    assert(resultado(1) == 2, "T1 debe iniciar en t=2")
    assert(resultado(4) == 5, "T4 debe iniciar en t=5")
    assert(resultado(3) == 9, "T3 debe iniciar en t=9")
    assert(resultado(0) == 10, "T0 debe iniciar en t=10")
  }

  // Probar con el Ejemplo 2, primera programación
  test("tIR - Ejemplo 2 del PDF, Programación Π₁ = ⟨2,1,4,3,0⟩") {
    val pi1: ProRiego = Vector(2, 1, 4, 3, 0)
    val resultado = TiempoRiego.tIR(f2, pi1)
    val esperado = Vector(10, 2, 0, 9, 5)

    assert(resultado == esperado,
      s"Los tiempos no coinciden con el Ejemplo 2 del PDF.\n" +
        s"Esperado: $esperado\n" +
        s"Obtenido: $resultado")
  }

  // Probar con el Ejemplo 2, segunda programación
  test("tIR - Ejemplo 2 del PDF, Programación Π₂ = ⟨2,1,4,0,3⟩") {
    val pi2: ProRiego = Vector(2, 1, 4, 0, 3)
    val resultado = TiempoRiego.tIR(f2, pi2)
    val esperado = Vector(9, 2, 0, 12, 5)

    assert(resultado == esperado,
      s"Los tiempos no coinciden con el Ejemplo 2 del PDF.\n" +
        s"Esperado: $esperado\n" +
        s"Obtenido: $resultado")
  }

  // Caso más simple posible: una finca con un solo tablón
  test("tIR - Finca con un solo tablón") {
    val finca: Finca = Vector((10, 5, 3))
    val pi: ProRiego = Vector(0)

    val resultado = TiempoRiego.tIR(finca, pi)
    val esperado = Vector(0)

    assert(resultado == esperado,
      "Un solo tablón debe iniciar en t=0")
  }

  // Finca pequeña: dos tablones en orden normal
  test("tIR - Finca con dos tablones en orden secuencial") {
    val finca: Finca = Vector(
      (10, 3, 2),  // T0 tarda 3 días en regarse
      (8, 5, 1)    // T1 tarda 5 días
    )
    val pi: ProRiego = Vector(0, 1)  // Primero T0, después T1

    val resultado = TiempoRiego.tIR(finca, pi)

    // T0 empieza en 0, T1 empieza cuando termina T0 (en 3)
    val esperado = Vector(0, 3)

    assert(resultado == esperado,
      s"Esperado: $esperado, Obtenido: $resultado")
  }

  // Mismo caso pero al revés: primero T1, después T0
  test("tIR - Finca con dos tablones en orden inverso") {
    val finca: Finca = Vector(
      (10, 3, 2),  // T0
      (8, 5, 1)    // T1
    )
    val pi: ProRiego = Vector(1, 0)  // Ahora primero T1, luego T0

    val resultado = TiempoRiego.tIR(finca, pi)

    // T1 empieza en 0, T0 empieza cuando termina T1 (en 5)
    val esperado = Vector(5, 0)

    assert(resultado == esperado,
      s"Esperado: $esperado, Obtenido: $resultado")
  }

  // Tres tablones regados en su orden natural
  test("tIR - Tres tablones en orden natural ⟨0,1,2⟩") {
    val finca: Finca = Vector(
      (10, 2, 3),  // T0 tarda 2 días
      (8, 3, 2),   // T1 tarda 3 días
      (6, 4, 1)    // T2 tarda 4 días
    )
    val pi: ProRiego = Vector(0, 1, 2)

    val resultado = TiempoRiego.tIR(finca, pi)

    // T0 en 0, T1 en 2, T2 en 5
    val esperado = Vector(0, 2, 5)

    assert(resultado == esperado,
      s"Esperado: $esperado, Obtenido: $resultado")
  }

  // Tres tablones pero en orden completamente inverso
  test("tIR - Tres tablones en orden inverso ⟨2,1,0⟩") {
    val finca: Finca = Vector(
      (10, 2, 3),  // T0
      (8, 3, 2),   // T1
      (6, 4, 1)    // T2
    )
    val pi: ProRiego = Vector(2, 1, 0)

    val resultado = TiempoRiego.tIR(finca, pi)

    // T2 en 0, T1 en 4, T0 en 7
    val esperado = Vector(7, 4, 0)

    assert(resultado == esperado,
      s"Esperado: $esperado, Obtenido: $resultado")
  }

  // Verificar que la función usa correctamente Utilidades.treg
  test("tIR - Verificar integración con Utilidades") {
    val finca: Finca = Vector(
      (15, 7, 4),
      (10, 3, 2)
    )
    val pi: ProRiego = Vector(0, 1)

    val resultado = TiempoRiego.tIR(finca, pi)

    // T1 debería empezar después del tiempo de riego de T0
    assert(resultado(0) == 0)
    assert(resultado(1) == 7)
    assert(resultado(1) == Utilidades.treg(finca, 0),
      "Debe usar Utilidades.treg para obtener el tiempo de riego")
  }

  // Probar con datos aleatorios que la estructura sea correcta
  test("tIR - Finca aleatoria debe retornar vector del tamaño correcto") {
    val finca = Generadores.fincaAlAzar(6)
    val pi = Generadores.generarProRiego(6)

    val resultado = TiempoRiego.tIR(finca, pi)

    assert(resultado.length == 6,
      "El vector de tiempos debe tener el mismo tamaño que la finca")

    // El primer tablón que se riega siempre empieza en tiempo 0
    val primerTablon = pi(0)
    assert(resultado(primerTablon) == 0,
      "El primer tablón de la programación debe iniciar en t=0")
  }

  // Verificar que los tiempos respetan el orden de la programación
  test("tIR - Los tiempos deben seguir el orden de la programación") {
    val finca: Finca = Vector(
      (10, 2, 1),
      (10, 3, 1),
      (10, 1, 1),
      (10, 4, 1)
    )
    val pi: ProRiego = Vector(0, 1, 2, 3)

    val tiempos = TiempoRiego.tIR(finca, pi)

    // Cada tablón debe empezar justo cuando termina el anterior
    assert(tiempos(0) == 0)
    assert(tiempos(1) == tiempos(0) + Utilidades.treg(finca, 0))
    assert(tiempos(2) == tiempos(1) + Utilidades.treg(finca, 1))
    assert(tiempos(3) == tiempos(2) + Utilidades.treg(finca, 2))
  }

  // Verificar que cumple con la fórmula matemática del PDF
  test("tIR - Verificar fórmula t_πⱼ = t_π(j-1) + tr_π(j-1)") {
    val finca: Finca = Vector(
      (20, 5, 3),
      (15, 3, 2),
      (10, 7, 4)
    )
    val pi: ProRiego = Vector(2, 0, 1)

    val tiempos = TiempoRiego.tIR(finca, pi)

    // Verificar que cada tiempo se calcula correctamente
    // Primer tablón (T2) siempre empieza en 0
    assert(tiempos(pi(0)) == 0)

    // Segundo tablón (T0) empieza cuando termina T2
    val t1_esperado = tiempos(pi(0)) + Utilidades.treg(finca, pi(0))
    assert(tiempos(pi(1)) == t1_esperado)

    // Tercer tablón (T1) empieza cuando termina T0
    val t2_esperado = tiempos(pi(1)) + Utilidades.treg(finca, pi(1))
    assert(tiempos(pi(2)) == t2_esperado)
  }
}
