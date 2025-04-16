<template>
  <v-container>
    <v-card>
      <v-layout>
        <v-navigation-drawer color="#028CF5" expand-on-hover rail>
          <v-list>
            <v-list-item
              prepend-avatar="https://randomuser.me/api/portraits/women/85.jpg"
              subtitle="User@gmailcom"
              title="User"
              color="black"
            ></v-list-item>
          </v-list>

          <v-divider></v-divider>

          <v-list density="compact" nav>
            <v-list-item prepend-icon="mdi-account-multiple" title="Empleados" color="black"></v-list-item>
            <v-list-item prepend-icon="mdi-folder" title="Proyectos" @click="$router.push('/Proyectos')" color="black"></v-list-item>
            <v-list-item prepend-icon="mdi-star" title="Alta empleado" @click="openAltaEmpleadoDialog" color="black"></v-list-item>
          </v-list>
        </v-navigation-drawer>

        <v-main>
          <h1 style="background-color: #028CF5">Empleados</h1>
          <v-data-table
            :headers="headers"
            :items="empleados"
            :items-per-page="5"
            class="elevation-1"
            style="background-color: #F3F7FB; color: black;"
          >
            <template v-slot:[`item.nif`]="{ item }">
              {{ item.nif }}
            </template>
            <template v-slot:[`item.nombre`]="{ item }">
              {{ item.nombre }}
            </template>
            <template v-slot:[`item.apellidos`]="{ item }">
              {{ item.apellido1 }} {{ item.apellido2 }}
            </template>
            <template v-slot:[`item.fechaNacimiento`]="{ item }">
              {{ item.fechaNacimiento }}
            </template>
            <template v-slot:[`item.telefono1`]="{ item }">
              {{ item.telefono1 }}
            </template>
            <template v-slot:[`item.email`]="{ item }">
              {{ item.email }}
            </template>
            <template v-slot:[`item.estadoCivil`]="{ item }">
              {{ item.estadoCivil }}
            </template>
            <template v-slot:[`item.formacionUniversitaria`]="{ item }">
              {{ item.formacionUniversitaria }}
            </template>
            <template v-slot:[`item.actions`]="{ item }">
              <v-btn class="mr-2" @click="openEditDialog(item)" color="#7c9ecc">Editar</v-btn>
              <v-btn class="mr-2" @click="deleteEmpleado(item.idEmpleado)" color="#ff7b5a">
                <v-icon right>mdi-close</v-icon>
              </v-btn>
              <v-btn @click="showProjects(item.idEmpleado)" color="#f0e68c">Proyectos</v-btn>
            </template>
          </v-data-table>

          <v-dialog v-model="altaEmpleadoDialog" max-width="800px">
            <v-card color="#D4F1FF">
              <v-card-title>
                <span class="headline">Alta de Empleado</span>
              </v-card-title>
              <v-card-text>
                <v-form ref="formRef">
                  <v-container>
                    <v-row>
                      <v-col cols="12" md="6">
                        <v-text-field v-model="nuevoEmpleado.nombre" label="Nombre" required></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field v-model="nuevoEmpleado.apellido1" label="Apellido 1" required></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field v-model="nuevoEmpleado.apellido2" label="Apellido 2"></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field v-model="nuevoEmpleado.nif" label="NIF" required></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field v-model="nuevoEmpleado.email" label="Email" required></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field v-model="nuevoEmpleado.telefono1" label="Teléfono 1" required></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field v-model="nuevoEmpleado.telefono2" label="Teléfono 2"></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-select v-model="nuevoEmpleado.estadoCivil" :items="estadoCivilOptions" label="Estado Civil" required></v-select>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-select v-model="nuevoEmpleado.formacionUniversitaria" :items="formacionUniversitariaOptions" label="Formación Universitaria" required></v-select>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field
                          v-model="nuevoEmpleado.fechaNacimiento"
                          label="Fecha de Nacimiento"
                          type="date"
                          :max="fechaMinimaPermitida"
                          required
                          :error-messages="fechaNacimientoError"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field v-model="nuevoEmpleado.fechaAlta" label="Fecha de Alta" type="date" required></v-text-field>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeAltaEmpleadoDialog">Cancelar</v-btn>
                <v-btn color="blue darken-1" text @click="altaEmpleado">Aceptar</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>

          <v-dialog v-model="editDialog" max-width="800px">
            <v-card color="#D4F1FF">
              <v-card-title>
                <span class="headline">Editar Empleado</span>
              </v-card-title>
              <v-card-text>
                <v-form ref="formRef">
                  <v-container>
                    <v-row>
                      <v-col cols="12" md="6">
                        <v-text-field v-model="selectedEmpleado.nombre" :error-messages="errors.nombre" label="Nombre" required></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field v-model="selectedEmpleado.apellido1" :error-messages="errors.apellido1" label="Apellido 1" required></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field v-model="selectedEmpleado.apellido2" :error-messages="errors.apellido2" label="Apellido 2"></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field v-model="selectedEmpleado.nif" :error-messages="errors.nif" label="NIF" required></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field v-model="selectedEmpleado.email" :error-messages="errors.email" label="Email" required></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field v-model="selectedEmpleado.telefono1" :error-messages="errors.telefono1" label="Teléfono 1" required></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field v-model="selectedEmpleado.telefono2" :error-messages="errors.telefono2" label="Teléfono 2"></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-select v-model="selectedEmpleado.estadoCivil" :items="estadoCivilOptions" :error-messages="errors.estadoCivil" label="Estado Civil" required></v-select>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-select v-model="selectedEmpleado.formacionUniversitaria" :items="formacionUniversitariaOptions" :error-messages="errors.formacionUniversitaria" label="Formación Universitaria" required></v-select>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeEditDialog">Cancelar</v-btn>
                <v-btn color="blue darken-1" text @click="saveEditEmpleado">Guardar</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>

          <v-dialog v-model="viewDialog" max-width="800px">
            <v-card color="#D4F1FF">
              <v-card-title>
                <span class="headline">Proyectos de {{ selectedEmpleadoName }}</span>
              </v-card-title>
              <v-card-text>
                <v-list v-if="proyectosEmpleado.length > 0">
                  <v-list-item v-for="proyecto in proyectosEmpleado" :key="proyecto.idProyecto">
                    <v-list-item-content>
                      <v-list-item-title>{{ proyecto.descripcion }}</v-list-item-title>
                      <v-list-item-subtitle>{{ proyecto.fechaInicio }} - {{ proyecto.fechaFin }}</v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
                <div v-else>
                  <p>Este empleado no tiene proyectos asignados.</p>
                </div>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeViewDialog">Cerrar</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>

          <v-snackbar v-model="snackbar.visible" :color="snackbar.color" :timeout="snackbar.timeout">
            {{ snackbar.text }}
            <template v-slot:actions>
              <v-btn color="white" text @click="snackbar.visible = false">Cerrar</v-btn>
            </template>
          </v-snackbar>
        </v-main>
      </v-layout>
    </v-card>
  </v-container>
</template>

<script>
import { useEmpleadoStore } from './../stores/Empleados'; // Importa el store de Pinia para empleados
import { storeToRefs } from 'pinia'; // Importa storeToRefs para extraer propiedades reactivas del store
import { useRouter } from 'vue-router'; // Importa useRouter para la navegación
import { ref } from 'vue'; // Importa ref para crear referencias reactivas

export default {
  name: 'EmpleadoList', // Nombre del componente
  setup() {
    const empleadoStore = useEmpleadoStore(); // Obtiene el store de Pinia para empleados
    const { empleados } = storeToRefs(empleadoStore); // Extrae la lista de empleados del store
    const router = useRouter(); // Obtiene el enrutador

    empleadoStore.fetchEmpleados(); // Obtiene la lista de empleados del backend

    const headers = [
      { title: 'DNI', key: 'nif', align: 'start' },
      { title: 'Nombre', key: 'nombre', align: 'start' },
      { title: 'Apellidos', key: 'apellidos', align: 'start' },
      { title: 'Fecha nacimiento', key: 'fechaNacimiento', align: 'start' },
      { title: 'Telefono', key: 'telefono1', align: 'start' },
      { title: 'Email', key: 'email', align: 'start' },
      { title: 'Estado civil', key: 'estadoCivil', align: 'start' },
      { title: 'Formación universitaria', key: 'formacionUniversitaria', align: 'start' },
      { title: 'Acciones', key: 'actions', align: 'center' },
    ]; // Encabezados para la tabla de datos

    const estadoCivilOptions = ['S', 'C']; // Opciones para el estado civil
    const formacionUniversitariaOptions = ['S', 'N']; // Opciones para la formación universitaria

    const editDialog = ref(false); // Ref para controlar el diálogo de edición
    const viewDialog = ref(false); // Ref para controlar el diálogo de proyectos
    const selectedEmpleado = ref({
      idEmpleado: null,
      nombre: '',
      apellido1: '',
      apellido2: '',
      nif: '',
      email: '',
      telefono1: '',
      telefono2: '',
      estadoCivil: '',
      formacionUniversitaria: '',
      proyectos: [],
    }); // Ref para el empleado seleccionado para editar

    const selectedEmpleadoName = ref(''); // Ref para el nombre del empleado seleccionado
    const proyectosEmpleado = ref([]); // Ref para los proyectos del empleado
    const errors = ref({}); // Ref para almacenar los errores de validación

    const snackbar = ref({
      visible: false,
      text: '',
      color: 'red',
      timeout: 5000,
    }); // Ref para controlar el snackbar

    const altaEmpleadoDialog = ref(false); // Ref para controlar el diálogo de alta de empleado
    const nuevoEmpleado = ref({
      nombre: '',
      apellido1: '',
      apellido2: '',
      nif: '',
      email: '',
      telefono1: '',
      telefono2: '',
      estadoCivil: '',
      formacionUniversitaria: '',
      fechaNacimiento: null,
      fechaAlta: null,
      fechaBaja: null,
    }); // Ref para el nuevo empleado

    const fechaMinimaPermitida = ref(calcularFechaMinimaPermitida()); // Ref para la fecha mínima permitida
    const fechaNacimientoError = ref(''); // Ref para el mensaje de error de la fecha de nacimiento

    function calcularFechaMinimaPermitida() {
      const fechaActual = new Date();
      const fechaMinima = new Date(
        fechaActual.getFullYear() - 18,
        fechaActual.getMonth(),
        fechaActual.getDate()
      );
      return fechaMinima.toISOString().split('T')[0];
    } // Función para calcular la fecha mínima permitida

    const openEditDialog = (empleado) => {
      selectedEmpleado.value = { ...empleado };
      errors.value = {};
      editDialog.value = true;
    }; // Función para abrir el diálogo de edición

    const closeEditDialog = () => {
      editDialog.value = false;
    }; // Función para cerrar el diálogo de edición

    const saveEditEmpleado = async () => {
      errors.value = {};

      try {
        const isSuccess = await empleadoStore.editEmpleado(selectedEmpleado.value);

        if (isSuccess) {
          snackbar.value.text = 'Empleado modificado correctamente';
          snackbar.value.color = 'green';
          snackbar.value.visible = true;
          closeEditDialog();
        } else {
          if (empleadoStore.errors && Object.keys(empleadoStore.errors).length > 0) {
            for (let key in empleadoStore.errors) {
              errors.value[key] = empleadoStore.errors[key];
            }
          } else {
            alert('Hubo un problema al actualizar el empleado. Verifique los detalles e intente nuevamente.');
          }
        }
      } catch (error) {
        console.error('Error al actualizar el empleado:', error);
        alert('Hubo un error al procesar la solicitud. Intente nuevamente más tarde.');
      }
    }; // Función para guardar los cambios del empleado editado

    const showProjects = async (idEmpleado) => {
      try {
        await empleadoStore.obtenerProyectosEmpleado(idEmpleado);
        proyectosEmpleado.value = empleadoStore.proyectosEmpleado;
        selectedEmpleadoName.value = empleados.value.find(emp => emp.idEmpleado === idEmpleado).nombre;
        viewDialog.value = true;
      } catch (error) {
        console.error('Error al obtener proyectos del empleado:', error);
      }
    }; // Función para mostrar los proyectos del empleado

    const closeViewDialog = () => {
      viewDialog.value = false;
    }; // Función para cerrar el diálogo de proyectos

    const deleteEmpleado = async (idEmpleado) => {
      try {
        await empleadoStore.deleteEmpleado(idEmpleado);
        snackbar.value.text = 'Empleado dado de baja';
        snackbar.value.color = 'green';
        snackbar.value.visible = true;
      } catch (error) {
        if (error.response && error.response.data) {
          snackbar.value.text = error.response.data;
        } else {
          snackbar.value.text = 'Error al dar de baja el empleado. Inténtelo de nuevo.';
          console.error('Error en la solicitud:', error);
        }
        snackbar.value.visible = true;
      }
    }; // Función para dar de baja un empleado

    const openAltaEmpleadoDialog = () => {
      altaEmpleadoDialog.value = true;
    }; // Función para abrir el diálogo de alta de empleado

    const closeAltaEmpleadoDialog = () => {
      altaEmpleadoDialog.value = false;
      nuevoEmpleado.value = {
        nombre: '',
        apellido1: '',
        apellido2: '',
        nif: '',
        email: '',
        telefono1: '',
        telefono2: '',
        estadoCivil: '',
        formacionUniversitaria: '',
        fechaNacimiento: null,
        fechaAlta: null,
        fechaBaja: null,
      };
    }; // Función para cerrar el diálogo de alta de empleado

    const altaEmpleado = async () => {
      if (nuevoEmpleado.value.fechaNacimiento > fechaMinimaPermitida.value) {
        fechaNacimientoError.value = 'El empleado debe tener 18 años o más.';
        return;
      }
      fechaNacimientoError.value = '';
      try {
        await empleadoStore.altaEmpleado(nuevoEmpleado.value);
        snackbar.value.text = 'Empleado dado de alta correctamente';
        snackbar.value.color = 'green';
        snackbar.value.visible = true;
        closeAltaEmpleadoDialog();
      } catch (error) {
        if (error.response && error.response.data) {
          alert('Es obligatorio introducir todos los datos para dar de alta un nuevo empleado');
          snackbar.value.text = error.response.data;
        } else {
          snackbar.value.text = 'Error al dar de alta el empleado. Inténtelo de nuevo.';
          console.error('Error en la solicitud:', error);
        }
        snackbar.value.visible = true;
      }
    }; // Función para dar de alta un empleado

    return {
      empleados,
      headers,
      estadoCivilOptions,
      formacionUniversitariaOptions,
      editDialog,
      viewDialog,
      selectedEmpleado,
      proyectosEmpleado,
      selectedEmpleadoName,
      errors,
      snackbar,
      altaEmpleadoDialog,
      nuevoEmpleado,
      openEditDialog,
      closeEditDialog,
      saveEditEmpleado,
      showProjects,
      closeViewDialog,
      deleteEmpleado,
      openAltaEmpleadoDialog,
      closeAltaEmpleadoDialog,
      altaEmpleado,
      fechaMinimaPermitida,
      fechaNacimientoError,
    }; // Retorna las propiedades y funciones para que estén disponibles en el template
  },
};
</script>

<style>
.v-text-field .v-messages__message,
.v-select .v-messages__message {
  color: red;
} 
</style>