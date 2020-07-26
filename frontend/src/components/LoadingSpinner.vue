<template>
    <div>
        <md-progress-spinner md-mode="indeterminate"></md-progress-spinner>
    </div>
</template>

<script>
export default {
  name: 'LoadingSpinner',
};
</script>

<style lang="scss" scoped>
.md-progress-spinner {
    left: 50%;
    top: 100px;
}
</style>
