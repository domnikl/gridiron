<template>
    <div class="page-container">
        <v-app>
            <v-app-bar app clipped-right color="#1976d2" dark>
                <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
                <v-toolbar-title>Gridiron</v-toolbar-title>
            </v-app-bar>

            <v-navigation-drawer v-model="drawer" app>
                <v-list dense>
                    <v-list-item to="/games">
                        <v-list-item-action>
                            <v-icon>mdi-football</v-icon>
                        </v-list-item-action>
                        <v-list-item-content>
                            <v-list-item-title>Games</v-list-item-title>
                        </v-list-item-content>
                    </v-list-item>
                    <v-list-item to="/teams">
                        <v-list-item-action>
                            <v-icon>mdi-account-group</v-icon>
                        </v-list-item-action>
                        <v-list-item-content>
                            <v-list-item-title>Teams</v-list-item-title>
                        </v-list-item-content>
                    </v-list-item>
                </v-list>
            </v-navigation-drawer>

            <v-main>
                <v-container fluid>
                    <v-row justify="start" align="start">
                        <v-col cols="12" sm="12" md="12">
                            <router-view></router-view>
                        </v-col>
                    </v-row>
                </v-container>
            </v-main>
        </v-app>
    </div>
</template>

<script>

export default {
  name: 'App',
  data: () => ({
    drawer: false,
  }),
};
</script>

<style lang="scss" scoped>
</style>
