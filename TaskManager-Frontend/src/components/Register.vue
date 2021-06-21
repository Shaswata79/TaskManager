<template>
  <div id="registerForm">
    <b-form @submit="onSubmit" v-if="show">

      <b-form-group id="input-group-0" label="Name:" label-for="input-0">
        <b-form-input
          id="input-0"
          v-model="form.name"
          type="text"
          placeholder="Enter Full Name"
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-1" label="Email address:" label-for="input-1">
        <b-form-input
          id="input-1"
          v-model="form.email"
          type="email"
          placeholder="Enter email"
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-2" label="Password:" label-for="input-2">
        <b-form-input
          id="input-2"
          v-model="form.password"
          placeholder="Enter name"
          required
          type="password"
        ></b-form-input>
      </b-form-group>

      <b-button type="submit" variant="primary">Sign Up</b-button>

    </b-form>
  </div>
</template>



<script>
import {
  REGISTER_ENDPOINT,
  LOCALHOST_BACKEND
} from "../constants/constants";
import axios from "axios";


export default {
  data() {
    return {
      form: {
        name: "",
        email: "",
        password: ""
      },
      show: true
    };
  },

  methods: {
    onSubmit(event) {
      event.preventDefault();
      
      axios
        .post(LOCALHOST_BACKEND + REGISTER_ENDPOINT, {
          email: this.form.email,
          password: this.form.password,
          name: this.form.name,
        })
        .then(
          response => {
            console.log(response);
            alert("Account created for " + response.data.name + " with email " + response.data.email + ".");
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
#registerForm {
  margin-top: 8%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>
