/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.test;  
@SuppressWarnings("all")
/** Information about a DataHub Test */
@org.apache.avro.specific.AvroGenerated
public class TestInfo extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"TestInfo\",\"namespace\":\"com.linkedin.pegasus2avro.test\",\"doc\":\"Information about a DataHub Test\",\"fields\":[{\"name\":\"name\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"The name of the test\",\"Searchable\":{\"fieldType\":\"TEXT_PARTIAL\"}},{\"name\":\"category\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"Category of the test\",\"Searchable\":{\"fieldType\":\"KEYWORD\"}},{\"name\":\"description\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"doc\":\"Description of the test\",\"default\":null,\"Searchable\":{\"fieldType\":\"TEXT\"}},{\"name\":\"definition\",\"type\":{\"type\":\"record\",\"name\":\"TestDefinition\",\"fields\":[{\"name\":\"type\",\"type\":{\"type\":\"enum\",\"name\":\"TestDefinitionType\",\"symbols\":[\"JSON\"],\"symbolDocs\":{\"JSON\":\"JSON / YAML test def\"}},\"doc\":\"The Test Definition Type\"},{\"name\":\"json\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"doc\":\"JSON format configuration for the test\",\"default\":null}]},\"doc\":\"Configuration for the Test\"}],\"Aspect\":{\"name\":\"testInfo\"}}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** The name of the test */
  @Deprecated public java.lang.String name;
  /** Category of the test */
  @Deprecated public java.lang.String category;
  /** Description of the test */
  @Deprecated public java.lang.String description;
  /** Configuration for the Test */
  @Deprecated public com.linkedin.pegasus2avro.test.TestDefinition definition;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public TestInfo() {}

  /**
   * All-args constructor.
   */
  public TestInfo(java.lang.String name, java.lang.String category, java.lang.String description, com.linkedin.pegasus2avro.test.TestDefinition definition) {
    this.name = name;
    this.category = category;
    this.description = description;
    this.definition = definition;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return name;
    case 1: return category;
    case 2: return description;
    case 3: return definition;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: name = (java.lang.String)value$; break;
    case 1: category = (java.lang.String)value$; break;
    case 2: description = (java.lang.String)value$; break;
    case 3: definition = (com.linkedin.pegasus2avro.test.TestDefinition)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'name' field.
   * The name of the test   */
  public java.lang.String getName() {
    return name;
  }

  /**
   * Sets the value of the 'name' field.
   * The name of the test   * @param value the value to set.
   */
  public void setName(java.lang.String value) {
    this.name = value;
  }

  /**
   * Gets the value of the 'category' field.
   * Category of the test   */
  public java.lang.String getCategory() {
    return category;
  }

  /**
   * Sets the value of the 'category' field.
   * Category of the test   * @param value the value to set.
   */
  public void setCategory(java.lang.String value) {
    this.category = value;
  }

  /**
   * Gets the value of the 'description' field.
   * Description of the test   */
  public java.lang.String getDescription() {
    return description;
  }

  /**
   * Sets the value of the 'description' field.
   * Description of the test   * @param value the value to set.
   */
  public void setDescription(java.lang.String value) {
    this.description = value;
  }

  /**
   * Gets the value of the 'definition' field.
   * Configuration for the Test   */
  public com.linkedin.pegasus2avro.test.TestDefinition getDefinition() {
    return definition;
  }

  /**
   * Sets the value of the 'definition' field.
   * Configuration for the Test   * @param value the value to set.
   */
  public void setDefinition(com.linkedin.pegasus2avro.test.TestDefinition value) {
    this.definition = value;
  }

  /** Creates a new TestInfo RecordBuilder */
  public static com.linkedin.pegasus2avro.test.TestInfo.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.test.TestInfo.Builder();
  }
  
  /** Creates a new TestInfo RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.test.TestInfo.Builder newBuilder(com.linkedin.pegasus2avro.test.TestInfo.Builder other) {
    return new com.linkedin.pegasus2avro.test.TestInfo.Builder(other);
  }
  
  /** Creates a new TestInfo RecordBuilder by copying an existing TestInfo instance */
  public static com.linkedin.pegasus2avro.test.TestInfo.Builder newBuilder(com.linkedin.pegasus2avro.test.TestInfo other) {
    return new com.linkedin.pegasus2avro.test.TestInfo.Builder(other);
  }
  
  /**
   * RecordBuilder for TestInfo instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<TestInfo>
    implements org.apache.avro.data.RecordBuilder<TestInfo> {

    private java.lang.String name;
    private java.lang.String category;
    private java.lang.String description;
    private com.linkedin.pegasus2avro.test.TestDefinition definition;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.test.TestInfo.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.test.TestInfo.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.name)) {
        this.name = data().deepCopy(fields()[0].schema(), other.name);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.category)) {
        this.category = data().deepCopy(fields()[1].schema(), other.category);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.description)) {
        this.description = data().deepCopy(fields()[2].schema(), other.description);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.definition)) {
        this.definition = data().deepCopy(fields()[3].schema(), other.definition);
        fieldSetFlags()[3] = true;
      }
    }
    
    /** Creates a Builder by copying an existing TestInfo instance */
    private Builder(com.linkedin.pegasus2avro.test.TestInfo other) {
            super(com.linkedin.pegasus2avro.test.TestInfo.SCHEMA$);
      if (isValidValue(fields()[0], other.name)) {
        this.name = data().deepCopy(fields()[0].schema(), other.name);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.category)) {
        this.category = data().deepCopy(fields()[1].schema(), other.category);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.description)) {
        this.description = data().deepCopy(fields()[2].schema(), other.description);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.definition)) {
        this.definition = data().deepCopy(fields()[3].schema(), other.definition);
        fieldSetFlags()[3] = true;
      }
    }

    /** Gets the value of the 'name' field */
    public java.lang.String getName() {
      return name;
    }
    
    /** Sets the value of the 'name' field */
    public com.linkedin.pegasus2avro.test.TestInfo.Builder setName(java.lang.String value) {
      validate(fields()[0], value);
      this.name = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'name' field has been set */
    public boolean hasName() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'name' field */
    public com.linkedin.pegasus2avro.test.TestInfo.Builder clearName() {
      name = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'category' field */
    public java.lang.String getCategory() {
      return category;
    }
    
    /** Sets the value of the 'category' field */
    public com.linkedin.pegasus2avro.test.TestInfo.Builder setCategory(java.lang.String value) {
      validate(fields()[1], value);
      this.category = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'category' field has been set */
    public boolean hasCategory() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'category' field */
    public com.linkedin.pegasus2avro.test.TestInfo.Builder clearCategory() {
      category = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'description' field */
    public java.lang.String getDescription() {
      return description;
    }
    
    /** Sets the value of the 'description' field */
    public com.linkedin.pegasus2avro.test.TestInfo.Builder setDescription(java.lang.String value) {
      validate(fields()[2], value);
      this.description = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'description' field has been set */
    public boolean hasDescription() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'description' field */
    public com.linkedin.pegasus2avro.test.TestInfo.Builder clearDescription() {
      description = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'definition' field */
    public com.linkedin.pegasus2avro.test.TestDefinition getDefinition() {
      return definition;
    }
    
    /** Sets the value of the 'definition' field */
    public com.linkedin.pegasus2avro.test.TestInfo.Builder setDefinition(com.linkedin.pegasus2avro.test.TestDefinition value) {
      validate(fields()[3], value);
      this.definition = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'definition' field has been set */
    public boolean hasDefinition() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'definition' field */
    public com.linkedin.pegasus2avro.test.TestInfo.Builder clearDefinition() {
      definition = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    public TestInfo build() {
      try {
        TestInfo record = new TestInfo();
        record.name = fieldSetFlags()[0] ? this.name : (java.lang.String) defaultValue(fields()[0]);
        record.category = fieldSetFlags()[1] ? this.category : (java.lang.String) defaultValue(fields()[1]);
        record.description = fieldSetFlags()[2] ? this.description : (java.lang.String) defaultValue(fields()[2]);
        record.definition = fieldSetFlags()[3] ? this.definition : (com.linkedin.pegasus2avro.test.TestDefinition) defaultValue(fields()[3]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
