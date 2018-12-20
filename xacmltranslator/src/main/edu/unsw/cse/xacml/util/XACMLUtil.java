/**
 * 
 */
package edu.unsw.cse.xacml.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Mohsen
 *
 */
public class XACMLUtil {
	static protected Random rand = new Random(1);
	
	/**
	 * get random number in [min, max)
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	static public int getRandomNumber(int min, int max) {
		if (max == min)
			return max;
		return rand.nextInt(max - min) + min;
	}

	public static ArrayList<Integer> getUniqueRandomList(int size, int min, int max) {
		// generate unique random values
		ArrayList<Integer> randomValues = new ArrayList<Integer>();
		int i = 0;
		while (i < size) {
			int p = XACMLUtil.getRandomNumber(min, max);
			if (!randomValues.contains(p)) {
				randomValues.add(p);
				i++;
			}
		}
		return randomValues;
	}
	
    public static File[] getXMLFileList(String dirPath) {
        File dir = new File(dirPath);   

        File[] fileList = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        });
        return fileList;
    }

}
