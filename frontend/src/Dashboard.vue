<template>
    <div>
        <v-card>
            <v-card-title>
                Highscore
            </v-card-title>

            <v-data-table :loading="loading" :headers="headers" :items="users" :disable-sort="true">
                <template v-slot:item.username="{ item }">
                    {{ item.username }} <v-chip x-small v-if="item.uuid === currentUser.uuid" color="primary">that's you!</v-chip>
                </template>
                <template v-slot:item.score="{ item }">
                    <v-icon v-if="isGoldTrophy(item)" color="#FFDF00">mdi-trophy</v-icon>
                    <v-icon v-if="isSilverTrophy(item)" color="#D3D3D3">mdi-trophy</v-icon>
                    <v-icon v-if="isBronzeTrophy(item)" color="#cc8e34">mdi-trophy</v-icon>
                    {{ item.score }}
                </template>
            </v-data-table>
        </v-card>
    </div>
</template>

<script>
export default {
  name: 'Dashboard',
  data: () => ({
    loading: true,
    headers: [
      {
        text: 'user',
        value: 'username',
      },
      {
        text: 'score',
        value: 'score',
      }
    ],
  }),
  computed: {
    currentUser() { return this.$store.state.user },
    users() { return this.$store.state.users },
  },
  created() {
    this.fetchData();
  },
  watch: {
    $route: 'fetchData',
  },
  methods: {
    fetchData() {
      this.loading = true;
      this.$store.dispatch('GET_USERS')
        .finally(() => {
          this.loading = false
        })
    },
    isGoldTrophy(user) { return this.users[0] && this.users[0].uuid === user.uuid },
    isSilverTrophy(user) { return this.users[1] && this.users[1].uuid === user.uuid },
    isBronzeTrophy(user) { return this.users[2] && this.users[2].uuid === user.uuid },
  },
};
</script>

<style lang="scss" scoped>
</style>
