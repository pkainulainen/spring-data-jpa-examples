'use strict';

angular.module('app.search.controllers', [])
    .config(['$stateProvider',
        function ($stateProvider) {
            $stateProvider
                .state('todo.search', {
                    authenticate: true,
                    url: 'todo/search/:searchTerm',
                    controller: 'SearchResultController',
                    templateUrl: 'search/search-result-view.html',
                    resolve: {
                        searchResults: ['TodoSearchService', '$stateParams', function(TodoSearchService, $stateParams) {
                            if ($stateParams.searchTerm) {
                                return TodoSearchService.findBySearchTerm($stateParams.searchTerm);
                            }

                            return null;
                        }]
                    }
                });
        }
    ])
    .controller('SearchResultController', ['$log', '$scope', 'searchResults',
        function($log, $scope, searchResults) {
            var logger = $log.getInstance('app.search.controllers.SearchResultController');
            logger.info('Rendering search results page.');
            $scope.todoEntries = searchResults;
        }]);

