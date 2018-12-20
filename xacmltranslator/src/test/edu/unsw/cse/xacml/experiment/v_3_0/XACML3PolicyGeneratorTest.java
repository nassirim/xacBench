/**
 * 
 */
package edu.unsw.cse.xacml.experiment.v_3_0;

import static edu.unsw.cse.xacml.experiment.v_3_0.PolSetGenTest.testGenerator;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import edu.unsw.cse.xacml.experiment.v_3_0.XACML3Analyzer;
import edu.unsw.cse.xacml.experiment.v_3_0.XACML3PolicyGenerator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Mohsen
 *
 */
public class XACML3PolicyGeneratorTest {    
    
        public static void main(String[] args) {
            
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter the full path to input policyset (.xml file):");
        String xacmlFilePath = sc.next();

        System.out.println("Please enter the number of generated rules in output policyset:");
        int ruleNumber = sc.nextInt();

            try {
                testGenerator(xacmlFilePath, ruleNumber);
            } catch (Exception ex) {
                Logger.getLogger(XACML3PolicyGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
   	
/*	@Test
	public void testXACMLPolicyGenerator1() throws Exception {
		String xacmlFileName;
//		xacmlFileName = "src/test/resources/xacml3-policyset-sli.xml";
//		xacmlFileName = "src/test/resources/xacml3-AllOfExpressionTest2.xml";
		xacmlFileName = "src/test/resources/continue-a-xacml3.xml";

		XACML3PolicyGenerator policyGenerator = new XACML3PolicyGenerator(xacmlFileName);
		assertNotNull(policyGenerator.getPolicyset());
		XACML3Analyzer xacmlAnalyzer = new XACML3Analyzer(policyGenerator.getPolicyset());
		xacmlAnalyzer.printPolicysetStat();
		System.out.println();
		
//		policyGenerator.duplicateXACMLPolicy("datasets/xacml3_3.xml", 5);
//		xacmlAnalyzer.printPolicysetStat();
//		System.out.println();
		
		policyGenerator.generateXACMLPolicy("datasets/xacml3_2.xml", 30);
//		policyGenerator.generateXACMLPolicy("datasets/xacml3_2.xml", 3);
		xacmlAnalyzer.setPolicyset(policyGenerator.getPolicyset());
		xacmlAnalyzer.printPolicysetStat();
	}

	@Ignore
	@Test
	public void testXACMLPolicyGenerator() throws Exception {
		String xacmlFileName;
		xacmlFileName = "src/test/resources/xacml3-policyset-sli.xml";
//		xacmlFileName = "src/test/resources/xacml3-AllOfExpressionTest2.xml";
//		xacmlFileName = "src/test/resources/continue-a-xacml3.xml";

		XACML3PolicyGenerator policyGenerator = new XACML3PolicyGenerator(xacmlFileName);
		assertNotNull(policyGenerator.getPolicyset());
		XACML3Analyzer xacmlAnalyzer = new XACML3Analyzer(policyGenerator.getPolicyset());
		xacmlAnalyzer.printPolicysetStat();
		System.out.println();
		
		policyGenerator.duplicateXACMLPolicy("datasets/xacml3_3.xml", 5);
		xacmlAnalyzer.printPolicysetStat();
		System.out.println();
		
		policyGenerator.generateXACMLPolicy("datasets/xacml3_2.xml", 30);
//		policyGenerator.generateXACMLPolicy("datasets/xacml3_2.xml", 3);
		xacmlAnalyzer.setPolicyset(policyGenerator.getPolicyset());
		xacmlAnalyzer.printPolicysetStat();
	} */
}
