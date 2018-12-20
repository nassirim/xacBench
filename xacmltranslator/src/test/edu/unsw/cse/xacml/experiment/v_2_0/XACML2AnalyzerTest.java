/**
 * 
 */
package edu.unsw.cse.xacml.experiment.v_2_0;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import edu.unsw.cse.xacml.profiles._2_0_.policy.PolicySetType;
import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.util.XACMLUtilV2;

/**
 * @author Mohsen
 *
 */
public class XACML2AnalyzerTest {
	@Test
	public void testTranslate() throws Exception {
		String xacmlFileName;
		xacmlFileName = "src/test/resources/xacml-v2/xacml2-sample-policy.xml";
		xacmlFileName = "src/test/resources/xacml-v2/continue-a.xml";
//		xacmlFileName = "src/test/resources/xacml-v2/continue-b.xml";
		PolicySetType policysetObj = XACMLUtilV2.unmarshalPolicySetType(xacmlFileName);
		assertNotNull(policysetObj);
		XACML2Analyzer xacmlAnalyzer = new XACML2Analyzer(policysetObj);
		xacmlAnalyzer.printPolicysetStat();
		AttributeMapper mapper = AttributeMapper.getInstance();
		mapper.printAttributes();
	}

}
