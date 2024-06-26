/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.metadata.query;  
@SuppressWarnings("all")
/** The model for the result of a browse query */
@org.apache.avro.specific.AvroGenerated
public class BrowseResult extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"BrowseResult\",\"namespace\":\"com.linkedin.pegasus2avro.metadata.query\",\"doc\":\"The model for the result of a browse query\",\"fields\":[{\"name\":\"entities\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"BrowseResultEntity\",\"doc\":\"Data model for an entity returned as part of a browse query\",\"fields\":[{\"name\":\"name\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"doc\":\"Name of the entity\",\"default\":null},{\"name\":\"urn\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"URN of the entity\",\"java\":{\"class\":\"com.linkedin.pegasus2avro.common.urn.Urn\"}}]}},\"doc\":\"A list of entities under the queried path\"},{\"name\":\"metadata\",\"type\":{\"type\":\"record\",\"name\":\"BrowseResultMetadata\",\"doc\":\"The model for browse result metadata\",\"fields\":[{\"name\":\"path\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"Path that is being browsed\"},{\"name\":\"groups\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"BrowseResultGroup\",\"fields\":[{\"name\":\"name\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"Name of the group\"},{\"name\":\"count\",\"type\":\"long\",\"doc\":\"Number of entities that can be reached from this path\"}]}},\"doc\":\"A list of groups and total number of entities inside those groups under the queried path\",\"default\":[]},{\"name\":\"totalNumEntities\",\"type\":\"long\",\"doc\":\"Total number of entities we can reach from path\"}]},\"doc\":\"Metadata specific to the browse result of the queried path\"},{\"name\":\"from\",\"type\":\"int\",\"doc\":\"Offset of the first entity in the result\"},{\"name\":\"pageSize\",\"type\":\"int\",\"doc\":\"Size of each page in the result\"},{\"name\":\"numEntities\",\"type\":\"int\",\"doc\":\"The total number of entities directly under queried path\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** A list of entities under the queried path */
  @Deprecated public java.util.List<com.linkedin.pegasus2avro.metadata.query.BrowseResultEntity> entities;
  /** Metadata specific to the browse result of the queried path */
  @Deprecated public com.linkedin.pegasus2avro.metadata.query.BrowseResultMetadata metadata;
  /** Offset of the first entity in the result */
  @Deprecated public int from;
  /** Size of each page in the result */
  @Deprecated public int pageSize;
  /** The total number of entities directly under queried path */
  @Deprecated public int numEntities;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public BrowseResult() {}

  /**
   * All-args constructor.
   */
  public BrowseResult(java.util.List<com.linkedin.pegasus2avro.metadata.query.BrowseResultEntity> entities, com.linkedin.pegasus2avro.metadata.query.BrowseResultMetadata metadata, java.lang.Integer from, java.lang.Integer pageSize, java.lang.Integer numEntities) {
    this.entities = entities;
    this.metadata = metadata;
    this.from = from;
    this.pageSize = pageSize;
    this.numEntities = numEntities;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return entities;
    case 1: return metadata;
    case 2: return from;
    case 3: return pageSize;
    case 4: return numEntities;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: entities = (java.util.List<com.linkedin.pegasus2avro.metadata.query.BrowseResultEntity>)value$; break;
    case 1: metadata = (com.linkedin.pegasus2avro.metadata.query.BrowseResultMetadata)value$; break;
    case 2: from = (java.lang.Integer)value$; break;
    case 3: pageSize = (java.lang.Integer)value$; break;
    case 4: numEntities = (java.lang.Integer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'entities' field.
   * A list of entities under the queried path   */
  public java.util.List<com.linkedin.pegasus2avro.metadata.query.BrowseResultEntity> getEntities() {
    return entities;
  }

  /**
   * Sets the value of the 'entities' field.
   * A list of entities under the queried path   * @param value the value to set.
   */
  public void setEntities(java.util.List<com.linkedin.pegasus2avro.metadata.query.BrowseResultEntity> value) {
    this.entities = value;
  }

  /**
   * Gets the value of the 'metadata' field.
   * Metadata specific to the browse result of the queried path   */
  public com.linkedin.pegasus2avro.metadata.query.BrowseResultMetadata getMetadata() {
    return metadata;
  }

  /**
   * Sets the value of the 'metadata' field.
   * Metadata specific to the browse result of the queried path   * @param value the value to set.
   */
  public void setMetadata(com.linkedin.pegasus2avro.metadata.query.BrowseResultMetadata value) {
    this.metadata = value;
  }

  /**
   * Gets the value of the 'from' field.
   * Offset of the first entity in the result   */
  public java.lang.Integer getFrom() {
    return from;
  }

  /**
   * Sets the value of the 'from' field.
   * Offset of the first entity in the result   * @param value the value to set.
   */
  public void setFrom(java.lang.Integer value) {
    this.from = value;
  }

  /**
   * Gets the value of the 'pageSize' field.
   * Size of each page in the result   */
  public java.lang.Integer getPageSize() {
    return pageSize;
  }

  /**
   * Sets the value of the 'pageSize' field.
   * Size of each page in the result   * @param value the value to set.
   */
  public void setPageSize(java.lang.Integer value) {
    this.pageSize = value;
  }

  /**
   * Gets the value of the 'numEntities' field.
   * The total number of entities directly under queried path   */
  public java.lang.Integer getNumEntities() {
    return numEntities;
  }

  /**
   * Sets the value of the 'numEntities' field.
   * The total number of entities directly under queried path   * @param value the value to set.
   */
  public void setNumEntities(java.lang.Integer value) {
    this.numEntities = value;
  }

  /** Creates a new BrowseResult RecordBuilder */
  public static com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder();
  }
  
  /** Creates a new BrowseResult RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder newBuilder(com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder other) {
    return new com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder(other);
  }
  
  /** Creates a new BrowseResult RecordBuilder by copying an existing BrowseResult instance */
  public static com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder newBuilder(com.linkedin.pegasus2avro.metadata.query.BrowseResult other) {
    return new com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder(other);
  }
  
  /**
   * RecordBuilder for BrowseResult instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<BrowseResult>
    implements org.apache.avro.data.RecordBuilder<BrowseResult> {

    private java.util.List<com.linkedin.pegasus2avro.metadata.query.BrowseResultEntity> entities;
    private com.linkedin.pegasus2avro.metadata.query.BrowseResultMetadata metadata;
    private int from;
    private int pageSize;
    private int numEntities;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.metadata.query.BrowseResult.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.entities)) {
        this.entities = data().deepCopy(fields()[0].schema(), other.entities);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.metadata)) {
        this.metadata = data().deepCopy(fields()[1].schema(), other.metadata);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.from)) {
        this.from = data().deepCopy(fields()[2].schema(), other.from);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.pageSize)) {
        this.pageSize = data().deepCopy(fields()[3].schema(), other.pageSize);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.numEntities)) {
        this.numEntities = data().deepCopy(fields()[4].schema(), other.numEntities);
        fieldSetFlags()[4] = true;
      }
    }
    
    /** Creates a Builder by copying an existing BrowseResult instance */
    private Builder(com.linkedin.pegasus2avro.metadata.query.BrowseResult other) {
            super(com.linkedin.pegasus2avro.metadata.query.BrowseResult.SCHEMA$);
      if (isValidValue(fields()[0], other.entities)) {
        this.entities = data().deepCopy(fields()[0].schema(), other.entities);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.metadata)) {
        this.metadata = data().deepCopy(fields()[1].schema(), other.metadata);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.from)) {
        this.from = data().deepCopy(fields()[2].schema(), other.from);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.pageSize)) {
        this.pageSize = data().deepCopy(fields()[3].schema(), other.pageSize);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.numEntities)) {
        this.numEntities = data().deepCopy(fields()[4].schema(), other.numEntities);
        fieldSetFlags()[4] = true;
      }
    }

    /** Gets the value of the 'entities' field */
    public java.util.List<com.linkedin.pegasus2avro.metadata.query.BrowseResultEntity> getEntities() {
      return entities;
    }
    
    /** Sets the value of the 'entities' field */
    public com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder setEntities(java.util.List<com.linkedin.pegasus2avro.metadata.query.BrowseResultEntity> value) {
      validate(fields()[0], value);
      this.entities = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'entities' field has been set */
    public boolean hasEntities() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'entities' field */
    public com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder clearEntities() {
      entities = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'metadata' field */
    public com.linkedin.pegasus2avro.metadata.query.BrowseResultMetadata getMetadata() {
      return metadata;
    }
    
    /** Sets the value of the 'metadata' field */
    public com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder setMetadata(com.linkedin.pegasus2avro.metadata.query.BrowseResultMetadata value) {
      validate(fields()[1], value);
      this.metadata = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'metadata' field has been set */
    public boolean hasMetadata() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'metadata' field */
    public com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder clearMetadata() {
      metadata = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'from' field */
    public java.lang.Integer getFrom() {
      return from;
    }
    
    /** Sets the value of the 'from' field */
    public com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder setFrom(int value) {
      validate(fields()[2], value);
      this.from = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'from' field has been set */
    public boolean hasFrom() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'from' field */
    public com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder clearFrom() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'pageSize' field */
    public java.lang.Integer getPageSize() {
      return pageSize;
    }
    
    /** Sets the value of the 'pageSize' field */
    public com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder setPageSize(int value) {
      validate(fields()[3], value);
      this.pageSize = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'pageSize' field has been set */
    public boolean hasPageSize() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'pageSize' field */
    public com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder clearPageSize() {
      fieldSetFlags()[3] = false;
      return this;
    }

    /** Gets the value of the 'numEntities' field */
    public java.lang.Integer getNumEntities() {
      return numEntities;
    }
    
    /** Sets the value of the 'numEntities' field */
    public com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder setNumEntities(int value) {
      validate(fields()[4], value);
      this.numEntities = value;
      fieldSetFlags()[4] = true;
      return this; 
    }
    
    /** Checks whether the 'numEntities' field has been set */
    public boolean hasNumEntities() {
      return fieldSetFlags()[4];
    }
    
    /** Clears the value of the 'numEntities' field */
    public com.linkedin.pegasus2avro.metadata.query.BrowseResult.Builder clearNumEntities() {
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    public BrowseResult build() {
      try {
        BrowseResult record = new BrowseResult();
        record.entities = fieldSetFlags()[0] ? this.entities : (java.util.List<com.linkedin.pegasus2avro.metadata.query.BrowseResultEntity>) defaultValue(fields()[0]);
        record.metadata = fieldSetFlags()[1] ? this.metadata : (com.linkedin.pegasus2avro.metadata.query.BrowseResultMetadata) defaultValue(fields()[1]);
        record.from = fieldSetFlags()[2] ? this.from : (java.lang.Integer) defaultValue(fields()[2]);
        record.pageSize = fieldSetFlags()[3] ? this.pageSize : (java.lang.Integer) defaultValue(fields()[3]);
        record.numEntities = fieldSetFlags()[4] ? this.numEntities : (java.lang.Integer) defaultValue(fields()[4]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
