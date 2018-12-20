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
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceExpressionsType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ConditionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationExpressionsType;

/**
 *
 * @author shayan_310
 */
public class AdviceMapper {
    private static AdviceMapper instance = null;
    private Map<String, AdviceExpressionsType> adviceMap;

	protected AdviceMapper() {
		adviceMap = new HashMap<>();
	}

	public static AdviceMapper getInstance() {
		if(instance == null) {
			instance = new AdviceMapper();
		}
		return instance;
	}
        
        public void clearAll() {
		adviceMap = new HashMap<>();
	}
        
        public void addAdvice(String id, AdviceExpressionsType adv) {
		if (adv!=null) {
			adviceMap.put(id, adv);
		}

	}
        
        public List getListOfAdvices() {
            List<AdviceExpressionsType> advList = new ArrayList<>(adviceMap.values());            
            return advList;
        }

	public void printNumberOfAdvices()
        {
            System.out.printf("Number of Advice Expressions in entire policy set: %s%n", getListOfAdvices().size());
	}

        public int getNumberOfAdvices()
        {
            int k = 0;
            List<AdviceExpressionsType> advsList = getListOfAdvices();            
            for(AdviceExpressionsType advs : advsList)
                for(AdviceExpressionType adv : advs.getAdviceExpression())
                    if(adv.getAttributeAssignmentExpression()!=null)
                    {
                        k++;
                        break;
                    }
            
            return k;
                    
        }
        
}
