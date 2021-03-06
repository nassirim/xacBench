
package edu.unsw.cse.xacml.translator.v_3_0;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.RuleType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.TargetType;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.translator.v_3_0.TargetExpression;
import edu.unsw.cse.xacml.util.XACMLUtilV3;

import javax.xml.bind.JAXBElement;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TargetExpressionTest {

	//	private static final String SAMPLE_POLICY_FILE = "src/test/resources/xacml3-AnyOf.xml";
//	private static final String SAMPLE_POLICY_FILE = "src/test/resources/xacml3-null.xml";
	private static final String SAMPLE_POLICYSET_FILE = "src/test/resources/xacml3-AllOfExpressionTest2.xml";

	@Test
	public void testTranslate() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		TargetType target = readTarget();
		assertNotNull(target);
//		assertNotNull(target.getAnyOf());
//		assertTrue(target.getAnyOf().size() > 0);

		System.out.println("\nHere is the translation of a target with two Anyof objects:");
		String containerId = "r1";
		TargetExpression te = new TargetExpression(target);
		try {
			te.parse();
			String aspProgStr = te.translateToASP(containerId);
			System.out.println(aspProgStr);
//			System.out.println(te);
//			AllOfExpression.printAttributes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("\nHere is the translation of an empty target:");
		testTranslateEmptyTarget();
	}

	private void testTranslateEmptyTarget() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		TargetType target = readEmptyTarget();
		assertNotNull(target);
//		assertNotNull(target.getAnyOf());
//		assertTrue(target.getAnyOf().size() > 0);

		String containerId = "r1";
		TargetExpression te = new TargetExpression(target);
		try {
			te.parse();
			String aspProgStr = te.translateToASP(containerId);
			System.out.println(aspProgStr);
//			System.out.println(te);
//			AllOfExpression.printAttributes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void testParse() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		TargetType target = readTarget();
		assertNotNull(target);
//		assertNotNull(target.getAnyOf());
//		assertTrue(target.getAnyOf().size() > 0);

		TargetExpression te = new TargetExpression(target);
		try {
			te.parse();
			System.out.println(te);
			AttributeMapper mapper = AttributeMapper.getInstance();
			mapper.printAttributes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private TargetType readTarget() throws ParserConfigurationException,
	SAXException, IOException, FileNotFoundException {
//		PolicyType p1 = XACMLUtil.unmarshalPolicyType(new FileInputStream(SAMPLE_POLICY_FILE));
//		return p1.getTarget();
		PolicySetType policySet = XACMLUtilV3.unmarshalPolicySetType(SAMPLE_POLICYSET_FILE);
        assertNotNull(policySet);
		List<JAXBElement<?>> objs = policySet.getPolicySetOrPolicyOrPolicySetIdReference();

		assertNotNull(objs);
		assertTrue(objs.size() >= 1);
		Object objValue = objs.get(0).getValue();
		assertTrue(objValue instanceof PolicyType);
		
		PolicyType p1 = (PolicyType) objValue;
		
		List<Object> objs2 = p1.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition();

		assertNotNull(objs2);
		assertTrue(objs2.size() >= 1);
		assertTrue(objs2.get(0) instanceof RuleType);

		RuleType r1 = (RuleType) objs2.get(0);

		TargetType target = r1.getTarget();
		return target;
	}
	
	private TargetType readEmptyTarget() throws ParserConfigurationException,
	SAXException, IOException, FileNotFoundException {
//		PolicyType p1 = XACMLUtil.unmarshalPolicyType(new FileInputStream(SAMPLE_POLICY_FILE));
//		return p1.getTarget();
		PolicySetType policySet = XACMLUtilV3.unmarshalPolicySetType(SAMPLE_POLICYSET_FILE);
        assertNotNull(policySet);
		List<JAXBElement<?>> objs = policySet.getPolicySetOrPolicyOrPolicySetIdReference();

		assertNotNull(objs);
		assertTrue(objs.size() >= 1);
		Object objValue = objs.get(0).getValue();
		assertTrue(objValue instanceof PolicyType);
		
		PolicyType p1 = (PolicyType) objValue;
		
		TargetType target = p1.getTarget();
		return target;
	}
}
