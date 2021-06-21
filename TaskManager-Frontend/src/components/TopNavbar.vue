<template>
  <b-navbar
    fixed="top"
    :sticky="true"
    type="dark"
    variant="info"
    toggleable="lg"
  >

    <!-- Title button -->
    <b-navbar-brand to="/">Task Manager</b-navbar-brand>
    <!-- Left aligned buttons -->

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
    <b-collapse id="nav-collapse" is-nav>



    <b-navbar-nav>
      <b-nav-item to="Login">Log In</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav>
      <b-nav-item to="Register">Register</b-nav-item>
    </b-navbar-nav>


    <b-navbar-nav v-show="this.$root.$data.userType === 'ADMIN'">

        <b-nav-item-dropdown text="Users">
          <b-dropdown-item to="ProjectsByUser">View Projects by User</b-dropdown-item>
          <b-dropdown-item to="TasksByUser">View Tasks by User</b-dropdown-item>
          <b-dropdown-item to="AssignToProject">Assign to Project</b-dropdown-item>
          <b-dropdown-item to="AssignToTask">Assign to Task</b-dropdown-item>
          <b-dropdown-item to="GetAllUsers">View all Users</b-dropdown-item>
        </b-nav-item-dropdown>

        <b-nav-item-dropdown text="Tasks">
          <b-dropdown-item to="CreateTask">Create Task</b-dropdown-item>
          <b-dropdown-item to="ViewTasks">View and Edit tasks</b-dropdown-item>
          <b-dropdown-item to="GetTaskByProject">View Tasks by Project</b-dropdown-item>
          <b-dropdown-item to="GetTasksByStatus">View Tasks by Status</b-dropdown-item>
          <b-dropdown-item to="GetExpiredTasks">View Expired Tasks</b-dropdown-item>
        </b-nav-item-dropdown>

        <b-nav-item-dropdown text="Projects">
          <b-dropdown-item to="CreateProject">Create Project</b-dropdown-item>
          <b-dropdown-item to="ViewProjects">View Projects</b-dropdown-item>
          <b-dropdown-item to="DeleteProject">Delete Project</b-dropdown-item>
        </b-nav-item-dropdown>

        <b-navbar-nav>
          <b-nav-item to="Logout" right>Logout</b-nav-item>
        </b-navbar-nav>

    </b-navbar-nav>

    
    
    <b-navbar-nav v-show="this.$root.$data.userType === 'USER'">

        <b-nav-item-dropdown text="Users">
          <b-dropdown-item to="AssignToProject">Assign to Project</b-dropdown-item>
          <b-dropdown-item to="AssignToTask">Assign to Task</b-dropdown-item>
        </b-nav-item-dropdown>

        <b-nav-item-dropdown text="Tasks">
          <b-dropdown-item to="CreateTask">Create Task</b-dropdown-item>
          <b-dropdown-item to="ViewTasks">View and Edit tasks</b-dropdown-item>
          <b-dropdown-item to="GetTaskByProject">View Tasks by Project</b-dropdown-item>
          <b-dropdown-item to="GetTasksByStatus">View Tasks by Status</b-dropdown-item>
          <b-dropdown-item to="GetExpiredTasks">View Expired Tasks</b-dropdown-item>
        </b-nav-item-dropdown>

        <b-nav-item-dropdown text="Projects">
          <b-dropdown-item to="CreateProject">Create Project</b-dropdown-item>
          <b-dropdown-item to="ViewProjects">View Projects</b-dropdown-item>
          <b-dropdown-item to="DeleteProject">Delete Project</b-dropdown-item>
        </b-nav-item-dropdown>

        <b-navbar-nav>
          <b-nav-item to="Logout" right>Logout</b-nav-item>
        </b-navbar-nav>

    </b-navbar-nav>



    



  </b-collapse>  

  </b-navbar>
</template>



<script>
export default {
  name: "TopNavbar"
};
</script>

<style></style>
