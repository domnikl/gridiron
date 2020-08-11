<template>
    <div>
        <v-dialog :value="!!team" @click:outside="close()" @keydown.esc="close()">
            <v-card>
                <v-card-title class="headline">Create new team</v-card-title>

                <v-card-text>
                    <v-text-field label="Name" v-model="name" autofocus v-on:keydown.enter="save()"></v-text-field>
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
export default {
  name: 'TeamFormDialog',
  props: ['team'],
  data: () => ({
    name: null,
  }),
  watch: {
    team(newValue) {
      if (newValue.name) this.name = newValue.name
    }
  },
  methods: {
    close() {
      this.name = null
      this.$emit('close')
    },
    save() {
      this.$emit('save', {
        uuid: this.team.uuid,
        name: this.name,
      })
      this.name = null
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
