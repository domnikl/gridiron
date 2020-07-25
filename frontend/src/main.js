import Vue from 'vue';
import Vuex from 'vuex';
import VueRouter from 'vue-router';
import App from '@/App.vue';
import Dashboard from '@/Dasboard.vue';
import Teams from '@/Teams.vue';

Vue.use(VueRouter);
Vue.use(Vuex);

const routes = [
  { path: '/', component: Dashboard },
  { path: '/teams', component: Teams },
];

const router = new VueRouter({
  routes,
});

new Vue({
  render: (h) => h(App),
  router,
}).$mount('#app');
