/**
 * 
 */
package edu.unsw.cse.xacml.experiment.v_3_0;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.unsw.cse.xacml.translator.share.XACMLToASP;
import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.translator.util.PolicyElementIdentifierUtil;
import edu.unsw.cse.xacml.translator.v_3_0.XACML3ToASP;
import edu.unsw.cse.xacml.util.XACMLUtil;
import edu.unsw.cse.xacml.util.XACMLUtilV3;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;

/**
 * @author Mohsen
 *
 */
public class XACML3Experiments {

	public static String ROOT_DATASETS_FOLDER = "datasets";
	public static String XACML_DB_FOLDER = "xacml3_db";
	public static String BASE_XACML_FILE_NAME = "base_xacml3_match_expr.xml";
	public static String EXPR_XACML_FILE_NAME = "xacml3.xml";
	public static String EXPR_XACML_STAT_FILE_NAME = "stat.txt";

	public String originalXACMLFileName;
	private String baseXACMLFileName;
	public int numberOfIterations;
	public List<Integer> ruleNoList;

	/**
	 * Constructor.
	 */
	public XACML3Experiments(String originalXACMLFileName) {
		// TODO Auto-generated constructor stub
		this.originalXACMLFileName = originalXACMLFileName;
		numberOfIterations = 100;
		ruleNoList = new ArrayList<Integer>();
		for (int i = 100; i <= 1000; i += 100)
			ruleNoList.add(i);
		baseXACMLFileName = ROOT_DATASETS_FOLDER + "/" + XACML_DB_FOLDER + "/" + BASE_XACML_FILE_NAME;
	}

	public void generateXACMLPolicies() throws Exception {
		createBasePolicy();
		for (int i = 0; i < ruleNoList.size(); i++) {
			int ruleNo = ruleNoList.get(i);
			System.out.println("Rule number : " + String.valueOf(ruleNo));
			for (int iter = 1; iter <= numberOfIterations; iter++) {
				XACML3PolicyGenerator policyGenerator = new XACML3PolicyGenerator(baseXACMLFileName);
				AttributeMapper mapper = AttributeMapper.getInstance();
				mapper.clearAll();
				PolicyElementIdentifierUtil.clearAll();
				String xacmlFolderName = ROOT_DATASETS_FOLDER + "/" + XACML_DB_FOLDER + "/" + String.valueOf(ruleNo)
						+ "/" + String.valueOf(iter);
				// generate the dataset folder
				File f = new File(xacmlFolderName);
				if (!f.isDirectory())
					f.mkdirs();
				// generate the XACML policy file and its stat
				String xacmlFN = xacmlFolderName + "/" + EXPR_XACML_FILE_NAME;
				String statFN = xacmlFolderName + "/" + EXPR_XACML_STAT_FILE_NAME;
				policyGenerator.generateXACMLPolicy(xacmlFN, ruleNo);
				XACML3Analyzer xacmlAnalyzer = new XACML3Analyzer(policyGenerator.getPolicyset());
				xacmlAnalyzer.printPolicysetStat(new PrintStream(new File(statFN)));
				// convert the XACML policy into ASP programs
				XACML3ToASP xacml3ToASP = new XACML3ToASP();
				xacml3ToASP.translateToASP(xacmlFN, xacmlFolderName + "/" + "policy_v3.asp");
				xacml3ToASP.generateAllRequestProgram(xacmlFolderName + "/" + "generate_all.asp");
				xacml3ToASP.generateMatchProgram(xacmlFolderName);
				// update the ASP programs for various properties
				xacml3ToASP.generatePropertyProgram(xacmlFolderName, XACMLToASP.ASP_ALL_PROPERTY_FILE);
				xacml3ToASP.generatePropertyProgram(xacmlFolderName, XACMLToASP.ASP_ALL_RULES_TOTAL_REDUNDANCY_FILE);
				xacml3ToASP.generatePropertyProgram(xacmlFolderName, XACMLToASP.ASP_COMPLETENESS_FILE);
				xacml3ToASP.generatePropertyProgram(xacmlFolderName, XACMLToASP.ASP_INTER_POLICY_ANOMALY_FILE);
				xacml3ToASP.generatePropertyProgram(xacmlFolderName, XACMLToASP.ASP_INTRA_POLICY_ANOMALY_FILE);
				xacml3ToASP.generatePropertyProgram(xacmlFolderName, XACMLToASP.ASP_REFINEMENT_FILE);
				xacml3ToASP.generatePropertyProgram(xacmlFolderName, XACMLToASP.ASP_EFFECTIVENESS_COMPLETENESS_FILE);
				xacml3ToASP.generatePropertyProgram(xacmlFolderName, XACMLToASP.ASP_EFFECTIVENESS_ISOMORPHISM_FILE);
				xacml3ToASP.generatePropertyProgram(xacmlFolderName, XACMLToASP.ASP_EFFECTIVENESS_POLICY_FILE);
				xacml3ToASP.generatePropertyProgram(xacmlFolderName, XACMLToASP.ASP_EFFECTIVENESS_POLICYSET_FILE);
				xacml3ToASP.generatePropertyProgram(xacmlFolderName, XACMLToASP.ASP_EFFECTIVENESS_RULE_FILE);
				// generate simple and complex requests
				for (int j = 1; j <= numberOfIterations; j++) {
					String reqFN = xacmlFolderName + "/" + "request_" + String.valueOf(j) + ".asp";
					xacml3ToASP.generateSampleRequestProgram(reqFN, 1);
					String scenarioFN = xacmlFolderName + "/" + "scenario_" + String.valueOf(j) + ".asp";
					xacml3ToASP.generateScenarioProgram(scenarioFN);
				}
			}
		}
	}

	public void generateUsingBaseXACML(String xacmlBaseFileName, String outputFolderName) throws Exception {
		XACML3PolicyGenerator policyGenerator = new XACML3PolicyGenerator(xacmlBaseFileName);
		AttributeMapper mapper = AttributeMapper.getInstance();
		mapper.clearAll();
		PolicyElementIdentifierUtil.clearAll();
		// generate the output folder
		File f = new File(outputFolderName);
		if (!f.isDirectory())
			f.mkdirs();
		// generate the XACML policy file and its stat
		String statFN = outputFolderName + "/" + EXPR_XACML_STAT_FILE_NAME;
		XACML3Analyzer xacmlAnalyzer = new XACML3Analyzer(policyGenerator.getPolicyset());
		xacmlAnalyzer.printPolicysetStat(new PrintStream(new File(statFN)));
		// convert the XACML policy into ASP programs
		XACML3ToASP xacml3ToASP = new XACML3ToASP();
		xacml3ToASP.translateToASP(xacmlBaseFileName, outputFolderName + "/" + "policy_v3.asp");
		xacml3ToASP.generateAllRequestProgram(outputFolderName + "/" + "generate_all.asp");
		xacml3ToASP.generateMatchProgram(outputFolderName);
		// update the ASP programs for various properties
		xacml3ToASP.generatePropertyProgram(outputFolderName, XACMLToASP.ASP_ALL_PROPERTY_FILE);
		xacml3ToASP.generatePropertyProgram(outputFolderName, XACMLToASP.ASP_ALL_RULES_TOTAL_REDUNDANCY_FILE);
		xacml3ToASP.generatePropertyProgram(outputFolderName, XACMLToASP.ASP_COMPLETENESS_FILE);
		xacml3ToASP.generatePropertyProgram(outputFolderName, XACMLToASP.ASP_INTER_POLICY_ANOMALY_FILE);
		xacml3ToASP.generatePropertyProgram(outputFolderName, XACMLToASP.ASP_INTRA_POLICY_ANOMALY_FILE);
		xacml3ToASP.generatePropertyProgram(outputFolderName, XACMLToASP.ASP_REFINEMENT_FILE);
		xacml3ToASP.generatePropertyProgram(outputFolderName, XACMLToASP.ASP_EFFECTIVENESS_COMPLETENESS_FILE);
		xacml3ToASP.generatePropertyProgram(outputFolderName, XACMLToASP.ASP_EFFECTIVENESS_ISOMORPHISM_FILE);
		xacml3ToASP.generatePropertyProgram(outputFolderName, XACMLToASP.ASP_EFFECTIVENESS_POLICY_FILE);
		xacml3ToASP.generatePropertyProgram(outputFolderName, XACMLToASP.ASP_EFFECTIVENESS_POLICYSET_FILE);
		xacml3ToASP.generatePropertyProgram(outputFolderName, XACMLToASP.ASP_EFFECTIVENESS_RULE_FILE);
		// generate simple and complex requests
		for (int j = 1; j <= numberOfIterations; j++) {
			String reqFN = outputFolderName + "/" + "request_" + String.valueOf(j) + ".asp";
			xacml3ToASP.generateSampleRequestProgram(reqFN, 1);
			String scenarioFN = outputFolderName + "/" + "scenario_" + String.valueOf(j) + ".asp";
			xacml3ToASP.generateScenarioProgram(scenarioFN);
		}
	}

	public void generateForFolder(String xacmlSourceFolder, String outputFolderName) throws Exception {
		// TODO Auto-generated method stub
		File[] xacmlFileList = XACMLUtil.getXMLFileList(xacmlSourceFolder);
		for (int i = 0; i < xacmlFileList.length; i++) {
			String FileName = xacmlFileList[i].getName().replaceFirst("[.][^.]+$", "");
			String fullFileName = xacmlFileList[i].getPath();
			generateUsingBaseXACML(fullFileName, outputFolderName + "/" + FileName);
		}
	}

	/**
	 * This function generate the base XACMl policy which must be in the size of
	 * the double of the maximum rule numbers in the experiments. We use the
	 * result of this function for generating the XACMl policies used in the
	 * experiments.
	 * 
	 * @throws Exception
	 */
	private void createBasePolicy() throws Exception {
		// TODO Auto-generated method stub
		File f = new File(ROOT_DATASETS_FOLDER + "/" + XACML_DB_FOLDER);
		if (!f.isDirectory())
			f.mkdirs();
		baseXACMLFileName = ROOT_DATASETS_FOLDER + "/" + XACML_DB_FOLDER + "/" + BASE_XACML_FILE_NAME;
		PolicySetType policyset = XACMLUtilV3.unmarshalPolicySetType(originalXACMLFileName);
		XACML3Analyzer analyzer = new XACML3Analyzer(policyset);
		int n = analyzer.getNumberOfRules();
		int maxRulesExpr = Collections.max(ruleNoList);
		int copiesNo;
		if (n < 2 * maxRulesExpr) {
			copiesNo = Math.floorDiv(2 * maxRulesExpr, n);
		} else {
			copiesNo = 0;
		}
		XACML3PolicyGenerator policyGenerator = new XACML3PolicyGenerator(originalXACMLFileName);
		policyGenerator.duplicateXACMLPolicy(baseXACMLFileName, copiesNo);
		String statFN = ROOT_DATASETS_FOLDER + "/" + XACML_DB_FOLDER + "/" + "stat.txt";
		XACML3Analyzer xacmlAnalyzer = new XACML3Analyzer(policyGenerator.getPolicyset());
		xacmlAnalyzer.printPolicysetStat(new PrintStream(new File(statFN)));
	}

	public void generateStatForFolder(String xacmlSourceFolder, String outputFolderName) throws Exception {
		// TODO Auto-generated method stub
		File[] xacmlFileList = XACMLUtil.getXMLFileList(xacmlSourceFolder);
		for (int i = 0; i < xacmlFileList.length; i++) {
			String FileName = xacmlFileList[i].getName().replaceFirst("[.][^.]+$", "");
			String fullFileName = xacmlFileList[i].getPath();
			AttributeMapper mapper = AttributeMapper.getInstance();
			mapper.clearAll();
			PolicyElementIdentifierUtil.clearAll();
			// generate the output folder
			File f = new File(outputFolderName);
			if (!f.isDirectory())
				f.mkdirs();
			// generate the XACML policy file and its stat
			String statFN = outputFolderName + "/stat_v3_" + FileName + ".txt";
			PolicySetType policyset = XACMLUtilV3.unmarshalPolicySetType(fullFileName);
			XACML3Analyzer xacmlAnalyzer = new XACML3Analyzer(policyset);
			xacmlAnalyzer.printPolicysetStat(new PrintStream(new File(statFN)));
		}
	}

}
