/**
 * 
 */
package edu.unsw.cse.xacml.translator.v_3_0;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.translator.util.AttributeMapperV3;
import edu.unsw.cse.xacml.translator.util.MatchIdConverterUtil;
import edu.unsw.cse.xacml.translator.util.XACMLTranslatingException;
import edu.unsw.cse.xacml.translator.util.MatchIdConverterUtil.OperatorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AllOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeDesignatorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.MatchType;

/**
 * @author Mohsen
 *
 */
public class AllOfExpression {

	private AllOfType allOf = null;
	
	public static final String WILDCARD_STRING = "any"; 
	
	//	private static final Logger log = LoggerFactory.getLogger(AllOfExpression.class);

	/**
	 * List of <attribute_id, attribute_value>
	 */
	private Map<String, String> matchList;


	public AllOfExpression(AllOfType allOf) {
		this.allOf = allOf;
		matchList = new HashMap<String, String>();
	}

	/**
	 * Parse an AllOf object
	 * @throws XACMLTranslatingException
	 */
	public void parse() throws XACMLTranslatingException {
		if (allOf == null) {
			throw new XACMLTranslatingException("allOf element must not be null");
		}
		List<MatchType> lstMatches = allOf.getMatch();
		matchList.clear();
		for (MatchType match : lstMatches) {
			// validating the match expression
			validate(match);

			String attributeId = match.getAttributeDesignator().getAttributeId();
			//String attributeId = getAttributeId(varId);
			String attributeValue = getAttributeValue(match.getAttributeValue());
			//			boolean isMustBePresent = match.getAttributeDesignator().isMustBePresent();

			// discard the invalid values/ids
			if (attributeId == null || attributeValue == null || attributeId.equals("") || attributeValue.equals(""))
				continue;
			// we ignore all operators but equality
			OperatorType operator = MatchIdConverterUtil.getOperator(match
					.getMatchId());
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
			// add the attribute id/value into the mapper
			AttributeMapper mapper = AttributeMapper.getInstance();
			mapper.addAttributeValue(attributeId, attributeValue);
                        
                        AttributeMapperV3 mapperV3 = AttributeMapperV3.getInstance();
                        mapperV3.addAttributeValue(attributeId, match.getAttributeValue());
                        
			matchList.put(attributeId, attributeValue);
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
		parse();
		String aspRuleStr = "";
		aspRuleStr += "anyof(" + containerId + ", " + anyOfId;
		// add the attribute values for this object by considering wildcard values
		AttributeMapper mapper = AttributeMapper.getInstance();
		List<String> attrTypeIds = mapper.getAttributeTypeList();
		for (String attrTypeId : attrTypeIds) {
			// do something
			if (matchList.containsKey(attrTypeId))
				aspRuleStr += ", " + matchList.get(attrTypeId).toLowerCase();
			else
				aspRuleStr += ", " + WILDCARD_STRING;
		}
		aspRuleStr += ").";
		return aspRuleStr;
	}

	/**
	 * @return List of variables with their equivalent intervals.
	 */
	public Map<String, String> getMatchList() {
		return this.matchList;
	}

	/**
	 * This function returns an identifier for an attribute.
	 * @param origAttributeId
	 * @return
	 * @throws XACMLTranslatingException
	 */
	public static String getAttributeId(String origAttributeId)
			throws XACMLTranslatingException {
		if (origAttributeId == null || origAttributeId.length() == 0) {
			throw new XACMLTranslatingException("Cannot extract attribute identifier");
		}
		String newAttributeId = origAttributeId;
		if (origAttributeId.toLowerCase().contains("resource"))
			newAttributeId = "resource";
		else if (origAttributeId.toLowerCase().contains("action"))
			newAttributeId = "action";
		else if (origAttributeId.toLowerCase().contains("subject"))
			newAttributeId = "subject";
		else if (origAttributeId.toLowerCase().contains("role"))
			newAttributeId = "role";
		/*		if (origAttributeId.toLowerCase().contains("resource:resource-id") || origAttributeId.toLowerCase().contains("resource/resource-id"))
			newAttributeId = "resource";
		else if (origAttributeId.toLowerCase().contains("action:action-id") || origAttributeId.toLowerCase().contains("action/action-id"))
			newAttributeId = "action";
		else if (origAttributeId.toLowerCase().contains("subject:subject-id") || origAttributeId.toLowerCase().contains("subject/subject-id"))
			newAttributeId = "subject";*/
		newAttributeId = newAttributeId.toLowerCase();
		newAttributeId = newAttributeId.replace('-', '_');
		return newAttributeId;
	}

	public static String getAttributeValue(AttributeValueType attributeValue)
			throws XACMLTranslatingException {
		List<Object> objects = attributeValue.getContent();
		if (objects == null || objects.size() == 0) {
			throw new XACMLTranslatingException("Cannot extract attribute value");
		}

		// only support 1-value at the moment.

		String strValue = String.valueOf(objects.get(0));
		strValue = strValue.toLowerCase();
		strValue = strValue.replace('-', '_');
		strValue = strValue.replace('.', '_');
		//        Comparable value = DataTypeConverterUtil.convert(strValue, attributeValue.getDataType());
		return strValue;
	}

	private void validate(MatchType match) throws XACMLTranslatingException {
		AttributeValueType attrValue = match.getAttributeValue();
		AttributeDesignatorType attrDesignator = match.getAttributeDesignator();
		if (null == attrValue) {
			throw new XACMLTranslatingException("No attribute value found");
		}
		if (null == attrDesignator) {
			throw new XACMLTranslatingException("No attribute designator found");
		}

		String attrValueDataType = attrValue.getDataType();
		if (attrValueDataType == null
				|| !attrValueDataType.equals(attrDesignator.getDataType())) {
			throw new XACMLTranslatingException(
					"Data types in match expression do not valid");
		}
	}

	@Override
	public String toString() {
		return "allOf=" + matchList;
	}
}
