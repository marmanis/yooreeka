/**
 * __________________________________________
 * 
 *   Yooreeka
 * __________________________________________
 * 
 * Created on: Apr 27, 2016
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *  
 */
package org.yooreeka.util.internet.crawling.core;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.yooreeka.util.P;

/**
 * @author babis
 *
 */
public class MyFileVisitor extends SimpleFileVisitor<Path> {

	List<String> filePaths = new ArrayList<String>();
	
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {

    	if (attr.isSymbolicLink()) {
            
    		P.println("Symbolic link found: %s ", file);
            
        } else if (attr.isRegularFile()) {

        	P.println("Regular file: %s ", file);
        	add("file:///"+file.toString());
        	
        } else {
        	
            P.println("Other: %s ", file);
        }
        //System.out.println("(" + attr.size() + "bytes)");
        return CONTINUE;
    }

    // Print each directory visited.
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        P.println("Directory processed: %s%n", dir);
        return CONTINUE;
    }

    // If there is some error accessing the file, let the user know.
    // If you don't override this method and an error occurs, an IOException is thrown.
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        exc.printStackTrace();
        return CONTINUE;
    }
    
    private void add(String s) {
    	filePaths.add(s);
    }
    
    public List<String> getFilePaths() {
    	return filePaths;
    }
}
