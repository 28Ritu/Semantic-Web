package org.example;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

public class EX {
	public static final String baseIRI = "http://www.semanticweb.org/ontologies/2019/hotel-recommendation/";
	
	public static final IRI PPROPERTY = getIRI("property");
	
	public static final IRI GEOGRAPHIC_ENTITY = getIRI("geographicEntity");
	
	public static final IRI COUNTRY = getIRI("country");
	
	public static final IRI STATE = getIRI("state");
	
	public static final IRI CITY = getIRI("city");
	
	public static final IRI AREA = getIRI("area");
	
	private static IRI getIRI(String localName) {
		return SimpleValueFactory.getInstance().createIRI(baseIRI, localName);
	}
}
