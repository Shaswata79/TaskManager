<template>
  <div>
    <h1 v-show="success">Logged Out Successfully!</h1>
    <h1 v-show="!success">You were not logged in.</h1>
  </div>
</template>



<script>

export default {

  data() {
    return {
      success: true
    };
  },

  mounted() {
    if (this.$root.$data.email === null) {
      //user was not logged in
      this.success = false;
      this.$router.push("/login");
      return;
    }else {
        this.$root.$data.email = null;
        this.$root.$data.password = null;
        this.$root.$data.userType = null;
        this.$root.$data.token = null;
        this.$router.push("/login");
    }
    
  }
};
</script>