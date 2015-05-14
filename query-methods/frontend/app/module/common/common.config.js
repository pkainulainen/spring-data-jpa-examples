'use strict';

angular.module('app.common.config', [])
    .config(['$urlRouterProvider', '$locationProvider',
        function ($urlRouterProvider, $locationProvider) {
            $urlRouterProvider.otherwise('/');

            // Without server side support html5 must be disabled.
            $locationProvider.html5Mode(false);
        }
    ])
    .config(['$translateProvider', function ($translateProvider) {
        // Initialize angular-translate
        $translateProvider.useStaticFilesLoader({
            prefix: '/frontend/i18n/',
            suffix: '.json'
        });

        $translateProvider.preferredLanguage('en');
        $translateProvider.useLocalStorage();
        $translateProvider.useMissingTranslationHandlerLog();
    }])
    .config(['growlProvider', function (growlProvider) {
        growlProvider.globalTimeToLive(5000);
    }]);