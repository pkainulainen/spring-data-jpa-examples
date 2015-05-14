'use strict';

angular.module('app.todo.controllers', [])
    .config(['$stateProvider',
        function ($stateProvider) {
            $stateProvider
                .state('todo', {
                    url: '/',
                    abstract: true,
                    template: '<ui-view/>'
                } )
                .state('todo.list', {
                    url: '',
                    controller: 'TodoListController',
                    templateUrl: 'frontend/partials/todo/todo-list.html'
                });
        }
    ])
    .controller('TodoListController', [function () {
            console.log('Rendering todo entry list page');
        }]);