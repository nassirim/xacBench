
package edu.unsw.cse.xacml.translator.v_3_0;

import org.junit.Test;

import edu.unsw.cse.xacml.translator.share.XACMLToASP;
import edu.unsw.cse.xacml.translator.v_3_0.XACML3ToASP;

public class XACML3ToASPTranslatorTest {

	@Test
	public void testTranslate() {
		String xacmlFileName;
		xacmlFileName = "src/test/resources/xacml3-policyset-sli.xml";
		xacmlFileName = "src/test/resources/xacml3-AllOfExpressionTest2.xml";
		xacmlFileName = "src/test/resources/continue-a-xacml3.xml";
		XACML3ToASP xacmlToASP = new XACML3ToASP();
		xacmlToASP.translateToASP(xacmlFileName, "outputs_v3/policy_v3.asp");
		xacmlToASP.generateAllRequestProgram("outputs_v3/generate_all.asp");
		xacmlToASP.generateSampleRequestProgram("outputs_v3/request.asp", 5);
		xacmlToASP.generateScenarioProgram("outputs_v3/scenario_finding.asp");
		
		xacmlToASP.generateMatchProgram("outputs_v3/");
		xacmlToASP.generatePropertyProgram("outputs_v3/", XACMLToASP.ASP_ALL_PROPERTY_FILE);
	//	fail("Exceptions occured");
	}

}
