/**
 * 
 */
package edu.unsw.cse.xacml.experiment.v_2_0;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.unsw.cse.xacml.profiles._2_0_.policy.ActionMatchType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.ActionType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.ActionsType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.EnvironmentMatchType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.EnvironmentType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.EnvironmentsType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.PolicySetType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.PolicyType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.ResourceMatchType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.ResourceType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.ResourcesType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.RuleType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.SubjectMatchType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.SubjectType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.SubjectsType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.TargetType;
import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.translator.util.PolicyElementIdentifierUtil;
import edu.unsw.cse.xacml.translator.util.XACMLTranslatingException;
import edu.unsw.cse.xacml.translator.v_2_0.PolicySetTranslator;
import edu.unsw.cse.xacml.translator.v_2_0.TargetExpression;
import edu.unsw.cse.xacml.util.XACMLUtil;

/**
 * @author Mohsen
 *
 */
public class XACML2Analyzer {
	private PolicySetType policyset;

	/**
	 * Constructor.
	 *
	 * @param policy
	 *            a XACML 3.0 policy element.
	 * @throws XACMLTranslatingException
	 */
	public XACML2Analyzer(PolicySetType policyset) throws Exception {
		if (policyset == null) {
			throw new IllegalArgumentException("PolicySetType argument must not be null");
		}
		this.policyset = policyset;
	}

	/**
	 * Returns the number of rules in this XACML policy
	 * 
	 * @return
	 */
	public int getNumberOfRules() {
		return getNumberOfRules(this.policyset);
	}

	/**
	 * Returns the number of rules for an XACML element
	 * 
	 * @param objValue
	 * @return
	 */
	private int getNumberOfRules(Object objValue) {
		// TODO Auto-generated method stub
		if (objValue == null)
			return 0;
		if (objValue instanceof RuleType)
			return 1;
		if (objValue instanceof PolicyType) {
			List<RuleType> rules = getRulesOfPolicy((PolicyType) objValue);
			return rules.size();
		}
		if (objValue instanceof PolicySetType) {
			List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
			int k = 0;
			for (Object obj : children) {
				k += getNumberOfRules(obj);
			}
			return k;
		}
		return 0;
	}

	/**
	 * Returns the number of policies in this XACML policy
	 * 
	 * @return
	 */
	public int getNumberOfPolicies() {
		return getNumberOfPolicies(this.policyset);
	}

	/**
	 * Returns the number of policies for an XACML element
	 * 
	 * @param objValue
	 * @return
	 */
	private int getNumberOfPolicies(Object objValue) {
		// TODO Auto-generated method stub
		if (objValue == null)
			return 0;
		if (objValue instanceof PolicyType) {
			return 1;
		}
		if (objValue instanceof PolicySetType) {
			List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
			int k = 0;
			for (Object obj : children) {
				k += getNumberOfPolicies(obj);
			}
			return k;
		}
		return 0;
	}

	/**
	 * Returns the number of policy sets in this XACML policy
	 * 
	 * @return
	 */
	public int getNumberOfPolicysets() {
		return getNumberOfPolicysets(this.policyset);
	}

	/**
	 * Returns the number of policy sets for an XACML element
	 * 
	 * @param objValue
	 * @return
	 */
	private int getNumberOfPolicysets(Object objValue) {
		// TODO Auto-generated method stub
		if (objValue == null)
			return 0;
		if (objValue instanceof PolicySetType) {
			List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
			int k = 1;
			for (Object obj : children) {
				k += getNumberOfPolicysets(obj);
			}
			return k;
		}
		return 0;
	}

	/**
	 * Extract the list of rules from a policy object
	 * 
	 * @param policy
	 * @return
	 * @throws XACMLTranslatingException
	 */
	private List<RuleType> getRulesOfPolicy(PolicyType policy) {
		List<Object> objs = policy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition();

		List<RuleType> rules = new ArrayList<RuleType>();
		if (objs == null || objs.size() == 0) {
			return rules;
		}
		for (Object obj : objs) {
			if (obj instanceof RuleType)
				rules.add((RuleType) obj);
		}
		return rules;
	}

	/**
	 * Extract the list of children for this policy object
	 * 
	 * @return
	 * @throws XACMLTranslatingException
	 */
	private List<Object> extractChildenOfPolicyset(PolicySetType policysetObj) {
		List<Object> objs = policysetObj.getPolicySetOrPolicyOrPolicySetIdReference();
		List<Object> children = new ArrayList<>();
		if (objs == null || objs.size() == 0) {
			return children;
		}
		for (Object objValue : objs) {
			if (objValue != null) {
				if (objValue instanceof PolicyType || objValue instanceof PolicySetType) {
					children.add(objValue);
				}
			}
		}
		return children;
	}

	/**
	 * Removes a rule from this XACML policy
	 * 
	 * @return
	 */
	public void removeRule(ArrayList<Integer> removedIds) {
		removeRule(this.policyset, removedIds, 1);
	}

	private int removeRule(Object objValue, ArrayList<Integer> removedIds, int beginingRuleId) {
		// TODO Auto-generated method stub
		int theRuleId = beginingRuleId;
		if (objValue == null)
			return beginingRuleId;
		if (objValue instanceof PolicyType) {
			List<RuleType> rules = getRulesOfPolicy((PolicyType) objValue);
			List<Object> rList = new ArrayList<Object>();
			for (int i = 0; i < rules.size(); i++) {
				if (removedIds.contains(theRuleId)) {
					rList.add(rules.get(i));
					removedIds.remove(Integer.valueOf(theRuleId));
				}
				theRuleId++;
			}
			for (Object j : rList)
				((PolicyType) objValue).getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().remove(j);
			return theRuleId;
		}
		if (objValue instanceof PolicySetType) {
			List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
			for (Object obj : children) {
				theRuleId = removeRule(obj, removedIds, theRuleId);
			}
			return theRuleId;
		}
		return beginingRuleId;
	}

	/**
	 * Remove all empty policies/policy sets
	 */
	public void removeEmptyElements() {
		// TODO Auto-generated method stub
		while (removeEmptyElements(this.policyset))
			;
	}

	private boolean removeEmptyElements(Object objValue) {
		// TODO Auto-generated method stub
		if (objValue == null)
			return false;
		boolean changedFlag = false;
		if (objValue instanceof PolicySetType) {
			List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
			List<Integer> rList = new ArrayList<Integer>();
			for (int i = 0; i < children.size(); i++) {
				Object obj = children.get(i);
				if (obj instanceof PolicyType) {
					if (getRulesOfPolicy((PolicyType) obj).size() <= 0)
						rList.add(i);
					// rList.add(((PolicySetType)
					// objValue).getPolicySetOrPolicyOrPolicySetIdReference().get(i));
				}
				if (obj instanceof PolicySetType) {
					boolean flag = removeEmptyElements(obj);
					if (flag)
						changedFlag = true;
					if (extractChildenOfPolicyset((PolicySetType) obj).size() <= 0)
						rList.add(i);
					// rList.add(((PolicySetType)
					// objValue).getPolicySetOrPolicyOrPolicySetIdReference().get(i));
				}
			}
			Collections.sort(rList, Collections.reverseOrder());
			for (int j = 0; j < rList.size(); j++)
				((PolicySetType) objValue).getPolicySetOrPolicyOrPolicySetIdReference().remove(rList.get(j).intValue());
			if (changedFlag || rList.size() > 0)
				return true;
			else
				return false;
		}
		return false;
	}

	/**
	 * squeeze the attribute types/values based on the new type/values in the
	 * argument
	 * 
	 * @param newAttributeValues
	 */
	public void squeezeAttributes(Map<String, List<String>> newAttributeValues) {
		// TODO Auto-generated method stub
		squeezeAttributes(this.policyset, newAttributeValues);
	}

	/**
	 * squeeze the attribute types/values based on the new type/values in the
	 * argument
	 * 
	 * @param newAttributeValues
	 */
	private void squeezeAttributes(Object objValue, Map<String, List<String>> newAttributeValues) {
		// TODO Auto-generated method stub
		if (objValue == null)
			return;
		if (objValue instanceof PolicyType) {
			PolicyType policy = (PolicyType) objValue;
			squeezeAttributesTarget(policy.getTarget(), newAttributeValues);
			List<RuleType> rules = getRulesOfPolicy(policy);
			for (int i = 0; i < rules.size(); i++)
				squeezeAttributesTarget(rules.get(i).getTarget(), newAttributeValues);
		}
		if (objValue instanceof PolicySetType) {
			PolicySetType policyset = (PolicySetType) objValue;
			squeezeAttributesTarget(policyset.getTarget(), newAttributeValues);
			List<Object> children = extractChildenOfPolicyset(policyset);
			for (int i = 0; i < children.size();i++) {
				Object obj = children.get(i);
				squeezeAttributes(obj, newAttributeValues);
			}
		}
	}

	/**
	 * squeeze the attribute types/values of a target object based on the new
	 * type/values in the argument
	 * 
	 * @param newAttributeValues
	 */
	private void squeezeAttributesTarget(TargetType targetObj, Map<String, List<String>> newAttributeValues) {
		// TODO Auto-generated method stub
		if (targetObj == null) {
			return;
		}
		if ((targetObj.getActions() == null || targetObj.getActions().getAction() == null) &&
				(targetObj.getEnvironments() == null || targetObj.getEnvironments().getEnvironment() == null) &&
				(targetObj.getResources() == null || targetObj.getResources().getResource() == null) &&
				(targetObj.getSubjects() == null || targetObj.getSubjects().getSubject() == null)) {
			return;
		}
		if (!newAttributeValues.keySet().contains(PolicyElementIdentifierUtil.XACML_V2_ACTION_ATTR_NAME))
			targetObj.setActions(null);
		else
			squeezeAttributesActions(targetObj.getActions(), newAttributeValues);
		if (!newAttributeValues.keySet().contains(PolicyElementIdentifierUtil.XACML_V2_ENVIRONMENT_ATTR_NAME))
			targetObj.setEnvironments(null);
		else
			squeezeAttributesEnvironments(targetObj.getEnvironments(), newAttributeValues);
		if (!newAttributeValues.keySet().contains(PolicyElementIdentifierUtil.XACML_V2_RESOURCE_ATTR_NAME))
			targetObj.setResources(null);
		else
			squeezeAttributesResources(targetObj.getResources(), newAttributeValues);
		if (!newAttributeValues.keySet().contains(PolicyElementIdentifierUtil.XACML_V2_SUBJECT_ATTR_NAME))
			targetObj.setSubjects(null);
		else
			squeezeAttributesSubjects(targetObj.getSubjects(), newAttributeValues);
	}

	private void squeezeAttributesActions(ActionsType actions, Map<String, List<String>> newAttributeValues) {
		// TODO Auto-generated method stub
		if (actions == null || actions.getAction() == null)
			return;
		String attributeId = PolicyElementIdentifierUtil.XACML_V2_ACTION_ATTR_NAME;
		if (!newAttributeValues.keySet().contains(attributeId))
			return;
		for (ActionType a : actions.getAction()) {
			List<ActionMatchType> atList = a.getActionMatch();
			if (atList != null) {
				List<Integer> rList = new ArrayList<Integer>();
				List<String> currAttributeValues = new ArrayList<String>();
				for (int i = 0; i < atList.size();i++) {
					ActionMatchType amt = atList.get(i);
					String attributeValue;
					try {
						attributeValue = TargetExpression.getAttributeValue(amt.getAttributeValue());
					} catch (XACMLTranslatingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						rList.add(i);
						continue;
					}
					if (newAttributeValues.get(attributeId).contains(attributeValue)) {
						currAttributeValues.add(attributeValue);
						continue;
					}
					else {
						// if there is no remaining attribute id
						if (currAttributeValues.size() >= newAttributeValues.get(attributeId).size()) {
							rList.add(i);
							continue;
						}
						// find a random attribute values and replace by the current value
						int indx = -1;
						String newAttrVal = "";
						while (true) {
							indx = XACMLUtil.getRandomNumber(0, newAttributeValues.get(attributeId).size());
							newAttrVal = newAttributeValues.get(attributeId).get(indx);
							if (!currAttributeValues.contains(newAttrVal))
								break;
						}
						if (indx < 0) {
							rList.add(i);
							continue;
						}
						else {
							amt.getAttributeValue().getContent().set(0, newAttrVal);
							currAttributeValues.add(newAttrVal);
						}
					}
				}
				// remove the extra attribute values
				if (rList.size() > 0) {
					Collections.sort(rList, Collections.reverseOrder());
					for (int j = 0; j < rList.size(); j++)
						atList.remove(rList.get(j).intValue());
				}
			}
		}
	}

	private void squeezeAttributesEnvironments(EnvironmentsType environments,
			Map<String, List<String>> newAttributeValues) {
		// TODO Auto-generated method stub
		if (environments == null || environments.getEnvironment() == null)
			return;
		String attributeId = PolicyElementIdentifierUtil.XACML_V2_ENVIRONMENT_ATTR_NAME;
		if (!newAttributeValues.keySet().contains(attributeId))
			return;
		for (EnvironmentType e : environments.getEnvironment()) {
			List<EnvironmentMatchType> etList = e.getEnvironmentMatch();
			if (etList != null) {
				List<Integer> rList = new ArrayList<Integer>();
				List<String> currAttributeValues = new ArrayList<String>();
				for (int i = 0; i < etList.size();i++) {
					EnvironmentMatchType emt = etList.get(i);
					String attributeValue;
					try {
						attributeValue = TargetExpression.getAttributeValue(emt.getAttributeValue());
					} catch (XACMLTranslatingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						rList.add(i);
						continue;
					}
					if (newAttributeValues.get(attributeId).contains(attributeValue)) {
						currAttributeValues.add(attributeValue);
						continue;
					}
					else {
						// if there is no remaining attribute id
						if (currAttributeValues.size() >= newAttributeValues.get(attributeId).size()) {
							rList.add(i);
							continue;
						}
						// find a random attribute values and replace by the current value
						int indx = -1;
						String newAttrVal = "";
						while (true) {
							indx = XACMLUtil.getRandomNumber(0, newAttributeValues.get(attributeId).size());
							newAttrVal = newAttributeValues.get(attributeId).get(indx);
							if (!currAttributeValues.contains(newAttrVal))
								break;
						}
						if (indx < 0) {
							rList.add(i);
							continue;
						}
						else {
							emt.getAttributeValue().getContent().set(0, newAttrVal);
							currAttributeValues.add(newAttrVal);
						}
					}
				}
				// remove the extra attribute values
				if (rList.size() > 0) {
					Collections.sort(rList, Collections.reverseOrder());
					for (int j = 0; j < rList.size(); j++)
						etList.remove(rList.get(j).intValue());
				}
			}
		}
	}

	private void squeezeAttributesResources(ResourcesType resources, Map<String, List<String>> newAttributeValues) {
		// TODO Auto-generated method stub
		if (resources == null || resources.getResource() == null)
			return;
		String attributeId = PolicyElementIdentifierUtil.XACML_V2_RESOURCE_ATTR_NAME;
		if (!newAttributeValues.keySet().contains(attributeId))
			return;
		for (ResourceType r : resources.getResource()) {
			List<ResourceMatchType> rtList = r.getResourceMatch();
			if (rtList != null) {
				List<Integer> rList = new ArrayList<Integer>();
				List<String> currAttributeValues = new ArrayList<String>();
				for (int i = 0; i < rtList.size();i++) {
					ResourceMatchType rmt = rtList.get(i);
					String attributeValue;
					try {
						attributeValue = TargetExpression.getAttributeValue(rmt.getAttributeValue());
					} catch (XACMLTranslatingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						rList.add(i);
						continue;
					}
					if (newAttributeValues.get(attributeId).contains(attributeValue)) {
						currAttributeValues.add(attributeValue);
						continue;
					}
					else {
						// if there is no remaining attribute id
						if (currAttributeValues.size() >= newAttributeValues.get(attributeId).size()) {
							rList.add(i);
							continue;
						}
						// find a random attribute values and replace by the current value
						int indx = -1;
						String newAttrVal = "";
						while (true) {
							indx = XACMLUtil.getRandomNumber(0, newAttributeValues.get(attributeId).size());
							newAttrVal = newAttributeValues.get(attributeId).get(indx);
							if (!currAttributeValues.contains(newAttrVal))
								break;
						}
						if (indx < 0) {
							rList.add(i);
							continue;
						}
						else {
							rmt.getAttributeValue().getContent().set(0, newAttrVal);
							currAttributeValues.add(newAttrVal);
						}
					}
				}
				// remove the extra attribute values
				if (rList.size() > 0) {
					Collections.sort(rList, Collections.reverseOrder());
					for (int j = 0; j < rList.size(); j++)
						rtList.remove(rList.get(j).intValue());
				}
			}
		}
	}

	private void squeezeAttributesSubjects(SubjectsType subjects, Map<String, List<String>> newAttributeValues) {
		// TODO Auto-generated method stub
		if (subjects == null || subjects.getSubject() == null)
			return;
		String attributeId = PolicyElementIdentifierUtil.XACML_V2_SUBJECT_ATTR_NAME;
		if (!newAttributeValues.keySet().contains(attributeId))
			return;
		for (SubjectType s : subjects.getSubject()) {
			List<SubjectMatchType> stList = s.getSubjectMatch();
			if (stList != null) {
				List<Integer> rList = new ArrayList<Integer>();
				List<String> currAttributeValues = new ArrayList<String>();
				for (int i = 0; i < stList.size();i++) {
					SubjectMatchType smt = stList.get(i);
					String attributeValue;
					try {
						attributeValue = TargetExpression.getAttributeValue(smt.getAttributeValue());
					} catch (XACMLTranslatingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						rList.add(i);
						continue;
					}
					if (newAttributeValues.get(attributeId).contains(attributeValue)) {
						currAttributeValues.add(attributeValue);
						continue;
					}
					else {
						// if there is no remaining attribute id
						if (currAttributeValues.size() >= newAttributeValues.get(attributeId).size()) {
							rList.add(i);
							continue;
						}
						// find a random attribute values and replace by the current value
						int indx = -1;
						String newAttrVal = "";
						while (true) {
							indx = XACMLUtil.getRandomNumber(0, newAttributeValues.get(attributeId).size());
							newAttrVal = newAttributeValues.get(attributeId).get(indx);
							if (!currAttributeValues.contains(newAttrVal))
								break;
						}
						if (indx < 0) {
							rList.add(i);
							continue;
						}
						else {
							smt.getAttributeValue().getContent().set(0, newAttrVal);
							currAttributeValues.add(newAttrVal);
						}
					}
				}
				// remove the extra attribute values
				if (rList.size() > 0) {
					Collections.sort(rList, Collections.reverseOrder());
					for (int j = 0; j < rList.size(); j++)
						stList.remove(rList.get(j).intValue());
				}
			}
		}
	}

	/**
	 * Print the statistical information of this policy on the standard output
	 * 
	 * @throws Exception
	 */
	public void printPolicysetStat() throws Exception {
		printPolicysetStat(System.out);
	}

	/**
	 * Print the statistical information of this policy
	 * 
	 * @param out
	 * @throws Exception
	 */
	public void printPolicysetStat(PrintStream out) throws Exception {
		out.println("Number of policy sets: " + String.valueOf(getNumberOfPolicysets()));
		out.println("Number of policies: " + String.valueOf(getNumberOfPolicies()));
		out.println("Number of rules: " + String.valueOf(getNumberOfRules()));
		// print the stat of attributes
		AttributeMapper mapper = AttributeMapper.getInstance();
		mapper.clearAll();
		PolicySetTranslator pst = new PolicySetTranslator(policyset);
		pst.parse();
		out.println("Number of attribute types: " + String.valueOf(mapper.getNumberOfAttributeTypes()));
		out.println("Number of attribute values: " + String.valueOf(mapper.getNumberOfAttributeValues()));

	}

	/**
	 * @return the policyset
	 */
	public PolicySetType getPolicyset() {
		return policyset;
	}

	/**
	 * @param policyset
	 *            the policyset to set
	 */
	public void setPolicyset(PolicySetType policyset) {
		this.policyset = policyset;
	}
}
