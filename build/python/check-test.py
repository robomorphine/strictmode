import sys

if( len(sys.argv) < 2 ): 
  print 'usage: <script> <report.xml>'
  sys.exit(-1);

reportFilename = sys.argv[1];

infile = open(reportFilename);
for line in infile:
  if line.find("<failure") >= 0 or line.find("<error") >= 0: 
	print 'Test report '+ reportFilename +' contains failures or errors!';
	sys.exit(-1);
