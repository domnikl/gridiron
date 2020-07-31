import Vue from 'vue'
import Vuex from 'vuex'
import Api from './api'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    lastError: '',
    teams: [],
    games: [],
    user: null,
  },
  mutations: {
    SET_ERROR: (state, errorMessage) => {
      state.lastError = errorMessage
    },
    SET_TEAMS: (state, payload) => {
      state.teams = payload
    },
    SET_GAMES: (state, payload) => {
      state.games = payload
    },
    SET_USER: (state, payload) => {
      state.user = payload
    }
  },
  actions: {
    GET_TEAMS(context) {
      return Api.get('/teams').then((response, error) => {
        if (error) {
          context.commit('SET_ERROR', error.toString())
        } else {
          context.commit('SET_TEAMS', response.data)
        }
      });
    },
    GET_GAMES(context) {
      return Api.get('/games').then((response, error) => {
        if (error) {
          context.commit('SET_ERROR', error.toString())
        } else {
          context.commit('SET_GAMES', response.data)
        }
      });
    },
    SAVE_GAME(context, payload) {
      return Api.post('/games', payload).then((response, error) => {
        if (error) {
          context.commit('SET_ERROR', error.toString())
        }
      });
    },
    SAVE_TEAM(context, payload) {
      return Api.post('/teams', payload).then((response, error) => {
        if (error) {
          context.commit('SET_ERROR', error.toString())
        }
      });
    },
    LOGIN(context, payload) {
      return Api.post('/auth', payload).then((response, error) => {
        if (error) {
          context.commit('SET_ERROR', error.toString())
        }

        context.commit('SET_USER', response.data);
      });
    },
    CHECK_AUTH(context) {
      return Api.get('/auth').then((response, error) => {
        if (error) {
          context.commit('SET_ERROR', error.toString())
        }

        context.commit('SET_USER', response.data);
      });
    },
    LOGOUT(context, payload) {
      return Api.delete('/auth/logout', payload).then((response, error) => {
        if (error) {
          context.commit('SET_ERROR', error.toString())
        }

        context.commit('SET_USER', null);
      });
    },
    SIGN_UP(context, payload) {
      return Api.post('/users', payload).then((response, error) => {
        if (error) {
          context.commit('SET_ERROR', error.toString())
        }
      });
    }
  }
})

export default store;
