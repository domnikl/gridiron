<template>
    <div>
        <md-dialog :md-active="show" v-on:md-closed="close()" v-on:md-clicked-outside="close()">
            <md-dialog-title>Create new game</md-dialog-title>

            <md-dialog-content>
                <md-field>
                    <label for="team1">Team 1</label>
                    <md-select v-model="team1" name="team1" id="team1">
                        <md-option v-for="team in teams1" :key="team.uuid" :value="team.uuid">{{team.name}}</md-option>
                    </md-select>
                </md-field>

                <md-field>
                    <label for="team2">Team 2</label>
                    <md-select v-model="team2" name="team2" id="team2">
                        <md-option v-for="team in teams2" :key="team.uuid" :value="team.uuid">{{team.name}}</md-option>
                    </md-select>
                </md-field>

                <md-field>
                    <label for="start">Start</label>
                    <datetime v-model="start" type="datetime" id="start"></datetime>
                </md-field>
            </md-dialog-content>

            <md-dialog-actions>
                <md-button class="md-primary" @click="close()">Close</md-button>
                <md-button class="md-primary" @click="save()">Save</md-button>
            </md-dialog-actions>
        </md-dialog>
    </div>
</template>

<script>
import { Datetime } from 'vue-datetime';

const filterTeams = (teams, selected) => teams.filter((e) => selected === null || e.uuid !== selected)

export default {
  name: 'CreateGame',
  props: ['show'],
  components: { datetime: Datetime },
  data: () => ({
    team1: null,
    team2: null,
    start: null,
  }),
  computed: {
    teams() { return this.$store.state.teams },
    teams1() { return filterTeams(this.teams, this.team2); },
    teams2() { return filterTeams(this.teams, this.team1); },
  },
  created() {
    this.fetchData();
  },
  methods: {
    close() {
      this.team1 = null;
      this.team2 = null;
      this.start = null;

      this.$emit('closed')
    },
    save() {
      this.$store.dispatch('SAVE_GAME', {
        team1: this.team1,
        team2: this.team2,
        start: this.start
      }).then(() => {
        this.close()
      })
    },
    fetchData() {
      this.loading = true;
      this.$store.dispatch('GET_TEAMS').finally(() => {
        this.loading = false
      })
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
