/*
 *   ________________________________________________________________________________________
 *   
 *   Y O O R E E K A
 *   A library for data mining, machine learning, soft computing, and mathematical analysis
 *   ________________________________________________________________________________________ 
 *    
 *   The Yooreeka project started with the code of the book "Algorithms of the Intelligent Web " 
 *   (Manning 2009). Although the term "Web" prevailed in the title, in essence, the algorithms 
 *   are valuable in any software application.
 *  
 *   Copyright (c) 2007-2009 Haralambos Marmanis & Dmitry Babenko
 *   Copyright (c) 2009-${year} Marmanis Group LLC and individual contributors as indicated by the @author tags.  
 * 
 *   Certain library functions depend on other Open Source software libraries, which are covered 
 *   by different license agreements. See the NOTICE file distributed with this work for additional 
 *   information regarding copyright ownership and licensing.
 * 
 *   Marmanis Group LLC licenses this file to You under the Apache License, Version 2.0 (the "License"); 
 *   you may not use this file except in compliance with the License.  
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software distributed under 
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 *   either express or implied. See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 */
package iweb2.beanshellscripts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.yooreeka.config.YooreekaConfigurator;

import bsh.Interpreter;

class ScriptEvalUtils {
    
    public static String BEANSHELL_SCRIPTS_DIR = YooreekaConfigurator.getHome()+"/docs/BeanShell-Notes";
    
    public static List<String> getScripts(String filter) {
        return getScripts(BEANSHELL_SCRIPTS_DIR, filter);
    }
    
    public static void runScripts(String chapter) {
        List<String> scripts = ScriptEvalUtils.getScripts(chapter);
        for(String script : scripts) {
            ScriptEvalUtils.runScript(script);
        }
    }

    public static void runDependentScripts(String[] scripts) {
        Interpreter i = new Interpreter();
        try {
            i.eval("import *;");
        }
        catch(Exception e) {
            throw new RuntimeException("Failed to execute 'import *' : ", e);
        }
        
        for(String name : scripts) {
            String script = getScript(name);
            try {
                i.source(script);
            }
            catch(Exception e) {
                throw new RuntimeException("Failed to run script: " + script, e);
            }
                
        }
    }
    
    public static void runIndependentScripts(String[] scripts) {
        
        for(String name : scripts) {
            String script = getScript(name);            
            runScript(script);
        }
    }
    
    
    public static String getScript(String scriptName) {
        File scriptFile = new File(BEANSHELL_SCRIPTS_DIR, scriptName);
        if( !scriptFile.exists() ) {
            throw new RuntimeException("Script file doesn't exists: '" + 
                    scriptFile.getAbsolutePath() + "'");
        }
        return scriptFile.getAbsolutePath();
    }
    
    
    public static List<String> getScripts(String dir, String filter) {
        List<String> allScripts = new ArrayList<String>();
        File scriptsDir = new File(dir);

        for(File f : scriptsDir.listFiles()) {
            if( f.isFile() && f.getName().contains(filter) ) {
                String script = f.getAbsolutePath();
                allScripts.add(script);
                System.out.println("Added script: " + script);
            }
        }
        return allScripts;
    }

    public static void runScript(String script) {
        Interpreter i = new Interpreter();
        try {
        i.eval("import *;");
        i.source(script);
        }
        catch(Exception e) {
            throw new RuntimeException("Failed to run script: " + script, e);
        }
        
    }
    
}
