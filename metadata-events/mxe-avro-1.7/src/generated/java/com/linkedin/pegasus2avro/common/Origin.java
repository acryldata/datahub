/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.common;  
@SuppressWarnings("all")
/** Carries information about where an entity originated from. */
@org.apache.avro.specific.AvroGenerated
public class Origin extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Origin\",\"namespace\":\"com.linkedin.pegasus2avro.common\",\"doc\":\"Carries information about where an entity originated from.\",\"fields\":[{\"name\":\"type\",\"type\":{\"type\":\"enum\",\"name\":\"OriginType\",\"doc\":\"Enum to define where an entity originated from.\",\"symbols\":[\"NATIVE\",\"EXTERNAL\"],\"symbolDocs\":{\"EXTERNAL\":\"The entity is external to DataHub.\",\"NATIVE\":\"The entity is native to DataHub.\"}},\"doc\":\"Where an entity originated from. Either NATIVE or EXTERNAL.\"},{\"name\":\"externalType\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"doc\":\"Only populated if type is EXTERNAL. The externalType of the entity, such as the name of the identity provider.\",\"default\":null}],\"Aspect\":{\"name\":\"origin\"}}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** Where an entity originated from. Either NATIVE or EXTERNAL. */
  @Deprecated public com.linkedin.pegasus2avro.common.OriginType type;
  /** Only populated if type is EXTERNAL. The externalType of the entity, such as the name of the identity provider. */
  @Deprecated public java.lang.String externalType;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public Origin() {}

  /**
   * All-args constructor.
   */
  public Origin(com.linkedin.pegasus2avro.common.OriginType type, java.lang.String externalType) {
    this.type = type;
    this.externalType = externalType;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return type;
    case 1: return externalType;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: type = (com.linkedin.pegasus2avro.common.OriginType)value$; break;
    case 1: externalType = (java.lang.String)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'type' field.
   * Where an entity originated from. Either NATIVE or EXTERNAL.   */
  public com.linkedin.pegasus2avro.common.OriginType getType() {
    return type;
  }

  /**
   * Sets the value of the 'type' field.
   * Where an entity originated from. Either NATIVE or EXTERNAL.   * @param value the value to set.
   */
  public void setType(com.linkedin.pegasus2avro.common.OriginType value) {
    this.type = value;
  }

  /**
   * Gets the value of the 'externalType' field.
   * Only populated if type is EXTERNAL. The externalType of the entity, such as the name of the identity provider.   */
  public java.lang.String getExternalType() {
    return externalType;
  }

  /**
   * Sets the value of the 'externalType' field.
   * Only populated if type is EXTERNAL. The externalType of the entity, such as the name of the identity provider.   * @param value the value to set.
   */
  public void setExternalType(java.lang.String value) {
    this.externalType = value;
  }

  /** Creates a new Origin RecordBuilder */
  public static com.linkedin.pegasus2avro.common.Origin.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.common.Origin.Builder();
  }
  
  /** Creates a new Origin RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.common.Origin.Builder newBuilder(com.linkedin.pegasus2avro.common.Origin.Builder other) {
    return new com.linkedin.pegasus2avro.common.Origin.Builder(other);
  }
  
  /** Creates a new Origin RecordBuilder by copying an existing Origin instance */
  public static com.linkedin.pegasus2avro.common.Origin.Builder newBuilder(com.linkedin.pegasus2avro.common.Origin other) {
    return new com.linkedin.pegasus2avro.common.Origin.Builder(other);
  }
  
  /**
   * RecordBuilder for Origin instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Origin>
    implements org.apache.avro.data.RecordBuilder<Origin> {

    private com.linkedin.pegasus2avro.common.OriginType type;
    private java.lang.String externalType;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.common.Origin.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.common.Origin.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.type)) {
        this.type = data().deepCopy(fields()[0].schema(), other.type);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.externalType)) {
        this.externalType = data().deepCopy(fields()[1].schema(), other.externalType);
        fieldSetFlags()[1] = true;
      }
    }
    
    /** Creates a Builder by copying an existing Origin instance */
    private Builder(com.linkedin.pegasus2avro.common.Origin other) {
            super(com.linkedin.pegasus2avro.common.Origin.SCHEMA$);
      if (isValidValue(fields()[0], other.type)) {
        this.type = data().deepCopy(fields()[0].schema(), other.type);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.externalType)) {
        this.externalType = data().deepCopy(fields()[1].schema(), other.externalType);
        fieldSetFlags()[1] = true;
      }
    }

    /** Gets the value of the 'type' field */
    public com.linkedin.pegasus2avro.common.OriginType getType() {
      return type;
    }
    
    /** Sets the value of the 'type' field */
    public com.linkedin.pegasus2avro.common.Origin.Builder setType(com.linkedin.pegasus2avro.common.OriginType value) {
      validate(fields()[0], value);
      this.type = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'type' field has been set */
    public boolean hasType() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'type' field */
    public com.linkedin.pegasus2avro.common.Origin.Builder clearType() {
      type = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'externalType' field */
    public java.lang.String getExternalType() {
      return externalType;
    }
    
    /** Sets the value of the 'externalType' field */
    public com.linkedin.pegasus2avro.common.Origin.Builder setExternalType(java.lang.String value) {
      validate(fields()[1], value);
      this.externalType = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'externalType' field has been set */
    public boolean hasExternalType() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'externalType' field */
    public com.linkedin.pegasus2avro.common.Origin.Builder clearExternalType() {
      externalType = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    public Origin build() {
      try {
        Origin record = new Origin();
        record.type = fieldSetFlags()[0] ? this.type : (com.linkedin.pegasus2avro.common.OriginType) defaultValue(fields()[0]);
        record.externalType = fieldSetFlags()[1] ? this.externalType : (java.lang.String) defaultValue(fields()[1]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
