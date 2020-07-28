import Vue from 'vue'
import Vuex from 'vuex'
import Api from './api'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    lastError: '',
    teams: [],
    games: [],
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
    }
  }
})

export default store;
