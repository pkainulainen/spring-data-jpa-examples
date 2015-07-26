'use strict';

angular.module('app.account.services', ['ngResource'])
    .service('AuthenticatedUser', function () {
        this.create = function (username, role) {
            this.username = username;
            this.role = role;
        };
        this.destroy = function () {
            this.username = null;
            this.role = null;
        };
    })
    .factory('AuthenticationService', ['$http', '$resource', '$rootScope', '$state', 'AUTH_EVENTS', 'AuthenticatedUser',
        function($http, $resource, $rootScope, $state, AUTH_EVENTS, AuthenticatedUser) {

            var accountApi = $resource('/api/authenticated-user', {}, {get: {method: 'GET'}});

            return {
                authorizeStateChange: function(event, toState, toParams) {
                    console.log('Authorizing state change to state: ', toState);
                    if (toState.authenticate && !this.isAuthenticated()) {
                        event.preventDefault();

                        console.log('Authentication is not found. Fetching it from the backend.');
                        var self = this;
                        accountApi.get().$promise.then(function(user) {
                            console.log('Found authenticated user: ', user);
                            AuthenticatedUser.create(user.username, user.role);

                            if (!self.isAuthenticated) {
                                console.log('Unauthenticated users is: ', AuthenticatedUser);
                                $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
                            }
                            else {
                                console.log('User is authenticated. Continuing to the target state: ', toState);
                                $state.go(toState.name, toParams);
                            }
                        });
                    }
                },
                isAuthenticated: function() {
                    console.log('Checking if user is authenticated: ', AuthenticatedUser);
                    return AuthenticatedUser.username;
                },
                logIn: function(username, password) {
                    console.log('Logging in user with username: ', username);

                    var transform = function(data){
                        return $.param(data);
                    };

                    $http.post('/api/login', {username: username, password: password}, {
                        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                        transformRequest: transform
                    })
                        .success(function(user) {
                            console.log('Login successful: ', user);
                            AuthenticatedUser.create(user.username, user.role);
                            $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
                        })
                        .error(function() {
                            console.log('Login failed');
                            $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
                     });
                },
                logOut: function() {
                    if (this.isAuthenticated()) {
                        $http.post('/api/logout', {})
                            .success(function() {
                                console.log('User is logged out.');
                                AuthenticatedUser.destroy();
                                $rootScope.$broadcast(AUTH_EVENTS.logoutSuccess);
                            });
                    }
                }
            };
        }]);