/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.telemetry;  
@SuppressWarnings("all")
/** A simple wrapper around a String to persist the client ID for telemetry in DataHub's backend DB */
@org.apache.avro.specific.AvroGenerated
public class TelemetryClientId extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"TelemetryClientId\",\"namespace\":\"com.linkedin.pegasus2avro.telemetry\",\"doc\":\"A simple wrapper around a String to persist the client ID for telemetry in DataHub's backend DB\",\"fields\":[{\"name\":\"clientId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"A string representing the telemetry client ID\"}],\"Aspect\":{\"name\":\"telemetryClientId\"}}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** A string representing the telemetry client ID */
  @Deprecated public java.lang.String clientId;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public TelemetryClientId() {}

  /**
   * All-args constructor.
   */
  public TelemetryClientId(java.lang.String clientId) {
    this.clientId = clientId;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return clientId;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: clientId = (java.lang.String)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'clientId' field.
   * A string representing the telemetry client ID   */
  public java.lang.String getClientId() {
    return clientId;
  }

  /**
   * Sets the value of the 'clientId' field.
   * A string representing the telemetry client ID   * @param value the value to set.
   */
  public void setClientId(java.lang.String value) {
    this.clientId = value;
  }

  /** Creates a new TelemetryClientId RecordBuilder */
  public static com.linkedin.pegasus2avro.telemetry.TelemetryClientId.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.telemetry.TelemetryClientId.Builder();
  }
  
  /** Creates a new TelemetryClientId RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.telemetry.TelemetryClientId.Builder newBuilder(com.linkedin.pegasus2avro.telemetry.TelemetryClientId.Builder other) {
    return new com.linkedin.pegasus2avro.telemetry.TelemetryClientId.Builder(other);
  }
  
  /** Creates a new TelemetryClientId RecordBuilder by copying an existing TelemetryClientId instance */
  public static com.linkedin.pegasus2avro.telemetry.TelemetryClientId.Builder newBuilder(com.linkedin.pegasus2avro.telemetry.TelemetryClientId other) {
    return new com.linkedin.pegasus2avro.telemetry.TelemetryClientId.Builder(other);
  }
  
  /**
   * RecordBuilder for TelemetryClientId instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<TelemetryClientId>
    implements org.apache.avro.data.RecordBuilder<TelemetryClientId> {

    private java.lang.String clientId;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.telemetry.TelemetryClientId.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.telemetry.TelemetryClientId.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.clientId)) {
        this.clientId = data().deepCopy(fields()[0].schema(), other.clientId);
        fieldSetFlags()[0] = true;
      }
    }
    
    /** Creates a Builder by copying an existing TelemetryClientId instance */
    private Builder(com.linkedin.pegasus2avro.telemetry.TelemetryClientId other) {
            super(com.linkedin.pegasus2avro.telemetry.TelemetryClientId.SCHEMA$);
      if (isValidValue(fields()[0], other.clientId)) {
        this.clientId = data().deepCopy(fields()[0].schema(), other.clientId);
        fieldSetFlags()[0] = true;
      }
    }

    /** Gets the value of the 'clientId' field */
    public java.lang.String getClientId() {
      return clientId;
    }
    
    /** Sets the value of the 'clientId' field */
    public com.linkedin.pegasus2avro.telemetry.TelemetryClientId.Builder setClientId(java.lang.String value) {
      validate(fields()[0], value);
      this.clientId = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'clientId' field has been set */
    public boolean hasClientId() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'clientId' field */
    public com.linkedin.pegasus2avro.telemetry.TelemetryClientId.Builder clearClientId() {
      clientId = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    @Override
    public TelemetryClientId build() {
      try {
        TelemetryClientId record = new TelemetryClientId();
        record.clientId = fieldSetFlags()[0] ? this.clientId : (java.lang.String) defaultValue(fields()[0]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
