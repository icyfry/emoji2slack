import Vue from 'vue';
import App from './App.vue';
import router from './router';
import VueAui from 'vue-aui';

Vue.config.productionTip = false;

Vue.use(VueAui);

new Vue({
  router,
  render: (h) => h(App, {
    props:{
      repositoryId: (document) ? (document.querySelector("#app") as any).dataset.repositoryId : null
    }
  }),
}).$mount('#app');
