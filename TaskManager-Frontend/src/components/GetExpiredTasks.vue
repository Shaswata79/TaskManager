<template>

  <div>
    <h1>View Expired Tasks</h1>


    <div class="formContainer" id="viewExpiredTasks">

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

import { LOCALHOST_BACKEND, GET_EXPIRED_TASKS_ENDPOINT } from "../constants/constants";
import axios from "axios";

export default {
  data() {
    return {
      errorGetTask: "",
      noTask: "",
      fields: ["ID", "Description", "Status", "Due Date"],
      items: [],
      tasks: []
    };
  },


  created: function() {
    this.getTasks();
  },


  methods: {
    
    getTasks(event) {

      this.errorGetTask = "";
      this.noTask = "";
    
      axios.get( LOCALHOST_BACKEND + GET_EXPIRED_TASKS_ENDPOINT,
        {headers: {
              token: "Bearer " + this.$root.$data.token,
            }
        }).then(
          response => {
            this.tasks = response.data
            if (this.tasks.length === 0) {
              this.noTask = "There are no tasks for this project.";
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