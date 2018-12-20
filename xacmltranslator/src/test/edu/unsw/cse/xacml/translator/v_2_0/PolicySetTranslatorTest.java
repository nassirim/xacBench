package edu.unsw.cse.xacml.translator.v_2_0;

import static org.junit.Assert.fail;

import org.junit.Test;
import edu.unsw.cse.xacml.profiles._2_0_.policy.PolicySetType;
import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.translator.util.PolicyElementIdentifierUtil;
import edu.unsw.cse.xacml.util.XACMLUtilV2;

public class PolicySetTranslatorTest {

    private static final String POLICY_FILE = "src/test/resources/xacml2-sample-policy.xml";

	@Test
	public void testTranslate() {
		AttributeMapper mapper = AttributeMapper.getInstance();
		try {
			PolicySetType policySet = XACMLUtilV2.unmarshalPolicySetType(POLICY_FILE);
			PolicySetTranslator pst = new PolicySetTranslator(policySet);
			int policysetId = PolicyElementIdentifierUtil.ROOT_POLICYSET_IDENTIFIER;
			String containerId = PolicyElementIdentifierUtil.POLICYSET_NAME_PREFIX
					+ String.valueOf(policysetId);
			String aspProgStr = pst.translateToASP(containerId, policysetId);
			System.out.println(aspProgStr);
			mapper.printAttributes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fail("Exceptions occured");
	}
}
