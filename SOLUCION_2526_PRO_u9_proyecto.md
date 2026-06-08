# Solución del proyecto

- **Proyecto:** <!-- Nombre del proyecto --> GymManager
- **Alumno/a:** <!-- Nombre y apellidos --> Jesús Gallardo Domínguez
- **Repositorio:** <!-- URL del repositorio --> [repo](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo)

## 1. Resumen del proyecto

- **Problema que resuelve:** <!-- Explicación breve --> Mi proyecto resuelve el problema de gestionar la información y los 
datos de un gimnasio (socios, actividades, entrenadores, inscripciones y cuotas).


- **Usuarios principales:** <!-- A quién va dirigido --> Va dirigido a la administración de gimnasios que necesiten gestionar
datos digitalmente para poder consultar cualquier duda más fácilmente.


- **Funcionalidades principales:** <!-- Lista breve -->

  - CRUD para entidades principales (csv, mongo y h2)
  
  - Gestión de inscripciones con requisitos (socio activo/inactivo, socios sin repetir, plazas disponibles para actividades... )
  
  - Validación de datos regex
  
  - Manejo de excepciones
  
  - Menú interactivo por consola


- **Entidades principales:** <!-- Clases o conceptos del dominio -->
  - Socios
  - Entrenadores
  - Actividades
  - Cuotas
  - Inscripciones

- **Estructura del proyecto:** <!-- Paquetes principales y responsabilidad -->

## Estructura del proyecto

```text
src/main/
├── kotlin/
│   ├── app/
│   │   └── Main.kt                     # Punto de entrada
│   ├── exception/                      # Excepciones personalizadas
│   │   ├── NotFoundException.kt
│   │   ├── ValidationException.kt
│   │   ├── SocioNotFoundException.kt
│   │   ├── ActividadNotFoundException.kt
│   │   ├── EntrenadorNotFoundException.kt
│   │   ├── InscripcionNotFoundException.kt
│   │   ├── CuotaNotFoundException.kt
│   │   ├── SocioInactivoException.kt
│   │   ├── ActividadSinPlazasException.kt
│   │   └── SocioYaInscritoException.kt
│   ├── model/                          # Entidades del dominio
│   │   ├── Socio.kt
│   │   ├── Actividad.kt
│   │   ├── Entrenador.kt
│   │   ├── Inscripcion.kt
│   │   └── Cuota.kt
│   ├── repository/                     # Capa de acceso a datos
│   │   ├── Repository.kt              # Interfaz genérica
│   │   ├── file/                      # Repositorios CSV
│   │   │   ├── SocioCsvRepository.kt
│   │   │   ├── ActividadCsvRepository.kt
│   │   │   ├── EntrenadorCsvRepository.kt
│   │   │   ├── InscripcionCsvRepository.kt
│   │   │   └── CuotaCsvRepository.kt
│   │   ├── sql/                       # Repositorios H2 (socios, actividades, entrenadores, inscripciones)
│   │   │   ├── SqlSocioRepository.kt
│   │   │   ├── SqlActividadRepository.kt
│   │   │   ├── SqlEntrenadorRepository.kt
│   │   │   └── SqlInscripcionRepository.kt
│   │   └── mongo/                     # Repositorio MongoDB (solo cuotas)
│   │       └── MongoCuotaRepository.kt
│   ├── service/                        # Lógica de negocio
│   │   ├── SocioService.kt
│   │   ├── ActividadService.kt
│   │   ├── EntrenadorService.kt
│   │   ├── InscripcionService.kt
│   │   └── CuotaService.kt
│   ├── ui/                             # Interfaz de usuario
│   │   └── ConsolaUI.kt
│   ├── util/                           # Utilidades y gestores de conexión
│   │   ├── DatabaseManager.kt          # Gestión de conexión H2
│   │   └── MongodbManager.kt           # Gestión de conexión MongoDB
│   └── validator/                      # Validadores de datos
│       ├── SocioValidator.kt
│       ├── ActividadValidator.kt
│       ├── EntrenadorValidator.kt
│       ├── CuotaValidator.kt
│       └── InscripcionValidator.kt
└── resources/                          # Archivos de configuración
    └── simplelogger.properties         # Configuración para silenciar logs de MongoDB
```

- **Requisitos previos:** <!-- JDK, MongoDB, SGBD, variables de entorno -->
  - JDK 21 o superior
  - MongoDB atlas
  - H2
  - Variables de entornos definidas en el `.env`
- **Configuración necesaria:** <!-- Ficheros, puertos, datos de prueba -->
  - Archivo `.env` con las variables de entorno
  - Las tablas en H2 se crean automáticamente al arrancar (DatabaseManager.initDataBase)
  - Los ficheros se crean en `data/` la primera vez que se escribe.
- **Datos de prueba incluidos:** <!-- Dónde están y cómo se usan -->
  - En `data/` están los ficheros:
    - socios.csv
    - actividades.csv
    - entrenadores.csv
    - cuotas.csv
    - inscripciones.csv
  - los datos de H2 (`gymManager.mv.db`)
  - Los datos que hay de mis pruebas se cargan automáticamente

## 3. Diseño y modelo

- **Clases principales:** <!-- Clase -> responsabilidad -->
- **Relaciones importantes:** <!-- Herencia, interfaces, composición -->
- **Genéricos usados:** <!-- Clase/interfaz/función y motivo -->
- **Colecciones usadas:** <!-- Tipo, uso y justificación -->
- **Principios SOLID aplicados:** <!-- Al menos dos, con enlace al código -->
- **Patrones de diseño:** <!-- Patrón, problema que resuelve y enlace -->

## 4. Persistencia

### Ficheros

- **Ficheros usados:** <!-- Nombre y ruta -->
- **Formato y contenido:** <!-- CSV, JSON, TXT... -->
- **Lectura/escritura:** <!-- Qué operaciones realiza -->
- **Clase responsable:** <!-- Enlace al código -->
- **Errores controlados:** <!-- Qué ocurre si falla -->

### MongoDB

- **Base de datos:** <!-- Nombre -->
- **Colecciones:** <!-- Nombre y uso -->
- **Documento de ejemplo:**

```json
{
  "campo": "valor"
}
```

- **Operaciones realizadas:** <!-- Insertar, consultar, modificar, borrar -->
- **Clase responsable:** <!-- Enlace al código -->

### Base de datos relacional

- **SGBD utilizado:** <!-- H2, SQLite, MySQL... -->
- **Script SQL:** <!-- Ruta del script -->
- **Tablas y relaciones:** <!-- Resumen -->
- **Operaciones CRUD:** <!-- Qué entidades cubren -->
- **Consultas parametrizadas:** <!-- Enlace a ejemplo en código -->
- **Gestión de conexión y cierre:** <!-- Enlace al código -->

## 5. Validaciones y errores

- **Expresiones regulares:** <!-- Dato, regex, ejemplo válido/no válido, enlace -->
- **Excepciones controladas:** <!-- Tipo de error y respuesta del programa -->
- **Excepciones propias:** <!-- Si existen, indicar clase y motivo -->

## 6. Pruebas y evidencias

- **Pruebas realizadas:** <!-- Manuales o automatizadas -->
- **Datos de prueba:** <!-- Qué datos se usaron -->
- **Evidencia de ejecución:** <!-- Salida de consola o captura -->
- **Evidencia de ficheros:** <!-- Fichero generado/leído -->
- **Evidencia de MongoDB:** <!-- Inserción/consulta -->
- **Evidencia de SQL:** <!-- CRUD realizado -->

## 7. Refactorización, documentación y Git

- **Refactorizaciones aplicadas:** <!-- Qué se mejoró y por qué -->
- **Código limpio:** <!-- Ejemplos concretos -->
- **Documentación:** <!-- KDoc, Dokka, README, diagramas... -->
- **Control de versiones:** <!-- Commits, ramas, conflictos si los hubo -->

## 8. Problemas encontrados y soluciones

| Problema | Solución aplicada | Enlace o evidencia |
|----------|-------------------|--------------------|
| <!-- Problema --> | <!-- Solución --> | <!-- Enlace --> |

## 9. Respuestas a los criterios de evaluación

Completa cada criterio con una respuesta breve (Por ejemplo, si habla de clases puedes listar las mas importantes, y entrar en detalle en alguna), técnica y con enlaces al código.

### 9.1. Diseño general

<!-- Temática, problema, entidades, funcionalidades, estructura y justificación. -->

### 9.2. Clases y objetos

<!-- Clases, propiedades, métodos, constructores, objetos instanciados y enlaces al código. -->

### 9.3. Encapsulación y visibilidad

<!-- Propiedades públicas/privadas, validaciones, métodos de modificación y decisiones. -->

### 9.4. Colecciones

<!-- Tipo de colección, información almacenada, motivo de elección y enlace al código. -->

### 9.5. Genéricos

<!-- Elemento genérico creado, problema que resuelve, ventaja y enlace al código. -->

### 9.6. Herencia, interfaces o clases abstractas

<!-- Relación entre clases/interfaces, ventaja, polimorfismo si existe y enlace al código. -->

### 9.7. Expresiones regulares

<!-- Dato validado, expresión regular, ejemplo válido, ejemplo no válido y enlace al código. -->

### 9.8. Ficheros

<!-- Ficheros, operaciones de lectura/escritura, formato, errores controlados y enlace al código. -->

### 9.9. MongoDB

<!-- Base de datos, colecciones, documentos, operaciones realizadas y enlace al código. -->

### 9.10. Base de datos relacional

<!-- SGBD, tablas, relaciones, script SQL, CRUD, conexión, cierre de recursos, consultas parametrizadas y enlace al código. -->

### 9.11. Excepciones

<!-- Errores controlados, excepciones propias, comportamiento ante error, ejemplos y enlace al código. -->

### 9.12. SOLID y buenas prácticas

<!-- Principios aplicados, clases donde aparecen, problema que evitan, mejora aportada y enlace al código. -->

### 9.13. Librerías externas

<!-- Nombre, finalidad, configuración, uso en código y motivo. -->

### 9.14. Pruebas y evidencias

<!-- Pruebas, datos, salidas, capturas si procede, ficheros generados, MongoDB y SQL. -->

### 9.15. Refactorización y código limpio

<!-- Técnicas aplicadas, mejoras conseguidas, ejemplos y enlaces. -->

### 9.16. Patrones de diseño

<!-- Patrón aplicado, ubicación, problema que resuelve, ventaja y enlace al código. -->

### 9.17. Documentación

<!-- Herramientas, partes documentadas, formato, ejemplo y enlace. -->

### 9.18. Control de versiones

<!-- Git, commits, ramas, conflictos si existen, repositorio e historial. -->

## 10. Conclusiones

- **Qué he aprendido:** <!-- Resumen -->
- **Qué mejoraría si tuviera más tiempo:** <!-- Mejoras realistas -->
- **Decisión técnica más importante:** <!-- Decisión y motivo -->

## 11. Autoevaluación

Indica en cada criterio el nivel o puntuación que consideras que has alcanzado. Usa la escala de la guía de evaluación: `0`, `2.5`, `5`, `7.5` o `10`. Justifica siempre la puntuación con evidencias concretas: clases, funciones, commits, capturas, documentación o enlaces al código.

### 11.1. Programación

| Criterio | Puntuación/Nivel | Justificación de la puntuación |
|----------|------------------|--------------------------------|
| Completitud de requisitos mínimos | <!-- 0 / 2.5 / 5 / 7.5 / 10 --> | <!-- Justifica el cumplimiento de POO, colecciones, genéricos, herencia/interfaces, regex, excepciones, SOLID, librerías, pruebas y evidencias. --> |
| Acceso a ficheros | <!-- 0 / 2.5 / 5 / 7.5 / 10 --> | <!-- Indica ficheros usados, formato, operaciones de lectura/escritura, clase responsable y control de errores. --> |
| Integración de MongoDB | <!-- 0 / 2.5 / 5 / 7.5 / 10 --> | <!-- Indica base de datos, colecciones, documentos, operaciones y clase responsable. --> |
| Base de datos relacional y operaciones CRUD | <!-- 0 / 2.5 / 5 / 7.5 / 10 --> | <!-- Indica SGBD, tablas, relaciones, script SQL, CRUD, conexión, cierre de recursos y consultas parametrizadas. --> |
| Preguntas de evaluación de Programación | <!-- 0 / 2.5 / 5 / 7.5 / 10 --> | <!-- Justifica si las respuestas de Programación están completas, son técnicas e incluyen enlaces y evidencias. --> |

### 11.2. Entornos de Desarrollo

| Criterio | Puntuación/Nivel | Justificación de la puntuación |
|----------|------------------|--------------------------------|
| Refactorización y código limpio | <!-- 0 / 2.5 / 5 / 7.5 / 10 --> | <!-- Refactorizaciones, técnicas aplicadas, mejoras y ejemplos. --> |
| Patrones de diseño | <!-- 0 / 2.5 / 5 / 7.5 / 10 --> | <!-- Patrón usado, ubicación, problema resuelto y ventaja. --> |
| Documentación | <!-- 0 / 2.5 / 5 / 7.5 / 10 --> | <!-- Herramientas, partes documentadas, formato y ejemplo. --> |
| Control de versiones | <!-- 0 / 2.5 / 5 / 7.5 / 10 --> | <!-- Commits, ramas, repositorio, conflictos si existen e historial. --> |
| Preguntas de evaluación de Entornos de Desarrollo | <!-- 0 / 2.5 / 5 / 7.5 / 10 --> | <!-- Justifica si las respuestas de Entornos están completas, son técnicas e incluyen enlaces y evidencias. --> |
