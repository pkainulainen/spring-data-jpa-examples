'use strict';

angular.module('app.search.services', ['ngResource'])
    .factory('TodoSearchService', ['$resource', function($resource) {
        var api = $resource('/api/todo/search', {}, {
            'query':  {method:'GET', isArray:true}
        });

        return {
            findBySearchTerm: function(searchTerm) {
                return api.query({searchTerm: searchTerm});
            }
        };
    }]);