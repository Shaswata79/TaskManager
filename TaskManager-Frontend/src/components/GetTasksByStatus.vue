<template>

  <div>
    <h1>View Tasks by Status</h1>


    <div class="formContainer" id="viewTaskByStatus">
      
        <div id="nameInput">
          <b-form @submit="getTasks">

            <b-form-group
                id="input-group-1"
                label="Enter Task Status:"
                label-for="input-1"
            >
                <b-form-input
                    id="input-1"
                    v-model="form.status"
                    type="text"
                    required
                ></b-form-input>
            </b-form-group>

            <b-button type="submit" variant="primary">Get Tasks</b-button>

          </b-form>
        </div>


        <div v-if="errorGetTask">
          <span v-if="errorGetTask" style="color: red">
            {{ errorGetTask }}
          </span>
        </div>

        <div v-if="noTask">
          <span v-if="noTask" style="color: blue">
            {{ noTask }}
          </span>
        </div>


        <div id="taskList">
          <b-table
            :items="items"
            :fields="fields"
            :outlined="true"
            :key="this.items.length"
          />
        </div>


        
      </div>
    
  </div>
</template>



<script>

import { LOCALHOST_BACKEND, GET_TASKS_BY_STATUS } from "../constants/constants";
import axios from "axios";

export default {
  data() {
    return {
      form: {
          status: ""
      },
      errorGetTask: "",
      noTask: "",
      fields: ["ID", "Description", "Status", "Due Date"],
      items: [],
      tasks: []
    };
  },


  methods: {
    
    getTasks(event) {

      event.preventDefault();
      this.errorGetTask = "";
      this.noTask = "";
    
      axios.get( LOCALHOST_BACKEND + GET_TASKS_BY_STATUS,
        {headers: {
              token: "Bearer " + this.$root.$data.token,
              status: this.form.status
            }
        }

        )
        .then(
          response => {
            this.tasks = response.data
            if (this.tasks.length === 0) {
              this.noTask = "There are no tasks with chosen status.";
            } else {
              this.tasks.forEach(item => {
              this.items.push({
                ID: item.id,
                Description: item.description,
                Status: item.status,
                'Due Date': item.dueDate
              });
            });
            }
          },

          error => {
            console.log(error.response.data);
            this.errorGetTask = error.response.data;
          }

        );

    }
  },

  
};

</script>



<style></style>