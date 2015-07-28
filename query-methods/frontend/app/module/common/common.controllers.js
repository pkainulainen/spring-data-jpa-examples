'use strict';

angular.module('app.common.controllers', [])
    .config(['$stateProvider',
        function ($stateProvider) {
            $stateProvider
                .state('todo.404', {
                    url: 'not-found',
                    controller: 'NotFoundController',
                    templateUrl: 'common/not-found-view.html'
                });
        }
    ])
    .controller('NotFoundController', ['$log', function($log) {
        var logger = $log.getInstance('app.common.controllers.NotFoundController');
        logger.info("Rendering 404 view.");
    }]);

