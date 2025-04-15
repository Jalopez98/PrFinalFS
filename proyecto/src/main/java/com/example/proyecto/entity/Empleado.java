package com.example.proyecto.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "EM_EMPLEADOS")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idEmpleado", scope = Empleado.class)
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EMPLEADO")
    private int idEmpleado;
    @NotBlank(message = "El DNI tiene que tener 8 numeros y una letra")
    @Pattern(regexp = "^[0-9]{8}[A-Za-z]$", message = "El NIF debe tener 8 dígitos seguidos de una letra")
    @Column(name = "TX_NIF", nullable = false, unique = true, length = 9)
    private String nif;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(name = "TX_NOMBRE", nullable = false, length = 50)
    private String nombre;
    @NotBlank(message = "El apellido no puede estar vacío")
    @Column(name = "TX_APELLIDO1", nullable = false, length = 50)
    private String apellido1;

    @Column(name = "TX_APELLIDO2", length = 50)
    private String apellido2;

    @Column(name = "F_NACIMIENTO", nullable = false)
    //@Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @NotBlank(message = "Añade al menos un número de teléfono")
    @Column(name = "N_TELEFONO1", nullable = false, length = 15)
    private String telefono1;

    @Column(name = "N_TELEFONO2", length = 15)
    private String telefono2;
    @NotBlank(message = "El campo email no puede estar vacío")
    @Column(name = "TX_EMAIL", nullable = false, unique = true, length = 100)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@(gmail|hotmail)\\.(com|es)$", message = "El correo electrónico debe ser @gmail.com, @gmail.es, @hotmail.com o @hotmail.es")
    private String email;
    @NotNull
    @Column(name = "F_ALTA", nullable = false)
    //@Temporal(TemporalType.DATE)
    private Date fechaAlta;

    @Column(name = "F_BAJA")
    //@Temporal(TemporalType.DATE)
    private Date fechaBaja;
    @NotNull(message = "Selecciona un estado civil")
    @Pattern(regexp = "^[SCD]$", message = "El estado civil debe ser S, C o D")
    @Column(name = "CX_EDOCIVIL", nullable = false)
    private String estadoCivil;
    @NotNull(message = "Indica si tienes formacion universitaria")
    @Pattern(regexp = "^[SN]$", message = "La formación universitaria debe ser S o N")
    @Column(name = "B_FORMACIONU", nullable = false, length = 1)
    private String formacionUniversitaria;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "PR_EMPLEADOS_PROYECTO",
            joinColumns = @JoinColumn(name = "ID_EMPLEADO"),
            inverseJoinColumns = @JoinColumn(name = "ID_PROYECTO")
    )
    //@JsonManagedReference
    private Set<Proyecto> proyectos;

    // Getters y Setters

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getFormacionUniversitaria() {
        return formacionUniversitaria;
    }

    public void setFormacionUniversitaria(String formacionUniversitaria) {
        this.formacionUniversitaria = formacionUniversitaria;
    }

    public Set<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(Set<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "idEmpleado=" + idEmpleado +
                ", nif='" + nif + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido1='" + apellido1 + '\'' +
                ", apellido2='" + apellido2 + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", telefono1='" + telefono1 + '\'' +
                ", telefono2='" + telefono2 + '\'' +
                ", email='" + email + '\'' +
                ", fechaAlta=" + fechaAlta +
                ", fechaBaja=" + fechaBaja +
                ", estadoCivil=" + estadoCivil +
                ", formacionUniversitaria=" + formacionUniversitaria +
                ", proyectos=" + proyectos +
                '}';
    }
}
