'use strict';

angular.module('app.todo.services', ['ngResource'])
    .factory('TodoService', ['$log', '$resource', function($log, $resource) {
        var api = $resource('/api/todo/:id', {"id": "@id"}, {
            get: {method: 'GET'},
            save: {method: 'POST'},
            update: {method: 'PUT'},
            query:  {method: 'GET', params: {}, isArray: true}
        });

        var logger = $log.getInstance('app.todo.services.TodoService');

        return {
            add: function(todo, successCallback, errorCallback) {
                logger.info('Adding new todo entry: %j', todo);
                return api.save(todo,
                    function(added) {
                        logger.info('Added a new todo entry: %j', added);
                        successCallback(added);
                    },
                    function(error) {
                        logger.error('Adding a todo entry failed because of an error: %j', error);
                        errorCallback(error);
                    });
            },
            delete: function(todo, successCallback, errorCallback) {
                logger.info('Deleting todo entry: %j', todo);
                return api.delete(todo,
                    function(deleted) {
                        logger.info('Deleted todo entry: %j', deleted);
                        successCallback(deleted);
                    },
                    function(error) {
                        logger.error('Deleting the todo entry failed because of an error: %j', error);
                        errorCallback(error);
                    }
                );
            },
            findAll: function() {
                logger.info('Finding all todo entries.');
                return api.query();
            },
            findById: function(id) {
                logger.info('Finding todo entry by id: %s', id);
                return api.get({id: id}).$promise;
            },
            update: function(todo, successCallback, errorCallback) {
                logger.info('Updating todo entry: %j', todo);
                return api.update(todo,
                    function(updated) {
                        logger.info('Updated the information of the todo entry: %j', updated);
                        successCallback(updated);
                    },
                    function(error) {
                        logger.error('Updating the information of the todo entry failed because of an error: %j', error);
                        errorCallback(error);
                    });
            }
        };
    }]);