'use strict';

angular.module('app.common.config', [])
    .constant('COMMON_EVENTS', {
        notFound: 'event:not-found'
    })
    .config(['logEnhancerProvider', function (logEnhancerProvider) {
        logEnhancerProvider.datetimePattern = 'DD.MM.YYYY HH:mm:ss';
        logEnhancerProvider.prefixPattern = '%s::[%s]> ';
        logEnhancerProvider.logLevels = {
            '*': logEnhancerProvider.LEVEL.OFF
        };
    }])
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
    }])
    .config(['$httpProvider', function ($httpProvider) {
        $httpProvider.interceptors.push([
            '$injector',
            function ($injector) {
                return $injector.get('404Interceptor');
            }
        ]);
    }])
    .factory('404Interceptor', ['$rootScope', '$q', 'COMMON_EVENTS', function ($rootScope, $q, COMMON_EVENTS) {
        return {
            responseError: function(response) {
                if (response.status === 404) {
                    $rootScope.$broadcast(COMMON_EVENTS.notFound);
                }
                return $q.reject(response);
            }
        };
    }]);
