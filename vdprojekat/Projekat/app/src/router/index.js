import { createRouter, createWebHistory } from 'vue-router'
import IndexView from '../views/IndexView.vue'
import UmetnineShowView from '../views/UmetnineShowView.vue'
import UmetninaDetails from '../views/UmetninaDetails.vue'
import MojNalogView from '../views/MojNalogView.vue'
import UlogujSeView from '../views/UlogujseView.vue'
import UmetniciShowView from '../views/UmetniciShowView.vue'
import ONamaView from '../views/ONamaView.vue'

const routes = [
  {
    path: '/',
    name: 'IndexView',
    component: IndexView
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  }, 
  {
    path: '/slike',
    name: 'SlikeView',
    component: UmetnineShowView
  },
  {
    path: '/skulpture',
    name: 'SkulptureView',
    component: UmetnineShowView
  },
  {
    path: '/ostale',
    name: 'OstaleView',
    component: UmetnineShowView
  },
  {
    path: '/sve_umetnine',
    name: 'SveView',
    component: UmetnineShowView
  },
  {
    path: '/umetnina/:id',
    name: 'UmetninaView',
    component: UmetninaDetails,
    // props: route => ({ query: route.query.q })
  },
  {
    path: '/moj_nalog',
    name: 'MojNalogView',
    component: MojNalogView
  },
  {

    path: '/ulogujse',
    name: 'UlogujSe',
    component: UlogujSeView
  },
  {
    path: '/umetnici',
    name: 'UmetniciShowView',
    component: UmetniciShowView

  },
  {
    path: '/o_nama',
    name: 'ONamaView',
    component: ONamaView

  },
  
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
