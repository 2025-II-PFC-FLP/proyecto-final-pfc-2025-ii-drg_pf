package Proyecto

import Costos.costoRiegoTablon
import Costos.costoRiegoFinca
import Costos.costoMovilidad
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers


class   CostosTest extends AnyFunSuite with Matchers {
  //------------- Costo riego tablon ---------------//


  test("El costo de riego del tablon finca(4) (index en la lista) es igual a (2 - 1 >= 5) => 2*((5+1)-2) = 8"){
    val finca: Finca = Vector((3,5,2), (5,2,4), (4,4,1), (5,1,2), (2,1,2))
    val pi: ProRiego = Vector(3,2,4,1,0)
    assert(costoRiegoTablon(4,finca,pi) == 8)
  }



  test("El costo de riego del tablon finca(0) es igual a (6-1 >= 10) => 3*((10+1)-6) = 15"){
    val finca2: Finca = Vector((6,1,3), (4,5,3), (4,2,4), (2,5,4), (6,2,3))
    val pi: ProRiego = Vector(1,3,0,2,4)
    assert(costoRiegoTablon(0,finca2,pi) == 15)
  }



  test("El costo de riego del tablon finca(1) es igual a (6-4 >= 11) => 4*((11+4)-6) == 36"){
    val finca3: Finca = Vector((1,4,4), (6,4,4), (5,1,1), (2,5,3), (6,1,1))
    val pi: ProRiego = Vector(4,0,3,2,1)
    assert(costoRiegoTablon(1,finca3,pi) == 36)
  }



  test("El costo de riego del tablon finca(2) es igual a (6-2 >= 2) => 6 - (2+2) = 2"){
    val finca4: Finca = Vector((4,1,1), (2,5,2), (6,2,4), (4,2,4), (7,1,2))
    val pi: ProRiego = Vector(3,2,1,4,0)
    assert(costoRiegoTablon(2,finca4,pi) == 2)
  }



  test("El costo de riego del tablon finca(3) es igual a (5-2 >= 3) => 5 - (3+2) = 0"){
    val finca5: Finca = Vector((4,3,1), (6,4,1), (3,2,2), (5,2,1), (4,2,3))
    val pi: ProRiego = Vector(0,3,4,1,2)
    assert(costoRiegoTablon(3,finca5,pi) == 0)
  }



  //--------------------- Costo riego finca --------------------//



  test("El costo de riego de la finca es igual a la suma del costo de riego de los tablones finca(1) y finca(0)"){
    val finca: Finca = Vector((1,2,4), (4,2,4))
    val pi: ProRiego = Vector(1,0)
    val sum = costoRiegoTablon(1,finca,pi) + costoRiegoTablon(0,finca,pi)
    assert(costoRiegoFinca(finca,pi) == sum)
  }



  test("El costo de riego de la finca es igual a la suma del costo de cada tablon de la finca"){
    val finca: Finca = Vector((3,2,4), (3,2,1), (5,3,4))
    val pi: ProRiego = Vector(2,0,1)
    val sum = costoRiegoTablon(1,finca,pi) + costoRiegoTablon(0,finca,pi) + costoRiegoTablon(2,finca,pi)
    assert(costoRiegoFinca(finca,pi) == sum)
  }



  test("El costo de riego de la finca cambia dependiendo de la programacion de riego"){
    val finca: Finca = Vector((7,5,4), (3,4,4), (2,3,4), (6,1,2), (3,2,3))
    val pi1: ProRiego = Vector(0,3,2,4,1)
    val pi2: ProRiego = Vector(4,2,0,3,1)
    assert(costoRiegoFinca(finca,pi1) != costoRiegoFinca(finca,pi2))
  }



  test("El costo de riego de la finca con la programacion pi3 es igual a 79"){
    val finca: Finca = Vector((8,2,4), (8,3,3), (1,5,1), (3,1,1), (8,4,3), (3,2,2))
    val pi3: ProRiego = Vector(3,4,2,0,1,5)
    assert(costoRiegoFinca(finca,pi3) == 79)
  }



  test("El costo de riego de la finca anterior con la programacion pi4 es igual a 62"){
    val finca: Finca = Vector((8,2,4), (8,3,3), (1,5,1), (3,1,1), (8,4,3), (3,2,2))
    val pi3: ProRiego = Vector(5,0,2,1,3,4)
    assert(costoRiegoFinca(finca,pi3) == 62)
  }



  //-------------- Costo movilidad -------------//



  test("El costo de movilidad en finca con la programacion de riego pi y con sus distancias es igual a 5"){
    val finca: Finca = Vector((5,2,3), (5,3,3), (1,3,2))
    val dis1: Distancia = Vector(
      Vector(0, 3, 1),
      Vector(4, 0, 3),
      Vector(2, 4, 0))
    val pi: ProRiego = Vector(1,0,2)
    assert(costoMovilidad(finca,pi,dis1) == 5)
  }



  test("El costo de movilidad en finca con la programacion de riego pi y la matriz dis1 es igual a 18"){
    val finca: Finca = Vector((3,1,4), (5,5,4), (2,4,2), (3,1,2), (3,3,4))
    val dis1: Distancia = Vector(
      Vector(0, 6, 2, 7, 7),
      Vector(5, 0, 5, 7, 1),
      Vector(2, 1, 0, 8, 1),
      Vector(3, 4, 3, 0, 5),
      Vector(2, 7, 3, 5, 0))
    val pi: ProRiego = Vector(2,1,0,3,4)
    assert(costoMovilidad(finca,pi,dis1) == 18)
  }



  test("El costo de movilidad en finca con la programacion de riego pi y la matriz dis1 es igual a 9"){
    val finca: Finca = Vector((5,2,4), (3,1,1), (1,2,1), (6,1,4))
    val dis1: Distancia = Vector(
      Vector(0, 3, 3, 4),
      Vector(1, 0, 4, 7),
      Vector(6, 2, 0, 5),
      Vector(4, 1, 1, 0))
    val pi: ProRiego = Vector(3,0,2,1)
    assert(costoMovilidad(finca,pi,dis1) == 9)
  }



  test("El costo de movilidad en finca con la programacion de riego pi y la matriz dis1 es igual a 2"){
    val finca: Finca = Vector((3,2,4), (4,1,1))
    val dis1: Distancia = Vector(
      Vector(0, 5),
      Vector(2, 0))
    val pi: ProRiego = Vector(1,0)
    assert(costoMovilidad(finca,pi,dis1) == 2)
  }



  test("El costo de movilidad en finca con la programacion de riego pi y la matriz de distancias dis1 es 24"){
    val finca: Finca = Vector((7,4,4), (2,5,4), (4,4,1), (2,5,3), (4,5,4))
    val dis1: Distancia = Vector(
      Vector(0, 6, 7, 8, 2),
      Vector(7, 0, 7, 1, 2),
      Vector(3, 1, 0, 2, 2),
      Vector(4, 1, 1, 0, 6),
      Vector(8, 6, 3, 2, 0))
    val pi: ProRiego = Vector(1,0,3,4,2)
    assert(costoMovilidad(finca,pi,dis1) == 24)
  }



}




