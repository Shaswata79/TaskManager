<template>
  <div>
    <h1>Assign user to project</h1>

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
            
            
            <div id="projectNameInput">
              <b-form-group label="Select a Project" class="mt-4">

                <b-form-radio
                  v-for="project in projects"
                  :key="project.name"
                  v-model="projectName"
                  :value="project.name"
                >
                  {{ project.name }}
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
import { LOCALHOST_BACKEND, ASSIGN_TO_PROJECT_ENDPOINT, GET_ALL_PROJECTS_ENDPOINT, GET_ALL_USERS_ENDPOINT } from "../constants/constants";
import axios from "axios";

export default {

  data() {
    return {
      users: [],
      userEmail: "",
      projects: [],
      projectName: "",
      successAssign: "",
      errorProjects: "",
      errorUsers: ""
      
    };
  },


  created: function() {
      this.getProjects();
      this.getUsers();
  },



  methods: {

    getUsers() {
      axios.get(LOCALHOST_BACKEND + GET_ALL_USERS_ENDPOINT, 
      {
        headers: {
          token: "Bearer " + this.$root.$data.token
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


    getProjects() {
      axios.get(LOCALHOST_BACKEND + GET_ALL_PROJECTS_ENDPOINT, 
      {
        headers: {
          token: "Bearer " + this.$root.$data.token
      }
      }).then(response => {
          this.projects = response.data;
          if (this.projects.length == 0) {
            this.errorProjects = "There are no projects available";
          } 
        })
        .catch(e => {
          console.log(e);
        });
    },


    assignUser(event){
        event.preventDefault();
        let url = LOCALHOST_BACKEND + ASSIGN_TO_PROJECT_ENDPOINT

        axios({
            method: 'POST',
            url,
            headers: {
                'Content-Type': 'application/json',
                'token': "Bearer " + this.$root.$data.token,
                'username': this.userEmail,
                'projectName': this.projectName
            },
          
        }).then(
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

#projectNameInput {
  margin-top: 8%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>