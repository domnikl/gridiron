<template>
    <v-dialog :value="!!game" v-if="!!game" @click:outside="close()" @keydown.esc="close()">
        <v-card>
            <v-card-title class="headline">
                End game
                <v-spacer></v-spacer>
                <span class="text-subtitle-1">{{ game.bets.length }} bets</span>
            </v-card-title>

            <v-card-text>
                <v-text-field :label="game.team1.name" v-model="away" append-outer-icon="mdi-scoreboard-outline" autofocus></v-text-field>
                at
                <v-text-field :label="game.team2.name" v-model="home" append-outer-icon="mdi-scoreboard-outline" v-on:keydown.enter="save()"></v-text-field>

                <p>
                    Game started at {{ start }}
                </p>

                <v-alert type="warning" border="right" elevation="2" dense>Are you sure? This change can not be reverted!</v-alert>
            </v-card-text>

            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn @click.stop="close()" text>Cancel</v-btn>
                <v-btn @click.stop="save()" text color="primary">Save</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
import moment from 'moment-timezone';

export default {
  name: 'EndGameDialog',
  props: ['game'],
  data() {
    return {
      home: null,
      away: null,
    }
  },
  computed: {
    start() { return moment(this.game.start).format('lll') }
  },
  methods: {
    close() {
      this.home = null
      this.away = null
      this.$emit('close')
    },
    save() {
      this.$emit('endGame', {
        game: { uuid: this.game.uuid },
        home: this.home,
        away: this.away
      })
      this.home = null
      this.away = null
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
