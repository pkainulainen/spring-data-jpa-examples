'use strict';

angular.module('app.todo.services', ['ngResource'])
    .factory('Todos', ['$resource', function($resource) {
        var api = $resource('/api/todo/:id', {"id": "@id"}, {
            query:  {method: 'GET', params: {}, isArray: true}
        });

        return {
            findAll: function() {
                return api.query();
            }
        };
    }]);