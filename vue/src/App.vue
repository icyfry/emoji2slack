<template>
  <div id="app">
    <router-view :repository-id="repositoryId" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';
import { Route } from 'vue-router';

@Component
export default class App extends Vue {
  @Prop() private repositoryId: any;
}
</script>

<style>

#app {
  margin: 0px;
}

#nav {
  padding: 0px;
}

</style>
