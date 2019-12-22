<template>
  <div class="emoji2slack-configuration">
    <span class="emoji">{{ emoji }}</span><span class="channel">{{ channelId }}</span>
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

.emoji2slack-configuration {
  vertical-align: middle;
  width: fit-content;
  margin-bottom: 5px;
}

.emoji2slack-configuration .emoji {
  font-size: x-large;
  vertical-align: sub;
}

.emoji2slack-configuration .channel {
  margin-left: 15px;
  font-weight: bold;
}

</style>