package org.example;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
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
		
		builder.defaultGraph().subject(EX.RESOURCE).add(RDF.TYPE, RDFS.CLASS);
		builder.defaultGraph().subject(EX.IMAGE)
							  .add(RDF.TYPE, RDFS.CLASS)
							  .add(RDFS.SUBCLASSOF, EX.RESOURCE);
		builder.defaultGraph().subject(EX.PAGE)
		                      .add(RDF.TYPE, RDFS.CLASS)
		                      .add(RDFS.SUBCLASSOF, EX.RESOURCE);
		
		builder.defaultGraph().subject(EX.LOCATION).add(RDF.TYPE, RDFS.CLASS);
		builder.defaultGraph().subject(EX.PROPERTY_HIGHLIGHTS).add(RDF.TYPE, RDFS.CLASS);
		builder.defaultGraph().subject(EX.REVIEWANDRATING).add(RDF.TYPE, RDFS.CLASS);
		builder.defaultGraph().subject(EX.ROOMTYPE).add(RDF.TYPE, RDFS.CLASS);
		builder.defaultGraph().subject(EX.SITE).add(RDF.TYPE, RDFS.CLASS);
		builder.defaultGraph().subject(EX.SITECRAWL).add(RDF.TYPE, RDFS.CLASS);
		builder.defaultGraph().subject(EX.TRAVELLERTYPE).add(RDF.TYPE, RDFS.CLASS);
		builder.defaultGraph().subject(EX.FAMILY)
							  .add(RDF.TYPE, RDFS.CLASS)
							  .add(RDFS.SUBCLASSOF, EX.TRAVELLERTYPE);
		
		builder.defaultGraph().subject(EX.COUPLE)
		  					  .add(RDF.TYPE, RDFS.CLASS)
		  					  .add(RDFS.SUBCLASSOF, EX.TRAVELLERTYPE);
		
		builder.defaultGraph().subject(EX.SOLO)
		  					  .add(RDF.TYPE, RDFS.CLASS)
		  					  .add(RDFS.SUBCLASSOF, EX.TRAVELLERTYPE);
		
		builder.defaultGraph().subject(EX.BUSINESS)
		  					  .add(RDF.TYPE, RDFS.CLASS)
		  					  .add(RDFS.SUBCLASSOF, EX.TRAVELLERTYPE);
		
		builder.defaultGraph().subject(EX.FRIENDS)
		  					  .add(RDF.TYPE, RDFS.CLASS)
		  					  .add(RDFS.SUBCLASSOF, EX.TRAVELLERTYPE);
		
	}
	
	// Create IRI for all properties
	public static void createPropertyIRI()
	{
		// Object properties
		builder.defaultGraph().subject(EX.HAS_CRAWLED)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, EX.SITE)
							  .add(RDFS.RANGE, EX.SITECRAWL);
		
		builder.defaultGraph().subject(EX.LOCATED_AT)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, EX.PPROPERTY)
							  .add(RDFS.RANGE, EX.LOCATION);
		
		builder.defaultGraph().subject(EX.HAS_AREA)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, EX.CITY)
							  .add(RDFS.RANGE, EX.AREA);
		
		builder.defaultGraph().subject(EX.HAS_CITY)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, EX.STATE)
							  .add(RDFS.RANGE, EX.CITY);
		
		builder.defaultGraph().subject(EX.HAS_STATE)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, EX.COUNTRY)
							  .add(RDFS.RANGE, EX.STATE);
		
		builder.defaultGraph().subject(EX.SITUATED_IN)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, EX.PPROPERTY)
							  .add(RDFS.RANGE, EX.GEOGRAPHIC_ENTITY);
		
		builder.defaultGraph().subject(EX.SITUATED_IN_AREA)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.SUBPROPERTYOF, EX.SITUATED_IN)
							  .add(RDFS.DOMAIN, EX.PPROPERTY)
							  .add(RDFS.RANGE, EX.AREA);
		
		builder.defaultGraph().subject(EX.SITUATED_IN_CITY)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.SUBPROPERTYOF, EX.SITUATED_IN)
							  .add(RDFS.DOMAIN, EX.PPROPERTY)
							  .add(RDFS.RANGE, EX.CITY);
		
		builder.defaultGraph().subject(EX.SITUATED_IN_STATE)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.SUBPROPERTYOF, EX.SITUATED_IN)
							  .add(RDFS.DOMAIN, EX.PPROPERTY)
							  .add(RDFS.RANGE, EX.STATE);
		
		builder.defaultGraph().subject(EX.SITUATED_IN_COUNTRY)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.SUBPROPERTYOF, EX.SITUATED_IN)
							  .add(RDFS.DOMAIN, EX.PPROPERTY)
							  .add(RDFS.RANGE, EX.COUNTRY);
		
		builder.defaultGraph().subject(EX.HAS_ROOM_TYPE)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, EX.PPROPERTY)
							  .add(RDFS.RANGE, EX.ROOMTYPE);
		
		builder.defaultGraph().subject(EX.ASSOCIATED_SITE)
		                      .add(RDF.TYPE, RDF.PROPERTY)
		                      .add(RDFS.DOMAIN, EX.PPROPERTY)
		                      .add(RDFS.RANGE, EX.SITE);
		
		builder.defaultGraph().subject(EX.HAS_HIGHLIGHTS)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, EX.PPROPERTY)
							  .add(RDFS.RANGE, EX.PROPERTY_HIGHLIGHTS);
		
		builder.defaultGraph().subject(EX.HAS_REVIEWANDRATING)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, EX.SITE)
							  .add(RDFS.RANGE, EX.REVIEWANDRATING);
		
		builder.defaultGraph().subject(EX.HAS_TRAVELLERS)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, EX.PPROPERTY)
							  .add(RDFS.RANGE, EX.TRAVELLERTYPE);
		
		builder.defaultGraph().subject(EX.MORE_INFO)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, EX.PPROPERTY)
							  .add(RDFS.RANGE, EX.RESOURCE);
		
		builder.defaultGraph().subject(EX.PAGE_INFO)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.SUBPROPERTYOF, EX.MORE_INFO)
							  .add(RDFS.DOMAIN, EX.PPROPERTY)
							  .add(RDFS.RANGE, EX.PAGE);
		
		builder.defaultGraph().subject(EX.IMAGE_INFO)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.SUBPROPERTYOF, EX.MORE_INFO)
							  .add(RDFS.DOMAIN, EX.PPROPERTY)
							  .add(RDFS.RANGE, EX.IMAGE);
		
		// Data properties
		builder.defaultGraph().subject(EX.HAS_CRAWL_DATE).add(RDF.TYPE, RDF.PROPERTY);		
		builder.defaultGraph().subject(EX.SITE_REVIEWANDRATING).add(RDF.TYPE, RDF.PROPERTY);	
		builder.defaultGraph().subject(EX.PAGE_URL).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.IMAGE_URL).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.AREA_NAME).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.CITY_NAME).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.STATE_NAME).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.COUNTRY_NAME).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.TRAVELLER_REVIEW_COUNT).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.HIGHLIGHT_OVERVIEW).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.HIGHLIGHT_VALUE).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.HOLIDAY_REVIEW_COUNT).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.IS_VALUE_PLUS).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.LOCATION_RATING).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.QTS).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.QUERY_TIMESTAMP).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.REVIEW_COUNT).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.REVIEW_RATING).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.REVIEW_SCORE).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.ROOM_ACCESSORIES).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.SITE_NAME).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.SITE_REVIEW_COUNT).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.TRIP_ADVISOR_COUNT).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.UNIQ_ID).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.HAS_LATITUDE).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.HAS_LONGITUDE).add(RDF.TYPE, RDF.PROPERTY);		
		builder.defaultGraph().subject(EX.HAS_PROPERTY_ID).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.HAS_PROPERTY_NAME).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.HAS_PROPERTY_ADDRESS).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.HAS_PROPERTY_TYPE).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.HAS_STAR_RATING).add(RDF.TYPE, RDF.PROPERTY);
		builder.defaultGraph().subject(EX.HAS_TRAVELLER_RATING).add(RDF.TYPE, RDF.PROPERTY);
	}
	
	// Create all instances
	public static void createInstances(List<String[]> allData) {
		int i = 1;
		for (String[] row : allData) { 
            builder.defaultGraph().subject(EX.PPROPERTY + "#" + i)
            					  .add(RDF.TYPE, EX.PPROPERTY)
            					  .add(EX.HAS_PROPERTY_NAME, row[22])
            					  .add(EX.HAS_PROPERTY_TYPE, row[23])
            					  .add(EX.HAS_PROPERTY_ADDRESS, row[20])
            					  .add(EX.HAS_PROPERTY_ID, row[21])
            					  .add(EX.UNIQ_ID, row[32]);
            
			if (!row[2].equals(""))
			{
				builder.defaultGraph().subject("ex:" + row[2])
            					  	  .add(FOAF.NAME, row[2])
            					  	  .add(RDF.TYPE, EX.COUNTRY);
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(baseIRI + row[2]))
						builder1.defaultGraph().subject(EX.PPROPERTY + "#" + i)
											  .add(EX.SITUATED_IN_COUNTRY, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
			
			
			if (!row[0].equals(""))
			{
				builder.defaultGraph().subject(EX.AREA + "#" + i)
            					  	  .add(FOAF.NAME, row[0])
            					  	  .add(RDF.TYPE, EX.AREA);
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(EX.AREA + "#" + i))
						builder1.defaultGraph().subject(EX.PPROPERTY + "#" + i)
											  .add(EX.SITUATED_IN_AREA, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
			
			if (!row[30].equals(""))
			{
				builder.defaultGraph().subject("ex:" + row[30])
            					  	  .add(FOAF.NAME, row[30])
            					  	  .add(RDF.TYPE, EX.STATE);
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(baseIRI + row[30]))
						builder1.defaultGraph().subject(EX.PPROPERTY + "#" + i)
											  .add(EX.SITUATED_IN_STATE, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
			
			if (!row[1].equals(""))
			{
				builder.defaultGraph().subject("ex:" + row[1])
            					  	  .add(FOAF.NAME, row[1])
            					  	  .add(RDF.TYPE, EX.CITY);
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(baseIRI + row[1]))
						builder1.defaultGraph().subject(EX.PPROPERTY + "#" + i)
											  .add(EX.SITUATED_IN_CITY, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
			if (!row[10].equals("") && !row[11].equals("")) 
			{
				builder.defaultGraph().subject(EX.LOCATION + "#" + i)
									  .add(RDF.TYPE, EX.LOCATION)
									  .add(EX.HAS_LATITUDE, row[10])
									  .add(EX.HAS_LONGITUDE, row[11]);
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(EX.LOCATION + "#" + i))
						builder1.defaultGraph().subject(EX.PPROPERTY + "#" + i)
											  .add(EX.LOCATED_AT, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
			
			if (!row[7].equals(""))
			{
				builder.defaultGraph().subject(EX.IMAGE + "#" + i)
									  .add(RDF.TYPE, EX.IMAGE)
									  .add(EX.IMAGE_URL, row[7]);
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(EX.IMAGE + "#" + i))
						builder1.defaultGraph().subject(EX.PPROPERTY + "#" + i)
											  .add(EX.IMAGE_INFO, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
			
			if (!row[19].equals(""))
			{
				builder.defaultGraph().subject(EX.PAGE + "#" + i)
									  .add(RDF.TYPE, EX.PAGE)
									  .add(EX.PAGE_URL, row[19]);
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(EX.IMAGE + "#" + i))
						builder1.defaultGraph().subject(EX.PPROPERTY + "#" + i)
											  .add(EX.PAGE_INFO, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
			
			if (!row[29].equals(""))
			{
				builder.defaultGraph().subject("ex:" + row[29])
									  .add(RDF.TYPE, EX.SITE)
									  .add(EX.SITE_NAME, row[29]);
				if (!row[27].equals(""))
					builder.defaultGraph().subject("ex:" + row[29])
										  .add(EX.SITE_REVIEW_COUNT, row[27].substring(1));
				if (!row[28].equals(""))
					builder.defaultGraph().subject("ex:" + row[29])
					  					  .add(EX.SITE_REVIEWANDRATING, row[28]);
				
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(baseIRI + row[29]))
						builder1.defaultGraph().subject(EX.PPROPERTY + "#" + i)
											  .add(EX.ASSOCIATED_SITE, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
			
			if (!row[3].equals("")) 
			{
				builder.defaultGraph().subject(EX.SITECRAWL + "#" + i)
									  .add(RDF.TYPE, EX.SITECRAWL)
									  .add(EX.HAS_CRAWL_DATE, row[3]);
				if (!row[24].equals(""))
					builder.defaultGraph().subject(EX.SITECRAWL + "#" + i)
										  .add(EX.QTS, row[24]);
				if (!row[25].equals(""))
					builder.defaultGraph().subject(EX.SITECRAWL + "#" + i)
										  .add(EX.QUERY_TIMESTAMP, row[25]);
				
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(EX.SITECRAWL + "#" + i))
						builder1.defaultGraph().subject(baseIRI + row[29])
											  .add(EX.HAS_CRAWLED, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
			
			if (!row[5].equals("") || !row[4].equals("") || !row[9].equals("") 
					|| !row[8].equals("") || !row[6].equals("") || !row[31].equals(""))
			{
				builder.defaultGraph().subject(EX.PROPERTY_HIGHLIGHTS + "#" + i)
				  					  .add(RDF.TYPE, EX.PROPERTY_HIGHLIGHTS);
				
				if (!row[5].equals(""))
					builder.defaultGraph().subject(EX.PROPERTY_HIGHLIGHTS + "#" + i)
										  .add(EX.HIGHLIGHT_OVERVIEW, row[5]);
				
				if (!row[4].equals("") && !row[4].equals("{{facility}}"))
					builder.defaultGraph().subject(EX.PROPERTY_HIGHLIGHTS + "#" + i)
										  .add(EX.HIGHLIGHT_VALUE, row[4]);
				
				if (!row[9].equals(""))
					builder.defaultGraph().subject(EX.PROPERTY_HIGHLIGHTS + "#" + i)
										  .add(EX.IS_VALUE_PLUS, row[9]);
				
				if (!row[8].equals("") && !row[8].equals("{{value}}"))
					builder.defaultGraph().subject(EX.PROPERTY_HIGHLIGHTS + "#" + i)
										  .add(EX.ROOM_ACCESSORIES, row[8]);
				
				if (!row[6].equals(""))
					builder.defaultGraph().subject(EX.PROPERTY_HIGHLIGHTS + "#" + i)
					  					  .add(EX.HAS_STAR_RATING, row[6]);
				
				if (!row[31].equals(""))
					builder.defaultGraph().subject(EX.PROPERTY_HIGHLIGHTS + "#" + i)
					  					  .add(EX.HAS_TRAVELLER_RATING, row[21]);
				
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(EX.PROPERTY_HIGHLIGHTS + "#" + i))
						builder1.defaultGraph().subject(EX.PPROPERTY + "#" + i)
											  .add(EX.HAS_HIGHLIGHTS, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
			
			if (!row[12].equals("") || !row[13].equals("") || !row[14].equals("") 
					|| !row[15].equals("") || !row[16].equals("") || !row[18].equals(""))
			{
				builder.defaultGraph().subject(EX.REVIEWANDRATING + "#" + i)
									  .add(RDF.TYPE, EX.REVIEWANDRATING);
				
				if (!row[12].equals(""))
					builder.defaultGraph().subject(EX.REVIEWANDRATING + "#" + i)
										  .add(EX.HOLIDAY_REVIEW_COUNT, row[12]);
				
				if (!row[13].equals("") && !row[13].equals(".."))
					builder.defaultGraph().subject(EX.REVIEWANDRATING + "#" + i)
										  .add(EX.LOCATION_RATING, row[13]);
				
				if (!row[14].equals(""))
					builder.defaultGraph().subject(EX.REVIEWANDRATING + "#" + i)
										  .add(EX.REVIEW_COUNT, row[14]);
				
				if (!row[15].equals("") && !row[15].equals("{{ratingCriteria.name}}{{ratingCriteria.value}}"))
					builder.defaultGraph().subject(EX.REVIEWANDRATING + "#" + i)
										  .add(EX.REVIEW_RATING, row[15]);
				
				if (!row[16].equals(""))
					builder.defaultGraph().subject(EX.REVIEWANDRATING + "#" + i)
										  .add(EX.REVIEW_SCORE, row[16]);
				
				if (!row[18].equals(""))
					builder.defaultGraph().subject(EX.REVIEWANDRATING + "#" + i)
										  .add(EX.TRIP_ADVISOR_COUNT, row[18]);
				
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(EX.REVIEWANDRATING + "#" + i))
						builder1.defaultGraph().subject(baseIRI + row[29])
											  .add(EX.HAS_REVIEWANDRATING, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
			
			if (!row[17].equals("") && !row[17].equals("Families:{{ratingSummaryInfo.miscMap['family']}}|"
					+ "Couples:{{ratingSummaryInfo.miscMap['couple']}}|"
					+ "Business:{{ratingSummaryInfo.miscMap['business']}}|"
					+ "Solo:{{ratingSummaryInfo.miscMap['solo']}}|"
					+ "Friends:{{ratingSummaryInfo.miscMap['friends']}}"))
			{
				String[] parts = row[17].split("\\|");
				for (String s : parts) 
				{
					String[] parts1 = s.split(":");
					if (parts1.length > 1) 
					{
						if (parts1[0].equalsIgnoreCase("Family") || parts1[0].equalsIgnoreCase("Families"))
						{
							builder.defaultGraph().subject(EX.FAMILY + "#" + i)
												  .add(RDF.TYPE, EX.FAMILY);
							if (!parts1[1].equals(""))
								builder.defaultGraph().subject(EX.FAMILY + "#" + i).add(EX.TRAVELLER_REVIEW_COUNT, parts1[1]);
						}
						if (parts1[0].equalsIgnoreCase("Couple") || parts1[0].equalsIgnoreCase("Couples"))
						{
							builder.defaultGraph().subject(EX.COUPLE + "#" + i)
												  .add(RDF.TYPE, EX.COUPLE);
							if (!parts1[1].equals(""))
								builder.defaultGraph().subject(EX.COUPLE + "#" + i).add(EX.TRAVELLER_REVIEW_COUNT, parts1[1]);
						}
						if (parts1[0].equalsIgnoreCase("Solo"))
						{
							builder.defaultGraph().subject(EX.SOLO + "#" + i)
												  .add(RDF.TYPE, EX.SOLO);
							if (!parts1[1].equals(""))
								builder.defaultGraph().subject(EX.SOLO + "#" + i).add(EX.TRAVELLER_REVIEW_COUNT, parts1[1]);
						}
						if (parts1[0].equalsIgnoreCase("Business"))
						{
							builder.defaultGraph().subject(EX.BUSINESS + "#" + i)
												  .add(RDF.TYPE, EX.BUSINESS);
							if (!parts1[1].equals(""))
								builder.defaultGraph().subject(EX.BUSINESS + "#" + i).add(EX.TRAVELLER_REVIEW_COUNT, parts1[1]);
						}
						if (parts1[0].equalsIgnoreCase("Friends"))
						{
							builder.defaultGraph().subject(EX.FRIENDS + "#" + i)
												  .add(RDF.TYPE, EX.FRIENDS);
							if (!parts1[1].equals(""))
								builder.defaultGraph().subject(EX.FRIENDS + "#" + i).add(EX.TRAVELLER_REVIEW_COUNT, parts1[1]);
						}
					}
				}
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(EX.FAMILY + "#" + i) ||
							stmt.getSubject().toString().equals(EX.COUPLE + "#" + i) ||
							stmt.getSubject().toString().equals(EX.SOLO + "#" + i) ||
							stmt.getSubject().toString().equals(EX.BUSINESS + "#" + i) ||
							stmt.getSubject().toString().equals(EX.FRIENDS + "#" + i))
						builder1.defaultGraph().subject(EX.PPROPERTY + "#" + i)
											  .add(EX.HAS_TRAVELLERS, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
			
			if (!row[26].equals("")) 
			{
				builder.defaultGraph().subject(EX.ROOMTYPE + "#" + i)
									  .add(RDF.TYPE, EX.ROOMTYPE);
				if (row[26].contains("|")) {
					String[] parts = row[26].split("\\|");
					for (String room : parts)
					{
						builder.defaultGraph().subject(EX.ROOMTYPE + "#" + i)
											  .add(EX.ROOM_INFO, room);
					}
				}
				else
					builder.defaultGraph().subject(EX.ROOMTYPE + "#" + i)
					  					  .add(EX.ROOM_INFO, row[26]);
				
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(EX.ROOMTYPE + "#" + i))
						builder1.defaultGraph().subject(EX.PPROPERTY + "#" + i)
											  .add(EX.HAS_ROOM_TYPE, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
//            break;
          i++;
//          if (i == 33)
//        	  break;
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
//		for (Statement stmt : model) {
//			//System.out.println(stmt);
//			i += 1;
////			if (i==7)
////				break;
//		}
//		Rio.write(model, System.out, RDFFormat.TURTLE);
//		System.out.println(i);
		
		RepositoryConnection conn = repo.getConnection();
		conn.add(model);
		String queryString = "SELECT (COUNT(?s) AS ?triples) WHERE { ?s ?p ?o }";
		TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
		TupleQueryResult result = tupleQuery.evaluate();
		String v = result.next().getValue("triples").stringValue();
		result.close();
		System.out.println(Long.parseLong(v));
	}

}
