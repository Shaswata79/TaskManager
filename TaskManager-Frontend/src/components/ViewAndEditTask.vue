<template>

  <div id="taskView">

  <div id="AllTasks" v-if="viewSection == 1">
  <h1>Your Tasks</h1>

    <div class="formContainer" id="ViewTasks">

        <div v-if="errorViewTasks" class="text-center" style="color: red">
         Error: {{ errorViewTasks }}
        </div>

        <div v-else>
          <b-table :items="items" :fields="fields" :outlined="true" :key="this.items.length">
            <template #cell(edit)="row">
              <b-button 
                size="sm" 
                v-on:click="goToEditTask(row.item.ID, row.item.Description, row.item.Project)"
                class="mr-2"
                variant="danger"
              >
                Edit Task
              </b-button>
            </template>
          </b-table>
        </div>

    </div>

  </div>




  <div id="EditTaskForm" v-if="viewSection == 2">
      <h2>Editing task "{{this.editTaskName}}" in project "{{this.editTaskProject}}"</h2>

    <div>
      <b-form @submit="editTask">

      <b-form-group id="input-group-0" label="Description:" label-for="input-0">
        <b-form-input
          id="input-0"
          v-model="form.description"
          type="text"
          placeholder="Enter new task description"
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-1" label="Status:" label-for="input-1">
        <b-form-input
          id="input-1"
          v-model="form.status"
          type="text"
          placeholder="Update task status"
        ></b-form-input>
      </b-form-group>

      <div>
            <label for="task-datepicker">Due date:</label>
            <b-form-datepicker
                id="task-datepicker"
                v-model="form.dueDate"
                class="mb-2"
                placeholder="New due date"
            ></b-form-datepicker>
      </div>

      <b-button type="submit" variant="primary">Confirm</b-button>

    </b-form>
    </div>


      <div>
          <b-button 
                size="sm" 
                v-on:click="backToViewTasks()"
                class="mr-2"
                variant="danger"
          >
          Back
          </b-button>
      </div>


  </div>

  </div>

</template>



<script>
import axios from "axios";
import { GET_ALL_TASKS_ENDPOINT, LOCALHOST_BACKEND, EDIT_TASK_ENDPOINT } from "../constants/constants";


export default {
  data() {
    return {
      errorEditTask: "",
      successEditTask: "",
      tasks: [],
      fields: ["ID", "Description", "Status", "Due Date", "Project", "edit"],
      items: [],
      viewSection: 1,
      editTaskID: "",
      editTaskProject: "",
      editTaskName: "",
      form: {
        description: "",
        status: "",
        dueDate: ""
      }
    };
  },

  //fetch all of this user's projects and display them in a table
  created: function() {
    this.getTasks();
    this.viewSection = 1;
  },


  methods: {


    goToEditTask(id, desc, proj){
        this.editTaskID = id
        this.editTaskProject = proj
        this.editTaskName = desc 
        this.viewSection = 2
    },


    editTask(event){
      event.preventDefault();
      axios.put(LOCALHOST_BACKEND + EDIT_TASK_ENDPOINT + this.editTaskID, 
        {
          description: this.form.description,
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
            this.successEditTask = "Task '" + response.data.description + "' in project '" + response.data.projectName + "' was successfully updated." 
          },
          error => {
            console.log(error);
            this.errorEditTask = error.response.data
          }
        );
    },

    
    backToViewTasks(){
      this.viewSection = 1
      this.editTaskID = ""
      this.editTaskProject = ""
      this.editTaskName = ""
    },
    

    //fetches all of customer's appointments
    getTasks() {
      axios.get(LOCALHOST_BACKEND + GET_ALL_TASKS_ENDPOINT, 
      {
        headers: {
          token: "Bearer " + this.$root.$data.token
      }
      }).then(response => {
          this.tasks = response.data;
          if (this.tasks.length == 0) {
            this.errorViewTasks = "There are no projects";
          } else {
            this.tasks.forEach(item => {
              this.items.push({
                ID: item.id,
                Description: item.description,
                Status: item.status,
                'Due Date': item.dueDate, 
                Project : item.projectName
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




<style>
#EditTaskForm {
  margin-top: 8%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>