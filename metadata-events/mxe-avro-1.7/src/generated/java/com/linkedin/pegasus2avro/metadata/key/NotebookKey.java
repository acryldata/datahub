/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.metadata.key;  
@SuppressWarnings("all")
/** Key for a Notebook */
@org.apache.avro.specific.AvroGenerated
public class NotebookKey extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"NotebookKey\",\"namespace\":\"com.linkedin.pegasus2avro.metadata.key\",\"doc\":\"Key for a Notebook\",\"fields\":[{\"name\":\"notebookTool\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"The name of the Notebook tool such as QueryBook, etc.\"},{\"name\":\"notebookId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"Unique id for the Notebook. This id should be globally unique for a Notebook tool even when there are multiple deployments of it. As an example, Notebook URL could be used here for QueryBook such as 'querybook.com/notebook/773'\"}],\"Aspect\":{\"name\":\"notebookKey\"}}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** The name of the Notebook tool such as QueryBook, etc. */
  @Deprecated public java.lang.String notebookTool;
  /** Unique id for the Notebook. This id should be globally unique for a Notebook tool even when there are multiple deployments of it. As an example, Notebook URL could be used here for QueryBook such as 'querybook.com/notebook/773' */
  @Deprecated public java.lang.String notebookId;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public NotebookKey() {}

  /**
   * All-args constructor.
   */
  public NotebookKey(java.lang.String notebookTool, java.lang.String notebookId) {
    this.notebookTool = notebookTool;
    this.notebookId = notebookId;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return notebookTool;
    case 1: return notebookId;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: notebookTool = (java.lang.String)value$; break;
    case 1: notebookId = (java.lang.String)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'notebookTool' field.
   * The name of the Notebook tool such as QueryBook, etc.   */
  public java.lang.String getNotebookTool() {
    return notebookTool;
  }

  /**
   * Sets the value of the 'notebookTool' field.
   * The name of the Notebook tool such as QueryBook, etc.   * @param value the value to set.
   */
  public void setNotebookTool(java.lang.String value) {
    this.notebookTool = value;
  }

  /**
   * Gets the value of the 'notebookId' field.
   * Unique id for the Notebook. This id should be globally unique for a Notebook tool even when there are multiple deployments of it. As an example, Notebook URL could be used here for QueryBook such as 'querybook.com/notebook/773'   */
  public java.lang.String getNotebookId() {
    return notebookId;
  }

  /**
   * Sets the value of the 'notebookId' field.
   * Unique id for the Notebook. This id should be globally unique for a Notebook tool even when there are multiple deployments of it. As an example, Notebook URL could be used here for QueryBook such as 'querybook.com/notebook/773'   * @param value the value to set.
   */
  public void setNotebookId(java.lang.String value) {
    this.notebookId = value;
  }

  /** Creates a new NotebookKey RecordBuilder */
  public static com.linkedin.pegasus2avro.metadata.key.NotebookKey.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.metadata.key.NotebookKey.Builder();
  }
  
  /** Creates a new NotebookKey RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.metadata.key.NotebookKey.Builder newBuilder(com.linkedin.pegasus2avro.metadata.key.NotebookKey.Builder other) {
    return new com.linkedin.pegasus2avro.metadata.key.NotebookKey.Builder(other);
  }
  
  /** Creates a new NotebookKey RecordBuilder by copying an existing NotebookKey instance */
  public static com.linkedin.pegasus2avro.metadata.key.NotebookKey.Builder newBuilder(com.linkedin.pegasus2avro.metadata.key.NotebookKey other) {
    return new com.linkedin.pegasus2avro.metadata.key.NotebookKey.Builder(other);
  }
  
  /**
   * RecordBuilder for NotebookKey instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<NotebookKey>
    implements org.apache.avro.data.RecordBuilder<NotebookKey> {

    private java.lang.String notebookTool;
    private java.lang.String notebookId;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.metadata.key.NotebookKey.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.metadata.key.NotebookKey.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.notebookTool)) {
        this.notebookTool = data().deepCopy(fields()[0].schema(), other.notebookTool);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.notebookId)) {
        this.notebookId = data().deepCopy(fields()[1].schema(), other.notebookId);
        fieldSetFlags()[1] = true;
      }
    }
    
    /** Creates a Builder by copying an existing NotebookKey instance */
    private Builder(com.linkedin.pegasus2avro.metadata.key.NotebookKey other) {
            super(com.linkedin.pegasus2avro.metadata.key.NotebookKey.SCHEMA$);
      if (isValidValue(fields()[0], other.notebookTool)) {
        this.notebookTool = data().deepCopy(fields()[0].schema(), other.notebookTool);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.notebookId)) {
        this.notebookId = data().deepCopy(fields()[1].schema(), other.notebookId);
        fieldSetFlags()[1] = true;
      }
    }

    /** Gets the value of the 'notebookTool' field */
    public java.lang.String getNotebookTool() {
      return notebookTool;
    }
    
    /** Sets the value of the 'notebookTool' field */
    public com.linkedin.pegasus2avro.metadata.key.NotebookKey.Builder setNotebookTool(java.lang.String value) {
      validate(fields()[0], value);
      this.notebookTool = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'notebookTool' field has been set */
    public boolean hasNotebookTool() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'notebookTool' field */
    public com.linkedin.pegasus2avro.metadata.key.NotebookKey.Builder clearNotebookTool() {
      notebookTool = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'notebookId' field */
    public java.lang.String getNotebookId() {
      return notebookId;
    }
    
    /** Sets the value of the 'notebookId' field */
    public com.linkedin.pegasus2avro.metadata.key.NotebookKey.Builder setNotebookId(java.lang.String value) {
      validate(fields()[1], value);
      this.notebookId = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'notebookId' field has been set */
    public boolean hasNotebookId() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'notebookId' field */
    public com.linkedin.pegasus2avro.metadata.key.NotebookKey.Builder clearNotebookId() {
      notebookId = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    public NotebookKey build() {
      try {
        NotebookKey record = new NotebookKey();
        record.notebookTool = fieldSetFlags()[0] ? this.notebookTool : (java.lang.String) defaultValue(fields()[0]);
        record.notebookId = fieldSetFlags()[1] ? this.notebookId : (java.lang.String) defaultValue(fields()[1]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
