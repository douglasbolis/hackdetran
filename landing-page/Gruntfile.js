module.exports = function (grunt) {
  "use strict";
  var mozjpeg = require ('imagemin-mozjpeg');
  // Project configuration.
  grunt.initConfig({
    clean: {
  		main: ["dist"]
	  },
    imagemin: {                          // Task
      /*static: {                          // Target
        options: {                       // Target options
          optimizationLevel: 3,
          svgoPlugins: [{ removeViewBox: false }],
          use: [mozjpeg()]
        },
        files: {                         // Dictionary of files
          'dist/img.png': 'src/img.png', // 'destination': 'source'
          'dist/img.jpg': 'src/img.jpg',
          'dist/img.gif': 'src/img.gif'
        }
      },*/
      dynamic: {                         // Another target
        options: {                       // Target options
          optimizationLevel: 3,
          svgoPlugins: [{ removeViewBox: false }],
          use: [mozjpeg()]
        },
        files: [{
          expand: true,                  // Enable dynamic expansion
          cwd: 'assets/images',                   // Src matches are relative to this path
          src: ['**/*.{png,jpg,gif}'],   // Actual patterns to match
          dest: 'dist/assets/images'                  // Destination path prefix
        }]
      }
    },
    compress: {
		  main: {
		    options: {
		      mode: 'gzip'
		    },
		    expand: true,
		    cwd: '.',
		    src: ['**/*','!**/node_modules/**','!**/dist/**','!LICENSE','!README.md','!Gruntfile.js','!index.html','dist/index.html'],
		    dest: 'dist/'//,

		  }
    },
    processhtml: {
      dist: {
        files: {
          'dist/index.html': ['index.html']
        }
      }
    },
    htmlmin: {
			dev: {
				options: {
					removeComments: true,
					collapseWhitespace: true
				},
        files: {
        'dist/index.html': 'dist/index.html'
        }
			}
  	},
    cssmin: {
            bundle: {
                options: {
                    banner: '/* xDevel Sistemas */'
                },
                files: {
                    'dist/assets/css/bundle.css': ['assets/css/*.css'],
                }
            }
        },
    uglify: {
            options: {
                compress: true
            },
            bundle: {
                src: [
                  'lib/jquery/dist/jquery.js',
                	'lib/jquery.scrollTo/jquery.scrollTo.js',
                	'lib/jquery.localScroll/jquery.localScroll.js',
                	'lib/jquery-form/jquery.form.js',
                	'lib/jquery-validation/dist/jquery.validate.js',
                	'assets/js/waypoints.min.js',
                	'assets/js/bootstrap.min.js',
                	'assets/js/mc-validate.js',
                	'assets/js/jquery.prettyPhoto.js',
                	'assets/js/scripts.js'],
                dest: 'dist/assets/js/bundle.js'
            }
    },
    copy: {
  	  main: {
  	    files: [
  	      // includes files within path and its sub-directories
  	      {expand: true, src: ['**/*','!**/*.{png,jpg,gif,css,js}','!**/dist/**','!**/node_modules/**'], dest: 'dist/'}
  	    ]
  	  }
	  }

  });

  grunt.loadNpmTasks('grunt-contrib-imagemin');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-compress');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-processhtml');
  grunt.loadNpmTasks('grunt-contrib-htmlmin');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-cssmin');



  // Default task(s).
  grunt.registerTask('default', ['clean','copy','processhtml','htmlmin','cssmin','uglify','imagemin']);

};
