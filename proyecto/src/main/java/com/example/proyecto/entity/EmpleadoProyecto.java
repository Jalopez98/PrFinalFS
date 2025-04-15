package com.example.proyecto.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PR_EMPLEADOS_PROYECTO")
@IdClass(EmpleadoProyectoId.class)
public class EmpleadoProyecto {
    @Id
    @Column(name = "ID_PROYECTO")
    private int idProyecto;

    @Id
    @Column(name = "ID_EMPLEADO")
    private int idEmpleado;

    @Column(name = "F_ALTA")
    private Date fechaAlta;


    // Getters y Setters

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    @Override
    public String toString() {
        return "EmpleadoProyecto{" +
                "idProyecto=" + idProyecto +
                ", idEmpleado=" + idEmpleado +
                ", fechaAlta=" + fechaAlta +
                '}';
    }
}

