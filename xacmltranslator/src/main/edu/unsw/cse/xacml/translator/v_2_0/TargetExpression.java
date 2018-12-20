
package edu.unsw.cse.xacml.translator.v_2_0;

import edu.unsw.cse.xacml.profiles._2_0_.policy.ActionMatchType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.ActionType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.AttributeValueType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.EnvironmentMatchType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.EnvironmentType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.ResourceMatchType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.ResourceType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.SubjectMatchType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.SubjectType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.TargetType;

import java.util.List;

import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.translator.util.MatchIdConverterUtil;
import edu.unsw.cse.xacml.translator.util.PolicyElementIdentifierUtil;
import edu.unsw.cse.xacml.translator.util.XACMLTranslatingException;
import edu.unsw.cse.xacml.translator.util.MatchIdConverterUtil.OperatorType;

/**
 * TargetExpression: contains list of match expressions combined by conjunctive
 * operator
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
	 * Parse this object to collect its elements.
	 * 
	 * @throws XACMLTranslatingException
	 */
	public void parse() throws XACMLTranslatingException {
		if (targetObj == null) {
			throw new XACMLTranslatingException("null target object.");
		}
		if ((targetObj.getActions() == null || targetObj.getActions().getAction() == null) &&
				(targetObj.getEnvironments() == null || targetObj.getEnvironments().getEnvironment() == null) &&
				(targetObj.getResources() == null || targetObj.getResources().getResource() == null) &&
				(targetObj.getSubjects() == null || targetObj.getSubjects().getSubject() == null)) {
			return;
		}
		String containerId = "1";
		translateActionsToASP(containerId);
		translateEnvironmentsToASP(containerId);
		translateResourcesToASP(containerId);
		translateSubjectsToASP(containerId);
	}

	/**
	 * Translate this object into an ASP program.
	 * 
	 * @throws XACMLTranslatingException
	 */
	public String translateToASP(String containerId) throws XACMLTranslatingException {
		if (targetObj == null) {
			return translateEmptyTarget(containerId);
		}
		if ((targetObj.getActions() == null || targetObj.getActions().getAction() == null) &&
				(targetObj.getEnvironments() == null || targetObj.getEnvironments().getEnvironment() == null) &&
				(targetObj.getResources() == null || targetObj.getResources().getResource() == null) &&
				(targetObj.getSubjects() == null || targetObj.getSubjects().getSubject() == null)) {
			return translateEmptyTarget(containerId);
		}
		String aspProgStr = "";
		aspProgStr += translateActionsToASP(containerId);
		aspProgStr += translateEnvironmentsToASP(containerId);
		aspProgStr += translateResourcesToASP(containerId);
		aspProgStr += translateSubjectsToASP(containerId);
		return aspProgStr;
	}

	/**
	 * Translate the actions of this target into an ASP program.
	 * 
	 * @throws XACMLTranslatingException
	 */
	private String translateActionsToASP(String containerId) throws XACMLTranslatingException {
		if (targetObj == null) {
			return "";
		}
		AttributeMapper mapper = AttributeMapper.getInstance();
		// extract the list of action values accepted by this target
		String aspProg = "";
		if (targetObj.getActions() != null && targetObj.getActions().getAction() != null) {
			for (ActionType a : targetObj.getActions().getAction()) {
				List<ActionMatchType> atList = a.getActionMatch();
				if (atList != null) {
					for (ActionMatchType amt : atList) {
						String attrVal = getAttributeValue(amt.getAttributeValue());
						OperatorType operator = MatchIdConverterUtil.getOperator(amt.getMatchId());
						switch (operator) {
						case LESS_THAN:
						case LESS_THAN_OR_EQUAL:
						case GREATER_THAN:
						case GREATER_THAN_OR_EQUAL:
							break;
						case EQUAL:
						}
						if (operator != OperatorType.EQUAL)
							continue;
						mapper.addAttributeValue(PolicyElementIdentifierUtil.XACML_V2_ACTION_ATTR_NAME, attrVal);
						aspProg += "actions(" + containerId + ", " + attrVal + ").\n";
					}
				}
			}
		}
		if (aspProg.equals(""))
			aspProg += "actions(" + containerId + ").\n";
		return aspProg;
	}

	/**
	 * Translate the environments of this target into an ASP program.
	 * 
	 * @throws XACMLTranslatingException
	 */
	private String translateEnvironmentsToASP(String containerId) throws XACMLTranslatingException {
		if (targetObj == null) {
			return "";
		}
		AttributeMapper mapper = AttributeMapper.getInstance();
		// extract the list of environments values accepted by this target
		String aspProg = "";
		if (targetObj.getEnvironments() != null && targetObj.getEnvironments().getEnvironment() != null) {
			for (EnvironmentType a : targetObj.getEnvironments().getEnvironment()) {
				List<EnvironmentMatchType> atList = a.getEnvironmentMatch();
				if (atList != null) {
					for (EnvironmentMatchType amt : atList) {
						String attrVal = getAttributeValue(amt.getAttributeValue());
						OperatorType operator = MatchIdConverterUtil.getOperator(amt.getMatchId());
						switch (operator) {
						case LESS_THAN:
						case LESS_THAN_OR_EQUAL:
						case GREATER_THAN:
						case GREATER_THAN_OR_EQUAL:
							break;
						case EQUAL:
						}
						if (operator != OperatorType.EQUAL)
							continue;
						mapper.addAttributeValue(PolicyElementIdentifierUtil.XACML_V2_ENVIRONMENT_ATTR_NAME, attrVal);
						aspProg += "environments(" + containerId + ", " + attrVal + ").\n";
					}
				}
			}
		}
		if (aspProg.equals(""))
			aspProg += "environments(" + containerId + ").\n";
		return aspProg;
	}
	
	/**
	 * Translate the resources of this target into an ASP program.
	 * 
	 * @throws XACMLTranslatingException
	 */
	private String translateResourcesToASP(String containerId) throws XACMLTranslatingException {
		if (targetObj == null) {
			return "";
		}
		AttributeMapper mapper = AttributeMapper.getInstance();
		// extract the list of resources values accepted by this target
		String aspProg = "";
		if (targetObj.getResources() != null && targetObj.getResources().getResource() != null) {
			for (ResourceType a : targetObj.getResources().getResource()) {
				List<ResourceMatchType> atList = a.getResourceMatch();
				if (atList != null) {
					for (ResourceMatchType amt : atList) {
						String attrVal = getAttributeValue(amt.getAttributeValue());
						OperatorType operator = MatchIdConverterUtil.getOperator(amt.getMatchId());
						switch (operator) {
						case LESS_THAN:
						case LESS_THAN_OR_EQUAL:
						case GREATER_THAN:
						case GREATER_THAN_OR_EQUAL:
							break;
						case EQUAL:
						}
						if (operator != OperatorType.EQUAL)
							continue;
						mapper.addAttributeValue(PolicyElementIdentifierUtil.XACML_V2_RESOURCE_ATTR_NAME, attrVal);
						aspProg += "resources(" + containerId + ", " + attrVal + ").\n";
					}
				}
			}
		}
		if (aspProg.equals(""))
			aspProg += "resources(" + containerId + ").\n";
		return aspProg;
	}
	
	/**
	 * Translate the subjects of this target into an ASP program.
	 * 
	 * @throws XACMLTranslatingException
	 */
	private String translateSubjectsToASP(String containerId) throws XACMLTranslatingException {
		if (targetObj == null) {
			return "";
		}
		AttributeMapper mapper = AttributeMapper.getInstance();
		// extract the list of subjects values accepted by this target
		String aspProg = "";
		if (targetObj.getSubjects() != null && targetObj.getSubjects().getSubject() != null) {
			for (SubjectType a : targetObj.getSubjects().getSubject()) {
				List<SubjectMatchType> atList = a.getSubjectMatch();
				if (atList != null) {
					for (SubjectMatchType amt : atList) {
						String attrVal = getAttributeValue(amt.getAttributeValue());
						OperatorType operator = MatchIdConverterUtil.getOperator(amt.getMatchId());
						switch (operator) {
						case LESS_THAN:
						case LESS_THAN_OR_EQUAL:
						case GREATER_THAN:
						case GREATER_THAN_OR_EQUAL:
							break;
						case EQUAL:
						}
						if (operator != OperatorType.EQUAL)
							continue;
						mapper.addAttributeValue(PolicyElementIdentifierUtil.XACML_V2_SUBJECT_ATTR_NAME, attrVal);
						aspProg += "subjects(" + containerId + ", " + attrVal + ").\n";
					}
				}
			}
		}
		if (aspProg.equals(""))
			aspProg += "subjects(" + containerId + ").\n";
		return aspProg;
	}

	/**
	 * Translate an empty target object which accepts every request. A sample
	 * rule: target(r1).
	 * 
	 * @param containerId
	 * @return
	 */
	private String translateEmptyTarget(String containerId) {
		// TODO Auto-generated method stub
		String aspProgStr = "";
		aspProgStr += "target(" + containerId + ").";
		return aspProgStr;
	}

	public static String getAttributeValue(AttributeValueType attributeValue) throws XACMLTranslatingException {
		List<Object> objects = attributeValue.getContent();
		if (objects == null || objects.size() == 0) {
			throw new XACMLTranslatingException("Cannot extract attribute value");
		}
		String strValue = (String) objects.get(0);
		strValue = strValue.toLowerCase();
		strValue = strValue.replace('-', '_');
		strValue = strValue.replace('@', '_');
		strValue = strValue.replace('.', '_');
		// Comparable value = DataTypeConverterUtil.convert(strValue,
		// attributeValue.getDataType());
		return strValue;
	}

}
