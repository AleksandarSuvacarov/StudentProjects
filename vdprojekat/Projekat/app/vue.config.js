const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false
})

module.exports = {
  chainWebpack: config => {
    config.resolve.alias.set('jquery', 'jquery/dist/jquery.slim.js');
    config.plugin('provide').use(require('webpack').ProvidePlugin, [{
      $: 'jquery',
      jQuery: 'jquery',
      'window.jQuery': 'jquery'
    }]);
  }
};




