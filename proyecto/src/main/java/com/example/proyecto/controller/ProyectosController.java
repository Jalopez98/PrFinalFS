package com.example.proyecto.controller;

import com.example.proyecto.entity.Empleado;
import com.example.proyecto.entity.Proyecto;
import com.example.proyecto.exceptions.ExceptionPersonalizada;
import com.example.proyecto.service.ProyectoServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/proyectos")
@Validated
public class ProyectosController {

    @Autowired
    private ProyectoServiceImpl servicio;

    @GetMapping
    public List<Proyecto> obtenerProyectos(){
        return servicio.obtenerP();
    }

    // Método POST para crear un nuevo proyecto
    @PostMapping("/nuevo")
    public ResponseEntity<String> crearProyecto(@Valid @RequestBody Proyecto proyecto) {
        try
        {
            Proyecto nuevoProyecto = servicio.save(proyecto);
            return ResponseEntity.ok("Proyecto creado con exito");
        }catch(ExceptionPersonalizada ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

    }

    // Método PUT para actualizar un proyecto existente
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarProyecto(@PathVariable int id, @Valid @RequestBody Proyecto proyecto) {
        try
        {
            Proyecto proyectoExistente = servicio.findById(id);
            if (proyectoExistente == null) {
                return ResponseEntity.notFound().build();
            }
            proyecto.setIdProyecto(id);
            Proyecto proyectoActualizado = servicio.save(proyecto);
            return ResponseEntity.ok("Proyecto actualizado con éxito");
        }catch(ExceptionPersonalizada ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

    }

    // Método DELETE para eliminar un proyecto existente
    @PutMapping("/darBaja/{id}")
    public ResponseEntity<?> bajaProyecto(@PathVariable int id) {
        Proyecto proyectoExistente = servicio.findById(id);
        if (proyectoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El proyecto con ID " + id + " no existe.");
        }
        if (!proyectoExistente.getEmpleados().isEmpty()) {
            // Obtener los nombres de los empleados asociados al proyecto
            List<String> nombresEmpleados = proyectoExistente.getEmpleados().stream()
                    .map(Empleado::getNombre)
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede eliminar el proyecto con ID " + id + " porque tiene empleados asociados: " + nombresEmpleados);
        }
        servicio.delete(id);
        return ResponseEntity.ok("Proyecto eliminado correctamente.");
    }


    @PutMapping("/{id}/asignar-empleado/{idUsuario}")
    public ResponseEntity<Proyecto> asignarEmpleado(@PathVariable int id, @PathVariable int idUsuario) {
        Proyecto proyectoExistente = servicio.findById(id);
        if (proyectoExistente == null) {
            return ResponseEntity.notFound().build();
        }
        proyectoExistente = servicio.asignarEmpleado(id, idUsuario);
        return ResponseEntity.ok(proyectoExistente);
    }



}
