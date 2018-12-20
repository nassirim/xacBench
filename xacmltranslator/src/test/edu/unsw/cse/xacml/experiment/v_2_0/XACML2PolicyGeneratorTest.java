/**
 * 
 */
package edu.unsw.cse.xacml.experiment.v_2_0;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import edu.unsw.cse.xacml.translator.util.AttributeMapper;

/**
 * @author Mohsen
 *
 */
public class XACML2PolicyGeneratorTest {
	
	@Ignore
	@Test
	public void testXACMLPolicyGenerator1() throws Exception {
		String xacmlFileName;
		xacmlFileName = "src/test/resources/xacml-v2/xacml2-sample-policy.xml";
		xacmlFileName = "src/test/resources/xacml-v2/continue-a.xml";
//		xacmlFileName = "src/test/resources/xacml-v2/continue-b.xml";

		XACML2PolicyGenerator policyGenerator = new XACML2PolicyGenerator(xacmlFileName);
		assertNotNull(policyGenerator.getPolicyset());
		XACML2Analyzer xacmlAnalyzer = new XACML2Analyzer(policyGenerator.getPolicyset());
		xacmlAnalyzer.printPolicysetStat();
		AttributeMapper mapper = AttributeMapper.getInstance();
		mapper.printAttributes();
		System.out.println();
		
//		policyGenerator.duplicateXACMLPolicy("datasets/xacml3_3.xml", 5);
//		xacmlAnalyzer.printPolicysetStat();
//		System.out.println();
		
		policyGenerator.generateXACMLPolicy("datasets/xacml2_1.xml", 30);
//		policyGenerator.generateXACMLPolicy("datasets/xacml2_2.xml", 3);
		xacmlAnalyzer.setPolicyset(policyGenerator.getPolicyset());
		xacmlAnalyzer.printPolicysetStat();
		mapper = AttributeMapper.getInstance();
		mapper.printAttributes();
	}

	@Test
	public void testXACMLPolicyGenerator() throws Exception {
		String xacmlFileName;
		xacmlFileName = "src/test/resources/xacml-v2/xacml2-sample-policy.xml";
		xacmlFileName = "src/test/resources/xacml-v2/continue-a.xml";
//		xacmlFileName = "src/test/resources/xacml-v2/continue-b.xml";

		XACML2PolicyGenerator policyGenerator = new XACML2PolicyGenerator(xacmlFileName);
		assertNotNull(policyGenerator.getPolicyset());
		XACML2Analyzer xacmlAnalyzer = new XACML2Analyzer(policyGenerator.getPolicyset());
		xacmlAnalyzer.printPolicysetStat();
		System.out.println();
		
		policyGenerator.duplicateXACMLPolicy("datasets/xacml2_3.xml", 5);
		xacmlAnalyzer.printPolicysetStat();
		System.out.println();
		
		policyGenerator.generateXACMLPolicy("datasets/xacml2_2.xml", 30);
//		policyGenerator.generateXACMLPolicy("datasets/xacml3_2.xml", 3);
		xacmlAnalyzer.setPolicyset(policyGenerator.getPolicyset());
		xacmlAnalyzer.printPolicysetStat();
	}
}
