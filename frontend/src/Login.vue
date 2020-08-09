<template>
    <v-card class="elevation-12">
        <v-toolbar color="primary" dark flat>
            <v-toolbar-title><v-icon>mdi-football</v-icon> Gridiron: Login</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
            <v-form>
                <v-text-field label="Username" name="username" autofocus prepend-icon="mdi-account" type="text" v-model="username"></v-text-field>
                <v-text-field label="Password" name="password" prepend-icon="mdi-lock" type="password" v-model="password" v-on:keydown.enter="login()"></v-text-field>
            </v-form>
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn @click.stop="signUp()">create new account</v-btn>
            <v-btn @click.stop="login()" color="primary">Login</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
export default {
  name: 'Login',
  data: () => ({
    username: '',
    password: '',
  }),
  methods: {
    signUp() {
      this.$emit('signUp')
    },
    login() {
      this.$emit('login', {
        username: this.username,
        password: this.password
      })
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
