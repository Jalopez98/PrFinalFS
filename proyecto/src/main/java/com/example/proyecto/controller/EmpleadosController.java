package com.example.proyecto.controller;

import com.example.proyecto.entity.Empleado;
import com.example.proyecto.entity.Proyecto;
import com.example.proyecto.exceptions.ExceptionPersonalizada;
import com.example.proyecto.repo.EmpleadoRepository;
import com.example.proyecto.service.EmpleadoServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empleados")
@Validated
public class EmpleadosController {

    @Autowired // Inyecta la dependencia EmpleadoRepository
    private EmpleadoRepository empleadosRepository;

    @Autowired // Inyecta la dependencia EmpleadoServiceImpl
    private EmpleadoServiceImpl empleadoService;

    @GetMapping // Maneja peticiones GET a la ruta base ("/empleados")
    public List<Empleado> getEmpleados() {
        return empleadoService.obtenerUsuarios(); // Llama al servicio para obtener la lista de empleados
    }

    @GetMapping("/{id}") // Maneja peticiones GET a "/empleados/{id}"
    public ResponseEntity<?> obtenerEmpleadoPorId(@PathVariable int id) {
        Empleado buscado = empleadoService.findById(id); // Llama al servicio para buscar un empleado por ID
        if (buscado == null) {
            return ResponseEntity.status(404).body("Empleado con id " +id+ " no encontrado"); // Si no se encuentra, devuelve un error 404
        }
        return ResponseEntity.ok(buscado); // Si se encuentra, devuelve el empleado con un código 200 (OK)
    }

    @PostMapping("/nuevo") // Maneja peticiones POST a "/empleados/nuevo"
    public ResponseEntity<String> addEmpleado(@Valid @RequestBody Empleado empleado) { // Valida el objeto Empleado recibido en el cuerpo de la petición
        try
        {
            empleadoService.crearNuevoEmpleado(empleado); // Llama al servicio para crear un nuevo empleado
            return ResponseEntity.ok("Empleado creado con éxito"); // Devuelve un mensaje de éxito con código 200 (OK)
        }catch(ExceptionPersonalizada ex){
            //Aqui para ver el error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); // Si hay un error, devuelve un mensaje de error con código 400 (BAD_REQUEST)
        }
    }

    @PutMapping("/{id}") // Maneja peticiones PUT a "/empleados/{id}"
    public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable int id, @Valid @RequestBody Empleado empleadoDetalles) { // Valida el objeto Empleado recibido en el cuerpo de la petición
        Empleado empleadoActualizado = empleadoService.actualizarEmpleado(id, empleadoDetalles); // Llama al servicio para actualizar un empleado
        if (empleadoActualizado != null) {
            return ResponseEntity.ok(empleadoActualizado); // Si se actualiza correctamente, devuelve el empleado actualizado con código 200 (OK)
        } else {
            return ResponseEntity.notFound().build(); // Si no se encuentra el empleado, devuelve un código 404 (NOT_FOUND)
        }
    }

    @PutMapping("/baja/{id}") // Maneja peticiones PUT a "/empleados/baja/{id}"
    public ResponseEntity<?> eliminarEmpleado(@PathVariable int id) { // Da de baja a un empleado
        try {
            empleadoService.eliminarEmpleado(id); // Llama al servicio para eliminar un empleado
            return ResponseEntity.ok("Usuario eliminado correctamente"); // Devuelve un mensaje de éxito con código 200 (OK)
        } catch (ExceptionPersonalizada e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Si hay un error, devuelve un mensaje de error con código 400 (BAD_REQUEST)
        }
    }

    @PostMapping("/{empleadoId}/proyectos/{proyectoId}") // Maneja peticiones POST a "/empleados/{empleadoId}/proyectos/{proyectoId}"
    public String asignarProyecto(@PathVariable int empleadoId, @PathVariable int proyectoId) { // Asigna un proyecto a un empleado
        return empleadoService.asignarProyecto(empleadoId, proyectoId); // Llama al servicio para asignar un proyecto a un empleado
    }

    @DeleteMapping("/{id}/eliminar-proyecto/{idProyecto}") // Maneja peticiones DELETE a "/empleados/{id}/eliminar-proyecto/{idProyecto}"
    public ResponseEntity<String> eliminarProyecto(@PathVariable int id, @PathVariable int idProyecto) { // Elimina un proyecto de un empleado
        Empleado empleadoExistente = empleadoService.findById(id); // Busca el empleado por ID
        if (empleadoExistente == null) {
            return ResponseEntity.status(404).body("El empleado con ID " + id + " no existe."); // Si el empleado no existe, devuelve un error 404
        }
        Proyecto proyectoExistente = empleadoService.findProyectoById(idProyecto); // Busca el proyecto por ID
        if (proyectoExistente == null) {
            return ResponseEntity.status(404).body("El proyecto con ID " + idProyecto + " no existe."); // Si el proyecto no existe, devuelve un error 404
        }
        empleadoService.eliminarProyecto(id, idProyecto); // Llama al servicio para eliminar el proyecto del empleado
        return ResponseEntity.ok("Proyecto eliminado correctamente del empleado."); // Devuelve un mensaje de éxito con código 200 (OK)
    }
}
