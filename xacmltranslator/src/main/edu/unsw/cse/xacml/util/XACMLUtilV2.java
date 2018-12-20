/**
 * 
 */
package edu.unsw.cse.xacml.util;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import edu.unsw.cse.xacml.profiles._2_0_.policy.ObjectFactory;
import edu.unsw.cse.xacml.profiles._2_0_.policy.PolicySetType;
import edu.unsw.cse.xacml.profiles._2_0_.policy.PolicyType;

/**
 * @author mohsen
 *
 */
public class XACMLUtilV2 {

	public static PolicySetType unmarshalPolicySetType(String policysetFile)
			throws ParserConfigurationException, SAXException, IOException {
		Document doc = readXML(policysetFile);
		Element xmlDom = doc.getDocumentElement();

		return unmarshalPolicySetType(xmlDom);
	}

	public static Document readXML(String xmlFile) throws ParserConfigurationException, SAXException, IOException {
		javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);

		javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();

		org.w3c.dom.Document doc = db.parse(xmlFile);

		return doc;
	}

	/**
	 * @param domRequest
	 * @return
	 */
	private static PolicySetType unmarshalPolicySetType(Element dom) {
		return unmarshall(PolicySetType.class, dom);
	}

	private static <T> T unmarshall(Class<T> cls, Element dom) {
		try {
			JAXBContext jc = JAXBContext.newInstance(cls);
			Unmarshaller unmarshaller = jc.createUnmarshaller();

			JAXBElement<T> jaxbObject = unmarshaller.unmarshal(dom, cls);
			return jaxbObject.getValue();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void savePolicy(PolicySetType policyset, OutputStream arg1)
			throws ParserConfigurationException, SAXException, IOException {
		ObjectFactory of = new ObjectFactory();
		write(of.createPolicySetTypePolicySet(policyset), arg1);
	}

	private void write(JAXBElement<PolicySetType> jaxbElement, OutputStream arg1) {
		try {
			JAXBContext jc = JAXBContext.newInstance(PolicySetType.class, PolicyType.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(jaxbElement, arg1);

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
