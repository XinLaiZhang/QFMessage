import footer from './src/main.vue';

/* istanbul ignore next */
footer.install = function(Vue) {
  Vue.component(footer.name, footer);
};

export default footer;