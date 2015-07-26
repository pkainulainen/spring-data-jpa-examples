'use strict';

angular.module('app.common.config', [])
    .config(['$urlRouterProvider', '$locationProvider',
        function ($urlRouterProvider, $locationProvider) {
            //this prevents infinite $digest loop when we invoke the
            //preventDefault() method in $stateChangeStart event handler.
            //See: https://github.com/angular-ui/ui-router/issues/600#issuecomment-47228922
            $urlRouterProvider.otherwise( function($injector, $location) {
                var $state = $injector.get("$state");
                $state.go("todo.list");
            });

            // Without server side support html5 must be disabled.
            $locationProvider.html5Mode(false);
        }
    ])
    .config(['$translateProvider', function ($translateProvider) {
        // Initialize angular-translate
        $translateProvider.useStaticFilesLoader({
            prefix: '/i18n/',
            suffix: '.json'
        });

        $translateProvider.preferredLanguage('en');
        $translateProvider.useSanitizeValueStrategy('escaped');
        $translateProvider.useLocalStorage();
        $translateProvider.useMissingTranslationHandlerLog();
    }])
    .config(['growlProvider', function (growlProvider) {
        growlProvider.globalTimeToLive(5000);
    }]);