'use strict';

angular.module('app.todo.directives', [])
    .controller('DeleteTodoController', ['$scope', '$modalInstance', '$state', 'TodoService', 'todoEntry', 'successCallback', 'errorCallback',
        function($scope, $modalInstance, $state, TodoService, todoEntry, successCallback, errorCallback) {
            console.log('Showing delete confirmation dialog for todo entry: ', todoEntry);
            $scope.todoEntry = todoEntry;

            $scope.cancel = function() {
                console.log('User clicked cancel button. Todo entry is not deleted.');
                $modalInstance.dismiss('cancel');
            };

            $scope.delete = function() {
                console.log('User clicked delete button. Todo entry is deleted.');
                $modalInstance.close();
                TodoService.delete(todoEntry, successCallback, errorCallback);
            };
        }])
    .directive('deleteTodoEntryLink', ['$modal', function($modal) {
        return {
            link: function (scope, element, attr) {
                scope.showDeleteConfirmationDialog = function() {
                    $modal.open({
                        templateUrl: 'todo/delete-todo-modal.html',
                        controller: 'DeleteTodoController',
                        resolve: {
                            errorCallback: function() {
                                return scope.onError;
                            },
                            successCallback: function() {
                                return scope.onSuccess;
                            },
                            todoEntry: function () {
                                return scope.todoEntry;
                            }
                        }
                    });
                };
            },
            template: '<a ng-click="showDeleteConfirmationDialog()" translate="pages.delete.link"></a>',
            scope: {
                onError: '&',
                onSuccess: '&',
                todoEntry: '='
            }
        };
    }])
    .directive('todoEntryList', [function() {
        return {
            templateUrl: 'todo/todo-list-directive.html',
            scope: {
                todoEntries: '='
            }
        };
    }]);