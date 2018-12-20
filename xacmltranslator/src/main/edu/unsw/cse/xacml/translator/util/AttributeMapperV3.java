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
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType;

/**
 * @author shayan_310
 *
 */
public class AttributeMapperV3 {

	private static AttributeMapperV3 instance = null;

	/**
	 * Collect all attribute values for each attribute identifier: List<attribute_id, List<attribute_values>>
	 */
	private Map<String, List<AttributeValueType>> attributeValues;

	protected AttributeMapperV3() {
		attributeValues = new HashMap<String, List<AttributeValueType>>();
	}

	public static AttributeMapperV3 getInstance() {
		if(instance == null) {
			instance = new AttributeMapperV3();
		}
		return instance;
	}

	public void clearAll() {
		attributeValues = new HashMap<String, List<AttributeValueType>>();
	}
	/**
	 * Add the attribute type and its value to the map
	 * @param attributeId
	 * @param attributeValue
	 */
	public void addAttributeValue(String attributeId, AttributeValueType attributeValue) {
		List<AttributeValueType> attrValList;
		// add the attribute id into the list
		if (!attributeValues.containsKey(attributeId)) {
			attrValList = new ArrayList<AttributeValueType>();
			attributeValues.put(attributeId, attrValList);
		}
		// add the attribute value into the list
		attrValList = attributeValues.get(attributeId);
		if (attrValList == null || attributeValue == null) {
			return;
		}
		if (!attrValList.contains(attributeValue))
			attrValList.add(attributeValue);
	}
	
	public List<AttributeValueType> getAttributeValues(String attributeId) {
		return attributeValues.get(attributeId);
	}
        
        public Map getAttributeValuesMap()
        {
            return attributeValues;
        }
        
}
