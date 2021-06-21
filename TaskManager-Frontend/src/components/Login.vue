<template>
  <div id="loginForm">
    <b-form @submit="onSubmit" v-if="show">
      <b-form-group id="input-group-1" label="Email :" label-for="input-1">
        <b-form-input
          id="input-1"
          v-model="form.username"
          type="email"
          placeholder="Enter email"
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-2" label="Password:" label-for="input-2">
        <b-form-input
          id="input-2"
          v-model="form.password"
          placeholder="Enter password"
          required
          type="password"
        ></b-form-input>
      </b-form-group>

      <b-button type="submit" variant="primary">Sign In</b-button>
    </b-form>
  </div>
</template>



<script>
import { LOGIN_ENDPOINT, LOCALHOST_BACKEND } from "../constants/constants";
import axios from "axios";

export default {
  data() {
    return {
      form: {
        username: "",
        password: ""
      },
      show: true
    };
  },

  methods: {
    onSubmit(event) {
      event.preventDefault();
      axios
        .post(LOCALHOST_BACKEND + LOGIN_ENDPOINT, {
          username: this.form.username,
          password: this.form.password,
        })
        .then(
          response => {
            // set global state of logged in user
            this.$root.$data.email = this.form.username;
            this.$root.$data.password = this.form.password;
            this.$root.$data.userType = response.data.userType;
            this.$root.$data.token = response.data.jwt;
            console.log(this.$root.$data);
            alert("Login Success " + this.$root.$data.userType);
            this.$router.push("/");   //Refreshes the TopNavBar
          },
          error => {
            console.log(error);
            if (error.response) {
              alert(error.response)
            }
          }
        );
    }
  }
};
</script>

<style>
#loginForm {
  margin-top: 8%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>
