<template>
  <div id="repository">
    <RepositoryEmojisConfigurations :repository-id="repositoryId" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';
import RepositoryEmojisConfigurations from './views/RepositoryEmojisConfigurations.vue';

@Component({
  components: {
    RepositoryEmojisConfigurations,
  },
})
export default class Emoji2slackRepository extends Vue {
  @Prop() private repositoryId: any;
}
</script>

<style>

#repository {
  margin: 0px;
}

</style>
