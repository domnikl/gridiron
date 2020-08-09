<template>
    <div>
        <PlaceBetDialog
            :game="placeBetOn"
            :bet="getMyBet(placeBetOn)"
            @close="placeBetOn = null"
            @placeBet="placeBet"></PlaceBetDialog>

        <EndGameDialog
            :game="gameToEnd"
            @close="gameToEnd = null"
            @endGame="endGame"></EndGameDialog>

        <CreateGameDialog
            :show="showCreate"
            @close="showCreate = false"
            @save="saveGame"></CreateGameDialog>

        <v-card>
            <v-card-title>
                Games
                <v-spacer></v-spacer>
                <v-text-field v-model="search" append-icon="mdi-magnify" label="Search" single-line hide-details></v-text-field>
            </v-card-title>

            <v-data-table
                :loading="loading"
                :headers="headers"
                :items="games"
                :search="search"
                group-by="startDate">

                <template v-slot:group.header="{ group }">
                    <v-chip small>{{ group }}</v-chip>
                </template>
                <template v-slot:item.score="{ item }">
                    <v-icon v-if="$store.state.user.isAdmin" small class="mr-2" @click="gameToEnd = item">mdi-check</v-icon>

                    <v-icon v-if="getMyBet(item)" small class="mr-2" @click="startBetting(item)">mdi-scoreboard-outline</v-icon>
                    <span v-if="getMyBet(item)">{{ String(getMyBet(item).score.away).padStart(2, '0') }}:{{ String(getMyBet(item).score.home).padStart(2, '0') }}</span>&nbsp;
                    <v-btn color="primary" small @click="startBetting(item)">bet</v-btn>
                </template>
            </v-data-table>
        </v-card>

        <v-btn v-if="this.$store.state.user.isAdmin" color="pink" dark fixed bottom right fab @click.stop="showCreate = true">
            <v-icon>mdi-plus</v-icon>
        </v-btn>
    </div>
</template>

<script>
import moment from 'moment-timezone';
import CreateGameDialog from './CreateGameDialog.vue';
import PlaceBetDialog from './PlaceBetDialog.vue';
import EndGameDialog from './EndGameDialog.vue';

const filterTeams = (teams, selected) => teams.filter((e) => selected === null || e.uuid !== selected)

export default {
  name: 'Games',
  components: { CreateGameDialog, PlaceBetDialog, EndGameDialog },
  data: () => ({
    loading: true,
    search: '',
    showCreate: false,
    placeBetOn: null,
    gameToEnd: null,
    headers: [
      {
        text: 'away',
        value: 'team1.name',
      },
      {
        text: 'at',
        value: 'team2.name',
      },
      {
        text: 'start',
        value: 'startDateTime',
      },
      {
        text: 'your bet on score',
        value: 'score',
      },
    ],
  }),
  computed: {
    games() {
      const games = this.$store.state.games.filter((game) => game.score === null)

      games.map((g) => {
        g.startDate = moment(g.start).format('YYYY-MM-DD')
        g.startDateTime = moment(g.start).format('lll')
        return g
      })

      return games;
    },
    teams() { return this.$store.state.teams },
    teams1() { return filterTeams(this.teams, this.team2); },
    teams2() { return filterTeams(this.teams, this.team1); },
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
      this.$store.dispatch('GET_GAMES')
        .then(() => this.$store.dispatch('GET_TEAMS'))
        .finally(() => {
          this.loading = false
        })
    },
    saveGame(game) {
      this.$store.dispatch('SAVE_GAME', {
        team1: game.team1,
        team2: game.team2,
        start: game.start,
      }).then(() => {
        this.fetchData()
        this.showCreate = false
      })
    },
    startBetting(item) {
      if (moment(item.start).isBefore(moment())) {
        this.$store.commit('SET_ERROR', 'Game has already started.')
        return;
      }

      this.placeBetOn = item;
    },
    placeBet(bet) {
      this.$store.dispatch('PLACE_BET', bet)
        .then(() => {
          this.fetchData()
          this.placeBetOn = null;
        })
    },
    getMyBet(game) {
      if (game === null) {
        return null;
      }

      const filtered = game.bets.filter((e) => e.user === this.$store.state.user.uuid);

      if (filtered.length === 1) {
        return filtered[0]
      }

      return null;
    },
    endGame(payload) {
      this.$store.dispatch('END_GAME', {
        game: payload.game,
        away: payload.away,
        home: payload.home
      }).then(() => {
        this.gameToEnd = null;
        this.fetchData()
      })
    },
  },
};
</script>

<style lang="scss">
.theme--light.v-data-table .v-row-group__header {
    background: transparent;

    span {
        margin: 5px;
    }
}
</style>
