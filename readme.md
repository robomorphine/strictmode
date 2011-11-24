Doc
=============

# Build System

##Prerequisites

1. Latest Android SDK (http://developer.android.com/sdk/index.html)
2. Ant 1.8.2 (or later)
3. Ivy 2.2 (or later)

## Android Virtual Devices

Build system can automatically start emulators in order to run 
tests on them. Currently build system does not support automatic 
creation of emulators. Before running automated tests a number of 
different emulators should be created with specific names and settings.

1.  All emulators must have SD card
2.  All emulators should be named “avd-test-t#”, where # is API Level (7 or 8, or 9, etc.)

The first requirement is a result of some Android restrictions. 
In order to pull test reports, build system asks test runner 
to copy those reports to SD Card. And since SD card is always 
accessible via ADB, build system is able to download those 
reports to host machine.
 
In order to automatically start emulators, build system needs
to know its name. That’s why a special “contract” on emulator 
names is introduced.

Please create next emulators:

1. `avd-test-t7`  : target – API **Level 7**, Android 2.1;      SD Card **>= 100 mb**;
2. `avd-test-t8`  : target – API **Level 8**, Android 2.2;      SD Card **>= 100 mb**;
3. `avd-test-t9`  : target – API **Level 9**, Android 2.3;      SD Card **>= 100 mb**;
4. `avd-test-t10` : target – API **Level 10**, Android 2.3.3;   SD Card **>= 100 mb**;
5. `avd-test-t13` : target – API **Level 13**, Android 3.2;     SD Card **>= 100 mb**;

##Setting up build system

Before using build system, one more file should be created in order
for build system to know the location of Android SDK. This file is 
called `local.properties` and should be placed in root directory 
(at the same level top `build.xml` file is located).
  
For this please copy `local.properties.template` as `local.properties`
and set correct path to `sdk.dir` property.

For example:

    sdk.dir=/opt/android/sdk
  
Once this is done, build system is ready to be used.

##Using build scripts

There are two files in the root folder called `build.xml` and `easy-build.xml`. 
The `easy-build.xml` file is simply an adapter that ultimately calls `build.xml`
to make all the work. The reason for introducing this file is because Easy has
some rules for naming build targets and expects specific behavior for each 
of these targets. But original build system had some other behavior for 
targets with identical names. And in order to avoid confusion `easy-build.xml`
adapter was introduced.
 
The `easy-build.xml` has next targets:

 * `clean`   - Removes all compiled code and artifacts created during the build process
 * `compile` - Compiles all source for project
 * `test`    - Runs all tests for project against release version of code
 * `testcc`  - Runs all tests with code coverage enabled
 * `package` - Creates all deployable artifacts for the project
 
Now about the main `build.xml` build file. To briefly describe all possible options:

    ant -f build.xml [debug|instrument|release] [build] [test | test-on-emulator | test-on-all-emulators] 
                     [check-test-report] [check-code-coverage]
   
The first target should be either `debug`, `instrument` or `release`. If none is specified, 
then `debug` is used as default. These targets only set some properties that 
are appropriate for each of these build modes. 

Next target is called `build` and will re-build all projects. 

There are three `test*` targets:

 * `test` runs tests, assumes projects are built and emulator is running or device is connected.
 * `test-on-emulator` starts emulator and runs tests on it, 
 * `test-on-all-emulators` starts emulators one by one and runs tests on each of them, 
   and then consolidates test reports into one file.

By default `test-on-emulator` starts API Level 8 emulator (i.e. emulator with name `avd-test-t8`).
This can be overridden by `test.target` property. For example, here is how you 
can run tests on Android 3.1 emulator:

    ant –f build.xml test-on-emulator –Dtest.target=11

Similar, `test-on-all-emulators` uses default list of emulators, which currently
is `7,8,9,10,11,12,13`. This can be overridden using `test.targets` property. 
For example, here is how you can run tests only on 2.x emulators:

    ant –f build.xml test-on-all-emulators –Dtest.targets=7,8,9,10

The `check-test-report` target checks that there is no errors or failures in test report from
last test run. If there is any, it fails a build.

Also, if `check-code-coverage` is specified, build system verifies that code coverage is not 
below threshold. Currently default values are 90% for block and line coverage. This can be 
overrided using -Dcc.blocks.threshold=X and -Dcc.lines.threshold=X

And the last supported target is `clean`. It cleans all build artifacts
produced by `build` or `test*` targets.

## Examples 

Build in debug mode:

    ant -f build.xml debug build

Build in release mode:

    ant -f build.xml release build
    
Build and runt test on connected device (or running emulator):

    ant -f build.xml debug build test
    
Run tests against pre-built bineries in debug mode:

    ant -f build.xml debug test
    
Run tests against pre-built bineries in debug mode on all emulators:

    ant -f build.xml debug test-on-all-emulators
    
Build in release mode and run tests on all emulators:

    ant -f build.xml release build test-on-all-emulators

Start and emulator manually from the command line:
	
	emulator @avd-test-t8	
	
Install the application on a device

	adb install -r bin/release/com.example.apk
	
Uninstall the application from a device

	adb uninstall com.example.auth
	
Print meta-data for 

	aapt d badging bin/debug/com.example.apk
