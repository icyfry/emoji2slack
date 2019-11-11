<template>

  <div class="configurations">

    <header class="aui-page-header">
      <div class="aui-page-header-inner">
        <div class="aui-page-header-main">
          <h2>Emoji-2-Slack Configuration</h2>
        </div>
        <div class="aui-page-header-actions"></div>
      </div>
    </header>

    <div class="field-group">
      <label for="name">Configuration list</label>
      <ul v-if="configurations && configurations.length">
        <li v-bind:key="item" v-for="item in configurations">
          <EmojiConfiguration :channelId="item.channelId" :emoji="item.emoji" />
        </li>
      </ul>
    </div>

    <h4>Add a new configuration</h4>
    <div class="field-group">
      <label for="name">Channel</label>
      <input v-model="channelId" />
    </div>
    <div class="field-group">
      <label for="name">Emoji</label>
        <select v-model="emoji" >
          <option :selected="emoji==c" v-bind:key="c" v-for="(c,shortcut) in emojis" :value="shortcut">{{c}} ({{shortcut}})</option>
        </select>
    </div>
    <va-button @click="saveConfiguration">Save config</va-button>
    
    <!-- errors -->
    <template v-if="errors.length">
      <va-message type="error" v-bind:key="error" v-for="error in errors">
        <p>{{error}}</p>
      </va-message>
    </template>

    <!-- infos -->
    <template v-if="infos.length">
      <va-message v-bind:key="info" v-for="info in infos">
        <p>{{info}}</p>
      </va-message>
    </template>

  </div>

</template>

<script lang="ts">

import axios from 'axios';
import { Component, Prop, Vue } from 'vue-property-decorator';
import EmojiConfiguration from '@/components/EmojiConfiguration.vue';

/* Provided by bitbucket */
declare var AJS: any;

@Component({
  components: {
    EmojiConfiguration,
  },
})
export default class RepositoryEmojisConfigurations extends Vue {

  @Prop() private configurations: EmojiConfiguration[] = [];
  @Prop() private emojis: any;
  @Prop() private errors: any[] = [];
  @Prop() private infos: any[] = [];
  @Prop() private channelId: string = `#fakechannel`;
  @Prop() private emoji: string = `smile_cat`;
  @Prop() private repositoryId!: string;

  /**
   * Constructor
   */
  public created() {
    this.callConfigurations();
    this.callEmoji();
  }

  /**
   * Rest call to get configurations of current repository
   */
  public callConfigurations() {
    axios.get(AJS.contextPath() + `/rest/emoji2slack/1.0/emojis/configurations/` + this.repositoryId)
    .then((response) => {
      this.configurations = response.data.configurations;
    })
    .catch((e) => {
      this.errors.push(e);
    });
  }

  /**
   * Rest call to get all emojis that can be used in bitbucket
   */
  public callEmoji() {
    axios.get(AJS.contextPath() + `/rest/emoji2slack/1.0/emojis`)
    .then((response) => {
      this.emojis = response.data.emojis;
      this.emoji = this.emojis[0];
    })
    .catch((e) => {
      this.errors.push(e);
    });
  }

  /**
   * Rest call to save a new configuration
   */
  public saveConfiguration() {
    axios.post(AJS.contextPath() + `/rest/emoji2slack/1.0/emojis/configurations/add`, {
        channelId: this.channelId,
        emojiShortcut: this.emoji,
        repositoryId: this.repositoryId,
    })
    .then((response) => {
      this.infos.push(`Configuration saved`);
      this.callConfigurations();
    })
    .catch((e) => {
      this.errors.push(e);
    });
  }

}
</script>

<style>
.configurations {
  width: 100%;
}
</style>