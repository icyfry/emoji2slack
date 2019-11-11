import Vue from 'vue';
import Emoji2slackGlobal from './Emoji2slackGlobal.vue';
import Emoji2slackRepository from './Emoji2slackRepository.vue';
import router from './router';
import VueAui from 'vue-aui';

Vue.config.productionTip = false;

Vue.use(VueAui);

if (document.getElementById('emoji-slack-global') != null) {
  new Vue({
    router,
    render: (h) => h(Emoji2slackGlobal),
  }).$mount('#emoji-slack-global');
}

if (document.getElementById('emoji-slack-repository') != null) {
  new Vue({
    router,
    render: (h) => h(Emoji2slackRepository, {
      props:{ 
        repositoryId: (document) ? (document.querySelector('#emoji-slack-repository') as any).dataset.repositoryId : null,
      },
    }),
  }).$mount('#emoji-slack-repository');
}
