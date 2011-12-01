#!/usr/bin/env python

# Imports the monkeyrunner modules used by this program
import shlex
import subprocess
import time
import sys
import os

sdkDir = os.environ['ANDROID_SDK'];
if(sdkDir == None):
  print "Environment variable ANDROID_SDK must be set!"
  sys.exit(-1);

adbScript = os.path.join(sdkDir,"platform-tools", "adb");

start = time.time();

adbRetryCount = 0;
adbMaxRetryCount = 3;

while True:
  process = subprocess.Popen([adbScript, 'shell', 'mount'], stdout=subprocess.PIPE, stderr=subprocess.PIPE); 
  result = process.communicate();  
    
  #print output if something went wrong   
  if(process.returncode != 0):
	print result[1].strip();
  
  #if sdcard is mounted, we're done
  if result[0].find("sdcard") >= 0:
	print("-- sdcard mounted --");
	print("--  done waiting  --");
	break;
  
  #now list all devices and verify emulator is still connected
  
  result = subprocess.Popen([adbScript, 'devices'], stdout=subprocess.PIPE, stderr=subprocess.PIPE).communicate();  
  
  emulatorCount = 0;  
  lines = result[0].strip().split('\n');
  lines = lines[1:]
  
  for line in lines:
	if(line.find("emulator-") >= 0):
	  print line;
	  emulatorCount+=1;
	  
  if(emulatorCount == 0):
	print "Lost connection with emulator ("+str(adbRetryCount)+"/"+str(adbMaxRetryCount)+")";
	if(adbRetryCount <= adbMaxRetryCount):
		subprocess.Popen([adbScript, 'kill-server']).communicate();
		subprocess.Popen([adbScript, 'start-server']).communicate();
		adbRetryCount+=1;
		continue; #retry 
	else:
		sys.exit(-1);	

  if(emulatorCount > 1):
	print "There are multiple emulators running!";	
	print str(lines);
	sys.exit(-1);
	
  #print number of running processes
  process = subprocess.Popen([adbScript, 'shell', 'ps'], stdout=subprocess.PIPE, stderr=subprocess.PIPE);
  result = process.communicate();
  procCount = len(result[0].strip().split('\n'));
  
  if(process.returncode != 0):
	print "Number of processes: N/A";
  else:
	print "Number of processes: " + str(procCount);  

  #print elapsed time
  elapsed = time.time() - start;
  minutes = int(elapsed) / 60;
  seconds = int(elapsed) % 60;
  
  #emulator is still connected, but not yet booted completely. Wait...  
  print("-- waiting for device to mount sdcard (elapsed " + str(minutes) + ":" + str(seconds) + ") --");
  time.sleep(5);

# unlock the device (i.e. press menu button)
subprocess.Popen([adbScript, 'shell', 'input', 'keyevent', '82']).communicate();  

