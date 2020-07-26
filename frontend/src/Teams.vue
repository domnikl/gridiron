<template>
    <div>
        <h1>Teams</h1>

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
</template>

<script>
import Api from '@/api.js';

export default {
  name: 'app',
  data: () => ({
    loading: false,
    teams: [],
    error: null,
  }),
  created() {
    this.fetchData();
  },
  watch: {
    $route: 'fetchData',
  },
  methods: {
    fetchData() {
      this.loading = true;

      Api.get('/teams').then((response, error) => {
        if (error) {
          this.error = error.toString();
        } else {
          this.teams = response.data;
        }
      });

      this.loading = false;
    },
  },
};
</script>

<style lang="scss" scoped>
.md-card {
    height: 120px;
}
</style>
