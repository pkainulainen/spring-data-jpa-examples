'use strict';

angular.module('app.search.services', ['ngResource'])
    .factory('TodoSearchService', ['$log', '$resource', function($log, $resource) {
        var api = $resource('/api/todo/search', {}, {
            'query':  {method:'GET', isArray:true}
        });

        var logger = $log.getInstance('app.search.services.TodoSearchService');

        return {
            findBySearchTerm: function(searchTerm) {
                logger.info('Searching todo entries with search term: %s', searchTerm);
                return api.query({searchTerm: searchTerm});
            }
        };
    }]);