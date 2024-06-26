/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.schema;  
@SuppressWarnings("all")
/** Union field type. */
@org.apache.avro.specific.AvroGenerated
public class UnionType extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"UnionType\",\"namespace\":\"com.linkedin.pegasus2avro.schema\",\"doc\":\"Union field type.\",\"fields\":[{\"name\":\"nestedTypes\",\"type\":[\"null\",{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"doc\":\"List of types in union type.\",\"default\":null}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** List of types in union type. */
  @Deprecated public java.util.List<java.lang.String> nestedTypes;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public UnionType() {}

  /**
   * All-args constructor.
   */
  public UnionType(java.util.List<java.lang.String> nestedTypes) {
    this.nestedTypes = nestedTypes;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return nestedTypes;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: nestedTypes = (java.util.List<java.lang.String>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'nestedTypes' field.
   * List of types in union type.   */
  public java.util.List<java.lang.String> getNestedTypes() {
    return nestedTypes;
  }

  /**
   * Sets the value of the 'nestedTypes' field.
   * List of types in union type.   * @param value the value to set.
   */
  public void setNestedTypes(java.util.List<java.lang.String> value) {
    this.nestedTypes = value;
  }

  /** Creates a new UnionType RecordBuilder */
  public static com.linkedin.pegasus2avro.schema.UnionType.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.schema.UnionType.Builder();
  }
  
  /** Creates a new UnionType RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.schema.UnionType.Builder newBuilder(com.linkedin.pegasus2avro.schema.UnionType.Builder other) {
    return new com.linkedin.pegasus2avro.schema.UnionType.Builder(other);
  }
  
  /** Creates a new UnionType RecordBuilder by copying an existing UnionType instance */
  public static com.linkedin.pegasus2avro.schema.UnionType.Builder newBuilder(com.linkedin.pegasus2avro.schema.UnionType other) {
    return new com.linkedin.pegasus2avro.schema.UnionType.Builder(other);
  }
  
  /**
   * RecordBuilder for UnionType instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<UnionType>
    implements org.apache.avro.data.RecordBuilder<UnionType> {

    private java.util.List<java.lang.String> nestedTypes;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.schema.UnionType.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.schema.UnionType.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.nestedTypes)) {
        this.nestedTypes = data().deepCopy(fields()[0].schema(), other.nestedTypes);
        fieldSetFlags()[0] = true;
      }
    }
    
    /** Creates a Builder by copying an existing UnionType instance */
    private Builder(com.linkedin.pegasus2avro.schema.UnionType other) {
            super(com.linkedin.pegasus2avro.schema.UnionType.SCHEMA$);
      if (isValidValue(fields()[0], other.nestedTypes)) {
        this.nestedTypes = data().deepCopy(fields()[0].schema(), other.nestedTypes);
        fieldSetFlags()[0] = true;
      }
    }

    /** Gets the value of the 'nestedTypes' field */
    public java.util.List<java.lang.String> getNestedTypes() {
      return nestedTypes;
    }
    
    /** Sets the value of the 'nestedTypes' field */
    public com.linkedin.pegasus2avro.schema.UnionType.Builder setNestedTypes(java.util.List<java.lang.String> value) {
      validate(fields()[0], value);
      this.nestedTypes = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'nestedTypes' field has been set */
    public boolean hasNestedTypes() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'nestedTypes' field */
    public com.linkedin.pegasus2avro.schema.UnionType.Builder clearNestedTypes() {
      nestedTypes = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    @Override
    public UnionType build() {
      try {
        UnionType record = new UnionType();
        record.nestedTypes = fieldSetFlags()[0] ? this.nestedTypes : (java.util.List<java.lang.String>) defaultValue(fields()[0]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
