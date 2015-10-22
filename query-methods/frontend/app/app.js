'use strict';

var App = angular.module('app', [
    'angular-logger',
    'http-auth-interceptor',
    'ngLocale',
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'pascalprecht.translate',
    'ui.bootstrap',
    'ui.router',
    'ui.utils',
    'angular-growl',
    'angularMoment',
    'angularUtils.directives.dirPagination',
    'spring-security-csrf-token-interceptor',

    //Partials
    'templates',

    //Account
    'app.account.config', 'app.account.directives', 'app.account.controllers', 'app.account.services',

    //Common
    'app.common.config', 'app.common.controllers', 'app.common.directives', 'app.common.services',

    //Todo
    'app.todo.controllers', 'app.todo.directives', 'app.todo.services',

    //Search
    'app.search.controllers', 'app.search.directives', 'app.search.services'

]);

App.run(['$log', '$rootScope', '$state', 'AUTH_EVENTS', 'AuthenticatedUser', 'authService', 'AuthenticationService', 'COMMON_EVENTS',
    function ($log, $rootScope, $state, AUTH_EVENTS, AuthenticatedUser, authService, AuthenticationService, COMMON_EVENTS) {

        var logger = $log.getInstance('app');

        //This function retries all requests that were failed because of
        //the 401 response.
        function listenAuthenticationEvents() {
            var confirmLogin = function() {
                authService.loginConfirmed();
            };

            $rootScope.$on(AUTH_EVENTS.loginSuccess, confirmLogin);

            var viewLogInPage = function() {
                logger.info('User is not authenticated. Rendering login view.');
                $state.go('todo.login');
            };

            $rootScope.$on(AUTH_EVENTS.notAuthenticated, viewLogInPage);

            var viewTodoListPage = function() {
                logger.info("User logged out. REndering todo list view.");
                $state.go('todo.list', {}, {reload: true});
            };

            $rootScope.$on(AUTH_EVENTS.logoutSuccess, viewTodoListPage);

            var viewForbiddenPage = function() {
                logger.info('Permission was denied for user: %j', AuthenticatedUser);
                $state.go('todo.forbidden');
            };

            $rootScope.$on(AUTH_EVENTS.notAuthorized, viewForbiddenPage);
        }

        function listenCommonEvents() {

            var view404Page = function() {
                logger.info('Requested page was not found.');
                $state.go('todo.404');
            };

            $rootScope.$on(COMMON_EVENTS.notFound, view404Page);
        }

        //This function ensures that anonymous users cannot access states
        //that marked as protected (i.e. the value of the authenticated
        //property is set to true).
        function secureProtectedStates() {
            $rootScope.$on('$stateChangeStart', function (event, toState, toParams) {
                logger.trace('Moving to state: %s', toState.name);
                AuthenticationService.authorizeStateChange(event, toState, toParams);
            });
        }

        $rootScope.currentUser = AuthenticatedUser;

        listenAuthenticationEvents();
        listenCommonEvents();
        secureProtectedStates();
    }]);

