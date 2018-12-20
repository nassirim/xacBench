
package edu.unsw.cse.xacml.translator.v_3_0;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AnyOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.TargetType;

import java.util.Iterator;
import java.util.List;

import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.translator.util.PolicyElementIdentifierUtil;
import edu.unsw.cse.xacml.translator.util.XACMLTranslatingException;

/**
 * TargetExpression: contains list of AnyOf expression combined by conjunctive operator
 *
 * @author Mohsen
 */
public class TargetExpression {

	/**
	 * The target object
	 */
	private TargetType targetObj;

	public TargetExpression(TargetType targetObj) {
		this.targetObj = targetObj;
	}

	/**
	 * Parse a target object
	 * @throws XACMLTranslatingException
	 */
	public void parse() throws XACMLTranslatingException {
		if (targetObj == null || targetObj.getAnyOf() == null) {
			return;
		}
		List<AnyOfType> lstAnyOf = targetObj.getAnyOf();
		// if the target is empty which means it is always true
		if (lstAnyOf == null || lstAnyOf.size() == 0) {
			return;
		}
		Iterator<AnyOfType> itAnyOf = lstAnyOf.iterator();
		while (itAnyOf.hasNext()) {
			AnyOfType currentAnyOfExp = itAnyOf.next();
			AnyOfExpression aoe = new AnyOfExpression(currentAnyOfExp);
			aoe.parse();
		}
	}

	/**
	 * Translate this object into an ASP program.
	 * A sample rule: target(r1) :- anyof(r1, any1, _, _, _), anyof(r1, any2, _, _, _).
	 * @param containerId	The identifier of the rule, policy, or policy set which is the container of this AllOf object
	 * @throws XACMLTranslatingException 
	 */
	public String translateToASP(String containerId) throws XACMLTranslatingException {
		// TODO Auto-generated method stub
		if (targetObj == null || targetObj.getAnyOf() == null) {
			return translateEmptyTarget(containerId);
		}
		List<AnyOfType> lstAnyOf = targetObj.getAnyOf();
		// if the target is empty which means it is always true
		if (lstAnyOf == null || lstAnyOf.size() == 0) {
			return translateEmptyTarget(containerId);
		}
		String aspProgStr = "", targetRuleStr, targetsubstr;
		boolean flag = true;
		targetRuleStr = "target(" + containerId + ") :- ";
		AttributeMapper mapper = AttributeMapper.getInstance();
		targetsubstr = mapper.getUnderlineAttributes();
		int anyOfId = PolicyElementIdentifierUtil.ANYOF_INITIAL_IDENTIFIER;
		Iterator<AnyOfType> itAnyOf = lstAnyOf.iterator();
		while (itAnyOf.hasNext()) {
			String theAnyOfName;
			anyOfId++;
			theAnyOfName = PolicyElementIdentifierUtil.ANYOF_NAME_PREFIX + String.valueOf(anyOfId);
			AnyOfType currentAnyOfExp = itAnyOf.next();
			AnyOfExpression aoe = new AnyOfExpression(currentAnyOfExp);
			aspProgStr += aoe.translateToASP(containerId, theAnyOfName);
			aspProgStr += "\n";
			if (!flag)
				targetRuleStr += ", ";
			else
				flag = false;
			targetRuleStr += "anyof(" + containerId + " ," + theAnyOfName + ", " + targetsubstr + ")";
		}
		targetRuleStr += ".";
		aspProgStr += targetRuleStr;
		return aspProgStr;
	}

	/**
	 * Translate an empty target object which accepts every request.
	 * A sample rule: target(r1).
	 * @param containerId
	 * @return
	 */
	private String translateEmptyTarget(String containerId) {
		// TODO Auto-generated method stub
		String aspProgStr = "";
		aspProgStr += "target(" + containerId + ").";
		return aspProgStr;
	}

	@Override
	public String toString() {
		String str = "Target=";
		List<AnyOfType> lstAnyOf = targetObj.getAnyOf();
		if (lstAnyOf != null && lstAnyOf.size() > 0) {
			for (AnyOfType anyOf : lstAnyOf) {
				try {
					AnyOfExpression aoe = new AnyOfExpression(anyOf);
					aoe.parse();
					str += "[" + aoe.toString() + "]\n";
				} catch (Exception e) {

				}
			}
		}
		else
			str += "{}";
		return str;
	}
}
