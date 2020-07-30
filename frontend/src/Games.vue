<template>
    <div>
        <v-dialog v-model="showCreate">
            <v-card>
                <v-card-title class="headline">Create new game</v-card-title>

                <v-card-text>
                    <v-select v-model="team1" :items="teams1" item-text="name" item-value="uuid" label="Team 1" autofocus></v-select>
                    <v-select v-model="team2" :items="teams2" item-text="name" item-value="uuid" label="Team 2 (at)"></v-select>

                    <v-card flat class="justify-center">
                        <v-date-picker v-if="!selectDate" v-model="selectDate" full-width scrollable></v-date-picker>
                        <v-time-picker format="24hr" v-if="selectDate && !selectTime" v-model="selectTime" full-width scrollable></v-time-picker>
                    </v-card>

                    <div v-if="selectDate && selectTime">
                        <v-text-field @click:prepend="selectDate = null; selectTime = null" prepend-icon="mdi-clock" label="Date" v-model="start"></v-text-field>
                    </div>
                </v-card-text>

                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn @click="close()" text>Close</v-btn>
                    <v-btn @click="save()" text color="primary">Save</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <v-card>
            <v-card-title>
                Games
                <v-spacer></v-spacer>
                <v-text-field v-model="search" append-icon="mdi-magnify" label="Search" single-line hide-details></v-text-field>
            </v-card-title>

            <v-data-table :loading="loading" :headers="headers" :items="games" :search="search">
                <template v-slot:item.start="{ item }">
                    {{ formatDateTime(item.start) }}
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

const filterTeams = (teams, selected) => teams.filter((e) => selected === null || e.uuid !== selected)

export default {
  name: 'Games',
  data: () => ({
    loading: true,
    team1: null,
    team2: null,
    start: null,
    selectDate: '',
    selectTime: '',
    search: '',
    error: null,
    showCreate: false,
    headers: [
      {
        text: 'team',
        value: 'team1.name',
      },
      {
        text: 'at',
        value: 'team2.name',
      },
      {
        text: 'start',
        value: 'start',
      },
    ],
  }),
  computed: {
    games() { return this.$store.state.games },
    teams() { return this.$store.state.teams },
    teams1() { return filterTeams(this.teams, this.team2); },
    teams2() { return filterTeams(this.teams, this.team1); },
  },
  created() {
    this.fetchData();
  },
  watch: {
    $route: 'fetchData',
    selectDate() {
      this.start = this.formatDateTime(`${this.selectDate} ${this.selectTime}`);
    },
    selectTime() {
      this.start = this.formatDateTime(`${this.selectDate} ${this.selectTime}`);
    }
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
    close() {
      this.showCreate = false;
      this.team1 = null;
      this.team2 = null;
    },
    save() {
      this.$store.dispatch('SAVE_GAME', {
        team1: this.team1,
        team2: this.team2,
        start: this.start,
      }).then(() => {
        this.fetchData()
        this.close()
      })
    },
    formatDateTime(e) { return moment(e).tz('Europe/Berlin').format() }
  },
};
</script>

<style lang="scss" scoped>
</style>
