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
    .factory('AuthenticationService', ['$http', '$log', '$rootScope', '$state', 'AUTH_EVENTS', 'AuthenticatedUser',
        function($http, $log, $rootScope, $state, AUTH_EVENTS, AuthenticatedUser) {

            var logger = $log.getInstance('app.account.services.AuthenticationService');

            return {
                authorizeStateChange: function(event, toState, toParams) {
                    logger.debug('Authorizing state change to state: %s', toState.name);
                    if (toState.authenticate && !this.isAuthenticated()) {
                        event.preventDefault();

                        logger.debug('Authentication is not found. Fetching it from the backend.');
                        var self = this;
                        $http.get('/api/authenticated-user').success(function(user) {
                            logger.debug('Found authenticated user: %j', user);
                            AuthenticatedUser.create(user.username, user.role);

                            if (!self.isAuthenticated) {
                                logger.debug('Unauthenticated users is: %j', AuthenticatedUser);
                                $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
                            }
                            else {
                                logger.debug('User is authenticated. Continuing to the target state: %s', toState.name);
                                $state.go(toState.name, toParams);
                            }
                        });
                    }
                },
                isAuthenticated: function() {
                    logger.debug('Checking if user: %j is authenticated.', AuthenticatedUser);
                    return AuthenticatedUser.username;
                },
                logIn: function(username, password) {
                    logger.info('Logging in user with username: %s', username);

                    var transform = function(data){
                        return $.param(data);
                    };

                    $http.post('/api/login', {username: username, password: password}, {
                        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                        ignoreAuthModule: true,
                        transformRequest: transform
                    })
                        .success(function(user) {
                            logger.info('Login successful for user: %j', user);
                            AuthenticatedUser.create(user.username, user.role);
                            $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
                        })
                        .error(function() {
                            logger.info('Login failed');
                            $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
                     });
                },
                logOut: function() {
                    if (this.isAuthenticated()) {
                        $http.post('/api/logout', {})
                            .success(function() {
                                logger.info('User is logged out.');
                                AuthenticatedUser.destroy();
                                $rootScope.$broadcast(AUTH_EVENTS.logoutSuccess);
                            });
                    }
                }
            };
        }]);