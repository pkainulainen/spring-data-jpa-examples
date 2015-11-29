/*
 * Copyright 2014 Allan Ditzel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * spring-security-csrf-token-interceptor
 *
 * Sets up an interceptor for all HTTP requests that adds the CSRF Token Header that Spring Security requires.
 */
(function() {
    'use strict';
    angular.module('spring-security-csrf-token-interceptor', [])
        .factory('csrfInterceptor', ['$injector', '$q',
            function($injector) {
                var $q = $injector.get('$q'),
                    csrf = $injector.get('csrf'),
                    // initialize the csrf provider service which in-turn invokes the injected csrfService 
                    // to fire the synchronous XHR call to get the CSRF token
                    csrfService = csrf.init();

                return {
                    request: function(config) {
                        // intercept HTTP request only for the configured HTTP types
                        if (csrfService.settings.httpTypes.indexOf(config.method.toUpperCase()) > -1) {
                            config.headers[csrfService.settings.csrfTokenHeader] = csrfService.token;
                        }
                        return config || $q.when(config);
                    },
                    responseError: function(response) {
                        var $http,
                            newToken = response.headers(csrfService.settings.csrfTokenHeader);
                            
                        if (response.status === 403 && csrfService.numRetries < csrfService.settings.maxRetries) {
                            csrfService.getTokenData();
                            $http = $injector.get('$http');
                            csrfService.numRetries = csrfService.numRetries + 1;
                            return $http(response.config);
                        } else if (newToken) {
                            // update the csrf token in-case of response errors other than 403
                            csrfService.token = newToken;
                        }
                        // Fix for interceptor causing failing requests
                        return $q.reject(response);
                    },
                    response: function(response) {
                        // reset number of retries on a successful response
                        csrfService.numRetries = 0;
                        return response;
                    }
                };
            }
        ]).factory('csrfService', [

            function() {
                var defaults = {
                    url: '/', // the URL to which the CSRF call has to be made to get the token
                    csrfHttpType: 'head', // the HTTP method type which is used for making the CSRF token call
                    maxRetries: 5, // number of retires allowed for forbidden requests
                    csrfTokenHeader: 'X-CSRF-TOKEN',
                    httpTypes: ['GET', 'HEAD', 'PUT', 'POST', 'DELETE'] // default allowed HTTP types
                };
                return {
                    inited: false,
                    settings: null,
                    numRetries: 0,
                    token: '',
                    init: function(options) {
                        this.settings = angular.extend({}, defaults, options);
                        this.getTokenData();
                        console.log(this.settings, this.defaults, options);
                    },
                    getTokenData: function() {
                        var xhr = new XMLHttpRequest();
                        xhr.open(this.settings.csrfHttpType, this.settings.url, false);
                        xhr.send();

                        this.token = xhr.getResponseHeader(this.settings.csrfTokenHeader);
                        this.inited = true;
                    }
                };

            }
        ]).provider('csrf', [

            function() {
                var CsrfModel = function CsrfModel(options) {
                    return {
                        options: options,
                        csrfService: null
                    };
                };

                return {
                    $get: ['csrfService',
                        function(csrfService) {
                            var self = this;
                            return {
                                init: function() {
                                    self.model = new CsrfModel(self.options);
                                    self.model.csrfService = csrfService;
                                    self.model.csrfService.init(self.model.options);
                                    return self.model.csrfService;
                                }
                            };
                        }
                    ],

                    model: null,

                    options: {},

                    config: function(options) {
                        this.options = options;
                    }
                };
            }
        ]).config(['$httpProvider',
            function($httpProvider) {
                $httpProvider.interceptors.push('csrfInterceptor');
            }
        ]);
}());