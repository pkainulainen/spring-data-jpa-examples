'use strict';

angular.module('app.todo.directives', [])
    .directive('todoEntryList', [function() {
        return {
            templateUrl: 'todo/todo-list-directive.html',
            scope: {
                todoEntries: '='
            }
        };
    }]);