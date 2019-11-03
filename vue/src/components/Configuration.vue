<template>
  <div class="configuration">
    <b>{{ channelId }}</b>
    <b>{{ emoji }}</b>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';

@Component
export default class Configuration extends Vue {
  @Prop() private channelId!: string;
  @Prop() private emoji!: string;
}
</script>

<style scoped>
.configuration {
  background-color: rgb(129, 129, 129); 
}
</style>