<template>
  <div>
    <h1>Delete a Project</h1>
    <div class="formContainer" id="viewProjects">
      
        <div v-if="errorDeleteProject" class="text-center" style="color: red">
          {{ errorDeleteProject }}
        </div>

        <div v-else>
        <b-table
            :items="items"
            :fields="fields"
            :outlined="true"
            :key="this.items.length"
          >
            <template #cell(deletion)="row">
              <b-button
                size="sm"
                v-on:click="deleteProject(row.item.Project)"
                class="mr-2"
                variant="danger"
              >
                Delete Project
              </b-button>
            </template>
        </b-table>
        </div>

    </div>
  </div>
</template>





<script>

import axios from "axios";
import { LOCALHOST_BACKEND, DELETE_PROJECT_ENDPOINT, GET_ALL_PROJECTS_ENDPOINT } from "../constants/constants";


export default {
  data() {
    return {
      errorDeleteProject: "",
      projects: [],
      fields: ["Project", "deletion"],
      items: []
    };
  },

  //fetch all of this user's projects
  created: function() {
    this.getProjects();
  },

  methods: {
    
    deleteProject(projectName) {
      axios.delete(LOCALHOST_BACKEND + DELETE_PROJECT_ENDPOINT + projectName, 
      {
        headers: {
          token: "Bearer " + this.$root.$data.token
        }
      })
        .then(response => {
          this.items = [];
          this.getProjects();
          console.log(response);
        })
        .catch(error => {
            this.errorDeleteProject = error.response.data
            console.log(error)
        });
    },

    
    getProjects() {
      axios.get(LOCALHOST_BACKEND + GET_ALL_PROJECTS_ENDPOINT, 
      {
        headers: {
          token: "Bearer " + this.$root.$data.token
        }
      })
        .then(response => {
          this.projects = response.data;
          if (this.projects.length == 0) {
            this.errorDeleteProject = "There are no projects";
          } else {
            this.projects.forEach(item => {
              this.items.push({
                Project: item.name,
              });
            });
          }
        })
        .catch(e => {
            console.log(e);
        });
    }
  }
};

</script>




<style></style>