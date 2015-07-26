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
    .controller('ForbiddenController', [function() {
        console.log("Render forbidden view.");
    }])
    .controller('LoginController', [function() {
        console.log('Rendering login form.');
    }]);

