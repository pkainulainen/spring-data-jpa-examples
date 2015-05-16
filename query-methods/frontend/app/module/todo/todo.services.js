'use strict';

angular.module('app.todo.services', ['ngResource'])
    .factory('TodoService', ['$resource', function($resource) {
        var api = $resource('/api/todo/:id', {"id": "@id"}, {
            get: {method: 'GET'},
            save: {method: 'POST'},
            query:  {method: 'GET', params: {}, isArray: true}
        });

        return {
            add: function(todo, successCallback, errorCallback) {
                return api.save(todo,
                    function(added) {
                        console.log('Added a new todo entry: ', added);
                        successCallback(added);
                    },
                    function(error) {
                        console.log('Adding todo entry failed because of an error: ', error);
                        errorCallback(error);
                    });
            },
            findAll: function() {
                return api.query();
            },
            findById: function(id) {
                console.log('Finding todo entry by id: ', id);
                return api.get({id: id}).$promise;
            }
        };
    }]);