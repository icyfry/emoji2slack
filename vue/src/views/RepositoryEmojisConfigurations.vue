<template>

  <div class="emoji2slack-repository-configuration">

    <header class="aui-page-header">
      <div class="aui-page-header-inner">
        <div class="aui-page-header-main">
          <h2>Emoji-2-Slack Configuration</h2>
        </div>
        <div class="aui-page-header-actions"></div>
      </div>
    </header>

    <form class="aui">

      <h4>Configurations list</h4>
      <div>
        <div class="field-group ">
          <ul id="emojis-configurations" v-if="!loadingConfigurations">
            <li v-bind:key="item" v-for="item in configurations">
              <EmojiConfiguration :channelId="item.channelId" :emoji="item.emoji" />
              <va-button v-on:click="deleteConfiguration(item.id)">delete</va-button>
            </li>
          </ul>
          <aui-spinner v-else size="medium"></aui-spinner>
        </div>
      </div>

      <h4>New configuration</h4>
      <div v-if="emojis">
        <div class="field-group">
          <label for="name">Channel</label>
          <input placeholder="#channel"  class="text" type="text" v-model="channelId" />
        </div>
        <div class="field-group">
          <label for="name">Emoji</label>
            <select  id="emoji-select" v-model="emoji" >
              <option v-bind:key="c" v-for="(c,shortcut) in emojis" :value="shortcut">{{c}}</option>
            </select>
        </div>
        <div class="buttons-container">
          <div class="buttons">
            <va-button v-on:click="saveConfiguration()">Save configuration</va-button>
          </div>
        </div>        
      </div>
      <div v-else class="field-group">
        <aui-spinner size="small"></aui-spinner>
      </div>

    </form>

    <template v-if="errors.length">
      <va-message type="error" v-bind:key="error" v-for="error in errors">
        <p>{{error}}</p>
      </va-message>
    </template>

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
  @Prop() private channelId: string = ``;
  @Prop() private emoji: string = `cat`;
  @Prop() private repositoryId!: string;
  @Prop() private loadingConfigurations: boolean = false;

  /**
   * Initialize
   */
  public created() {
    this.callConfigurations();
    this.callEmoji();
  }

  /**
   * Handle an error
   */
  public handleError(e) {
      if(e.response.data.message !== null) {
        this.errors.push(e.response.data.message);
      } else {
        this.errors.push(e.message);
      }
  }

  /**
   * Rest call to get configurations of current repository
   */
 public callConfigurations() {
    this.loadingConfigurations = true;
    axios.get(AJS.contextPath() + `/rest/emoji2slack/1.0/emojis/configurations/` + this.repositoryId)
    .then((response) => {
      this.loadingConfigurations = false;
      this.configurations = response.data.configurations;
    })
    .catch((e) => {
      this.loadingConfigurations = false;
      this.handleError(e);
    });
  }

  /**
   * Rest call to get all emojis that can be used in bitbucket
   */
  public callEmoji() {
    axios.get(AJS.contextPath() + `/rest/emoji2slack/1.0/emojis`)
    .then((response) => {
      this.emojis = response.data.emojis;
    })
    .catch((e) => {
      this.handleError(e);
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
      // this.infos.push(`Configuration saved`);
      this.callConfigurations();
    })
    .catch((e) => {
      this.handleError(e);
    });
  }

  /**
   * Rest call to delete a configuration
   */
  public deleteConfiguration(id) {
    axios.delete(AJS.contextPath() + `/rest/emoji2slack/1.0/emojis/configuration/` + id)
    .then((response) => {
      // this.infos.push(`Configuration deleted`);
      this.callConfigurations();
    })
    .catch((e) => {
      this.handleError(e);
    });
  }

}
</script>

<style>

.emoji2slack-repository-configuration {
  width: 100%;
}

.emoji2slack-repository-configuration #emoji-select {
  font-size: large;
  height: 50px;
  border-color: #dfe1e6;
}

.emoji2slack-repository-configuration #emojis-configurations {
  list-style: none;
}

.emoji2slack-repository-configuration #emojis-configurations li {
  margin: 12px;
}

</style>