<template>
    <v-dialog :value="!!game" v-if="!!game" @click:outside="close()" @keydown.esc="close()">
        <v-card>
            <v-card-title class="headline">What's your bet on the score?</v-card-title>

            <v-card-text>
                <v-text-field :label="game.team1.name" v-model="away" append-outer-icon="mdi-scoreboard-outline" autofocus></v-text-field>
                at
                <v-text-field :label="game.team2.name" v-model="home" append-outer-icon="mdi-scoreboard-outline" v-on:keydown.enter="save()"></v-text-field>

                Game starts at {{ start }}
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
  name: 'PlaceBetDialog',
  props: ['game', 'bet'],
  data() {
    return {
      home: null,
      away: null,
    }
  },
  watch: {
    bet(newValue) {
      if (newValue === null) return

      this.home = newValue.score.home
      this.away = newValue.score.away
    }
  },
  computed: {
    start() { return moment(this.game.start).tz(Intl.DateTimeFormat().resolvedOptions().timeZone).format('lll z') }
  },
  methods: {
    close() {
      this.home = null
      this.away = null
      this.$emit('close')
    },
    save() {
      this.$emit('placeBet', {
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
