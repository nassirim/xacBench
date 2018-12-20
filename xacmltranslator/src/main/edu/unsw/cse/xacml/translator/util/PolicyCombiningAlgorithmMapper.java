/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unsw.cse.xacml.translator.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shayan310
 */
public class PolicyCombiningAlgorithmMapper {
    
    private static PolicyCombiningAlgorithmMapper instance = null;

	/**
	 * Collect all policy set identifiers for each policy combining algorithm value : List<policyCombiningAlgo, List<policyset_id>>
	 */
	private Map<String, List<String>> policyCombAlgoValues;

	protected PolicyCombiningAlgorithmMapper() {
		policyCombAlgoValues = new HashMap<>();
	}

	public static PolicyCombiningAlgorithmMapper getInstance() {
		if(instance == null) {
			instance = new PolicyCombiningAlgorithmMapper();
		}
		return instance;
	}
        
        public void clearAll() {
		policyCombAlgoValues = new HashMap<>();
	}
        
        /**
	 * Add the policy combining algorithm and its value to the map
	 * @param policyCombiningAlgorithm
	 * @param policysetId
	 */
        public void addPolicyCombiningAlgorithmValue(String policyCombiningAlgorithm, String policysetId) {
		List<String> strList;
		// add the policy combining algorithm into the list
		if (!policyCombAlgoValues.containsKey(policyCombiningAlgorithm)) {
			strList = new ArrayList<String>();
			policyCombAlgoValues.put(policyCombiningAlgorithm, strList);
		}
		// add the policy set id value into the list
		strList = policyCombAlgoValues.get(policyCombiningAlgorithm);
		if (strList == null || policysetId == null || policysetId == "") {
			return;
		}
		if (!strList.contains(policysetId))
			strList.add(policysetId);
	}
        
        /**
	 * Return the number of policy combining algorithms in the program
	 * @return
	 */
	public int getNumberOfPolicyCombiningAlgorithms() {
		return policyCombAlgoValues.keySet().size();
	}
        
        public List getListOfPolicyCombiningAlgorithms() {
            List<String> lst = new ArrayList<>(policyCombAlgoValues.keySet());
            return lst;
        }

	/**
	 * Return the number of policy sets use a policy combining algorithm
	 */
	public void printNumberOfPolicysetsForEachPolicyCombiningAlgorithm()
        {

            System.out.println();
            System.out.println("#Number of policysets that use a policy combining algorithm");
            
            Iterator<String> it = policyCombAlgoValues.keySet().iterator();
		while (it.hasNext()) {
			String pca = it.next();
			System.out.printf("%s\t%s%n", pca, policyCombAlgoValues.get(pca).size());
		}
	}
        
}
