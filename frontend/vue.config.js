const path = require('path');

module.exports = {
    devServer: {
        proxy: {
            '^/api': {
                target: 'http://localhost:4000',
            },
        },
    },
    pluginOptions: {
        'style-resources-loader': {
            preProcessor: 'scss',
            patterns: [
                path.resolve(__dirname, 'src/scss/*.scss'),
            ],
        },
    },
};
