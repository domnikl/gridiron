<template>
    <div>
        <md-dialog :md-active="show" v-on:md-closed="close()" v-on:md-clicked-outside="close()">
            <md-dialog-title>Create new game</md-dialog-title>

            <md-dialog-content>
                <md-field>
                    <label for="team1">Team 1</label>
                    <md-select v-model="team1" name="team1" id="team1">
                        <md-option value="fight-club">Fight Club</md-option>
                    </md-select>
                </md-field>

                <md-field>
                    <label for="team2">Team 2</label>
                    <md-select v-model="team2" name="team2" id="team2">
                        <md-option value="fight-club">Fight Club</md-option>
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

export default {
  name: 'CreateGame',
  props: ['show'],
  components: { datetime: Datetime },
  data: () => ({
    team1: null,
    team2: null,
    start: null,
    teams: [],
  }),
  methods: {
    close() {
      this.$emit('md-closed')
    },
    save() {
      // TODO: commit to VueX store, trigger reload in background
      this.close()
    }
  },
};
</script>

<style lang="scss" scoped>
</style>
