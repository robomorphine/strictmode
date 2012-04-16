package com.robomorphine.strictmode.entity.violation;

import com.robomorphine.strictmode.entity.violation.Violation.ViolationFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class ViolationTest extends BaseTestCase {
    
    /**
     * Verify that all real violations can be created using ViolationFactory.
     */
    public void testViolationFactory() throws IOException {
        List<String> fileNames = new LinkedList<String>();
        fileNames.add("dropbox/thread_disk_read.txt");
        fileNames.add("dropbox/thread_disk_write.txt");
        fileNames.add("dropbox/thread_network.txt");
        fileNames.add("dropbox/thread_custom.txt");
        fileNames.add("dropbox/thread_disk_write_remote.txt");
        fileNames.add("dropbox/vm_close.txt");
        fileNames.add("dropbox/vm_end.txt");
        fileNames.add("dropbox/vm_instance_count.txt");
        
        ViolationFactory factory = new ViolationFactory();
        for(String name : fileNames) {
            RawViolation rawViolation = openAssetAsRawViolation(name); 
            assertNotNull(factory.create(rawViolation.headers, rawViolation.exception));
        }
    }
}
