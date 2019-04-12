package org.example;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

public class EX {
	public static final String baseIRI = "http://www.semanticweb.org/ontologies/2019/hotel-recommendation/";
	
	// IRI of Classes
	
	public static final IRI PPROPERTY = getIRI("property");
	
	public static final IRI GEOGRAPHIC_ENTITY = getIRI("geographicEntity");
	
	public static final IRI COUNTRY = getIRI("country");
	
	public static final IRI STATE = getIRI("state");
	
	public static final IRI CITY = getIRI("city");
	
	public static final IRI AREA = getIRI("area");
	
	public static final IRI IMAGE = getIRI("image");
	
	public static final IRI PAGE = getIRI("page");
	
	public static final IRI LOCATION = getIRI("location");
	
	public static final IRI RESOURCE = getIRI("resource");
	
	public static final IRI PROPERTY_HIGHLIGHTS = getIRI("propertyHighlights");
	
	public static final IRI REVIEWANDRATING = getIRI("reviewAndRating");
	
	public static final IRI ROOMTYPE = getIRI("roomType");
	
	public static final IRI SITE = getIRI("site");
	
	public static final IRI SITECRAWL = getIRI("siteCrawl");
	
	public static final IRI TRAVELLERTYPE = getIRI("travellerType");
	
	public static final IRI FAMILY = getIRI("family");
	
	public static final IRI COUPLE = getIRI("couple");
	
	public static final IRI SOLO = getIRI("solo");
	
	public static final IRI BUSINESS = getIRI("business");
	
	public static final IRI FRIENDS = getIRI("friends");
	
	// IRI of Object properties

	public static final IRI HAS_CRAWLED= getIRI("hasCrawled");

	public static final IRI LOCATED_AT = getIRI("isLocatedAt");

	public static final IRI HAS_AREA = getIRI("hasArea");
	
	public static final IRI HAS_CITY = getIRI("hasCity");
	
	public static final IRI HAS_STATE = getIRI("hasState");

	public static final IRI SITUATED_IN = getIRI("situatedIn");

	public static final IRI SITUATED_IN_AREA = getIRI("situatedInArea");
	
	public static final IRI SITUATED_IN_CITY = getIRI("situatedInCity");
	
	public static final IRI SITUATED_IN_STATE = getIRI("situatedInState");
	
	public static final IRI SITUATED_IN_COUNTRY = getIRI("situatedInCountry");
	
	public static final IRI HAS_ROOM_TYPE = getIRI("hasRoomType");
	
	public static final IRI ASSOCIATED_SITE = getIRI("associatedSite");
	
	public static final IRI HAS_HIGHLIGHTS = getIRI("hasHighlights");
	
	public static final IRI HAS_REVIEWANDRATING = getIRI("hasReviewAndRating");
	
	public static final IRI HAS_TRAVELLERS = getIRI("hasTravellers");
	
	public static final IRI IMAGE_INFO = getIRI("imageInfo");
	
	public static final IRI PAGE_INFO = getIRI("pageInfo");
	
	public static final IRI MORE_INFO = getIRI("moreInfo");


	// IRI of Data properties

	public static final IRI HAS_CRAWL_DATE = getIRI("crawlDate");
	
	public static final IRI SITE_REVIEWANDRATING = getIRI("siteReviewRating");
	
	public static final IRI PAGE_URL = getIRI("hasPageUrl");
	
	public static final IRI IMAGE_URL = getIRI("hasImageUrl");
	
	public static final IRI AREA_NAME = getIRI("areaName");

	public static final IRI CITY_NAME = getIRI("cityName");
	
	public static final IRI STATE_NAME = getIRI("stateName");
	
	public static final IRI COUNTRY_NAME = getIRI("countryName");
	
	public static final IRI TRAVELLER_REVIEW_COUNT = getIRI("hasTravellerReviewCount");
	
	public static final IRI HIGHLIGHT_OVERVIEW = getIRI("highlightOverview");
	
	public static final IRI HIGHLIGHT_VALUE = getIRI("highlightValue");
	
	public static final IRI HOLIDAY_REVIEW_COUNT = getIRI("holidayReviewCount");
	
	public static final IRI IS_VALUE_PLUS = getIRI("isValuePlus");
	
	public static final IRI LOCATION_RATING = getIRI("locationRating");
	
	public static final IRI QTS = getIRI("qts");
	
	public static final IRI QUERY_TIMESTAMP = getIRI("queryTimeStamp");
	
	public static final IRI REVIEW_COUNT = getIRI("reviewCount");
	
	public static final IRI REVIEW_RATING = getIRI("reviewRating");
	
	public static final IRI REVIEW_SCORE = getIRI("reviewScore");
	
	public static final IRI ROOM_ACCESSORIES = getIRI("roomAccessories");
	
	public static final IRI SITE_NAME = getIRI("siteName");
	
	public static final IRI SITE_REVIEW_COUNT = getIRI("siteReviewCount");
	
	public static final IRI TRIP_ADVISOR_COUNT = getIRI("tripAdvisorCount");
	
	public static final IRI UNIQ_ID = getIRI("uniqId");
	
	public static final IRI HAS_LATITUDE = getIRI("hasLatitude");
	
	public static final IRI HAS_LONGITUDE = getIRI("hasLongitude");

	public static final IRI HAS_PROPERTY_ADDRESS = getIRI("propertyAdress");

	public static final IRI HAS_PROPERTY_ID = getIRI("propertyId");

	public static final IRI HAS_PROPERTY_NAME = getIRI("propertyName");
	
	public static final IRI HAS_PROPERTY_TYPE = getIRI("propertyType");

	public static final IRI HAS_STAR_RATING = getIRI("starRating");
	
	public static final IRI HAS_TRAVELLER_RATING = getIRI("travellerRating");

	public static final IRI ROOM_INFO = getIRI("roomInfo");
	
	private static IRI getIRI(String localName) {
		return SimpleValueFactory.getInstance().createIRI(baseIRI, localName);
	}
}
