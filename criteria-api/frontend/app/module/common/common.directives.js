'use strict';

angular.module('app.common.directives', [])
    .directive('staticInclude', ['$http', '$templateCache', '$compile', function ($http, $templateCache, $compile) {
        return function(scope, element, attrs) {
            var templatePath = attrs.staticInclude;

            $http.get(templatePath, {cache: $templateCache}).success(function (response) {
                var contents = $('<div/>').html(response).contents();
                element.html(contents);
                $compile(contents)(scope);
            });
        };
    }]);