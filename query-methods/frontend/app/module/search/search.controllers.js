'use strict';

angular.module('app.search.controllers', [])
    .constant('paginationConfig', {
        firstPageNumber: 1,
        pageSize: 5
    })
    .config(['$stateProvider',
        function ($stateProvider) {
            $stateProvider
                .state('todo.search', {
                    authenticate: true,
                    url: 'todo/search/:searchTerm/page/:pageNumber/size/:pageSize',
                    controller: 'SearchResultController',
                    templateUrl: 'search/search-result-view.html',
                    resolve: {
                        searchResults: ['TodoSearchService', '$stateParams', function(TodoSearchService, $stateParams) {
                            if ($stateParams.searchTerm) {
                                return TodoSearchService.findBySearchTerm($stateParams.searchTerm,
                                    $stateParams.pageNumber - 1,
                                    $stateParams.pageSize
                                );
                            }

                            return null;
                        }],
                        searchTerm: ['$stateParams', function($stateParams) {
                            return $stateParams.searchTerm;
                        }]
                    }
                });
        }
    ])
    .controller('SearchResultController', ['$log', '$scope', '$state', 'paginationConfig', 'searchResults', 'searchTerm',
        function($log, $scope, $state, paginationConfig, searchResults, searchTerm) {
            var logger = $log.getInstance('app.search.controllers.SearchResultController');
            logger.info('Rendering search results page for search term: %s with search results: %j', searchTerm, searchResults);
            $scope.searchResults = searchResults;
            $scope.searchTerm = searchTerm;
        }]);

