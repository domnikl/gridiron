import Vue from 'vue';
import VueRouter from 'vue-router';
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import './app.css'

import App from '@/App.vue';
import Dashboard from '@/Dasboard.vue';
import Teams from '@/Teams.vue';
import Games from '@/Games.vue';
import store from './store';

Vue.use(Vuetify)
Vue.use(VueRouter);

const routes = [
  { path: '/', component: Dashboard },
  { path: '/teams', component: Teams },
  { path: '/games', component: Games },
];

const vuetify = new Vuetify({})

const router = new VueRouter({
  routes
});

new Vue({
  render: (h) => h(App),
  router,
  vuetify,
  store,
}).$mount('#app');
