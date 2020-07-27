<template>
    <div>
        <LoadingSpinner v-if="loading"></LoadingSpinner>

        <div v-if="!loading">
            <h1><md-icon>people</md-icon> Teams</h1>

            <div class="md-layout md-gutter md-alignment-center">
                <div class="md-layout-item md-medium-size-33 md-small-size-50 md-xsmall-size-100"
                     v-for="team in teams" :key="team.uuid">
                    <md-card md-with-hover>
                        <md-ripple>
                            <md-card-header>
                                <div class="md-title">{{team.name}}</div>
                                <div class="md-subhead">Football Team</div>
                            </md-card-header>

                            <!--<md-card-content>
                                This is some content
                            </md-card-content>

                            <md-card-actions>
                                <md-button>Action</md-button>
                                <md-button>Action</md-button>
                            </md-card-actions>-->
                        </md-ripple>
                    </md-card>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import LoadingSpinner from './components/LoadingSpinner.vue';

export default {
  name: 'Teams',
  components: { LoadingSpinner },
  data: () => ({
    loading: false,
    error: null,
  }),
  computed: {
    teams() {
      return this.$store.state.teams
    }
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
  },
};
</script>

<style lang="scss" scoped>
.md-card {
    height: 120px;
}
</style>
