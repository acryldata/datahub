/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.dataset;  
@SuppressWarnings("all")
/** Downstream lineage information about a dataset including the source reporting the lineage */
@org.apache.avro.specific.AvroGenerated
public class Downstream extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Downstream\",\"namespace\":\"com.linkedin.pegasus2avro.dataset\",\"doc\":\"Downstream lineage information about a dataset including the source reporting the lineage\",\"fields\":[{\"name\":\"auditStamp\",\"type\":{\"type\":\"record\",\"name\":\"AuditStamp\",\"namespace\":\"com.linkedin.pegasus2avro.common\",\"doc\":\"Data captured on a resource/association/sub-resource level giving insight into when that resource/association/sub-resource moved into a particular lifecycle stage, and who acted to move it into that specific lifecycle stage.\",\"fields\":[{\"name\":\"time\",\"type\":\"long\",\"doc\":\"When did the resource/association/sub-resource move into the specific lifecycle stage represented by this AuditEvent.\"},{\"name\":\"actor\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"The entity (e.g. a member URN) which will be credited for moving the resource/association/sub-resource into the specific lifecycle stage. It is also the one used to authorize the change.\",\"java\":{\"class\":\"com.linkedin.pegasus2avro.common.urn.Urn\"}},{\"name\":\"impersonator\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"doc\":\"The entity (e.g. a service URN) which performs the change on behalf of the Actor and must be authorized to act as the Actor.\",\"default\":null,\"java\":{\"class\":\"com.linkedin.pegasus2avro.common.urn.Urn\"}},{\"name\":\"message\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"doc\":\"Additional context around how DataHub was informed of the particular change. For example: was the change created by an automated process, or manually.\",\"default\":null}]},\"doc\":\"Audit stamp containing who reported the lineage and when\"},{\"name\":\"dataset\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"The downstream dataset the lineage points to\",\"java\":{\"class\":\"com.linkedin.pegasus2avro.common.urn.DatasetUrn\"}},{\"name\":\"type\",\"type\":{\"type\":\"enum\",\"name\":\"DatasetLineageType\",\"doc\":\"The various types of supported dataset lineage\",\"symbols\":[\"COPY\",\"TRANSFORMED\",\"VIEW\"],\"symbolDocs\":{\"COPY\":\"Direct copy without modification\",\"TRANSFORMED\":\"Transformed data with modification (format or content change)\",\"VIEW\":\"Represents a view defined on the sources e.g. Hive view defined on underlying hive tables or a Hive table pointing to a HDFS dataset or DALI view defined on multiple sources\"}},\"doc\":\"The type of the lineage\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** Audit stamp containing who reported the lineage and when */
  @Deprecated public com.linkedin.pegasus2avro.common.AuditStamp auditStamp;
  /** The downstream dataset the lineage points to */
  @Deprecated public java.lang.String dataset;
  /** The type of the lineage */
  @Deprecated public com.linkedin.pegasus2avro.dataset.DatasetLineageType type;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public Downstream() {}

  /**
   * All-args constructor.
   */
  public Downstream(com.linkedin.pegasus2avro.common.AuditStamp auditStamp, java.lang.String dataset, com.linkedin.pegasus2avro.dataset.DatasetLineageType type) {
    this.auditStamp = auditStamp;
    this.dataset = dataset;
    this.type = type;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return auditStamp;
    case 1: return dataset;
    case 2: return type;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: auditStamp = (com.linkedin.pegasus2avro.common.AuditStamp)value$; break;
    case 1: dataset = (java.lang.String)value$; break;
    case 2: type = (com.linkedin.pegasus2avro.dataset.DatasetLineageType)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'auditStamp' field.
   * Audit stamp containing who reported the lineage and when   */
  public com.linkedin.pegasus2avro.common.AuditStamp getAuditStamp() {
    return auditStamp;
  }

  /**
   * Sets the value of the 'auditStamp' field.
   * Audit stamp containing who reported the lineage and when   * @param value the value to set.
   */
  public void setAuditStamp(com.linkedin.pegasus2avro.common.AuditStamp value) {
    this.auditStamp = value;
  }

  /**
   * Gets the value of the 'dataset' field.
   * The downstream dataset the lineage points to   */
  public java.lang.String getDataset() {
    return dataset;
  }

  /**
   * Sets the value of the 'dataset' field.
   * The downstream dataset the lineage points to   * @param value the value to set.
   */
  public void setDataset(java.lang.String value) {
    this.dataset = value;
  }

  /**
   * Gets the value of the 'type' field.
   * The type of the lineage   */
  public com.linkedin.pegasus2avro.dataset.DatasetLineageType getType() {
    return type;
  }

  /**
   * Sets the value of the 'type' field.
   * The type of the lineage   * @param value the value to set.
   */
  public void setType(com.linkedin.pegasus2avro.dataset.DatasetLineageType value) {
    this.type = value;
  }

  /** Creates a new Downstream RecordBuilder */
  public static com.linkedin.pegasus2avro.dataset.Downstream.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.dataset.Downstream.Builder();
  }
  
  /** Creates a new Downstream RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.dataset.Downstream.Builder newBuilder(com.linkedin.pegasus2avro.dataset.Downstream.Builder other) {
    return new com.linkedin.pegasus2avro.dataset.Downstream.Builder(other);
  }
  
  /** Creates a new Downstream RecordBuilder by copying an existing Downstream instance */
  public static com.linkedin.pegasus2avro.dataset.Downstream.Builder newBuilder(com.linkedin.pegasus2avro.dataset.Downstream other) {
    return new com.linkedin.pegasus2avro.dataset.Downstream.Builder(other);
  }
  
  /**
   * RecordBuilder for Downstream instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Downstream>
    implements org.apache.avro.data.RecordBuilder<Downstream> {

    private com.linkedin.pegasus2avro.common.AuditStamp auditStamp;
    private java.lang.String dataset;
    private com.linkedin.pegasus2avro.dataset.DatasetLineageType type;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.dataset.Downstream.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.dataset.Downstream.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.auditStamp)) {
        this.auditStamp = data().deepCopy(fields()[0].schema(), other.auditStamp);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.dataset)) {
        this.dataset = data().deepCopy(fields()[1].schema(), other.dataset);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.type)) {
        this.type = data().deepCopy(fields()[2].schema(), other.type);
        fieldSetFlags()[2] = true;
      }
    }
    
    /** Creates a Builder by copying an existing Downstream instance */
    private Builder(com.linkedin.pegasus2avro.dataset.Downstream other) {
            super(com.linkedin.pegasus2avro.dataset.Downstream.SCHEMA$);
      if (isValidValue(fields()[0], other.auditStamp)) {
        this.auditStamp = data().deepCopy(fields()[0].schema(), other.auditStamp);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.dataset)) {
        this.dataset = data().deepCopy(fields()[1].schema(), other.dataset);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.type)) {
        this.type = data().deepCopy(fields()[2].schema(), other.type);
        fieldSetFlags()[2] = true;
      }
    }

    /** Gets the value of the 'auditStamp' field */
    public com.linkedin.pegasus2avro.common.AuditStamp getAuditStamp() {
      return auditStamp;
    }
    
    /** Sets the value of the 'auditStamp' field */
    public com.linkedin.pegasus2avro.dataset.Downstream.Builder setAuditStamp(com.linkedin.pegasus2avro.common.AuditStamp value) {
      validate(fields()[0], value);
      this.auditStamp = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'auditStamp' field has been set */
    public boolean hasAuditStamp() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'auditStamp' field */
    public com.linkedin.pegasus2avro.dataset.Downstream.Builder clearAuditStamp() {
      auditStamp = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'dataset' field */
    public java.lang.String getDataset() {
      return dataset;
    }
    
    /** Sets the value of the 'dataset' field */
    public com.linkedin.pegasus2avro.dataset.Downstream.Builder setDataset(java.lang.String value) {
      validate(fields()[1], value);
      this.dataset = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'dataset' field has been set */
    public boolean hasDataset() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'dataset' field */
    public com.linkedin.pegasus2avro.dataset.Downstream.Builder clearDataset() {
      dataset = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'type' field */
    public com.linkedin.pegasus2avro.dataset.DatasetLineageType getType() {
      return type;
    }
    
    /** Sets the value of the 'type' field */
    public com.linkedin.pegasus2avro.dataset.Downstream.Builder setType(com.linkedin.pegasus2avro.dataset.DatasetLineageType value) {
      validate(fields()[2], value);
      this.type = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'type' field has been set */
    public boolean hasType() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'type' field */
    public com.linkedin.pegasus2avro.dataset.Downstream.Builder clearType() {
      type = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    public Downstream build() {
      try {
        Downstream record = new Downstream();
        record.auditStamp = fieldSetFlags()[0] ? this.auditStamp : (com.linkedin.pegasus2avro.common.AuditStamp) defaultValue(fields()[0]);
        record.dataset = fieldSetFlags()[1] ? this.dataset : (java.lang.String) defaultValue(fields()[1]);
        record.type = fieldSetFlags()[2] ? this.type : (com.linkedin.pegasus2avro.dataset.DatasetLineageType) defaultValue(fields()[2]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
