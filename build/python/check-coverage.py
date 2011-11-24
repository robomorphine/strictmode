#!/usr/bin/env python

import linecache
import re
import sys
import os

if( len(sys.argv) < 4 ): 
  print 'usage: <script> <coverage.txt> <blockThreshold> <linesThreshold>';
  sys.exit(-1);

coverageTxtFile = sys.argv[1];
blocksThreshold = int(sys.argv[2]);
linesThreshold = int(sys.argv[3]);

open(coverageTxtFile);
line = linecache.getline(coverageTxtFile, 6);

parts = line.split("\t");
rawblocks = parts[2];
rawlines  = parts[3];

#print rawblocks;
#print rawlines;

regexp = "(\d+)\%.*";
blocks = int(re.search(regexp, rawblocks).group(1));
lines  = int(re.search(regexp, rawlines).group(1));

#print blocks;
#print lines;


blocksMsg = 'CC blocks '+str(blocks)+", threshold "+str(blocksThreshold);
linesMsg  = 'CC lines  '+str(lines)+", threshold "+str(linesThreshold);

print blocksMsg;
print linesMsg;

if blocks < blocksThreshold or lines < linesThreshold:
  print "Code Coverage is below threshold!";
  sys.exit(-1);
  
print "Code Coverage is OK";