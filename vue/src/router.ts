import Vue from 'vue';
import Router from 'vue-router';
//import RepositoryEmojisConfigurations from './views/RepositoryEmojisConfigurations.vue';
//import GlobalConfigurations from './views/GlobalConfigurations.vue';
import About from './views/About.vue';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'about',
      component: About,
    },
    {
      path: '/repository',
      name: 'repository',
      component: () => import('./views/RepositoryEmojisConfigurations.vue'),
    },
    {
      path: '/global',
      name: 'global',
      component: () => import('./views/GlobalConfiguration.vue'),
    },
  ],
});
