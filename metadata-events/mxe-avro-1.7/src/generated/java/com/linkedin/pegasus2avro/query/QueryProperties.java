/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.query;  
@SuppressWarnings("all")
/** Information about a Query against one or more data assets (e.g. Tables or Views). */
@org.apache.avro.specific.AvroGenerated
public class QueryProperties extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"QueryProperties\",\"namespace\":\"com.linkedin.pegasus2avro.query\",\"doc\":\"Information about a Query against one or more data assets (e.g. Tables or Views).\",\"fields\":[{\"name\":\"statement\",\"type\":{\"type\":\"record\",\"name\":\"QueryStatement\",\"doc\":\"A query statement against one or more data assets.\",\"fields\":[{\"name\":\"value\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"The query text\"},{\"name\":\"language\",\"type\":{\"type\":\"enum\",\"name\":\"QueryLanguage\",\"symbols\":[\"SQL\"],\"symbolDocs\":{\"SQL\":\"A SQL Query\"}},\"doc\":\"The language of the Query, e.g. SQL.\",\"default\":\"SQL\"}]},\"doc\":\"The Query Statement.\"},{\"name\":\"source\",\"type\":{\"type\":\"enum\",\"name\":\"QuerySource\",\"symbols\":[\"MANUAL\"],\"symbolDocs\":{\"MANUAL\":\"The query was entered manually by a user (via the UI).\"}},\"doc\":\"The source of the Query\",\"Searchable\":{}},{\"name\":\"name\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"doc\":\"Optional display name to identify the query.\",\"default\":null,\"Searchable\":{\"boostScore\":10.0,\"enableAutocomplete\":true,\"fieldType\":\"TEXT_PARTIAL\"}},{\"name\":\"description\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"doc\":\"The Query description.\",\"default\":null},{\"name\":\"created\",\"type\":{\"type\":\"record\",\"name\":\"AuditStamp\",\"namespace\":\"com.linkedin.pegasus2avro.common\",\"doc\":\"Data captured on a resource/association/sub-resource level giving insight into when that resource/association/sub-resource moved into a particular lifecycle stage, and who acted to move it into that specific lifecycle stage.\",\"fields\":[{\"name\":\"time\",\"type\":\"long\",\"doc\":\"When did the resource/association/sub-resource move into the specific lifecycle stage represented by this AuditEvent.\"},{\"name\":\"actor\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"The entity (e.g. a member URN) which will be credited for moving the resource/association/sub-resource into the specific lifecycle stage. It is also the one used to authorize the change.\",\"java\":{\"class\":\"com.linkedin.pegasus2avro.common.urn.Urn\"}},{\"name\":\"impersonator\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"doc\":\"The entity (e.g. a service URN) which performs the change on behalf of the Actor and must be authorized to act as the Actor.\",\"default\":null,\"java\":{\"class\":\"com.linkedin.pegasus2avro.common.urn.Urn\"}},{\"name\":\"message\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"doc\":\"Additional context around how DataHub was informed of the particular change. For example: was the change created by an automated process, or manually.\",\"default\":null}]},\"doc\":\"Audit stamp capturing the time and actor who created the Query.\",\"Searchable\":{\"/actor\":{\"fieldName\":\"createdBy\",\"fieldType\":\"URN\"},\"/time\":{\"fieldName\":\"createdAt\",\"fieldType\":\"DATETIME\"}}},{\"name\":\"lastModified\",\"type\":\"com.linkedin.pegasus2avro.common.AuditStamp\",\"doc\":\"Audit stamp capturing the time and actor who last modified the Query.\",\"Searchable\":{\"/actor\":{\"fieldName\":\"lastModifiedBy\",\"fieldType\":\"URN\"},\"/time\":{\"fieldName\":\"lastModifiedAt\",\"fieldType\":\"DATETIME\"}}}],\"Aspect\":{\"name\":\"queryProperties\"}}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** The Query Statement. */
  @Deprecated public com.linkedin.pegasus2avro.query.QueryStatement statement;
  /** The source of the Query */
  @Deprecated public com.linkedin.pegasus2avro.query.QuerySource source;
  /** Optional display name to identify the query. */
  @Deprecated public java.lang.String name;
  /** The Query description. */
  @Deprecated public java.lang.String description;
  /** Audit stamp capturing the time and actor who created the Query. */
  @Deprecated public com.linkedin.pegasus2avro.common.AuditStamp created;
  /** Audit stamp capturing the time and actor who last modified the Query. */
  @Deprecated public com.linkedin.pegasus2avro.common.AuditStamp lastModified;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public QueryProperties() {}

  /**
   * All-args constructor.
   */
  public QueryProperties(com.linkedin.pegasus2avro.query.QueryStatement statement, com.linkedin.pegasus2avro.query.QuerySource source, java.lang.String name, java.lang.String description, com.linkedin.pegasus2avro.common.AuditStamp created, com.linkedin.pegasus2avro.common.AuditStamp lastModified) {
    this.statement = statement;
    this.source = source;
    this.name = name;
    this.description = description;
    this.created = created;
    this.lastModified = lastModified;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return statement;
    case 1: return source;
    case 2: return name;
    case 3: return description;
    case 4: return created;
    case 5: return lastModified;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: statement = (com.linkedin.pegasus2avro.query.QueryStatement)value$; break;
    case 1: source = (com.linkedin.pegasus2avro.query.QuerySource)value$; break;
    case 2: name = (java.lang.String)value$; break;
    case 3: description = (java.lang.String)value$; break;
    case 4: created = (com.linkedin.pegasus2avro.common.AuditStamp)value$; break;
    case 5: lastModified = (com.linkedin.pegasus2avro.common.AuditStamp)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'statement' field.
   * The Query Statement.   */
  public com.linkedin.pegasus2avro.query.QueryStatement getStatement() {
    return statement;
  }

  /**
   * Sets the value of the 'statement' field.
   * The Query Statement.   * @param value the value to set.
   */
  public void setStatement(com.linkedin.pegasus2avro.query.QueryStatement value) {
    this.statement = value;
  }

  /**
   * Gets the value of the 'source' field.
   * The source of the Query   */
  public com.linkedin.pegasus2avro.query.QuerySource getSource() {
    return source;
  }

  /**
   * Sets the value of the 'source' field.
   * The source of the Query   * @param value the value to set.
   */
  public void setSource(com.linkedin.pegasus2avro.query.QuerySource value) {
    this.source = value;
  }

  /**
   * Gets the value of the 'name' field.
   * Optional display name to identify the query.   */
  public java.lang.String getName() {
    return name;
  }

  /**
   * Sets the value of the 'name' field.
   * Optional display name to identify the query.   * @param value the value to set.
   */
  public void setName(java.lang.String value) {
    this.name = value;
  }

  /**
   * Gets the value of the 'description' field.
   * The Query description.   */
  public java.lang.String getDescription() {
    return description;
  }

  /**
   * Sets the value of the 'description' field.
   * The Query description.   * @param value the value to set.
   */
  public void setDescription(java.lang.String value) {
    this.description = value;
  }

  /**
   * Gets the value of the 'created' field.
   * Audit stamp capturing the time and actor who created the Query.   */
  public com.linkedin.pegasus2avro.common.AuditStamp getCreated() {
    return created;
  }

  /**
   * Sets the value of the 'created' field.
   * Audit stamp capturing the time and actor who created the Query.   * @param value the value to set.
   */
  public void setCreated(com.linkedin.pegasus2avro.common.AuditStamp value) {
    this.created = value;
  }

  /**
   * Gets the value of the 'lastModified' field.
   * Audit stamp capturing the time and actor who last modified the Query.   */
  public com.linkedin.pegasus2avro.common.AuditStamp getLastModified() {
    return lastModified;
  }

  /**
   * Sets the value of the 'lastModified' field.
   * Audit stamp capturing the time and actor who last modified the Query.   * @param value the value to set.
   */
  public void setLastModified(com.linkedin.pegasus2avro.common.AuditStamp value) {
    this.lastModified = value;
  }

  /** Creates a new QueryProperties RecordBuilder */
  public static com.linkedin.pegasus2avro.query.QueryProperties.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.query.QueryProperties.Builder();
  }
  
  /** Creates a new QueryProperties RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.query.QueryProperties.Builder newBuilder(com.linkedin.pegasus2avro.query.QueryProperties.Builder other) {
    return new com.linkedin.pegasus2avro.query.QueryProperties.Builder(other);
  }
  
  /** Creates a new QueryProperties RecordBuilder by copying an existing QueryProperties instance */
  public static com.linkedin.pegasus2avro.query.QueryProperties.Builder newBuilder(com.linkedin.pegasus2avro.query.QueryProperties other) {
    return new com.linkedin.pegasus2avro.query.QueryProperties.Builder(other);
  }
  
  /**
   * RecordBuilder for QueryProperties instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<QueryProperties>
    implements org.apache.avro.data.RecordBuilder<QueryProperties> {

    private com.linkedin.pegasus2avro.query.QueryStatement statement;
    private com.linkedin.pegasus2avro.query.QuerySource source;
    private java.lang.String name;
    private java.lang.String description;
    private com.linkedin.pegasus2avro.common.AuditStamp created;
    private com.linkedin.pegasus2avro.common.AuditStamp lastModified;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.query.QueryProperties.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.query.QueryProperties.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.statement)) {
        this.statement = data().deepCopy(fields()[0].schema(), other.statement);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.source)) {
        this.source = data().deepCopy(fields()[1].schema(), other.source);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.name)) {
        this.name = data().deepCopy(fields()[2].schema(), other.name);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.description)) {
        this.description = data().deepCopy(fields()[3].schema(), other.description);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.created)) {
        this.created = data().deepCopy(fields()[4].schema(), other.created);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.lastModified)) {
        this.lastModified = data().deepCopy(fields()[5].schema(), other.lastModified);
        fieldSetFlags()[5] = true;
      }
    }
    
    /** Creates a Builder by copying an existing QueryProperties instance */
    private Builder(com.linkedin.pegasus2avro.query.QueryProperties other) {
            super(com.linkedin.pegasus2avro.query.QueryProperties.SCHEMA$);
      if (isValidValue(fields()[0], other.statement)) {
        this.statement = data().deepCopy(fields()[0].schema(), other.statement);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.source)) {
        this.source = data().deepCopy(fields()[1].schema(), other.source);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.name)) {
        this.name = data().deepCopy(fields()[2].schema(), other.name);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.description)) {
        this.description = data().deepCopy(fields()[3].schema(), other.description);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.created)) {
        this.created = data().deepCopy(fields()[4].schema(), other.created);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.lastModified)) {
        this.lastModified = data().deepCopy(fields()[5].schema(), other.lastModified);
        fieldSetFlags()[5] = true;
      }
    }

    /** Gets the value of the 'statement' field */
    public com.linkedin.pegasus2avro.query.QueryStatement getStatement() {
      return statement;
    }
    
    /** Sets the value of the 'statement' field */
    public com.linkedin.pegasus2avro.query.QueryProperties.Builder setStatement(com.linkedin.pegasus2avro.query.QueryStatement value) {
      validate(fields()[0], value);
      this.statement = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'statement' field has been set */
    public boolean hasStatement() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'statement' field */
    public com.linkedin.pegasus2avro.query.QueryProperties.Builder clearStatement() {
      statement = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'source' field */
    public com.linkedin.pegasus2avro.query.QuerySource getSource() {
      return source;
    }
    
    /** Sets the value of the 'source' field */
    public com.linkedin.pegasus2avro.query.QueryProperties.Builder setSource(com.linkedin.pegasus2avro.query.QuerySource value) {
      validate(fields()[1], value);
      this.source = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'source' field has been set */
    public boolean hasSource() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'source' field */
    public com.linkedin.pegasus2avro.query.QueryProperties.Builder clearSource() {
      source = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'name' field */
    public java.lang.String getName() {
      return name;
    }
    
    /** Sets the value of the 'name' field */
    public com.linkedin.pegasus2avro.query.QueryProperties.Builder setName(java.lang.String value) {
      validate(fields()[2], value);
      this.name = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'name' field has been set */
    public boolean hasName() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'name' field */
    public com.linkedin.pegasus2avro.query.QueryProperties.Builder clearName() {
      name = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'description' field */
    public java.lang.String getDescription() {
      return description;
    }
    
    /** Sets the value of the 'description' field */
    public com.linkedin.pegasus2avro.query.QueryProperties.Builder setDescription(java.lang.String value) {
      validate(fields()[3], value);
      this.description = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'description' field has been set */
    public boolean hasDescription() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'description' field */
    public com.linkedin.pegasus2avro.query.QueryProperties.Builder clearDescription() {
      description = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /** Gets the value of the 'created' field */
    public com.linkedin.pegasus2avro.common.AuditStamp getCreated() {
      return created;
    }
    
    /** Sets the value of the 'created' field */
    public com.linkedin.pegasus2avro.query.QueryProperties.Builder setCreated(com.linkedin.pegasus2avro.common.AuditStamp value) {
      validate(fields()[4], value);
      this.created = value;
      fieldSetFlags()[4] = true;
      return this; 
    }
    
    /** Checks whether the 'created' field has been set */
    public boolean hasCreated() {
      return fieldSetFlags()[4];
    }
    
    /** Clears the value of the 'created' field */
    public com.linkedin.pegasus2avro.query.QueryProperties.Builder clearCreated() {
      created = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /** Gets the value of the 'lastModified' field */
    public com.linkedin.pegasus2avro.common.AuditStamp getLastModified() {
      return lastModified;
    }
    
    /** Sets the value of the 'lastModified' field */
    public com.linkedin.pegasus2avro.query.QueryProperties.Builder setLastModified(com.linkedin.pegasus2avro.common.AuditStamp value) {
      validate(fields()[5], value);
      this.lastModified = value;
      fieldSetFlags()[5] = true;
      return this; 
    }
    
    /** Checks whether the 'lastModified' field has been set */
    public boolean hasLastModified() {
      return fieldSetFlags()[5];
    }
    
    /** Clears the value of the 'lastModified' field */
    public com.linkedin.pegasus2avro.query.QueryProperties.Builder clearLastModified() {
      lastModified = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    @Override
    public QueryProperties build() {
      try {
        QueryProperties record = new QueryProperties();
        record.statement = fieldSetFlags()[0] ? this.statement : (com.linkedin.pegasus2avro.query.QueryStatement) defaultValue(fields()[0]);
        record.source = fieldSetFlags()[1] ? this.source : (com.linkedin.pegasus2avro.query.QuerySource) defaultValue(fields()[1]);
        record.name = fieldSetFlags()[2] ? this.name : (java.lang.String) defaultValue(fields()[2]);
        record.description = fieldSetFlags()[3] ? this.description : (java.lang.String) defaultValue(fields()[3]);
        record.created = fieldSetFlags()[4] ? this.created : (com.linkedin.pegasus2avro.common.AuditStamp) defaultValue(fields()[4]);
        record.lastModified = fieldSetFlags()[5] ? this.lastModified : (com.linkedin.pegasus2avro.common.AuditStamp) defaultValue(fields()[5]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
