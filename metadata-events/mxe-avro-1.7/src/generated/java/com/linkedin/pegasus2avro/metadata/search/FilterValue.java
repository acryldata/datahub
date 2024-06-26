/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.metadata.search;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class FilterValue extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"FilterValue\",\"namespace\":\"com.linkedin.pegasus2avro.metadata.search\",\"fields\":[{\"name\":\"value\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"entity\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"default\":null,\"java\":{\"class\":\"com.linkedin.pegasus2avro.common.urn.Urn\"}},{\"name\":\"facetCount\",\"type\":\"long\"},{\"name\":\"filtered\",\"type\":[\"null\",\"boolean\"],\"default\":null}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.String value;
  @Deprecated public java.lang.String entity;
  @Deprecated public long facetCount;
  @Deprecated public java.lang.Boolean filtered;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public FilterValue() {}

  /**
   * All-args constructor.
   */
  public FilterValue(java.lang.String value, java.lang.String entity, java.lang.Long facetCount, java.lang.Boolean filtered) {
    this.value = value;
    this.entity = entity;
    this.facetCount = facetCount;
    this.filtered = filtered;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return value;
    case 1: return entity;
    case 2: return facetCount;
    case 3: return filtered;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: value = (java.lang.String)value$; break;
    case 1: entity = (java.lang.String)value$; break;
    case 2: facetCount = (java.lang.Long)value$; break;
    case 3: filtered = (java.lang.Boolean)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'value' field.
   */
  public java.lang.String getValue() {
    return value;
  }

  /**
   * Sets the value of the 'value' field.
   * @param value the value to set.
   */
  public void setValue(java.lang.String value) {
    this.value = value;
  }

  /**
   * Gets the value of the 'entity' field.
   */
  public java.lang.String getEntity() {
    return entity;
  }

  /**
   * Sets the value of the 'entity' field.
   * @param value the value to set.
   */
  public void setEntity(java.lang.String value) {
    this.entity = value;
  }

  /**
   * Gets the value of the 'facetCount' field.
   */
  public java.lang.Long getFacetCount() {
    return facetCount;
  }

  /**
   * Sets the value of the 'facetCount' field.
   * @param value the value to set.
   */
  public void setFacetCount(java.lang.Long value) {
    this.facetCount = value;
  }

  /**
   * Gets the value of the 'filtered' field.
   */
  public java.lang.Boolean getFiltered() {
    return filtered;
  }

  /**
   * Sets the value of the 'filtered' field.
   * @param value the value to set.
   */
  public void setFiltered(java.lang.Boolean value) {
    this.filtered = value;
  }

  /** Creates a new FilterValue RecordBuilder */
  public static com.linkedin.pegasus2avro.metadata.search.FilterValue.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.metadata.search.FilterValue.Builder();
  }
  
  /** Creates a new FilterValue RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.metadata.search.FilterValue.Builder newBuilder(com.linkedin.pegasus2avro.metadata.search.FilterValue.Builder other) {
    return new com.linkedin.pegasus2avro.metadata.search.FilterValue.Builder(other);
  }
  
  /** Creates a new FilterValue RecordBuilder by copying an existing FilterValue instance */
  public static com.linkedin.pegasus2avro.metadata.search.FilterValue.Builder newBuilder(com.linkedin.pegasus2avro.metadata.search.FilterValue other) {
    return new com.linkedin.pegasus2avro.metadata.search.FilterValue.Builder(other);
  }
  
  /**
   * RecordBuilder for FilterValue instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<FilterValue>
    implements org.apache.avro.data.RecordBuilder<FilterValue> {

    private java.lang.String value;
    private java.lang.String entity;
    private long facetCount;
    private java.lang.Boolean filtered;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.metadata.search.FilterValue.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.metadata.search.FilterValue.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.value)) {
        this.value = data().deepCopy(fields()[0].schema(), other.value);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.entity)) {
        this.entity = data().deepCopy(fields()[1].schema(), other.entity);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.facetCount)) {
        this.facetCount = data().deepCopy(fields()[2].schema(), other.facetCount);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.filtered)) {
        this.filtered = data().deepCopy(fields()[3].schema(), other.filtered);
        fieldSetFlags()[3] = true;
      }
    }
    
    /** Creates a Builder by copying an existing FilterValue instance */
    private Builder(com.linkedin.pegasus2avro.metadata.search.FilterValue other) {
            super(com.linkedin.pegasus2avro.metadata.search.FilterValue.SCHEMA$);
      if (isValidValue(fields()[0], other.value)) {
        this.value = data().deepCopy(fields()[0].schema(), other.value);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.entity)) {
        this.entity = data().deepCopy(fields()[1].schema(), other.entity);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.facetCount)) {
        this.facetCount = data().deepCopy(fields()[2].schema(), other.facetCount);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.filtered)) {
        this.filtered = data().deepCopy(fields()[3].schema(), other.filtered);
        fieldSetFlags()[3] = true;
      }
    }

    /** Gets the value of the 'value' field */
    public java.lang.String getValue() {
      return value;
    }
    
    /** Sets the value of the 'value' field */
    public com.linkedin.pegasus2avro.metadata.search.FilterValue.Builder setValue(java.lang.String value) {
      validate(fields()[0], value);
      this.value = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'value' field has been set */
    public boolean hasValue() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'value' field */
    public com.linkedin.pegasus2avro.metadata.search.FilterValue.Builder clearValue() {
      value = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'entity' field */
    public java.lang.String getEntity() {
      return entity;
    }
    
    /** Sets the value of the 'entity' field */
    public com.linkedin.pegasus2avro.metadata.search.FilterValue.Builder setEntity(java.lang.String value) {
      validate(fields()[1], value);
      this.entity = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'entity' field has been set */
    public boolean hasEntity() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'entity' field */
    public com.linkedin.pegasus2avro.metadata.search.FilterValue.Builder clearEntity() {
      entity = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'facetCount' field */
    public java.lang.Long getFacetCount() {
      return facetCount;
    }
    
    /** Sets the value of the 'facetCount' field */
    public com.linkedin.pegasus2avro.metadata.search.FilterValue.Builder setFacetCount(long value) {
      validate(fields()[2], value);
      this.facetCount = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'facetCount' field has been set */
    public boolean hasFacetCount() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'facetCount' field */
    public com.linkedin.pegasus2avro.metadata.search.FilterValue.Builder clearFacetCount() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'filtered' field */
    public java.lang.Boolean getFiltered() {
      return filtered;
    }
    
    /** Sets the value of the 'filtered' field */
    public com.linkedin.pegasus2avro.metadata.search.FilterValue.Builder setFiltered(java.lang.Boolean value) {
      validate(fields()[3], value);
      this.filtered = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'filtered' field has been set */
    public boolean hasFiltered() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'filtered' field */
    public com.linkedin.pegasus2avro.metadata.search.FilterValue.Builder clearFiltered() {
      filtered = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    public FilterValue build() {
      try {
        FilterValue record = new FilterValue();
        record.value = fieldSetFlags()[0] ? this.value : (java.lang.String) defaultValue(fields()[0]);
        record.entity = fieldSetFlags()[1] ? this.entity : (java.lang.String) defaultValue(fields()[1]);
        record.facetCount = fieldSetFlags()[2] ? this.facetCount : (java.lang.Long) defaultValue(fields()[2]);
        record.filtered = fieldSetFlags()[3] ? this.filtered : (java.lang.Boolean) defaultValue(fields()[3]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
