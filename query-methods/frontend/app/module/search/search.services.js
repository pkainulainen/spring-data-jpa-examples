'use strict';

angular.module('app.search.services', ['ngResource'])
    .factory('TodoSearchService', ['$log', '$resource', function($log, $resource) {
        var api = $resource('/api/todo/search', {}, {
            'query':  {method:'GET', isArray:false}
        });

        var logger = $log.getInstance('app.search.services.TodoSearchService');

        return {
            findBySearchTerm: function(searchTerm, pageNumber, pageSize) {
                logger.info('Searching todo entries with search term: %s, pageNumber: %s, and page size: %s', searchTerm, pageNumber, pageSize);
                return api.query({
                    page: pageNumber,
                    searchTerm: searchTerm,
                    size: pageSize,
                    sort: "title"
                }).$promise;
            }
        };
    }]);