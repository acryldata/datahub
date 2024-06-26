/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.ml.metadata;  
@SuppressWarnings("all")
/** BaseData record */
@org.apache.avro.specific.AvroGenerated
public class BaseData extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"BaseData\",\"namespace\":\"com.linkedin.pegasus2avro.ml.metadata\",\"doc\":\"BaseData record\",\"fields\":[{\"name\":\"dataset\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"What dataset were used in the MLModel?\",\"java\":{\"class\":\"com.linkedin.pegasus2avro.common.urn.DatasetUrn\"}},{\"name\":\"motivation\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"doc\":\"Why was this dataset chosen?\",\"default\":null},{\"name\":\"preProcessing\",\"type\":[\"null\",{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"doc\":\"How was the data preprocessed (e.g., tokenization of sentences, cropping of images, any filtering such as dropping images without faces)?\",\"default\":null}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** What dataset were used in the MLModel? */
  @Deprecated public java.lang.String dataset;
  /** Why was this dataset chosen? */
  @Deprecated public java.lang.String motivation;
  /** How was the data preprocessed (e.g., tokenization of sentences, cropping of images, any filtering such as dropping images without faces)? */
  @Deprecated public java.util.List<java.lang.String> preProcessing;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public BaseData() {}

  /**
   * All-args constructor.
   */
  public BaseData(java.lang.String dataset, java.lang.String motivation, java.util.List<java.lang.String> preProcessing) {
    this.dataset = dataset;
    this.motivation = motivation;
    this.preProcessing = preProcessing;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return dataset;
    case 1: return motivation;
    case 2: return preProcessing;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: dataset = (java.lang.String)value$; break;
    case 1: motivation = (java.lang.String)value$; break;
    case 2: preProcessing = (java.util.List<java.lang.String>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'dataset' field.
   * What dataset were used in the MLModel?   */
  public java.lang.String getDataset() {
    return dataset;
  }

  /**
   * Sets the value of the 'dataset' field.
   * What dataset were used in the MLModel?   * @param value the value to set.
   */
  public void setDataset(java.lang.String value) {
    this.dataset = value;
  }

  /**
   * Gets the value of the 'motivation' field.
   * Why was this dataset chosen?   */
  public java.lang.String getMotivation() {
    return motivation;
  }

  /**
   * Sets the value of the 'motivation' field.
   * Why was this dataset chosen?   * @param value the value to set.
   */
  public void setMotivation(java.lang.String value) {
    this.motivation = value;
  }

  /**
   * Gets the value of the 'preProcessing' field.
   * How was the data preprocessed (e.g., tokenization of sentences, cropping of images, any filtering such as dropping images without faces)?   */
  public java.util.List<java.lang.String> getPreProcessing() {
    return preProcessing;
  }

  /**
   * Sets the value of the 'preProcessing' field.
   * How was the data preprocessed (e.g., tokenization of sentences, cropping of images, any filtering such as dropping images without faces)?   * @param value the value to set.
   */
  public void setPreProcessing(java.util.List<java.lang.String> value) {
    this.preProcessing = value;
  }

  /** Creates a new BaseData RecordBuilder */
  public static com.linkedin.pegasus2avro.ml.metadata.BaseData.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.ml.metadata.BaseData.Builder();
  }
  
  /** Creates a new BaseData RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.ml.metadata.BaseData.Builder newBuilder(com.linkedin.pegasus2avro.ml.metadata.BaseData.Builder other) {
    return new com.linkedin.pegasus2avro.ml.metadata.BaseData.Builder(other);
  }
  
  /** Creates a new BaseData RecordBuilder by copying an existing BaseData instance */
  public static com.linkedin.pegasus2avro.ml.metadata.BaseData.Builder newBuilder(com.linkedin.pegasus2avro.ml.metadata.BaseData other) {
    return new com.linkedin.pegasus2avro.ml.metadata.BaseData.Builder(other);
  }
  
  /**
   * RecordBuilder for BaseData instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<BaseData>
    implements org.apache.avro.data.RecordBuilder<BaseData> {

    private java.lang.String dataset;
    private java.lang.String motivation;
    private java.util.List<java.lang.String> preProcessing;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.ml.metadata.BaseData.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.ml.metadata.BaseData.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.dataset)) {
        this.dataset = data().deepCopy(fields()[0].schema(), other.dataset);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.motivation)) {
        this.motivation = data().deepCopy(fields()[1].schema(), other.motivation);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.preProcessing)) {
        this.preProcessing = data().deepCopy(fields()[2].schema(), other.preProcessing);
        fieldSetFlags()[2] = true;
      }
    }
    
    /** Creates a Builder by copying an existing BaseData instance */
    private Builder(com.linkedin.pegasus2avro.ml.metadata.BaseData other) {
            super(com.linkedin.pegasus2avro.ml.metadata.BaseData.SCHEMA$);
      if (isValidValue(fields()[0], other.dataset)) {
        this.dataset = data().deepCopy(fields()[0].schema(), other.dataset);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.motivation)) {
        this.motivation = data().deepCopy(fields()[1].schema(), other.motivation);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.preProcessing)) {
        this.preProcessing = data().deepCopy(fields()[2].schema(), other.preProcessing);
        fieldSetFlags()[2] = true;
      }
    }

    /** Gets the value of the 'dataset' field */
    public java.lang.String getDataset() {
      return dataset;
    }
    
    /** Sets the value of the 'dataset' field */
    public com.linkedin.pegasus2avro.ml.metadata.BaseData.Builder setDataset(java.lang.String value) {
      validate(fields()[0], value);
      this.dataset = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'dataset' field has been set */
    public boolean hasDataset() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'dataset' field */
    public com.linkedin.pegasus2avro.ml.metadata.BaseData.Builder clearDataset() {
      dataset = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'motivation' field */
    public java.lang.String getMotivation() {
      return motivation;
    }
    
    /** Sets the value of the 'motivation' field */
    public com.linkedin.pegasus2avro.ml.metadata.BaseData.Builder setMotivation(java.lang.String value) {
      validate(fields()[1], value);
      this.motivation = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'motivation' field has been set */
    public boolean hasMotivation() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'motivation' field */
    public com.linkedin.pegasus2avro.ml.metadata.BaseData.Builder clearMotivation() {
      motivation = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'preProcessing' field */
    public java.util.List<java.lang.String> getPreProcessing() {
      return preProcessing;
    }
    
    /** Sets the value of the 'preProcessing' field */
    public com.linkedin.pegasus2avro.ml.metadata.BaseData.Builder setPreProcessing(java.util.List<java.lang.String> value) {
      validate(fields()[2], value);
      this.preProcessing = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'preProcessing' field has been set */
    public boolean hasPreProcessing() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'preProcessing' field */
    public com.linkedin.pegasus2avro.ml.metadata.BaseData.Builder clearPreProcessing() {
      preProcessing = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    public BaseData build() {
      try {
        BaseData record = new BaseData();
        record.dataset = fieldSetFlags()[0] ? this.dataset : (java.lang.String) defaultValue(fields()[0]);
        record.motivation = fieldSetFlags()[1] ? this.motivation : (java.lang.String) defaultValue(fields()[1]);
        record.preProcessing = fieldSetFlags()[2] ? this.preProcessing : (java.util.List<java.lang.String>) defaultValue(fields()[2]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
