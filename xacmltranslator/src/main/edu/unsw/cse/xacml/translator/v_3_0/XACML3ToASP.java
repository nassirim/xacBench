/**
 * 
 */
package edu.unsw.cse.xacml.translator.v_3_0;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import edu.unsw.cse.xacml.translator.share.XACMLToASP;
import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.translator.util.PolicyElementIdentifierUtil;
import edu.unsw.cse.xacml.util.XACMLUtilV3;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;

/**
 * @author Mohsen
 *
 */
public class XACML3ToASP extends XACMLToASP {

	/**
	 * Constructor.
	 */
	public XACML3ToASP() {
		super();
	}

	/**
	 * Read the XACML program and translate it into an ASP program.
	 * 
	 * @param xacmlFileName
	 * @param aspFileName
	 */
	public void translateToASP(String xacmlFileName, String aspFileName) {
//		AttributeMapper mapper = AttributeMapper.getInstance();
		try {
			PolicySetType policySet = XACMLUtilV3.unmarshalPolicySetType(xacmlFileName);
			PolicySetTranslator pst = new PolicySetTranslator(policySet);
			// set the root policy set id/name
			int policysetId = PolicyElementIdentifierUtil.ROOT_POLICYSET_IDENTIFIER;
			String containerId = PolicyElementIdentifierUtil.POLICYSET_NAME_PREFIX + String.valueOf(policysetId);
			pst.parse();
			String aspProgStr = pst.translateToASP(containerId, policysetId);
			PrintWriter out = new PrintWriter(aspFileName);
			initialASPProgram(out);
			out.print(aspProgStr);
			out.print("\n\n\n");
			out.close();
			// System.out.println(aspProgStr);
			// System.out.println(pst);
			// mapper.printAttributes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method generates the match programs, the common program and the one
	 * specified for XACML version 3.
	 * 
	 * @param outputFolder
	 */
	public void generateMatchProgram(String outputFolder) {
		// TODO Auto-generated method stub
		super.generateMatchProgram(outputFolder);
		generateMatchV3Program(outputFolder);
	}
	
	/**
	 * Generate the match for XACML version 3
	 * @param outputFolder
	 */
	private void generateMatchV3Program(String outputFolder) {
		AttributeMapper mapper = AttributeMapper.getInstance();
		String reqParamVarStr = mapper.getVariableAttributes();
		String reqParamUnderStr = mapper.getUnderlineAttributes();
		try {
			PrintWriter out = new PrintWriter(outputFolder + "/" + ASP_MATCH_V3_FILE);
			Scanner scan = new Scanner(new File(ASP_MATCH_INPUT_FOLDER + "/" + ASP_MATCH_V3_FILE));
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				if (line.contains("ActReq, EnvReq, ResReq, SubReq)"))
					line = line.replace("ActReq, EnvReq, ResReq, SubReq", reqParamVarStr);
				if (line.contains("anyof") && line.contains("_, _, _, _)"))
					line = line.replace("_, _, _, _", reqParamUnderStr);
				if (line.contains("ActAnyof, EnvAnyof, ResAnyof, SubAnyof)")) {
					line = line.replace("ActAnyof, EnvAnyof, ResAnyof, SubAnyof", reqParamVarStr.replace('V', 'Z'));
				}
				if (line.contains("@match_str_func(ActReq, ActAnyof, any) == 1")
						|| line.contains("@match_str_func(EnvReq, EnvAnyof, any) == 1")
						|| line.contains("@match_str_func(ResReq, ResAnyof, any) == 1"))
					continue;
				if (line.contains("@match_str_func(SubReq, SubAnyof, any) == 1")) {
					int n = mapper.getNumberOfAttributeTypes();
					for (int i = 0; i < n; i++) {
						String str;
						str = "\t\t\t\t\t@match_str_func(" + "V" + String.valueOf(i + 1) + ", Z" + String.valueOf(i + 1)
								+ ", any) == 1";
						if (i < n - 1)
							str += ",";
						else
							str += ".";
						out.println(str);
					}
					continue;
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
}
