/**
 * 
 */
package edu.unsw.cse.xacml.translator.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Mohsen
 *
 */
public class AttributeMapper {

	private static AttributeMapper instance = null;

	/**
	 * Collect all attribute values for each attribute identifier: List<attribute_id, List<attribute_values>>
	 */
	private Map<String, List<String>> attributeValues;

	protected AttributeMapper() {
		attributeValues = new HashMap<String, List<String>>();
	}

	public static AttributeMapper getInstance() {
		if(instance == null) {
			instance = new AttributeMapper();
		}
		return instance;
	}

	public void printAttributes() {
		Iterator<String> it = attributeValues.keySet().iterator();

		while (it.hasNext()) {
			String attrId = it.next();
			System.out.println("Attribute '" + attrId + "' has values: " + attributeValues.get(attrId));
		}
	}

	public void clearAll() {
		attributeValues = new HashMap<String, List<String>>();
	}
	/**
	 * Add the attribute type and its value to the map
	 * @param attributeId
	 * @param attributeValue
	 */
	public void addAttributeValue(String attributeId, String attributeValue) {
		List<String> strList;
		// add the attribute id into the list
		if (!attributeValues.containsKey(attributeId)) {
			strList = new ArrayList<String>();
			attributeValues.put(attributeId, strList);
		}
		// add the attribute value into the list
		strList = attributeValues.get(attributeId);
		if (strList == null || attributeValue == null || attributeValue == "") {
			return;
		}
		if (!strList.contains(attributeValue))
			strList.add(attributeValue);
	}
	
        
	public List<String> getAttributeTypeList() {
		List<String> sortedKeys = new ArrayList<String>(attributeValues.keySet());
		Collections.sort(sortedKeys);
		return sortedKeys;
	}

	public List<String> getAttributeValues(String attributeId) {
		return attributeValues.get(attributeId);
	}
	
	/**
	 * Return the list of underlines to show a list of attribute types
	 * @return
	 */
	public String getUnderlineAttributes() {
		String str = "";
		for (int i = 0; i < attributeValues.keySet().size(); i++) {
			if (i > 0)
				str += ", ";
			str += "_";
		}
		return str;
	}

	/**
	 * Return the list of underlines to show a list of attribute types
	 * @return
	 */
	public String getVariableAttributes() {
		String str = "";
		for (int i = 0; i < attributeValues.keySet().size(); i++) {
			if (i > 0)
				str += ", ";
			str += PolicyElementIdentifierUtil.ATTRIBUTE_VARIABLE_NAME_PREFIX + String.valueOf(i + 1);
		}
		return str;
	}

	/**
	 * Return the number of attribute types in the program
	 * @return
	 */
	public int getNumberOfAttributeTypes() {
		return attributeValues.keySet().size();
	}

	/**
	 * Return the number of attribute types in the program
	 * @return
	 */
	public int getNumberOfAttributeValues() {
		int n = 0;
		Iterator<String> it = attributeValues.keySet().iterator();
		while (it.hasNext()) {
			String attrId = it.next();
			n += attributeValues.get(attrId).size();
		}
		return n;
	}
        
        public Map getAttributeValuesMap()
        {
            return attributeValues;
        }
        
}
