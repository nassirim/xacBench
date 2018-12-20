/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unsw.cse.xacml.translator.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shayan310
 */
public class RuleCombiningAlgorithmMapper {

    private static RuleCombiningAlgorithmMapper instance = null;

	/**
	 * Collect all policy identifiers for each rule combining algorithm value : List<policyCombiningAlgo, List<policyset_id>>
	 */
	private Map<String, List<String>> ruleCombAlgoValues;

	protected RuleCombiningAlgorithmMapper() {
		ruleCombAlgoValues = new HashMap<>();
	}

	public static RuleCombiningAlgorithmMapper getInstance() {
		if(instance == null) {
			instance = new RuleCombiningAlgorithmMapper();
		}
		return instance;
	}
        
        public void clearAll() {
		ruleCombAlgoValues = new HashMap<>();
	}
        
        /**
	 * Add the rule combining algorithm and its value to the map
	 * @param ruleCombiningAlgorithm
	 * @param policyId
	 */
        public void addRuleCombiningAlgorithmValue(String ruleCombiningAlgorithm, String policyId) {
		List<String> strList;
		// add the rule combining algorithm into the list
		if (!ruleCombAlgoValues.containsKey(ruleCombiningAlgorithm)) {
			strList = new ArrayList<String>();
			ruleCombAlgoValues.put(ruleCombiningAlgorithm, strList);
		}
		// add the policy id value into the list
		strList = ruleCombAlgoValues.get(ruleCombiningAlgorithm);
		if (strList == null || policyId == null || policyId == "") {
			return;
		}
		if (!strList.contains(policyId))
			strList.add(policyId);
	}
        
        /**
	 * Return the number of rule combining algorithms in the program
	 * @return
	 */
	public int getNumberOfRuleCombiningAlgorithms() {
		return ruleCombAlgoValues.keySet().size();
	}
        
        public List getListOfRuleCombiningAlgorithms() {
            List<String> lst = new ArrayList<>(ruleCombAlgoValues.keySet());
            return lst;
        }

	/**
	 * Return the number of policies associated with each rule combining algorithm
	 */
	public void printNumberOfPoliciesForEachRuleCombiningAlgorithm() 
        {
            System.out.println();
            System.out.println("#Number of policies that use a rule combining algorithm");
        
            Iterator<String> it = ruleCombAlgoValues.keySet().iterator();
		while (it.hasNext()) 
                {
			String rca = it.next();
			System.out.printf("%s\t%s%n", rca, ruleCombAlgoValues.get(rca).size());
		}
	}
            
}
