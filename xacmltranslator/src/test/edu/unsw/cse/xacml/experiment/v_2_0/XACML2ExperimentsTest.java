/**
 * 
 */
package edu.unsw.cse.xacml.experiment.v_2_0;

import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Mohsen
 *
 */
public class XACML2ExperimentsTest {
	
	@Ignore
	@Test
	public void testXACML3MatchExpr() throws Exception {
		String xacmlFileName;
		xacmlFileName = "src/test/resources/xacml-v2/xacml2-sample-policy.xml";
		xacmlFileName = "src/test/resources/xacml-v2/continue-a.xml";
//		xacmlFileName = "src/test/resources/xacml-v2/continue-b.xml";

		System.out.println("Generating the dataset ...");
		XACML2Experiments.ROOT_DATASETS_FOLDER = "d:/datasets";
		XACML2Experiments exprObj = new XACML2Experiments(xacmlFileName);
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
		String xacmlFileName;
		char[] charArray ={ 'a', 'b'};
		System.out.println("Generating the dataset ...");
		for (int i = 0; i < charArray.length;i++) {
			xacmlFileName = "src/test/resources/xacml-v2/continue-" + String.valueOf(charArray[i]) + ".xml";
			XACML2Experiments exprObj = new XACML2Experiments(xacmlFileName);
			exprObj.numberOfIterations = 100;
			String outputFolderName = "d:/datasets/realworld/" + "continue-" + String.valueOf(charArray[i]) + "-xacml2";
			exprObj.generateUsingBaseXACML(xacmlFileName, outputFolderName);
		}
		System.out.println("Done.");
	}

	@Test
	public void createReadlworldHongxinHuExpr() throws Exception {
		String xacmlSourceFolder, outputFolderName;
		XACML2Experiments exprObj;
		System.out.println("Generating the dataset ...");

		xacmlSourceFolder = "src/test/resources/real-world policies/Hongxin Hu, XACMLv2";
		exprObj = new XACML2Experiments("");
		exprObj.numberOfIterations = 100;
		outputFolderName = "d:/datasets/realworld/HongxinHu_XACMLv2";
		exprObj.generateForFolder(xacmlSourceFolder, outputFolderName);

		System.out.println("Done.");
	}

	@Ignore
	@Test
	public void createExprForFolder() throws Exception {
		String xacmlSourceFolder, outputFolderName;
		XACML2Experiments exprObj;
		System.out.println("Generating the dataset ...");

		xacmlSourceFolder = "src/test/resources/real-world policies/xacml version 2";
		exprObj = new XACML2Experiments("");
		exprObj.numberOfIterations = 100;
		outputFolderName = "d:/datasets/realworld/xacml2";
		exprObj.generateForFolder(xacmlSourceFolder, outputFolderName);

		System.out.println("Done.");
	}

	@Ignore
	@Test
	public void createRealworldStat() throws Exception {
		String xacmlSourceFolder, outputFolderName;
		XACML2Experiments exprObj;
		System.out.println("Generating the dataset ...");

		xacmlSourceFolder = "src/test/resources/real-world policies/xacml version 2";
		exprObj = new XACML2Experiments("");
		outputFolderName = "d:/datasets/realworld/xacml2/stats";
		exprObj.generateStatForFolder(xacmlSourceFolder, outputFolderName);

		System.out.println("Done.");
	}
}
