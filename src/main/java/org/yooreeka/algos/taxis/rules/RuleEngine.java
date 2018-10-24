package org.yooreeka.algos.taxis.rules;

import java.util.Collection;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.yooreeka.examples.spamfilter.ClassificationResult;
import org.yooreeka.examples.spamfilter.data.Email;
import org.yooreeka.util.P;

public class RuleEngine {

	//private RuleBase rules;
	private KnowledgeBase kbase;
	
	public RuleEngine(String rulesFile) throws RuleEngineException {

		try {
			/*
			 * A <tt>KnowledgeBuilder</tt> is used to turn a DRL source file
			 * into <tt>Package</tt> objects which the Knowledge Base can
			 * consume.
			 */
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			
			// this will parse and compile the DRL file
			Resource r = ResourceFactory.newFileResource(rulesFile);
			kbuilder.add(r, ResourceType.DRL);

			// Check the builder for errors
			if (kbuilder.hasErrors()) {
				P.println(kbuilder.getErrors().toString());
				throw new RuntimeException("Unable to compile the DRL file: "
						+ rulesFile);
			}

			// get the compiled packages
			final Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();

			// add the packages to a KnowledgeBase (deploy the knowledge
			// packages).
			kbase = KnowledgeBaseFactory.newKnowledgeBase();
			kbase.addKnowledgePackages(pkgs);

			// // build a rule package
			// PackageBuilder builder = new PackageBuilder(cfg);
			//
			// // parse and compile rules
			// builder.addPackageFromDrl(source);
			//
			// Package pkg = builder.getPackage();
			//
			// rules = RuleBaseFactory.newRuleBase();
			// rules.addPackage(pkg);

		} catch (Exception e) {
			throw new RuleEngineException("Could not load/compile rules from DRL file: '" 
		                                 + rulesFile+ "' ", e);
		}
	}

	public void executeRules(ClassificationResult classificationResult, Email email) {
		
		final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

		ksession.setGlobal("classificationResult", classificationResult);
		ksession.insert(email);
		ksession.fireAllRules();
	}
}