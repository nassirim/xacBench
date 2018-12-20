/**
 * 
 */
package edu.unsw.cse.xacml.experiment.v_3_0;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import edu.unsw.cse.xacml.experiment.v_3_0.XACML3Analyzer;
import edu.unsw.cse.xacml.util.XACMLUtilV3;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;

/**
 * @author Mohsen
 *
 */
public class XACML3AnalyzerTest {
	@Test
	public void testTranslate() throws Exception {
		String xacmlFileName;                
		xacmlFileName = "/Users/shayan_310/Documents/repositories/xacml/XACMLTranslator/src/test/resources/xacml3-policyset-sli.xml";
//		xacmlFileName = "src/test/resources/xacml3-policyset-sli.xml";
//		xacmlFileName = "src/test/resources/xacml3-AllOfExpressionTest2.xml";
//		xacmlFileName = "src/test/resources/continue-a-xacml3.xml";
		PolicySetType policysetObj = XACMLUtilV3.unmarshalPolicySetType(xacmlFileName);
		assertNotNull(policysetObj);
		XACML3Analyzer xacmlAnalyzer = new XACML3Analyzer(policysetObj);
		xacmlAnalyzer.printPolicysetStat();
		
	}

}
