<template>
  <div>
    <h1>Assign user to Task</h1>

    <div class="formContainer" id="projectAssignment">
      

        <div id="dataInput">
          <b-form @submit="assignUser">

            <div id="userEmailInput">
              <b-form-group label="Select a User" class="mt-4">

                <b-form-radio
                  v-for="user in users"
                  :key="user.email"
                  v-model="userEmail"
                  :value="user.email"
                >
                  {{ user.name + " (" + user.email + ")" }}
                </b-form-radio>

                <p v-show="errorUsers">
                    {{errorUsers}}
                </p>

              </b-form-group>
            </div>
            
            
            <div id="taskIDInput">
              <b-form-group label="Select a Task" class="mt-4">

                <b-form-radio
                  v-for="task in tasks"
                  :key="task.id"
                  v-model="taskID"
                  :value="task.id"
                >
                  Task: {{task.description}} in Project {{task.projectName}}.
                </b-form-radio>

                <p v-show="errorProjects">
                    {{errorProjects}}
                </p>

              </b-form-group>
            </div>


            <b-button type="submit" variant="primary" style="margin-top:15px">
                Confirm
            </b-button>

          </b-form>

          <p v-if="successAssign" style="color: green">
                Success: {{ successAssign }}
            </p>
        </div>

      
    </div>
  </div>
</template>




<script>
import { LOCALHOST_BACKEND, ASSIGN_TO_TASK_ENDPOINT, GET_ALL_PROJECTS_ENDPOINT, GET_ALL_USERS_ENDPOINT } from "../constants/constants";
import axios from "axios";

export default {

  data() {
    return {
      users: [],
      userEmail: "",
      tasks: [],
      taskID: "",
      successAssign: "",
      errorProjects: "",
      errorUsers: ""
      
    };
  },


  created: function() {
      this.getUsers();
      this.getTasks();
  },



  methods: {

    getUsers() {
      axios.get(LOCALHOST_BACKEND + GET_ALL_USERS_ENDPOINT, 
      {
        headers: {
          token: "Bearer " + this.$root.$data.token,
      }
      }).then(response => {
          this.users = response.data;
          if (this.users.length == 0) {
            this.errorUsers = "There are no users registered";
          } 
        })
        .catch(e => {
          console.log(e);
        });
    },


    getTasks() {
      var projects = [];
      var taskList = [];

      axios.get(LOCALHOST_BACKEND + GET_ALL_PROJECTS_ENDPOINT, 
      {
        headers: {
          token: "Bearer " + this.$root.$data.token,
      }
      }).then(response => {
          projects = response.data;
          projects.forEach(project => {
              taskList = project.tasks;
              taskList.forEach(task => {
                this.tasks.push(task);
              }) 
          });

          if (this.tasks.length == 0) {
            this.errorProjects = "There are no tasks available";
          } 
        },
        error => {
            console.log(error.response);
        });
    },


    assignUser(event){
        event.preventDefault();
        let url = LOCALHOST_BACKEND + ASSIGN_TO_TASK_ENDPOINT

        axios({
            method: 'POST',
            url,
            headers: {
                'Content-Type': 'application/json',
                'token': "Bearer " + this.$root.$data.token,
                'username': this.userEmail,
                'taskID': this.taskID,
              }
        })
        .then(
          response => {
            console.log(response);
            this.successAssign = response.data;
          },
          error => {
            console.log(error.response.data);
          }
        );

    }

    
    
  },
  


};
</script>



<style>
#userEmailInput {
  margin-top: 8%;
  margin-left: 5%;
  margin-right: 5%;
}

#taskIDInput {
  margin-top: 8%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>