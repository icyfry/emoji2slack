module.exports = {
    outputDir: "dist",
    publicPath: "bitbucket/download/resources/io.icyfry.bitbucket.emoji2slack:emoji2slack-resources",
    configureWebpack: {
        optimization: {
          splitChunks: false
        }
      },
      filenameHashing: false
  }