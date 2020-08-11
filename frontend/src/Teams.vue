<template>
    <div>
        <TeamFormDialog
            :team="edit"
            @close="edit = null"
            @save="saveTeam">
        </TeamFormDialog>

        <h1><v-icon>mdi-account-group</v-icon> Teams</h1>

        <v-data-iterator :items="teams" :loading="loading" :items-per-page="itemsPerPage">
            <template v-slot:default="props">
                <v-row>
                    <v-col v-for="item in props.items" :key="item.uuid" cols="12" sm="6" md="4" lg="3">
                        <v-card>
                            <v-card-title class="subheading font-weight-bold">{{ item.name }}</v-card-title>
                            <v-card-subtitle>Football team</v-card-subtitle>
                            <v-card-actions>
                                <v-spacer></v-spacer>
                                <v-btn v-if="$store.state.user.isAdmin" icon color="primary" @click.stop="edit = item">
                                    <v-icon>mdi-pencil</v-icon>
                                </v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-col>
                </v-row>
            </template>
        </v-data-iterator>

        <v-btn v-if="this.$store.state.user.isAdmin" color="pink" dark fixed bottom right fab @click.stop="edit = {}">
            <v-icon>mdi-plus</v-icon>
        </v-btn>
    </div>
</template>

<script>
import TeamFormDialog from './TeamFormDialog.vue';

export default {
  name: 'Teams',
  components: { TeamFormDialog },
  data: () => ({
    itemsPerPage: 15,
    loading: false,
    error: null,
    edit: null,
  }),
  computed: {
    teams() { return this.$store.state.teams }
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
      this.$store.dispatch('GET_TEAMS').finally(() => {
        this.loading = false
      })
    },
    saveTeam(team) {
      this.$store.dispatch('SAVE_TEAM', {
        uuid: team.uuid,
        name: team.name,
      }).then(() => {
        this.fetchData()
        this.edit = null
      })
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
