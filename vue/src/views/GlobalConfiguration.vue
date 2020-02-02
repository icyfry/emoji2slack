<template>

  <div class="emoji2slack-global-configuration">

    <header class="aui-page-header">
      <div class="aui-page-header-inner">
        <div class="aui-page-header-main">
          <h2>Emoji-2-Slack Configuration</h2>
        </div>
      </div>
    </header>

    <form class="aui">

      <div class="field-group">
        <label for="bot-access-token">Bot Access Token</label>
        <input class="text long-field" type="text" id="bot-access-token" v-model="botAccessToken" />
      </div>

      <div class="buttons-container">
        <div class="buttons">
          <va-button type="primary" @click="saveConfiguration">Save</va-button>
        </div>
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

/* Provided by bitbucket */
declare var AJS: any;

@Component
export default class GlobalConfiguration extends Vue {

  @Prop() private errors: any[] = [];
  @Prop() private infos: any[] = [];
  @Prop() private botAccessToken!: string;

  /**
   * Constructor
   */
  public created() {
    this.callConfiguration();
  }

  /**
   * Handle an error
   */
  public handleError(e) {
      if (e.response.data.message !== null) {
        this.errors.push(e.response.data.message);
      } else {
        this.errors.push(e.message);
      }
  }

  /**
   * Rest call to get configuration
   */
  public callConfiguration() {
    axios.get(AJS.contextPath() + `/rest/emoji2slack/1.0/configuration`)
    .then((response) => {
      this.botAccessToken = response.data.botAccessToken;
    })
    .catch((e) => {
      this.handleError(e);
    });
  }

  /**
   * Rest call to save global configuration
   */
  public saveConfiguration() {
    axios.post(AJS.contextPath() + `/rest/emoji2slack/1.0/configuration`, {
        botAccessToken: this.botAccessToken,
    })
    .then((response) => {
      this.infos.push(`Configuration saved`);
      this.callConfiguration();
    })
    .catch((e) => {
      this.handleError(e);
    });
  }

}
</script>

<style>

.emoji2slack-global-configuration {
  width: 100%;
}

</style>