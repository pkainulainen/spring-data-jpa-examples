'use strict';

angular.module('app.search.directives', [])
    .directive('searchForm', ['$state', function($state) {
        return {
            link: function (scope, element, attr) {
                var userWritingSearchTerm = false;
                var minimumSearchTermLength = 3;

                scope.missingChars = minimumSearchTermLength;
                scope.searchTerm = "";

                scope.searchFieldBlur = function() {
                    userWritingSearchTerm = false;
                };

                scope.searchFieldFocus = function() {
                    userWritingSearchTerm = true;
                };

                scope.showMissingCharacterText = function() {
                    if (userWritingSearchTerm) {
                        if (scope.searchTerm.length < minimumSearchTermLength) {
                            return true;
                        }
                    }

                    return false;
                };

                scope.search = function() {
                    if (scope.searchTerm.length < minimumSearchTermLength) {
                        scope.missingChars = minimumSearchTermLength - scope.searchTerm.length;
                    }
                    else {
                        scope.missingChars = 0;
                        $state.go('todo.search',
                            {searchTerm: scope.searchTerm},
                            {reload: true, inherit: true, notify: true}
                        );
                    }
                };

            },
            templateUrl: 'search/search-form-directive.html',
            scope: {
            }
        };
    }]);