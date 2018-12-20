/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unsw.cse.xacml.experiment.v_3_0;

import java.util.Random;

/**
 *
 * @author shayan_310
 */
public class ZipfGenerator {

    private Random rnd = new Random(System.currentTimeMillis());
    private int size;
    private double skew;
    private double bottom = 0;

    public ZipfGenerator(int size, double skew) {
        this.size = size;
        this.skew = skew;

        for (int i = 1; i <= size; i++) {
            this.bottom += (1 / Math.pow(i, this.skew));
        }
    }

    // the next() method returns an random rank id.
    // The frequency of returned rank ids are follows Zipf distribution.
    public int next() {
        int rank;
        double friquency = 0;
        double dice;

        rank = rnd.nextInt(size) + 1;
        friquency = (1.0d / Math.pow(rank, this.skew)) / this.bottom;
        dice = rnd.nextDouble();

        while (!(dice < friquency)) {
            rank = rnd.nextInt(size);
            friquency = (1.0d / Math.pow(rank, this.skew)) / this.bottom;
            dice = rnd.nextDouble();
        }

        return rank;
    }

    // This method returns a probability that the given rank occurs.
    public double getProbability(int rank) {
        return (1.0d / Math.pow(rank, this.skew)) / this.bottom;
    }

/*    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("usage: ./zipf size skew");
            System.exit(-1);
        }

        ZipfGenerator zipf = new ZipfGenerator(Integer.valueOf(args[0]), Double.valueOf(args[1]));
        for (int i = 1; i <= 10; i++) {
            System.out.println(i + " " + zipf.getProbability(i));
        }
        //use size = 10 and skew = 2 for testing below
        int hist[] = new int[12];
        for (int i = 0; i < 12; i++) {
            hist[i] = 0;
        }
        System.out.println("Testing the probability distribution:");
        int sum = 0;
        for (int i = 1; i <= 1000000; i++) {
            hist[zipf.next()]++;
        }
        for (int i = 0; i < 12; i++) {
            System.out.println(i + " " + hist[i] / 1000000.0);
        }
    } */
}
