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
    @Autowired
    private EmpleadoRepository empleadosRepository;

    @Autowired
    private EmpleadoServiceImpl empleadoService;

    @GetMapping
    public List<Empleado> getEmpleados() {
        return empleadoService.obtenerUsuarios();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerEmpleadoPorId(@PathVariable int id) {
        Empleado buscado = empleadoService.findById(id);
        if (buscado == null) {
            return ResponseEntity.status(404).body("Empleado con id " +id+ " no encontrado");
        }
        return ResponseEntity.ok(buscado);
    }


    @PostMapping("/nuevo")
    public ResponseEntity<String> addEmpleado(@Valid @RequestBody Empleado empleado) {
        try
        {
            empleadoService.crearNuevoEmpleado(empleado);
            return ResponseEntity.ok("Empleado creado con éxito");
        }catch(ExceptionPersonalizada ex){
            //Aqui para ver el error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable int id, @Valid @RequestBody Empleado empleadoDetalles) {
        Empleado empleadoActualizado = empleadoService.actualizarEmpleado(id, empleadoDetalles);
        if (empleadoActualizado != null) {
            return ResponseEntity.ok(empleadoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/baja/{id}")
    public ResponseEntity<?> eliminarEmpleado(@PathVariable int id) {
        try {
            empleadoService.eliminarEmpleado(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (ExceptionPersonalizada e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{empleadoId}/proyectos/{proyectoId}")
    public String asignarProyecto(@PathVariable int empleadoId, @PathVariable int proyectoId) {

        return empleadoService.asignarProyecto(empleadoId, proyectoId);
    }

    @DeleteMapping("/{id}/eliminar-proyecto/{idProyecto}")
    public ResponseEntity<String> eliminarProyecto(@PathVariable int id, @PathVariable int idProyecto) {
        Empleado empleadoExistente = empleadoService.findById(id);
        if (empleadoExistente == null) {
            return ResponseEntity.status(404).body("El empleado con ID " + id + " no existe.");
        }
        Proyecto proyectoExistente = empleadoService.findProyectoById(idProyecto);
        if (proyectoExistente == null) {
            return ResponseEntity.status(404).body("El proyecto con ID " + idProyecto + " no existe.");
        }
        empleadoService.eliminarProyecto(id, idProyecto);
        return ResponseEntity.ok("Proyecto eliminado correctamente del empleado.");
    }





}
