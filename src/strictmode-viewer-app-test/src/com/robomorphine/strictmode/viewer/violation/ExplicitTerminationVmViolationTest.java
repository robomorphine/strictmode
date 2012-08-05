package com.robomorphine.strictmode.viewer.violation;

import com.robomorphine.strictmode.viewer.violation.ExplicitTerminationVmViolation.ExplicitTerminationVmViolationFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ExplicitTerminationVmViolationTest extends BaseTestCase {
    
    public void testViolationFactory() throws IOException {
        List<String> goodNames = new LinkedList<String>();
        goodNames.add("dropbox/vm_close.txt");
        goodNames.add("dropbox/vm_end.txt");
        
        
        List<String> badNames = new LinkedList<String>();
        badNames.add("dropbox/vm_instance_count.txt");
        badNames.add("dropbox/thread_disk_read.txt");
        badNames.add("dropbox/thread_disk_write.txt");
        badNames.add("dropbox/thread_network.txt");
        badNames.add("dropbox/thread_custom.txt");
        badNames.add("dropbox/thread_disk_write_remote.txt");
        
        ExplicitTerminationVmViolationFactory factory = new ExplicitTerminationVmViolationFactory();
        for(String name : goodNames) {
            RawViolation rawViolation = openAssetAsRawViolation(name); 
            assertNotNull(factory.create(rawViolation.headers, rawViolation.exception));
        }
        
        for(String name : badNames) {
            RawViolation rawViolation = openAssetAsRawViolation(name); 
            assertNull(factory.create(rawViolation.headers, rawViolation.exception));
        }
    }
}
