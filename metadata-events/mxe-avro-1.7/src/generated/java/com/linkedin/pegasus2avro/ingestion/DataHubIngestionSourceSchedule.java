/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.ingestion;  
@SuppressWarnings("all")
/** The schedule associated with an ingestion source. */
@org.apache.avro.specific.AvroGenerated
public class DataHubIngestionSourceSchedule extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"DataHubIngestionSourceSchedule\",\"namespace\":\"com.linkedin.pegasus2avro.ingestion\",\"doc\":\"The schedule associated with an ingestion source.\",\"fields\":[{\"name\":\"interval\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"A cron-formatted execution interval, as a cron string, e.g. * * * * *\"},{\"name\":\"timezone\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"Timezone in which the cron interval applies, e.g. America/Los Angeles\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** A cron-formatted execution interval, as a cron string, e.g. * * * * * */
  @Deprecated public java.lang.String interval;
  /** Timezone in which the cron interval applies, e.g. America/Los Angeles */
  @Deprecated public java.lang.String timezone;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public DataHubIngestionSourceSchedule() {}

  /**
   * All-args constructor.
   */
  public DataHubIngestionSourceSchedule(java.lang.String interval, java.lang.String timezone) {
    this.interval = interval;
    this.timezone = timezone;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return interval;
    case 1: return timezone;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: interval = (java.lang.String)value$; break;
    case 1: timezone = (java.lang.String)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'interval' field.
   * A cron-formatted execution interval, as a cron string, e.g. * * * * *   */
  public java.lang.String getInterval() {
    return interval;
  }

  /**
   * Sets the value of the 'interval' field.
   * A cron-formatted execution interval, as a cron string, e.g. * * * * *   * @param value the value to set.
   */
  public void setInterval(java.lang.String value) {
    this.interval = value;
  }

  /**
   * Gets the value of the 'timezone' field.
   * Timezone in which the cron interval applies, e.g. America/Los Angeles   */
  public java.lang.String getTimezone() {
    return timezone;
  }

  /**
   * Sets the value of the 'timezone' field.
   * Timezone in which the cron interval applies, e.g. America/Los Angeles   * @param value the value to set.
   */
  public void setTimezone(java.lang.String value) {
    this.timezone = value;
  }

  /** Creates a new DataHubIngestionSourceSchedule RecordBuilder */
  public static com.linkedin.pegasus2avro.ingestion.DataHubIngestionSourceSchedule.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.ingestion.DataHubIngestionSourceSchedule.Builder();
  }
  
  /** Creates a new DataHubIngestionSourceSchedule RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.ingestion.DataHubIngestionSourceSchedule.Builder newBuilder(com.linkedin.pegasus2avro.ingestion.DataHubIngestionSourceSchedule.Builder other) {
    return new com.linkedin.pegasus2avro.ingestion.DataHubIngestionSourceSchedule.Builder(other);
  }
  
  /** Creates a new DataHubIngestionSourceSchedule RecordBuilder by copying an existing DataHubIngestionSourceSchedule instance */
  public static com.linkedin.pegasus2avro.ingestion.DataHubIngestionSourceSchedule.Builder newBuilder(com.linkedin.pegasus2avro.ingestion.DataHubIngestionSourceSchedule other) {
    return new com.linkedin.pegasus2avro.ingestion.DataHubIngestionSourceSchedule.Builder(other);
  }
  
  /**
   * RecordBuilder for DataHubIngestionSourceSchedule instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<DataHubIngestionSourceSchedule>
    implements org.apache.avro.data.RecordBuilder<DataHubIngestionSourceSchedule> {

    private java.lang.String interval;
    private java.lang.String timezone;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.ingestion.DataHubIngestionSourceSchedule.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.ingestion.DataHubIngestionSourceSchedule.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.interval)) {
        this.interval = data().deepCopy(fields()[0].schema(), other.interval);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.timezone)) {
        this.timezone = data().deepCopy(fields()[1].schema(), other.timezone);
        fieldSetFlags()[1] = true;
      }
    }
    
    /** Creates a Builder by copying an existing DataHubIngestionSourceSchedule instance */
    private Builder(com.linkedin.pegasus2avro.ingestion.DataHubIngestionSourceSchedule other) {
            super(com.linkedin.pegasus2avro.ingestion.DataHubIngestionSourceSchedule.SCHEMA$);
      if (isValidValue(fields()[0], other.interval)) {
        this.interval = data().deepCopy(fields()[0].schema(), other.interval);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.timezone)) {
        this.timezone = data().deepCopy(fields()[1].schema(), other.timezone);
        fieldSetFlags()[1] = true;
      }
    }

    /** Gets the value of the 'interval' field */
    public java.lang.String getInterval() {
      return interval;
    }
    
    /** Sets the value of the 'interval' field */
    public com.linkedin.pegasus2avro.ingestion.DataHubIngestionSourceSchedule.Builder setInterval(java.lang.String value) {
      validate(fields()[0], value);
      this.interval = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'interval' field has been set */
    public boolean hasInterval() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'interval' field */
    public com.linkedin.pegasus2avro.ingestion.DataHubIngestionSourceSchedule.Builder clearInterval() {
      interval = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'timezone' field */
    public java.lang.String getTimezone() {
      return timezone;
    }
    
    /** Sets the value of the 'timezone' field */
    public com.linkedin.pegasus2avro.ingestion.DataHubIngestionSourceSchedule.Builder setTimezone(java.lang.String value) {
      validate(fields()[1], value);
      this.timezone = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'timezone' field has been set */
    public boolean hasTimezone() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'timezone' field */
    public com.linkedin.pegasus2avro.ingestion.DataHubIngestionSourceSchedule.Builder clearTimezone() {
      timezone = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    public DataHubIngestionSourceSchedule build() {
      try {
        DataHubIngestionSourceSchedule record = new DataHubIngestionSourceSchedule();
        record.interval = fieldSetFlags()[0] ? this.interval : (java.lang.String) defaultValue(fields()[0]);
        record.timezone = fieldSetFlags()[1] ? this.timezone : (java.lang.String) defaultValue(fields()[1]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
