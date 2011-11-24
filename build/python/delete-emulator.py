#!/usr/bin/env python

import shlex
import subprocess
import time
import re
import sys
import os


if(len(sys.argv) != 2):
  print "usage: <script> <targetLevel>";
  sys.exit(-1);

sdkDir = os.environ['ANDROID_SDK'];
if(sdkDir == None):
  print "Environment variable ANDROID_SDK must be set!"
  sys.exit(-1);

androidScript = os.path.join(sdkDir,"tools", "android");

targetLevel = int(sys.argv[1]);
emulatorName = "avd-test-t" + str(targetLevel);

print "Deleting old emulator ... ";

args = [androidScript, "delete", "avd", "-n", emulatorName];
process = subprocess.Popen(args, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE);
result = process.communicate();

print "Emulator " + emulatorName + " deleted successfuly!";
