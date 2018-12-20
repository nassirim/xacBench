/**
 * 
 */
package edu.unsw.cse.xacml.experiment.v_3_0;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.translator.v_3_0.PolicySetTranslator;
import edu.unsw.cse.xacml.util.XACMLUtil;
import edu.unsw.cse.xacml.util.XACMLUtilV3;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import org.xml.sax.SAXException;

/**
 * @author Mohsen
 *
 */
public class XACML3PolicyGenerator {
    
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, Exception{
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter the full path to input policyset (.xml file):");
        String xacmlFilePath = sc.next();

        System.out.println("Please enter the number of generated rules in output policyset:");
        int ruleNumber = sc.nextInt();
        
        PolicySetType policysetObj = XACMLUtilV3.unmarshalPolicySetType(xacmlFilePath);
        //assertNotNull(policysetObj);

        int start = 0;
        for (int i = 0; i < xacmlFilePath.length(); i++) {
            if (xacmlFilePath.charAt(i) == '/') // Looking for '(' position in string
            {
                start = i;
            }
        }
        
        System.out.println("*** Input policyset informations ***");
        System.out.println("Input file name: " + xacmlFilePath.substring(start + 1));
        XACML3Analyzer xacmlAnalyzer_inp = new XACML3Analyzer(policysetObj);
        xacmlAnalyzer_inp.printPolicysetStat();
        
        System.out.println();
        System.out.println("Please wait! xacBench is generating synthetic policyset... .");

        String xacmlFileName = "Synthetic_" + String.valueOf(ruleNumber) + "_" + xacmlFilePath.substring(start + 1);

        PolSetGen generator = new PolSetGen(policysetObj, ruleNumber, xacmlFileName);
        PolicySetType generatedPolicyset = generator.syntheticPolicySetGenerator();

        System.out.println("-------------------------------------------");
        System.out.println("Synthetic policyset Successfully generated!");
        System.out.println("-------------------------------------------");

        System.out.println("*** Output policyset informations ***");
        System.out.println("Output file name: " + xacmlFileName);
        XACML3Analyzer xacmlAnalyzer_out = new XACML3Analyzer(generatedPolicyset);
        xacmlAnalyzer_out.printPolicysetStat();
    }

	private String baseXACMLFileName;
	private PolicySetType policyset;
	// private String outputXACMLFileName;

	/**
	 * @param baseXACMLFileName
	 */
	public XACML3PolicyGenerator(String baseXACMLFileName) {
		this.baseXACMLFileName = baseXACMLFileName;
		policyset = null;
		initPolicy();
	}

	/**
	 * Read the XACML file and initialize the policy set object.
	 */
	private void initPolicy() {
		try {
			policyset = XACMLUtilV3.unmarshalPolicySetType(baseXACMLFileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			policyset = null;
		}
	}

	public void generateXACMLPolicy(String outputFileName, int numberOfRules) {
		try {
			// we first make the attribute map which will be used to squeeze the
			// attributes in the new policy
			PolicySetTranslator pst = new PolicySetTranslator(policyset);
			pst.parse();
			XACML3Analyzer analyzer = new XACML3Analyzer(policyset);
			int basesRuleNo = analyzer.getNumberOfRules();
			if (numberOfRules < basesRuleNo) {
				removeRulesRandomly(numberOfRules);
				squeezeAttributes(numberOfRules, basesRuleNo);
			}
			XACMLUtilV3 xacml3Util = new XACMLUtilV3();
			FileOutputStream out = new FileOutputStream(outputFileName);
			xacml3Util.savePolicy(policyset, out/* System.out */);
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
		XACML3Analyzer analyzer = new XACML3Analyzer(policyset);
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
		XACML3Analyzer analyzer = new XACML3Analyzer(policyset);
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
			List<JAXBElement<?>> objs = policyset.getPolicySetOrPolicyOrPolicySetIdReference();
			int n = objs.size();
			for (int i = 0; i < numberOfRep; i++) {
				for (int j = 0; j < n; j++) {
					objs.add(objs.get(j));
				}
			}
			XACMLUtilV3 xacml3Util = new XACMLUtilV3();
			FileOutputStream out = new FileOutputStream(outputFileName);
			xacml3Util.savePolicy(policyset, out/* System.out */);
			// we read the file again in order to rebuild all element object in
			// the policy set
			policyset = XACMLUtilV3.unmarshalPolicySetType(outputFileName);
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
