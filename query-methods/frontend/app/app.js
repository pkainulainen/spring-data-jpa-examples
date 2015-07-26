'use strict';

console.log('Registering AngularJS modules');

var App = angular.module('app', [
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
    'spring-security-csrf-token-interceptor',

    //Partials
    'templates',

    //Account
    'app.account.config', 'app.account.directives', 'app.account.controllers', 'app.account.services',

    //Common
    'app.common.config', 'app.common.directives', 'app.common.services',

    //Todo
    'app.todo.controllers', 'app.todo.directives', 'app.todo.services',

    //Search
    'app.search.controllers', 'app.search.directives', 'app.search.services'

]);

App.run(['$rootScope', '$state', 'AUTH_EVENTS', 'AuthenticatedUser', 'authService', 'AuthenticationService',
    function ($rootScope, $state, AUTH_EVENTS, AuthenticatedUser, authService, AuthenticationService) {

        //This function retries all requests that were failed because of
        //the 401 response.
        function listenAuthenticationEvents() {
            var confirmLogin = function() {
                authService.loginConfirmed();
            };

            $rootScope.$on(AUTH_EVENTS.loginSuccess, confirmLogin);

            var viewLogInPage = function() {
                console.log('User is not authenticated.');
                $state.go('login');
            };

            $rootScope.$on(AUTH_EVENTS.logoutSuccess, viewLogInPage);
            $rootScope.$on(AUTH_EVENTS.notAuthenticated, viewLogInPage);

            var viewForbiddenPage = function() {
                console.log('Permission was denied for user: ', AuthenticatedUser);
                $state.go('forbidden');
            };

            $rootScope.$on(AUTH_EVENTS.notAuthorized, viewForbiddenPage);
        }

        //This function ensures that anonymous users cannot access states
        //that marked as protected (i.e. the value of the authenticated
        //property is set to true).
        function secureProtectedStates() {
            $rootScope.$on('$stateChangeStart', function (event, toState, toParams) {
                console.log('Moving to state: ', toState);
                AuthenticationService.authorizeStateChange(event, toState, toParams);
            });
        }

        $rootScope.currentUser = AuthenticatedUser;

        listenAuthenticationEvents();
        secureProtectedStates();
}]);

console.log('Registered AngularJS modules');

