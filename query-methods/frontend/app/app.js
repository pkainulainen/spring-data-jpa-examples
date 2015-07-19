'use strict';

console.log('Registering AngularJS modules');

var App = angular.module('app', [
    'ngLocale',
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'pascalprecht.translate',
    'ui.bootstrap',
    'ui.router',
    'ui.utils',
    'angular-growl',
    'angularMoment',

    //Partials
    'templates',

    //Common
    'app.common.config', 'app.common.directives', 'app.common.services',

    //Todo
    'app.todo.controllers', 'app.todo.directives', 'app.todo.services',

    //Search
    'app.search.controllers', 'app.search.directives', 'app.search.services'

]);

console.log('Registered AngularJS modules');

