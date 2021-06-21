import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Login from '@/components/Login'
import Register from '@/components/Register'
import CreateProject from '@/components/CreateProject'
import CreateTask from '@/components/CreateTask'
import ViewProjects from '@/components/ViewProjects'
import DeleteProject from '@/components/DeleteProject'
import ViewAndEditTask from '@/components/ViewAndEditTask'
import GetTaskByProject from '@/components/GetTaskByProject'
import GetTasksByStatus from '@/components/GetTasksByStatus'
import GetExpiredTasks from '@/components/GetExpiredTasks'
import AssignToProject from '@/components/AssignToProject'
import AssignToTask from '@/components/AssignToTask'
import ProjectsByUser from '@/components/ProjectsByUser'
import TasksByUser from '@/components/TasksByUser'
import GetAllUsers from '@/components/GetAllUsers'
import Logout from '@/components/Logout'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    }, {
      path: '/',
      name: "Hello",
      component: Hello
    }, {
      path: '/register',
      name: 'Register',
      component : Register
    }, {
      path: '/createProject',
      name: 'Create Project',
      component : CreateProject
    }, {
      path: '/createTask',
      name: 'Create Task',
      component : CreateTask
    }, {
      path: '/viewProjects',
      name: 'View Projects',
      component : ViewProjects
    }, {
      path: '/deleteProject',
      name: 'Delete Project',
      component : DeleteProject
    },{
      path: '/viewTasks',
      name: 'ViewTasks',
      component : ViewAndEditTask
    },{
      path: '/getTaskByProject',
      name: 'GetTaskByProject',
      component : GetTaskByProject
    }, {
      path: '/getExpiredTasks',
      name: 'GetExpiredTasks',
      component : GetExpiredTasks
    }, {
      path: '/getTasksByStatus',
      name: 'GetTasksByStatus',
      component : GetTasksByStatus
    }, {
      path: '/assignToProject',
      name: 'AssignToProject',
      component : AssignToProject
    }, {
      path: '/assignToTask',
      name: 'AssignToTask',
      component : AssignToTask
    }, {
      path: '/projectsByUser',
      name: 'ProjectsByUser',
      component : ProjectsByUser
    }, {
      path: '/tasksByUser',
      name: 'TasksByUser',
      component : TasksByUser
    }, {
      path: '/getAllUsers',
      name: 'GetAllUsers',
      component : GetAllUsers
    }, {
      path: '/logout',
      name: 'Logout',
      component : Logout
    }


  ]
})
