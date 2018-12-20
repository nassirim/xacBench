
package edu.unsw.cse.xacml.translator.v_3_0;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.unsw.cse.xacml.translator.v_3_0.PolicyTranslator;
import edu.unsw.cse.xacml.util.XACMLUtilV3;

import javax.xml.bind.JAXBElement;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PolicyTranslatorTest {

	//	private static final String SAMPLE_POLICY_FILE = "src/test/resources/xacml3-AnyOf.xml";
//	private static final String SAMPLE_POLICYSET_FILE = "src/test/resources/PolicySetMedicalDemo_Dhouha.xml";
	private static final String SAMPLE_POLICYSET_FILE = "src/test/resources/xacml3-AllOfExpressionTest2.xml";

	@Test
	public void testTranslate() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		PolicyType policy = readPolicy();
		assertNotNull(policy);
		//		assertNotNull(target.getAnyOf());
		//		assertTrue(target.getAnyOf().size() > 0);

		String containerId = "ps0";
		int policyId = 1;
		PolicyTranslator pt = new PolicyTranslator(policy);
		try {
			pt.parse();
			String aspProgStr = pt.translateToASP(containerId, policyId);
			System.out.println(aspProgStr);
//			System.out.println(pt);
			//			AllOfExpression.printAttributes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void testParse() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		PolicyType policy = readPolicy();
		assertNotNull(policy);
		//		assertNotNull(target.getAnyOf());
		//		assertTrue(target.getAnyOf().size() > 0);

		PolicyTranslator pt = new PolicyTranslator(policy);
		try {
			pt.parse();
			System.out.println(pt);
			//			AllOfExpression.printAttributes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private PolicyType readPolicy() throws ParserConfigurationException,
	SAXException, IOException, FileNotFoundException {
		PolicySetType policySet = XACMLUtilV3.unmarshalPolicySetType(SAMPLE_POLICYSET_FILE);
        assertNotNull(policySet);
		List<JAXBElement<?>> objs = policySet.getPolicySetOrPolicyOrPolicySetIdReference();

		assertNotNull(objs);
		assertTrue(objs.size() >= 1);
		Object objValue = objs.get(0).getValue();
		assertTrue(objValue instanceof PolicyType);
		
		PolicyType p1 = (PolicyType) objValue;
		/*
		List<Object> objs2 = p1.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition();
		assertNotNull(objs2);
		assertTrue(objs2.size() >= 1);
		assertTrue(objs2.get(0) instanceof RuleType);

		RuleType r1 = (RuleType) objs2.get(0);
*/
		return p1;
	}
}
