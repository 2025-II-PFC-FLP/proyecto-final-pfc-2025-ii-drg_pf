# Informe de Corrección del Proyecto de Riego Óptimo

**Fundamentos de Programación Funcional y Concurrente**  
Documento elaborado siguiendo el estilo y estructura del material de ejemplo proporcionado por el docente.

---

## Argumentación de corrección de las funciones implementadas

En este documento se presenta la argumentación formal de corrección de las funciones desarrolladas para el proyecto de optimización del riego.  
Al igual que en el archivo de referencia, la corrección se basa en los principios de:

- **Inducción estructural**
- **Correspondencia con especificaciones matemáticas**
- **Análisis del comportamiento recursivo y funcional**

El objetivo es demostrar que cada función implementada cumple su especificación formal.

---

## 1. Corrección de la función `tIR`

La función `tIR` está definida en:

```scala
def tIR(f: Finca, pi: ProRiego): TiempoInicioRiego = { ... }
```

Basada en las ecuaciones formales del documento:

- Caso base:  
  $t_{\pi_0} = 0$

- Caso inductivo:  
  $t_{\pi_j} = t_{\pi_{j-1}} + tr(f, \pi_{j-1})$

### Demostración por inducción estructural

Sea $f$ una finca con $n$ tablones y $\pi$ una permutación válida:

### Caso base: $j = 0$

El programa ejecuta:

```scala
if (turno == 0) 0
```

lo cual coincide exactamente con la definición formal.  
Por lo tanto:  
$P_{tIR}(\pi_0) = 0 = t_{\pi_0}$.

### Caso inductivo

Hipótesis de inducción:

$$
P_{tIR}(\pi_k) = t_{\pi_k}
$$

El programa calcula:

```scala
tiempoInicio = tiempoInicioAnterior + tiempoRiegoAnterior
```

que corresponde a:

$$
t_{\pi_{k+1}} = t_{\pi_k} + tr(f,\pi_k)
$$

Luego actualiza el acumulador inmutable:

```scala
acumulador.updated(tablonActual, tiempoInicio)
```

Con esto se cumple que:

$$
P_{tIR}(\pi_{k+1}) = t_{\pi_{k+1}}
$$

### Conclusión

Por inducción estructural:

$$
\forall i \in \{0,\ldots,n-1\},\; P_{tIR}(i) = t(i)
$$

La función es correcta con respecto a su especificación.

---

## 2. Corrección de `generarProgramacionesRiego`

La función:

```scala
def generarProgramacionesRiego(f: Finca): Vector[ProRiego] =
  (0 until n).toVector.permutations.map(_.toVector).toVector
```

Su especificación formal es generar:

$$
Perm(0,1,\ldots,n-1)
$$

### Argumentación

La función `permutations` de Scala implementa la definición matemática clásica:

- Caso base:  
  $Perm([]) = [[]]$

- Caso inductivo:  
  Para una lista $L = [x_1,\ldots,x_n]$:

$$
Perm(L) =
\bigcup_{x\in L} x :: Perm(L - \{x\})
$$

Como la función no altera el proceso generativo, y solo convierte los resultados a `Vector`, entonces:

$$
P_{perms}(L) = Perm(L)
$$

### Conclusión

La función genera exactamente todas las permutaciones posibles.  
Por lo tanto, es correcta.

---

## 3. Corrección de `costoRiegoTablon` y `costoRiegoFinca`

La definición implementada es:

$$
costo(i) =
\begin{cases}
ts(i) - (t(i) + tr(i)), & \text{si } ts(i) - tr(i) \ge t(i) \\
p(i)\cdot\big((t(i) + tr(i)) - ts(i)\big), & \text{en caso contrario}
\end{cases}
$$

El código:

```scala
if(ts - tr >= t)
    ts - (t + tr)
else
    tp * ((t + tr) - ts)
```

Esto sigue exactamente la definición matemática.  
Como `tIR`, `tsup`, `treg`, `prio` son correctas, se concluye:

$$
P_{costo}(i) = costo(i)
$$

La función de finca suma todos los costos:

```scala
(0 until f.length).map(i => costoRiegoTablon(i,f,pi)).sum
```

lo cual corresponde a:

$$
CostoTotal = \sum_{i=0}^{n-1} costo(i)
$$

### Conclusión

La implementación coincide con la especificación formal.  
Por lo tanto, es correcta.

---

## 4. Corrección de `costoMovilidad`

Especificación:

$$
CostoMov(\pi)=\sum_{j=0}^{n-2} d(\pi_j,\pi_{j+1})
$$

Implementación:

```scala
val parejas = pi.sliding(2).map{case Vector(a,b) => (a,b)}.toVector
parejas.map{case (a,b) => d(a)(b)}.sum
```

`sliding(2)` produce los pares consecutivos, cumpliendo:

$$
(\pi_0,\pi_1),(\pi_1,\pi_2),\ldots,(\pi_{n-2},\pi_{n-1})
$$

La suma es exactamente la definida.

### Conclusión

$$
P_{mov}(\pi)=CostoMov(\pi)
$$

La función es correcta.

---

## 5. Corrección de `mejorProgramacion`

```scala
lista.map(pi => (pi, costoTotal(f,pi,d))).minBy(_._2)
```

La definición formal del problema es:

$$
\pi^* = \arg\min_{\pi\in Perm(n)} costoTotal(\pi)
$$

Dado que:

- la generación de permutaciones es correcta,
- el costo total es correcto,
- la búsqueda del mínimo es correcta,

entonces:

$$
P_{opt} = \pi^*
$$

### Conclusión

La función implementa exactamente la especificación del óptimo global.

---

## Conclusión General

Todas las funciones del proyecto fueron verificadas formalmente, y cada una:

- cumple su especificación matemática,
- mantiene consistencia interna,
- utiliza principios funcionales que facilitan la verificación,
- respeta definiciones recursivas o combinatorias según corresponda.

Por lo tanto:

$$
\forall \text{ funciones del proyecto: } P_f(x) = f(x)
$$

El proyecto es **correcto** desde el punto de vista matemático y computacional.

---




**Fin del informe.**
```mermaid
flowchart TD

    A["Especificación Matemática"] --> B["Corrección de tIR"]
    A --> C["Corrección de Generación de Permutaciones"]
    A --> D["Corrección del Costo de Riego"]
    A --> E["Corrección del Costo de Movilidad"]

    B --> F["Funciones individuales correctas"]
    C --> F
    D --> F
    E --> F

    F --> G["Evaluación de todas las programaciones"]
    G --> H["Selección del mínimo"]
    H --> I["Corrección de la Programación Óptima"]