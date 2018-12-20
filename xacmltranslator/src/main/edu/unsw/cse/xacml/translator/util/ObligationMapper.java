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
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationExpressionsType;

/**
 *
 * @author shayan_310
 */
public class ObligationMapper {
    private static ObligationMapper instance = null;
    private Map<String, ObligationExpressionsType> obligationsMap;

	protected ObligationMapper() {
		obligationsMap = new HashMap<>();
	}

	public static ObligationMapper getInstance() {
		if(instance == null) {
			instance = new ObligationMapper();
		}
		return instance;
	}
        
        public void clearAll() {
		obligationsMap = new HashMap<>();
	}
        
        public void addObligation(String id, ObligationExpressionsType obl) {
		if (obl!=null) {
			obligationsMap.put(id, obl);
		}

	}
        
        public List getListOfObligations() {
            List<ObligationExpressionsType> oblList = new ArrayList<>(obligationsMap.values());            
            return oblList;
        }

	public void printNumberOfObligationtions()
        {
            System.out.printf("Number of Obligation Expressions in entire policy set: %s%n", getListOfObligations().size());
	}
        
                public int getNumberOfObligationtions()
        {
            int k = 0;
            List<ObligationExpressionsType> oblsList = getListOfObligations();            
            for(ObligationExpressionsType advs : oblsList)
                for(ObligationExpressionType obl : advs.getObligationExpression())
                    if(obl.getAttributeAssignmentExpression()!=null)
                    {
                        k++;
                        break;
                    }
            
            return k;
                    
        }
}
