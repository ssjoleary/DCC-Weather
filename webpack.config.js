var webpack = require('webpack');

var config = {
    entry: ["./resources/js/weather.js"],
  output: {
    path: "./build",
    filename: "bundle.js"
  },
  resolve: {
    extensions: ["", ".js", ".jsx"]
  },
  module: {
    loaders: [
      {
        test: /\.jsx?$/,
        loader: "babel",
        exclude: /node_modules/
      }
    ]
  },
  plugins: [
    new webpack.NoErrorsPlugin()
]
};

module.exports = config;