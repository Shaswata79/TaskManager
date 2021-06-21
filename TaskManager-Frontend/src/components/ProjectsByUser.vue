<template>

  <div>

    <h1>User's Projects</h1>

    <div class="formContainer" id="userProjects">
      
        <div id="dataInput">
          <b-form @submit="getProjects">

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

            <p v-if="noProjects" style="color: blue">
                {{ noProjects }}
            </p>
            <p v-else-if="errorProjects" style="color: red">
              {{ errorProjects }}
            </p>

            <b-button type="submit" variant="primary" style="margin-top:15px">
                Get Projects
            </b-button>

          </b-form>
        </div>


        <div v-if="this.items.length != 0">
          <b-table :fields="fields" :items="items" responsive="sm" style="margin-top: 50px">

            <template #cell(index)="data">
                {{ data.index + 1 }}
            </template>

          </b-table>
        </div>
      
    </div>
  </div>
</template>




<script>
import { LOCALHOST_BACKEND, GET_ALL_USERS_ENDPOINT, GET_PROJECTS_BY_USER_ENDPOINT } from "../constants/constants";
import axios from "axios";

export default {
  data() {
    return {
      users: [],
      projects: [],
      userEmail: "",
      noProjects: "",
      errorProjects: "",
      fields: ['index', 'project'],
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
    
    getProjects(event) {
      event.preventDefault();

      this.noProjects = "";
      this.errorProjects = "";

      this.items = [];
      var url = LOCALHOST_BACKEND + GET_PROJECTS_BY_USER_ENDPOINT;
      
      axios.get(url, {
          headers: {
            token: "Bearer " + this.$root.$data.token,
            email: this.userEmail
          }
        })
        .then(
          response => {
            this.projects = response.data;
            if (this.projects.length === 0) {
              this.noProjects = "Selected user has no projects.";
            } else {
              this.projects.forEach(item => {
                this.items.push({
                    project: item.name
                });
              });
            }
          },
          error => {
            console.log(error.response);
            this.errorProjects = error.response.data;
          }
        );
    }
  }


};
</script>





<style></style>