import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Dashboard',
    component: () => import('../views/DashboardView.vue')
  },
  {
    path: '/candidates',
    name: 'Candidates',
    component: () => import('../views/CandidatesView.vue')
  },
  {
    path: '/jobs',
    name: 'Jobs',
    component: () => import('../views/JobsView.vue')
  },
  {
    path: '/interviews',
    name: 'Interviews',
    component: () => import('../views/InterviewsView.vue')
  },
  {
    path: '/employees',
    name: 'Employees',
    component: () => import('../views/EmployeesView.vue')
  },
  {
    path: '/insights',
    name: 'Insights',
    component: () => import('../views/InsightsView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router