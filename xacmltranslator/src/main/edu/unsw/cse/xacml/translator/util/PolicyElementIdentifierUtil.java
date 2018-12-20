package edu.unsw.cse.xacml.translator.util;

public class PolicyElementIdentifierUtil {

	public static int ROOT_POLICYSET_IDENTIFIER = 0;

	public static int ANYOF_INITIAL_IDENTIFIER = 1;
	public static int RULE_INITIAL_IDENTIFIER = 1;
	public static int POLICY_INITIAL_IDENTIFIER = 1;
	public static int POLICYSET_INITIAL_IDENTIFIER = 1;

	public static String ANYOF_NAME_PREFIX = "any";
	public static String RULE_NAME_PREFIX = "r";
	public static String POLICY_NAME_PREFIX = "p";
	public static String POLICYSET_NAME_PREFIX = "ps";
	
	public static String ATTRIBUTE_VARIABLE_NAME_PREFIX = "V";
	
	private static PolicyElementIdentifierUtil ruleIdGenerator = null;
	private static PolicyElementIdentifierUtil policyIdGenerator = null;
	private static PolicyElementIdentifierUtil policysetIdGenerator = null;
	
	public static String XACML_V2_ACTION_ATTR_NAME = "action";
	public static String XACML_V2_ENVIRONMENT_ATTR_NAME = "environment";
	public static String XACML_V2_RESOURCE_ATTR_NAME = "resource";
	public static String XACML_V2_SUBJECT_ATTR_NAME = "subject";
	

	private int nextIdentifier;
	protected PolicyElementIdentifierUtil(int initialId) {
		// Exists only to defeat instantiation.
		nextIdentifier = initialId;
	}

	public static void clearAll() {
		ruleIdGenerator = null;
		policyIdGenerator = null;
		policysetIdGenerator = null;
	}
	
	/**
	 * Create and return the identifier of the next rule.
	 * @return
	 */
	public static int getNextRuleId() {
		if(ruleIdGenerator == null) {
			ruleIdGenerator = new PolicyElementIdentifierUtil(RULE_INITIAL_IDENTIFIER);
		}
		return ruleIdGenerator.nextIdentifier();
	}

	/**
	 * Create and return the identifier of the next policy.
	 * @return
	 */
	public static int getNextPolicyId() {
		if(policyIdGenerator == null) {
			policyIdGenerator = new PolicyElementIdentifierUtil(POLICY_INITIAL_IDENTIFIER);
		}
		return policyIdGenerator.nextIdentifier();
	}

	/**
	 * Create and return the identifier of the next policy set.
	 * @return
	 */
	public static int getNextPolicysetId() {
		if(policysetIdGenerator == null) {
			policysetIdGenerator = new PolicyElementIdentifierUtil(POLICYSET_INITIAL_IDENTIFIER);
		}
		return policysetIdGenerator.nextIdentifier();
	}

	/**
	 * Generate the next identifier and return it.
	 * @return
	 */
	private int nextIdentifier() {
		// TODO Auto-generated method stub
		int theId = nextIdentifier;
		nextIdentifier++;
		return theId;
	}
}
