<template>
    <v-app>
        <v-main>
            <v-container class="fill-height" fluid>
                <v-row align="center" justify="center">
                    <v-col cols="12" sm="8" md="4">
                        <Login v-if="showLogin" v-on:login="login" v-on:signUp="showLogin = false"></Login>
                        <SignUp v-if="!showLogin" v-on:signUp="signUp" v-on:login="showLogin = true"></SignUp>
                    </v-col>
                </v-row>

                <v-snackbar v-model="snackbar">
                    <v-row justify="center">
                        {{ message }}
                    </v-row>
                </v-snackbar>
            </v-container>
        </v-main>
    </v-app>
</template>

<script>
import Login from './Login.vue';
import SignUp from './SignUp.vue';

export default {
  name: 'NotLoggedIn',
  components: { Login, SignUp },
  data: () => ({
    showLogin: true,
    snackbar: false,
    message: '',
  }),
  methods: {
    login(credentials) {
      this.$store.dispatch('LOGIN', credentials).catch(() => {
        this.message = 'Invalid credentials';
        this.snackbar = true;
      })
    },
    signUp(data) {
      this.$store.dispatch('SIGN_UP', data).catch(() => {
        this.message = 'Please fill in all the fields.';
        this.snackbar = true;
      }).then(() => {
        this.message = 'Your account has been created but must be activated by an administrator.';
        this.snackbar = true;
      })
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
