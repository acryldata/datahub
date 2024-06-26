/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.usage;  
@SuppressWarnings("all")
/**  Records field-level usage counts for a given resource  */
@org.apache.avro.specific.AvroGenerated
public class FieldUsageCounts extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"FieldUsageCounts\",\"namespace\":\"com.linkedin.pegasus2avro.usage\",\"doc\":\" Records field-level usage counts for a given resource \",\"fields\":[{\"name\":\"fieldName\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"count\",\"type\":\"int\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.String fieldName;
  @Deprecated public int count;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public FieldUsageCounts() {}

  /**
   * All-args constructor.
   */
  public FieldUsageCounts(java.lang.String fieldName, java.lang.Integer count) {
    this.fieldName = fieldName;
    this.count = count;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return fieldName;
    case 1: return count;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: fieldName = (java.lang.String)value$; break;
    case 1: count = (java.lang.Integer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'fieldName' field.
   */
  public java.lang.String getFieldName() {
    return fieldName;
  }

  /**
   * Sets the value of the 'fieldName' field.
   * @param value the value to set.
   */
  public void setFieldName(java.lang.String value) {
    this.fieldName = value;
  }

  /**
   * Gets the value of the 'count' field.
   */
  public java.lang.Integer getCount() {
    return count;
  }

  /**
   * Sets the value of the 'count' field.
   * @param value the value to set.
   */
  public void setCount(java.lang.Integer value) {
    this.count = value;
  }

  /** Creates a new FieldUsageCounts RecordBuilder */
  public static com.linkedin.pegasus2avro.usage.FieldUsageCounts.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.usage.FieldUsageCounts.Builder();
  }
  
  /** Creates a new FieldUsageCounts RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.usage.FieldUsageCounts.Builder newBuilder(com.linkedin.pegasus2avro.usage.FieldUsageCounts.Builder other) {
    return new com.linkedin.pegasus2avro.usage.FieldUsageCounts.Builder(other);
  }
  
  /** Creates a new FieldUsageCounts RecordBuilder by copying an existing FieldUsageCounts instance */
  public static com.linkedin.pegasus2avro.usage.FieldUsageCounts.Builder newBuilder(com.linkedin.pegasus2avro.usage.FieldUsageCounts other) {
    return new com.linkedin.pegasus2avro.usage.FieldUsageCounts.Builder(other);
  }
  
  /**
   * RecordBuilder for FieldUsageCounts instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<FieldUsageCounts>
    implements org.apache.avro.data.RecordBuilder<FieldUsageCounts> {

    private java.lang.String fieldName;
    private int count;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.usage.FieldUsageCounts.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.usage.FieldUsageCounts.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.fieldName)) {
        this.fieldName = data().deepCopy(fields()[0].schema(), other.fieldName);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.count)) {
        this.count = data().deepCopy(fields()[1].schema(), other.count);
        fieldSetFlags()[1] = true;
      }
    }
    
    /** Creates a Builder by copying an existing FieldUsageCounts instance */
    private Builder(com.linkedin.pegasus2avro.usage.FieldUsageCounts other) {
            super(com.linkedin.pegasus2avro.usage.FieldUsageCounts.SCHEMA$);
      if (isValidValue(fields()[0], other.fieldName)) {
        this.fieldName = data().deepCopy(fields()[0].schema(), other.fieldName);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.count)) {
        this.count = data().deepCopy(fields()[1].schema(), other.count);
        fieldSetFlags()[1] = true;
      }
    }

    /** Gets the value of the 'fieldName' field */
    public java.lang.String getFieldName() {
      return fieldName;
    }
    
    /** Sets the value of the 'fieldName' field */
    public com.linkedin.pegasus2avro.usage.FieldUsageCounts.Builder setFieldName(java.lang.String value) {
      validate(fields()[0], value);
      this.fieldName = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'fieldName' field has been set */
    public boolean hasFieldName() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'fieldName' field */
    public com.linkedin.pegasus2avro.usage.FieldUsageCounts.Builder clearFieldName() {
      fieldName = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'count' field */
    public java.lang.Integer getCount() {
      return count;
    }
    
    /** Sets the value of the 'count' field */
    public com.linkedin.pegasus2avro.usage.FieldUsageCounts.Builder setCount(int value) {
      validate(fields()[1], value);
      this.count = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'count' field has been set */
    public boolean hasCount() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'count' field */
    public com.linkedin.pegasus2avro.usage.FieldUsageCounts.Builder clearCount() {
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    public FieldUsageCounts build() {
      try {
        FieldUsageCounts record = new FieldUsageCounts();
        record.fieldName = fieldSetFlags()[0] ? this.fieldName : (java.lang.String) defaultValue(fields()[0]);
        record.count = fieldSetFlags()[1] ? this.count : (java.lang.Integer) defaultValue(fields()[1]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
