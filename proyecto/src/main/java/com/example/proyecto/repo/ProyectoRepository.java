package com.example.proyecto.repo;

import com.example.proyecto.entity.Empleado;
import com.example.proyecto.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {

    @Query(value = "select * from pr_proyectos where F_BAJA is null",nativeQuery = true)
    List<Proyecto> findAll();

}


