'use strict';

angular.module('app.common.services', [])
    .service('NotificationService', ['$rootScope', 'growl', function ($rootScope, growl) {
        var flashMessageQueue = [];

        function displayNotification(message, type) {
            if (type === 'success') {
                growl.success(message);
            } else if (type === 'warn') {
                growl.warning(message);
            } else if (type === 'info') {
                growl.info(message);
            } else {
                growl.error(message);
            }
        }

        // Display all flash notifications after state has changed
        $rootScope.$on("$stateChangeSuccess", function () {
            while (flashMessageQueue.length > 0) {
                var item = flashMessageQueue.shift();
                if (item) {
                    displayNotification(item.message, item.type);
                }
            }
        });

        // Public API
        return {
            'flashMessage': function (message, type) {
                flashMessageQueue.push({message: message, type: type || 'info'});
            }
        };
    }]);
