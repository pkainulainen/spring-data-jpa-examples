'use strict';

angular.module('app.account.config', [])
    .constant('AUTH_EVENTS', {
        loginSuccess: 'event:auth-login-success',
        loginFailed: 'event:auth-login-failed',
        logoutSuccess: 'event:auth-logout-success',
        sessionTimeout: 'event:auth-session-timeout',
        notAuthenticated: 'event:auth-loginRequired',
        notAuthorized: 'event:auth-forbidden'
    })
    .config(['csrfProvider', function(csrfProvider) {
        // optional configurations
        csrfProvider.config({
            httpTypes: ['PUT', 'POST', 'DELETE'],
            maxRetries: 1,
            url: '/api/csrf'
        });
    }]);