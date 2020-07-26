<template>
    <div class="page-container">
        <md-app md-mode="fixed">
            <md-app-toolbar class="md-primary">
                <md-button class="md-icon-button" @click="menuVisible = !menuVisible">
                    <md-icon>menu</md-icon>
                </md-button>

                <span class="md-title">Gridiron</span>
            </md-app-toolbar>

            <md-app-drawer :md-active.sync="menuVisible">
                <md-toolbar class="md-transparent" md-elevation="0">Navigation</md-toolbar>

                <md-list>
                    <md-list-item to="/">
                        <md-icon>move_to_inbox</md-icon>
                        <span class="md-list-item-router">Dashboard</span>
                    </md-list-item>
                    <md-list-item to="/games">
                        <md-icon>sports_football</md-icon>
                        <span class="md-list-item-router">Games</span>
                    </md-list-item>
                    <md-list-item to="/teams">
                        <md-icon>people</md-icon>
                        <span class="md-list-item-router">Teams</span>
                    </md-list-item>
                </md-list>
            </md-app-drawer>

            <md-app-content>
                <router-view></router-view>
            </md-app-content>
        </md-app>
    </div>
</template>

<script>

export default {
  name: 'app',
  data: () => ({
    menuVisible: false,
  }),
};
</script>

<style lang="scss" scoped>
.md-app {
    height: 100vh;
}
</style>
