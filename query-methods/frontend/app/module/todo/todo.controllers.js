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
                    authenticate: true,
                    url: 'todo/add',
                    controller: 'AddTodoController',
                    templateUrl: 'todo/add-todo-view.html'
                })
                .state('todo.edit', {
                    authenticate: true,
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
                    authenticate: true,
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
                    authenticate: true,
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
    .controller('AddTodoController', ['$log', '$scope', function($log, $scope) {
        var logger = $log.getInstance('app.todo.controllers.AddTodoController');
        logger.info('Rendering add todo entry page.');
        $scope.todoEntry = {};
    }])
    .controller('EditTodoController', ['$log', '$scope', 'todoEntry', function($log, $scope, todoEntry) {
        var logger = $log.getInstance('app.todo.controllers.EditTodoController');
        logger.info('Rendering edit todo entry page for todo entry: %j', todoEntry);
        $scope.todoEntry = todoEntry;
    }])
    .controller('TodoListController', ['$log', '$scope', 'todoEntries', function($log, $scope, todoEntries) {
        var logger = $log.getInstance('app.todo.controllers.TodoListController');
        logger.info('Rendering todo entry list page for %s todo entries.', todoEntries.length);
        $scope.todoEntries = todoEntries;
    }])
    .controller('ViewTodoController', ['$log', '$scope', 'todoEntry', function($log, $scope, todoEntry) {
        var logger = $log.getInstance('app.todo.controllers.ViewTodoController');
        logger.info('Rendering view todo entry page for todo entry: %j', todoEntry);
        $scope.todoEntry = todoEntry;
    }]);