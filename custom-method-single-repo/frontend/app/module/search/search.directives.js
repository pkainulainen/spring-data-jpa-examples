'use strict';

angular.module('app.search.directives', [])
    .directive('searchForm', ['$log', '$state', function($log, $state) {

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
                            {searchTerm: scope.search.searchTerm},
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
    }]);