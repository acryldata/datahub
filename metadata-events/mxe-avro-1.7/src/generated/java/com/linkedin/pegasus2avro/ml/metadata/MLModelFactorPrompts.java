/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.ml.metadata;  
@SuppressWarnings("all")
/** Prompts which affect the performance of the MLModel */
@org.apache.avro.specific.AvroGenerated
public class MLModelFactorPrompts extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"MLModelFactorPrompts\",\"namespace\":\"com.linkedin.pegasus2avro.ml.metadata\",\"doc\":\"Prompts which affect the performance of the MLModel\",\"fields\":[{\"name\":\"relevantFactors\",\"type\":[\"null\",{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"MLModelFactors\",\"doc\":\"Factors affecting the performance of the MLModel.\",\"fields\":[{\"name\":\"groups\",\"type\":[\"null\",{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"doc\":\"Groups refers to distinct categories with similar characteristics that are present in the evaluation data instances.\\nFor human-centric machine learning MLModels, groups are people who share one or multiple characteristics.\",\"default\":null},{\"name\":\"instrumentation\",\"type\":[\"null\",{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"doc\":\"The performance of a MLModel can vary depending on what instruments were used to capture the input to the MLModel.\\nFor example, a face detection model may perform differently depending on the camera's hardware and software,\\nincluding lens, image stabilization, high dynamic range techniques, and background blurring for portrait mode.\",\"default\":null},{\"name\":\"environment\",\"type\":[\"null\",{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"doc\":\"A further factor affecting MLModel performance is the environment in which it is deployed.\",\"default\":null}]}}],\"doc\":\"What are foreseeable salient factors for which MLModel performance may vary, and how were these determined?\",\"default\":null},{\"name\":\"evaluationFactors\",\"type\":[\"null\",{\"type\":\"array\",\"items\":\"MLModelFactors\"}],\"doc\":\"Which factors are being reported, and why were these chosen?\",\"default\":null}],\"Aspect\":{\"name\":\"mlModelFactorPrompts\"}}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** What are foreseeable salient factors for which MLModel performance may vary, and how were these determined? */
  @Deprecated public java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors> relevantFactors;
  /** Which factors are being reported, and why were these chosen? */
  @Deprecated public java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors> evaluationFactors;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public MLModelFactorPrompts() {}

  /**
   * All-args constructor.
   */
  public MLModelFactorPrompts(java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors> relevantFactors, java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors> evaluationFactors) {
    this.relevantFactors = relevantFactors;
    this.evaluationFactors = evaluationFactors;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return relevantFactors;
    case 1: return evaluationFactors;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: relevantFactors = (java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors>)value$; break;
    case 1: evaluationFactors = (java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'relevantFactors' field.
   * What are foreseeable salient factors for which MLModel performance may vary, and how were these determined?   */
  public java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors> getRelevantFactors() {
    return relevantFactors;
  }

  /**
   * Sets the value of the 'relevantFactors' field.
   * What are foreseeable salient factors for which MLModel performance may vary, and how were these determined?   * @param value the value to set.
   */
  public void setRelevantFactors(java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors> value) {
    this.relevantFactors = value;
  }

  /**
   * Gets the value of the 'evaluationFactors' field.
   * Which factors are being reported, and why were these chosen?   */
  public java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors> getEvaluationFactors() {
    return evaluationFactors;
  }

  /**
   * Sets the value of the 'evaluationFactors' field.
   * Which factors are being reported, and why were these chosen?   * @param value the value to set.
   */
  public void setEvaluationFactors(java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors> value) {
    this.evaluationFactors = value;
  }

  /** Creates a new MLModelFactorPrompts RecordBuilder */
  public static com.linkedin.pegasus2avro.ml.metadata.MLModelFactorPrompts.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.ml.metadata.MLModelFactorPrompts.Builder();
  }
  
  /** Creates a new MLModelFactorPrompts RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.ml.metadata.MLModelFactorPrompts.Builder newBuilder(com.linkedin.pegasus2avro.ml.metadata.MLModelFactorPrompts.Builder other) {
    return new com.linkedin.pegasus2avro.ml.metadata.MLModelFactorPrompts.Builder(other);
  }
  
  /** Creates a new MLModelFactorPrompts RecordBuilder by copying an existing MLModelFactorPrompts instance */
  public static com.linkedin.pegasus2avro.ml.metadata.MLModelFactorPrompts.Builder newBuilder(com.linkedin.pegasus2avro.ml.metadata.MLModelFactorPrompts other) {
    return new com.linkedin.pegasus2avro.ml.metadata.MLModelFactorPrompts.Builder(other);
  }
  
  /**
   * RecordBuilder for MLModelFactorPrompts instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<MLModelFactorPrompts>
    implements org.apache.avro.data.RecordBuilder<MLModelFactorPrompts> {

    private java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors> relevantFactors;
    private java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors> evaluationFactors;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.ml.metadata.MLModelFactorPrompts.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.ml.metadata.MLModelFactorPrompts.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.relevantFactors)) {
        this.relevantFactors = data().deepCopy(fields()[0].schema(), other.relevantFactors);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.evaluationFactors)) {
        this.evaluationFactors = data().deepCopy(fields()[1].schema(), other.evaluationFactors);
        fieldSetFlags()[1] = true;
      }
    }
    
    /** Creates a Builder by copying an existing MLModelFactorPrompts instance */
    private Builder(com.linkedin.pegasus2avro.ml.metadata.MLModelFactorPrompts other) {
            super(com.linkedin.pegasus2avro.ml.metadata.MLModelFactorPrompts.SCHEMA$);
      if (isValidValue(fields()[0], other.relevantFactors)) {
        this.relevantFactors = data().deepCopy(fields()[0].schema(), other.relevantFactors);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.evaluationFactors)) {
        this.evaluationFactors = data().deepCopy(fields()[1].schema(), other.evaluationFactors);
        fieldSetFlags()[1] = true;
      }
    }

    /** Gets the value of the 'relevantFactors' field */
    public java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors> getRelevantFactors() {
      return relevantFactors;
    }
    
    /** Sets the value of the 'relevantFactors' field */
    public com.linkedin.pegasus2avro.ml.metadata.MLModelFactorPrompts.Builder setRelevantFactors(java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors> value) {
      validate(fields()[0], value);
      this.relevantFactors = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'relevantFactors' field has been set */
    public boolean hasRelevantFactors() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'relevantFactors' field */
    public com.linkedin.pegasus2avro.ml.metadata.MLModelFactorPrompts.Builder clearRelevantFactors() {
      relevantFactors = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'evaluationFactors' field */
    public java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors> getEvaluationFactors() {
      return evaluationFactors;
    }
    
    /** Sets the value of the 'evaluationFactors' field */
    public com.linkedin.pegasus2avro.ml.metadata.MLModelFactorPrompts.Builder setEvaluationFactors(java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors> value) {
      validate(fields()[1], value);
      this.evaluationFactors = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'evaluationFactors' field has been set */
    public boolean hasEvaluationFactors() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'evaluationFactors' field */
    public com.linkedin.pegasus2avro.ml.metadata.MLModelFactorPrompts.Builder clearEvaluationFactors() {
      evaluationFactors = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    public MLModelFactorPrompts build() {
      try {
        MLModelFactorPrompts record = new MLModelFactorPrompts();
        record.relevantFactors = fieldSetFlags()[0] ? this.relevantFactors : (java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors>) defaultValue(fields()[0]);
        record.evaluationFactors = fieldSetFlags()[1] ? this.evaluationFactors : (java.util.List<com.linkedin.pegasus2avro.ml.metadata.MLModelFactors>) defaultValue(fields()[1]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
