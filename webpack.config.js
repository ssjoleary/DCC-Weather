var webpack = require('webpack');
var path = require('path');

var BUILD_DIR = path.resolve(__dirname, 'resources/public/build');
var APP_DIR = path.resolve(__dirname, 'resources/js');

var config = {
    entry: ['babel-polyfill', APP_DIR + '/weather.js'],
  output: {
    path: BUILD_DIR,
    filename: "bundle.js"
  },
  resolve: {
    extensions: ["", ".js", ".jsx"]
  },
  module: {
    loaders: [
      {
        test: /\.jsx?$/,
        include: APP_DIR,
        loader: "babel",
        exclude: /node_modules/
      }
    ]
  },
  plugins: [
    new webpack.NoErrorsPlugin(),
    new webpack.ProvidePlugin({    // <added>
        jQuery: 'jquery',
        $: 'jquery',
        jquery: 'jquery'   // </added>
    })
]
};

module.exports = config;