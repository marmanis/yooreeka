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
 *   Copyright (c) 2007-2009    Haralambos Marmanis & Dmitry Babenko
 *   Copyright (c) 2009-2014 Marmanis Group LLC and individual contributors as indicated by the @author tags.  
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
package org.yooreeka.util.system;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.yooreeka.util.P;

/**
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class Sysbench {

	public Options options = new Options();

	/**
	 * @return the options
	 */
	public Options getOptions() {
		return options;
	}

	public Sysbench() {
		initBooleanOptions();
		initArgOptions();
		initPropertyOptions();
	}

	public void help() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "sysbench", options,true);
	}
	
	private void initBooleanOptions() {
		Option help = new Option("help", "print this message");
		options.addOption(help);

		Option version = new Option("version",
				"print the version information and exit");
		options.addOption(version);

		Option quiet = new Option("quiet", "be extra quiet");
		options.addOption(quiet);

		Option verbose = new Option("verbose", "be extra verbose");
		options.addOption(verbose);

		Option debug = new Option("debug", "print debugging information");
		options.addOption(debug);
	}

	@SuppressWarnings("static-access")
	private void initArgOptions() {
		Option logFile = OptionBuilder.withArgName("file")
				                      .hasArg()
				                      .withDescription("use given file for log")
				                      .create("logfile");

		Option logger = OptionBuilder.withArgName("classname")
				                     .hasArg()
				                     .withDescription("the class which it to perform logging")
				                     .create("logger");

		Option testFile = OptionBuilder.withArgName("file")
				                       .hasArg()
				                       .withDescription("use given test file")
				                       .create("testfile");
		options.addOption(logFile);
		options.addOption(logger);
		options.addOption(testFile);
	}
	
	@SuppressWarnings("static-access")
	private void initPropertyOptions() {
		Option testType  = OptionBuilder.withArgName( "test=value" )
                                        .hasArgs(2).isRequired()
                                        .withValueSeparator()
                                        .withDescription( "use value to set the appropriate test, e.g. cpu, mem, io" )
                                        .create("T");
		options.addOption(testType);

Option property  = OptionBuilder.withArgName( "property=value" )
                                        .hasArgs(2)
                                        .withValueSeparator()
                                        .withDescription( "use value for given property" )
                                        .create( "D" );
		options.addOption(property);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Sysbench sysbench = new Sysbench();

		if (args == null || args.length <= 0 ) {
			sysbench.help();
		} else {
			// create the parser
			CommandLineParser parser = new BasicParser();
			try {
				// parse the command line arguments
				CommandLine line = parser.parse(sysbench.getOptions(), args);
				P.println(line.toString());
				
			} catch (ParseException exp) {
				// oops, something went wrong
				P.println("Parsing failed.  Reason: " + exp.getMessage());
			}
		}
	}

}
