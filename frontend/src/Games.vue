<template>
    <div>
        <LoadingSpinner v-if="loading"></LoadingSpinner>
        <CreateGame :show="showCreate" v-on:md-closed="showCreate = false" v-on:md-clicked-outside="showCreate = false">
        </CreateGame>

        <md-table v-if="!loading" v-model="filtered" md-sort="name" md-sort-order="asc" md-card md-fixed-header>
            <md-table-toolbar>
                <div class="md-toolbar-section-start">
                    <h1 class="md-title"><md-icon>sports_football</md-icon> Games</h1>
                </div>

                <md-field md-clearable class="md-toolbar-section-end">
                    <md-input placeholder="Search by teams..." v-model="search" @input="searchOnTable" />
                </md-field>
            </md-table-toolbar>

            <md-table-empty-state
                md-label="No games found"
                :md-description="`No game found for this '${search}' query. Try a different search term.`">
                <md-button class="md-primary md-raised">Create New Game</md-button>
            </md-table-empty-state>

            <md-table-row slot="md-table-row" slot-scope="{ item }">
                <md-table-cell md-label="Team" md-sort-by="team1">{{ item.team1.name }}</md-table-cell>
                <md-table-cell md-label="At" md-sort-by="team2">{{ item.team2.name }}</md-table-cell>
                <md-table-cell md-label="start" md-sort-by="start">{{ item.start }}</md-table-cell>
            </md-table-row>
        </md-table>

        <md-speed-dial class="md-bottom-right">
            <md-speed-dial-target @click="create">
                <md-icon>add</md-icon>
            </md-speed-dial-target>
        </md-speed-dial>
    </div>
</template>

<script>
import Api from '@/api.js';
import LoadingSpinner from './components/LoadingSpinner.vue';
import CreateGame from './components/CreateGame.vue';

const toLower = (text) => text.toString().toLowerCase()

const searchByTeams = (items, term) => {
  if (term) {
    return items.filter((item) => toLower(item.team1.name).includes(toLower(term)) || toLower(item.team2.name).includes(toLower(term)))
  }

  return items
}

export default {
  name: 'Games',
  components: { LoadingSpinner, CreateGame },
  data: () => ({
    loading: true,
    games: [],
    filtered: [],
    search: '',
    error: null,
    showCreate: false,
  }),
  created() {
    this.fetchData();
  },
  watch: {
    $route: 'fetchData',
  },
  methods: {
    searchOnTable() {
      this.filtered = searchByTeams(this.games, this.search)
    },
    fetchData() {
      this.loading = true;

      Api.get('/games').then((response, error) => {
        if (error) {
          this.error = error.toString();
          this.loading = false;
        } else {
          this.filtered = response.data;
          this.games = response.data;
        }
      });

      this.loading = false;
    },
    create() {
      this.showCreate = true
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
