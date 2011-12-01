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

# List available targets
args = [androidScript, 'list', 'target' ];
process =  subprocess.Popen(args, stdout=subprocess.PIPE, stderr=subprocess.PIPE);
result = process.communicate();

if(process.returncode != 0):
  print "Failed to list available targets.";
  print result[1];  
  sys.exit(-1);

# Now analyze the input and extract target number for each android target level.
# Example: target 8 -> id 1, target 9 -> id 3, etc.

targetsString = str(result[0]);
targets = dict();
targetRegex = 'id:\s+(\d+)\s+or\s\"android-(\d+)\"'

for line in targetsString.split("\n"):
  #match string:  `id: X or "android=Y"`, where X and Y are positive numbers.
  if(re.match(targetRegex, line)):
	match = re.search(targetRegex, line);
	targets[int(match.group(2))] = int(match.group(1));
	
# print available targets

keys = targets.keys();
keys.sort();

for t in keys:
  print "android-"+str(t) + " -> id: " + str(targets[t]);

# calculate targetLevel id
if(not targets.has_key(targetLevel)):
  print "Target level " + str(targetLevel) + " is not found!";
  sys.exit(-1);

targetId = targets[targetLevel];

if(targetId == None):
  print 'There is no ' + str(targetLevel) + '[API Level] target available.';
  sys.exit(-1);
  
print "Selected target: " + str(targetLevel) + ", selected target id: " + str(targetId);

# Create emulator

emulatorName = "avd-test-t" + str(targetLevel);

print "Deleting old emulator ... ";
args = [androidScript, "delete", "avd", "-n", emulatorName];
process = subprocess.Popen(args, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE);
result = process.communicate();

if(process.returncode != 0):
  print result[1];

print "Creating new emulator ... ";
args = [androidScript, "create", "avd", "-n", emulatorName, "-t", str(targetId), "-f", "-c", "100M"];

process = subprocess.Popen(args, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE);
result = process.communicate("\n");  

if(process.returncode != 0):
  print "Failed to create emulator.";
  print result[1];  
  sys.exit(-1);
  
print "Emulator " + emulatorName + " created successfuly!";
