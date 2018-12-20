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
public class ruleEffectMapper {
    private static ruleEffectMapper instance = null;

	private Map<String, String> ruleEffectValues;

	protected ruleEffectMapper() {
		ruleEffectValues = new HashMap<>();
	}

	public static ruleEffectMapper getInstance() {
		if(instance == null) {
			instance = new ruleEffectMapper();
		}
		return instance;
	}
        
        public void clearAll() {
		ruleEffectValues = new HashMap<>();
	}
        
        /**
	 * Add the rule combining algorithm and its value to the map
	 * @param ruleId
	 * @param ruleEffect
	 */
        public void addRuleEffectValue(String ruleId, String ruleEffect) {

            
		if (!ruleEffectValues.containsKey(ruleId)) {
			ruleEffectValues.put(ruleId, ruleEffect);
		}
		// add the policy id value into the list
		if (ruleEffect == null || ruleId == null || ruleId == "") {
			return;
		}

	}
        public List getPercentageOfEachEffect()
        {
            int p=0;
            Iterator<String> it = ruleEffectValues.keySet().iterator();
            while (it.hasNext())
            {
                if(ruleEffectValues.get(it.next()).equals("permit"))
                    p++;
            }
                        
            double pPrc=(double)p/ruleEffectValues.size();
            double dPrc = 1.00-pPrc;
            
            List<Double> pAndD = new ArrayList<>(2);
            pAndD.add(pPrc);
            pAndD.add(dPrc);
            
            return pAndD;
        }
        
        /**
	 * Return the number of rule combining algorithms in the program
	 * @return
	 */
	public void printPercentageOfPermitAndDenyEffect()
        {
            List prc = getPercentageOfEachEffect();

            System.out.println("#Percentage of each rule effect");            
            System.out.printf("Permit\t%s%%%n",((double) (prc.get(0)))*100);
            System.out.printf("Deny\t%s%%%n",((double) (prc.get(1)))*100);
            
        }
         
}
