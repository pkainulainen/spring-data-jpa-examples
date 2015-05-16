'use strict';

angular.module('app.todo.services', ['ngResource'])
    .factory('TodoService', ['$resource', function($resource) {
        var api = $resource('/api/todo/:id', {"id": "@id"}, {
            get: {method: 'GET'},
            save: {method: 'POST'},
            update: {method: 'PUT'},
            query:  {method: 'GET', params: {}, isArray: true}
        });

        return {
            add: function(todo, successCallback, errorCallback) {
                console.log('Adding new todo entry: ', todo);
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
            delete: function(todo, successCallback, errorCallback) {
                console.log('Deleting todo entry: ', todo);
                return api.delete(todo,
                    function(deleted) {
                        console.log('Deleted todo entry: ', deleted);
                        successCallback(deleted);
                    },
                    function(error) {
                        console.log('Deleting the todo entry failed because of an error: ', error);
                        errorCallback(error);
                    }
                );
            },
            findAll: function() {
                console.log('Finding all todo entries.');
                return api.query();
            },
            findById: function(id) {
                console.log('Finding todo entry by id: ', id);
                return api.get({id: id}).$promise;
            },
            update: function(todo, successCallback, errorCallback) {
                console.log('Updating todo entry: ', todo);
                return api.update(todo,
                    function(updated) {
                        console.log('Updated the information of the todo entry: ', updated);
                        successCallback(updated);
                    },
                    function(error) {
                        console.log('Updating the information of the todo entry failed because of an error: ', error);
                        errorCallback(error);
                    });
            }
        };
    }]);