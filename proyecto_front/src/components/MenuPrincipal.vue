<template>
  <div class="d-flex align-center justify-center text-center fill-height">
    <v-container fluid>
      <v-row>
        <v-col cols="12">
          <v-toolbar-title class="reduce-margin">Proyecto gestión de empleados Future Space</v-toolbar-title>
        </v-col>
        <v-col cols="12" class="d-flex align-center justify-center">
          <img :src="myImage" alt="Future Space" style="max-width: 300px; margin-bottom: 20px;" />
        </v-col>
        <v-col cols="12" class="d-flex align-center justify-center text-center fill-height">
          <v-btn style="background-color: #001021; color: white;" @click="$router.push('/Empleados')">
            Acceder
            <v-tooltip activator="parent" location="bottom">
              Accede al sistema de gestión
            </v-tooltip>
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import myImage from '/src/assets/Castilla_La_Mancha-(negro).png'; 

export default {
  setup() {
    return {
      myImage,
    };
  },
};
</script>

<style>
/* Puedes añadir estilos adicionales aquí */
</style>