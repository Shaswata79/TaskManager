<template>
<div>
    <h1>Create Project</h1>
  <div id="projectForm">
    <b-form @submit="onSubmit" v-if="show">
      <b-form-group id="input-group-1" label="Project Name:" label-for="input-1">
        <b-form-input
          id="input-1"
          v-model="form.projectName"
          type="text"
          placeholder="Enter project name"
          required
        ></b-form-input>
      </b-form-group>

        <p v-if="errorCreateProject" style="color: red">
          Error: {{ errorCreateProject }}
        </p>
        <p v-else-if="successCreateProject" style="color: green">
          Success: {{ successCreateProject }}
        </p>

      <b-button type="submit" variant="primary">Create new Project</b-button>
    </b-form>

    </div>

</div>
</template>




<script>
import { CREATE_PROJECT_ENDPOINT, LOCALHOST_BACKEND } from "../constants/constants";
import axios from "axios";

export default {
  data() {
    return {
      form: {
        projectName: "",
      },
      show: true,
      successCreateProject: "",
      errorCreateProject: ""
    };
  },

  methods: {
    onSubmit(event) {
      event.preventDefault();
      axios
        .post(LOCALHOST_BACKEND + CREATE_PROJECT_ENDPOINT, 
        {
          name: this.form.projectName,
          tasks: [],
        },
        {
          headers: {
            token: "Bearer " + this.$root.$data.token
          }
        }
        
        )
        .then(
          response => {
            // set global state of logged in user
            this.successCreateProject = "Created new project '" + response.data.name + "'."
          },
          error => {
            console.log(error);
            this.errorCreateProject = error.response.data
          }
        );
    }
  }
};
</script>

<style>
#projectForm {
  margin-top: 8%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>
