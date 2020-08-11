<template>
    <div>
        <TeamFormDialog
            :show="showCreate"
            @close="showCreate = false"
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
                        </v-card>
                    </v-col>
                </v-row>
            </template>
        </v-data-iterator>

        <v-btn v-if="this.$store.state.user.isAdmin" color="pink" dark fixed bottom right fab @click.stop="showCreate = true">
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
    showCreate: false,
    name: '',
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
    saveTeam() {
      this.$store.dispatch('SAVE_TEAM', {
        name: this.name,
      }).then(() => {
        this.fetchData()
        this.showCreate = false
      })
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
