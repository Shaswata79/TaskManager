<template>

  <div>

    <h1>User's Tasks</h1>

    <div class="formContainer" id="userTasks">
      
        <div id="dataInput">
          <b-form @submit="getTasks">

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

                <p v-show="users.length === 0">
                  No users registered.
                </p>
              </b-form-group>
            </div>

            <p v-if="noTasks" style="color: blue">
                {{ noTasks }}
            </p>
            <p v-else-if="errorTasks" style="color: red">
              {{ errorTasks }}
            </p>

            <b-button type="submit" variant="primary" style="margin-top:15px">
                Get Tasks
            </b-button>

          </b-form>
        </div>


        <div v-if="this.items.length != 0">
          <b-table :fields="fields" :items="items" responsive="sm" style="margin-top: 50px">
          </b-table>
        </div>
      
    </div>
  </div>
</template>




<script>
import { LOCALHOST_BACKEND, GET_ALL_USERS_ENDPOINT, GET_TASKS_BY_USER_ENDPOINT } from "../constants/constants";
import axios from "axios";

export default {
  data() {
    return {
      users: [],
      tasks: [],
      userEmail: "",
      noTasks: "",
      errorTasks: "",
      fields: ['ID', 'description', 'status', 'due date', 'project'],
      items: []
    };
  },


  created: function() {

    let url = LOCALHOST_BACKEND + GET_ALL_USERS_ENDPOINT;

    axios.get(url, {
        headers: {
          token: "Bearer " + this.$root.$data.token
        }

    }).then(response => {
        this.users = response.data;
      }, error => {
        console.log(error.response)
      }
    )

  },


  methods: {
    
    getTasks(event) {
      event.preventDefault();

      this.noTasks = "";
      this.errorTasks = "";

      this.items = [];
      var url = LOCALHOST_BACKEND + GET_TASKS_BY_USER_ENDPOINT;
      
      axios.get(url, {
          headers: {
            token: "Bearer " + this.$root.$data.token,
            email: this.userEmail
          }
        })
        .then(
          response => {
            this.tasks = response.data;
            if (this.tasks.length === 0) {
              this.noTasks = "Selected user has no tasks.";
            } else {
              this.tasks.forEach(item => {
                this.items.push({
                    ID: item.id,
                    description: item.description,
                    status: item.status,
                    'due date': item.dueDate,
                    project: item.projectName
                });
              });
            }
          },
          error => {
            console.log(error.response);
            this.errorTasks = error.response.data;
          }
        );
    }
  }


};
</script>





<style></style>