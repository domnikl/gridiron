<template>
    <v-card class="elevation-12">
        <v-toolbar color="primary" dark flat>
            <v-toolbar-title>Gridiron register new user account</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
            <v-form>
                <v-text-field label="Email" name="email" autofocus prepend-icon="mdi-email" type="email" v-model="email"></v-text-field>
                <v-text-field label="Username" name="username" prepend-icon="mdi-account" type="text" v-model="username"></v-text-field>
                <v-text-field label="Password" name="password" prepend-icon="mdi-lock" type="password" v-model="password" v-on:keydown.enter="signUp"></v-text-field>
            </v-form>
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn @click.stop="login">login</v-btn>
            <v-btn @click.stop="signUp" color="primary">sign up</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
export default {
  name: 'SignUp',
  data: () => ({
    username: '',
    password: '',
    email: '',
  }),
  methods: {
    login() {
      this.$emit('login')
    },
    signUp() {
      this.$emit('signUp', {
        username: this.username,
        password: this.password,
        email: this.email,
      })
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
