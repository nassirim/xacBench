/**
 * 
 */
package edu.unsw.cse.xacml.experiment.v_3_0;

import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Test;

import edu.unsw.cse.xacml.experiment.v_3_0.XACML3Experiments;

/**
 * @author Mohsen
 *
 */
public class XACML3ExperimentsTest {
	
	@Ignore
	@Test
	public void testXACML3MatchExpr() throws Exception {
		String xacmlFileName;
//		xacmlFileName = "src/test/resources/xacml3-policyset-sli.xml";
//		xacmlFileName = "src/test/resources/xacml3-AllOfExpressionTest2.xml";
		xacmlFileName = "src/test/resources/continue-a-xacml3.xml";

		System.out.println("Generating the dataset ...");
		XACML3Experiments.ROOT_DATASETS_FOLDER = "d:/datasets";
		XACML3Experiments exprObj = new XACML3Experiments(xacmlFileName);
		exprObj.numberOfIterations = 100;
		exprObj.ruleNoList = new ArrayList<Integer>();
		for (int i = 100; i <= 1000; i += 100)
			exprObj.ruleNoList.add(i);
		exprObj.generateXACMLPolicies();
		System.out.println("Done.");
	}
	
	@Ignore
	@Test
	public void createReadlworldExpr() throws Exception {
		String xacmlFileName, outputFolderName;
		XACML3Experiments exprObj;
		System.out.println("Generating the dataset ...");

		xacmlFileName = "src/test/resources/continue-a-xacml3.xml";
		xacmlFileName = "src/test/resources/real-world policies/xacml version 3/web-image-filter-policy.xml";
		exprObj = new XACML3Experiments(xacmlFileName);
		exprObj.numberOfIterations = 100;
		outputFolderName = "d:/datasets/realworld/" + "continue-a-xacml3";
		exprObj.generateUsingBaseXACML(xacmlFileName, outputFolderName);

		System.out.println("Done.");
	}

	@Test
	public void createExprForFolder() throws Exception {
		String xacmlSourceFolder, outputFolderName;
		XACML3Experiments exprObj;
		System.out.println("Generating the dataset ...");

		xacmlSourceFolder = "src/test/resources/real-world policies/xacml version 3";
		exprObj = new XACML3Experiments("");
		exprObj.numberOfIterations = 100;
		outputFolderName = "d:/datasets/realworld/xacml3";
		exprObj.generateForFolder(xacmlSourceFolder, outputFolderName);

		System.out.println("Done.");
	}

	@Ignore
	@Test
	public void createRealworldStat() throws Exception {
		String xacmlSourceFolder, outputFolderName;
		XACML3Experiments exprObj;
		System.out.println("Generating the dataset ...");

		xacmlSourceFolder = "src/test/resources/real-world policies/xacml version 3";
		exprObj = new XACML3Experiments("");
		outputFolderName = "d:/datasets/realworld/xacml3/stats";
		exprObj.generateStatForFolder(xacmlSourceFolder, outputFolderName);

		System.out.println("Done.");
	}
}
