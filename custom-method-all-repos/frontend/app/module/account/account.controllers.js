'use strict';

angular.module('app.account.controllers', [])
    .config(['$stateProvider',
        function ($stateProvider) {
            $stateProvider
                .state('todo.login', {
                    url: 'login',
                    controller: 'LoginController',
                    templateUrl: 'account/login-view.html'
                })
                .state('todo.forbidden', {
                    url: 'forbidden',
                    controller: 'ForbiddenController',
                    templateUrl: 'account/forbidden-view.html'
                });
        }
    ])
    .controller('ForbiddenController', ['$log', function($log) {
        var logger = $log.getInstance('app.account.controllers.ForbiddenController');
        logger.info("Rendering forbidden view.");
    }])
    .controller('LoginController', ['$log', function($log) {
        var logger = $log.getInstance('app.account.controllers.LoginController');
        logger.info('Rendering login form.');
    }]);

