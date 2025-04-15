package com.example.proyecto.repo;

import com.example.proyecto.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    @Query(value = "select * from em_empleados where F_BAJA is null",nativeQuery = true)
    List<Empleado> findAll();

}

