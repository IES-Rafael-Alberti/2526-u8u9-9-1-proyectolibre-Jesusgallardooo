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
  - Socio: datos del socio 
  - Entrenador: entrenador del gimnasio
  - Actividad: actividad del gimnasio
  - Inscripción: vincula un socio a una actividad
  - Cuota: pagos de los socios.

- **Relaciones importantes:** <!-- Herencia, interfaces, composición -->

  - Interfaz genérica `Repository<T, ID>` implementada en toda la capa repository. Los servicios dependen de esta interfaz
  no de las implementaciones concretas.
  
  - Herencia de excepciones: `NotFoundException` (open) -> `SocioNotFoundException`, `ActividadNotFoundException` con cada entidad... 
  
  - Composición: los servicios reciben sus repositorios por constructor (`SocioService(csvRepo, sqlRepo)`)

  - Polimorfismo: SocioService puede utilizarse con cualquier implementación de `Repository<Socio, Long>`

- **Genéricos usados:** <!-- Clase/interfaz/función y motivo -->
    
    - `Repository<T, ID>`: interfaz genérica que me permite definir métodos CRUD para las entidades principales sin repetir
    código. `T` es el tipo de entidad e `ID` es el tipo de su identificador (siempre es de tipo `Long`).
  

- **Colecciones usadas:** <!-- Tipo, uso y justificación -->

  - MutableMap<Long, T>: en repositorios CSV (5 clases). Almacena entidades en memoria con clave = ID. 
  Justificación: acceso O(1) por ID, evita duplicados, facilita actualización/eliminación.

  - MutableList<T>: en repositorios SQL y MongoDB para construir listas desde ResultSet o FindIterable. También en escritura
    de CSV para armar líneas. Es ordenada y eficiente para añadir al final.
  
  - List<T>: retornada por findAll(). Inmutable, mantiene orden (sortedBy { it.id }).


- **Principios SOLID aplicados:** <!-- Al menos dos, con enlace al código -->

  - Single Responsibility(S): Cada clase tiene una única responsabilidad
    - `SocioValidator`: valida datos [(enlace)](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/69def15af6aba7e0c1e3c790f61d08c783ef2abc/src/main/kotlin/validator/SocioValidator.kt#L8-L32)
    - `SocioService`: contiene lógica de negocio [(enlace)](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/69def15af6aba7e0c1e3c790f61d08c783ef2abc/src/main/kotlin/service/SocioService.kt#L12-L51)
    - `ConsolaUI`: interactúa con el usuario [(enlace)](./src/main/kotlin/ui/ConsolaUI.kt)

  - Open / Closed (O): la interfaz `Repository<T, ID>` está cerrada a modificaciones pero abierta a extensiones, posibilitando
  añadir nuevas implementaciones. También `NotFoundException` es una clase abierta que se extiende en cada tipo concreto de
  excepción por herencia. [(enlace)](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/69def15af6aba7e0c1e3c790f61d08c783ef2abc/src/main/kotlin/repository/Repository.kt#L3-L14)
  
  - Liskov Substitution(L): cualquier implementación de la interfaz `Repository<T, ID>` puede sustituir a otra sin modificar
  el comportamiento esperado. Por ejemplo, `SocioService` acepta cualquier `Repository<Socio, Long>` y funciona correctamente.
  [(enlace)](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/69def15af6aba7e0c1e3c790f61d08c783ef2abc/src/main/kotlin/service/SocioService.kt#L13-L14)
  
  - Dependency Inversion(D): los servicios dependen de la abstracción `Repository<T, ID>`, no de implementaciones concretas,
  lo que facilita el cambio de la capa de persistencia sin necesidad de modificar la lógica de negocio. [(enlace)](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/69def15af6aba7e0c1e3c790f61d08c783ef2abc/src/main/kotlin/service/InscripcionService.kt#L17-L20)
  
- **Patrones de diseño:** <!-- Patrón, problema que resuelve y enlace -->

  - Repository:
    - `Repository.kt`: interfaz genérica [(enlace)](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/69def15af6aba7e0c1e3c790f61d08c783ef2abc/src/main/kotlin/repository/Repository.kt#L8-L14)
    - `MongoCuotaRepository.kt`: implementación de mongoDB [(enlace)](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/69def15af6aba7e0c1e3c790f61d08c783ef2abc/src/main/kotlin/repository/mongo/MongoCuotaRepository.kt#L17-L69)    
  
  - Dependency Injection: 
    - `SocioService.kt`: constructor con interfaz genérica [(enlace)](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/69def15af6aba7e0c1e3c790f61d08c783ef2abc/src/main/kotlin/service/SocioService.kt#L12-L16)
    - `ActividadService.kt` y `EntrenadorService.kt`: mismos constructores  
  
  - Singleton: 
    - `DatabaseManager.kt`: object [(enlace)](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/69def15af6aba7e0c1e3c790f61d08c783ef2abc/src/main/kotlin/util/DatabaseManager.kt#L11)
    - `MongodbManager.kt`: object [(enlace)](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/69def15af6aba7e0c1e3c790f61d08c783ef2abc/src/main/kotlin/util/MongodbManager.kt#L14)
  

## 4. Persistencia

### Ficheros

- **Ficheros usados:** <!-- Nombre y ruta -->
  - [`actividades.csv`](./data/actividades.csv)
  - [`cuotas.csv`](./data/cuotas.csv)
  - [`entrenadores.csv`](./data/entrenadores.csv)
  - [`inscrpciones.csv`](./data/inscripciones.csv)
  - [`socios.csv`](./data/socios.csv)
  
- **Formato y contenido:** <!-- CSV, JSON, TXT... --> Para mi proyecto he escogido el formato csv y he guardado todos los datos.

- **Lectura/escritura:** <!-- Qué operaciones realiza -->
  Todos los repositorios hacen lo mismo:
  - Lectura:
    - `cargar()`: lee el csv completo con `file.readlines()`, salta la cabecera, parsea cada línea y la mete en un `MutableMap<Long, T>`
  - Escritura:
    - `guardar()`: escribe el csv completo con `file.WriteText(...)` Primero construye una `MutableList` con la cabecera + 
    1 linea por cada entidad, luego lo une y lo escribe.
  
- **Clase responsable:** <!-- Enlace al código --> Cada entidad tiene su clase responsable que hereda de `Repository.kt`:
  - Enlace al [directorio de los repositorios que gestionan los ficheros](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/tree/main/src/main/kotlin/repository/file)
  
- **Errores controlados:** <!-- Qué ocurre si falla -->
  - `cargar()`: se usa `try-catch` genérico. Si falla la lectura, se imprime `"Error al cargar CSV..."` y se deja el mapa vacío.
  No se guardan datos.
  
  - `guardar()`: ocurre lo mismo, se captura cualquier excepción, se muestra el error y se continúa. Si falla la escritura, 
  los datos nuevos se pierden al cerrar la aplicación.

    No se lanzan excepciones, solo imprimo el mensaje por consola. Si falla la carga, continua sin datos, y si falla el 
  guardado, los datos se pierden al cerrar el programa.

### MongoDB

- **Base de datos:** <!-- Nombre --> gymManager
- **Colecciones:** <!-- Nombre y uso --> cuotas
- **Documento de ejemplo:**:

```json
{
  "_id": {
    "$oid": "6a2437cd20c2e04042f9f1a7"
  },
  "idCuota": {
    "$numberLong": "1"
  },
  "socioId": {
    "$numberLong": "1"
  },
  "importe": 29.99,
  "fechaPago": "2026-06-06"
}
```

```json
{
  "_id": {
    "$oid": "6a2684e038c001f5bd30c194"
  },
  "idCuota": {
    "$numberLong": "2"
  },
  "socioId": {
    "$numberLong": "2"
  },
  "importe": 29.99,
  "fechaPago": "2026-06-08"
}
```

- **Operaciones realizadas:** <!-- Insertar, consultar, modificar, borrar -->
  - **Operaciones:** 
    - inserción
    - consulta (todos, por ID, por `socioId`)
    - modificación, borrado... 
    
    todas implementadas en `MongoCuotaRepository.kt`


- **Clase responsable:** <!-- Enlace al código -->

  - **Clase responsable:** [`MongoCuotaRepository`](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/69def15af6aba7e0c1e3c790f61d08c783ef2abc/src/main/kotlin/repository/mongo/MongoCuotaRepository.kt#L17) 
  - **Gestor de conexión:** [`MongoDBManager`](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/69def15af6aba7e0c1e3c790f61d08c783ef2abc/src/main/kotlin/util/MongodbManager.kt#L14)

### Base de datos relacional

- **SGBD utilizado:** <!-- H2, SQLite, MySQL... --> H2
- **Script SQL:** <!-- Ruta del script --> no he utilizado ningún script externo, las tablas las he creado desde el código
en [`DataBaseManager.kt`](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/69def15af6aba7e0c1e3c790f61d08c783ef2abc/src/main/kotlin/util/DatabaseManager.kt#L11)

- **Tablas y relaciones:** <!-- Resumen -->

| Tabla         | Columnas                                                                | FK                                                                                      |
|---------------|-------------------------------------------------------------------------|-----------------------------------------------------------------------------------------|
| socios        | id, nombre, apellido, email, telefono, activo                           | -                                                                                       |
| actividades   | id, nombre, plazas_maximas                                              | -                                                                                       |
| entrenadores  | id, nombre, email, especialidad                                         | -                                                                                       |
| inscripciones | id, socio_id, actividad_id, fecha_inscripcion                           | socio_id → socios(id) ON DELETE CASCADE, actividad_id → actividades(id) ON DELETE CASCADE |

- **Operaciones CRUD:** <!-- Qué entidades cubren --> 

  Cubren las entidades principales excepto las cuotas, que de eso se encarga la parte de mongo

- **Consultas parametrizadas:** <!-- Enlace a ejemplo en código -->

  Todas las consultas realizadas en mi proyecto usan `PreparedStatement` con `?`: en cualquier repository del directorio
  sql. [(Enlace)](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/69def15af6aba7e0c1e3c790f61d08c783ef2abc/src/main/kotlin/repository/sql/SqlSocioRepository.kt#L39)

- **Gestión de conexión y cierre:** <!-- Enlace al código -->
  - [Conexión](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/69def15af6aba7e0c1e3c790f61d08c783ef2abc/src/main/kotlin/util/DatabaseManager.kt#L19-L24)
  - [Cierre](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/69def15af6aba7e0c1e3c790f61d08c783ef2abc/src/main/kotlin/util/DatabaseManager.kt#L87-L94)
  - En los repository cada método abre y cierra la conexión manualmente.

## 5. Validaciones y errores

- **Expresiones regulares:** <!-- Dato, regex, ejemplo válido/no válido, enlace -->

  | Dato               | Regex                                                      | Ejemplo válido     | Ejemplo no válido | Archivo de validación                          |
  |--------------------|------------------------------------------------------------|--------------------|-------------------|------------------------------------------------|
  | Email socio/entren. | `^[A-Za-z0-9_+-.@[A-Za-z0-9._-]+\.[A-Za-z]{2,}$`         | jesus@gmail.com    | jesus@            | `SocioValidator.kt:10`, `EntrenadorValidator.kt:10` |
  | Teléfono           | `^[679][0-9]{8}$`                                          | 612345678          | 012345678         | `SocioValidator.kt:11`                         |
  | Nombre socio/entren. | `^[A-Za-záéíóúüñÁÉÍÓÚÜÑ\s]{2,50}$`                        | Juan               | A                 | `SocioValidator.kt:12`, `EntrenadorValidator.kt:11` |
  | Nombre actividad   | `^[A-Za-záéíóúüñÁÉÍÓÚÜÑ\s]{3,50}$`                        | Yoga               | AB                | `ActividadValidator.kt:10`                     |

- **Excepciones controladas:** <!-- Tipo de error y respuesta del programa -->

  - `ValidationException` – datos no cumplen regex o reglas de negocio. El mensaje se muestra por consola y se pide reintentar en `ConsolaUI`.
  - `NotFoundException` y subclases – entidad no encontrada. Se muestra mensaje al usuario.
  - `SocioInactivoException` – socio inactivo no puede inscribirse.
  - `ActividadSinPlazasException` – actividad llena.
  - `SocioYaInscritoException` – socio ya apuntado a esa actividad.
  

- **Excepciones propias:** <!-- Si existen, indicar clase y motivo -->

  - `NotFoundException.kt` – clase `open`, base para:
    - `SocioNotFoundException.kt`
    - `ActividadNotFoundException.kt`
    - `EntrenadorNotFoundException.kt`
    - `InscripcionNotFoundException.kt`
    - `CuotaNotFoundException.kt`
  - `ValidationException.kt` – datos inválidos.
  - `SocioInactivoException.kt` – socio no activo.
  - `ActividadSinPlazasException.kt` – sin plazas.
  - `SocioYaInscritoException.kt` – duplicado en actividad.

## 6. Pruebas y evidencias

- **Pruebas realizadas:** <!-- Manuales o automatizadas -->

  He realizado pruebas tanto manuales, probando mi codigo y refactorizando para que funcione correctamente, como automatizadas
  con la ayuda de **Kotest**. (52 tests en total sacados de opencode)

- **Datos de prueba:** <!-- Qué datos se usaron -->

  Para realizar las pruebas he utilizado tests con datos inline (ej. "Juan", "García", "juan@mail.com", "633809570"). y 
  los csv que he ido creando para realizar las pruebas manuales teniendo datos ya precargados.

- **Evidencia de ejecución:** <!-- Salida de consola o captura -->


  ![](./assets/tests.png)

- **Evidencia de ficheros:** <!-- Fichero generado/leído -->


  ![](./assets/ficheros.png)

- **Evidencia de MongoDB:** <!-- Inserción/consulta -->
  

  ![](./assets/mongo.png)


- **Evidencia de SQL:** <!-- CRUD realizado -->


  ![](./assets/H2.png)

## 7. Refactorización, documentación y Git

- **Refactorizaciones aplicadas:** <!-- Qué se mejoró y por qué -->

  En cuanto a refactorización no ha habido "gran cosa" ya que he intentado tener todo en cuenta desde el principio. Empecé
  con una idea, y acabé eligiendo otra... Pero si puedo destacar algo sería el paso de realizar las pruebas de la estructura 
  y de la idea en memoria a persistencia real poco a poco en h2, mongo y ficheros, y algún cambio mínimo puntual.

- **Código limpio:** <!-- Ejemplos concretos -->

  - Nombres descriptivos: `SocioValidator.kt` -> `emailRegex` `telfonoRegex` [(enlace)](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/6e13e76a43da6a54ce73569dee56ea01d04c254d/src/main/kotlin/validator/SocioValidator.kt#L8-L32)
  - Funciones pequeñas con una sola respoonsabilidad en cada service [directorio service](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/tree/main/src/main/kotlin/service)
  - Uso del `require()` para precondiciones [(enlace)](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/6e13e76a43da6a54ce73569dee56ea01d04c254d/src/main/kotlin/validator/CuotaValidator.kt#L15)
  - Inmutabilidad con data class y `copy()` en modelos 


- **Documentación:** <!-- KDoc, Dokka, README, diagramas... -->
  Para la documentación he utilizado kdoc en la mayoría de clases principales; modelos, validators, repositorios, managers... [ejemplo](https://github.com/IES-Rafael-Alberti/2526-u8u9-9-1-proyectolibre-Jesusgallardooo/blob/6e13e76a43da6a54ce73569dee56ea01d04c254d/src/main/kotlin/repository/file/SocioCsvRepository.kt#L7-L10)


- **Control de versiones:** <!-- Commits, ramas, conflictos si los hubo -->
  - Rama principal main
  - Justo en este momento 29 commits
  - No he encontrado ningún conflicto, al ser un proyecto en solitario y saber exactamente qué y dónde tocaba, no ha habido problema.

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
