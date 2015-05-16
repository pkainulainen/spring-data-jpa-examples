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
                    templateUrl: 'todo/todo-list-view.html',
                    resolve: {
                        todoEntries: ['Todos', function(Todos) {
                            return Todos.findAll();
                        }]
                    }
                });
        }
    ])
    .controller('TodoListController', ['$scope', 'todoEntries', function ($scope, todoEntries) {
            console.log('Rendering todo entry list page for todo entries: ', todoEntries);
            $scope.todoEntries = todoEntries;
        }]);