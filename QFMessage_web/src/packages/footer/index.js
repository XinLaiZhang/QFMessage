import header from './src/main.vue';

/* istanbul ignore next */
header.install = function(Vue) {
  Vue.component(header.name, header);
};

export default header;