import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios';

Vue.use(Vuex)

const client = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL,
});

client.interceptors.response.use(
  (response) => response,
  (error) => Promise.reject(error),
);

const request = (context, config) => {
  if (context.state.user) {
    config.headers = { Authorization: `Bearer ${context.state.user.jwt}` }
  }

  return client.request(config).catch((reason) => {
    if (reason.response && reason.response.status === 401) {
      context.commit('SET_USER', null)
    }

    if (reason.response && reason.response.data && reason.response.data.message) {
      reason = reason.response.data.message
    }

    context.commit('SET_ERROR', reason)
  })
}

const nullableFileInput = (file) => {
  if (!file) {
    return Promise.resolve(null)
  }

  return new Promise((resolve) => {
    const fileReader = new FileReader()
    fileReader.onload = resolve
    fileReader.readAsBinaryString(file)
  })
}

const store = new Vuex.Store({
  state: {
    lastError: '',
    teams: [],
    games: [],
    users: [],
    user: JSON.parse(localStorage.getItem('user')),
  },
  mutations: {
    SET_ERROR: (state, error) => {
      state.lastError = error
    },
    SET_TEAMS: (state, payload) => {
      state.teams = payload
    },
    SET_GAMES: (state, payload) => {
      state.games = payload
    },
    SET_USERS: (state, payload) => {
      state.users = payload
    },
    SET_USER: (state, payload) => {
      localStorage.setItem('user', JSON.stringify(payload))

      state.user = payload
    }
  },
  actions: {
    GET_USERS(context) {
      return request(context, { url: '/users' })
        .then((response) => { context.commit('SET_USERS', response.data) })
    },
    GET_TEAMS(context) {
      return request(context, { url: '/teams' })
        .then((response) => { context.commit('SET_TEAMS', response.data) })
    },
    GET_GAMES(context) {
      return request(context, { url: '/games' })
        .then((response) => { context.commit('SET_GAMES', response.data) })
    },
    SAVE_GAME(context, payload) {
      return request(context, { method: 'POST', url: '/games', data: payload })
    },
    PLACE_BET(context, payload) {
      return request(context, { method: 'POST', url: `/games/${payload.game.uuid}/bets`, data: payload })
    },
    END_GAME(context, payload) {
      return request(context, { method: 'PATCH', url: `/games/${payload.game.uuid}`, data: payload })
    },
    SAVE_TEAM(context, payload) {
      async function f1() {
        const result = await nullableFileInput(payload.logo)

        payload.logo = result && result.target ? btoa(result.target.result) : null

        let promise;

        if (payload.uuid) {
          promise = request(context, { method: 'PATCH', url: `/teams/${payload.uuid}`, data: payload })
        } else {
          promise = request(context, { method: 'POST', url: '/teams', data: payload })
        }

        return promise
      }

      return f1()
    },
    LOGIN(context, payload) {
      return request(context, { method: 'POST', url: '/auth', data: payload })
        .then((response) => { context.commit('SET_USER', response.data) })
    },
    LOGOUT(context) {
      context.commit('SET_USER', null);
    },
    SIGN_UP(context, payload) {
      return request(context, { method: 'POST', url: '/users', data: payload })
    }
  }
})

export default store;
