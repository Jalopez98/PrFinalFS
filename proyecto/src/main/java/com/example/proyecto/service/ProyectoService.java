package com.example.proyecto.service;

import com.example.proyecto.entity.Proyecto;

import java.util.List;

public interface ProyectoService {

    List<Proyecto> obtenerP();

    Proyecto save(Proyecto proyecto);

    Proyecto findById(int id);

    void delete(int id);

    Proyecto asignarEmpleado(int proyectoId, int empleadoId);


}
