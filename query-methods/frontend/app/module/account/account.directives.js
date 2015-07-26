'use strict';

angular.module('app.account.directives', [])
    .directive('logOutLink', ['AuthenticationService', function (AuthenticationService) {
        return {
            link: function (scope, element, attr) {
                scope.logOut = function() {
                    console.log('Logging user out.');
                    AuthenticationService.logOut();
                };
            },
            templateUrl: 'account/logout-link-directive.html',
            scope: {
                currentUser: '='
            }
        };
    }])
    .directive('loginForm', ['AUTH_EVENTS', 'AuthenticationService', function (AUTH_EVENTS, AuthenticationService) {
        return {
            link: function (scope, element, attr) {
                scope.login = {};
                scope.loginFailed = false;

                scope.$on(AUTH_EVENTS.loginFailed, function() {
                    console.log('Received login failed event.');
                    scope.loginFailed = true;
                });

                scope.submitLoginForm = function() {
                    console.log('Submitting log in form.');
                    AuthenticationService.logIn(scope.login.username, scope.login.password);
                };
            },
            templateUrl: 'account/login-form-directive.html',
            scope: {
            }
        };
    }]);