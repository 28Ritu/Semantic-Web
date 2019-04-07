package org.example;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class HotelRecommendation {
	static String baseIRI = "http://www.semanticweb.org/ontologies/2019/hotel-recommendation/";
	static ModelBuilder builder;
	
	// Create IRI for all classes
	public static void createClassIRI() {
		builder.defaultGraph().subject(EX.PPROPERTY).add(RDF.TYPE, RDFS.CLASS);// Property == Hotel
		builder.defaultGraph().subject(EX.GEOGRAPHIC_ENTITY).add(RDF.TYPE, RDFS.CLASS);
		builder.defaultGraph().subject(EX.COUNTRY)
							  .add(RDF.TYPE, RDFS.CLASS)
							  .add(RDFS.SUBCLASSOF, EX.GEOGRAPHIC_ENTITY);
		builder.defaultGraph().subject(EX.STATE)
		  					  .add(RDF.TYPE, RDFS.CLASS)
		  					  .add(RDFS.SUBCLASSOF, EX.GEOGRAPHIC_ENTITY);
		builder.defaultGraph().subject(EX.CITY)
		  					  .add(RDF.TYPE, RDFS.CLASS)
		  					  .add(RDFS.SUBCLASSOF, EX.GEOGRAPHIC_ENTITY);
		builder.defaultGraph().subject(EX.AREA)
		                      .add(RDF.TYPE, RDFS.CLASS)
		                      .add(RDFS.SUBCLASSOF, EX.GEOGRAPHIC_ENTITY);
	}
	
	// Create all instances
	public static void createInstances(List<String[]> allData) {
		int i = 1;
		for (String[] row : allData) { 
//            builder.defaultGraph().subject(EX.PPROPERTY + "#" + i)
//            					  .add(FOAF.NAME, row[22])
//            					  .add(RDF.TYPE, EX.PPROPERTY);
//			if (!row[2].equals(""))
//				builder.defaultGraph().subject("ex:" + row[2])
//            					  	  .add(FOAF.NAME, row[2])
//            					  	  .add(RDF.TYPE, EX.COUNTRY);
			if (!row[0].equals(""))
				builder.defaultGraph().subject("ex:" + row[0])
            					  	  .add(FOAF.NAME, row[0])
            					  	  .add(RDF.TYPE, EX.AREA);
        i++;
		} 
		
	}
	
	// CSV Parser
	public static void parseCSV() {
		try {
			FileReader filereader = new FileReader("/home/ritu/makemytrip_com-travel_sample.csv");
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			List<String[]> allData = csvReader.readAll(); 
			ArrayList<String> propertyNames = new ArrayList<String>();
			ArrayList<String> areaNames = new ArrayList<String>();
			
			createInstances(allData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File dataDir = new File("/home/ritu/sweb");
		Repository repo = new SailRepository(new NativeStore(dataDir));
		repo.initialize();
		builder = new ModelBuilder();
		builder.setNamespace("ex", baseIRI)
				.setNamespace(FOAF.NS)
				.setNamespace(RDF.NS)
				.setNamespace(RDFS.NS);
		createClassIRI();
		parseCSV();
		Model model = builder.build();
		int i = 0;
		for (Statement stmt : model) {
			System.out.println(stmt);
			i += 1;
//			if (i==7)
//				break;
		}
		System.out.println(i);
	}

}
