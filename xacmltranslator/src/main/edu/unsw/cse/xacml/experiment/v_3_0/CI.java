/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unsw.cse.xacml.experiment.v_3_0;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author shayan_310
 */
public class CI {
    public double[] calculateCI(List<Double> data) { 

        while (data.size() < 100)
            data.add(0.0);
        // first pass: read in data, compute sample mean
        int num = 0;
        double dataSum = 0.0;
        while (num<data.size()) {
            dataSum  += data.get(num);
            num++;
        }
        double ave = dataSum / num;


        double variance1 = 0.0;
        for (int i = 0; i < num; i++) {
            variance1 += (data.get(i) - ave) * (data.get(i) - ave);
        }
        double variance = variance1 / (num - 1);
        double standardDeviation= Math.sqrt(variance);
        double ci = 1.96 * (standardDeviation / Math.sqrt(num));
        
        // print results
        //System.out.println(ave + "\t" + ci);
        double[] a = {ave, ci};
        return a;

    }
}
