package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandler;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class HotelRecommendation {
	static String baseIRI = "http://www.semanticweb.org/ontologies/2019/hotel-recommendation/";
	static ModelBuilder builder;
	static RepositoryConnection conn;
	static ValueFactory vf = SimpleValueFactory.getInstance();
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
		builder.defaultGraph().subject(EX.HAS_CRAWL_DATE)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("hasCrawlDate")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("hasCrawlDate")[1]);		
		builder.defaultGraph().subject(EX.SITE_REVIEWANDRATING)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("siteReviewRating")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("siteReviewRating")[1]);		
		builder.defaultGraph().subject(EX.PAGE_URL)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("hasPageUrl")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("hasPageUrl")[1]);		
		builder.defaultGraph().subject(EX.IMAGE_URL)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("hasImageUrl")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("hasImageUrl")[1]);		
		builder.defaultGraph().subject(EX.AREA_NAME)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("areaName")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("areaName")[1]);		
		builder.defaultGraph().subject(EX.CITY_NAME)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("cityName")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("cityName")[1]);		
		builder.defaultGraph().subject(EX.STATE_NAME)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("stateName")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("stateName")[1]);		
		builder.defaultGraph().subject(EX.COUNTRY_NAME)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("countryName")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("countryName")[1]);		
		builder.defaultGraph().subject(EX.TRAVELLER_REVIEW_COUNT)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("hasTravellerReviewCount")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("hasTravellerReviewCount")[1]);		
		builder.defaultGraph().subject(EX.HIGHLIGHT_OVERVIEW)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("highlightsOverview")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("highlightsOverview")[1]);
		builder.defaultGraph().subject(EX.HIGHLIGHT_VALUE)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("highlightValue")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("highlightValue")[1]);
		builder.defaultGraph().subject(EX.HOLIDAY_REVIEW_COUNT)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("holidayReviewCount")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("holidayReviewCount")[1]);
		builder.defaultGraph().subject(EX.IS_VALUE_PLUS)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("isValuePlus")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("isValuePlus")[1]);
		builder.defaultGraph().subject(EX.LOCATION_RATING)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("locationRating")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("locationRating")[1]);
		builder.defaultGraph().subject(EX.QTS)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("qts")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("qts")[1]);
		builder.defaultGraph().subject(EX.QUERY_TIMESTAMP)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("queryTimeStamp")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("queryTimeStamp")[1]);
		builder.defaultGraph().subject(EX.REVIEW_COUNT)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("reviewCount")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("reviewCount")[1]);
		builder.defaultGraph().subject(EX.REVIEW_RATING)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("reviewRating")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("reviewRating")[1]);
		builder.defaultGraph().subject(EX.REVIEW_SCORE)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("reviewScore")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("reviewScore")[1]);
		builder.defaultGraph().subject(EX.ROOM_ACCESSORIES)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("roomAccessories")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("roomAccessories")[1]);
		builder.defaultGraph().subject(EX.SITE_NAME)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("siteName")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("siteName")[1]);
		builder.defaultGraph().subject(EX.SITE_REVIEW_COUNT)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("siteReviewCount")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("siteReviewCount")[1]);
		builder.defaultGraph().subject(EX.TRIP_ADVISOR_COUNT)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("tripAdvisorCount")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("tripAdvisorCount")[1]);
		builder.defaultGraph().subject(EX.UNIQ_ID)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("uniqId")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("uniqId")[1]);
		builder.defaultGraph().subject(EX.HAS_LATITUDE)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("hasLatitude")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("hasLatitude")[1]);
		builder.defaultGraph().subject(EX.HAS_LONGITUDE)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("hasLongitude")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("hasLongitude")[1]);
		builder.defaultGraph().subject(EX.HAS_PROPERTY_ID)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("propertyId")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("propertyId")[1]);
		builder.defaultGraph().subject(EX.HAS_PROPERTY_NAME)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("propertyName")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("propertyName")[1]);
		builder.defaultGraph().subject(EX.HAS_PROPERTY_ADDRESS)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("propertyAddress")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("propertyAddress")[1]);
		builder.defaultGraph().subject(EX.HAS_PROPERTY_TYPE)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("propertyType")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("propertyType")[1]);
		builder.defaultGraph().subject(EX.HAS_STAR_RATING)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("starRating")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("starRating")[1]);
		builder.defaultGraph().subject(EX.HAS_TRAVELLER_RATING)
							  .add(RDF.TYPE, RDF.PROPERTY)
							  .add(RDFS.DOMAIN, Mapping.hMap1.get("travellerRating")[0])
							  .add(RDFS.RANGE, Mapping.hMap1.get("travellerRating")[1]);
	}
	
	// Create all instances
	public static void createInstances(List<String[]> allData) {
		int i = 1;
		for (String[] row : allData) { 
			if (i>=1) {
            builder.defaultGraph().subject(EX.PPROPERTY + "#" + i)
            					  .add(RDF.TYPE, EX.PPROPERTY)
            					  .add(EX.HAS_PROPERTY_NAME, vf.createLiteral(row[Mapping.hMap.get("propertyName")], Mapping.hMap1.get("propertyName")[1]))
            					  .add(EX.HAS_PROPERTY_TYPE, vf.createLiteral(row[Mapping.hMap.get("propertyType")], Mapping.hMap1.get("propertyType")[1]))
            					  .add(EX.HAS_PROPERTY_ADDRESS, vf.createLiteral(row[Mapping.hMap.get("propertyAddress")], Mapping.hMap1.get("propertyAddress")[1]))
            					  .add(EX.HAS_PROPERTY_ID, vf.createLiteral(row[Mapping.hMap.get("propertyId")], Mapping.hMap1.get("propertyId")[1]))
            					  .add(EX.UNIQ_ID, vf.createLiteral(row[Mapping.hMap.get("uniqId")], Mapping.hMap1.get("uniqId")[1]));
            
			if (!row[Mapping.hMap.get("countryName")].equals(""))
			{
				builder.defaultGraph().subject("ex:" + row[Mapping.hMap.get("countryName")])
            					  	  .add(EX.COUNTRY_NAME, vf.createLiteral(row[Mapping.hMap.get("countryName")], Mapping.hMap1.get("countryName")[1]))
            					  	  .add(RDF.TYPE, EX.COUNTRY);
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(baseIRI + row[Mapping.hMap.get("countryName")]))
						builder1.defaultGraph().subject(EX.PPROPERTY + "#" + i)
											  .add(EX.SITUATED_IN_COUNTRY, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
			
			if (!row[Mapping.hMap.get("stateName")].equals(""))
			{
				builder.defaultGraph().subject("ex:" + row[Mapping.hMap.get("stateName")])
            					  	  .add(EX.STATE_NAME, vf.createLiteral(row[Mapping.hMap.get("stateName")], Mapping.hMap1.get("stateName")[1]))
            					  	  .add(RDF.TYPE, EX.STATE);
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(baseIRI + row[Mapping.hMap.get("stateName")]))
						builder1.defaultGraph().subject(EX.PPROPERTY + "#" + i)
											  .add(EX.SITUATED_IN_STATE, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
				
				if (!row[Mapping.hMap.get("countryName")].equals("")) 
				{
					ModelBuilder builder2 = new ModelBuilder();
					for (Statement stmt : builder.defaultGraph().build()) {
						if (stmt.getSubject().toString().equals(baseIRI + row[Mapping.hMap.get("stateName")]))
							builder2.defaultGraph().subject(baseIRI + row[Mapping.hMap.get("countryName")])
												  .add(EX.HAS_STATE, stmt.getSubject());
					}
					for (Statement stmt : builder2.defaultGraph().build()) {
						builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
					}
				}
			}
			
			if (!row[Mapping.hMap.get("cityName")].equals(""))
			{
				builder.defaultGraph().subject("ex:" + row[Mapping.hMap.get("cityName")])
            					  	  .add(EX.CITY_NAME, vf.createLiteral(row[Mapping.hMap.get("cityName")], Mapping.hMap1.get("cityName")[1]))
            					  	  .add(RDF.TYPE, EX.CITY);
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(baseIRI + row[Mapping.hMap.get("cityName")]))
						builder1.defaultGraph().subject(EX.PPROPERTY + "#" + i)
											  .add(EX.SITUATED_IN_CITY, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
				
				if (!row[Mapping.hMap.get("stateName")].equals(""))
				{
					ModelBuilder builder2 = new ModelBuilder();
					for (Statement stmt : builder.defaultGraph().build()) {
						if (stmt.getSubject().toString().equals(baseIRI + row[Mapping.hMap.get("cityName")]))
							builder2.defaultGraph().subject(baseIRI + row[Mapping.hMap.get("stateName")])
												  .add(EX.HAS_CITY, stmt.getSubject());
					}
					for (Statement stmt : builder2.defaultGraph().build()) {
						builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
					}
				}
			}
			
			if (!row[Mapping.hMap.get("areaName")].equals(""))
			{
				builder.defaultGraph().subject(EX.AREA + "#" + i)
            					  	  .add(EX.AREA_NAME, vf.createLiteral(row[Mapping.hMap.get("areaName")], Mapping.hMap1.get("areaName")[1]))
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
				
				if (!row[Mapping.hMap.get("cityName")].equals(""))
				{
					ModelBuilder builder2 = new ModelBuilder();
					for (Statement stmt : builder.defaultGraph().build()) {
						if (stmt.getSubject().toString().equals(EX.AREA + "#" + i))
							builder2.defaultGraph().subject(baseIRI + row[Mapping.hMap.get("cityName")])
												  .add(EX.HAS_AREA, stmt.getSubject());
					}
					for (Statement stmt : builder2.defaultGraph().build()) {
						builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
					}
				}
			}
			
			if (!row[Mapping.hMap.get("hasLatitude")].equals("") && !row[Mapping.hMap.get("hasLongitude")].equals("")) 
			{
				builder.defaultGraph().subject(EX.LOCATION + "#" + i)
									  .add(RDF.TYPE, EX.LOCATION)
									  .add(EX.HAS_LATITUDE, vf.createLiteral(row[Mapping.hMap.get("hasLatitude")], Mapping.hMap1.get("hasLatitude")[1]))
									  .add(EX.HAS_LONGITUDE, vf.createLiteral(row[Mapping.hMap.get("hasLongitude")], Mapping.hMap1.get("hasLongitude")[1]));
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
			
			if (!row[Mapping.hMap.get("hasImageUrl")].equals(""))
			{
				builder.defaultGraph().subject(EX.IMAGE + "#" + i)
									  .add(RDF.TYPE, EX.IMAGE)
									  .add(EX.IMAGE_URL, vf.createLiteral(row[Mapping.hMap.get("hasImageUrl")], Mapping.hMap1.get("hasImageUrl")[1]));
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
			
			if (!row[Mapping.hMap.get("hasPageUrl")].equals(""))
			{
				builder.defaultGraph().subject(EX.PAGE + "#" + i)
									  .add(RDF.TYPE, EX.PAGE)
									  .add(EX.PAGE_URL, vf.createLiteral(row[Mapping.hMap.get("hasPageUrl")], Mapping.hMap1.get("hasPageUrl")[1]));
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
			
			if (!row[Mapping.hMap.get("siteName")].equals(""))
			{
				builder.defaultGraph().subject(EX.SITE + "#" + i)
									  .add(RDF.TYPE, EX.SITE)
									  .add(EX.SITE_NAME, vf.createLiteral(row[Mapping.hMap.get("siteName")], Mapping.hMap1.get("siteName")[1]));
				if (!row[Mapping.hMap.get("siteReviewCount")].equals(""))
					builder.defaultGraph().subject(EX.SITE + "#" + i)
										  .add(EX.SITE_REVIEW_COUNT, vf.createLiteral(row[Mapping.hMap.get("siteReviewCount")].substring(1), Mapping.hMap1.get("siteReviewCount")[1]));
				if (!row[Mapping.hMap.get("siteReviewRating")].equals(""))
					builder.defaultGraph().subject(EX.SITE + "#" + i)
					  					  .add(EX.SITE_REVIEWANDRATING, vf.createLiteral(row[Mapping.hMap.get("siteReviewRating")], Mapping.hMap1.get("siteReviewRating")[1]));
				
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(EX.SITE + "#" + i))
						builder1.defaultGraph().subject(EX.PPROPERTY + "#" + i)
											  .add(EX.ASSOCIATED_SITE, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
			
			if (!row[Mapping.hMap.get("hasCrawlDate")].equals("")) 
			{
				builder.defaultGraph().subject(EX.SITECRAWL + "#" + i)
									  .add(RDF.TYPE, EX.SITECRAWL)
									  .add(EX.HAS_CRAWL_DATE, vf.createLiteral(row[Mapping.hMap.get("hasCrawlDate")], Mapping.hMap1.get("hasCrawlDate")[1]));
				if (!row[Mapping.hMap.get("qts")].equals(""))
					builder.defaultGraph().subject(EX.SITECRAWL + "#" + i)
										  .add(EX.QTS, vf.createLiteral(row[Mapping.hMap.get("qts")], Mapping.hMap1.get("qts")[1]));
				if (!row[Mapping.hMap.get("queryTimeStamp")].equals(""))
					builder.defaultGraph().subject(EX.SITECRAWL + "#" + i)
										  .add(EX.QUERY_TIMESTAMP, vf.createLiteral(row[Mapping.hMap.get("queryTimeStamp")], Mapping.hMap1.get("queryTimeStamp")[1]));
				
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(EX.SITECRAWL + "#" + i))
						builder1.defaultGraph().subject(EX.SITE + "#" + i)
											  .add(EX.HAS_CRAWLED, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
			
			if (!row[Mapping.hMap.get("highlightsOverview")].equals("") || !row[Mapping.hMap.get("highlightValue")].equals("") || !row[Mapping.hMap.get("isValuePlus")].equals("") 
					|| !row[Mapping.hMap.get("roomAccessories")].equals("") || !row[Mapping.hMap.get("starRating")].equals("") || !row[Mapping.hMap.get("travellerRating")].equals(""))
			{
				builder.defaultGraph().subject(EX.PROPERTY_HIGHLIGHTS + "#" + i)
				  					  .add(RDF.TYPE, EX.PROPERTY_HIGHLIGHTS);
				
				if (!row[Mapping.hMap.get("highlightsOverview")].equals(""))
					builder.defaultGraph().subject(EX.PROPERTY_HIGHLIGHTS + "#" + i)
										  .add(EX.HIGHLIGHT_OVERVIEW, vf.createLiteral(row[Mapping.hMap.get("highlightsOverview")], Mapping.hMap1.get("highlightsOverview")[1]));
				
				if (!row[Mapping.hMap.get("highlightValue")].equals("") && !row[4].equals("{{facility}}"))
					builder.defaultGraph().subject(EX.PROPERTY_HIGHLIGHTS + "#" + i)
										  .add(EX.HIGHLIGHT_VALUE, vf.createLiteral(row[Mapping.hMap.get("highlightValue")], Mapping.hMap1.get("highlightValue")[1]));
				
				if (!row[Mapping.hMap.get("isValuePlus")].equals(""))
					builder.defaultGraph().subject(EX.PROPERTY_HIGHLIGHTS + "#" + i)
										  .add(EX.IS_VALUE_PLUS, vf.createLiteral(row[Mapping.hMap.get("isValuePlus")], Mapping.hMap1.get("isValuePlus")[1]));
				
				if (!row[Mapping.hMap.get("roomAccessories")].equals("") && !row[Mapping.hMap.get("roomAccessories")].equals("{{value}}"))
					builder.defaultGraph().subject(EX.PROPERTY_HIGHLIGHTS + "#" + i)
										  .add(EX.ROOM_ACCESSORIES, vf.createLiteral(row[Mapping.hMap.get("roomAccessories")], Mapping.hMap1.get("roomAccessories")[1]));
				
				if (!row[Mapping.hMap.get("starRating")].equals(""))
					builder.defaultGraph().subject(EX.PROPERTY_HIGHLIGHTS + "#" + i)
					  					  .add(EX.HAS_STAR_RATING, vf.createLiteral(row[Mapping.hMap.get("starRating")], Mapping.hMap1.get("starRating")[1]));
				
				if (!row[Mapping.hMap.get("travellerRating")].equals(""))
					builder.defaultGraph().subject(EX.PROPERTY_HIGHLIGHTS + "#" + i)
					  					  .add(EX.HAS_TRAVELLER_RATING, vf.createLiteral(row[Mapping.hMap.get("travellerRating")], Mapping.hMap1.get("travellerRating")[1]));
				
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
			
			if (!row[Mapping.hMap.get("holidayReviewCount")].equals("") || !row[Mapping.hMap.get("locationRating")].equals("") || !row[Mapping.hMap.get("reviewCount")].equals("") 
					|| !row[Mapping.hMap.get("reviewRating")].equals("") || !row[Mapping.hMap.get("reviewScore")].equals("") || !row[Mapping.hMap.get("tripAdvisorCount")].equals(""))
			{
				builder.defaultGraph().subject(EX.REVIEWANDRATING + "#" + i)
									  .add(RDF.TYPE, EX.REVIEWANDRATING);
				
				if (!row[Mapping.hMap.get("holidayReviewCount")].equals(""))
					builder.defaultGraph().subject(EX.REVIEWANDRATING + "#" + i)
										  .add(EX.HOLIDAY_REVIEW_COUNT, vf.createLiteral(row[Mapping.hMap.get("holidayReviewCount")], Mapping.hMap1.get("holidayReviewCount")[1]));
				
				if (!row[Mapping.hMap.get("locationRating")].equals("") && !row[Mapping.hMap.get("locationRating")].equals(".."))
					builder.defaultGraph().subject(EX.REVIEWANDRATING + "#" + i)
										  .add(EX.LOCATION_RATING, vf.createLiteral(row[Mapping.hMap.get("locationRating")], Mapping.hMap1.get("locationRating")[1]));
				
				if (!row[Mapping.hMap.get("reviewCount")].equals(""))
					builder.defaultGraph().subject(EX.REVIEWANDRATING + "#" + i)
										  .add(EX.REVIEW_COUNT, vf.createLiteral(row[Mapping.hMap.get("reviewCount")], Mapping.hMap1.get("reviewCount")[1]));
				
				if (!row[Mapping.hMap.get("reviewRating")].equals("") && !row[Mapping.hMap.get("reviewRating")].equals("{{ratingCriteria.name}}{{ratingCriteria.value}}"))
					builder.defaultGraph().subject(EX.REVIEWANDRATING + "#" + i)
										  .add(EX.REVIEW_RATING, vf.createLiteral(row[Mapping.hMap.get("reviewRating")], Mapping.hMap1.get("reviewRating")[1]));
				
				if (!row[Mapping.hMap.get("reviewScore")].equals(""))
					builder.defaultGraph().subject(EX.REVIEWANDRATING + "#" + i)
										  .add(EX.REVIEW_SCORE, vf.createLiteral(row[Mapping.hMap.get("reviewScore")], Mapping.hMap1.get("reviewScore")[1]));
				
				if (!row[Mapping.hMap.get("tripAdvisorCount")].equals(""))
					builder.defaultGraph().subject(EX.REVIEWANDRATING + "#" + i)
										  .add(EX.TRIP_ADVISOR_COUNT, vf.createLiteral(row[Mapping.hMap.get("tripAdvisorCount")], Mapping.hMap1.get("tripAdvisorCount")[1]));
				
				ModelBuilder builder1 = new ModelBuilder();
				for (Statement stmt : builder.defaultGraph().build()) {
					if (stmt.getSubject().toString().equals(EX.REVIEWANDRATING + "#" + i))
						builder1.defaultGraph().subject(baseIRI + row[Mapping.hMap.get("siteName")])
											  .add(EX.HAS_REVIEWANDRATING, stmt.getSubject());
				}
				for (Statement stmt : builder1.defaultGraph().build()) {
					builder.defaultGraph().add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
				}
			}
			
			if (!row[Mapping.hMap.get("hasTravellerReviewCount")].equals("") && !row[Mapping.hMap.get("hasTravellerReviewCount")].equals("Families:{{ratingSummaryInfo.miscMap['family']}}|"
					+ "Couples:{{ratingSummaryInfo.miscMap['couple']}}|"
					+ "Business:{{ratingSummaryInfo.miscMap['business']}}|"
					+ "Solo:{{ratingSummaryInfo.miscMap['solo']}}|"
					+ "Friends:{{ratingSummaryInfo.miscMap['friends']}}"))
			{
				String[] parts = row[Mapping.hMap.get("hasTravellerReviewCount")].split("\\|");
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
								builder.defaultGraph().subject(EX.FAMILY + "#" + i).add(EX.TRAVELLER_REVIEW_COUNT, vf.createLiteral(parts1[1], Mapping.hMap1.get("hasTravellerReviewCount")[1]));
						}
						if (parts1[0].equalsIgnoreCase("Couple") || parts1[0].equalsIgnoreCase("Couples"))
						{
							builder.defaultGraph().subject(EX.COUPLE + "#" + i)
												  .add(RDF.TYPE, EX.COUPLE);
							if (!parts1[1].equals(""))
								builder.defaultGraph().subject(EX.COUPLE + "#" + i).add(EX.TRAVELLER_REVIEW_COUNT, vf.createLiteral(parts1[1], Mapping.hMap1.get("hasTravellerReviewCount")[1]));
						}
						if (parts1[0].equalsIgnoreCase("Solo"))
						{
							builder.defaultGraph().subject(EX.SOLO + "#" + i)
												  .add(RDF.TYPE, EX.SOLO);
							if (!parts1[1].equals(""))
								builder.defaultGraph().subject(EX.SOLO + "#" + i).add(EX.TRAVELLER_REVIEW_COUNT, vf.createLiteral(parts1[1], Mapping.hMap1.get("hasTravellerReviewCount")[1]));
						}
						if (parts1[0].equalsIgnoreCase("Business"))
						{
							builder.defaultGraph().subject(EX.BUSINESS + "#" + i)
												  .add(RDF.TYPE, EX.BUSINESS);
							if (!parts1[1].equals(""))
								builder.defaultGraph().subject(EX.BUSINESS + "#" + i).add(EX.TRAVELLER_REVIEW_COUNT, vf.createLiteral(parts1[1], Mapping.hMap1.get("hasTravellerReviewCount")[1]));
						}
						if (parts1[0].equalsIgnoreCase("Friends"))
						{
							builder.defaultGraph().subject(EX.FRIENDS + "#" + i)
												  .add(RDF.TYPE, EX.FRIENDS);
							if (!parts1[1].equals(""))
								builder.defaultGraph().subject(EX.FRIENDS + "#" + i).add(EX.TRAVELLER_REVIEW_COUNT, vf.createLiteral(parts1[1], Mapping.hMap1.get("hasTravellerReviewCount")[1]));
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
			
			if (!row[Mapping.hMap.get("roomInfo")].equals("")) 
			{
				builder.defaultGraph().subject(EX.ROOMTYPE + "#" + i)
									  .add(RDF.TYPE, EX.ROOMTYPE);
				if (row[26].contains("|")) {
					String[] parts = row[Mapping.hMap.get("roomInfo")].split("\\|");
					for (String room : parts)
					{
						builder.defaultGraph().subject(EX.ROOMTYPE + "#" + i)
											  .add(EX.ROOM_INFO, vf.createLiteral(room, Mapping.hMap1.get("roomInfo")[1]));
					}
				}
				else
					builder.defaultGraph().subject(EX.ROOMTYPE + "#" + i)
					  					  .add(EX.ROOM_INFO, vf.createLiteral(row[Mapping.hMap.get("roomInfo")], Mapping.hMap1.get("roomInfo")[1]));
				
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
//          i++;
		  System.out.println(i);
			}
          if (i == 15000)
        	  break;
          else
				i++;
			
		}
		
	}
	
	// CSV Parser
	public static void parseCSV() {
		try {
			FileReader filereader = new FileReader("./makemytrip_com-travel_sample.csv");
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			List<String[]> allData = csvReader.readAll(); 
			ArrayList<String> propertyNames = new ArrayList<String>();
			ArrayList<String> areaNames = new ArrayList<String>();
			//System.out.println(allData.size());
			createInstances(allData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readQuery(String filename) {
		try {
			FileReader reader = new FileReader(filename);
			BufferedReader br = new BufferedReader(reader);
			String line, queryString = "";
			while ((line = br.readLine()) != null)
			{
				queryString += line + "\n";
			}
			System.out.println(queryString);
			queryRDFdb(filename, queryString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void queryRDFdb(String filename, String queryString) {
//		TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
//		TupleQueryResult result = tupleQuery.evaluate();
		GraphQuery query = conn.prepareGraphQuery(QueryLanguage.SPARQL, queryString);
	    try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename.substring(0, filename.length()-4) + "_output.txt"));
			RDFHandler turtleWriter = Rio.createWriter(RDFFormat.TURTLE, writer);
		    query.evaluate(turtleWriter);
		    RDFHandler turtleWriter1 = Rio.createWriter(RDFFormat.TURTLE, System.out);
		    query.evaluate(turtleWriter1);
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File dataDir = new File("./sweb");
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
			//System.out.println(stmt);
			i += 1;
			if (i==7)
				break;
		}
//		Rio.write(model, System.out, RDFFormat.TURTLE);
		System.out.println(i);
		
		conn = repo.getConnection();
		conn.add(model);
		String queryString = "SELECT (COUNT(*) AS ?triples) WHERE { ?s ?p ?o }";
		TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
		TupleQueryResult result = tupleQuery.evaluate();
		String v = result.next().getValue("triples").stringValue();
		result.close();
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("./sparql_output.txt"));
			writer.write("Total No. of Triples: ");
			writer.append(String.valueOf(Long.parseLong(v)));
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Long.parseLong(v));
		
//		for (int i = 1; i < 11; i++)
//		readQuery("/home/ritu/query" + 6 + ".txt");
	}
}
