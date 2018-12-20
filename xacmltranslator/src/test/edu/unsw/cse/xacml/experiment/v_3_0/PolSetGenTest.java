/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unsw.cse.xacml.experiment.v_3_0;

import static edu.unsw.cse.xacml.experiment.v_3_0.PolSetGenTest.testGenerator;
import org.junit.Test;
import edu.unsw.cse.xacml.util.XACMLUtilV3;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType;
import static org.junit.Assert.assertNotNull;

/**
 *
 * @author shayan_310
 */
public class PolSetGenTest {
 
    public static void testGenerator(String xacmlFilePath, int ruleNumber) throws Exception {
        //String xacmlFileName;

        //xacmlFileName = "/Users/shayan_310/Documents/repositories/xacml/XACMLTranslator/src/test/resources/PolicySetMedicalDemo_Dhouha.xml";
        PolicySetType policysetObj = XACMLUtilV3.unmarshalPolicySetType(xacmlFilePath);
        assertNotNull(policysetObj);
        
        int start = 0;
        for (int i = 0; i < xacmlFilePath.length(); i++) {
            if (xacmlFilePath.charAt(i) == '/') // Looking for '(' position in string
            {
                start = i;
            }
        }

        System.out.println("*** Input policyset informations ***");
        System.out.println("Input file name: " + xacmlFilePath.substring(start + 1));
        XACML3Analyzer xacmlAnalyzer_inp = new XACML3Analyzer(policysetObj);
        xacmlAnalyzer_inp.printPolicysetStat();
        
        System.out.println();
        System.out.println("Please wait! xacBench is generating synthetic policyset... .");

        String xacmlFileName = "Synthetic_" + String.valueOf(ruleNumber) + "_" + xacmlFilePath.substring(start + 1);

        PolSetGen generator = new PolSetGen(policysetObj, ruleNumber, xacmlFileName);
        PolicySetType generatedPolicyset = generator.syntheticPolicySetGenerator();

        System.out.println("-------------------------------------------");
        System.out.println("Synthetic policyset Successfully generated!");
        System.out.println("-------------------------------------------");

        System.out.println("*** Output policyset informations ***");
        System.out.println("Output file name: " + xacmlFileName);
        XACML3Analyzer xacmlAnalyzer_out = new XACML3Analyzer(generatedPolicyset);
        xacmlAnalyzer_out.printPolicysetStat();

        /*    BufferedWriter bw_MPA = new BufferedWriter(new FileWriter("MPA_input.txt", true));
        for (Map.Entry<Integer, Integer> entry : xacmlAnalyzer.getMPA().entrySet()) {
                bw_MPA.write(String.valueOf(entry.getKey()));
                bw_MPA.write("\t");
                bw_MPA.write(String.valueOf(entry.getValue()));
                bw_MPA.newLine();
                bw_MPA.flush();
        }*/
 /*        BufferedWriter bw_MPA = new BufferedWriter(new FileWriter("MPA_6.txt", true));
                BufferedWriter bw_APA = new BufferedWriter(new FileWriter("APA_6.txt", true));
                BufferedWriter bw_APT = new BufferedWriter(new FileWriter("APT_6.txt", true));
                BufferedWriter bw_RPP = new BufferedWriter(new FileWriter("RPP_6.txt", true));
         */
 /*    BufferedWriter bw_TA = new BufferedWriter(new FileWriter("TA_xacml3-policyset-sli.txt", true));
        BufferedWriter bw_AA = new BufferedWriter(new FileWriter("AA_xacml3-policyset-sli.txt", true));
        BufferedWriter bw_AM = new BufferedWriter(new FileWriter("AM_xacml3-policyset-sli.txt", true));
         */
        //    List<Integer> it = new ArrayList<>(Arrays.asList(8000, 9000, 10000));
        //PolSetGen generator;
        //    xacmlAnalyzer.printPolicysetStat();
        //    generator = new PolSetGen(policysetObj, 7000, "outputDataset.xml");
        //    generator.syntheticPolicySetGenerator();

        /*  //Attr
        //Map<String, Integer> attrValFrq = new HashMap<>();
        for (Map.Entry<String, List<AttributeValueType>> entry : generator.attrVals.entrySet()) {
            for (AttributeValueType atr : entry.getValue()) {
                int count = attrValFrq.containsKey(String.valueOf(atr.getContent().get(0))) ? attrValFrq.get(String.valueOf(atr.getContent().get(0))) : 0;
                attrValFrq.put(String.valueOf(atr.getContent().get(0)), count + 1);
            }
            bw = new BufferedWriter(new FileWriter(entry.getKey().substring(entry.getKey().lastIndexOf("/")+1), true));
            for (Map.Entry<String, Integer> entry2 : attrValFrq.entrySet()) {
                bw.write(String.valueOf(entry2.getKey()));
                bw.write("\t");
                bw.write(String.valueOf(entry2.getValue()));
                bw.newLine();
                bw.flush();
                //attrValFrq = null;
            }

        }
         */
 /*    Map<Integer, List<Double>> rslt = new HashMap<Integer, List<Double>>();
     for (int i = 0; i < 100; i++) {
            xacmlAnalyzer.printPolicysetStat();
            generator = new PolSetGen(policysetObj, 5000, "outputDataset.xml");
            generator.syntheticPolicySetGenerator();

            for (Map.Entry<Integer, Integer> entry : generator.targetAnyOfMap.entrySet()) {
                if (rslt.containsKey(entry.getKey()))
                    rslt.get(entry.getKey()).add((double)entry.getValue());
                else
                rslt.put( entry.getKey(), new ArrayList<Double>(Arrays.asList((double)entry.getValue())) );
            }
            generator = null;
            System.gc();
     }
            BufferedWriter bw = new BufferedWriter(new FileWriter("APT_sli_5000.txt", true));
            CI calcCi = new CI();
            for (Map.Entry<Integer, List<Double>> entry : rslt.entrySet()) {
                double[] a = calcCi.calculateCI(entry.getValue());
                bw.write(entry.getKey() + "\t" + a[0] + "\t" + a[1]);
                bw.newLine();
                bw.flush();
                //System.out.println(entry.getKey() + "\t" + a[0] + "\t" + a[1]);
            }
         */
 /*       BufferedWriter wMPA = new BufferedWriter(new FileWriter("wMPA_Continue-a.txt", true));
        BufferedWriter wAPA = new BufferedWriter(new FileWriter("wAPA_Continue-a.txt", true));
        BufferedWriter wAPT = new BufferedWriter(new FileWriter("wAPT_Medical_CI.txt", true));

        double weightedAvg = 0.0, mpa = 0.0, apa = 0.0, apt = 0.0, rpp = 0.0;
        int a = 0, b = 0;
        List<Double> aptList = new ArrayList<>();
        CI ci = new CI();
        for (int j = 0; j < it.size(); j++) {
            mpa = 0.0;
            apa = 0.0;
            apt = 0.0;
            rpp = 0.0;
            for (int i = 0; i < 100; i++) {
                xacmlAnalyzer.printPolicysetStat();
                a = 0;
                b = 0;
                generator = new PolSetGen(policysetObj, it.get(j), "outputDataset.xml");
                generator.syntheticPolicySetGenerator();

                for (Map.Entry<Integer, Integer> entry : generator.targetAnyOfMap.entrySet()) {
                    a += entry.getKey() * entry.getValue();
                    b += entry.getValue();
                }
                weightedAvg = (double) a / b;
                aptList.add(weightedAvg);
                    apt+= weightedAvg;
                    wAPT.write(String.format("%.16f", weightedAvg));
                    wAPT.write("\t");
                    wAPT.flush();

                a = 0;
                b = 0;
                for (Map.Entry<Integer, Integer> entry : generator.anyOfAllOfMap.entrySet()) {
                    a += entry.getKey() * entry.getValue();
                    b += entry.getValue();
                }
                weightedAvg = (double) a / b;
                apa += weightedAvg;
                    wAPA.write(String.format("%.16f", weightedAvg));
                    wAPA.write("\t");
                    wAPA.flush();

                a = 0;
                b = 0;
                for (Map.Entry<Integer, Integer> entry : generator.allOfMatchMap.entrySet()) {
                    a += entry.getKey() * entry.getValue();
                    b += entry.getValue();
                }
                weightedAvg = (double) a / b;
                mpa += weightedAvg;
                    wMPA.write(String.format("%.16f", weightedAvg));
                    wMPA.write("\t");
                    wMPA.flush();

                generator = null;
                System.gc();
            }
                wAPA.write(String.valueOf(it.get(j)));
                wAPA.write("\t");
                wAPA.write(String.format("%.16f", apa / 100.0));
                wAPA.newLine();
                wAPA.flush();

                wAPT.write(String.valueOf(it.get(j)));
                wAPT.write("\t");
                wAPT.write(String.format("%.16f", ci.calculateCI(aptList)[0]));
                wAPT.write("\t");
                wAPT.write(String.format("%.16f", ci.calculateCI(aptList)[1]));
                wAPT.newLine();
                wAPT.flush();

                wMPA.write(String.valueOf(it.get(j)));
                wMPA.write("\t");
                wMPA.write(String.format("%.16f", mpa / 100.0));
                wMPA.newLine();
                wMPA.flush();
            
        }
            wMPA.close();
            wAPA.close();
            wAPT.close();
         */
 /*       BufferedWriter bw = new BufferedWriter(new FileWriter("elapsedTime_sli-2.txt", true));
        long startTime, elapsedTime, stopTime;
        double res;
        for (int i = 0; i < it.size(); i++) {
            elapsedTime = 0;
            for (int j = 0; j < 100; j++) {
                xacmlAnalyzer.printPolicysetStat();
                startTime = System.currentTimeMillis();
                generator = new PolSetGen(policysetObj, it.get(i), "outputDataset.xml");
                generator.syntheticPolicySetGenerator();
                stopTime = System.currentTimeMillis();
                elapsedTime += (stopTime - startTime);
                generator = null;
                System.gc();
                //System.out.printf("Number of rules: %s\t%s%n", 8000, elapsedTime);
            }
            bw.write(String.valueOf(it.get(i)));
            bw.write("\t");
            res = ((double) elapsedTime / 100.0);
            bw.write(String.valueOf(res));
            bw.newLine();
            bw.flush();
        }
        bw.close(); */
    }
}