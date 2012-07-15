How to pre-build rXX .jar files:
1) repo init -u https://android.googlesource.com/platform/manifest -b android-4.0.1_r1
(where android-4.0.1_r1 should be substituted with version of specified API - XX in our case).
NOTE: see http://source.android.com/source/build-numbers.html
2) repo sync
3) make clean
4) make core framework
5) copy:
- core.jar from out/target/common/obj/JAVA_LIBRARIES/core_intermediates/classes-full-debug.jar
- framework from out/target/common/obj/JAVA_LIBRARIES/framework_intermediates/classes-full-debug.jar
