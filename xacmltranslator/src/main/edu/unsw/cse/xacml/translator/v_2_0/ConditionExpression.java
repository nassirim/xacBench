/**
 * 
 */
package edu.unsw.cse.xacml.translator.v_2_0;

import javax.xml.bind.JAXBElement;

import edu.unsw.cse.xacml.translator.util.XACMLTranslatingException;
import edu.unsw.cse.xacml.profiles._2_0_.policy.ConditionType;

/**
 * @author Mohsen
 *
 */
public class ConditionExpression {

	private ConditionType conditionObj = null;

	/**
	 * List of <attribute_id, attribute_value>
	 */
	private JAXBElement<?> expression;


	public ConditionExpression(ConditionType conditionObj) {
		this.conditionObj = conditionObj;
		expression = null;
	}

	/**
	 * Parse an AllOf object
	 * @throws XACMLTranslatingException
	 */
	public void parse() {
		if (conditionObj == null)
			expression = null;
		else
			expression = conditionObj.getExpression();
	}

	@Override
	public String toString() {
		String str = "condition = ";
		if (expression != null)
			str += expression;
		else
			str += "{}";
		return str;
	}

	public String translateToASP(String containerId) {
		// TODO Auto-generated method stub
		String aspProgStr = "";
		aspProgStr += "condition(" +  containerId + ", true).";
		return aspProgStr;
	}

}
