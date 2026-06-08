// ui/ConsolaUI.kt
package ui

import service.*
import repository.file.*
import repository.sql.*
import repository.mongo.*
import exception.*
import util.DatabaseManager
import util.MongodbManager
import java.time.LocalDate

class ConsolaUI {

    // Repositorios CSV (siempre)
    private val csvSocioRepo = SocioCsvRepository()
    private val csvActividadRepo = ActividadCsvRepository()
    private val csvEntrenadorRepo = EntrenadorCsvRepository()
    private val csvInscripcionRepo = InscripcionCsvRepository()
    private val csvCuotaRepo = CuotaCsvRepository()

    // Repositorios SQL (H2)
    private val sqlSocioRepo = SqlSocioRepository()
    private val sqlActividadRepo = SqlActividadRepository()
    private val sqlEntrenadorRepo = SqlEntrenadorRepository()
    private val sqlInscripcionRepo = SqlInscripcionRepository()

    // Repositorio MongoDB (solo cuotas)
    private val mongoCuotaRepo = MongoCuotaRepository()

    // Servicios (ahora con doble persistencia)
    private val socioService = SocioService(csvSocioRepo, sqlSocioRepo)
    private val actividadService = ActividadService(csvActividadRepo, sqlActividadRepo)
    private val entrenadorService = EntrenadorService(csvEntrenadorRepo, sqlEntrenadorRepo)
    private val inscripcionService = InscripcionService(csvInscripcionRepo, sqlInscripcionRepo, sqlSocioRepo, sqlActividadRepo)
    private val cuotaService = CuotaService(csvCuotaRepo, mongoCuotaRepo, sqlSocioRepo)

    fun iniciar() {
        DatabaseManager.initDatabase()

        println("\n=== GESTION DE GIMNASIO ===")
        println("Persistencia:")
        println("  - Socios, Actividades, Entrenadores, Inscripciones: CSV + H2")
        if (MongodbManager.testConnection()) {
            println("  - Cuotas: CSV + MongoDB")
        } else {
            println("  - Cuotas: solo CSV (MongoDB no disponible)")
        }

        var running = true
        do {
            mostrarMenuPrincipal()
            when (readlnOrNull()?.trim()) {
                "1" -> menuSocios()
                "2" -> menuActividades()
                "3" -> menuEntrenadores()
                "4" -> menuCuotas()
                "5" -> menuInscripciones()
                "6" -> mostrarEstadisticas()
                "0" -> {
                    println("Fin del programa")
                    DatabaseManager.closeConnection()
                    MongodbManager.closeConnection()
                    running = false
                }
                else -> println("Opcion invalida")
            }
            if (running) {
                print("\nEnter para continuar...")
                readlnOrNull()
            }
        } while (running)
    }

    private fun mostrarMenuPrincipal() {
        println("\n" + "-".repeat(30))
        println("1. Socios")
        println("2. Actividades")
        println("3. Entrenadores")
        println("4. Cuotas")
        println("5. Inscripciones")
        println("6. Estadisticas")
        println("0. Salir")
        print("Opcion: ")
    }

    // ==================== SOCIOS ====================

    private fun menuSocios() {
        var running = true
        do {
            println("\n--- SOCIOS ---")
            println("1. Crear")
            println("2. Listar")
            println("3. Buscar")
            println("4. Actualizar")
            println("5. Dar baja")
            println("6. Dar alta")
            println("7. Eliminar")
            println("0. Volver")
            print("Opcion: ")

            when (readlnOrNull()?.trim()) {
                "1" -> crearSocio()
                "2" -> listarSocios()
                "3" -> buscarSocio()
                "4" -> actualizarSocio()
                "5" -> darBajaSocio()
                "6" -> darAltaSocio()
                "7" -> eliminarSocio()
                "0" -> running = false
                else -> println("Opcion invalida")
            }
        } while (running)
    }

    private fun crearSocio() {
        println("\n--- CREAR SOCIO ---")
        print("Nombre: ")
        val nombre = readlnOrNull()?.trim() ?: return
        print("Apellido: ")
        val apellido = readlnOrNull()?.trim() ?: return
        print("Email: ")
        val email = readlnOrNull()?.trim() ?: return
        print("Telefono: ")
        val telefono = readlnOrNull()?.trim() ?: return

        try {
            val socio = socioService.crearSocio(nombre, apellido, email, telefono)
            println("Creado con ID: ${socio.id}")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun listarSocios() {
        println("\n--- LISTA DE SOCIOS ---")
        val socios = socioService.listarTodosLosSocios()
        if (socios.isEmpty()) {
            println("No hay socios")
            return
        }
        socios.forEach { s ->
            val estado = if (s.activo) "Activo" else "Inactivo"
            println("[${s.id}] ${s.nombre} ${s.apellido} - $estado")
        }
    }

    private fun buscarSocio() {
        print("\nID: ")
        val id = readlnOrNull()?.trim()?.toLongOrNull()
        if (id == null) {
            println("ID invalido")
            return
        }
        try {
            val s = socioService.obtenerSocio(id)
            println("${s.nombre} ${s.apellido} - ${s.email} - ${s.telefono}")
        } catch (e: NotFoundException) {
            println("Error: ${e.message}")
        }
    }

    private fun actualizarSocio() {
        print("\nID: ")
        val id = readlnOrNull()?.trim()?.toLongOrNull()
        if (id == null) return
        try {
            val s = socioService.obtenerSocio(id)
            print("Nombre (${s.nombre}): ")
            val nombre = readlnOrNull()?.trim()?.takeIf { it.isNotBlank() } ?: s.nombre
            print("Apellido (${s.apellido}): ")
            val apellido = readlnOrNull()?.trim()?.takeIf { it.isNotBlank() } ?: s.apellido
            print("Email (${s.email}): ")
            val email = readlnOrNull()?.trim()?.takeIf { it.isNotBlank() } ?: s.email
            print("Telefono (${s.telefono}): ")
            val telefono = readlnOrNull()?.trim()?.takeIf { it.isNotBlank() } ?: s.telefono
            socioService.actualizarSocio(s.copy(nombre = nombre, apellido = apellido, email = email, telefono = telefono))
            println("Actualizado")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun darBajaSocio() {
        print("\nID: ")
        val id = readlnOrNull()?.trim()?.toLongOrNull()
        if (id == null) return
        try {
            socioService.darDeBaja(id)
            println("Socio dado de baja")
        } catch (e: NotFoundException) {
            println("Error: ${e.message}")
        }
    }

    private fun darAltaSocio() {
        print("\nID: ")
        val id = readlnOrNull()?.trim()?.toLongOrNull()
        if (id == null) return
        try {
            socioService.darDeAlta(id)
            println("Socio dado de alta")
        } catch (e: NotFoundException) {
            println("Error: ${e.message}")
        }
    }

    private fun eliminarSocio() {
        print("\nID: ")
        val id = readlnOrNull()?.trim()?.toLongOrNull()
        if (id == null) return
        try {
            val s = socioService.obtenerSocio(id)
            print("Eliminar a ${s.nombre} ${s.apellido}? (s/n): ")
            if (readlnOrNull()?.trim()?.lowercase() == "s") {
                socioService.eliminarSocio(id)
                println("Eliminado")
            }
        } catch (e: NotFoundException) {
            println("Error: ${e.message}")
        }
    }

    // ==================== ACTIVIDADES ====================

    private fun menuActividades() {
        var running = true
        do {
            println("\n--- ACTIVIDADES ---")
            println("1. Crear")
            println("2. Listar")
            println("3. Buscar")
            println("4. Actualizar")
            println("5. Eliminar")
            println("0. Volver")
            print("Opcion: ")

            when (readlnOrNull()?.trim()) {
                "1" -> crearActividad()
                "2" -> listarActividades()
                "3" -> buscarActividad()
                "4" -> actualizarActividad()
                "5" -> eliminarActividad()
                "0" -> running = false
                else -> println("Opcion invalida")
            }
        } while (running)
    }

    private fun crearActividad() {
        println("\n--- CREAR ACTIVIDAD ---")
        print("Nombre: ")
        val nombre = readlnOrNull()?.trim() ?: return
        print("Plazas: ")
        val plazas = readlnOrNull()?.trim()?.toIntOrNull()
        if (plazas == null) {
            println("Numero invalido")
            return
        }
        try {
            val a = actividadService.crearActividad(nombre, plazas)
            println("Creada con ID: ${a.id}")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun listarActividades() {
        println("\n--- LISTA DE ACTIVIDADES ---")
        val actividades = actividadService.listarTodasLasActividades()
        if (actividades.isEmpty()) {
            println("No hay actividades")
            return
        }
        actividades.forEach { a ->
            println("[${a.id}] ${a.nombre} - ${a.plazasMaximas} plazas")
        }
    }

    private fun buscarActividad() {
        print("\nID: ")
        val id = readlnOrNull()?.trim()?.toLongOrNull()
        if (id == null) {
            println("ID invalido")
            return
        }
        try {
            val a = actividadService.obtenerActividad(id)
            println("${a.nombre} - ${a.plazasMaximas} plazas")
        } catch (e: NotFoundException) {
            println("Error: ${e.message}")
        }
    }

    private fun actualizarActividad() {
        print("\nID: ")
        val id = readlnOrNull()?.trim()?.toLongOrNull()
        if (id == null) return
        try {
            val a = actividadService.obtenerActividad(id)
            print("Nombre (${a.nombre}): ")
            val nombre = readlnOrNull()?.trim()?.takeIf { it.isNotBlank() } ?: a.nombre
            print("Plazas (${a.plazasMaximas}): ")
            val plazas = readlnOrNull()?.trim()?.toIntOrNull() ?: a.plazasMaximas
            actividadService.actualizarActividad(a.copy(nombre = nombre, plazasMaximas = plazas))
            println("Actualizada")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun eliminarActividad() {
        print("\nID: ")
        val id = readlnOrNull()?.trim()?.toLongOrNull()
        if (id == null) return
        try {
            actividadService.eliminarActividad(id)
            println("Eliminada")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    // ==================== ENTRENADORES ====================

    private fun menuEntrenadores() {
        var running = true
        do {
            println("\n--- ENTRENADORES ---")
            println("1. Crear")
            println("2. Listar")
            println("3. Buscar")
            println("4. Actualizar")
            println("5. Eliminar")
            println("0. Volver")
            print("Opcion: ")

            when (readlnOrNull()?.trim()) {
                "1" -> crearEntrenador()
                "2" -> listarEntrenadores()
                "3" -> buscarEntrenador()
                "4" -> actualizarEntrenador()
                "5" -> eliminarEntrenador()
                "0" -> running = false
                else -> println("Opcion invalida")
            }
        } while (running)
    }

    private fun crearEntrenador() {
        println("\n--- CREAR ENTRENADOR ---")
        print("Nombre: ")
        val nombre = readlnOrNull()?.trim() ?: return
        print("Email: ")
        val email = readlnOrNull()?.trim() ?: return
        print("Especialidad: ")
        val especialidad = readlnOrNull()?.trim() ?: return
        try {
            val e = entrenadorService.crearEntrenador(nombre, email, especialidad)
            println("Creado con ID: ${e.id}")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun listarEntrenadores() {
        println("\n--- LISTA DE ENTRENADORES ---")
        val entrenadores = entrenadorService.listarTodosLosEntrenadores()
        if (entrenadores.isEmpty()) {
            println("No hay entrenadores")
            return
        }
        entrenadores.forEach { e ->
            println("[${e.id}] ${e.nombre} - ${e.especialidad}")
        }
    }

    private fun buscarEntrenador() {
        print("\nID: ")
        val id = readlnOrNull()?.trim()?.toLongOrNull()
        if (id == null) {
            println("ID invalido")
            return
        }
        try {
            val e = entrenadorService.obtenerEntrenador(id)
            println("${e.nombre} - ${e.email} - ${e.especialidad}")
        } catch (e: NotFoundException) {
            println("Error: ${e.message}")
        }
    }

    private fun actualizarEntrenador() {
        print("\nID: ")
        val id = readlnOrNull()?.trim()?.toLongOrNull()
        if (id == null) return
        try {
            val e = entrenadorService.obtenerEntrenador(id)
            print("Nombre (${e.nombre}): ")
            val nombre = readlnOrNull()?.trim()?.takeIf { it.isNotBlank() } ?: e.nombre
            print("Email (${e.email}): ")
            val email = readlnOrNull()?.trim()?.takeIf { it.isNotBlank() } ?: e.email
            print("Especialidad (${e.especialidad}): ")
            val especialidad = readlnOrNull()?.trim()?.takeIf { it.isNotBlank() } ?: e.especialidad
            entrenadorService.actualizarEntrenador(e.copy(nombre = nombre, email = email, especialidad = especialidad))
            println("Actualizado")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun eliminarEntrenador() {
        print("\nID: ")
        val id = readlnOrNull()?.trim()?.toLongOrNull()
        if (id == null) return
        try {
            entrenadorService.eliminarEntrenador(id)
            println("Eliminado")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    // ==================== CUOTAS (CSV + MongoDB) ====================

    private fun menuCuotas() {
        var running = true
        do {
            println("\n--- CUOTAS (CSV + MongoDB) ---")
            println("1. Registrar cuota")
            println("2. Listar cuotas")
            println("3. Cuotas por socio")
            println("4. Total pagado por socio")
            println("5. Actualizar cuota")
            println("6. Eliminar cuota")
            println("0. Volver")
            print("Opcion: ")

            when (readlnOrNull()?.trim()) {
                "1" -> registrarCuota()
                "2" -> listarCuotas()
                "3" -> cuotasPorSocio()
                "4" -> totalPagadoPorSocio()
                "5" -> actualizarCuota()
                "6" -> eliminarCuota()
                "0" -> running = false
                else -> println("Opcion invalida")
            }
        } while (running)
    }

    private fun registrarCuota() {
        println("\n--- REGISTRAR CUOTA ---")
        print("ID del socio: ")
        val socioId = readlnOrNull()?.trim()?.toLongOrNull()
        if (socioId == null) {
            println("ID invalido")
            return
        }
        print("Importe: ")
        val importe = readlnOrNull()?.trim()?.toDoubleOrNull()
        if (importe == null) {
            println("Importe invalido")
            return
        }
        try {
            val c = cuotaService.registrarCuota(socioId, importe, LocalDate.now())
            println("Cuota registrada (ID: ${c.id}) - ${c.importe} euros")
            println("  Guardada en CSV y en MongoDB")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun listarCuotas() {
        println("\n--- LISTA DE CUOTAS (desde CSV) ---")
        val cuotas = cuotaService.listarTodasLasCuotas()
        if (cuotas.isEmpty()) {
            println("No hay cuotas")
            return
        }
        cuotas.forEach { c ->
            println("[${c.id}] Socio ${c.socioId} - ${c.importe}€ - ${c.fechaPago}")
        }
    }

    private fun cuotasPorSocio() {
        print("\nID del socio: ")
        val socioId = readlnOrNull()?.trim()?.toLongOrNull()
        if (socioId == null) return
        val cuotas = cuotaService.listarCuotasPorSocio(socioId)
        if (cuotas.isEmpty()) {
            println("No hay cuotas para este socio")
            return
        }
        cuotas.forEach { c ->
            println("${c.fechaPago}: ${c.importe}€")
        }
    }

    private fun totalPagadoPorSocio() {
        print("\nID del socio: ")
        val socioId = readlnOrNull()?.trim()?.toLongOrNull()
        if (socioId == null) return
        val total = cuotaService.obtenerTotalPagadoPorSocio(socioId)
        println("Total pagado: $total euros")
    }

    private fun actualizarCuota() {
        print("\nID de la cuota: ")
        val id = readlnOrNull()?.trim()?.toLongOrNull()
        if (id == null) {
            println("ID invalido")
            return
        }
        try {
            val cuota = cuotaService.listarTodasLasCuotas().find { it.id == id }
            if (cuota == null) {
                println("Cuota no encontrada")
                return
            }
            println("Datos actuales: Socio ${cuota.socioId}, Importe ${cuota.importe}€, Fecha ${cuota.fechaPago}")
            print("Nuevo importe (Enter para mantener ${cuota.importe}): ")
            val nuevoImporte = readlnOrNull()?.trim()?.toDoubleOrNull() ?: cuota.importe
            print("Nueva fecha (YYYY-MM-DD) (Enter para mantener ${cuota.fechaPago}): ")
            val nuevaFechaStr = readlnOrNull()?.trim()
            val nuevaFecha = if (nuevaFechaStr.isNullOrBlank()) cuota.fechaPago else LocalDate.parse(nuevaFechaStr)

            val cuotaActualizada = cuota.copy(importe = nuevoImporte, fechaPago = nuevaFecha)
            cuotaService.actualizarCuota(cuotaActualizada)
            println("Cuota actualizada en CSV y MongoDB")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun eliminarCuota() {
        print("\nID de la cuota: ")
        val id = readlnOrNull()?.trim()?.toLongOrNull()
        if (id == null) return
        try {
            cuotaService.eliminarCuota(id)
            println("Cuota eliminada de CSV y MongoDB")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    // ==================== INSCRIPCIONES ====================

    private fun menuInscripciones() {
        var running = true
        do {
            println("\n--- INSCRIPCIONES ---")
            println("1. Inscribir socio")
            println("2. Listar inscripciones")
            println("3. Inscripciones por socio")
            println("4. Inscripciones por actividad")
            println("5. Plazas disponibles")
            println("6. Cancelar inscripcion")
            println("0. Volver")
            print("Opcion: ")

            when (readlnOrNull()?.trim()) {
                "1" -> inscribirSocio()
                "2" -> listarInscripciones()
                "3" -> inscripcionesPorSocio()
                "4" -> inscripcionesPorActividad()
                "5" -> plazasDisponibles()
                "6" -> cancelarInscripcion()
                "0" -> running = false
                else -> println("Opcion invalida")
            }
        } while (running)
    }

    private fun inscribirSocio() {
        println("\n--- INSCRIBIR SOCIO ---")
        print("ID del socio: ")
        val socioId = readlnOrNull()?.trim()?.toLongOrNull()
        if (socioId == null) return
        print("ID de la actividad: ")
        val actividadId = readlnOrNull()?.trim()?.toLongOrNull()
        if (actividadId == null) return
        try {
            val i = inscripcionService.inscribirSocio(socioId, actividadId, LocalDate.now())
            println("Inscrito con ID: ${i.id}")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun listarInscripciones() {
        println("\n--- LISTA DE INSCRIPCIONES ---")
        val inscripciones = inscripcionService.listarTodasLasInscripciones()
        if (inscripciones.isEmpty()) {
            println("No hay inscripciones")
            return
        }
        inscripciones.forEach { i ->
            println("[${i.id}] Socio ${i.socioId} -> Actividad ${i.actividadId} (${i.fechaInscripcion})")
        }
    }

    private fun inscripcionesPorSocio() {
        print("\nID del socio: ")
        val socioId = readlnOrNull()?.trim()?.toLongOrNull()
        if (socioId == null) return
        val inscripciones = inscripcionService.listarInscripcionesPorSocio(socioId)
        if (inscripciones.isEmpty()) {
            println("No hay inscripciones para este socio")
            return
        }
        inscripciones.forEach { i ->
            println("Actividad ${i.actividadId} - ${i.fechaInscripcion}")
        }
    }

    private fun inscripcionesPorActividad() {
        print("\nID de la actividad: ")
        val actividadId = readlnOrNull()?.trim()?.toLongOrNull()
        if (actividadId == null) return
        val inscripciones = inscripcionService.listarInscripcionesPorActividad(actividadId)
        println("Socios inscritos: ${inscripciones.size}")
        inscripciones.forEach { i ->
            println("Socio ${i.socioId}")
        }
    }

    private fun plazasDisponibles() {
        print("\nID de la actividad: ")
        val actividadId = readlnOrNull()?.trim()?.toLongOrNull()
        if (actividadId == null) return
        try {
            val disponibles = inscripcionService.obtenerPlazasDisponibles(actividadId)
            println("Plazas disponibles: $disponibles")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun cancelarInscripcion() {
        print("\nID de la inscripcion: ")
        val id = readlnOrNull()?.trim()?.toLongOrNull()
        if (id == null) return
        try {
            inscripcionService.cancelarInscripcion(id)
            println("Inscripcion cancelada")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    // ==================== ESTADISTICAS ====================

    private fun mostrarEstadisticas() {
        println("\n=== ESTADISTICAS ===")
        val totalSocios = socioService.contarSocios()
        val sociosActivos = socioService.contarSociosActivos()
        val totalActividades = actividadService.contarActividades()
        val totalEntrenadores = entrenadorService.contarEntrenadores()
        val totalCuotas = cuotaService.listarTodasLasCuotas().size
        val totalInscripciones = inscripcionService.listarTodasLasInscripciones().size

        println("Socios: $totalSocios (Activos: $sociosActivos)")
        println("Actividades: $totalActividades")
        println("Entrenadores: $totalEntrenadores")
        println("Cuotas: $totalCuotas (CSV + MongoDB)")
        println("Inscripciones: $totalInscripciones")
    }
}