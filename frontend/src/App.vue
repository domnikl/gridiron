<template>
    <div id="app">
        <h1>Gridiron</h1>

        <p>
            <ul>
                <li><router-link to="/">Dashboard</router-link></li>
                <li><router-link to="/teams">Teams</router-link></li>
            </ul>
        </p>

        <router-view></router-view>
    </div>
</template>

<script>

export default {
  name: 'app',
};
</script>

<style lang="scss">
</style>
