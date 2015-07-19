'use strict';

angular.module('app.search.controllers', [])
    .config(['$stateProvider',
        function ($stateProvider) {
            $stateProvider
                .state('todo.search', {
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
    .controller('SearchResultController', ['$scope', 'searchResults',
        function($scope, searchResults) {
            console.log('Rendering search results page.');
            $scope.todoEntries = searchResults;
        }]);

