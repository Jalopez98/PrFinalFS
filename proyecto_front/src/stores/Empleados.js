import { defineStore } from 'pinia';
import axios from 'axios';

export const useEmpleadoStore = defineStore('empleado', {
  state: () => ({
    empleados: [],
    proyectos: [],
    errors: {},
  }),
  actions: {
    async fetchEmpleados() {
      try {
        const response = await axios.get('http://localhost:8081/empleados');
        this.empleados = response.data;
      } catch (error) {
        console.error('Error fetching empleados:', error);
      }
    },
    async altaEmpleado(empleado) {
      try {
        await axios.post('http://localhost:8081/empleados/nuevo', empleado);
        this.fetchEmpleados();
      } catch (error) {
        throw error;
      }
    },
    async editEmpleado(empleado) {
      try {
        const response = await axios.put(`http://localhost:8081/empleados/${empleado.idEmpleado}`, empleado, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
        this.fetchEmpleados();
        return true;
      } catch (error) {
        if (error.response && error.response.data) {
          this.errors = error.response.data;
        } else {
          console.error('Error inesperado:', error.message);
          this.errors = { general: 'Hubo un error al procesar la solicitud.' };
        }
        return false;
      }
    },
    async deleteEmpleado(idEmpleado) {
      try {
        await axios.put(`http://localhost:8081/empleados/baja/${idEmpleado}`);
        this.fetchEmpleados();
      } catch (error) {
        throw error;
      }
    },
    async fetchProyectos() {
      try {
        const response = await axios.get('http://localhost:8081/proyectos');
        this.proyectos = response.data;
      } catch (error) {
        console.error('Error inesperado:', error.message);
      }
    },
    async editProyecto(proyecto) {
      try {
        const response = await axios.put(`http://localhost:8081/proyectos/${proyecto.idProyecto}`, proyecto, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
        this.fetchProyectos();
        return true;
      } catch (error) {
        if (error.response && error.response.data) {
          this.errors = error.response.data;
        } else {
          console.error('Error inesperado:', error.message);
          this.errors = { general: 'Hubo un error al procesar la solicitud.' };
        }
        return false;
      }
    },
    async altaProyecto(proyecto) {
      try {
        await axios.post('http://localhost:8081/proyectos/nuevo', proyecto);
        this.errors = {}; // Limpiar errores si la solicitud es exitosa
        return true;
      } catch (error) {
        if (error.response && error.response.data) {
          this.errors = error.response.data;
          let errorMessage = '';
          for (const key in this.errors) {
            errorMessage += `${key}: ${this.errors[key]}\n`;
          }
          //alert(`Error, completa los campos obligatorios:\n${errorMessage}`);
        } else {
          console.error('Error inesperado:', error.message);
          this.errors = { general: 'Hubo un error al procesar la solicitud.' };
          alert('Hubo un error al procesar la solicitud.');
        }
        return false;
      }
    },
    
    async deleteProyecto(idProyecto) {
      try {
        const response = await axios.put(`http://localhost:8081/proyectos/darBaja/${idProyecto}`);
        console.log('Proyecto dado de baja');
        return response;
      } catch (error) {
        console.error('Error al dar de baja el proyecto:', error);
        if (error.response) {
          return error.response;
        } else {
          throw error;
        }
      }
    },

    async addEmployeesToProyecto(idProyecto, empleadosIds) {
  try {
    // Obtener los proyectos
    const response = await axios.get('http://localhost:8081/proyectos');
    const proyectos = response.data;

    // Obtener los empleados
    const response1 = await axios.get('http://localhost:8081/empleados');
    const empleados = response1.data;

    // Buscar la descripción del proyecto
    const proyecto = proyectos.find(p => p.idProyecto === idProyecto);
    const descripcionProyecto = proyecto ? proyecto.descripcion : 'Desconocido';

    // Buscar los nombres de los empleados
    const nombresEmpleados = empleadosIds.map(idEmpleado => {
      const empleado = empleados.find(e => e.idEmpleado === idEmpleado);
      return empleado ? empleado.nombre : 'Desconocido';
    });

    // Asignar empleados al proyecto
    const requests = empleadosIds.map(idEmpleado =>
      axios.post(`http://localhost:8081/empleados/${idEmpleado}/proyectos/${idProyecto}`)
    );
    await Promise.all(requests);

    // Mostrar la descripción del proyecto y los nombres de los empleados en el alert
    alert('Empleado/s asignado/s y/o desasignados a proyecto: ' + descripcionProyecto + '\nEmpleados activos en este proyecto: ' + nombresEmpleados.join(', '));
    return true;
  } catch (error) {
    console.error('Error al agregar empleados al proyecto:', error);
    return false;
  }
},

    
    
    async obtenerProyectosEmpleado(idEmpleado) {
      try {
        const response = await axios.get(`http://localhost:8081/empleados/${idEmpleado}`);
        this.proyectosEmpleado = response.data.proyectos;
      } catch (error) {
        console.error('Error al obtener los proyectos del empleado:', error);
      }
    },
    async getEmpleadosProyecto(idProyecto) {
      try {
        const response = await axios.get('http://localhost:8081/empleados');
        const empleados = response.data;

        // Realizar solicitudes individuales para cada empleado
        const empleadosConProyectos = await Promise.all(
          empleados.map(async (empleado) => {
            const empleadoResponse = await axios.get(`http://localhost:8081/empleados/${empleado.idEmpleado}`);
            return empleadoResponse.data;
          })
        );

        // Filtrar los empleados que tienen el proyecto especificado
        const empleadosProyecto = empleadosConProyectos.filter(
          (empleado) => empleado.proyectos && empleado.proyectos.some((proyecto) => proyecto.idProyecto === idProyecto)
        );

        return empleadosProyecto;
      } catch (error) {
        console.error('Error al obtener empleados del proyecto:', error);
        throw error;
      }
    },
    async eliminarProyectoEmpleado(idEmpleado, idProyecto) {
      try {
        await axios.delete(
          `http://localhost:8081/empleados/${idEmpleado}/eliminar-proyecto/${idProyecto}`
        );
      } catch (error) {
        console.error('Error al eliminar proyecto del empleado:', error);
        throw error;
      }
    },
  },
});