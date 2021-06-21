<template>

  <div>

  <div id="AllProjects" v-if="viewSection == 1">
  <h1>All Projects</h1>

    <div class="formContainer" id="ViewProjects">

        <div v-if="errorViewProjects" class="text-center" style="color: red">
         Error: {{ errorViewProjects }}
        </div>

        <div v-else>
          <b-table :items="projectItems" :fields="projectFields" :outlined="true" :key="this.projectItems.length">
            <template #cell(tasks)="row">
              <b-button 
                size="sm" 
                v-on:click="viewProjectTasks(row.item.Project)"
                class="mr-2"
                variant="danger"
              >
                View Tasks
              </b-button>
            </template>
          </b-table>
        </div>

    </div>

  </div>

  <div id="allTasks" v-if="viewSection == 2">
      <h2>Tasks of project "{{this.taskProjectName}}"</h2>

      <div id="ViewProjectTasks">

          <b-table :items="taskItems" :fields="taskFields" :outlined="true" :key="this.taskItems.length">
          </b-table>

      </div>

      <div>
          <b-button 
                size="sm" 
                v-on:click="backToProjects()"
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
import { GET_ALL_PROJECTS_ENDPOINT, LOCALHOST_BACKEND } from "../constants/constants";


export default {
  data() {
    return {
      errorViewAppt: "",
      projects: [],
      projectFields: ["Project", "tasks"],
      projectItems: [],
      viewSection: 1,
      taskFields: ["ID", "Description", "Status", "Due Date"],
      taskItems: [],
      taskProjectName: ""
    };
  },

  //fetch all of this user's projects and display them in a table
  created: function() {
    //let url = LOCALHOST_BACKEND + GET_ALL_PROJECTS_ENDPOINT;
    this.viewSection = 1;
    this.getProjects();
  },


  methods: {
    // Should output something like "Tue Mar 02 2021 10:00:00 GMT-0500 (Eastern Standard Time)" given a timestamp
    displayDateTime(dateTime) {
      let date = new Date(dateTime).toString();
      if (date == "Invalid Date") return "";
      else
        return (
          date.slice(0, 10) +
          ", " +
          date.slice(11, 15) +
          " at " +
          date.slice(16, 21)
        );
    },


    viewProjectTasks(projectName){
      var i;
      for (i = 0; i < this.projects.length; i++) {
          var projectDto = this.projects[i];
          if(projectDto.name === projectName){
            this.viewTasks(projectName, projectDto.tasks)
          }
      }
    },

    viewTasks(projectName, taskDtoList) {

        taskDtoList.forEach(item => {
              this.taskItems.push({
                ID: item.id,
                Description: item.description,
                Status: item.status,
                'Due Date': item.dueDate 
              });
        });
        this.taskProjectName = projectName
        this.viewSection = 2
         
    },


    backToProjects(){
      this.viewSection = 1
      this.taskItems = []
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
            this.errorViewProjects = "There are no projects";
          } else {
            this.projects.forEach(item => {
              this.projectItems.push({
                Project: item.name
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




<style></style>