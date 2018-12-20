package edu.unsw.cse.xacml.translator.v_3_0;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.translator.util.XACMLTranslatingException;
import edu.unsw.cse.xacml.translator.v_3_0.AllOfExpression;
import edu.unsw.cse.xacml.util.XACMLUtilV3;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AllOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AnyOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.RuleType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.TargetType;

public class AllOfExpressionTest {
//	private static final String SAMPLE_POLICY_FILE = "src/test/resources/xacml3-null.xml";
	private static final String SAMPLE_POLICYSET_FILE = "src/test/resources/xacml3-AllOfExpressionTest2.xml";

	@Ignore
	@Test
	public void testParse() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		TargetType target = readTarget();
		assertNotNull(target);

		List<AnyOfType> lstAnyOf = target.getAnyOf();
		assertTrue(lstAnyOf != null && lstAnyOf.size() >= 1);

		List<AllOfType> lstAllOf = lstAnyOf.get(0).getAllOf();
		assertTrue(lstAllOf != null && lstAllOf.size() >= 1);

		AllOfType allOf = lstAllOf.get(0);
		assertTrue(allOf != null);


		AllOfExpression allOfExp = new AllOfExpression(allOf);
		try {
			allOfExp.parse();
			System.out.println(allOfExp);
			AttributeMapper mapper = AttributeMapper.getInstance();
			mapper.printAttributes();
		} catch (XACMLTranslatingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testTranslate() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		TargetType target = readTarget();
		assertNotNull(target);

		List<AnyOfType> lstAnyOf = target.getAnyOf();
		assertTrue(lstAnyOf != null && lstAnyOf.size() >= 1);

		List<AllOfType> lstAllOf = lstAnyOf.get(0).getAllOf();
		assertTrue(lstAllOf != null && lstAllOf.size() >= 1);

		AllOfType allOf = lstAllOf.get(0);
		assertTrue(allOf != null);

		String containerId = "r1";
		String anyOfId = "any1";
		AllOfExpression allOfExp = new AllOfExpression(allOf);
		try {
			allOfExp.parse();
			String aspRuleStr = allOfExp.translateToASP(containerId, anyOfId);
			System.out.println(aspRuleStr);
//			System.out.println(allOfExp);
//			AllOfExpression.printAttributes();
		} catch (XACMLTranslatingException e) {
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
}
