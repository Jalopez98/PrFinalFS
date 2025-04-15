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
            ></v-list-item>
          </v-list>

          <v-divider></v-divider>

          <v-list density="compact" nav>
            <v-list-item prepend-icon="mdi-folder" title="Proyectos" value="proyectos"></v-list-item>
            <v-list-item prepend-icon="mdi-account-multiple" title="Empleados" @click="$router.push('/Empleados')"></v-list-item>
            <v-list-item prepend-icon="mdi-star" title="Alta Proyecto" @click="openAltaProyectoDialog"></v-list-item>
          </v-list>
        </v-navigation-drawer>

        <v-main>
          <h1 style="background-color: #028CF5">Proyectos</h1>
          <v-data-table
            :headers="headers"
            :items="proyectos"
            :items-per-page="5"
            class="elevation-1"
            style="background-color: #F3F7FB; color: black;"
          >
            <template v-slot:[`item.descripcion`]="{ item }">
              {{ item.descripcion }}
            </template>
            <template v-slot:[`item.fechaInicio`]="{ item }">
              {{ item.fechaInicio }}
            </template>
            <template v-slot:[`item.fechaFin`]="{ item }">
              {{ item.fechaFin }}
            </template>
            <template v-slot:[`item.lugar`]="{ item }">
              {{ item.lugar }}
            </template>
            <template v-slot:[`item.actions`]="{ item }">
              <v-btn class="mr-2" @click="openEditDialog(item)" color="#026FC1">Editar</v-btn>
              <v-btn class="mr-2" @click="deleteProyecto(item.idProyecto)" color="#ff7b5a">
                <v-icon right>mdi-close</v-icon>
              </v-btn>
              <v-btn @click="openAddEmployeesDialog(item)" color="#afff94">+</v-btn>
            </template>
          </v-data-table>

          <v-dialog v-model="altaProyectoDialog" max-width="800px">
            <v-card color="#D4F1FF">
              <v-card-title>
                <span class="headline">Alta de Proyecto</span>
              </v-card-title>
              <v-card-text>
                <v-form ref="formRef">
                  <v-container>
                    <v-row>
                      <v-col cols="12" md="6">
                        <v-text-field
                          v-model="nuevoProyecto.descripcion"
                          :error-messages="errors.descripcion"
                          label="Descripción"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field
                          v-model="nuevoProyecto.fechaInicio"
                          :error-messages="errors.fechaInicio"
                          label="Fecha Inicio"
                          type="date"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field
                          v-model="nuevoProyecto.fechaFin"
                          label="Fecha Fin"
                          type="date"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field
                          v-model="nuevoProyecto.lugar"
                          :error-messages="errors.lugar"
                          label="Ubicación"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field v-model="nuevoProyecto.observaciones" label="Observaciones" required></v-text-field>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeAltaProyectoDialog">Cancelar</v-btn>
                <v-btn color="blue darken-1" text @click="altaProyecto">Aceptar</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>

          <v-dialog v-model="editDialog" max-width="800px">
            <v-card color="#D4F1FF">
              <v-card-title>
                <span class="headline">Editar Proyecto</span>
              </v-card-title>
              <v-card-text>
                <v-form ref="formRef">
                  <v-container>
                    <v-row>
                      <v-col cols="12" md="6">
                        <v-text-field
                          v-model="selectedProyecto.descripcion"
                          :error-messages="errors.descripcion"
                          label="Descripción"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field
                          v-model="selectedProyecto.fechaInicio"
                          :error-messages="errors.fechaInicio"
                          label="Fecha Inicio"
                          type="date"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field
                          v-model="selectedProyecto.fechaFin"
                          :error-messages="errors.fechaFin"
                          label="Fecha Fin"
                          type="date"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field
                          v-model="selectedProyecto.lugar"
                          :error-messages="errors.lugar"
                          label="Ubicación"
                          required
                        ></v-text-field>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeEditDialog">Cancelar</v-btn>
                <v-btn color="blue darken-1" text @click="saveEditProyecto">Guardar</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>

          <v-dialog v-model="addEmployeesDialog" max-width="800px">
            <v-card color="#D4F1FF">
              <v-card-title>
                <span class="headline">Agregar Empleados al Proyecto: {{ selectedProyecto.descripcion }}</span>
              </v-card-title>
              <v-card-text>
                <v-form ref="formRef">
                  <v-container>
                    <v-row>
                      <v-col cols="12">
                        <v-select
                          v-model="selectedEmployees"
                          :items="empleados"
                          item-title="nombre"
                          item-value="idEmpleado"
                          label="Seleccionar Empleados"
                          multiple
                          chips
                          required
                        ></v-select>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeAddEmployeesDialog">Cancelar</v-btn>
                <v-btn color="blue darken-1" text @click="saveAddEmployees">Guardar</v-btn>
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
import { useEmpleadoStore } from './../stores/Empleados';
import { storeToRefs } from 'pinia';
import { ref, onMounted } from 'vue';

export default {
  name: 'ProyectosList',
  setup() {
    const empleadoStore = useEmpleadoStore();
    const { proyectos, empleados } = storeToRefs(empleadoStore);

    const snackbar = ref({
      visible: false,
      text: '',
      color: 'error', // O cualquier color predeterminado
      timeout: 5000, // O el tiempo que quieras que se muestre el snackbar
    });

    onMounted(() => {
      empleadoStore.fetchProyectos();
      empleadoStore.fetchEmpleados();
    });

    const headers = [
      { title: 'Proyecto', key: 'descripcion', align: 'start' },
      { title: 'Fecha Inicio', key: 'fechaInicio', align: 'start' },
      { title: 'Fecha Fin', key: 'fechaFin', align: 'start' },
      { title: 'Ubicación', key: 'lugar', align: 'start' },
      { title: 'Acciones', key: 'actions', align: 'center' },
    ];

    const altaProyectoDialog = ref(false);
    const editDialog = ref(false);
    const addEmployeesDialog = ref(false);
    const nuevoProyecto = ref({
      descripcion: '',
      fechaInicio: null,
      fechaFin: null,
      lugar: '',
    });
    const selectedProyecto = ref({
      idProyecto: null,
      descripcion: '',
      fechaInicio: '',
      fechaFin: '',
      lugar: '',
    });
    const selectedEmployees = ref([]);
    const errors = ref({});

    const openEditDialog = (proyecto) => {
      selectedProyecto.value = { ...proyecto };
      errors.value = {};
      editDialog.value = true;
    };

    const closeEditDialog = () => {
      editDialog.value = false;
    };

    const saveEditProyecto = async () => {
      errors.value = {};
      try {
        const isSuccess = await empleadoStore.editProyecto(selectedProyecto.value);
        if (isSuccess) {
          closeEditDialog();
          snackbar.value.text = 'Proyecto modificado con éxito';
          snackbar.value.color = 'green';
          snackbar.value.visible = true;
          empleadoStore.fetchProyectos();
        } else {
          if (empleadoStore.errors && Object.keys(empleadoStore.errors).length > 0) {
            for (let key in empleadoStore.errors) {
              errors.value[key] = empleadoStore.errors[key];
            }
          } else {
            alert('Hubo un problema al actualizar el proyecto. Verifique los detalles e intente nuevamente.');
          }
        }
      } catch (error) {
        console.error('Error al actualizar el proyecto:', error);
        alert('Hubo un error al procesar la solicitud. Intente nuevamente más tarde.');
      }
    };

    const openAddEmployeesDialog = async (proyecto) => {
      selectedProyecto.value = { ...proyecto };
      addEmployeesDialog.value = true;

      try {
        const empleadosAsignados = await empleadoStore.getEmpleadosProyecto(proyecto.idProyecto);
        selectedEmployees.value = empleadosAsignados.map((empleado) => empleado.idEmpleado);
      } catch (error) {
        console.error('Error al cargar empleados asignados:', error);
        alert('Hubo un error al cargar los empleados asignados al proyecto.');
      }
    };

    const closeAddEmployeesDialog = () => {
      addEmployeesDialog.value = false;
    };

    const saveAddEmployees = async () => {
      try {
        const proyectoId = selectedProyecto.value.idProyecto;

        // Obtener los empleados asignados actualmente al proyecto
        const empleadosAsignados = await empleadoStore.getEmpleadosProyecto(proyectoId);
        const empleadosAsignadosIds = empleadosAsignados.map((empleado) => empleado.idEmpleado);

        // Identificar los empleados que se han desmarcado
        const empleadosDesmarcados = empleadosAsignadosIds.filter(
          (id) => !selectedEmployees.value.includes(id)
        );

        // Eliminar los proyectos de los empleados desmarcados
        await Promise.all(
          empleadosDesmarcados.map(async (idEmpleado) => {
            await empleadoStore.eliminarProyectoEmpleado(idEmpleado, proyectoId);
          })
        );

        // Agregar los empleados seleccionados al proyecto
        const isSuccess = await empleadoStore.addEmployeesToProyecto(
          proyectoId,
          selectedEmployees.value
        );

        if (isSuccess) {
          closeAddEmployeesDialog();
          empleadoStore.fetchProyectos();
        } else {
          alert(
            'Hubo un problema al agregar o eliminar empleados del proyecto. Verifique los detalles e intente nuevamente.'
          );
        }
      } catch (error) {
        console.error('Error al agregar/eliminar empleados del proyecto:', error);
        alert('Hubo un error al procesar la solicitud. Intente nuevamente más tarde.');
      }
    };

    const deleteProyecto = async (idProyecto) => {
      try {
        const response = await empleadoStore.deleteProyecto(idProyecto);
        if (response.status === 200) {
          snackbar.value.text = 'Proyecto dado de baja correctamente';
          snackbar.value.color = 'green';
          snackbar.value.visible = true;
          empleadoStore.fetchProyectos();
        } else {
          alert(response.data);
        }
      } catch (error) {
        console.error('Error al eliminar el proyecto:', error);
          snackbar.value.text = 'Ha habido un error dando de baja al proyecto, inténtalo más tarde';
          snackbar.value.color = 'red';
          snackbar.value.visible = true;
      }
    };

    const openAltaProyectoDialog = () => {
      altaProyectoDialog.value = true;
    };

    const closeAltaProyectoDialog = () => {
      altaProyectoDialog.value = false;
      nuevoProyecto.value = {
        descripcion: '',
        fechaInicio: null,
        fechaFin: null,
        lugar: '',
      };
    };

  const altaProyecto = async () => {
  errors.value = {};
  try {
    const isSuccess = await empleadoStore.altaProyecto(nuevoProyecto.value);
    if (isSuccess) {
        snackbar.value.text = 'Proyecto dado de alta correctamente';
        snackbar.value.color = 'green';
        snackbar.value.visible = true;
      closeAltaProyectoDialog();
      empleadoStore.fetchProyectos();
    } else {
      if (empleadoStore.errors && Object.keys(empleadoStore.errors).length > 0) {
        alert('Es obligatorio introducir todos los datos para dar de alta un nuevo proyecto');
        for (let key in empleadoStore.errors) {
          errors.value[key] = empleadoStore.errors[key];
        }
      }
    }
  } catch (error) {
    console.error('Error al crear el proyecto:', error);
    alert('Hubo un error al procesar la solicitud. Intente nuevamente más tarde.');
  }
};


    return {
      proyectos,
      empleados,
      headers,
      altaProyectoDialog,
      editDialog,
      addEmployeesDialog,
      nuevoProyecto,
      selectedProyecto,
      selectedEmployees,
      errors,
      openEditDialog,
      closeEditDialog,
      saveEditProyecto,
      openAddEmployeesDialog,
      closeAddEmployeesDialog,
      saveAddEmployees,
      deleteProyecto,
      openAltaProyectoDialog,
      closeAltaProyectoDialog,
      altaProyecto,
      snackbar
    };
  },
};
</script>

<style>
.v-text-field .v-messages__message {
  color: red;
}
.v-select .v-messages__message {
  color: red;
}
</style>