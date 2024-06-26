/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.metadata.recommendation;  
@SuppressWarnings("all")
/** Parameters required to render a recommendation of a given type */
@org.apache.avro.specific.AvroGenerated
public class RecommendationParams extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"RecommendationParams\",\"namespace\":\"com.linkedin.pegasus2avro.metadata.recommendation\",\"doc\":\"Parameters required to render a recommendation of a given type\",\"fields\":[{\"name\":\"searchParams\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"SearchParams\",\"doc\":\"Context to define the search recommendations\",\"fields\":[{\"name\":\"types\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},\"doc\":\"Entity types to be searched. If this is not provided, all entities will be searched.\",\"default\":[]},{\"name\":\"query\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"Search query\"},{\"name\":\"filters\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"Criterion\",\"namespace\":\"com.linkedin.pegasus2avro.metadata.query.filter\",\"doc\":\"A criterion for matching a field with given value\",\"fields\":[{\"name\":\"field\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"The name of the field that the criterion refers to\"},{\"name\":\"value\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"The value of the intended field\"},{\"name\":\"values\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},\"doc\":\"Values. one of which the intended field should match\\nNote, if values is set, the above \\\"value\\\" field will be ignored\",\"default\":[]},{\"name\":\"condition\",\"type\":{\"type\":\"enum\",\"name\":\"Condition\",\"doc\":\"The matching condition in a filter criterion\",\"symbols\":[\"CONTAIN\",\"END_WITH\",\"EQUAL\",\"IS_NULL\",\"GREATER_THAN\",\"GREATER_THAN_OR_EQUAL_TO\",\"IN\",\"LESS_THAN\",\"LESS_THAN_OR_EQUAL_TO\",\"START_WITH\"],\"symbolDocs\":{\"CONTAIN\":\"Represent the relation: String field contains value, e.g. name contains Profile\",\"END_WITH\":\"Represent the relation: String field ends with value, e.g. name ends with Event\",\"EQUAL\":\"Represent the relation: field = value, e.g. platform = hdfs\",\"GREATER_THAN\":\"Represent the relation greater than, e.g. ownerCount > 5\",\"GREATER_THAN_OR_EQUAL_TO\":\"Represent the relation greater than or equal to, e.g. ownerCount >= 5\",\"IN\":\"Represent the relation: String field is one of the array values to, e.g. name in [\\\"Profile\\\", \\\"Event\\\"]\",\"IS_NULL\":\"Represent the relation: field is null, e.g. platform is null\",\"LESS_THAN\":\"Represent the relation less than, e.g. ownerCount < 3\",\"LESS_THAN_OR_EQUAL_TO\":\"Represent the relation less than or equal to, e.g. ownerCount <= 3\",\"START_WITH\":\"Represent the relation: String field starts with value, e.g. name starts with PageView\"}},\"doc\":\"The condition for the criterion, e.g. EQUAL, START_WITH\",\"default\":\"EQUAL\"},{\"name\":\"negated\",\"type\":\"boolean\",\"doc\":\"Whether the condition should be negated\",\"default\":false}]}},\"doc\":\"Filters\",\"default\":[]}]}],\"doc\":\"Context to define the search recommendations\",\"default\":null},{\"name\":\"entityProfileParams\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"EntityProfileParams\",\"doc\":\"Context to define the entity profile page\",\"fields\":[{\"name\":\"urn\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"Urn of the entity being shown\",\"java\":{\"class\":\"com.linkedin.pegasus2avro.common.urn.Urn\"}}]}],\"doc\":\"Context to define the entity profile page\",\"default\":null},{\"name\":\"contentParams\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"ContentParams\",\"doc\":\"Params about the recommended content\",\"fields\":[{\"name\":\"count\",\"type\":\"long\",\"doc\":\"Number of entities corresponding to the recommended content\"}]}],\"doc\":\"Context about the recommendation\",\"default\":null}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** Context to define the search recommendations */
  @Deprecated public com.linkedin.pegasus2avro.metadata.recommendation.SearchParams searchParams;
  /** Context to define the entity profile page */
  @Deprecated public com.linkedin.pegasus2avro.metadata.recommendation.EntityProfileParams entityProfileParams;
  /** Context about the recommendation */
  @Deprecated public com.linkedin.pegasus2avro.metadata.recommendation.ContentParams contentParams;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public RecommendationParams() {}

  /**
   * All-args constructor.
   */
  public RecommendationParams(com.linkedin.pegasus2avro.metadata.recommendation.SearchParams searchParams, com.linkedin.pegasus2avro.metadata.recommendation.EntityProfileParams entityProfileParams, com.linkedin.pegasus2avro.metadata.recommendation.ContentParams contentParams) {
    this.searchParams = searchParams;
    this.entityProfileParams = entityProfileParams;
    this.contentParams = contentParams;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return searchParams;
    case 1: return entityProfileParams;
    case 2: return contentParams;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: searchParams = (com.linkedin.pegasus2avro.metadata.recommendation.SearchParams)value$; break;
    case 1: entityProfileParams = (com.linkedin.pegasus2avro.metadata.recommendation.EntityProfileParams)value$; break;
    case 2: contentParams = (com.linkedin.pegasus2avro.metadata.recommendation.ContentParams)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'searchParams' field.
   * Context to define the search recommendations   */
  public com.linkedin.pegasus2avro.metadata.recommendation.SearchParams getSearchParams() {
    return searchParams;
  }

  /**
   * Sets the value of the 'searchParams' field.
   * Context to define the search recommendations   * @param value the value to set.
   */
  public void setSearchParams(com.linkedin.pegasus2avro.metadata.recommendation.SearchParams value) {
    this.searchParams = value;
  }

  /**
   * Gets the value of the 'entityProfileParams' field.
   * Context to define the entity profile page   */
  public com.linkedin.pegasus2avro.metadata.recommendation.EntityProfileParams getEntityProfileParams() {
    return entityProfileParams;
  }

  /**
   * Sets the value of the 'entityProfileParams' field.
   * Context to define the entity profile page   * @param value the value to set.
   */
  public void setEntityProfileParams(com.linkedin.pegasus2avro.metadata.recommendation.EntityProfileParams value) {
    this.entityProfileParams = value;
  }

  /**
   * Gets the value of the 'contentParams' field.
   * Context about the recommendation   */
  public com.linkedin.pegasus2avro.metadata.recommendation.ContentParams getContentParams() {
    return contentParams;
  }

  /**
   * Sets the value of the 'contentParams' field.
   * Context about the recommendation   * @param value the value to set.
   */
  public void setContentParams(com.linkedin.pegasus2avro.metadata.recommendation.ContentParams value) {
    this.contentParams = value;
  }

  /** Creates a new RecommendationParams RecordBuilder */
  public static com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams.Builder();
  }
  
  /** Creates a new RecommendationParams RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams.Builder newBuilder(com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams.Builder other) {
    return new com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams.Builder(other);
  }
  
  /** Creates a new RecommendationParams RecordBuilder by copying an existing RecommendationParams instance */
  public static com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams.Builder newBuilder(com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams other) {
    return new com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams.Builder(other);
  }
  
  /**
   * RecordBuilder for RecommendationParams instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<RecommendationParams>
    implements org.apache.avro.data.RecordBuilder<RecommendationParams> {

    private com.linkedin.pegasus2avro.metadata.recommendation.SearchParams searchParams;
    private com.linkedin.pegasus2avro.metadata.recommendation.EntityProfileParams entityProfileParams;
    private com.linkedin.pegasus2avro.metadata.recommendation.ContentParams contentParams;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.searchParams)) {
        this.searchParams = data().deepCopy(fields()[0].schema(), other.searchParams);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.entityProfileParams)) {
        this.entityProfileParams = data().deepCopy(fields()[1].schema(), other.entityProfileParams);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.contentParams)) {
        this.contentParams = data().deepCopy(fields()[2].schema(), other.contentParams);
        fieldSetFlags()[2] = true;
      }
    }
    
    /** Creates a Builder by copying an existing RecommendationParams instance */
    private Builder(com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams other) {
            super(com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams.SCHEMA$);
      if (isValidValue(fields()[0], other.searchParams)) {
        this.searchParams = data().deepCopy(fields()[0].schema(), other.searchParams);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.entityProfileParams)) {
        this.entityProfileParams = data().deepCopy(fields()[1].schema(), other.entityProfileParams);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.contentParams)) {
        this.contentParams = data().deepCopy(fields()[2].schema(), other.contentParams);
        fieldSetFlags()[2] = true;
      }
    }

    /** Gets the value of the 'searchParams' field */
    public com.linkedin.pegasus2avro.metadata.recommendation.SearchParams getSearchParams() {
      return searchParams;
    }
    
    /** Sets the value of the 'searchParams' field */
    public com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams.Builder setSearchParams(com.linkedin.pegasus2avro.metadata.recommendation.SearchParams value) {
      validate(fields()[0], value);
      this.searchParams = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'searchParams' field has been set */
    public boolean hasSearchParams() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'searchParams' field */
    public com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams.Builder clearSearchParams() {
      searchParams = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'entityProfileParams' field */
    public com.linkedin.pegasus2avro.metadata.recommendation.EntityProfileParams getEntityProfileParams() {
      return entityProfileParams;
    }
    
    /** Sets the value of the 'entityProfileParams' field */
    public com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams.Builder setEntityProfileParams(com.linkedin.pegasus2avro.metadata.recommendation.EntityProfileParams value) {
      validate(fields()[1], value);
      this.entityProfileParams = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'entityProfileParams' field has been set */
    public boolean hasEntityProfileParams() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'entityProfileParams' field */
    public com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams.Builder clearEntityProfileParams() {
      entityProfileParams = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'contentParams' field */
    public com.linkedin.pegasus2avro.metadata.recommendation.ContentParams getContentParams() {
      return contentParams;
    }
    
    /** Sets the value of the 'contentParams' field */
    public com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams.Builder setContentParams(com.linkedin.pegasus2avro.metadata.recommendation.ContentParams value) {
      validate(fields()[2], value);
      this.contentParams = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'contentParams' field has been set */
    public boolean hasContentParams() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'contentParams' field */
    public com.linkedin.pegasus2avro.metadata.recommendation.RecommendationParams.Builder clearContentParams() {
      contentParams = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    public RecommendationParams build() {
      try {
        RecommendationParams record = new RecommendationParams();
        record.searchParams = fieldSetFlags()[0] ? this.searchParams : (com.linkedin.pegasus2avro.metadata.recommendation.SearchParams) defaultValue(fields()[0]);
        record.entityProfileParams = fieldSetFlags()[1] ? this.entityProfileParams : (com.linkedin.pegasus2avro.metadata.recommendation.EntityProfileParams) defaultValue(fields()[1]);
        record.contentParams = fieldSetFlags()[2] ? this.contentParams : (com.linkedin.pegasus2avro.metadata.recommendation.ContentParams) defaultValue(fields()[2]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
