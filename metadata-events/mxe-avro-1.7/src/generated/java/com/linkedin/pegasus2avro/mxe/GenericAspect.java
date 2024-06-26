/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.mxe;  
@SuppressWarnings("all")
/** Generic record structure for serializing an Aspect */
@org.apache.avro.specific.AvroGenerated
public class GenericAspect extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"GenericAspect\",\"namespace\":\"com.linkedin.pegasus2avro.mxe\",\"doc\":\"Generic record structure for serializing an Aspect\",\"fields\":[{\"name\":\"value\",\"type\":\"bytes\",\"doc\":\"The value of the aspect, serialized as bytes.\"},{\"name\":\"contentType\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"The content type, which represents the fashion in which the aspect was serialized.\\nThe only type currently supported is application/json.\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** The value of the aspect, serialized as bytes. */
  @Deprecated public java.nio.ByteBuffer value;
  /** The content type, which represents the fashion in which the aspect was serialized.
The only type currently supported is application/json. */
  @Deprecated public java.lang.String contentType;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public GenericAspect() {}

  /**
   * All-args constructor.
   */
  public GenericAspect(java.nio.ByteBuffer value, java.lang.String contentType) {
    this.value = value;
    this.contentType = contentType;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return value;
    case 1: return contentType;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: value = (java.nio.ByteBuffer)value$; break;
    case 1: contentType = (java.lang.String)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'value' field.
   * The value of the aspect, serialized as bytes.   */
  public java.nio.ByteBuffer getValue() {
    return value;
  }

  /**
   * Sets the value of the 'value' field.
   * The value of the aspect, serialized as bytes.   * @param value the value to set.
   */
  public void setValue(java.nio.ByteBuffer value) {
    this.value = value;
  }

  /**
   * Gets the value of the 'contentType' field.
   * The content type, which represents the fashion in which the aspect was serialized.
The only type currently supported is application/json.   */
  public java.lang.String getContentType() {
    return contentType;
  }

  /**
   * Sets the value of the 'contentType' field.
   * The content type, which represents the fashion in which the aspect was serialized.
The only type currently supported is application/json.   * @param value the value to set.
   */
  public void setContentType(java.lang.String value) {
    this.contentType = value;
  }

  /** Creates a new GenericAspect RecordBuilder */
  public static com.linkedin.pegasus2avro.mxe.GenericAspect.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.mxe.GenericAspect.Builder();
  }
  
  /** Creates a new GenericAspect RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.mxe.GenericAspect.Builder newBuilder(com.linkedin.pegasus2avro.mxe.GenericAspect.Builder other) {
    return new com.linkedin.pegasus2avro.mxe.GenericAspect.Builder(other);
  }
  
  /** Creates a new GenericAspect RecordBuilder by copying an existing GenericAspect instance */
  public static com.linkedin.pegasus2avro.mxe.GenericAspect.Builder newBuilder(com.linkedin.pegasus2avro.mxe.GenericAspect other) {
    return new com.linkedin.pegasus2avro.mxe.GenericAspect.Builder(other);
  }
  
  /**
   * RecordBuilder for GenericAspect instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<GenericAspect>
    implements org.apache.avro.data.RecordBuilder<GenericAspect> {

    private java.nio.ByteBuffer value;
    private java.lang.String contentType;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.mxe.GenericAspect.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.mxe.GenericAspect.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.value)) {
        this.value = data().deepCopy(fields()[0].schema(), other.value);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.contentType)) {
        this.contentType = data().deepCopy(fields()[1].schema(), other.contentType);
        fieldSetFlags()[1] = true;
      }
    }
    
    /** Creates a Builder by copying an existing GenericAspect instance */
    private Builder(com.linkedin.pegasus2avro.mxe.GenericAspect other) {
            super(com.linkedin.pegasus2avro.mxe.GenericAspect.SCHEMA$);
      if (isValidValue(fields()[0], other.value)) {
        this.value = data().deepCopy(fields()[0].schema(), other.value);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.contentType)) {
        this.contentType = data().deepCopy(fields()[1].schema(), other.contentType);
        fieldSetFlags()[1] = true;
      }
    }

    /** Gets the value of the 'value' field */
    public java.nio.ByteBuffer getValue() {
      return value;
    }
    
    /** Sets the value of the 'value' field */
    public com.linkedin.pegasus2avro.mxe.GenericAspect.Builder setValue(java.nio.ByteBuffer value) {
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
    public com.linkedin.pegasus2avro.mxe.GenericAspect.Builder clearValue() {
      value = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'contentType' field */
    public java.lang.String getContentType() {
      return contentType;
    }
    
    /** Sets the value of the 'contentType' field */
    public com.linkedin.pegasus2avro.mxe.GenericAspect.Builder setContentType(java.lang.String value) {
      validate(fields()[1], value);
      this.contentType = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'contentType' field has been set */
    public boolean hasContentType() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'contentType' field */
    public com.linkedin.pegasus2avro.mxe.GenericAspect.Builder clearContentType() {
      contentType = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    public GenericAspect build() {
      try {
        GenericAspect record = new GenericAspect();
        record.value = fieldSetFlags()[0] ? this.value : (java.nio.ByteBuffer) defaultValue(fields()[0]);
        record.contentType = fieldSetFlags()[1] ? this.contentType : (java.lang.String) defaultValue(fields()[1]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
