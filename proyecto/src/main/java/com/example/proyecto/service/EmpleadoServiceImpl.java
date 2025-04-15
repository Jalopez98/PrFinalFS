package com.example.proyecto.service;

import com.example.proyecto.entity.Empleado;
import com.example.proyecto.entity.EmpleadoProyecto;
import com.example.proyecto.entity.Proyecto;
import com.example.proyecto.exceptions.ExceptionPersonalizada;
import com.example.proyecto.repo.EmpleadoProyectoRepository;
import com.example.proyecto.repo.EmpleadoRepository;
import com.example.proyecto.repo.ProyectoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmpleadoServiceImpl implements EmpleadoService{

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private EmpleadoProyectoRepository empleadoProyectoRepository;

    public List<Empleado> obtenerUsuarios(){return empleadoRepository.findAll();}

    public Empleado findById(int id) {
        return empleadoRepository.findById(id).orElseThrow(() -> new ExceptionPersonalizada("Empleado no encontrado"));
    }

    @Override
    public List<Empleado> findAll() {
        return empleadoRepository.findAll();
    }

    public ResponseEntity <String> crearNuevoEmpleado(Empleado e){

        empleadoRepository.save(e);
        return ResponseEntity.ok("Usuario agregado correctamente");
    }

    public Proyecto findProyectoById(int id) {
        Optional<Proyecto> proyecto = proyectoRepository.findById(id);
        return proyecto.orElse(null);
    }


    public Empleado actualizarEmpleado(int id, Empleado empleadoDetalles) {
        Optional<Empleado> optionalEmpleado = empleadoRepository.findById(id);
        if (optionalEmpleado.isPresent()) {
            Empleado empleado = optionalEmpleado.get();
            empleado.setNif(empleadoDetalles.getNif());
            empleado.setNombre(empleadoDetalles.getNombre());
            empleado.setApellido1(empleadoDetalles.getApellido1());
            empleado.setApellido2(empleadoDetalles.getApellido2());
            empleado.setFechaNacimiento(empleadoDetalles.getFechaNacimiento());
            empleado.setTelefono1(empleadoDetalles.getTelefono1());
            empleado.setTelefono2(empleadoDetalles.getTelefono2());
            empleado.setEmail(empleadoDetalles.getEmail());
            empleado.setFechaAlta(empleadoDetalles.getFechaAlta());
            empleado.setFechaBaja(empleadoDetalles.getFechaBaja());
            empleado.setEstadoCivil(empleadoDetalles.getEstadoCivil());
            empleado.setFormacionUniversitaria(empleadoDetalles.getFormacionUniversitaria());
            empleado.setProyectos(empleadoDetalles.getProyectos());
            return empleadoRepository.save(empleado);
        } else {
            return null;
        }
    }

    public void eliminarEmpleado(int id) {
        Empleado empleado = findById(id);

        if (empleado.getProyectos() != null && !empleado.getProyectos().isEmpty()) {
            // Convertir PersistentSet a List
            List<Proyecto> proyectos = empleado.getProyectos().stream().collect(Collectors.toList());

            String proyectosAsString = proyectos.stream().map(Proyecto::getDescripcion).reduce("", (a, b) -> a + ", " + b);
            throw new ExceptionPersonalizada("No se puede dar de baja al empleado: " + empleado.getNombre() + " , " + empleado.getApellido1() + ". Porque está asignado a el/los proyectos: " + proyectosAsString);
        }

        // Asignar la fecha de baja
        empleado.setFechaBaja(new Date(Calendar.getInstance().getTime().getTime()));

        // Guardar los cambios en el repositorio
        empleadoRepository.save(empleado);
    }




    public String asignarProyecto(int empleadoId, int proyectoId) {
        Optional<Empleado> empleadoOpt = empleadoRepository.findById(empleadoId);
        Optional<Proyecto> proyectoOpt = proyectoRepository.findById(proyectoId);

        if (empleadoOpt.isPresent() && proyectoOpt.isPresent()) {


            EmpleadoProyecto empleadoProyecto = new EmpleadoProyecto();
            empleadoProyecto.setIdEmpleado(empleadoId);
            empleadoProyecto.setIdProyecto(proyectoId);
            empleadoProyecto.setFechaAlta(new Date(Calendar.getInstance().getTime().getTime()));

            empleadoProyectoRepository.save(empleadoProyecto);

            return String.format("Proyecto %d asociado correctamente al empleado %d", proyectoId, empleadoId);
        } else {
            throw new EntityNotFoundException("Empleado o Proyecto no encontrado");
        }
    }


    public Empleado eliminarProyecto(int empleadoId, int proyectoId) {
        Empleado empleado = findById(empleadoId);
        Proyecto proyecto = findProyectoById(proyectoId);
        if (empleado != null && proyecto != null) {
            empleado.getProyectos().remove(proyecto);
            return empleadoRepository.save(empleado);
        }
        return null;
    }

}



