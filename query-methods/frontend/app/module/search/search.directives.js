'use strict';

angular.module('app.search.directives', [])
    .directive('searchForm', ['$log', '$state', 'paginationConfig', function($log, $state, paginationConfig) {

        var logger = $log.getInstance('app.search.directives.searchForm');

        return {
            link: function (scope, element, attr) {
                var userWritingSearchTerm = false;
                var minimumSearchTermLength = 3;

                scope.translationData = {
                    missingCharCount: minimumSearchTermLength
                };

                scope.search = {};
                scope.search.searchTerm = "";

                scope.searchFieldBlur = function() {
                    userWritingSearchTerm = false;
                    scope.search.searchTerm = "";
                    scope.translationData.missingCharCount = minimumSearchTermLength;
                };

                scope.searchFieldFocus = function() {
                    userWritingSearchTerm = true;
                };

                scope.showMissingCharacterText = function() {
                    if (!scope.search.searchTerm) {
                        scope.search.searchTerm = "";
                    }

                    if (userWritingSearchTerm) {
                        if (scope.search.searchTerm.length < minimumSearchTermLength) {
                            return true;
                        }
                    }

                    return false;
                };

                scope.search = function() {
                    logger.trace('User is using the search term: %s', scope.search.searchTerm);

                    if (scope.search.searchTerm.length < minimumSearchTermLength) {
                        scope.translationData.missingCharCount = minimumSearchTermLength - scope.search.searchTerm.length;
                        logger.trace('%s characters are missing. Search is not invoked.', scope.translationData.missingCharCount);
                    }
                    else {
                        scope.translationData.missingCharCount = 0;
                        $state.go('todo.search',
                            {
                                searchTerm: scope.search.searchTerm,
                                pageNumber: paginationConfig.firstPageNumber,
                                pageSize: paginationConfig.pageSize
                            },
                            {reload: true, inherit: true, notify: true}
                        );
                    }
                };

            },
            templateUrl: 'search/search-form-directive.html',
            scope: {
                currentUser: '='
            }
        };
    }])
    .directive('searchResultList', ['$log', '$state', 'paginationConfig', function($log, $state, paginationConfig) {
        var logger = $log.getInstance('app.search.directives.searchResultList');

        return {
            link: function(scope, element, attr) {
                logger.debug("Rendering search result list for search term: %s and search results: %j", scope.searchTerm, scope.searchResults);
                scope.todoEntries = scope.searchResults.content;

                scope.pagination = {
                    currentPage: scope.searchResults.number + 1,
                    itemsPerPage: paginationConfig.pageSize,
                    totalItems: scope.searchResults.totalElements
                };

                scope.pageChanged = function(newPageNumber) {
                    logger.debug('Requesting a new page: %s for search term: %s with page size: %s',
                        newPageNumber,
                        scope.searchTerm,
                        paginationConfig.pageSize
                    );

                    $state.go('todo.search',
                        {searchTerm: scope.searchTerm, pageNumber: newPageNumber, pageSize: paginationConfig.pageSize},
                        {reload: true, inherit: true, notify: true}
                    );
                };
            },
            templateUrl: 'search/search-result-list-directive.html',
            scope: {
                searchResults: '=',
                searchTerm: '@'
            }
        };
    }]);