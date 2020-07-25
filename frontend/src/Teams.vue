<template>
    <div>
        <h1>Teams</h1>

        <div v-for="team in teams" :key="team.uuid">
            {{ team.name }}
        </div>
    </div>
</template>

<script>
import Api from '@/api.js';

export default {
  name: 'app',
  data: () => ({
    loading: false,
    teams: [],
    error: null,
  }),
  created() {
    this.fetchData();
  },
  watch: {
    $route: 'fetchData',
  },
  methods: {
    fetchData() {
      this.loading = true;

      Api.get('/teams').then((response, error) => {
        if (error) {
          this.error = error.toString();
        } else {
          this.teams = response.data;
        }
      });

      this.loading = false;
    },
  },
};
</script>

<style lang="scss">
</style>
