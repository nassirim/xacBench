/**
 * 
 */
package edu.unsw.cse.xacml.experiment.v_2_0;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.unsw.cse.xacml.profiles._2_0_.policy.PolicySetType;
import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.translator.v_2_0.PolicySetTranslator;
import edu.unsw.cse.xacml.util.XACMLUtil;
import edu.unsw.cse.xacml.util.XACMLUtilV2;

/**
 * @author Mohsen
 *
 */
public class XACML2PolicyGenerator {

	private String baseXACMLFileName;
	private PolicySetType policyset;
	// private String outputXACMLFileName;

	/**
	 * @param baseXACMLFileName
	 */
	public XACML2PolicyGenerator(String baseXACMLFileName) {
		this.baseXACMLFileName = baseXACMLFileName;
		policyset = null;
		initPolicy();
	}

	/**
	 * Read the XACML file and initialize the policy set object.
	 */
	private void initPolicy() {
		try {
			policyset = XACMLUtilV2.unmarshalPolicySetType(baseXACMLFileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			policyset = null;
		}
	}

	/**
	 * Create a XACML policy with the number of rules and save it in a file.
	 * @param outputFileName
	 * @param numberOfRules
	 */
	public void generateXACMLPolicy(String outputFileName, int numberOfRules) {
		try {
			// we first make the attribute map which will be used to squeeze the
			// attributes in the new policy
			PolicySetTranslator pst = new PolicySetTranslator(policyset);
			pst.parse();
			XACML2Analyzer analyzer = new XACML2Analyzer(policyset);
			int basesRuleNo = analyzer.getNumberOfRules();
			if (numberOfRules < basesRuleNo) {
				removeRulesRandomly(numberOfRules);
				squeezeAttributes(numberOfRules, basesRuleNo);
			}
			XACMLUtilV2 xacml2Util = new XACMLUtilV2();
			FileOutputStream out = new FileOutputStream(outputFileName);
			xacml2Util.savePolicy(policyset, out/* System.out */);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Remove a number of rules from the policy in order to reduce the policy
	 * length.
	 * 
	 * @param numberOfRules
	 * @throws Exception
	 */
	private void removeRulesRandomly(int numberOfRules) throws Exception {
		// TODO Auto-generated method stub
		// we first make the attribute map which will be used to squeeze the attributes in the new policy
		XACML2Analyzer analyzer = new XACML2Analyzer(policyset);
		int baseRulesNo = analyzer.getNumberOfRules();
		if (numberOfRules >= baseRulesNo)
			return;
		ArrayList<Integer> removedIds = XACMLUtil.getUniqueRandomList(baseRulesNo - numberOfRules, 1, baseRulesNo + 1);
		analyzer.removeRule(removedIds);
		// remove all empty policies/policy sets
		analyzer.removeEmptyElements();
	}

	/**
	 * Squeeze the number of attribute types/values based on the number rules in
	 * the policy.
	 * 
	 * @param numberOfRules
	 * @param baseRuleNo
	 * @throws Exception
	 */
	private void squeezeAttributes(int numberOfRules, int baseRulesNo) throws Exception {
		// TODO Auto-generated method stub
		if (numberOfRules >= baseRulesNo)
			return;
		// select attribute types/values for the policy
		// note that the mapper in for the base policy
		AttributeMapper baseMapper = AttributeMapper.getInstance();
		int baseAttrTypeNo = baseMapper.getNumberOfAttributeTypes();
		int currAttrTypeNo;
		currAttrTypeNo = Math.max(3, Math.floorDiv(numberOfRules * baseAttrTypeNo, baseRulesNo));
		List<Integer> attrTypeIndexes = XACMLUtil.getUniqueRandomList(currAttrTypeNo, 0, baseAttrTypeNo);
		Map<String, List<String>> newAttributeValues = new HashMap<String, List<String>>();
		for (int indx : attrTypeIndexes) {
			List<String> strList;
			String attributeId;
			attributeId = baseMapper.getAttributeTypeList().get(indx);
			strList = new ArrayList<String>();
			newAttributeValues.put(attributeId, strList);
			// select the values for this attribute type
			List<String> baseAttrValues = baseMapper.getAttributeValues(attributeId);
			int baseAttrValuesNo = baseAttrValues.size();
			int currAttrValuesNo;
			currAttrValuesNo = Math.max(2, Math.floorDiv(numberOfRules * baseAttrValuesNo, baseRulesNo));
			if (currAttrValuesNo >= baseAttrValuesNo) {
				strList.addAll(baseAttrValues);
			}
			else {
				List<Integer> attrValIndexes = XACMLUtil.getUniqueRandomList(currAttrValuesNo, 0, baseAttrValuesNo);
				for (int k : attrValIndexes)
					strList.add(baseAttrValues.get(k));
			}
		}
		XACML2Analyzer analyzer = new XACML2Analyzer(policyset);
		analyzer.squeezeAttributes(newAttributeValues);
	}

	/**
	 * This function duplicate the policy within itself. Note that it does not
	 * close the element objects.
	 * 
	 * @param outputFileName
	 */
	public void duplicateXACMLPolicy(String outputFileName) {
		duplicateXACMLPolicy(outputFileName, 1);
	}

	/**
	 * This function duplicate the policy within itself. Note that it does not
	 * close the element objects.
	 * 
	 * @param outputFileName
	 * @param numberOfRep
	 */
	public void duplicateXACMLPolicy(String outputFileName, int numberOfRep) {
		// TODO Auto-generated method stub
		try {
			List<Object> objs = policyset.getPolicySetOrPolicyOrPolicySetIdReference();
			int n = objs.size();
			for (int i = 0; i < numberOfRep; i++) {
				for (int j = 0; j < n; j++) {
					objs.add(objs.get(j));
				}
			}
			XACMLUtilV2 xacml2Util = new XACMLUtilV2();
			FileOutputStream out = new FileOutputStream(outputFileName);
			xacml2Util.savePolicy(policyset, out/* System.out */);
			// we read the file again in order to rebuild all element object in
			// the policy set
			policyset = XACMLUtilV2.unmarshalPolicySetType(outputFileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
