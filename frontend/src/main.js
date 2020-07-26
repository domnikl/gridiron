import Vue from 'vue';
import Vuex from 'vuex';
import VueRouter from 'vue-router';
import VueMaterial from 'vue-material';
import 'vue-material/dist/vue-material.min.css';
import 'vue-material/dist/theme/default.css';

import App from '@/App.vue';
import Dashboard from '@/Dasboard.vue';
import Teams from '@/Teams.vue';
import Games from '@/Games.vue';

Vue.use(VueMaterial);
Vue.use(VueRouter);
Vue.use(Vuex);

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
