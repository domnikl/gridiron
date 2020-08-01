<template>
    <div class="page-container">
        <NotLoggedIn v-if="!isLoggedIn"></NotLoggedIn>

        <v-app v-if="isLoggedIn">
            <v-app-bar app clipped-right color="#1976d2" dark>
                <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
                <v-toolbar-title>Gridiron</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-toolbar-title>
                    <v-icon color="grey">mdi-account</v-icon>{{ loggedInUser.username }}

                    <v-btn text x-small @click="logout">
                        <v-icon small color="red">mdi-logout</v-icon>
                    </v-btn>
                </v-toolbar-title>
            </v-app-bar>

            <v-navigation-drawer v-model="drawer" app>
                <v-list dense>
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
            </v-navigation-drawer>

            <v-main>
                <v-container fluid>
                    <v-row justify="start" align="start">
                        <v-col cols="12" sm="12" md="12">
                            <router-view></router-view>
                        </v-col>
                    </v-row>
                </v-container>
            </v-main>

            <v-snackbar v-model="snackbar">
                <v-row justify="center">{{ snackbarMessage }}</v-row>
            </v-snackbar>
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
    snackbar: false,
  }),
  watch: {
    snackbarMessage() {
      this.snackbar = true;
    }
  },
  computed: {
    loggedInUser() { return this.$store.state.user },
    isLoggedIn() { return this.loggedInUser != null; },
    snackbarMessage() { return this.$store.state.lastError; }
  },
  methods: {
    logout() {
      this.$store.dispatch('LOGOUT')
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
