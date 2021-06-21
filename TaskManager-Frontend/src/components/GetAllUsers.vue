<template>
  <div>
    <h1>View all Users</h1>
    <div class="formContainer" id="ViewUsers">
      
        <div>
          <span style="color: red">
            {{ errorViewUsers }}
          </span>
        </div>

        <div>
          <b-table :items="items" :fields="fields" :outlined="true"> </b-table>
        </div>
      
    </div>
  </div>
</template>




<script>

import axios from "axios";
import { LOCALHOST_BACKEND, GET_ALL_USERS_ENDPOINT } from "../constants/constants";


export default {

  data() {
    return {
      errorViewUsers: "",
      users: [],
      fields: ["name", "email"],
      items: []
    };
  },


  created: function() {

    axios.get(LOCALHOST_BACKEND + GET_ALL_USERS_ENDPOINT,
        {
        headers: {
                token: "Bearer " + this.$root.$data.token
            }
        }
    )
      .then(response => {
        this.users = response.data;
        this.users.forEach((item) => {
          this.items.push({
            name: item.name,
            email: item.email
          });
        });
      },
        error => {
            this.errorViewTasks = error.response.data;
            console.log(error.response)
        }
      )
      
  }


};
</script>





<style></style>