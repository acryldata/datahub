/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.common;  
@SuppressWarnings("all")
/** MLFeature Data Type */
@org.apache.avro.specific.AvroGenerated
public enum MLFeatureDataType { 
  USELESS, NOMINAL, ORDINAL, BINARY, COUNT, TIME, INTERVAL, IMAGE, VIDEO, AUDIO, TEXT, MAP, SEQUENCE, SET, CONTINUOUS, BYTE, UNKNOWN  ;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"enum\",\"name\":\"MLFeatureDataType\",\"namespace\":\"com.linkedin.pegasus2avro.common\",\"doc\":\"MLFeature Data Type\",\"symbols\":[\"USELESS\",\"NOMINAL\",\"ORDINAL\",\"BINARY\",\"COUNT\",\"TIME\",\"INTERVAL\",\"IMAGE\",\"VIDEO\",\"AUDIO\",\"TEXT\",\"MAP\",\"SEQUENCE\",\"SET\",\"CONTINUOUS\",\"BYTE\",\"UNKNOWN\"],\"symbolDocs\":{\"AUDIO\":\"Audio Data\",\"BINARY\":\"Binary data is discrete data that can be in only one of two categories - either yes or no, 1 or 0, off or on, etc\",\"BYTE\":\"Bytes data are binary-encoded values that can represent complex objects.\",\"CONTINUOUS\":\"Continuous data are made of uncountable values, often the result of a measurement such as height, weight, age etc.\",\"COUNT\":\"Count data is discrete whole number data - no negative numbers here.\\nCount data often has many small values, such as zero and one.\",\"IMAGE\":\"Image Data\",\"INTERVAL\":\"Interval data has equal spaces between the numbers and does not represent a temporal pattern.\\nExamples include percentages, temperatures, and income.\",\"MAP\":\"Mapping Data Type ex: dict, map\",\"NOMINAL\":\"Nominal data is made of discrete values with no numerical relationship between the different categories - mean and median are meaningless.\\nAnimal species is one example. For example, pig is not higher than bird and lower than fish.\",\"ORDINAL\":\"Ordinal data are discrete integers that can be ranked or sorted.\\nFor example, the distance between first and second may not be the same as the distance between second and third.\",\"SEQUENCE\":\"Sequence Data Type ex: list, tuple, range\",\"SET\":\"Set Data Type ex: set, frozenset\",\"TEXT\":\"Text Data\",\"TIME\":\"Time data is a cyclical, repeating continuous form of data.\\nThe relevant time features can be any period- daily, weekly, monthly, annual, etc.\",\"UNKNOWN\":\"Unknown data are data that we don't know the type for.\",\"USELESS\":\"Useless data is unique, discrete data with no potential relationship with the outcome variable.\\nA useless feature has high cardinality. An example would be bank account numbers that were generated randomly.\",\"VIDEO\":\"Video Data\"}}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
}
