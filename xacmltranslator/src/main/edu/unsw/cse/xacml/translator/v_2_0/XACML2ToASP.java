/**
 * 
 */
package edu.unsw.cse.xacml.translator.v_2_0;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import edu.unsw.cse.xacml.profiles._2_0_.policy.PolicySetType;
import edu.unsw.cse.xacml.translator.share.XACMLToASP;
import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.translator.util.PolicyElementIdentifierUtil;
import edu.unsw.cse.xacml.util.XACMLUtilV2;

/**
 * @author Mohsen
 *
 */
public class XACML2ToASP extends XACMLToASP {

	/**
	 * Constructor.
	 */
	public XACML2ToASP() {
		super();
	}

	/**
	 * Read the XACML program and translate it into an ASP program.
	 * 
	 * @param xacmlFileName
	 * @param aspFileName
	 */
	public void translateToASP(String xacmlFileName, String aspFileName) {
		AttributeMapper mapper = AttributeMapper.getInstance();
		try {
			PolicySetType policySet = XACMLUtilV2.unmarshalPolicySetType(xacmlFileName);
			PolicySetTranslator pst = new PolicySetTranslator(policySet);
			// set the root policy set id/name
			int policysetId = PolicyElementIdentifierUtil.ROOT_POLICYSET_IDENTIFIER;
			String containerId = PolicyElementIdentifierUtil.POLICYSET_NAME_PREFIX + String.valueOf(policysetId);
			String aspProgStr = pst.translateToASP(containerId, policysetId);
			PrintWriter out = new PrintWriter(aspFileName);
			initialASPProgram(out);
			out.print(aspProgStr);
			out.print("\n\n\n");
			out.close();
			// System.out.println(aspProgStr);
			// System.out.println(pst);
//			mapper.printAttributes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method generates the match programs, the common program and the one
	 * specified for XACML version 2.
	 * 
	 * @param outputFolder
	 */
	public void generateMatchProgram(String outputFolder) {
		// TODO Auto-generated method stub
		super.generateMatchProgram(outputFolder);
		generateMatchV2Program(outputFolder);
	}

	/**
	 * Generate the match for XACML version 2
	 * 
	 * @param outputFolder
	 */
	private void generateMatchV2Program(String outputFolder) {
		AttributeMapper mapper = AttributeMapper.getInstance();
		String reqParamVarStr = mapper.getVariableAttributes();
		List<String> attrTypeIds = mapper.getAttributeTypeList();
		int n = mapper.getNumberOfAttributeTypes();
		try {
			PrintWriter out = new PrintWriter(outputFolder + "/" + ASP_MATCH_V2_FILE);
			Scanner scan = new Scanner(new File(ASP_MATCH_INPUT_FOLDER + "/" + ASP_MATCH_V2_FILE));
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				if (line.contains("ActReq, EnvReq, ResReq, SubReq)"))
					line = line.replace("ActReq, EnvReq, ResReq, SubReq", reqParamVarStr);
				if (line.contains("match_actions(TN, ActReq)") || line.contains("match_environments(TN, EnvReq)")
						|| line.contains("match_resources(TN, ResReq)"))
					continue;
				if (line.contains("match_subjects(TN, SubReq)")) {
					for (int i = 0; i < n; i++) {
						String str;
						str = "\t\t\t\t\t";
						str += "match_" + attrTypeIds.get(i) + "(TN, " + PolicyElementIdentifierUtil.ATTRIBUTE_VARIABLE_NAME_PREFIX + String.valueOf(i + 1) + ")";
						if (i < n - 1)
							str += ",";
						else
							str += ".";
						out.println(str);
					}
					break;
				}
				out.println(line);
			}
			scan.close();
			// add the matches for each attribute type
			for (int i = 0; i < n; i++) {
				String str, theVarStr;
				theVarStr = PolicyElementIdentifierUtil.ATTRIBUTE_VARIABLE_NAME_PREFIX + String.valueOf(i + 1);
				str = "match_" + attrTypeIds.get(i) + "(TN, " + theVarStr + ")";
				str += " :- \n";
				str += "\t\t\t\t\t";
				str += "request(";
				for (int j = 0; j < n; j++) {
					if (j > 0)
						str += ", ";
					if (j == i)
						str += theVarStr;
					else
						str += "_";
				}
				str += "),\n\t\t\t\t\t";
				out.print(str + attrTypeIds.get(i) + "s(TN).\n");
				out.print(str + attrTypeIds.get(i) + "s(TN, " + theVarStr + ").\n");
			}
			out.println();
			out.println();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
