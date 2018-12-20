
package edu.unsw.cse.xacml.translator.v_3_0;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.translator.util.PolicyElementIdentifierUtil;
import edu.unsw.cse.xacml.translator.util.XACMLTranslatingException;
import edu.unsw.cse.xacml.translator.v_3_0.PolicySetTranslator;
import edu.unsw.cse.xacml.util.XACMLUtilV3;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import static org.junit.Assert.*;

public class PolicySetTranslatorTest {
	private static final String POLICYSET_FILE = "src/test/resources/xacml3-policyset-sli.xml";
	// private static final String POLICYSET_FILE = "policies/sample-xacml3/continue-a-xacml3.xml";
	private static final String SAMPLE_POLICYSET_FILE = "src/test/resources/xacml3-AllOfExpressionTest2.xml";

	@Test
	public void testTranslate() throws ParserConfigurationException, SAXException, IOException {
		PolicySetType policySet = XACMLUtilV3.unmarshalPolicySetType(SAMPLE_POLICYSET_FILE);
		assertNotNull(policySet);
		PolicySetTranslator pst = new PolicySetTranslator(policySet);
		// set the root policy set id/name
		int policysetId = PolicyElementIdentifierUtil.ROOT_POLICYSET_IDENTIFIER;
		String containerId = PolicyElementIdentifierUtil.POLICYSET_NAME_PREFIX + String.valueOf(policysetId);;
		try {
			pst.parse();
			String aspProgStr = pst.translateToASP(containerId, policysetId);
			System.out.println(aspProgStr);
//			System.out.println(pst);
//			AllOfExpression.printAttributes();
			return;
		} catch (XACMLTranslatingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fail("Exceptions occured");
	}

	@Ignore
	@Test
	public void testParse() throws ParserConfigurationException, SAXException, IOException {
		PolicySetType policySet = XACMLUtilV3.unmarshalPolicySetType(POLICYSET_FILE);
		assertNotNull(policySet);
		PolicySetTranslator pst = new PolicySetTranslator(policySet);
		try {
			pst.parse();
			System.out.println(pst);
			AttributeMapper mapper = AttributeMapper.getInstance();
			mapper.printAttributes();
			return;
		} catch (XACMLTranslatingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fail("Exceptions occured");
	}

}
