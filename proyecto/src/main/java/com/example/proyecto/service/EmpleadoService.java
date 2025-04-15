package com.example.proyecto.service;

import com.example.proyecto.entity.Empleado;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmpleadoService {
    ResponseEntity<String> crearNuevoEmpleado(Empleado empleado);
    Empleado findById(int id);
    List<Empleado> findAll();
    Empleado actualizarEmpleado(int id, Empleado empleado);
    void eliminarEmpleado(int id);
    String asignarProyecto(int empleadoId, int proyectoId);


}
