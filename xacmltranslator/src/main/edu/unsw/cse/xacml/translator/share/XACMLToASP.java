/**
 * 
 */
package edu.unsw.cse.xacml.translator.share;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.translator.util.PolicyElementIdentifierUtil;
import edu.unsw.cse.xacml.util.XACMLUtil;

/**
 * @author Mohsen
 *
 */
public class XACMLToASP {

	public static String ASP_MATCH_INPUT_FOLDER = "src/test/resources/aspcodes/";
	public static String ASP_MATCH_COMMON_FILE = "match_common.asp";
	public static String ASP_MATCH_V3_FILE = "match_target_v3.asp";
	public static String ASP_MATCH_V2_FILE = "match_target_v2.asp";
//	public static String ASP_SCENARIO_FILE = "scenario_finding.asp";
	public static String ASP_ALL_PROPERTY_FILE = "all_properties.asp";
	public static String ASP_ALL_RULES_TOTAL_REDUNDANCY_FILE = "all_rules_total_redundancy.asp";
	public static String ASP_COMPLETENESS_FILE = "completeness.asp";
	public static String ASP_INTER_POLICY_ANOMALY_FILE = "inter_policy_anomalies.asp";
	public static String ASP_INTRA_POLICY_ANOMALY_FILE = "intra_policy_anomalies.asp";
	public static String ASP_REFINEMENT_FILE = "refinement.asp";

	public static String ASP_EFFECTIVENESS_COMPLETENESS_FILE = "effectiveness_completeness.asp";
	public static String ASP_EFFECTIVENESS_ISOMORPHISM_FILE = "effectiveness_isomorphism.asp";
	public static String ASP_EFFECTIVENESS_POLICY_FILE = "effectiveness_policy.asp";
	public static String ASP_EFFECTIVENESS_POLICYSET_FILE = "effectiveness_policyset.asp";
	public static String ASP_EFFECTIVENESS_RULE_FILE = "effectiveness_rule.asp";
	
	

	/**
	 * Constructor.
	 */
	public XACMLToASP() {
	}

	protected void initialASPProgram(PrintWriter out) {
		// TODO Auto-generated method stub
		out.print("effect(permit;deny;indeterminate).\n");
		out.print("comb_alg(deny_overrides;permit_overrides;first_applicable;only_one_applicable).\n");
		out.print("comb_alg(do;po;fa;ooa).\n");
		out.print("bool_expr(true).\n");
		out.print("\n");
	}

	public void generateAllRequestProgram(String aspFileName) {
		try {
			PrintWriter out = new PrintWriter(aspFileName);
			out.println();
			String domainRuleStr, headAllRule, bodyAllRule, varStr;
			headAllRule = "request(";
			bodyAllRule = "";
			int k = 0;
			// generate rules which define the domain of each attribute type.
			// also generate the head and body of the main rule for generating
			// all requests.
			AttributeMapper mapper = AttributeMapper.getInstance();
			List<String> attrTypeIds = mapper.getAttributeTypeList();
			Iterator<String> it = attrTypeIds.iterator();
			while (it.hasNext()) {
				k++;
				if (k > 1) {
					headAllRule += ", ";
					bodyAllRule += ", ";
				}
				varStr = PolicyElementIdentifierUtil.ATTRIBUTE_VARIABLE_NAME_PREFIX + String.valueOf(k);
				headAllRule += varStr;
				String attrId = it.next();
				domainRuleStr = attrId + "_domain";
				bodyAllRule += domainRuleStr + "(" + varStr + ")";
				domainRuleStr += "(";
				// comma separated list of values for this attribute type
				List<String> strList;
				boolean flag = true;
				// System.out.println(attrId);
				strList = mapper.getAttributeValues(attrId);
				for (String strVal : strList) {
					if (strVal == null)
						continue;
					if (!flag)
						domainRuleStr += "; ";
					else
						flag = false;
					domainRuleStr += strVal;
				}
				domainRuleStr += ").";
				out.println(domainRuleStr);
			}
			out.println();
			headAllRule += ")";
			// all the rule for generating all requests
			out.println(headAllRule + " :- " + bodyAllRule + ".");
			out.println();
			out.println();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Generate sample requests from the attributes domains
	 * 
	 * @param aspFileName
	 * @param num
	 */
	public void generateSampleRequestProgram(String aspFileName, int num) {
		// TODO Auto-generated method stub
		AttributeMapper mapper = AttributeMapper.getInstance();
		try {
			PrintWriter out = new PrintWriter(aspFileName);
			out.println();
			for (int j = 0; j < num; j++) {
				String reqStr;
				reqStr = "request(";
				int k = 0, i;
				// generate rules which define the domain of each attribute
				// type.
				// also generate the head and body of the main rule for
				// generating all requests.
				List<String> attrTypeIds = mapper.getAttributeTypeList();
				Iterator<String> it = attrTypeIds.iterator();
				while (it.hasNext()) {
					k++;
					if (k > 1) {
						reqStr += ", ";
					}
					String attrId = it.next();
					// randomly select one value for this attribute type
					List<String> strList;
					strList = mapper.getAttributeValues(attrId);
					i = XACMLUtil.getRandomNumber(0, strList.size());
					reqStr += strList.get(i);
				}
				reqStr += ").";
				// all requests must be commented except the first one
				if (j != 0)
					reqStr = "% " + reqStr;
				out.println(reqStr);
			}
			out.println();
			out.println();
			out.println("%#show no_match_anyof_req/" + String.valueOf(mapper.getNumberOfAttributeTypes() + 1) + ".");
			out.println("%#show match_rule/" + String.valueOf(mapper.getNumberOfAttributeTypes() + 4) + ".");
			out.println("%#show match_policy/" + String.valueOf(mapper.getNumberOfAttributeTypes() + 5) + ".");
			out.println("%#show match_policyset_alg/" + String.valueOf(mapper.getNumberOfAttributeTypes() + 5) + ".");
			out.println("%#show match_policyset/" + String.valueOf(mapper.getNumberOfAttributeTypes() + 6) + ".");
			out.println("%#show match_target/" + String.valueOf(mapper.getNumberOfAttributeTypes() + 1) + ".");
			out.println("#show match_program/" + String.valueOf(mapper.getNumberOfAttributeTypes() + 4) + ".");
			out.println();
			out.println();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This function updates the common match program written in ASP. We need to
	 * replace the lines including a request object.
	 * 
	 * @param outputFolder
	 */
	protected void generateMatchProgram(String outputFolder) {
		AttributeMapper mapper = AttributeMapper.getInstance();
		String reqParamVarStr = mapper.getVariableAttributes();
		List<String> attrTypeIds = mapper.getAttributeTypeList();
		try {
			PrintWriter out = new PrintWriter(outputFolder + "/" + ASP_MATCH_COMMON_FILE);
			Scanner scan = new Scanner(new File(ASP_MATCH_INPUT_FOLDER + "/" + ASP_MATCH_COMMON_FILE));
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				if (line.contains("ActReq, EnvReq, ResReq, SubReq)"))
					line = line.replace("ActReq, EnvReq, ResReq, SubReq", reqParamVarStr);
				if (line.contains("X == SubReq")) {
					int indx = attrTypeIds.indexOf(PolicyElementIdentifierUtil.XACML_V2_SUBJECT_ATTR_NAME);
					if (indx >= 0) {
						line = line.replace("SubReq", PolicyElementIdentifierUtil.ATTRIBUTE_VARIABLE_NAME_PREFIX + String.valueOf(indx + 1));
					}
				}
				if (line.contains("Y == ResReq")) {
					int indx = attrTypeIds.indexOf(PolicyElementIdentifierUtil.XACML_V2_RESOURCE_ATTR_NAME);
					if (indx >= 0) {
						line = line.replace("ResReq", PolicyElementIdentifierUtil.ATTRIBUTE_VARIABLE_NAME_PREFIX + String.valueOf(indx + 1));
					}
				}
				out.println(line);
			}
			scan.close();
			out.println();
			out.println();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This function updates the property program written in ASP. We need to
	 * replace the lines including a request object.
	 * 
	 * @param outputFolder
	 */
	public void generatePropertyProgram(String outputFolder, String inputASPProgFileName) {
		AttributeMapper mapper = AttributeMapper.getInstance();
		String reqParamVarStr = mapper.getVariableAttributes();
		String reqParamUnderStr = mapper.getUnderlineAttributes();
		try {
			PrintWriter out = new PrintWriter(outputFolder + "/" + inputASPProgFileName);
			Scanner scan = new Scanner(new File(ASP_MATCH_INPUT_FOLDER + "/" + inputASPProgFileName));
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				if (line.contains("ActReq, EnvReq, ResReq, SubReq"))
					line = line.replace("ActReq, EnvReq, ResReq, SubReq", reqParamVarStr);
				if (line.contains("incomplete(_, _, _, _)"))
					line = line.replace("_, _, _, _", reqParamUnderStr);
				if (line.contains("no_refine_program(PSN1, PSN2, _, _, _, _, _, _)"))
					line = line.replace("_, _, _, _, _, _", "_, _, " + reqParamUnderStr);
				if (line.contains("incomplete/4"))
					line = line.replace("incomplete/4", "incomplete/" + String.valueOf(mapper.getNumberOfAttributeTypes()));
				if (line.contains("incomplete_evidence/4"))
					line = line.replace("incomplete_evidence/4", "incomplete_evidence/" + String.valueOf(mapper.getNumberOfAttributeTypes()));
				if (line.contains("no_refine_program/8"))
					line = line.replace("no_refine_program/8", "no_refine_program/" + String.valueOf(mapper.getNumberOfAttributeTypes() + 4));
				if (line.contains("no_refine_program_evidence/8"))
					line = line.replace("no_refine_program_evidence/8", "no_refine_program_evidence/" + String.valueOf(mapper.getNumberOfAttributeTypes() + 4));
				out.println(line);
			}
			scan.close();
			out.println();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Randomly generate a complex scenario program.
	 * @param aspOutputFileName
	 */
	public void generateScenarioProgram(String aspOutputFileName) {
		// TODO Auto-generated method stub
		AttributeMapper mapper = AttributeMapper.getInstance();
		try {
			PrintWriter out = new PrintWriter(aspOutputFileName);
			out.println();
			// add the domains
			generateAttributesDomains(out);
			String domainRuleStr, headRule, bodyLine, varStr;
			headRule = "request(" + mapper.getVariableAttributes() + ") :- \n";
			out.print(headRule);
			int attrNo = mapper.getNumberOfAttributeTypes();
			// generate specific attributes randomly
			// we will have at least one wildcard attribute value
			int specificArrNo = XACMLUtil.getRandomNumber(1, attrNo);
			ArrayList<Integer> specificIndexes = new ArrayList<Integer>();
			int i = 0;
			while (i < specificArrNo) {
				int p = XACMLUtil.getRandomNumber(0, attrNo);
				if (!specificIndexes.contains(p)) {
					specificIndexes.add(p);
					i++;
				}
			}
			List<String> attrTypeIds = mapper.getAttributeTypeList();
			Iterator<String> it = attrTypeIds.iterator();
			int k = 0;
			// generate a complex request
			bodyLine = "";
			while (it.hasNext()) {
				String attrId = it.next();
				if (k > 0)
					bodyLine += ",\n";
				varStr = PolicyElementIdentifierUtil.ATTRIBUTE_VARIABLE_NAME_PREFIX + String.valueOf(k + 1);
				bodyLine += "\t\t\t\t\t"; 
				if (!specificIndexes.contains(k)) {
					domainRuleStr = attrId + "_domain";
					bodyLine += domainRuleStr + "(" + varStr + ")";
				}
				else {
					// get a random value for this attribute type
					List<String> strList;
					strList = mapper.getAttributeValues(attrId);
					int j = XACMLUtil.getRandomNumber(0, strList.size());
					bodyLine += varStr + " == " + strList.get(j);
				}
				k++;
			}
			bodyLine += ".\n\n";
			out.print(bodyLine);
			// add the scenario term in the program
			headRule = "scenarios(" + mapper.getVariableAttributes() + ") :- \n";
			out.print(headRule);
			bodyLine = "\t\t\t\t\t";
			bodyLine += "request(" + mapper.getVariableAttributes() + "),\n";
			out.print(bodyLine);
			bodyLine = "\t\t\t\t\t";
			bodyLine += "match_program(_, _, _, ";
			if (XACMLUtil.getRandomNumber(0, 2) == 0)
				bodyLine += "deny";
			else
				bodyLine += "permit";
			bodyLine += ", " + mapper.getVariableAttributes() + ").\n\n";
			out.print(bodyLine);
			out.print("#show scenarios/" + String.valueOf(attrNo) + ".\n\n\n");
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * It appends the domains of the attribute types into the ASP program.
	 * @param out
	 */
	private void generateAttributesDomains(PrintWriter out) {
		String domainRuleStr;
		AttributeMapper mapper = AttributeMapper.getInstance();
		List<String> attrTypeIds = mapper.getAttributeTypeList();
		Iterator<String> it = attrTypeIds.iterator();
		while (it.hasNext()) {
			String attrId = it.next();
			domainRuleStr = attrId + "_domain";
			domainRuleStr += "(";
			// comma separated list of values for this attribute type
			List<String> strList;
			boolean flag = true;
			// System.out.println(attrId);
			strList = mapper.getAttributeValues(attrId);
			for (String strVal : strList) {
				if (strVal == null)
					continue;
				if (!flag)
					domainRuleStr += "; ";
				else
					flag = false;
				domainRuleStr += strVal;
			}
			domainRuleStr += ").";
			out.println(domainRuleStr);
		}
		out.println();
	}

}
