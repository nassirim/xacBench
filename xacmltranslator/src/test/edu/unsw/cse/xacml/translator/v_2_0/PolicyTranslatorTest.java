package edu.unsw.cse.xacml.translator.v_2_0;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import edu.unsw.cse.xacml.profiles._2_0_.policy.PolicySetType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.PolicyType;
import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.util.XACMLUtilV2;

public class PolicyTranslatorTest {

    private static final String POLICY_FILE = "src/test/resources/xacml2-sample-policy.xml";

	@Test
	public void testTranslate() {
		AttributeMapper mapper = AttributeMapper.getInstance();
		try {
			String containerId = "ps0";
			int policyId = 1;
			PolicyType policy = readPolicy();
			assertNotNull(policy);
			PolicyTranslator pt = new PolicyTranslator(policy);
			String aspProgStr = pt.translateToASP(containerId, policyId);
			System.out.println(aspProgStr);
			mapper.printAttributes();
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fail("Exceptions occured");
	}

	/**
	 * Read an XACMl file, extract and return the first policy object
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private PolicyType readPolicy() throws ParserConfigurationException,
	SAXException, IOException, FileNotFoundException {
//		PolicyType p1 = XACMLUtil.unmarshalPolicyType(new FileInputStream(SAMPLE_POLICY_FILE));
//		return p1.getTarget();
		PolicySetType policySet = XACMLUtilV2.unmarshalPolicySetType(POLICY_FILE);
        assertNotNull(policySet);
		List<Object> objs = policySet.getPolicySetOrPolicyOrPolicySetIdReference();

		assertNotNull(objs);
		assertTrue(objs.size() >= 1);
		Object objValue = objs.get(0);
		assertTrue(objValue instanceof PolicyType);
		
		PolicyType p1 = (PolicyType) objValue;
		
		return p1;
	}  
}
