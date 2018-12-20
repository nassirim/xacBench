/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unsw.cse.xacml.translator.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ConditionType;

/**
 *
 * @author shayan_310
 */
public class ConditionMapper {
    private static ConditionMapper instance = null;
    private Map<String, ConditionType> conditionsMap;

	protected ConditionMapper() {
		conditionsMap = new HashMap<>();
	}

	public static ConditionMapper getInstance() {
		if(instance == null) {
			instance = new ConditionMapper();
		}
		return instance;
	}
        
        public void clearAll() {
		conditionsMap = new HashMap<>();
	}
        
        public void addCondition(String id, ConditionType cnd) {
		if (cnd!=null) {
			conditionsMap.put(id, cnd);
		}

	}
        
        public List<ConditionType> getListOfConditions() {
            List<ConditionType> cndList = new ArrayList<>(conditionsMap.values());            
            return cndList;
        }

	public void printNumberOfConditions()
        {
            System.out.printf("Number of Conditions in entire policy set: %s%n", getListOfConditions().size());
	}
        
        public int getNumberOfConditions()
        {
            List<ConditionType> cndList = getListOfConditions();            
            for(ConditionType cnd : cndList)
                if(cnd==null)
                    cndList.remove(cnd);
            
            return cndList.size();
                    
        }
}
