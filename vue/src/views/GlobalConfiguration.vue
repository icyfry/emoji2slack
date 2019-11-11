<template>

  <div class="configuration">

    <div class="field-group">
      <label for="name">botAccessToken</label>
      <input v-model="botAccessToken" />
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
  public mounted() {
    this.callConfiguration();
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
      this.errors.push(e);
    });
  }

  /**
   * Rest call to save global configuration
   */
  public saveConfiguration() {
    axios.post(AJS.contextPath() + `/rest/emoji2slack/1.0/configuration`, {
        botAccessToken: this.botAccessToken
    })
    .then((response) => {
      this.infos.push(`Configuration saved`);
      this.callConfiguration();
    })
    .catch((e) => {
      this.errors.push(e);
    });
  }

}
</script>

<style>
.configuration {
  width: 100%;
}
</style>