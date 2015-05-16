'use strict';

angular.module('app.todo.controllers', [])
    .config(['$stateProvider',
        function ($stateProvider) {
            $stateProvider
                .state('todo', {
                    url: '/',
                    abstract: true,
                    template: '<ui-view/>'
                })
                .state('todo.add', {
                    url: 'todo/add',
                    controller: 'AddTodoController',
                    templateUrl: 'todo/add-todo-view.html'
                })
                .state('todo.edit', {
                    url: 'todo/:id/edit',
                    controller: 'EditTodoController',
                    templateUrl: 'todo/edit-todo-view.html',
                    resolve: {
                        todoEntry: ['$stateParams', 'TodoService', function($stateParams, TodoService) {
                            return TodoService.findById($stateParams.id);
                        }]
                    }
                })
                .state('todo.list', {
                    url: '',
                    controller: 'TodoListController',
                    templateUrl: 'todo/todo-list-view.html',
                    resolve: {
                        todoEntries: ['TodoService', function(TodoService) {
                            return TodoService.findAll();
                        }]
                    }
                })
                .state('todo.view', {
                    url: 'todo/:id',
                    controller: 'ViewTodoController',
                    templateUrl: 'todo/view-todo-view.html',
                    resolve: {
                        todoEntry: ['$stateParams', 'TodoService', function($stateParams, TodoService) {
                            return TodoService.findById($stateParams.id);
                        }]
                    }
                });
        }
    ])
    .controller('AddTodoController', ['$scope', function($scope) {
            console.log('Rendering add todo entry page.');
            $scope.todoEntry = {};
    }])
    .controller('EditTodoController', ['$scope', 'todoEntry', function($scope, todoEntry) {
            console.log('Rendering edit todo entry page for todo entry: ', todoEntry);
            $scope.todoEntry = todoEntry;
    }])
    .controller('TodoListController', ['$scope', 'todoEntries', function ($scope, todoEntries) {
        console.log('Rendering todo entry list page for todo entries: ', todoEntries);
        $scope.todoEntries = todoEntries;
    }])
    .controller('ViewTodoController', ['$scope', '$state', 'NotificationService', 'todoEntry',
        function($scope, $state, NotificationService, todoEntry) {
            console.log('Rendering view todo entry page for todo entry: ', todoEntry);
            $scope.todoEntry = todoEntry;

            $scope.onDeleteSuccess = function() {
                NotificationService.flashMessage('todo.notifications.delete.success', 'success');
                $state.go('todo.list');
            };

            $scope.onDeleteError = function() {
                NotificationService.flashMessage('todo.notifications.delete.error', 'error');
            };
    }]);