package com.example.proyecto.service;

import com.example.proyecto.entity.Empleado;
import com.example.proyecto.entity.Proyecto;
import com.example.proyecto.exceptions.ExceptionPersonalizada;
import com.example.proyecto.repo.EmpleadoRepository;
import com.example.proyecto.repo.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProyectoServiceImpl implements ProyectoService{

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;


    public List<Proyecto> obtenerP(){return proyectoRepository.findAll();}

    public Proyecto save(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    public Proyecto findById(int id) {
        Optional<Proyecto> proyecto = proyectoRepository.findById(id);
        return proyecto.orElse(null);
    }

    @Override
    public void delete(int id) {
        // Verificar si el proyecto está asociado a algún empleado
        List<Empleado> empleados = empleadoRepository.findAll();
        for (Empleado empleado : empleados) {
            if (empleado.getProyectos() != null) {
                for (Proyecto proyecto : empleado.getProyectos()) {
                    if (proyecto.getIdProyecto() == id) {
                        throw new ExceptionPersonalizada("No se puede dar de baja el proyecto. Está asociado al empleado: " + empleado.getNombre());
                    }
                }
            }
        }

        // Dar de baja el proyecto si no está asociado a ningún empleado
        Proyecto proyecto = proyectoRepository.findById(id).orElse(null);
        if (proyecto != null) {
            proyecto.setFechaBaja(new Date()); // Establecer la fecha de baja a la fecha actual
            proyectoRepository.save(proyecto); // Guardar el proyecto actualizado
        }
    }

    public Proyecto asignarEmpleado(int proyectoId, int empleadoId) {
        Proyecto proyecto = findById(proyectoId);
        if (proyecto == null) {
            return null;
        }
        Empleado empleado = empleadoRepository.findById(empleadoId).orElse(null);
        if (empleado == null) {
            return null;
        }
        proyecto.getEmpleados().add(empleado);
        return proyectoRepository.save(proyecto);
    }

}

