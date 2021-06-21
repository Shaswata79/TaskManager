<template>
<div>
    <h1>Create Task</h1>
  <div id="taskForm">
    <b-form @submit="onSubmit" v-if="show">
        <b-form-group id="input-group-1" label="Task Description:" label-for="input-1">
            <b-form-input
                id="input-1"
                v-model="form.description"
                type="text"
                placeholder="Enter task description"
                required
            ></b-form-input>
        </b-form-group>

        <b-form-group id="input-group-2" label="Project Name:" label-for="input-2">
            <b-form-input
                id="input-2"
                v-model="form.projectName"
                type="text"
                placeholder="Enter project name"
                required
            ></b-form-input>
        </b-form-group>

        <b-form-group id="input-group-3" label="Task status:" label-for="input-3">
            <b-form-input
                id="input-3"
                v-model="form.status"
                type="text"
                placeholder="Enter task status"
                required
            ></b-form-input>
        </b-form-group>

        <div>
            <label for="task-datepicker">Due date</label>
            <b-form-datepicker
                id="task-datepicker"
                v-model="form.dueDate"
                class="mb-2"
            ></b-form-datepicker>
        </div>

        <p v-if="errorCreateTask" style="color: red">
          Error: {{ errorCreateTask }}
        </p>
        <p v-else-if="successCreateTask" style="color: green">
          Success: {{ successCreateTask }}
        </p>

      <b-button type="submit" variant="primary">Create new Task</b-button>
    </b-form>

    </div>

</div>
</template>




<script>
import { CREATE_TASK_ENDPOINT, LOCALHOST_BACKEND } from "../constants/constants";
import axios from "axios";

export default {
  data() {
    return {
      form: {
        description: "",
        projectName: "",
        status: "",
        dueDate: ""
      },
      show: true,
      successCreateTask: "",
      errorCreateTask: ""
    };
  },

  methods: {
    onSubmit(event) {
      event.preventDefault();
      axios
        .post(LOCALHOST_BACKEND + CREATE_TASK_ENDPOINT, 
        {
          description: this.form.description,
          projectName: this.form.projectName,
          status: this.form.status,
          dueDate: this.form.dueDate
        },
        {
          headers: {
            token: "Bearer " + this.$root.$data.token
          }
        }
        )
        .then(
          response => {
            this.successCreateTask = "Created new task '" + response.data.description + "' in project '" + response.data.projectName + "'."
          },
          error => {
            console.log(error);
            this.errorCreateTask = error.response
          }
        );
    }
  }
};
</script>

<style>
#taskForm {
  margin-top: 8%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>
