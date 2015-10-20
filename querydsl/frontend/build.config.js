'use strict';

var path = require('path');

var targetBase = './build/';

module.exports = {
    //Configures the directories in which the files created by Gulp are copied.
    target: {
        js: targetBase + '/js',
        lib: path.join(targetBase, 'js', 'lib'),
        css: path.join(targetBase, 'css'),
        partials: path.join(targetBase, 'partials'),
        assets: targetBase
    },

    //Configures the location of the used libraries and frameworks.
    vendorFiles: {
        code: [
            './bower_components/console-polyfill/index.js',
            './bower_components/lodash/dist/lodash.min.js',
            './bower_components/jquery/dist/jquery.min.js',
            './bower_components/angular/angular.js',
            './bower_components/moment/min/moment-with-locales.min.js',
            './bower_components/sprintf/dist/sprintf.min.js',
            './bower_components/angular-http-auth/src/http-auth-interceptor.js',
            './bower_components/angular-i18n/angular-locale_fi-fi.js',
            './bower_components/angular-cookies/angular-cookies.min.js',
            './bower_components/angular-moment/angular-moment.min.js',
            './bower_components/angular-logger/dist/angular-logger.min.js',
            './bower_components/angular-resource/angular-resource.min.js',
            './bower_components/angular-sanitize/angular-sanitize.min.js',
            './bower_components/angular-translate/angular-translate.min.js',
            './bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.min.js',
            './bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.min.js',
            './bower_components/angular-translate-storage-local/angular-translate-storage-local.min.js',
            './bower_components/angular-translate-handler-log/angular-translate-handler-log.min.js',
            './bower_components/angular-ui-router/release/angular-ui-router.min.js',
            './bower_components/angular-ui-utils/ui-utils.min.js',
            './bower_components/angular-ui-utils/ui-utils-ieshiv.min.js',
            './bower_components/angular-utils-pagination/dirPagination.js',
            './bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js',
            './bower_components/angular-growl-v2/build/angular-growl.min.js',
            './vendor/spring-security-csrf-token-interceptor/src/spring-security-csrf-token-interceptor.js'
        ]
    },

    //Configures the location of our application's files.
    appFiles: {
        //Configures the location of the Javascript files.
        code: [
            "./app/**/*.js"
        ],
        //Configures the location of the LESS files.
        styleBase: "./app/styles/",
        style: [
            "./bower_components/angular-growl-v2/build/angular-growl.min.css",
            "./app/styles/app.less"
        ],
        //Configures the location of the view templates.
        partials: [
            "./app/assets/partials/**/*.html"
        ],
        //Configures the location of static assets such as images, fonts, and localization files.
        assetsBase: './app/assets/',
        assets: [
            './app/assets/**'
        ],
        //Configures the location of shims (libraries that bring new APIs to older browsers)
        shim: [
            './bower_components/angular-loader/angular-loader.min.js',
            './bower_components/script.js/dist/script.min.js',
            './bower_components/es5-shim/es5-shim.min.js',
            './bower_components/json3/lib/json3.min.js'
        ]
    }
};