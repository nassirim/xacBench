/**
 *
 */
package edu.unsw.cse.xacml.translator.util;

/**
 * @author Canh Ngo
 */
public class DataTypeConverterUtil {

    public static String XACML_3_0_DATA_TYPE_STRING = "http://www.w3.org/2001/XMLSchema#string";

    public static String XACML_3_0_DATA_TYPE_INTEGER = "http://www.w3.org/2001/XMLSchema#integer";

    public static String XACML_3_0_DATA_TYPE_DOUBLE = "http://www.w3.org/2001/XMLSchema#double";

    public static String XACML_3_0_DATA_TYPE_ANYURI = "http://www.w3.org/2001/XMLSchema#anyURI";

//	public static String[] DATA_TYPES = {
//			XACML_3_0_DATA_TYPE_STRING,
//			XACML_3_0_DATA_TYPE_INTEGER,
//			XACML_3_0_DATA_TYPE_DOUBLE,
//			XACML_3_0_DATA_TYPE_ANYURI
//	};

    public static Comparable<?> convert(String value, String dataType) throws XACMLTranslatingException {

        if (dataType.equalsIgnoreCase(XACML_3_0_DATA_TYPE_STRING)) {
            return value;
        } else if (dataType.equalsIgnoreCase(XACML_3_0_DATA_TYPE_INTEGER)) {
            return new Integer(Integer.parseInt(value));
        } else if (dataType.equalsIgnoreCase(XACML_3_0_DATA_TYPE_DOUBLE)) {
            return new Double(Double.parseDouble(value));
        } else if (dataType.equalsIgnoreCase(XACML_3_0_DATA_TYPE_ANYURI)) {
            return value;
        } else {
            throw new XACMLTranslatingException("Not supported data type: " + dataType);
        }
    }


}
