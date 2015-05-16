var gulp = require("gulp");
var plugins = require('gulp-load-plugins')();
var config = require('./build.config.js');

//Analyzes the Javascript files of our application by using JSHint and reports the found problems.
gulp.task('jshint', function () {
    return gulp.src(config.appFiles.code)
        .pipe(plugins.changed(config.target.js))
        .pipe(plugins.jshint('.jshintrc'))
        .pipe(plugins.jshint.reporter('jshint-stylish'));
});

//Processes the Javascript files of our application.
gulp.task('appCode', function () {
    return gulp.src(config.appFiles.code)
        .pipe(plugins.sourcemaps.init())
        //Combines the Javascript files into a single Javascript file
        .pipe(plugins.concat('app.min.js'))
        //Minifies the created Javascript file
        .pipe(plugins.uglify({
            mangle: false
        }))
        .pipe(plugins.sourcemaps.write())
        //Copies the minified Javascript file to the target directory
        .pipe(gulp.dest(config.target.js))
        //Reports the size of the final Javascript file.
        .pipe(plugins.size({title: 'application'}))
});

//Processes the HTML templates of our application.
gulp.task('appPartials', function () {
    return gulp.src(config.appFiles.partials)
        .pipe(plugins.changed(config.target.js))
        //Minifies the HTML files
        .pipe(plugins.minifyHtml({
            empty: true,
            spare: true,
            quotes: true
        }))
        //Loads the HTML templates into AngularJS $templateCache
        .pipe(plugins.angularTemplatecache('partials.js', {
            standalone: true
        }))
        //Copy the created Javascript file to the target directory
        .pipe(gulp.dest(config.target.js))
        //Reports the size of created Javascript file
        .pipe(plugins.size({showFiles: true}))
});

//Processes the LESS files of our application.
gulp.task('appLess', function () {
    return gulp.src(config.appFiles.style)
        //Creates the final CSS file
        .pipe(plugins.less({
            paths: [config.appFiles.styleBase]
        }))
        .pipe(plugins.concat('app.css'))
        //Minifies the created CSS file
        .pipe(plugins.minifyCss())
        //Copies the CSS File into the target directory
        .pipe(gulp.dest(config.target.css))
        //Reports the size of the final CSS file.
        .pipe(plugins.size({ title: 'css' }))
});

gulp.task('appAssets', function () {
    return gulp.src(config.appFiles.assets, {base: config.appFiles.assetsBase})
        .pipe(gulp.dest(config.target.assets))
});

//Minimizes the shims used by our application and copies them to the target directory.
gulp.task('appShim', function () {
    return gulp.src(config.appFiles.shim)
        .pipe(plugins.uglify({
            mangle: false,
            compress: false,
            preserveComments: 'some'
        }))
        .pipe(gulp.dest(config.target.lib));
});

//Processes the Javascript files of the libraries and frameworks that are used in our application
gulp.task('vendorCode', function () {
    return gulp.src(config.vendorFiles.code)
        //Combine the Javascript files into a single Javascript file
        .pipe(plugins.concat('vendor.min.js'))
        //Skips minification of files that are already minified.
        .pipe(plugins.if('*.min.js', plugins.uglify({
            mangle: false,
            compress: false,
            preserveComments: 'some'
        })))
        //Minifies Javascript files that are not minified.
        .pipe(plugins.if('vendor/**/*.js', plugins.uglify({
            mangle: false,
            compress: true
        })))
        //Copies the created file to the target directory.
        .pipe(gulp.dest(config.target.js))
        //Reports the size of the final Javascript file
        .pipe(plugins.size({title: 'vendor'}))
});

//Analyzes our Javascript files by using JSHint and invokes the build when the watched files are changed
gulp.task('watch', ['jshint', 'build'], function () {
    gulp.watch(config.appFiles.partials, ['appPartials']);
    gulp.watch(config.appFiles.code, ['appCode', 'jshint']);
    gulp.watch(config.appFiles.style, ['appLess']);
    gulp.watch(config.appFiles.assets, ['appAssets']);
    gulp.watch(config.vendorFiles.code, ['vendorCode']);
});

//Configures the tasks of our build
gulp.task('build', [
    'appLess',
    'appShim',
    'appAssets',
    'appPartials',
    'appCode',
    'vendorCode'
]);

//Runs the watch task if no task is specified when gulp is run
gulp.task('default', ['watch']);