
package edu.unsw.cse.xacml.translator.v_3_0;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AllOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AnyOfType;

import java.util.*;

import edu.unsw.cse.xacml.translator.util.XACMLTranslatingException;

/**
 * AnyOfExpression class is to translate AnyOf XACML 3.0 element to return map of parsed attributes.
 * <p/>
 * Note: AnyOf element is composed from disjunctive sequence of AllOf elements.
 * <p/>
 * AnyOfType := SEQUENCE_OF<AllOfType>
 *
 * @author Mohsen
 */
public class AnyOfExpression {

	private AnyOfType anyOf = null;

	//    private List<AllOfExpression> allOfList = null;

	public AnyOfExpression(AnyOfType anyOf) {
		if (anyOf == null) {
			throw new IllegalArgumentException("Cannot parse a null AnyOf expression");
		}
		this.anyOf = anyOf;
		//       allOfList = new ArrayList<AllOfExpression>();
	}

	/**
	 * Parse an AnyOf object
	 * @throws XACMLTranslatingException
	 */
	public void parse() throws XACMLTranslatingException {
		if (anyOf == null) {
			throw new XACMLTranslatingException("AnyOf element must not be null");
		}

		List<AllOfType> lstAllOf = anyOf.getAllOf();
		if (lstAllOf == null || lstAllOf.size() == 0) {
			return;
		}
		for (AllOfType allOf : lstAllOf) {
			AllOfExpression allOfExpression = new AllOfExpression(allOf);
			allOfExpression.parse();
		}
	}

	/**
	 * Translate this object into an ASP program.
	 * @param containerId	The identifier of the rule, policy, or policy set which is the container of this AllOf object
	 * @param anyOfId		The identifier of the AnyOf object which is the container of this AllOf object 
	 * @throws XACMLTranslatingException 
	 */
	public String translateToASP(String containerId, String anyOfId) throws XACMLTranslatingException {
		// TODO Auto-generated method stub
		String aspProgStr = "";
		List<AllOfType> lstAllOf = anyOf.getAllOf();
		if (lstAllOf == null || lstAllOf.size() == 0) {
			return "";
		}
		for (AllOfType allOf : lstAllOf) {
			AllOfExpression allOfExpression = new AllOfExpression(allOf);
			aspProgStr += allOfExpression.translateToASP(containerId, anyOfId);
			aspProgStr += "\n";
		}
		return aspProgStr;
	}

	@Override
	public String toString() {
		String str = "anyOf=";
		if (this.anyOf != null) {
			List<AllOfType> lstAllOf = anyOf.getAllOf();
			for (AllOfType allOf : lstAllOf) {
				AllOfExpression allOfExpression = new AllOfExpression(allOf);
				try {
					allOfExpression.parse();
					str += "[" + allOfExpression.toString() + "]\n";
				} catch (Exception e) {

				}
			}
		}
		return str;
	}
}
