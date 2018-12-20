
package edu.unsw.cse.xacml.translator.v_2_0;

import edu.unsw.cse.xacml.profiles._2_0_.policy.PolicySetType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.PolicyType;
import edu.unsw.cse.xacml.translator.util.CombiningAlgConverterUtil;
import edu.unsw.cse.xacml.translator.util.PolicyElementIdentifierUtil;
import edu.unsw.cse.xacml.translator.util.XACMLTranslatingException;

import java.util.ArrayList;
import java.util.List;

/**
 * Create MIDD from a XACML 3.0 PolicySet element.
 */
public class PolicySetTranslator {
	//   private static final Logger log = LoggerFactory.getLogger(PolicySetTranslator.class);

	private PolicySetType policyset;
	private List<Object> children;

	/**
	 * Constructor.
	 *
	 * @param policy    a XACML 3.0 policy element.
	 */
	public PolicySetTranslator(PolicySetType policyset) {
		if (policyset == null) {
			throw new IllegalArgumentException("PolicySetType argument must not be null");
		}
		this.policyset = policyset;
	}

	/**
	 * Parse this object to collect its element.
	 * @param policyId		The identifier of this policy set
	 * @return
	 * @throws XACMLTranslatingException 
	 */
	public void parse() throws XACMLTranslatingException {
		// TODO Auto-generated method stub
		if (policyset == null) {
			throw new XACMLTranslatingException("null policy set object.");
		}
		// extract the children of the policy set
		extractChilden();
		// Warning: must convert children policy/policy set in its natural order to compliant with some ordered-RCAs (e.g: First-Applicable)
		for (Object obj : this.children) {
			if (obj instanceof PolicyType) {
				PolicyType pol = (PolicyType) obj;
				PolicyTranslator pt = new PolicyTranslator(pol);
				pt.parse();
			} else if (obj instanceof PolicySetType) {
				PolicySetType polset = (PolicySetType) obj;
				PolicySetTranslator pst = new PolicySetTranslator(polset);
				pst.parse();
			} else {
				throw new XACMLTranslatingException("Unknown children policyset type");
			}
		}
		// parse the target of the policy set
		TargetExpression te = new TargetExpression(policyset.getTarget());
		te.parse();
	}

	/**
	 * Translate this object into an ASP program.
	 * @param containerId	The identifier of the policy set which is the container of this object
	 * @param policyId		The identifier of this policy set
	 * @return
	 * @throws XACMLTranslatingException 
	 */
	public String translateToASP(String containerId, int policysetId) throws XACMLTranslatingException {
		// TODO Auto-generated method stub
		if (policyset == null) {
			throw new XACMLTranslatingException("null policy set object.");
		}
		String aspProgStr = "";
		String policysetName = PolicyElementIdentifierUtil.POLICYSET_NAME_PREFIX + String.valueOf(policysetId);
		// translate the children of the policy set
		extractChilden();
		// Warning: must convert children policy/policy set in its natural order to compliant with some ordered-RCAs (e.g: First-Applicable)
		int childPolicysetId;
		int policyId;
		for (Object obj : this.children) {
			if (obj instanceof PolicyType) {
				policyId = PolicyElementIdentifierUtil.getNextPolicyId();
				PolicyType pol = (PolicyType) obj;
				PolicyTranslator pt = new PolicyTranslator(pol);
				aspProgStr += pt.translateToASP(policysetName, policyId);
				aspProgStr += "\n";
			} else if (obj instanceof PolicySetType) {
				childPolicysetId = PolicyElementIdentifierUtil.getNextPolicysetId();
				PolicySetType polset = (PolicySetType) obj;
				PolicySetTranslator pst = new PolicySetTranslator(polset);
				aspProgStr += pst.translateToASP(policysetName, childPolicysetId);
				aspProgStr += "\n";
			} else {
				throw new XACMLTranslatingException("Unknown children policyset type");
			}
		}
		// translate the target of the policy set
		TargetExpression te = new TargetExpression(policyset.getTarget());
		aspProgStr += te.translateToASP(policysetName);
		aspProgStr += "\n";
		// translate the policy as a part of its container
		// sample: policy_set(ps1, 1, ps0, first_applicable).
		aspProgStr += "policy_set(" + policysetName + ", " + String.valueOf(policysetId)
								+ ", " + containerId + ", "
								+ CombiningAlgConverterUtil.getAlgorithm(policyset.getPolicyCombiningAlgId())
								+ ").";
		aspProgStr += "\n";
		return aspProgStr;
	}

	/**
	 * Extract the list of children for this policy object
	 * @throws XACMLTranslatingException
	 */
	private void extractChilden() throws XACMLTranslatingException {
		List<Object> objs = policyset.getPolicySetOrPolicyOrPolicySetIdReference();

		if (objs == null || objs.size() == 0) {
			throw new XACMLTranslatingException("No children policy/policyset found in the policyset " + policyset.getPolicySetId());
		}
		children = new ArrayList<>();

		for (Object objValue : objs) {
			if (objValue != null) {
				if (objValue instanceof PolicyType || objValue instanceof PolicySetType) {
					children.add(objValue);
				}
			}
		}

		if (children.size() == 0) {
			throw new XACMLTranslatingException("No children policy/policyset found in the policy: " + policyset.getPolicySetId());
		}
	}

	@Override
	public String toString() {
		String str = "Policy set=[";
		if (policyset != null) {
			str += policyset.getPolicySetId() + ",\n";
			try {
				str += CombiningAlgConverterUtil.getAlgorithm(policyset.getPolicyCombiningAlgId()) + ",\n";
				TargetExpression te = new TargetExpression(policyset.getTarget());
				str += te + ",\n";
				str += "Children=[";
				extractChilden();
				for (Object obj : this.children) {
					if (obj instanceof PolicyType) {
						PolicyType pol = (PolicyType) obj;
						PolicyTranslator pt = new PolicyTranslator(pol);
						str += pt + ",\n";
					} else if (obj instanceof PolicySetType) {
						PolicySetType polset = (PolicySetType) obj;
						PolicySetTranslator pst = new PolicySetTranslator(polset);
						str += pst + ",\n";
					}
				}
				str += "\n]";
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			str += "{}]";
		}
		return str;
	}
}
