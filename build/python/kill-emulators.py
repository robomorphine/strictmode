#!/usr/bin/env python
import os
import time
import signal

processname = 'emulator-arm';

sleepBeforeKill = 2;
sig = signal.SIGTERM;

for i in [0,1]:
    
    for line in os.popen("ps xa"):
        fields = line.split();
        pid = fields[0];
        process = fields[4];
    
        if process.find(processname) > 0:
            print "Killing process " + str(pid) + ", " + str(process);
            os.kill(int(pid), sig);
    
    print "Kill round "+str(i)+" completed...";
    
    time.sleep(sleepBeforeKill);
    sig = signal.SIGKILL;
    sleepBeforeKill = 0;
     


