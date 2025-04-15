package com.example.proyecto.entity;

import java.io.Serializable;
import java.util.Objects;

public class EmpleadoProyectoId implements Serializable {
    private int idProyecto;
    private int idEmpleado;

    public EmpleadoProyectoId() {}

    public EmpleadoProyectoId(int idProyecto, int idEmpleado) {
        this.idProyecto = idProyecto;
        this.idEmpleado = idEmpleado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmpleadoProyectoId that = (EmpleadoProyectoId) o;
        return idProyecto == that.idProyecto && idEmpleado == that.idEmpleado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProyecto, idEmpleado);
    }
}
