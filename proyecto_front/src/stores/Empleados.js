import { defineStore } from 'pinia';
import axios from 'axios';

export const useEmpleadoStore = defineStore('empleado', {
  state: () => ({
    empleados: [], // Array para almacenar la lista de empleados
    proyectos: [], // Array para almacenar la lista de proyectos
    errors: {},    // Objeto para almacenar errores de las solicitudes al backend
  }),
  actions: {
    async fetchEmpleados() {
      // Obtiene la lista de empleados desde el backend
      try {
        const response = await axios.get('http://localhost:8081/empleados');
        this.empleados = response.data; // Almacena la respuesta en el estado
      } catch (error) {
        console.error('Error fetching empleados:', error); // Maneja errores
      }
    },
    async altaEmpleado(empleado) {
      // Crea un nuevo empleado en el backend
      try {
        await axios.post('http://localhost:8081/empleados/nuevo', empleado);
        this.fetchEmpleados(); // Actualiza la lista de empleados después de la creación
      } catch (error) {
        throw error; // Reenvía el error para que sea manejado por el componente
      }
    },
    async editEmpleado(empleado) {
      // Actualiza un empleado existente en el backend
      try {
        const response = await axios.put(`http://localhost:8081/empleados/${empleado.idEmpleado}`, empleado, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
        this.fetchEmpleados(); // Actualiza la lista de empleados después de la actualización
        return true; // Indica que la operación fue exitosa
      } catch (error) {
        if (error.response && error.response.data) {
          this.errors = error.response.data; // Almacena los errores del backend en el estado
        } else {
          console.error('Error inesperado:', error.message);
          this.errors = { general: 'Hubo un error al procesar la solicitud.' };
        }
        return false; // Indica que la operación falló
      }
    },
    async deleteEmpleado(idEmpleado) {
      // Da de baja a un empleado en el backend
      try {
        await axios.put(`http://localhost:8081/empleados/baja/${idEmpleado}`);
        this.fetchEmpleados(); // Actualiza la lista de empleados después de la eliminación
      } catch (error) {
        throw error; // Reenvía el error para que sea manejado por el componente
      }
    },
    async fetchProyectos() {
      // Obtiene la lista de proyectos desde el backend
      try {
        const response = await axios.get('http://localhost:8081/proyectos');
        this.proyectos = response.data; // Almacena la respuesta en el estado
      } catch (error) {
        console.error('Error inesperado:', error.message); // Maneja errores
      }
    },
    async editProyecto(proyecto) {
      // Actualiza un proyecto existente en el backend
      try {
        const response = await axios.put(`http://localhost:8081/proyectos/${proyecto.idProyecto}`, proyecto, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
        this.fetchProyectos(); // Actualiza la lista de proyectos después de la actualización
        return true; // Indica que la operación fue exitosa
      } catch (error) {
        if (error.response && error.response.data) {
          this.errors = error.response.data; // Almacena los errores del backend en el estado
        } else {
          console.error('Error inesperado:', error.message);
          this.errors = { general: 'Hubo un error al procesar la solicitud.' };
        }
        return false; // Indica que la operación falló
      }
    },
    async altaProyecto(proyecto) {
      // Crea un nuevo proyecto en el backend
      try {
        await axios.post('http://localhost:8081/proyectos/nuevo', proyecto);
        this.errors = {}; // Limpia los errores si la solicitud es exitosa
        return true; // Indica que la operación fue exitosa
      } catch (error) {
        if (error.response && error.response.data) {
          this.errors = error.response.data; // Almacena los errores del backend en el estado
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
        return false; // Indica que la operación falló
      }
    },
    async deleteProyecto(idProyecto) {
      // Da de baja un proyecto en el backend
      try {
        const response = await axios.put(`http://localhost:8081/proyectos/darBaja/${idProyecto}`);
        console.log('Proyecto dado de baja');
        return response; // Retorna la respuesta del backend
      } catch (error) {
        console.error('Error al dar de baja el proyecto:', error);
        if (error.response) {
          return error.response; // Retorna la respuesta del backend en caso de error
        } else {
          throw error; // Reenvía el error para que sea manejado por el componente
        }
      }
    },
    async addEmployeesToProyecto(idProyecto, empleadosIds) {
      // Asigna empleados a un proyecto en el backend
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
        return true; // Indica que la operación fue exitosa
      } catch (error) {
        console.error('Error al agregar empleados al proyecto:', error);
        return false; // Indica que la operación falló
      }
    },
    async obtenerProyectosEmpleado(idEmpleado) {
      // Obtiene los proyectos de un empleado desde el backend
      try {
        const response = await axios.get(`http://localhost:8081/empleados/${idEmpleado}`);
        this.proyectosEmpleado = response.data.proyectos; // Almacena la respuesta en el estado
      } catch (error) {
        console.error('Error al obtener los proyectos del empleado:', error); // Maneja errores
      }
    },
    async getEmpleadosProyecto(idProyecto) {
      // Obtiene los empleados de un proyecto desde el backend
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

        return empleadosProyecto; // Retorna la lista de empleados del proyecto
      } catch (error) {
        console.error('Error al obtener empleados del proyecto:', error);
        throw error; // Reenvía el error para que sea manejado por el componente
      }
    },
    async eliminarProyectoEmpleado(idEmpleado, idProyecto) {
      // Elimina un proyecto de un empleado en el backend
      try {
        await axios.delete(
          `http://localhost:8081/empleados/${idEmpleado}/eliminar-proyecto/${idProyecto}`
        );
      } catch (error) {
        console.error('Error al eliminar proyecto del empleado:', error);
        throw error; // Reenvía el error para que sea manejado por el componente
      }
    },
  },
});