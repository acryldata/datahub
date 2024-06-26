/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.ml.metadata;  
@SuppressWarnings("all")
/** Properties associated with an ML Model Group */
@org.apache.avro.specific.AvroGenerated
public class MLModelGroupProperties extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"MLModelGroupProperties\",\"namespace\":\"com.linkedin.pegasus2avro.ml.metadata\",\"doc\":\"Properties associated with an ML Model Group\",\"fields\":[{\"name\":\"customProperties\",\"type\":{\"type\":\"map\",\"values\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"avro.java.string\":\"String\"},\"doc\":\"Custom property bag.\",\"default\":{},\"Searchable\":{\"/*\":{\"queryByDefault\":true}}},{\"name\":\"description\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"doc\":\"Documentation of the MLModelGroup\",\"default\":null,\"Searchable\":{\"fieldType\":\"TEXT\",\"hasValuesFieldName\":\"hasDescription\"}},{\"name\":\"createdAt\",\"type\":[\"null\",\"long\"],\"doc\":\"Date when the MLModelGroup was developed\",\"default\":null},{\"name\":\"version\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"VersionTag\",\"namespace\":\"com.linkedin.pegasus2avro.common\",\"doc\":\"A resource-defined string representing the resource state for the purpose of concurrency control\",\"fields\":[{\"name\":\"versionTag\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"default\":null}]}],\"doc\":\"Version of the MLModelGroup\",\"default\":null}],\"Aspect\":{\"name\":\"mlModelGroupProperties\"}}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** Custom property bag. */
  @Deprecated public java.util.Map<java.lang.String,java.lang.String> customProperties;
  /** Documentation of the MLModelGroup */
  @Deprecated public java.lang.String description;
  /** Date when the MLModelGroup was developed */
  @Deprecated public java.lang.Long createdAt;
  /** Version of the MLModelGroup */
  @Deprecated public com.linkedin.pegasus2avro.common.VersionTag version;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public MLModelGroupProperties() {}

  /**
   * All-args constructor.
   */
  public MLModelGroupProperties(java.util.Map<java.lang.String,java.lang.String> customProperties, java.lang.String description, java.lang.Long createdAt, com.linkedin.pegasus2avro.common.VersionTag version) {
    this.customProperties = customProperties;
    this.description = description;
    this.createdAt = createdAt;
    this.version = version;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return customProperties;
    case 1: return description;
    case 2: return createdAt;
    case 3: return version;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: customProperties = (java.util.Map<java.lang.String,java.lang.String>)value$; break;
    case 1: description = (java.lang.String)value$; break;
    case 2: createdAt = (java.lang.Long)value$; break;
    case 3: version = (com.linkedin.pegasus2avro.common.VersionTag)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'customProperties' field.
   * Custom property bag.   */
  public java.util.Map<java.lang.String,java.lang.String> getCustomProperties() {
    return customProperties;
  }

  /**
   * Sets the value of the 'customProperties' field.
   * Custom property bag.   * @param value the value to set.
   */
  public void setCustomProperties(java.util.Map<java.lang.String,java.lang.String> value) {
    this.customProperties = value;
  }

  /**
   * Gets the value of the 'description' field.
   * Documentation of the MLModelGroup   */
  public java.lang.String getDescription() {
    return description;
  }

  /**
   * Sets the value of the 'description' field.
   * Documentation of the MLModelGroup   * @param value the value to set.
   */
  public void setDescription(java.lang.String value) {
    this.description = value;
  }

  /**
   * Gets the value of the 'createdAt' field.
   * Date when the MLModelGroup was developed   */
  public java.lang.Long getCreatedAt() {
    return createdAt;
  }

  /**
   * Sets the value of the 'createdAt' field.
   * Date when the MLModelGroup was developed   * @param value the value to set.
   */
  public void setCreatedAt(java.lang.Long value) {
    this.createdAt = value;
  }

  /**
   * Gets the value of the 'version' field.
   * Version of the MLModelGroup   */
  public com.linkedin.pegasus2avro.common.VersionTag getVersion() {
    return version;
  }

  /**
   * Sets the value of the 'version' field.
   * Version of the MLModelGroup   * @param value the value to set.
   */
  public void setVersion(com.linkedin.pegasus2avro.common.VersionTag value) {
    this.version = value;
  }

  /** Creates a new MLModelGroupProperties RecordBuilder */
  public static com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.Builder();
  }
  
  /** Creates a new MLModelGroupProperties RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.Builder newBuilder(com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.Builder other) {
    return new com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.Builder(other);
  }
  
  /** Creates a new MLModelGroupProperties RecordBuilder by copying an existing MLModelGroupProperties instance */
  public static com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.Builder newBuilder(com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties other) {
    return new com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.Builder(other);
  }
  
  /**
   * RecordBuilder for MLModelGroupProperties instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<MLModelGroupProperties>
    implements org.apache.avro.data.RecordBuilder<MLModelGroupProperties> {

    private java.util.Map<java.lang.String,java.lang.String> customProperties;
    private java.lang.String description;
    private java.lang.Long createdAt;
    private com.linkedin.pegasus2avro.common.VersionTag version;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.customProperties)) {
        this.customProperties = data().deepCopy(fields()[0].schema(), other.customProperties);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.description)) {
        this.description = data().deepCopy(fields()[1].schema(), other.description);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.createdAt)) {
        this.createdAt = data().deepCopy(fields()[2].schema(), other.createdAt);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.version)) {
        this.version = data().deepCopy(fields()[3].schema(), other.version);
        fieldSetFlags()[3] = true;
      }
    }
    
    /** Creates a Builder by copying an existing MLModelGroupProperties instance */
    private Builder(com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties other) {
            super(com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.SCHEMA$);
      if (isValidValue(fields()[0], other.customProperties)) {
        this.customProperties = data().deepCopy(fields()[0].schema(), other.customProperties);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.description)) {
        this.description = data().deepCopy(fields()[1].schema(), other.description);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.createdAt)) {
        this.createdAt = data().deepCopy(fields()[2].schema(), other.createdAt);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.version)) {
        this.version = data().deepCopy(fields()[3].schema(), other.version);
        fieldSetFlags()[3] = true;
      }
    }

    /** Gets the value of the 'customProperties' field */
    public java.util.Map<java.lang.String,java.lang.String> getCustomProperties() {
      return customProperties;
    }
    
    /** Sets the value of the 'customProperties' field */
    public com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.Builder setCustomProperties(java.util.Map<java.lang.String,java.lang.String> value) {
      validate(fields()[0], value);
      this.customProperties = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'customProperties' field has been set */
    public boolean hasCustomProperties() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'customProperties' field */
    public com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.Builder clearCustomProperties() {
      customProperties = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'description' field */
    public java.lang.String getDescription() {
      return description;
    }
    
    /** Sets the value of the 'description' field */
    public com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.Builder setDescription(java.lang.String value) {
      validate(fields()[1], value);
      this.description = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'description' field has been set */
    public boolean hasDescription() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'description' field */
    public com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.Builder clearDescription() {
      description = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'createdAt' field */
    public java.lang.Long getCreatedAt() {
      return createdAt;
    }
    
    /** Sets the value of the 'createdAt' field */
    public com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.Builder setCreatedAt(java.lang.Long value) {
      validate(fields()[2], value);
      this.createdAt = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'createdAt' field has been set */
    public boolean hasCreatedAt() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'createdAt' field */
    public com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.Builder clearCreatedAt() {
      createdAt = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'version' field */
    public com.linkedin.pegasus2avro.common.VersionTag getVersion() {
      return version;
    }
    
    /** Sets the value of the 'version' field */
    public com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.Builder setVersion(com.linkedin.pegasus2avro.common.VersionTag value) {
      validate(fields()[3], value);
      this.version = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'version' field has been set */
    public boolean hasVersion() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'version' field */
    public com.linkedin.pegasus2avro.ml.metadata.MLModelGroupProperties.Builder clearVersion() {
      version = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    public MLModelGroupProperties build() {
      try {
        MLModelGroupProperties record = new MLModelGroupProperties();
        record.customProperties = fieldSetFlags()[0] ? this.customProperties : (java.util.Map<java.lang.String,java.lang.String>) defaultValue(fields()[0]);
        record.description = fieldSetFlags()[1] ? this.description : (java.lang.String) defaultValue(fields()[1]);
        record.createdAt = fieldSetFlags()[2] ? this.createdAt : (java.lang.Long) defaultValue(fields()[2]);
        record.version = fieldSetFlags()[3] ? this.version : (com.linkedin.pegasus2avro.common.VersionTag) defaultValue(fields()[3]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
