
package edu.unsw.cse.xacml.translator.util;

public class MatchIdConverterUtil {

	public enum OperatorType {
		GREATER_THAN,
		GREATER_THAN_OR_EQUAL,
		LESS_THAN,
		LESS_THAN_OR_EQUAL,
		EQUAL
	}

	public static final String[] EQUAL_OPERATORS = new String[]{
			"urn:oasis:names:tc:xacml:1.0:function:string-equal",
			"urn:oasis:names:tc:xacml:1.0:function:integer-equal",
			"urn:oasis:names:tc:xacml:1.0:function:double-equal",
			"urn:oasis:names:tc:xacml:1.0:function:boolean-equal",
			"urn:oasis:names:tc:xacml:1.0:function:date-equal",
			"urn:oasis:names:tc:xacml:1.0:function:time-equal",
			"urn:oasis:names:tc:xacml:1.0:function:dateTime-equal",
			"urn:oasis:names:tc:xacml:1.0:function:dayTimeDuration-equal",
			"urn:oasis:names:tc:xacml:1.0:function:yearMonthDuration-equal",
			"urn:oasis:names:tc:xacml:1.0:function:rfc822Name-equal",
			"urn:oasis:names:tc:xacml:1.0:function:x500Name-equal",
			"urn:oasis:names:tc:xacml:1.0:function:anyURI-equal",
			"urn:oasis:names:tc:xacml:1.0:function:base64Binary-equal"
	};

	public static final String[] GREATER_THAN_OPERATORS = new String[]{
			"urn:oasis:names:tc:xacml:1.0:function:string-greater-than",
			"urn:oasis:names:tc:xacml:1.0:function:integer-greater-than",
			"urn:oasis:names:tc:xacml:1.0:function:double-greater-than",
			"urn:oasis:names:tc:xacml:1.0:function:date-greater-than",
			"urn:oasis:names:tc:xacml:1.0:function:time-greater-than",
			"urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than"
	};

	public static final String[] GREATER_THAN_OR_EQUAL_OPERATORS = new String[]{
			"urn:oasis:names:tc:xacml:1.0:function:string-greater-than-or-equal",
			"urn:oasis:names:tc:xacml:1.0:function:integer-greater-than-or-equal",
			"urn:oasis:names:tc:xacml:1.0:function:double-greater-than-or-equal",
			"urn:oasis:names:tc:xacml:1.0:function:date-greater-than-or-equal",
			"urn:oasis:names:tc:xacml:1.0:function:time-greater-than-or-equal",
			"urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than-or-equal"
	};

	public static final String[] LESS_THAN_OPERATORS = new String[]{
			"urn:oasis:names:tc:xacml:1.0:function:string-less-than",
			"urn:oasis:names:tc:xacml:1.0:function:integer-less-than",
			"urn:oasis:names:tc:xacml:1.0:function:double-less-than",
			"urn:oasis:names:tc:xacml:1.0:function:date-less-than",
			"urn:oasis:names:tc:xacml:1.0:function:time-less-than",
			"urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than"
	};

	public static final String[] LESS_THAN_OR_EQUAL_OPERATORS = new String[]{
			"urn:oasis:names:tc:xacml:1.0:function:string-less-than-or-equal",
			"urn:oasis:names:tc:xacml:1.0:function:integer-less-than-or-equal",
			"urn:oasis:names:tc:xacml:1.0:function:double-less-than-or-equal",
			"urn:oasis:names:tc:xacml:1.0:function:date-less-than-or-equal",
			"urn:oasis:names:tc:xacml:1.0:function:time-less-than-or-equal",
			"urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than-or-equal"
	};

	public static OperatorType getOperator(String matchId) {
		if (find(matchId, EQUAL_OPERATORS)) {
			return OperatorType.EQUAL;
		}

		if (find(matchId, GREATER_THAN_OPERATORS)) {
			return OperatorType.GREATER_THAN;
		}
		if (find(matchId, GREATER_THAN_OR_EQUAL_OPERATORS)) {
			return OperatorType.GREATER_THAN_OR_EQUAL;
		}

		if (find(matchId, LESS_THAN_OPERATORS)) {
			return OperatorType.LESS_THAN;
		}
		if (find(matchId, LESS_THAN_OR_EQUAL_OPERATORS)) {
			return OperatorType.LESS_THAN_OR_EQUAL;
		}

		throw new UnsupportedOperationException("Unknown operator");
	}

	private static boolean find(String matchId, String[] operators) {
		for (int i = 0; i < operators.length; i++) {
			if (operators[i].equals(matchId)) {
				return true;
			}
		}
		return false;
	}

}
