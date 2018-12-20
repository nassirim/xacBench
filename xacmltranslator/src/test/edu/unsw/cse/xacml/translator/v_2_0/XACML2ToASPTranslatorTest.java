
package edu.unsw.cse.xacml.translator.v_2_0;

import org.junit.Test;

import edu.unsw.cse.xacml.translator.share.XACMLToASP;


public class XACML2ToASPTranslatorTest {

	@Test
	public void testTranslate() {
		String xacmlFileName;
		xacmlFileName = "src/test/resources/xacml-v2/xacml2-sample-policy.xml";
		xacmlFileName = "src/test/resources/xacml-v2/continue-a.xml";
		XACML2ToASP xacml2ToASP = new XACML2ToASP();
		xacml2ToASP.translateToASP(xacmlFileName, "outputs_v2/policy_v2.asp");
		xacml2ToASP.generateAllRequestProgram("outputs_v2/generate_all.asp");
		xacml2ToASP.generateSampleRequestProgram("outputs_v2/request.asp", 5);
		xacml2ToASP.generateMatchProgram("outputs_v2");
		xacml2ToASP.generateScenarioProgram("outputs_v2/scenario_finding.asp");
		xacml2ToASP.generatePropertyProgram("outputs_v2", XACMLToASP.ASP_ALL_PROPERTY_FILE);
	//	fail("Exceptions occured");
	}

}
