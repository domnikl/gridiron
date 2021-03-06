<template>
    <div class="page-container">
        <v-app v-if="!isLoggedIn">
            <v-main>
                <v-alert elevation="2" border="top" v-model="showErrors" dismissible type="error">
                    <v-row justify="center">{{ errorMessage }}</v-row>
                </v-alert>

                <NotLoggedIn></NotLoggedIn>
            </v-main>
        </v-app>

        <v-app v-if="isLoggedIn">
            <v-app-bar app clipped-right color="#1976d2" dark>
                <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
                <v-toolbar-title>Gridiron</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-toolbar-title>
                    <v-icon color="white">mdi-account</v-icon>{{ loggedInUser.username }}

                    <v-btn small @click="logout" color="white">
                        <v-icon small color="red">mdi-logout</v-icon>
                    </v-btn>
                </v-toolbar-title>
            </v-app-bar>

            <v-navigation-drawer v-model="drawer" app>
                <v-list>
                    <v-list-item>
                        <v-list-item-title class="title">
                            Gridiron
                        </v-list-item-title>
                        <v-list-item-subtitle>
                            Bet with your friends.
                        </v-list-item-subtitle>
                    </v-list-item>

                    <v-list-item to="/">
                        <v-list-item-action>
                            <v-icon>mdi-view-dashboard-outline</v-icon>
                        </v-list-item-action>
                        <v-list-item-content>
                            <v-list-item-title>Dashboard</v-list-item-title>
                        </v-list-item-content>
                    </v-list-item>
                    <v-list-item to="/games">
                        <v-list-item-action>
                            <v-icon>mdi-football</v-icon>
                        </v-list-item-action>
                        <v-list-item-content>
                            <v-list-item-title>Games</v-list-item-title>
                        </v-list-item-content>
                    </v-list-item>
                    <v-list-item to="/teams">
                        <v-list-item-action>
                            <v-icon>mdi-account-group</v-icon>
                        </v-list-item-action>
                        <v-list-item-content>
                            <v-list-item-title>Teams</v-list-item-title>
                        </v-list-item-content>
                    </v-list-item>
                </v-list>

                <div class="pa-2 logout-nav-bar">
                    <v-btn block color="error">Logout</v-btn>
                </div>
            </v-navigation-drawer>

            <v-main>
                <v-alert elevation="2" border="top" v-model="showErrors" dismissible type="error">
                    <v-row justify="center">{{ errorMessage }}</v-row>
                </v-alert>

                <v-container fluid>
                    <v-row justify="start" align="start">
                        <v-col cols="12" sm="12" md="12">
                            <router-view></router-view>
                        </v-col>
                    </v-row>
                </v-container>
            </v-main>
        </v-app>
    </div>
</template>

<script>
import NotLoggedIn from './NotLoggedIn.vue';

export default {
  name: 'App',
  components: { NotLoggedIn },
  data: () => ({
    drawer: false,
    showErrors: false,
    showErrorsFor: 3000,
  }),
  watch: {
    errorMessage(newValue) {
      if (newValue != null) {
        setTimeout(() => {
          this.$store.commit('SET_ERROR', null)
        }, this.showErrorsFor)

        this.showErrors = true;
      } else {
        this.showErrors = false;
      }
    },
  },
  computed: {
    loggedInUser() { return this.$store.state.user },
    isLoggedIn() { return this.loggedInUser != null; },
    errorMessage() { return this.$store.state.lastError; }
  },
  methods: {
    logout() {
      this.$store.dispatch('LOGOUT')
    }
  }
}
</script>

<style lang="scss" scoped>
.logout-nav-bar {
    position: absolute;
    bottom: 0;
    width: 100%;
}

.v-alert {
    margin: 5px;
}
</style>
