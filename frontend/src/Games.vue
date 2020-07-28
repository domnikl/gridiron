<template>
    <div>
        <v-dialog v-model="showCreate">
            <v-card>
                <v-card-title class="headline">Create new game</v-card-title>

                <v-card-text>
                    <v-select v-model="team1" :items="teams1" item-text="name" item-value="uuid" label="Team 1"></v-select>
                    <v-select v-model="team2" :items="teams2" item-text="name" item-value="uuid" label="Team 2 (at)"></v-select>

                    <v-card flat class="justify-center">
                        <v-date-picker @change="selectDate = false; selectTime = true" v-if="selectDate" v-model="startDate" full-width></v-date-picker>
                        <v-time-picker @change="selectTime = false" format="24hr" v-if="selectTime" v-model="startTime" full-width></v-time-picker>
                    </v-card>

                    <div v-if="!selectDate && !selectTime">
                        <v-text-field @click="selectDate = true;" prepend-icon="mdi-clock" label="Date" :value="start"></v-text-field>
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

        <v-btn color="pink" dark fixed bottom right fab @click.stop="showCreate = true">
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
    startDate: null,
    startTime: null,
    selectDate: true,
    selectTime: false,
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
    start() {
      if (!this.startDate || !this.startTime) {
        return ''
      }

      return this.formatDateTime(`${this.startDate} ${this.startTime}`)
    },
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
      this.startDate = null;
      this.startTime = null;
    },
    save() {
      this.$store.dispatch('SAVE_GAME', {
        team1: this.team1,
        team2: this.team2,
        start: moment(this.start).format()
      }).then(() => {
        this.fetchData()
        this.close()
      })
    },
    formatDateTime(e) { return moment(e).tz('Europe/Berlin').format('lll') }
  },
};
</script>

<style lang="scss" scoped>
</style>
