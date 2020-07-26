import Vue from 'vue';
import Vuex from 'vuex';
import VueRouter from 'vue-router';
import VueMaterial from 'vue-material';
import { Datetime } from 'vue-datetime';
import 'vue-material/dist/vue-material.min.css';
import 'vue-material/dist/theme/default.css';
import 'vue-datetime/dist/vue-datetime.css'

import App from '@/App.vue';
import Dashboard from '@/Dasboard.vue';
import Teams from '@/Teams.vue';
import Games from '@/Games.vue';

Vue.use(VueMaterial);
Vue.use(VueRouter);
Vue.use(Vuex);
Vue.use(Datetime);

const routes = [
  { path: '/', component: Dashboard },
  { path: '/teams', component: Teams },
  { path: '/games', component: Games },
];

const router = new VueRouter({
  routes,
});

new Vue({
  render: (h) => h(App),
  router,
}).$mount('#app');
