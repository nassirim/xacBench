/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unsw.cse.xacml.experiment.v_3_0;

import edu.unsw.cse.xacml.translator.util.AdviceMapper;
import edu.unsw.cse.xacml.translator.util.AttributeMapper;
import edu.unsw.cse.xacml.translator.util.ConditionMapper;
import edu.unsw.cse.xacml.translator.util.ObligationMapper;
import edu.unsw.cse.xacml.translator.util.PolicyCombiningAlgorithmMapper;
import edu.unsw.cse.xacml.translator.util.RuleCombiningAlgorithmMapper;
import edu.unsw.cse.xacml.translator.util.ruleEffectMapper;
import edu.unsw.cse.xacml.translator.v_3_0.PolicySetTranslator;
import edu.unsw.cse.xacml.util.XACMLUtilV3;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Timestamp;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceExpressionsType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AllOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AnyOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeAssignmentExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeDesignatorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ConditionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.EffectType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.MatchType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObjectFactory;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationExpressionsType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.RuleType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.TargetType;

/**
 *
 * @author shayan_310
 */
public class PolSetGen {

    String outputFileName;
    private PolicySetType policyset;
    int ruleTargetSize;
    int ruleSize;
    int policyTargetSize;
    int policySize;
    int policySetTargetSize;
    int policySetSize;
    int scaleFactor;
    int maxRules;
    int minRules;
    int minAnyOf;
    int maxAnyOf;
    int minAllOf;
    int maxAllOf;
    int numberOfPolicies;
    int maxMatch;
    int minMatch;
    int hierarchicalDegree;

    List<String> attributeIDs;
    Map<String, List<String>> matchIDs;
    Map<String, List<AttributeValueType>> attrVals;
    Map<String, List<AttributeValueType>> newAttrVals;
    List<String> attrIDsAskeys;
    Map<String, String> attributeIdDataTypeMap;

    Map<String, Integer> numberOfRulesInPolicy;
    //Map<Integer, Integer> scaleMap;
    Map<Integer, Integer> policySetMap;         //<Key: "n-PolicySet PolicySet", Value: "count">
    Map<Integer, Integer> policySetPolicyMap;   //<Key: "n-Policy PolicySet", Value: "count">
    Map<Integer, Integer> policyRuleMap;        //<Key: "n-Rule Policy", Value: "count">
    Map<Integer, Integer> targetAnyOfMap;       //<Key: "n-AnyOf Target", Value: "count">
    Map<Integer, Integer> anyOfAllOfMap;        //<Key: "n-AllOf AnyOf", Value: "count">
    Map<Integer, Integer> allOfMatchMap;        //<Key: "n-Match AllOf", Value: "count">
    List<String> nonEqualOperatorsAttrID;
    List<String> equalOperatorsAttrID;
    List<TargetType> generatedTargetList = new ArrayList<TargetType>();
    List<AnyOfType> generatedAnyOfList = new ArrayList<AnyOfType>();
    List<AllOfType> generatedAllOfList = new ArrayList<AllOfType>();
    List<MatchType> generatedMatchList = new ArrayList<MatchType>();

    Map<String, List<Integer>> attrIDValFrq;    //<Key: "AttributeID", Value: "List<unique attrVal,attrValFrq>">
    Map<String, Integer> numberOfAttrId;
    Map<String, List<Integer>> scaledAttrIDValFrq;
    List temp;
    List<MatchType> matchList;
    List<JAXBElement> JAXBElementList;
    //int totalNumberOfMatches;

    List<String> stringOperators = new ArrayList<>(Arrays.asList(
            "urn:oasis:names:tc:xacml:1.0:function:string-equal",
            "urn:oasis:names:tc:xacml:1.0:function:string-greater-than",
            "urn:oasis:names:tc:xacml:1.0:function:string-greater-than-or-equal",
            "urn:oasis:names:tc:xacml:1.0:function:string-less-than",
            "urn:oasis:names:tc:xacml:1.0:function:string-less-than-or-equal"));

    List<String> integerOperators = new ArrayList<>(Arrays.asList(
            "urn:oasis:names:tc:xacml:1.0:function:integer-equal",
            "urn:oasis:names:tc:xacml:1.0:function:integer-greater-than",
            "urn:oasis:names:tc:xacml:1.0:function:integer-greater-than-or-equal",
            "urn:oasis:names:tc:xacml:1.0:function:integer-less-than",
            "urn:oasis:names:tc:xacml:1.0:function:integer-less-than-or-equal"));

    List<String> doubleOperators = new ArrayList<>(Arrays.asList(
            "urn:oasis:names:tc:xacml:1.0:function:double-equal",
            "urn:oasis:names:tc:xacml:1.0:function:double-greater-than",
            "urn:oasis:names:tc:xacml:1.0:function:double-greater-than-or-equal",
            "urn:oasis:names:tc:xacml:1.0:function:double-less-than",
            "urn:oasis:names:tc:xacml:1.0:function:double-less-than-or-equal"));

    List<String> booleanOperators = new ArrayList<>(Arrays.asList(
            "urn:oasis:names:tc:xacml:1.0:function:boolean-equal"));

    List<String> dateOperators = new ArrayList<>(Arrays.asList(
            "urn:oasis:names:tc:xacml:1.0:function:date-equal",
            "urn:oasis:names:tc:xacml:1.0:function:date-greater-than",
            "urn:oasis:names:tc:xacml:1.0:function:date-greater-than-or-equal",
            "urn:oasis:names:tc:xacml:1.0:function:date-less-than",
            "urn:oasis:names:tc:xacml:1.0:function:date-less-than-or-equal"));

    PolicySetType PS;
    PolicyType policy;
    RuleType r;
    TargetType trg;
    int ruleNumber;
    int policyNumber;
    int policySetNumber;
    Random rv;
    ObjectFactory obj;
    JAXBElement<PolicyType> jaxb_policy;
    JAXBElement<PolicySetType> jaxb_policySet;
    List<JAXBElement<?>> generatedPolicyList;
    List<JAXBElement<?>> generatedPolicySetList;

    int cndNum;
    int oblNum;
    int rulOblNum;
    int polOblNum;
    int polsetOblNum;
    int advNum;
    int rulAdvNum;
    int polAdvNum;
    int polsetAdvNum;

    PolSetGen(PolicySetType polset, int size, String fileName) throws Exception {
        this.policyset = polset;
        outputFileName = fileName;

        XACML3Analyzer xAnalyzer = new XACML3Analyzer(policyset);
        //xAnalyzer.printPolicysetStat();

        ruleSize = xAnalyzer.getNumberOfRules();
        policySize = xAnalyzer.getNumberOfPolicies();
        policySetSize = xAnalyzer.getNumberOfPolicysets();

        ruleTargetSize = size;
        scaleFactor = ruleTargetSize / ruleSize;
        policyTargetSize = scaleFactor * policySize;
        policySetTargetSize = scaleFactor * policySetSize;
        hierarchicalDegree = 0;

        numberOfRulesInPolicy = xAnalyzer.getNumberOfRulesInPolicies();
        List<Integer> numberOfAnyOfInTarget = xAnalyzer.getNumberOfAnyOfInEachTarget();
        List<Integer> numberOfAllOfInAnyOf = xAnalyzer.getNumberOfAllOfsInEachAnyOf();
        List<Integer> numberOfMatchesInAllOf = xAnalyzer.getNumberOfMatchesInAllOf();
        attributeIDs = xAnalyzer.getAttrIDs();
        matchIDs = xAnalyzer.getMatchIDs();
        attrVals = xAnalyzer.getAttributeValues();
        attrIDsAskeys = new ArrayList<String>(attrVals.keySet());
        attributeIdDataTypeMap = xAnalyzer.getAttributeIdDataTypeMap();
        nonEqualOperatorsAttrID = xAnalyzer.getIntervals();
        equalOperatorsAttrID = xAnalyzer.getEqlOprAttrID();
        numberOfAttrId = xAnalyzer.getFrequnecyOfAttributeValue();
        matchList = xAnalyzer.getMatchList();

        minRules = Collections.min(numberOfRulesInPolicy.values());
        maxRules = Collections.max(numberOfRulesInPolicy.values());
        minAnyOf = Collections.min(numberOfAnyOfInTarget);
        maxAnyOf = Collections.max(numberOfAnyOfInTarget);
        minAllOf = Collections.min(numberOfAllOfInAnyOf);
        maxAllOf = Collections.max(numberOfAllOfInAnyOf);
        minMatch = Collections.min(numberOfMatchesInAllOf);
        maxMatch = Collections.max(numberOfMatchesInAllOf);

    }

    public PolicySetType syntheticPolicySetGenerator() throws Exception {

        /*    List numberOfRulesInPolicies = distributeRulesInPolicies();
        List<RuleType> rules = new ArrayList<>();
        //List<PolicyType> policies = new ArrayList<>();
        PolicySetType PS = new PolicySetType();
        PolicyType policy;
        RuleType r;
        int ruleNumber=0;
        Random rv = new Random();
        ObjectFactory obj;
        JAXBElement<?> jaxb;
        
        //for: iterate over policies
        for (int i=0; i<numberOfRulesInPolicies.size(); i++){
            policy = new PolicyType();
            rules.clear();
            for (int j=0; j<(int)numberOfRulesInPolicies.get(i); j++)
            {
                r = new RuleType();
                ruleNumber++;
                r.setRuleId("R"+ruleNumber);
                r.setEffect(getRandomEffect());
                r.setTarget(getRandomTarget());
                
                if (rv.nextBoolean())
                    r.setCondition(getRandomCondition());
                
                if(rv.nextBoolean())
                    r.setObligationExpressions(getRandomObligation());
                
                rules.add(r);
            }
            policy.setPolicyId("P"+i);
            policy.setTarget(getRandomTarget());
            policy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().addAll(rules);
            policy.setRuleCombiningAlgId(getRandomRCA());
            
            if (rv.nextBoolean())
                policy.setObligationExpressions(getRandomObligation());
            
            obj = new ObjectFactory();
            jaxb = obj.createPolicy(policy);
            PS.getPolicySetOrPolicyOrPolicySetIdReference().add(jaxb);
            //policies.add(policy);
        }
        PS.setPolicySetId("PS");
        PS.setTarget(getRandomTarget());
        PS.setPolicyCombiningAlgId(getRandomPCA());
        
        if (rv.nextBoolean())
            PS.setObligationExpressions(getRandomObligation());
         */
        parse(policyset);

        policySetMap = scale(policySetMap, policySetTargetSize);

        policySetTargetSize = 0;
        for (Map.Entry<Integer, Integer> entry : policySetMap.entrySet()) {
            policySetTargetSize += entry.getValue();
        }

        policySetPolicyMap = scale(policySetPolicyMap, policySetTargetSize);

        policyTargetSize = 0;
        for (Map.Entry<Integer, Integer> entry : policySetPolicyMap.entrySet()) {
            policyTargetSize += entry.getKey() * entry.getValue();
        }

        policyRuleMap = scale(policyRuleMap, policyTargetSize);

        ruleTargetSize = 0;
        for (Map.Entry<Integer, Integer> entry : policyRuleMap.entrySet()) {
            ruleTargetSize += entry.getKey() * entry.getValue();
        }

        targetAnyOfMap = scale(targetAnyOfMap, policySetTargetSize + policyTargetSize + ruleTargetSize);

        int targetNumberOfAnyOfs = 0;
        for (Map.Entry<Integer, Integer> entry : targetAnyOfMap.entrySet()) {
            targetNumberOfAnyOfs += entry.getKey() * entry.getValue();
        }

        anyOfAllOfMap = scale(anyOfAllOfMap, targetNumberOfAnyOfs);

        int targetNumberOfAllOfs = 0;
        for (Map.Entry<Integer, Integer> entry : anyOfAllOfMap.entrySet()) {
            targetNumberOfAllOfs += entry.getKey() * entry.getValue();
        }

        int inputMatchSize = 0;
        // Calculate total number of match elements used in input dataset
        for (Map.Entry<Integer, Integer> entry : allOfMatchMap.entrySet()) {
            inputMatchSize += entry.getKey() * entry.getValue();
        }
        allOfMatchMap = scale(allOfMatchMap, targetNumberOfAllOfs);

        int targetMatchSize = 0;
        // Calculate Target Match Size
        for (Map.Entry<Integer, Integer> entry : allOfMatchMap.entrySet()) {
            targetMatchSize += entry.getKey() * entry.getValue();
        }

        /*int inputMatchSize = 0;
        // Calculate total number of match elements used in input dataset
        for (Map.Entry<String, Integer> entry : numberOfAttrId.entrySet()) {
            inputMatchSize += entry.getValue();
        }*/
        int scl = (int) Math.ceil(targetMatchSize / (double) inputMatchSize);

        // generate scaled AttrIDValFrq
        scaledAttrIDValFrq = new HashMap<String, List<Integer>>();
        for (Map.Entry<String, List<Integer>> entry : attrIDValFrq.entrySet()) {
            temp = new ArrayList();
            temp.add(0, entry.getValue().get(0) * scl);
            temp.add(1, entry.getValue().get(1) * scl);
            //values = Arrays.asList(entry.getValue().get(0)*scl, entry.getValue().get(1)*scl);
            scaledAttrIDValFrq.put(entry.getKey(), temp);
        }

        newAttrVals = generateNewAttrValsMap(attrVals);

        initializeGeneratedMatchList();
        initializeGeneratedAllOfList();
        initializeGeneratedAnyOfList();
        initializeGeneratedTargetList();

        initializeJAXBElementList();

        /*totalNumberOfMatches = 0;
    for (Map.Entry<Integer, Integer> entry : allOfMatchMap.entrySet()){
        totalNumberOfMatches+=entry.getKey()*entry.getValue();
    }*/
        policy = null;
        trg = null;
        ruleNumber = 0;
        policyNumber = 0;
        policySetNumber = 0;
        rv = new Random();
        cndNum = getNumberOfGeneratedConditions();
        oblNum = getNumberOfGeneratedObligations();
        rulOblNum = (int) ((ruleSize / (double) (ruleSize + policySize + policySetSize)) * oblNum);
        polOblNum = (int) ((policySize / (double) (ruleSize + policySize + policySetSize)) * oblNum);
        polsetOblNum = (int) ((policySetSize / (double) (ruleSize + policySize + policySetSize)) * oblNum);
        advNum = getNumberOfGeneratedAdvices();
        rulAdvNum = (int) ((ruleSize / (double) (ruleSize + policySize + policySetSize)) * advNum);
        polAdvNum = (int) ((policySize / (double) (ruleSize + policySize + policySetSize)) * advNum);
        polsetAdvNum = (int) ((policySetSize / (double) (ruleSize + policySize + policySetSize)) * advNum);

        generatedPolicyList = new ArrayList<>();
        generatedPolicySetList = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : policyRuleMap.entrySet()) {
            //for: number of policies    
            for (int i = 0; i < entry.getValue(); i++) {
                generatedPolicyList.add(generatePolicy(entry.getKey()));
            }
        }
        
    /*    for (Map.Entry<Integer, Integer> entry : policySetPolicyMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                generatedPolicySetList.add(generatePolicySet(entry.getKey()));
            }
        }
    */    
        PolicySetType root_ps = new PolicySetType();
        //root_ps.getPolicySetOrPolicyOrPolicySetIdReference().addAll(generatedPolicySetList);
        root_ps.getPolicySetOrPolicyOrPolicySetIdReference().addAll(generatedPolicyList);
        root_ps.setPolicySetId("root_ps");
        root_ps.setTarget(getRandomTarget());
        root_ps.setPolicyCombiningAlgId(getRandomPCA());
            XACMLUtilV3 xacml3Util = new XACMLUtilV3();
        	FileOutputStream out = new FileOutputStream(outputFileName);
        	xacml3Util.savePolicy(root_ps, out/* System.out */);
            out.close();
        return root_ps;
    }

    public JAXBElement<PolicySetType> generatePolicySet(int policies) throws Exception {
        PS = new PolicySetType();
        policySetNumber++;

        for (int i = 0; i < policies; i++) {
            int s = rv.nextInt(generatedPolicyList.size());
            PS.getPolicySetOrPolicyOrPolicySetIdReference().add(generatedPolicyList.get(s));
            generatedPolicyList.remove(s);
        }

        PS.setPolicySetId("PS" + policySetNumber);
        PS.setTarget(getRandomTarget());
        PS.setPolicyCombiningAlgId(getRandomPCA());

        if (polsetOblNum > 0) {
            PS.setObligationExpressions(getNewObligation());
            polsetOblNum--;
        }

        if (polsetAdvNum > 0) {
            PS.setAdviceExpressions(getNewAdvice());
            polsetAdvNum--;
        }
        obj = new ObjectFactory();
        jaxb_policySet = obj.createPolicySet(PS);
        return jaxb_policySet;
    }

    public JAXBElement<PolicyType> generatePolicy(int rules) throws Exception {
        policy = new PolicyType();
        policyNumber++;
        //for: number of rules 
        for (int j = 0; j < rules; j++) {
            policy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().add(generateRule());
        }
        policy.setPolicyId("P" + policyNumber);
        policy.setTarget(getRandomTarget());
        policy.setRuleCombiningAlgId(getRandomRCA());

        if (polOblNum > 0) {
            policy.setObligationExpressions(getNewObligation());
            polOblNum--;
        }

        if (polAdvNum > 0) {
            policy.setAdviceExpressions(getNewAdvice());
            polAdvNum--;
        }

        obj = new ObjectFactory();
        jaxb_policy = obj.createPolicy(policy);

        return jaxb_policy;
    }

    public RuleType generateRule() throws Exception {
        r = new RuleType();
        ruleNumber++;
        r.setRuleId("R" + ruleNumber);
        r.setEffect(getRandomEffect());
        trg = getRandomTarget();
        r.setTarget(trg);

        /*if (trg == null || rv.nextBoolean()) {
                        r.setCondition(getRandomCondition());
                    }*/
        if (cndNum > 0) {
            r.setCondition(getNewCondition());
            cndNum--;
        }

        if (rulOblNum > 0) {
            r.setObligationExpressions(getNewObligation());
            rulOblNum--;
        }

        if (rulAdvNum > 0) {
            r.setAdviceExpressions(getNewAdvice());
            rulAdvNum--;
        }
        return r;
    }

    public void initializePolicyRuleMap(PolicySetType policySet, XACML3Analyzer xAnalyzer) throws Exception {

        List<Object> policySetChildren = xAnalyzer.extractChildenOfPolicyset(policySet);

        for (Object obj : policySetChildren) {
            if (obj instanceof PolicyType) {
                int count = policyRuleMap.containsKey(xAnalyzer.getRulesOfPolicy((PolicyType) obj).size()) ? policyRuleMap.get(xAnalyzer.getRulesOfPolicy((PolicyType) obj).size()) : 0;
                policyRuleMap.put(xAnalyzer.getRulesOfPolicy((PolicyType) obj).size(), count + 1);
            } else if (obj instanceof PolicySetType) {
                initializePolicyRuleMap((PolicySetType) obj, xAnalyzer);
            }

        }
    }

    public void initializePolicySetMap(PolicySetType policySet, XACML3Analyzer xAnalyzer) throws Exception {
        int ps_count = 0;
        List<Object> policySetChildren = xAnalyzer.extractChildenOfPolicyset(policySet);

        for (Object obj : policySetChildren) {
            if (obj instanceof PolicySetType) {
                ps_count++;
                initializePolicySetMap((PolicySetType) obj, xAnalyzer);
            }
        }
        int count = policySetMap.containsKey(ps_count) ? policySetMap.get(ps_count) : 0;
        policySetMap.put(ps_count, count + 1);

    }

    public void initializePolicySetPolicyMap(PolicySetType policySet, XACML3Analyzer xAnalyzer) throws Exception {
        int p_count = 0;
        List<Object> policySetChildren = xAnalyzer.extractChildenOfPolicyset(policySet);

        for (Object obj : policySetChildren) {
            if (obj instanceof PolicyType) {
                p_count++;
            } else if (obj instanceof PolicySetType) {
                initializePolicySetPolicyMap((PolicySetType) obj, xAnalyzer);
            }
        }
        int count = policySetPolicyMap.containsKey(p_count) ? policySetPolicyMap.get(p_count) : 0;
        policySetPolicyMap.put(p_count, count + 1);
    }

    public void printStats() throws IOException{
        BufferedWriter bw = new BufferedWriter(new FileWriter("policySetMap"+policyset.getPolicySetId()+".txt", true));
        BufferedWriter bw2 = new BufferedWriter(new FileWriter("policySetPolicyMap"+policyset.getPolicySetId()+".txt", true));
        BufferedWriter bw3 = new BufferedWriter(new FileWriter("policyRuleMap"+policyset.getPolicySetId()+".txt", true));
                
        for (Map.Entry<Integer, Integer> entry : policySetMap.entrySet()) {
                bw.write(String.valueOf(entry.getKey()));
                bw.write("\t");
                bw.write(String.valueOf(entry.getValue()));
                bw.newLine();
                bw.flush();
            }    
            for (Map.Entry<Integer, Integer> entry : policySetPolicyMap.entrySet()) {
                bw2.write(String.valueOf(entry.getKey()));
                bw2.write("\t");
                bw2.write(String.valueOf(entry.getValue()));
                bw2.newLine();
                bw2.flush();
            }    
            
            for (Map.Entry<Integer, Integer> entry : policyRuleMap.entrySet()) {
                bw3.write(String.valueOf(entry.getKey()));
                bw3.write("\t");
                bw3.write(String.valueOf(entry.getValue()));
                bw3.newLine();
                bw3.flush();
            } 
    }
    
    public void parse(PolicySetType policySet) throws Exception {
        policyRuleMap = new HashMap<>();
        policySetMap = new HashMap<>();
        policySetPolicyMap = new HashMap<>();

        XACML3Analyzer xAnalyzer = new XACML3Analyzer(policySet);

        initializePolicyRuleMap(policySet, xAnalyzer);
        initializePolicySetMap(policySet, xAnalyzer);
        initializePolicySetPolicyMap(policySet, xAnalyzer);

        //printStats();
        
        //-------------
        int count;

        targetAnyOfMap = new HashMap<>();
        List<TargetType> totalTarget = xAnalyzer.getTotalTargets(policySet);

        for (TargetType trg : totalTarget) {
            count = targetAnyOfMap.containsKey(trg.getAnyOf().size()) ? targetAnyOfMap.get(trg.getAnyOf().size()) : 0;
            targetAnyOfMap.put(trg.getAnyOf().size(), count + 1);
        }

        //-------------
        anyOfAllOfMap = new HashMap<>();
        List<AnyOfType> totalAnyOf = xAnalyzer.getTotalAnyOf();

        for (AnyOfType aot : totalAnyOf) {
            count = anyOfAllOfMap.containsKey(aot.getAllOf().size()) ? anyOfAllOfMap.get(aot.getAllOf().size()) : 0;
            anyOfAllOfMap.put(aot.getAllOf().size(), count + 1);
        }
        //-------------
        allOfMatchMap = new HashMap<>();
        List<AllOfType> totalAllof = new ArrayList<>();

        for (AnyOfType aot : totalAnyOf) {
            totalAllof.addAll(aot.getAllOf());
        }

        for (AllOfType alot : totalAllof) {
            count = allOfMatchMap.containsKey(alot.getMatch().size()) ? allOfMatchMap.get(alot.getMatch().size()) : 0;
            allOfMatchMap.put(alot.getMatch().size(), count + 1);
        }
        //------------
        attrIDValFrq = new HashMap<String, List<Integer>>();

        attrVals.entrySet().forEach((entry) -> {
            temp = new ArrayList();
            temp.add(0, entry.getValue().size());
            temp.add(1, numberOfAttrId.get(entry.getKey()));
            attrIDValFrq.put(entry.getKey(), temp);
        });
    }

    public Map<Integer, Integer> scale(Map<Integer, Integer> inMap, int n) {

        Map<Integer, Integer> outMap = new HashMap<>();

        int numberOfElements = 0;
        for (Map.Entry<Integer, Integer> entry : inMap.entrySet()) {
            numberOfElements += entry.getValue();
        }

        int ruleNum;
        Random r;
        int count, newCount;
        double alpha = 0.1;
        double val;
        for (Map.Entry<Integer, Integer> entry : inMap.entrySet()) {
            //outMap.put(entry.getKey(), (int) ((entry.getValue() / (double) numberOfElements) * n));

            r = new Random();
            count = (int) Math.ceil((entry.getValue() / (double) numberOfElements) * n);
            int u = entry.getKey();
            while (count > 0) {
                val = r.nextGaussian() * alpha * u + u;
                ruleNum = (int) Math.round(val);
                newCount = outMap.containsKey(ruleNum) ? outMap.get(ruleNum) : 0;
                outMap.put(ruleNum, newCount + 1);
                count--;
            }
        }

        return outMap;
    }

    public Map<Integer, Integer> scale_Zipf(Map<Integer, Integer> inMap, int n) {

        Map<Integer, Integer> outMap = new HashMap<>();

        int numberOfElements = 0;
        for (Map.Entry<Integer, Integer> entry : inMap.entrySet()) {
            numberOfElements += entry.getValue();
        }

        ZipfGenerator zipf = new ZipfGenerator(9, 0.1);

        int count, newCount, ruleNum;
        double val;
        for (Map.Entry<Integer, Integer> entry : inMap.entrySet()) {
            count = (int) Math.ceil((entry.getValue() / (double) numberOfElements) * n);
            int u = entry.getKey();
            while (count > 0) {
                val = (((zipf.next() - 1) / 8.0) * 2 - 1) * 0.1 * u + u;
                ruleNum = (int) Math.round(val);
                newCount = outMap.containsKey(ruleNum) ? outMap.get(ruleNum) : 0;
                outMap.put(ruleNum, newCount + 1);
                count--;
            }
        }

        return outMap;
    }

    public int getNumberOfGeneratedConditions() {
        ConditionMapper cndMapper = ConditionMapper.getInstance();
        int inputNumberOfConditions = cndMapper.getNumberOfConditions();
        int targetNumberOfConditions = (int) ((inputNumberOfConditions / (double) ruleSize) * ruleTargetSize);

        /*System.out.printf("cnd avalie: %s%n", inputNumberOfConditions);
        System.out.printf("cnd kol: %s%n", inputNumberOfRules);
        System.out.printf("cnd tolidi: %s%n", targetNumberOfConditions);*/
        return targetNumberOfConditions;
    }

    public int getNumberOfGeneratedObligations() {
        ObligationMapper oblMapper = ObligationMapper.getInstance();
        int inputNumberOfObligations = oblMapper.getNumberOfObligationtions();
        int targetNumberOfObligations = (int) ((inputNumberOfObligations / (double) ruleSize + policySize + policySetSize) * (ruleTargetSize + policyTargetSize + policySetTargetSize));

        return targetNumberOfObligations;

    }

    public int getNumberOfGeneratedAdvices() {
        AdviceMapper advMapper = AdviceMapper.getInstance();
        int inputNumberOfAdvices = advMapper.getNumberOfAdvices();
        int targetNumberOfAdvices = (int) ((inputNumberOfAdvices / (double) ruleSize + policySize + policySetSize) * (ruleTargetSize + policyTargetSize + policySetTargetSize));

        return targetNumberOfAdvices;
    }

    public List distributeRulesInPolicies() {
        List<Integer> rulesInPolicy = new ArrayList<>();
        Random rn = new Random();
        int total = 0, r = 0;
        while (total < ruleTargetSize) {
            r = minRules + rn.nextInt(maxRules - minRules + 1);
            rulesInPolicy.add(r);
            total += r;
        }
        int lastPolicy = ruleTargetSize - (total - r);
        rulesInPolicy.add(lastPolicy);

        numberOfPolicies = rulesInPolicy.size();

        return rulesInPolicy;
    }

    public String getRandomRCA() {
        RuleCombiningAlgorithmMapper rca = RuleCombiningAlgorithmMapper.getInstance();
        List<String> ruleCA = rca.getListOfRuleCombiningAlgorithms();

        Random rn = new Random();
        int r = rn.nextInt(ruleCA.size());
        String ruleCombiningAlgorithm = ruleCA.get(r);
        ruleCombiningAlgorithm = ruleCombiningAlgorithm.replace('_', '-');

        return ruleCombiningAlgorithm;
    }

    public String getRandomPCA() {
        PolicyCombiningAlgorithmMapper pca = PolicyCombiningAlgorithmMapper.getInstance();
        List<String> policyCA = pca.getListOfPolicyCombiningAlgorithms();

        Random rn = new Random();
        int r = rn.nextInt(policyCA.size());
        String policyCombiningAlgorithm = policyCA.get(r);
        policyCombiningAlgorithm = policyCombiningAlgorithm.replace('_', '-');

        return policyCombiningAlgorithm;
    }

    public EffectType getRandomEffect() {
        ruleEffectMapper rem = ruleEffectMapper.getInstance();
        List prc = rem.getPercentageOfEachEffect();

        Random rn = new Random();
        double r = rn.nextDouble();

        if (r < (double) prc.get(0)) {
            return EffectType.PERMIT;
        } else {
            return EffectType.DENY;
        }
    }

    public Boolean getMustBePresent() {
        Double t = 0.0, f = 0.0;
        for (MatchType m : matchList) {
            if (m.getAttributeDesignator().isMustBePresent()) {
                t++;
            } else if (!m.getAttributeDesignator().isMustBePresent()) {
                f++;
            }
        }
        //List[0]=true frq, List[1]=false frq
        List<Double> mbpPrc = new ArrayList<>();
        mbpPrc.add(0, t / (t + f));
        mbpPrc.add(1, f / (t + f));

        Random r = new Random();
        double rv = r.nextDouble();

        if (rv < mbpPrc.get(0)) {
            return true;
        } else {
            return false;
        }
    }

    public void initializeGeneratedTargetList() {
        TargetType trg = null;
        //List<TargetType> trgList = new ArrayList<TargetType>();

        for (Map.Entry<Integer, Integer> entry : targetAnyOfMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                trg = new TargetType();
                for (int j = 0; j < entry.getKey(); j++) {
                    AnyOfType any = getAnyOf();
                    trg.getAnyOf().add(any);
                }
                generatedTargetList.add(trg);
            }
        }

    }

    public TargetType getRandomTarget() {
        /*    TargetType trg=new TargetType();
        
        AnyOfType any;
        List<AnyOfType> anyOfList = new ArrayList<>();
        
        AllOfType all; 
        List<AllOfType> allOfList = new ArrayList<>();
        
        MatchType match;
        List<MatchType> matchList = new ArrayList<>();
        
        for (int i=0; i<getRandomNumberOfAnyOfsInTarget(); i++) {
            allOfList.clear();
            any = new AnyOfType();
            for (int j=0; j<getRandomNumberOfAllOfsInAnyOf(); j++) {
                matchList.clear();
                all = new AllOfType();
                for (int k=0; k<getRandomNumberOfMatchesInAllOf(); k++){
                    all.getMatch().add(getRandomMatch());
                    //match=getRandomMatch();
                    //matchList.add(match);
                }
                any.getAllOf().add(all);
                //all.getMatch().addAll(matchList);
                //allOfList.add(all);
            }
            trg.getAnyOf().add(any);
            //any.getAllOf().addAll(allOfList);
            //anyOfList.add(any);
        }                
        //trg.getAnyOf().addAll(anyOfList);
         */
 /*List<Integer> keysAsArray = new ArrayList<Integer>(targetAnyOfMap.keySet());
        int numberOfAnyOfs = keysAsArray.get(r.nextInt(keysAsArray.size()));
        if (targetAnyOfMap.get(numberOfAnyOfs) > 0){
            
            if (targetAnyOfMap.get(numberOfAnyOfs) != 1)
                targetAnyOfMap.put(numberOfAnyOfs, targetAnyOfMap.get(numberOfAnyOfs)-1);
            else if (targetAnyOfMap.get(numberOfAnyOfs) == 1)
                targetAnyOfMap.remove(numberOfAnyOfs);
            
            trg=new TargetType();
            AnyOfType any = null;
            for(int i=0; i<numberOfAnyOfs; i++)
            {
               any = getAnyOf();
               trg.getAnyOf().add(any);
            }
        }*/
        Random r = new Random();
        Collections.shuffle(generatedTargetList);
        TargetType t = generatedTargetList.get(r.nextInt(generatedTargetList.size()));
        //generatedTargetList.remove(t);
        return t;
    }

    public void initializeGeneratedAnyOfList() {
        AnyOfType any = null;
        //List<AnyOfType> anyOfList = new ArrayList<AnyOfType>();

        for (Map.Entry<Integer, Integer> entry : anyOfAllOfMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                any = new AnyOfType();
                for (int j = 0; j < entry.getKey(); j++) {
                    AllOfType all = getAllOf();
                    any.getAllOf().add(all);
                }
                generatedAnyOfList.add(any);
            }
        }

    }

    public AnyOfType getAnyOf() {
        /*AnyOfType any=null;
            
        List<Integer> keysAsArray = new ArrayList<Integer>(anyOfAllOfMap.keySet());
        Random r = new Random();
        int numberOfAllOfs = keysAsArray.get(r.nextInt(keysAsArray.size()));
        if (anyOfAllOfMap.get(numberOfAllOfs) > 0){
            
            if (anyOfAllOfMap.get(numberOfAllOfs) != 1)
                anyOfAllOfMap.put(numberOfAllOfs, anyOfAllOfMap.get(numberOfAllOfs)-1);
            else if (anyOfAllOfMap.get(numberOfAllOfs) == 1)
                anyOfAllOfMap.remove(numberOfAllOfs);
            
            any=new AnyOfType();
            AllOfType all = null;
            for(int i=0; i<numberOfAllOfs; i++)
            {
               all = getAllOf();
               any.getAllOf().add(all);
            }
        }
        return any;*/
        Random r = new Random();
        Collections.shuffle(generatedAnyOfList);
        AnyOfType a = generatedAnyOfList.get(r.nextInt(generatedAnyOfList.size()));
        //generatedAnyOfList.remove(a);
        return a;
    }

    public void initializeGeneratedAllOfList() {
        AllOfType all = null;
        //List<AllOfType> allOfList = new ArrayList<AllOfType>();

        for (Map.Entry<Integer, Integer> entry : allOfMatchMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                all = new AllOfType();
                for (int j = 0; j < entry.getKey(); j++) {
                    MatchType match = getNewMatch();
                    all.getMatch().add(match);
                }
                generatedAllOfList.add(all);
            }
        }

    }

    public AllOfType getAllOf() {
        /*if (allOfMatchMap.isEmpty())
            return null;
        else{
        AllOfType all=null;
            
        List<Integer> keys = new ArrayList<Integer>(allOfMatchMap.keySet());
        Random r = new Random();
        int numberOfMatches = keys.get(r.nextInt(keys.size()));
        if (allOfMatchMap.get(numberOfMatches) > 0){
            
            all=new AllOfType();
            MatchType mtch = null;
            for(int i=0; i<numberOfMatches; i++)
            {
               mtch = getNewMatch();
               scaledAttrIDValFrq.get(mtch.getAttributeDesignator().getAttributeId()).set(1, attrIDValFrq.get(mtch.getAttributeDesignator().getAttributeId()).get(1)-1);
               if (scaledAttrIDValFrq.get(mtch.getAttributeDesignator().getAttributeId()).get(1) == 0)
                   scaledAttrIDValFrq.remove(mtch.getAttributeDesignator().getAttributeId());
               all.getMatch().add(mtch);
            }
            allOfMatchMap.put(numberOfMatches, allOfMatchMap.get(numberOfMatches)-1);
            if (allOfMatchMap.get(numberOfMatches)==0)
                allOfMatchMap.remove(numberOfMatches);
        }
        return all;*/
        Random r = new Random();
        Collections.shuffle(generatedAllOfList);
        AllOfType a = generatedAllOfList.get(r.nextInt(generatedAllOfList.size()));
        //generatedAllOfList.remove(a);
        return a;
    }

    // <Key: "Attribute ID", Value: "List<AttributeValueType>">
    public Map<String, List<AttributeValueType>> generateNewAttrValsMap(Map<String, List<AttributeValueType>> inMap) {
        Map<String, List<AttributeValueType>> outMap = new HashMap<>();
        outMap = inMap;

        for (Map.Entry<String, List<AttributeValueType>> entry : inMap.entrySet()) {
            //String dType = entry.getValue().get(0).getDataType();

            String dType = entry.getValue().get(0).getDataType();

            if (dType.toLowerCase().contains("integer")) {
                List<Integer> newList = new ArrayList<Integer>(entry.getValue().size());
                for (AttributeValueType myVal : entry.getValue()) {
                    newList.add((int) myVal.getContent().get(0));
                }

                int max = Collections.max(newList);
                int min = Collections.min(newList);

                // new number of intervals
                int v1 = scaledAttrIDValFrq.get(entry.getKey()).get(0) - entry.getValue().size();
                Random r = new Random();
                //int rv = r.nextInt() + entry.getValue().size(); 

                int rv2;
                AttributeValueType attrVal;

                for (int i = 1; i <= v1; i++) {
                    rv2 = min + r.nextInt(max - min);
                    attrVal = new AttributeValueType();
                    //attrVal.getContent().add(rv2);
                    attrVal.getContent().add(0, rv2);

                    attrVal.setDataType(dType);

                    //if (outMap.get(entry.getKey()) == null)
                    //    outMap.put(entry.getKey(), new ArrayList<AttributeValueType>());
                    outMap.get(entry.getKey()).add(attrVal);
                }
            } else if (dType.toLowerCase().contains("double")) {
                List<Double> newList = new ArrayList<Double>(entry.getValue().size());
                for (AttributeValueType myVal : entry.getValue()) {
                    newList.add((double) myVal.getContent().get(0));
                }

                double max = Collections.max(newList);
                double min = Collections.min(newList);

                Random r = new Random();
                // new number of intervals
                int v1 = scaledAttrIDValFrq.get(entry.getKey()).get(0) - entry.getValue().size();
                //int rv = r.nextInt() + entry.getValue().size(); //Number of intervals
                double rv2;
                AttributeValueType attrVal;

                for (int i = 1; i <= v1; i++) {
                    rv2 = min + r.nextDouble() * (max - min);
                    attrVal = new AttributeValueType();
                    //attrVal.getContent().add(rv2);
                    attrVal.getContent().add(0, rv2);

                    attrVal.setDataType(dType);

                    //if (outMap.get(entry.getKey()) == null)
                    //    outMap.put(entry.getKey(), new ArrayList<AttributeValueType>());
                    outMap.get(entry.getKey()).add(attrVal);
                }
            } else if (dType.toLowerCase().contains("date")) {
                List<Date> newList = new ArrayList<Date>(entry.getValue().size());
                for (AttributeValueType myVal : entry.getValue()) {
                    newList.add((Date) myVal.getContent().get(0));
                }

                Date max = Collections.max(newList);
                Date min = Collections.min(newList);
                long diff = max.getTime() - min.getTime();
                BigDecimal bd = new BigDecimal(diff);

                Random r = new Random();
                // new number of intervals
                int v1 = scaledAttrIDValFrq.get(entry.getKey()).get(0) - entry.getValue().size();
                //int rv = r.nextInt() + entry.getValue().size(); //Number of intervals
                long lgv;
                AttributeValueType attrVal;

                for (int i = 1; i <= v1; i++) {
                    bd = bd.multiply(BigDecimal.valueOf(r.nextDouble()));
                    lgv = min.getTime() + bd.longValue(); // here (Convert long to Date)
                    Date d = new Date(lgv);
                    attrVal = new AttributeValueType();
                    //attrVal.getContent().add(d);
                    attrVal.getContent().add(0, d);

                    attrVal.setDataType(dType);

                    //if (outMap.get(entry.getKey()) == null)
                    //    outMap.put(entry.getKey(), new ArrayList<AttributeValueType>());
                    outMap.get(entry.getKey()).add(attrVal);
                }
            }

            if (dType.toLowerCase().contains("string")) {
                List<String> newList = new ArrayList<String>(entry.getValue().size());

                for (AttributeValueType myVal : entry.getValue()) {
                    newList.add(String.valueOf(myVal.getContent().get(0)));
                }

                int v1 = scaledAttrIDValFrq.get(entry.getKey()).get(0) - entry.getValue().size();

                AttributeValueType attrVal;
                Random r = new Random();
                for (int i = 0; i < v1; i++) {
                    attrVal = new AttributeValueType();
                    //attrVal.getContent().add(newList.get(r.nextInt(newList.size())) + String.valueOf(i));
                    attrVal.getContent().add(0, newList.get(r.nextInt(newList.size())) + String.valueOf(i));

                    attrVal.setDataType(dType);

                    //if (outMap.get(entry.getKey()) == null)
                    //    outMap.put(entry.getKey(), new ArrayList<AttributeValueType>());
                    outMap.get(entry.getKey()).add(attrVal);
                }
            }

        }

        return outMap;
    }

    public void initializeGeneratedMatchList() {
        MatchType m;
        String dType;
        AttributeDesignatorType attrDesg;
        Random r = new Random();
        //List<MatchType> matchList = new ArrayList<MatchType>();

        for (Map.Entry<String, List<Integer>> entry : scaledAttrIDValFrq.entrySet()) {
            for (int i = 0; i < entry.getValue().get(1); i++) {
                m = new MatchType();
                //AttributeDesignator
                attrDesg = new AttributeDesignatorType();
                attrDesg.setAttributeId(entry.getKey());
                //DataType
                dType = attributeIdDataTypeMap.get(entry.getKey());
                attrDesg.setDataType(dType);
                attrDesg.setMustBePresent(getMustBePresent());
                m.setAttributeDesignator(attrDesg);

                //AttributeValue
                m.setAttributeValue(newAttrVals.get(entry.getKey()).get(i % entry.getValue().get(0)));

                if (dType.contains("string")) {
                    m.setMatchId(stringOperators.get(r.nextInt(stringOperators.size())));
                } else if (dType.contains("integer")) {
                    m.setMatchId(integerOperators.get(r.nextInt(integerOperators.size())));
                } else if (dType.contains("double")) {
                    m.setMatchId(doubleOperators.get(r.nextInt(doubleOperators.size())));
                } else if (dType.contains("boolean")) {
                    m.setMatchId(booleanOperators.get(r.nextInt(booleanOperators.size())));
                } else if (dType.contains("date")) {
                    m.setMatchId(dateOperators.get(r.nextInt(dateOperators.size())));
                }

                generatedMatchList.add(m);
            }
        }

    }

    public MatchType getNewMatch() {
        /*if (scaledAttrIDValFrq.isEmpty())
            return null;
        else
        {
        MatchType m = new MatchType();
        
        //AttributeDesignator
        AttributeDesignatorType attrDesg = new AttributeDesignatorType();
        //Randomly selecting AttributeID
        Random r = new Random();
        List<String> keys = new ArrayList<String>(scaledAttrIDValFrq.keySet()); 
        String attrID = keys.get(r.nextInt(keys.size()));

        attrDesg.setAttributeId(attrID);
        //DataType
        String dType = attributeIdDataTypeMap.get(attrID);
        attrDesg.setDataType(dType);
        attrDesg.setMustBePresent(true);
        
        m.setAttributeDesignator(attrDesg);
        
        //AttributeValue
        AttributeValueType attrVal = new AttributeValueType();
        int rnd = r.nextInt(newAttrVals.get(attrID).size());
        attrVal.getContent().add(newAttrVals.get(attrID).get(rnd));
        //attrVals.get(attrID).remove(rnd);
        
        //DataType same as AttributeDesignator
        attrVal.setDataType(dType);
        
        m.setAttributeValue(attrVal);
        
        
        if (dType.contains("string"))
            m.setMatchId(stringOperators.get(r.nextInt(stringOperators.size())));       
        if (dType.contains("integer"))
            m.setMatchId(integerOperators.get(r.nextInt(integerOperators.size())));
        if (dType.contains("double"))
            m.setMatchId(doubleOperators.get(r.nextInt(doubleOperators.size())));
        if (dType.contains("boolean"))
            m.setMatchId(booleanOperators.get(r.nextInt(booleanOperators.size())));
        if (dType.contains("date"))
            m.setMatchId(dateOperators.get(r.nextInt(dateOperators.size())));
        
        attrIDValFrq.get(attrID).set(1, attrIDValFrq.get(attrID).get(1)-1);
        
        if (attrIDValFrq.get(attrID).get(1) == 0)
            attrIDsAskeys.remove(attrID);
        
        
        return m;
        }*/

        Random r = new Random();
        Collections.shuffle(generatedMatchList);
        MatchType mtch = generatedMatchList.get(r.nextInt(generatedMatchList.size()));
        //generatedMatchList.remove(mtch);
        return mtch;
    }

    public int getRandomNumberOfAnyOfsInTarget() {
        Random rn = new Random();
        int r = minAnyOf + rn.nextInt(maxAnyOf - minAnyOf + 1);
        return r;
    }

    public int getRandomNumberOfAllOfsInAnyOf() {
        Random rn = new Random();
        int r = minAllOf + rn.nextInt(maxAllOf - minAllOf + 1);
        return r;
    }

    public int getRandomNumberOfMatchesInAllOf() {
        Random rn = new Random();
        int r = minMatch + rn.nextInt(maxMatch - minMatch + 1);
        return r;
    }

    /*   public MatchType getRandomMatch() {
        MatchType match = new MatchType();
        Random rn = new Random();
        int random = 0;

        AttributeDesignatorType attrDesig = new AttributeDesignatorType();

        random = rn.nextInt(attributeIDs.size());
        attrDesig.setAttributeId(attributeIDs.get(random));

        List<String> dataType = new ArrayList<>(matchIDs.keySet());
        random = rn.nextInt(dataType.size());
        attrDesig.setDataType(dataType.get(random));

        random = rn.nextInt(2);
        if (random == 0) {
            attrDesig.setMustBePresent(false);
        } else {
            attrDesig.setMustBePresent(true);
        }

        match.setAttributeDesignator(attrDesig);

        List<AttributeValueType> vals = attrVals.get(match.getAttributeDesignator().getAttributeId());
        random = rn.nextInt(vals.size());
        match.setAttributeValue(vals.get(random));

        List<String> matchIds = matchIDs.get(match.getAttributeDesignator().getDataType());
        random = rn.nextInt(matchIds.size());
        match.setMatchId(matchIds.get(random));

        return match;
    }
     */
 /*    public ConditionType getRandomCondition() {
        ConditionMapper cMapper = ConditionMapper.getInstance();
        List<ConditionType> cndList = cMapper.getListOfConditions();

        if (!cndList.isEmpty()) {
            Random rn = new Random();
            return cndList.get(rn.nextInt(cndList.size()));
        } else {
            return null;
        }
    }
     */
    public void initializeJAXBElementList() {
        JAXBElementList = new ArrayList<>();

        ConditionMapper cMapper = ConditionMapper.getInstance();
        List<ConditionType> cndList = cMapper.getListOfConditions();
        for (ConditionType cnd : cndList) {
            if (cnd != null) {
                JAXBElementList.add(cnd.getExpression());
            }
        }

        ObligationMapper oblMapper = ObligationMapper.getInstance();
        List<ObligationExpressionsType> oblsList = oblMapper.getListOfObligations();
        List<ObligationExpressionType> oblList = new ArrayList<>();
        List<AttributeAssignmentExpressionType> attrAssExpressionList = new ArrayList<>();
        for (ObligationExpressionsType obls : oblsList) {
            if (obls != null) {
                oblList.addAll(obls.getObligationExpression());
            }
        }
        for (ObligationExpressionType obl : oblList) {
            if (obl != null) {
                attrAssExpressionList.addAll(obl.getAttributeAssignmentExpression());
            }
        }
        for (AttributeAssignmentExpressionType attrAss : attrAssExpressionList) {
            if (attrAss != null) {
                JAXBElementList.add(attrAss.getExpression());
            }
        }

        AdviceMapper advMapper = AdviceMapper.getInstance();
        List<AdviceExpressionsType> advsList = advMapper.getListOfAdvices();
        List<AdviceExpressionType> advList = new ArrayList<>();
        List<AttributeAssignmentExpressionType> attrAssExpressionList2 = new ArrayList<>();
        for (AdviceExpressionsType advs : advsList) {
            if (advs != null) {
                advList.addAll(advs.getAdviceExpression());
            }
        }
        for (AdviceExpressionType adv : advList) {
            if (adv != null) {
                attrAssExpressionList2.addAll(adv.getAttributeAssignmentExpression());
            }
        }
        for (AttributeAssignmentExpressionType attrAss : attrAssExpressionList2) {
            if (attrAss != null) {
                JAXBElementList.add(attrAss.getExpression());
            }
        }
    }

    public ConditionType getNewCondition() {
        ConditionType condition = new ConditionType();
        if (!JAXBElementList.isEmpty()) {
            Random rn = new Random();
            condition.setExpression(JAXBElementList.get(rn.nextInt(JAXBElementList.size())));
            return condition;
        } else {
            return null;
        }
    }

    /*   public ObligationExpressionsType getRandomObligation() {
        ObligationMapper oMapper = ObligationMapper.getInstance();
        List<ObligationExpressionsType> oblList = oMapper.getListOfObligations();

        if (!oblList.isEmpty()) {
            Random rn = new Random();
            return oblList.get(rn.nextInt(oblList.size()));
        } else {
            return null;
        }
    }
     */
    public ObligationExpressionsType getNewObligation() {
        ObligationExpressionsType obligations = new ObligationExpressionsType();
        ObligationExpressionType obligation = new ObligationExpressionType();
        AttributeAssignmentExpressionType attrAss = new AttributeAssignmentExpressionType();
        if (!JAXBElementList.isEmpty()) {
            Random rn = new Random();
            attrAss.setExpression(JAXBElementList.get(rn.nextInt(JAXBElementList.size())));
            obligation.getAttributeAssignmentExpression().add(0, attrAss);
            obligations.getObligationExpression().add(0, obligation);
            return obligations;
        } else {
            return null;
        }
    }

    public AdviceExpressionsType getNewAdvice() {
        AdviceExpressionsType advices = new AdviceExpressionsType();
        AdviceExpressionType advice = new AdviceExpressionType();
        AttributeAssignmentExpressionType attrAss = new AttributeAssignmentExpressionType();
        if (!JAXBElementList.isEmpty()) {
            Random rn = new Random();
            attrAss.setExpression(JAXBElementList.get(rn.nextInt(JAXBElementList.size())));
            advice.getAttributeAssignmentExpression().add(0, attrAss);
            advices.getAdviceExpression().add(0, advice);
            return advices;
        } else {
            return null;
        }
    }

    public void printGeneratedPolicysetStats() throws Exception {

        System.out.println("Min number of rules in each policy: " + minRules);
        System.out.println("Max number of rules in each policy: " + maxRules);
        System.out.println();
        List<Integer> rulesInPolicy = distributeRulesInPolicies();
        System.out.println("Total number of generated policies:");
        System.out.println(rulesInPolicy.size());
        System.out.println();
        System.out.println("Number of rules in each generated policy:");
        for (int i = 0; i < rulesInPolicy.size(); i++) {
            System.out.printf("P%s: %s%n", i + 1, rulesInPolicy.get(i));
        }

        ruleEffectMapper reMapper = ruleEffectMapper.getInstance();
        reMapper.clearAll();

        PolicySetTranslator pst = new PolicySetTranslator(policyset);
        pst.parse();

    }

    public void delete() {
        attributeIDs = null;
        matchIDs = null;
        attrVals = null;
        newAttrVals = null;
        attrIDsAskeys = null;
        attributeIdDataTypeMap = null;
        numberOfRulesInPolicy = null;
        policyRuleMap = null;
        targetAnyOfMap = null;
        anyOfAllOfMap = null;
        allOfMatchMap = null;
        nonEqualOperatorsAttrID = null;
        equalOperatorsAttrID = null;
        generatedTargetList = null;
        generatedAnyOfList = null;
        generatedAllOfList = null;
        generatedMatchList = null;
        attrIDValFrq = null;
        numberOfAttrId = null;
        scaledAttrIDValFrq = null;
        temp = null;
        matchList = null;
        JAXBElementList = null;
        attrVals = null;
        newAttrVals = null;
        generatedTargetList = null;
        generatedAnyOfList = null;
        generatedAllOfList = null;
        generatedMatchList = null;
    }
}
