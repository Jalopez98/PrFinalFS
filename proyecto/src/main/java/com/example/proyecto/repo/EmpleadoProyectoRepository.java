package com.example.proyecto.repo;

import com.example.proyecto.entity.EmpleadoProyecto;
import com.example.proyecto.entity.EmpleadoProyectoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoProyectoRepository extends JpaRepository<EmpleadoProyecto, EmpleadoProyectoId> {
}

