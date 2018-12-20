
package edu.unsw.cse.xacml.translator.v_3_0;

import edu.unsw.cse.xacml.translator.util.AdviceMapper;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.RuleType;
import java.util.ArrayList;
import java.util.List;

import edu.unsw.cse.xacml.translator.util.CombiningAlgConverterUtil;
import edu.unsw.cse.xacml.translator.util.ConditionMapper;
import edu.unsw.cse.xacml.translator.util.ObligationMapper;
import edu.unsw.cse.xacml.translator.util.PolicyElementIdentifierUtil;
import edu.unsw.cse.xacml.translator.util.XACMLTranslatingException;

public class PolicyTranslator {
	//    private static final Logger log = LoggerFactory.getLogger(PolicyTranslator.class);

	private PolicyType policy;

	/**
	 * @param policy    a XACML 3.0 policy element.
	 */
	public PolicyTranslator(PolicyType policy) throws IllegalArgumentException {
		if (policy == null) {
			throw new IllegalArgumentException("PolicyType argument must not be null");
		}
		this.policy = policy;
	}

	/**
	 * Parse the policy object
	 * @throws XACMLTranslatingException
	 */
	public void parse() throws XACMLTranslatingException {
		if (policy == null) {
			throw new XACMLTranslatingException("null policy object.");
		}

		TargetExpression te = new TargetExpression(policy.getTarget());
		te.parse();
		List<RuleType> rules = getRules();
		ConditionMapper cMapper = ConditionMapper.getInstance();
                ObligationMapper oMapper = ObligationMapper.getInstance();
                AdviceMapper advMapper = AdviceMapper.getInstance();
                
                // parse rules inside the policy
		for (RuleType r : rules) {
                    cMapper.addCondition(r.getRuleId(), r.getCondition());
                    oMapper.addObligation(r.getRuleId(), r.getObligationExpressions());
                    advMapper.addAdvice(r.getRuleId(), r.getAdviceExpressions());
                    RuleTranslator ruleTranslator = new RuleTranslator(r);
                    ruleTranslator.parse();
                        
		}
	}

	/**
	 * Translate this object into an ASP program.
	 * @param containerId	The identifier of the policy set which is the container of this object
	 * @param policyId		The identifier of this policy
	 * @return
	 * @throws XACMLTranslatingException 
	 */
	public String translateToASP(String containerId, int policyId) throws XACMLTranslatingException {
		// TODO Auto-generated method stub
		if (policy == null) {
			throw new XACMLTranslatingException("null policy object.");
		}
		String aspProgStr = "";
		String policyName = PolicyElementIdentifierUtil.POLICY_NAME_PREFIX + String.valueOf(policyId);

		List<RuleType> rules = getRules();
		int ruleId;
		// translate rules in the policy
		for (RuleType r : rules) {
			ruleId = PolicyElementIdentifierUtil.getNextRuleId();
			RuleTranslator ruleTranslator = new RuleTranslator(r);
			aspProgStr += ruleTranslator.translateToASP(policyName, ruleId);
			aspProgStr += "\n";
		}
		// translate the target of the policy
		TargetExpression te = new TargetExpression(policy.getTarget());
		aspProgStr += te.translateToASP(policyName);
		aspProgStr += "\n";
		// translate the policy as a part of its container
		// sample: policy(p1, 1, ps1, deny_overrides).
		aspProgStr += "policy(" + policyName + ", " + String.valueOf(policyId)
								+ ", " + containerId + ", "
								+ CombiningAlgConverterUtil.getAlgorithm(policy.getRuleCombiningAlgId())
								+ ").";
		aspProgStr += "\n";
		return aspProgStr;
	}
	
	/**
	 * Extract the list of rules from the policy object
	 * @return
	 * @throws XACMLTranslatingException
	 */
	private List<RuleType> getRules() throws XACMLTranslatingException {
		List<Object> objs = policy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition();

		if (objs == null || objs.size() == 0) {
			throw new XACMLTranslatingException("No children rules found in the policy: " + policy.getPolicyId());
		}
		List<RuleType> rules = new ArrayList<RuleType>();
		for (Object obj : objs) {
			if (obj instanceof RuleType) {
				rules.add((RuleType) obj);
			} else {
				//				log.info("Unsupport element of type " + obj.getClass() + " inside policy '" + policy.getPolicyId() + "'");
			}
		}
		if (rules.size() == 0) {
			throw new XACMLTranslatingException("No children rules found in the policy: '" + policy.getPolicyId() + "'");
		}
		return rules;
	}
             

	@Override
	public String toString() {
		String str = "Policy=[";
		if (policy != null) {
			str += policy.getPolicyId() + ",\n";
			try {
				str += CombiningAlgConverterUtil.getAlgorithm(policy.getRuleCombiningAlgId()) + ",\n";
				TargetExpression te = new TargetExpression(policy.getTarget());
				te.parse();
				str += te + ",\n";
				str += "Rules=[";
				List<RuleType> rules = getRules();
				for (RuleType r : rules) {
					RuleTranslator ruleTranslator = new RuleTranslator(r);
					ruleTranslator.parse();
					str += ruleTranslator + ",\n";
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
