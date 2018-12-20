
package edu.unsw.cse.xacml.translator.v_2_0;

import edu.unsw.cse.xacml.profiles._2_0_.policy.RuleType;
import edu.unsw.cse.xacml.translator.util.PolicyElementIdentifierUtil;
import edu.unsw.cse.xacml.translator.util.XACMLTranslatingException;

public class RuleTranslator {
	//    private static final transient org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RuleTranslator.class);

	private RuleType rule;

	/**
	 * @param rule       A XACML 3.0 Rule element
	 */
	public RuleTranslator(RuleType rule) {
		if (rule == null) {
			throw new IllegalArgumentException("RuleType argument must not be null");
		}
		//        log.debug("Processing rule: {}", rule.getRuleId());
		this.rule = rule;
	}

	/**
	 * Parse this object to collect its elements
	 * @throws XACMLTranslatingException 
	 */
	public void parse() throws XACMLTranslatingException {
		// TODO Auto-generated method stub
		if (rule == null) {
			throw new XACMLTranslatingException("null rule object.");
		}
		if (rule.getTarget() != null) {
			TargetExpression te = new TargetExpression(rule.getTarget());
			te.parse();
		}
		if (rule.getCondition() != null) {
			ConditionExpression ce = new ConditionExpression(rule.getCondition());
			ce.parse();
		}
	}

	/**
	 * Translate this object into an ASP program.
	 * @param containerId	The identifier of the policy or policy set which is the container of this object
	 * @param ruleId		The identifier of the rule
	 * @throws XACMLTranslatingException 
	 */
	public String translateToASP(String containerId, int ruleId) throws XACMLTranslatingException {
		// TODO Auto-generated method stub
		if (rule == null) {
			return "";
		}
		String aspProgStr = "";
		String ruleName = PolicyElementIdentifierUtil.RULE_NAME_PREFIX + String.valueOf(ruleId);
		TargetExpression te = new TargetExpression(rule.getTarget());
		aspProgStr += te.translateToASP(ruleName);
		aspProgStr += "\n";
		ConditionExpression ce = new ConditionExpression(rule.getCondition());
		aspProgStr += ce.translateToASP(ruleName);
		aspProgStr += "\n";
		//sample: rule(r7, 7, p4, deny).
		aspProgStr += "rule(" + ruleName + ", " + String.valueOf(ruleId)
		+ ", " + containerId + ", "
		+ rule.getEffect().value().toLowerCase() + ").";
		aspProgStr += "\n";
		return aspProgStr;
	}

	@Override
	public String toString() {
		String str = "Rule=[";
		if (rule != null) {
			str += rule.getRuleId() + ",\n";
			str += rule.getEffect().value().toLowerCase() + ",\n";
			try {
				TargetExpression te = new TargetExpression(rule.getTarget());
				str += te + ",\n";
				ConditionExpression ce = new ConditionExpression(rule.getCondition());
				ce.parse();
				str += ce + "\n]";
			} catch (Exception e) {
			}
		} else {
			str += "{}]";
		}
		return str;
	}
}
