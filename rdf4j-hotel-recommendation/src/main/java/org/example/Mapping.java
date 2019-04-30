package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;

public class Mapping {
	public static HashMap<String, Integer> hMap = ReadMapping();
	public static HashMap<String, IRI[]> hMap1 = ReadMappingDR();
	
	public static HashMap<String, Integer> ReadMapping() {
		FileReader reader;
		try {
			HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
			reader = new FileReader("./MappingFile.txt");
			BufferedReader br = new BufferedReader(reader);
			String line;
			int j = 0;
			while ((line = br.readLine()) != null) {
				if (j>8)
				{
//					System.out.println(line);
					String[] parts = line.split("\t\t");
//					System.out.println(parts[5]);
					String key = parts[3].split(" ")[0].trim();
//					System.out.println(key);
					int val = Integer.parseInt(parts[1].split(" ")[1].replaceAll("\\p{P}",""));
					hashMap.put(key, val);
//					System.out.println(val);
//					for (String k : hashMap.keySet()) 
//					{
//						System.out.println(k + " " + hashMap.get(k));
//					}
//					break;
				}
				j++;
			}
//			while ((line = br.readLine()) != null) {
//				if (i>8)
//				{
//					
//				}
//				i++;
//			}
			br.close();
			return hashMap;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static HashMap<String, IRI[]> ReadMappingDR() { // DR - Domain and Range
		FileReader reader;
		try {
			HashMap<String, IRI[]> hashMap = new HashMap<String, IRI[]>();
			reader = new FileReader("/home/ritu/MappingFile.txt");
			BufferedReader br = new BufferedReader(reader);
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				if (i>8)
				{
					String[] parts = line.split("\t\t");
//					System.out.println(parts[3] + " " +parts[5].toString().equals("Double"));
					String key = parts[3].split(" ")[0].trim();
					IRI[] iri = new IRI[2];
					iri[0] = getClassesIRI(parts[4]);
					iri[1] = getXMLDatatypes(parts[5]);
					hashMap.put(key, iri);
//					for (String k : hashMap.keySet()) 
//					{
//						System.out.println(k + " " + hashMap.get(k)[0] + " " + hashMap.get(k)[1]);
//					}
//					break;
				}
				i++;
			}
			br.close();
			return hashMap;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static IRI getXMLDatatypes(String s)
	{
		IRI iri = null;
		switch(s) 
		{
		case "String":	iri = XMLSchema.STRING;
		break;
		case "Integer": iri = XMLSchema.INT;
		break;
		case "Double": iri = XMLSchema.DOUBLE;
		break;
		case "Date": iri = XMLSchema.DATE;
		break;
		case "DateTime": iri = XMLSchema.DATETIME;
		break;
		case "UnsignedLong": iri = XMLSchema.UNSIGNED_LONG;
		break;
		case "URI": iri = XMLSchema.ANYURI;
		break;
		}
		return iri;
	}
	
	public static IRI getClassesIRI(String s)
	{
		IRI iri = null;
		switch(s)
		{
		case "Property": iri = EX.PPROPERTY;
		break;
		case "PropertyHighlights": iri = EX.PROPERTY_HIGHLIGHTS;
		break;
		case "Country": iri = EX.COUNTRY;
		break;
		case "State": iri = EX.STATE;
		break;
		case "City": iri = EX.CITY;
		break;
		case "Area": iri = EX.AREA;
		break;
		case "Latitude": iri = EX.LOCATION;
		break;
		case "Longitude": iri = EX.LOCATION;
		break;
		case "Site": iri = EX.SITE;
		break;
		case "SiteCrawl": iri = EX.SITECRAWL;
		break;
		case "ReviewAndRating": iri = EX.REVIEWANDRATING;
		break;
		case "TravellerType": iri = EX.TRAVELLERTYPE;
		break;
		case "RoomType": iri = EX.ROOMTYPE;
		break;
		}
		return iri;
	}
}
