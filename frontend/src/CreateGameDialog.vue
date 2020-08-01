<template>
    <div>
        <v-dialog :value="show" v-if="!!show" @click:outside="close()" @keydown.esc="close()">
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
    </div>
</template>

<script>
import moment from 'moment-timezone';

const filterTeams = (teams, selected) => teams.filter((e) => selected === null || e.uuid !== selected)

export default {
  name: 'CreateGame',
  props: ['show'],
  data: () => ({
    team1: null,
    team2: null,
    start: null,
    selectDate: '',
    selectTime: '',
  }),
  computed: {
    teams() { return this.$store.state.teams },
    teams1() { return filterTeams(this.teams, this.team2); },
    teams2() { return filterTeams(this.teams, this.team1); },
  },
  watch: {
    selectDate() {
      this.start = this.formatDateTime(`${this.selectDate} ${this.selectTime}`);
    },
    selectTime() {
      this.start = this.formatDateTime(`${this.selectDate} ${this.selectTime}`);
    }
  },
  methods: {
    close() {
      this.team1 = null;
      this.team2 = null;
    },
    save() {
      this.$emit('save', {
        team1: this.team1,
        team2: this.team2,
        start: this.start,
      })
    },
    formatDateTime(e) { return moment(e).format() },
  },
};
</script>

<style lang="scss" scoped>
</style>
