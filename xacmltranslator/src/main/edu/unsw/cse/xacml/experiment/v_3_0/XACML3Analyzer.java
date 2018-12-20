/**
 * 
 */
package edu.unsw.cse.xacml.experiment.v_3_0;

import edu.unsw.cse.xacml.profiles._2_0_.context.AttributeType;
import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.translator.util.AttributeMapperV3;
import edu.unsw.cse.xacml.translator.util.ConditionMapper;
import edu.unsw.cse.xacml.translator.util.MatchIdConverterUtil;
import edu.unsw.cse.xacml.translator.util.MatchIdConverterUtil.OperatorType;
import edu.unsw.cse.xacml.translator.util.ObligationMapper;
import edu.unsw.cse.xacml.translator.util.PolicyCombiningAlgorithmMapper;
import edu.unsw.cse.xacml.translator.util.RuleCombiningAlgorithmMapper;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import edu.unsw.cse.xacml.translator.util.XACMLTranslatingException;
import edu.unsw.cse.xacml.translator.util.ruleEffectMapper;
import edu.unsw.cse.xacml.translator.v_3_0.AllOfExpression;
import edu.unsw.cse.xacml.translator.v_3_0.PolicySetTranslator;
import edu.unsw.cse.xacml.util.XACMLUtil;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AllOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AnyOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeDesignatorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.MatchType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.RuleType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.TargetType;

/**
 * @author Mohsen
 *
 */
public class XACML3Analyzer {
	private PolicySetType policyset;

    //List of all anyOf expressions
    private List<AnyOfType> anyOfList= new ArrayList<>();    

    //Key: attributeID - Value: Number of attributeValues (duplicate values included)
    private Map<String, Integer> numberOfAttrId = new HashMap<>();     
    
    private Map<String, Integer> numberOfRulesInPolicy = new HashMap<>();
    
    private Map<String, Integer> numberOfPoliciesInPolicysets = new HashMap<>();
    
    private Map<String, Integer> numberOfPolicysetsInPolicysets = new HashMap<>();
    
    private List<Integer> anyOfInTarget = new ArrayList<>();
    
    private List<TargetType> totalTargets = new ArrayList<>();
    
    private Map<String, List<AnyOfType>> anyOfMap = new HashMap<>();
    
    //Attribute ID, List of attr values
    Map<String, List<AttributeValueType>> attrValues = new HashMap<>();
    /**
	 * Constructor.
	 *
	 * @param policyset
	 *            a XACML 3.0 policy element.
	 * @throws XACMLTranslatingException
	 */
	public XACML3Analyzer(PolicySetType policyset) throws Exception {
		if (policyset == null) {
			throw new IllegalArgumentException("PolicySetType argument must not be null");
		}
		this.policyset = policyset;
	}

	/**
	 * Returns the number of rules in this XACML policy
	 * 
	 * @return
	 */
	public int getNumberOfRules() {
		return getNumberOfRules(this.policyset);
	}

	/**
	 * Returns the number of rules for an XACML element
	 * 
	 * @param objValue
	 * @return
	 */
	private int getNumberOfRules(Object objValue) {
		// TODO Auto-generated method stub
		if (objValue == null)
			return 0;
		if (objValue instanceof RuleType)
			return 1;
		if (objValue instanceof PolicyType) {
			List<RuleType> rules = getRulesOfPolicy((PolicyType) objValue);
			return rules.size();
		}
		if (objValue instanceof PolicySetType) {
			List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
			int k = 0;
			for (Object obj : children) {
				k += getNumberOfRules(obj);
			}
			return k;
		}
		return 0;
	}

	/**
	 * Returns the number of policies in this XACML policy
	 * 
	 * @return
	 */
	public int getNumberOfPolicies() {
		return getNumberOfPolicies(this.policyset);
	}

	/**
	 * Returns the number of policies for an XACML element
	 * 
	 * @param objValue
	 * @return
	 */
	private int getNumberOfPolicies(Object objValue) {
		// TODO Auto-generated method stub
		if (objValue == null)
			return 0;
		if (objValue instanceof PolicyType) {
			return 1;
		}
		if (objValue instanceof PolicySetType) {
			List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
			int k = 0;
			for (Object obj : children) {
				k += getNumberOfPolicies(obj);
			}
			return k;
		}
		return 0;
	}

	/**
	 * Returns the number of policy sets in this XACML policy
	 * 
	 * @return
	 */
	public int getNumberOfPolicysets() {
		return getNumberOfPolicysets(this.policyset);
	}

	/**
	 * Returns the number of policy sets for an XACML element
	 * 
	 * @param objValue
	 * @return
	 */
	private int getNumberOfPolicysets(Object objValue) {
		// TODO Auto-generated method stub
		if (objValue == null)
			return 0;
		if (objValue instanceof PolicySetType) {
			List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
			int k = 1;
			for (Object obj : children) {
				k += getNumberOfPolicysets(obj);
			}
			return k;
		}
		return 0;
	}
        
        /******************* Codes written by Shayan Ahmadi *******************/
        /***************************** 1395/11/15 *****************************/

        /**
	 * Returns the mean number of rules in policies in this XACML policy
	 * 
	 * @return
	 */
        private int getMeanNumberOfRulesInPolicies()
        {
            if(getNumberOfPolicies() !=0 )
                return (getNumberOfRules()/getNumberOfPolicies());
            System.out.println("No Policies!");
            return 0;
        }
        
        /**
	 * Returns the mean number of policies in policy sets in this XACML policy
	 * 
	 * @return
	 */
        private int getMeanNumberOfPoliciesInPolicysets()
        {
            if(getNumberOfPolicysets() !=0)
                return (getNumberOfPolicies()/getNumberOfPolicysets());
            System.out.println("No PolicySets!");
            return 0;
        }
        
        /**
	 * Returns the number of AnyOf expressions in this XACML policy
	 * 
	 * @return
	 */
        private int getNumberOfAnyOf (Object objValue)
        {
            
                if (objValue == null)
			return 0;
		if (objValue instanceof RuleType)
			return ((RuleType) objValue).getTarget().getAnyOf().size();
		if (objValue instanceof PolicyType) {
			List<RuleType> rules = getRulesOfPolicy((PolicyType) objValue);
                        int k = ((PolicyType) objValue).getTarget().getAnyOf().size();
                        for (RuleType r : rules)
                            k+=getNumberOfAnyOf(r);
                        
                return k;
		}
		if (objValue instanceof PolicySetType) {
			List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
			int k = ((PolicySetType) objValue).getTarget().getAnyOf().size();
			for (Object obj : children) {
                            k+=getNumberOfAnyOf(obj);
                        }
			return k;
		}
		return 0;
        }
        
        /**
	 * Returns the number of AllOf expressions in this XACML policy
	 * 
	 * @return
	 */                
        private int getNumberOfAllOf (Object objValue)
        {
            
                if (objValue == null)
			return 0;
		if (objValue instanceof RuleType)
                {
                    List<AnyOfType> aot = ((RuleType) objValue).getTarget().getAnyOf();
                    int k=0;
                    for (AnyOfType a : aot)
                        k+=a.getAllOf().size();
                    
                    return k;
                }        
		if (objValue instanceof PolicyType) {
			List<RuleType> rules = getRulesOfPolicy((PolicyType) objValue);
                        int k=0;
                        for (RuleType r : rules)
                            k+=getNumberOfAllOf(r);
                        
                        List<AnyOfType> aot = ((PolicyType) objValue).getTarget().getAnyOf();
                        for (AnyOfType a : aot)
                            k+=a.getAllOf().size();
                        
                return k;		
                }
		if (objValue instanceof PolicySetType) {
			List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
			int k = 0;
			for (Object obj : children) {
                            k+=getNumberOfAllOf(obj);
                        }
                        
                        List<AnyOfType> aot = ((PolicySetType) objValue).getTarget().getAnyOf();
                        for (AnyOfType a : aot)
                            k+=a.getAllOf().size();
			return k;
		}
		return 0;
        }
        private void matchElementAnalyses ()
        {
            Map <String, List<AttributeValueType>> attrVals = new HashMap<String, List<AttributeValueType>>();
            /*attrVals=getAttributeValues();
            System.out.println("Number of Unique Attribute Values for each Attribute ID:");
            attrVals.entrySet().forEach((entry)->{
                System.out.printf("Attribute ID: %s \t Number of unique values: %s %n", entry.getKey(), entry.getValue().size());
            });       
            
            System.out.println();
            */
            System.out.println("Frequency of Each Attribute ID:");
            numberOfAttrId.entrySet().forEach((entry)->{
                System.out.printf("Attribute ID: %s \t Frequency: %s%n", entry.getKey(), entry.getValue());
            });  
            
            AttributeMapperV3 attrMprV3 = AttributeMapperV3.getInstance();
            //attrMpr.clearAll();
            attrVals=attrMprV3.getAttributeValuesMap();
            System.out.println("Number of Unique Attribute Values for each Attribute ID:");
            attrVals.entrySet().forEach((entry)->{
                System.out.printf("Attribute ID: %s \t Number of unique values: %s %n", entry.getKey(), entry.getValue().size());
            }); 
        }
        
        /***************************** 1395/12/11 *****************************/
        
	//Prints the number of rules in each policy
        public void printNumberOfRulesInPolicies ()
        {            
            System.out.println();
            System.out.println("#Number of rules in each policy");
            
            Map<String, Integer> rInP = getNumberOfRulesInPolicies();

            rInP.entrySet().forEach((entry)->{
            System.out.printf("%s\t%s%n", entry.getKey(),entry.getValue());
            });
        }

        public Map<String, Integer> getNumberOfRulesInPolicies ()
        {
            return getNumberOfRulesInPolicies(this.policyset);
        }
        
        /**
	 * Returns a Map containing policyID and number of rules in it
	 * 
	 * @param objValue
	 */
        private Map<String, Integer> getNumberOfRulesInPolicies (Object objValue)
        {
            if (objValue == null)
                return null;
            
            if(objValue instanceof PolicyType)
                {
                    PolicyType pol = (PolicyType) objValue;
                    List<RuleType> rules = getRulesOfPolicy(pol);
                    numberOfRulesInPolicy.put(pol.getPolicyId(), rules.size());

                }
            if (objValue instanceof PolicySetType)
                {
                    List<Object> polsetChildren = extractChildenOfPolicyset((PolicySetType) objValue);
                    for (Object obj : polsetChildren)
                        getNumberOfRulesInPolicies(obj);
                }
            return numberOfRulesInPolicy;
        }
        
	//Prints the number of policies in each policy set
        public void printNumberOfPoliciesInPolicyset ()
        {            
            System.out.println();
            System.out.println("#Number of policies in each policy set");
            
            printNumberOfPoliciesInPolicyset(this.policyset);
            
            numberOfPoliciesInPolicysets.entrySet().forEach((entry)->{
            System.out.printf("%s\t%s%n", entry.getKey(),entry.getValue());
            });
        }
        
        /**
	 * Prints the number of policies in each policy set
	 * 
	 * @param polset
	 */
        private void printNumberOfPoliciesInPolicyset (PolicySetType polset)
        {
            if (polset == null)
                return;

            List<Object> polsetChildren = extractChildenOfPolicyset(polset);
            for (Object obj : polsetChildren)
                if(obj instanceof PolicyType)
                {
                    int count = numberOfPoliciesInPolicysets.containsKey(polset.getPolicySetId()) ? numberOfPoliciesInPolicysets.get(polset.getPolicySetId()) : 0;
                    numberOfPoliciesInPolicysets.put(polset.getPolicySetId(), count+1);

                }
                else if (obj instanceof PolicySetType)
                {
                    printNumberOfPoliciesInPolicyset((PolicySetType) obj);
                }
        }
        
	//Prints the number of policy sets in each policy set
        public void printNumberOfPolicysetsInPolicyset ()
        {            
            System.out.println();
            System.out.println("#Number of policy sets in each policy set");
            
            printNumberOfPolicysetsInPolicyset(this.policyset);            
            
            numberOfPolicysetsInPolicysets.entrySet().forEach((entry)->{
            System.out.printf("%s\t%s%n", entry.getKey(),entry.getValue());
            });

        }
        
        /**
	 * Prints the number of policy sets in each policy set
	 * 
	 * @param polset
	 */
        private void printNumberOfPolicysetsInPolicyset (PolicySetType polset)
        {
            if (polset == null)
                return;
            
            numberOfPolicysetsInPolicysets.put(polset.getPolicySetId(), 0);
            List<Object> polsetChildren = extractChildenOfPolicyset(polset);
            for (Object obj : polsetChildren)
                if(obj instanceof PolicySetType)
                {
                    int count = numberOfPolicysetsInPolicysets.containsKey(polset.getPolicySetId()) ? numberOfPolicysetsInPolicysets.get(polset.getPolicySetId()) : 0;
                    numberOfPolicysetsInPolicysets.put(polset.getPolicySetId(), count+1);
                    
                    printNumberOfPolicysetsInPolicyset((PolicySetType) obj);
                }
        }
        
        private int emptyPolicyTargetCounter()
        {
            return emptyPolicyTargetCounter(this.policyset);
        }
        
        private int emptyPolicyTargetCounter(Object objValue)
        {
            int emptyPolicy = 0;
            if (objValue instanceof PolicyType)
            {
                if (((PolicyType) objValue).getTarget().getAnyOf().isEmpty())
                    return 1;
            }
            
            if (objValue instanceof PolicySetType)
            {
                List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
		for (Object obj : children) 
                {
                        emptyPolicy += emptyPolicyTargetCounter(obj);
                }
                return emptyPolicy;
            }            
            return 0;
        }        
                
        private int emptyPolicysetTargetCounter()
        {
            return emptyPolicysetTargetCounter(this.policyset);
        }
        
        private int emptyPolicysetTargetCounter(Object objValue)
        {
            int emptyPolicyset = 0;
            if (objValue instanceof PolicySetType)
            {
                List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
		for (Object obj : children) 
                {
                        emptyPolicyset += emptyPolicysetTargetCounter(obj);
                }
                if (((PolicySetType) objValue).getTarget().getAnyOf().isEmpty())
                    return emptyPolicyset+1;
                else
                    return emptyPolicyset;
            }
            return 0;
        }
        
        //Prints percentage of policies and policysets without target element
        private void printEmptyTargetPercentage() throws IOException
        {
            double epPrc = ((double) emptyPolicyTargetCounter()/(double) getNumberOfPolicies())*100;
            double epsPrc = ((double) emptyPolicysetTargetCounter()/(double) getNumberOfPolicysets())*100;
            
            System.out.println();
            System.out.println("#Percentage of policies and policysets with null target element");
            
            System.out.printf("Policyset\t%s%%%n",epsPrc);
            System.out.printf("Policy\t%s%%%n",epPrc);
        }
        
    /***************************** 1396/1/17 *****************************/
        
    public List getNumberOfAnyOfInEachTarget() 
    {   
        InitializeAnyOfMap();
        anyOfMap.values().forEach((value)->{
                anyOfInTarget.add(value.size());
        });
            /*if (objValue instanceof RuleType)
            {
                RuleType rId = (RuleType) objValue;
                anyOfInTarget.put(rId.getRuleId(), rId.getTarget().getAnyOf().size());
                //System.out.printf("%s\t%s%n", rId.getRuleId(), rId.getTarget().getAnyOf().size());
            }
            if (objValue instanceof PolicyType)
            {
                PolicyType pol = (PolicyType) objValue;
                anyOfInTarget.put(pol.getPolicyId(), pol.getTarget().getAnyOf().size());
                List<RuleType> rules = getRulesOfPolicy((PolicyType) objValue);
                for (RuleType r : rules)
                    getNumberOfAnyOfInEachRule(r);
            }
            if (objValue instanceof PolicySetType)
            {
                PolicySetType polset = (PolicySetType) objValue;
                anyOfInTarget.put(polset.getPolicySetId(), polset.getTarget().getAnyOf().size());
                List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
                for (Object obj : children) 
                    getNumberOfAnyOfInEachRule(obj);
            }*/
            return anyOfInTarget;
    }
    
    public void printAnyOfStats()
    {
        printNumberOfAnyOfInEachRule();
        printNumberOfAnyOfInEachPolicy();
        printNumberOfAnyOfInEachPolicyset();
    }
    
    private void printNumberOfAnyOfInEachRule(){
        System.out.println();
        System.out.println("#Number of AnyOf expression in each rule");
        System.out.println("#Rule ID\tNumber of AnyOf expressions");

        printNumberOfAnyOfInEachRule(this.policyset);
    }
    
    private void printNumberOfAnyOfInEachRule(Object objValue)
    {
        if (objValue instanceof RuleType)
            {
                System.out.printf("%s\t%s\n", ((RuleType)objValue).getRuleId(), ((RuleType)objValue).getTarget().getAnyOf().size());
            }
            if (objValue instanceof PolicyType)
            {
                List<RuleType> rules = getRulesOfPolicy((PolicyType) objValue);
                for (RuleType r : rules) 
                    printNumberOfAnyOfInEachRule(r);
            }
            if (objValue instanceof PolicySetType)
            {
                List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
                for (Object obj : children) 
                    printNumberOfAnyOfInEachRule(obj);
            } 
    }
       
    private void printNumberOfAnyOfInEachPolicy()
    {       
        System.out.println();
        System.out.println("#Number of AnyOf expressions in each policy");
        System.out.println("#Policy ID\tNumber of AnyOf expressions");
        
        printNumberOfAnyOfInEachPolicy(this.policyset);
    }

    private void printNumberOfAnyOfInEachPolicy(Object objValue) 
    {                    
            if (objValue instanceof RuleType)
            {
                return;
            }
            if (objValue instanceof PolicyType)
            {
                PolicyType pId = (PolicyType) objValue;
                System.out.printf("%s\t%s%n", pId.getPolicyId(), pId.getTarget().getAnyOf().size());
            }
            if (objValue instanceof PolicySetType)
            {
                List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
                for (Object obj : children) 
                    printNumberOfAnyOfInEachPolicy(obj);
            }
    }
    
    private void printNumberOfAnyOfInEachPolicyset()
    {
        System.out.println();
        System.out.println("#Number of AnyOf expressions in each policy set");
        System.out.println("#PolicySet ID\tNumber of AnyOf expression");
        
        printNumberOfAnyOfInEachPolicyset(this.policyset);
    }

    private void printNumberOfAnyOfInEachPolicyset(Object objValue) 
    {                    
            if (objValue instanceof RuleType)
            {
                return;
            }
            if (objValue instanceof PolicyType)
            {
                return;
            }
            if (objValue instanceof PolicySetType)
            {
                PolicySetType pId = (PolicySetType) objValue;
                System.out.printf("%s\t%s%n", pId.getPolicySetId(), pId.getTarget().getAnyOf().size());
                
                List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
                for (Object obj : children) 
                    printNumberOfAnyOfInEachPolicyset(obj);
            }
    }   
    
    public void printNumberOfAllOfsInEachAnyOf()
    {        
        System.out.println();
        System.out.println("#Number of AllOf expressions in each AnyOf expression");

        List anyInAll = getNumberOfAllOfsInEachAnyOf();
        
        for (int i=0; i<anyInAll.size(); i++)
            System.out.println(anyInAll.get(i));
    }
            
    public List getNumberOfAllOfsInEachAnyOf()
    {        
        initializeAnyOfList();
        
        List<Integer> allOfInAnyOf = new ArrayList<>();
        for (AnyOfType aot : anyOfList)
            allOfInAnyOf.add(aot.getAllOf().size());
            //System.out.println(aot.getAllOf().size());
            
        return allOfInAnyOf;
    }
    
    public void printMatchDistribution()
    {
        System.out.println();
        System.out.println("#Number of match element about each attribute ID in each rule (or policy or policy set)");
        printMatchDistribution(this.policyset);
    }
    
    public void printMatchDistribution(Object objValue)
    {
        if (objValue == null)
        {
                return;
        }
        if (objValue instanceof RuleType)
        {
            RuleType rule = (RuleType) objValue;
            System.out.println();
            System.out.println("RuleID: " + rule.getRuleId());
               
            Map<String, Integer> numberOfMatch = new HashMap<>();
            numberOfMatch.clear();
            List<AnyOfType> anyOf = rule.getTarget().getAnyOf();
            
            for (AnyOfType ao : anyOf)
            {
                List<AllOfType> allOfList = ao.getAllOf();
                for (AllOfType allOf : allOfList)
                {
                    List<MatchType> matchList = allOf.getMatch();
                    for (MatchType match : matchList)
                    {
                        String attrId = match.getAttributeDesignator().getAttributeId();
                        int count = numberOfMatch.containsKey(attrId) ? numberOfMatch.get(attrId) : 0;
                        numberOfMatch.put(attrId, count + 1);
                    }
                }
            }
                
            numberOfMatch.entrySet().forEach((temp) -> {
            System.out.printf("%s\t%s%n",temp.getKey(),temp.getValue());
            });
        }
        if (objValue instanceof PolicyType) 
        {
            PolicyType policy = (PolicyType) objValue;
            System.out.println();
            System.out.println("PolicyID: " + policy.getPolicyId());
               
            Map<String, Integer> numberOfMatch = new HashMap<>();
            numberOfMatch.clear();
            List<AnyOfType> anyOf = policy.getTarget().getAnyOf();
            
            for (AnyOfType ao : anyOf)
            {
                List<AllOfType> allOfList = ao.getAllOf();
                for (AllOfType allOf : allOfList)
                {
                    List<MatchType> matchList = allOf.getMatch();
                    for (MatchType match : matchList)
                    {
                        String attrId = match.getAttributeDesignator().getAttributeId();
                        int count = numberOfMatch.containsKey(attrId) ? numberOfMatch.get(attrId) : 0;
                        numberOfMatch.put(attrId, count + 1);
                    }
                }
            }
                
            numberOfMatch.entrySet().forEach((temp) -> {
            System.out.printf("%s\t%s%n",temp.getKey(),temp.getValue());
            });
            
            List<RuleType> rules = getRulesOfPolicy(policy);
            for (RuleType r : rules)
                printMatchDistribution(r);
        }
        if (objValue instanceof PolicySetType)
        {
            PolicySetType ps = (PolicySetType) objValue;
            System.out.println();
            System.out.println("PolicySetID: " + ps.getPolicySetId());
               
            Map<String, Integer> numberOfMatch = new HashMap<>();
            numberOfMatch.clear();
            List<AnyOfType> anyOf = ps.getTarget().getAnyOf();
            
            for (AnyOfType ao : anyOf)
            {
                List<AllOfType> allOfList = ao.getAllOf();
                for (AllOfType allOf : allOfList)
                {
                    List<MatchType> matchList = allOf.getMatch();
                    for (MatchType match : matchList)
                    {
                        String attrId = match.getAttributeDesignator().getAttributeId();
                        int count = numberOfMatch.containsKey(attrId) ? numberOfMatch.get(attrId) : 0;
                        numberOfMatch.put(attrId, count + 1);
                    }
                }
            }
                
            numberOfMatch.entrySet().forEach((temp) -> {
            System.out.printf("%s\t%s%n",temp.getKey(),temp.getValue());
            });
            
            List<Object> children = extractChildenOfPolicyset(ps);
            for (Object obj : children) 
                printMatchDistribution(obj);
        }        
    }
    
    public void printAttributeValueInAttributeIdDistribution()
    {
        initiazileNumberOfAttrId();
        System.out.println();
        System.out.println("#AttributeID:\tNumber of attributeValues");
        numberOfAttrId.entrySet().forEach((entry) -> {
            System.out.printf("attrID: %s\t%s%n", entry.getKey(),entry.getValue());
        });
    }
    
    public Map<String, Integer> getFrequnecyOfAttributeValue()
    {
        initiazileNumberOfAttrId();
        return numberOfAttrId;
    }
    
    //Prints all the attribute IDs used in input XACML policy set
    public void printAttributeIDs()
    {
        initiazileNumberOfAttrId();
        System.out.println();
        System.out.println("#All the AttributeIDs used in input XACML policyset:");
        numberOfAttrId.keySet().forEach((entry) -> {
        System.out.println(entry);
        });
    }
    
    public List getAttrIDs() {
        initiazileNumberOfAttrId();
        List<String> attr = new ArrayList<>(numberOfAttrId.keySet());
        return attr;
    }
    
    public void printNumberOfBooleanOperators()
    {
        initializeAnyOfList();
        Map<String, Integer> booleanOperator = new HashMap<>();
        for (AnyOfType aot : anyOfList)
        {
            List<AllOfType> allOfList = aot.getAllOf();
            for (AllOfType allOf : allOfList)
            {
                List<MatchType> matchList = allOf.getMatch();
                for(MatchType match : matchList)
                {
                    String matchId = match.getMatchId();
                    int count = booleanOperator.containsKey(matchId) ? booleanOperator.get(matchId) : 0;
                    booleanOperator.put(matchId, count + 1);
                }
            }
        }
        System.out.println();
        System.out.println("#All the MatchIDs (boolean operators) used in input XACML policyset:");
        System.out.println("#MatchID\tNumber of match element");
        booleanOperator.entrySet().forEach((entry) -> {
        System.out.printf("%s\t%s%n",entry.getKey(),entry.getValue());
        });
    }
    
/*    public List getAttributeDesignators() {
        List<AttributeDesignatorType> attrDesignator = new ArrayList<>();
        initializeAnyOfList();
        for (AnyOfType aot : anyOfList)
        {
            List<AllOfType> allOfList = aot.getAllOf();
            for (AllOfType allOf : allOfList)
            {
                List<MatchType> matchList = allOf.getMatch();
                for(MatchType match : matchList)
                {
                    AttributeDesignatorType attrDesig = match.getAttributeDesignator();
                    if (!attrDesignator.contains(attrDesig))
                        attrDesignator.add(attrDesig);
                }
            }
        }
        return attrDesignator;
    }
*/    
    public void initializeAttrValuesMap()
    {
        attrValues.clear();
        initializeAnyOfList();
        for (AnyOfType aot : anyOfList)
        {
            List<AllOfType> allOfList = aot.getAllOf();
            for (AllOfType allOf : allOfList)
            {
                List<MatchType> matchList = allOf.getMatch();
                for(MatchType match : matchList)
                {
                    AttributeValueType attrVal;
                    if(match.getAttributeValue() != null){
                        attrVal = match.getAttributeValue();
                    if (attrValues.get(match.getAttributeDesignator().getAttributeId()) == null) 
                        attrValues.put(match.getAttributeDesignator().getAttributeId(), new ArrayList<AttributeValueType>());
                    attrValues.get(match.getAttributeDesignator().getAttributeId()).add(attrVal);          
                    }      
                }
            }
        }
    }
    
    public Map getAttributeValues() 
    {
        /*initializeAttrValuesMap();
        // remove duplicates
        Set<AttributeValueType> hs = new HashSet<>();
        attrValues.entrySet().forEach((entry) -> {
            hs.clear();
            hs.addAll(entry.getValue());
            entry.getValue().clear();
            entry.getValue().addAll(hs);
        });
        
        return attrValues;  */    
        //Map <String, List<AttributeValueType>> attrVals = new HashMap<String, List<AttributeValueType>>();        
        AttributeMapperV3 attrMprV3 = AttributeMapperV3.getInstance();
        //attrVals = attrMpr.getAttributeValuesMap();
        
        return attrMprV3.getAttributeValuesMap();
    }
    
    public Map getAPA(){
       Map<Integer, Integer> apaMap = new HashMap<>();
       initializeAnyOfList();
       for (AnyOfType a : anyOfList){
            int count = apaMap.containsKey(a.getAllOf().size()) ? apaMap.get(a.getAllOf().size()) : 0;
            apaMap.put(a.getAllOf().size(), count + 1);
       }
       return apaMap;
    }
    
    public Map<Integer, Integer> getMPA(){
       Map<Integer, Integer> mpaMap = new HashMap<>();
       initializeAnyOfList();
       for (AnyOfType a : anyOfList){

           for (AllOfType al : a.getAllOf()){
            int count = mpaMap.containsKey(al.getMatch().size()) ? mpaMap.get(al.getMatch().size()) : 0;
            mpaMap.put(al.getMatch().size(), count + 1);
       }
       }
       return mpaMap;
    }
    
    public Map getMatchIDs(){
        //DataType, List<MatchID>
        Map<String, List<String>> matchIDs = new HashMap<>();
        //anyOfList.clear();
        initializeAnyOfList();
        for (AnyOfType aot : anyOfList)
        {
            List<AllOfType> allOfList = aot.getAllOf();
            for (AllOfType allOf : allOfList)
            {
                List<MatchType> matchList = allOf.getMatch();
                for(MatchType match : matchList)
                {
                    String matchId = match.getMatchId();
                    if (matchIDs.get(match.getAttributeDesignator().getDataType()) == null) 
                        matchIDs.put(match.getAttributeDesignator().getDataType(), new ArrayList<String>());
                    matchIDs.get(match.getAttributeValue().getDataType()).add(matchId); 
                }
            }
        }
        return matchIDs;
    }
    
    public List getNumberOfMatchesInAllOf()
    {
        List<Integer> numberOfMatchesInAllOf = new ArrayList<>();
        //anyOfList.clear();
        initializeAnyOfList();
        for (AnyOfType aot : anyOfList)
        {
            List<AllOfType> allOfList = aot.getAllOf();
            for (AllOfType allOf : allOfList)
            {
                numberOfMatchesInAllOf.add(allOf.getMatch().size());
            }
        }
        return numberOfMatchesInAllOf;
    }
    
    public List getMatchList()
    {
        List<MatchType> matchList = new ArrayList<>();
        
        for (AnyOfType aot : anyOfList)
        {
            List<AllOfType> allOfList = aot.getAllOf();
            for (AllOfType allOf : allOfList)
            {
                matchList.addAll(allOf.getMatch());
            }
        }
        
        return matchList;
    }
    
    public Map<String, String> getAttributeIdDataTypeMap()
    {
        //Attribute ID, DataType
        Map<String, String> attributeIdDataTypeMap = new HashMap<>();
        //anyOfList.clear();
        initializeAnyOfList();
        for (AnyOfType aot : anyOfList)
        {
            List<AllOfType> allOfList = aot.getAllOf();
            for (AllOfType allOf : allOfList)
            {
                List<MatchType> matchList = allOf.getMatch();
                for(MatchType match : matchList)
                {
                    attributeIdDataTypeMap.put(match.getAttributeDesignator().getAttributeId(), match.getAttributeDesignator().getDataType());
                }
            }
        }
        return attributeIdDataTypeMap;
    }
    
    public List<String> getIntervals ()
    {
        initializeAnyOfList();
        OperatorType operator;
        List<String> outList = new ArrayList<>();
        
        for (AnyOfType aot : anyOfList)
        {
            List<AllOfType> allOfList = aot.getAllOf();
            for (AllOfType allOf : allOfList)
            {
                List<MatchType> matchList = allOf.getMatch();
                for(MatchType match : matchList)
                {
                    operator = MatchIdConverterUtil.getOperator(match.getMatchId());
                    if (operator != OperatorType.EQUAL)
                    {
                        outList.add(match.getAttributeDesignator().getAttributeId());
                    }
                }
            }
        }
        
        Set<String> outListUniqueValues = new HashSet<>(outList);
        List<String> listWithUniqueValues = new ArrayList<>(outListUniqueValues);
        
        return listWithUniqueValues;
    }    
    
    public List<String> getEqlOprAttrID ()
    {
        initializeAnyOfList();
        OperatorType operator;
        List<String> outList = new ArrayList<>();
        
        for (AnyOfType aot : anyOfList)
        {
            List<AllOfType> allOfList = aot.getAllOf();
            for (AllOfType allOf : allOfList)
            {
                List<MatchType> matchList = allOf.getMatch();
                for(MatchType match : matchList)
                {
                    operator = MatchIdConverterUtil.getOperator(match.getMatchId());
                    if (operator == OperatorType.EQUAL)
                    {
                        outList.add(match.getAttributeDesignator().getAttributeId());
                    }
                }
            }
        }
        
        Set<String> outListUniqueValues = new HashSet<>(outList);
        List<String> listWithUniqueValues = new ArrayList<>(outListUniqueValues);
        
        return listWithUniqueValues;
    }

    public void initializeAnyOfList()
    {
        anyOfList.clear();
        InitializeAnyOfMap();
        anyOfMap.entrySet().forEach((entry) -> {
            anyOfList.addAll(entry.getValue());
        });  
        
        //InitializeAnyOfList(this.policyset);
    }
    
    public List<AnyOfType> getTotalAnyOf(){
        initializeAnyOfList();
        return anyOfList;
    }
    
    public void InitializeAnyOfMap(){
        anyOfMap.clear();
        InitializeAnyOfMap(this.policyset);
    }
    
    public List<TargetType> getTotalTargets(Object objValue){

        if (objValue instanceof RuleType)
        {
            totalTargets.add(((RuleType) objValue).getTarget());
        }
        if (objValue instanceof PolicyType)
        {
            totalTargets.add(((PolicyType) objValue).getTarget());
            
            List<RuleType> rules = getRulesOfPolicy((PolicyType) objValue);
                for (RuleType r : rules)
                    getTotalTargets(r);
        }
        if (objValue instanceof PolicySetType)
        {
            totalTargets.add(((PolicySetType) objValue).getTarget());
            
            List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
                for (Object obj : children)
                    getTotalTargets(obj);
        }
        
        return totalTargets;
    }
    
    public void InitializeAnyOfMap(Object objValue)
    {
        if (objValue instanceof RuleType)
        {
            anyOfMap.put(((RuleType) objValue).getRuleId(), ((RuleType) objValue).getTarget().getAnyOf());
        }
        if (objValue instanceof PolicyType)
        {
            anyOfMap.put(((PolicyType) objValue).getPolicyId(), ((PolicyType) objValue).getTarget().getAnyOf());
            
            List<RuleType> rules = getRulesOfPolicy((PolicyType) objValue);
                for (RuleType r : rules)
                    InitializeAnyOfMap(r);
        }
        if (objValue instanceof PolicySetType)
        {
            anyOfMap.put(((PolicySetType) objValue).getPolicySetId(), ((PolicySetType) objValue).getTarget().getAnyOf());
            
            List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
                for (Object obj : children)
                    InitializeAnyOfMap(obj);
        }
    }
    
    public void initiazileNumberOfAttrId()
    {
        numberOfAttrId.clear();
        for (AnyOfType aol : anyOfList)
            {
                List<AllOfType> allOfList = aol.getAllOf();
                for (AllOfType allOf : allOfList)
                {
                    List<MatchType> matchList = allOf.getMatch();
                    for (MatchType match : matchList)
                    {
                        int count = numberOfAttrId.containsKey(match.getAttributeDesignator().getAttributeId()) ? numberOfAttrId.get(match.getAttributeDesignator().getAttributeId()) : 0;
                        numberOfAttrId.put(match.getAttributeDesignator().getAttributeId(), count + 1);
                    }
                }
            }
    }
        /**********************************************************************/

                
	/**
	 * Extract the list of rules from a policy object
	 * 
	 * @param policy
	 * @return
	 * @throws XACMLTranslatingException
	 */
	public List<RuleType> getRulesOfPolicy(PolicyType policy) {
		List<Object> objs = policy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition();

		List<RuleType> rules = new ArrayList<RuleType>();
		if (objs == null || objs.size() == 0) {
			return rules;
		}
		for (Object obj : objs) {
			if (obj instanceof RuleType)
				rules.add((RuleType) obj);
		}
		return rules;
	}

	/**
	 * Extract the list of children for this policy object
	 * 
	 * @return
	 * @throws XACMLTranslatingException
	 */
	public List<Object> extractChildenOfPolicyset(PolicySetType policysetObj) {
		List<JAXBElement<?>> objs = policysetObj.getPolicySetOrPolicyOrPolicySetIdReference();
		List<Object> children = new ArrayList<>();
		if (objs == null || objs.size() == 0) {
			return children;
		}
		for (JAXBElement<?> obj : objs) {
			if (obj != null) {
				Object objValue = obj.getValue();
				if (objValue instanceof PolicyType || objValue instanceof PolicySetType)
					children.add(objValue);
			}
		}
		return children;
	}

	/**
	 * Removes a rule from this XACML policy
	 * 
	 * @return
	 */
	public void removeRule(ArrayList<Integer> removedIds) {
		removeRule(this.policyset, removedIds, 1);
	}

	private int removeRule(Object objValue, ArrayList<Integer> removedIds, int beginingRuleId) {
		// TODO Auto-generated method stub
		int theRuleId = beginingRuleId;
		if (objValue == null)
			return beginingRuleId;
		if (objValue instanceof PolicyType) {
			List<RuleType> rules = getRulesOfPolicy((PolicyType) objValue);
			List<Object> rList = new ArrayList<Object>();
			for (int i = 0; i < rules.size(); i++) {
				if (removedIds.contains(theRuleId)) {
					rList.add(rules.get(i));
					removedIds.remove(Integer.valueOf(theRuleId));
				}
				theRuleId++;
			}
			for (Object j : rList)
				((PolicyType) objValue).getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().remove(j);
			return theRuleId;
		}
		if (objValue instanceof PolicySetType) {
			List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
			for (Object obj : children) {
				theRuleId = removeRule(obj, removedIds, theRuleId);
			}
			return theRuleId;
		}
		return beginingRuleId;
	}

	/**
	 * Remove all empty policies/policy sets
	 */
	public void removeEmptyElements() {
		// TODO Auto-generated method stub
		while (removeEmptyElements(this.policyset))
			;
	}

	private boolean removeEmptyElements(Object objValue) {
		// TODO Auto-generated method stub
		if (objValue == null)
			return false;
		boolean changedFlag = false;
		if (objValue instanceof PolicySetType) {
			List<Object> children = extractChildenOfPolicyset((PolicySetType) objValue);
			List<Integer> rList = new ArrayList<Integer>();
			for (int i = 0; i < children.size(); i++) {
				Object obj = children.get(i);
				if (obj instanceof PolicyType) {
					if (getRulesOfPolicy((PolicyType) obj).size() <= 0)
						rList.add(i);
					// rList.add(((PolicySetType)
					// objValue).getPolicySetOrPolicyOrPolicySetIdReference().get(i));
				}
				if (obj instanceof PolicySetType) {
					boolean flag = removeEmptyElements(obj);
					if (flag)
						changedFlag = true;
					if (extractChildenOfPolicyset((PolicySetType) obj).size() <= 0)
						rList.add(i);
					// rList.add(((PolicySetType)
					// objValue).getPolicySetOrPolicyOrPolicySetIdReference().get(i));
				}
			}
			Collections.sort(rList, Collections.reverseOrder());
			for (int j = 0; j < rList.size(); j++)
				((PolicySetType) objValue).getPolicySetOrPolicyOrPolicySetIdReference().remove(rList.get(j).intValue());
			if (changedFlag || rList.size() > 0)
				return true;
			else
				return false;
		}
		return false;
	}

	/**
	 * squeeze the attribute types/values based on the new type/values in the
	 * argument
	 * 
	 * @param newAttributeValues
	 */
	public void squeezeAttributes(Map<String, List<String>> newAttributeValues) {
		// TODO Auto-generated method stub
		squeezeAttributes(this.policyset, newAttributeValues);
	}

	/**
	 * squeeze the attribute types/values based on the new type/values in the
	 * argument
	 * 
	 * @param newAttributeValues
	 */
	private void squeezeAttributes(Object objValue, Map<String, List<String>> newAttributeValues) {
		// TODO Auto-generated method stub
		if (objValue == null)
			return;
		if (objValue instanceof PolicyType) {
			PolicyType policy = (PolicyType) objValue;
			squeezeAttributesTarget(policy.getTarget(), newAttributeValues);
			List<RuleType> rules = getRulesOfPolicy(policy);
			for (int i = 0; i < rules.size(); i++)
				squeezeAttributesTarget(rules.get(i).getTarget(), newAttributeValues);
		}
		if (objValue instanceof PolicySetType) {
			PolicySetType policyset = (PolicySetType) objValue;
			squeezeAttributesTarget(policyset.getTarget(), newAttributeValues);
			List<Object> children = extractChildenOfPolicyset(policyset);
			for (int i = 0; i < children.size();i++) {
				Object obj = children.get(i);
				squeezeAttributes(obj, newAttributeValues);
			}
		}
	}

	/**
	 * squeeze the attribute types/values of a target object based on the new
	 * type/values in the argument
	 * 
	 * @param newAttributeValues
	 */
	private void squeezeAttributesTarget(TargetType targetObj, Map<String, List<String>> newAttributeValues) {
		// TODO Auto-generated method stub
		if (targetObj == null || targetObj.getAnyOf() == null)
			return;
		List<AnyOfType> lstAnyOf = targetObj.getAnyOf();
		// if the target is empty which means it is always true
		if (lstAnyOf == null || lstAnyOf.size() == 0) {
			return;
		}
		for (AnyOfType anyOf : lstAnyOf) {
			if (anyOf == null)
				continue;
			List<AllOfType> lstAllOf = anyOf.getAllOf();
			if (lstAllOf == null || lstAllOf.size() == 0)
				continue;
			for (AllOfType allOf : lstAllOf) {
				if (allOf == null)
					continue;
				List<MatchType> lstMatches = allOf.getMatch();
				List<Integer> rList = new ArrayList<Integer>();
				List<String> currAttributeTypes = new ArrayList<String>();
				for (int i = 0; i < lstMatches.size();i++) {
					MatchType match = lstMatches.get(i);
					if (match == null) {
						rList.add(i);
						continue;
					}
					String varId = match.getAttributeDesignator().getAttributeId();
					try {
						String attributeId = AllOfExpression.getAttributeId(varId);
						String attributeValue = AllOfExpression.getAttributeValue(match.getAttributeValue());
						if (currAttributeTypes.contains(attributeId)) {
							rList.add(i);
							continue;
						}
						// if the attribute type is in the list of new attributes
						if (newAttributeValues.keySet().contains(attributeId)) {
							if (newAttributeValues.get(attributeId).contains(attributeValue)) {
								currAttributeTypes.add(attributeId);
								continue;
							}
							// select a value for this attribute type and replace it
							int indx = XACMLUtil.getRandomNumber(0, newAttributeValues.get(attributeId).size());
							String newValue = newAttributeValues.get(attributeId).get(indx);
							match.getAttributeValue().getContent().set(0, newValue);
							currAttributeTypes.add(attributeId);
							continue;
						}
						else {
							// if there is no remaining attribute id
							if (currAttributeTypes.size() >= newAttributeValues.keySet().size()) {
								rList.add(i);
								continue;
							}
							// we have to replace the attribute with a random one from the new list
							int indx = -1;
							String newAttrId = "";
							while (true) {
								indx = XACMLUtil.getRandomNumber(0, newAttributeValues.keySet().size());
								newAttrId = (new ArrayList<String>(newAttributeValues.keySet())).get(indx);
								if (!currAttributeTypes.contains(newAttrId))
									break;
							}
							if (indx < 0) {
								rList.add(i);
								continue;
							}
							// select a value for this attribute type and replace it
							int indx2 = XACMLUtil.getRandomNumber(0, newAttributeValues.get(newAttrId).size());
							String newAttrValue = newAttributeValues.get(newAttrId).get(indx2);
							match.getAttributeValue().getContent().set(0, newAttrValue);
							match.getAttributeDesignator().setAttributeId(newAttrId);
							currAttributeTypes.add(newAttrId);
							continue;
						}
						
					} catch (XACMLTranslatingException e) {
						continue;
					}
				}
				// remove the extra match elements
				if (rList.size() > 0) {
					Collections.sort(rList, Collections.reverseOrder());
					for (int j = 0; j < rList.size(); j++)
						lstMatches.remove(rList.get(j).intValue());
				}

			}
		}
	}

	/**
	 * Print the statistical information of this policy on the standard output
	 * 
	 * @throws Exception
	 */
	public void printPolicysetStat() throws Exception {
		printPolicysetStat(System.out);
	}

	/**
	 * Print the statistical information of this policy
	 * 
	 * @param out
	 * @throws Exception
	 */
	public void printPolicysetStat(PrintStream out) throws Exception {
            
		out.println("Number of policy sets: " + String.valueOf(getNumberOfPolicysets()));
		out.println("Number of policies: " + String.valueOf(getNumberOfPolicies()));
		out.println("Number of rules: " + String.valueOf(getNumberOfRules()));
            //	out.println("Mean number of rules in policies: " + String.valueOf(getMeanNumberOfRulesInPolicies()));
            //	out.println("Mean number of policies in policy sets: " + String.valueOf(getMeanNumberOfPoliciesInPolicysets()));

                
            //    printNumberOfRulesInPolicies();
            //    printNumberOfPoliciesInPolicyset();
            //    printNumberOfPolicysetsInPolicyset();
                

                AttributeMapper attrMapper = AttributeMapper.getInstance();
		attrMapper.clearAll();
                
                PolicyCombiningAlgorithmMapper pcaMapper = PolicyCombiningAlgorithmMapper.getInstance();
                pcaMapper.clearAll();
                
                RuleCombiningAlgorithmMapper rcaMapper = RuleCombiningAlgorithmMapper.getInstance();
                rcaMapper.clearAll();
                
                ruleEffectMapper reMapper = ruleEffectMapper.getInstance();
                reMapper.clearAll();
                
                ConditionMapper cMapper = ConditionMapper.getInstance();
                cMapper.clearAll();
                
                ObligationMapper oMapper = ObligationMapper.getInstance();
                oMapper.clearAll();
                
                AttributeMapperV3 attrMprV3 = AttributeMapperV3.getInstance();
                attrMprV3.clearAll();
                
                PolicySetTranslator pst = new PolicySetTranslator(policyset);
		pst.parse();
		
                // print the stat of policy combining algorithm
        /*        out.println();
                out.println("Number of policy combining algorithms: " + String.valueOf(pcaMapper.getNumberOfPolicyCombiningAlgorithms()));
                pcaMapper.printNumberOfPolicysetsForEachPolicyCombiningAlgorithm();
                
                // print the stat of rule combining algorithm
                out.println();
                out.println("Number of rule combining algorithms: " + String.valueOf(rcaMapper.getNumberOfRuleCombiningAlgorithms()));
                rcaMapper.printNumberOfPoliciesForEachRuleCombiningAlgorithm();
                
                // print stats of rule effect
                out.println();        
                reMapper.printPercentageOfPermitAndDenyEffect();
                
                printEmptyTargetPercentage();
                
                out.println();        
                out.println("Number of  AnyOf expression: " + getNumberOfAnyOf(policyset));
                out.println("Number of  AllOf expression: " + getNumberOfAllOf(policyset));
                
                printAnyOfStats();
                printNumberOfAllOfsInEachAnyOf();
        
                printMatchDistribution();
                                
                // print the stat of attributes
                out.println();
                out.println("Number of attribute types: " + String.valueOf(attrMapper.getNumberOfAttributeTypes()));
		out.println("Number of attribute values: " + String.valueOf(attrMapper.getNumberOfAttributeValues()));

                printAttributeIDs();
                printAttributeValueInAttributeIdDistribution();      
                
                printNumberOfBooleanOperators();            
                
                out.println("--- Condition Status ---");
                cMapper.printNumberOfConditions();
                
                out.println("--- Obligation Status ---");
                oMapper.printNumberOfObligationtions();
                
                //getAttributeDesignators();
                initializeAttrValuesMap();
                matchElementAnalyses();
        */        
                /* attrMapper = null;               
                 pcaMapper = null;                
                 rcaMapper = null;                
                 reMapper = null;                
                 cMapper = null;
                 oMapper = null;
                 pst = null; */
    }

        
	/**
	 * @return the policy set
	 */
	public PolicySetType getPolicyset() {
		return policyset;
	}

	/**
	 * @param policyset
	 *            the policy set to set
	 */
	public void setPolicyset(PolicySetType policyset) {
		this.policyset = policyset;
	}
}
