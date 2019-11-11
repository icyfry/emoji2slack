<template>
  <div id="global">
    <GlobalConfiguration :repository-id="repositoryId" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';
import GlobalConfiguration from './views/GlobalConfiguration.vue';

@Component({
  components: {
    GlobalConfiguration,
  },
})
export default class Emoji2slackGlobal extends Vue {
  @Prop() private repositoryId: any;
}
</script>

<style>

#global {
  margin: 0px;
}

</style>
