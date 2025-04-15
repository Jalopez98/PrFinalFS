package com.example.proyecto.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "PR_PROYECTOS")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idProyecto", scope = Proyecto.class)
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROYECTO")
    private int idProyecto;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Column(name = "TX_DESCRIPCIÓN", nullable = false)
    private String descripcion;

    @NotNull(message = "La fecha de inicio no puede estar vacía")
    @Column(name = "F_INICIO", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    @Column(name = "F_FIN")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    @Column(name = "F_BAJA")
    @Temporal(TemporalType.DATE)
    private Date fechaBaja;

    @NotBlank(message = "El lugar no puede estar vacío")
    @Column(name = "TX_LUGAR", nullable = false)
    private String lugar;

    @Column(name = "TX_OBSERVACIONES")
    private String observaciones;


    @ManyToMany(mappedBy = "proyectos")
    @JsonIgnore
    private Set<Empleado> empleados;

    // Getters y Setters

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Set<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(Set<Empleado> empleados) {
        this.empleados = empleados;
    }

    @Override
    public String toString() {
        return "Proyecto{" +
                "idProyecto=" + idProyecto +
                ", descripcion='" + descripcion + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", fechaBaja=" + fechaBaja +
                ", lugar='" + lugar + '\'' +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}
