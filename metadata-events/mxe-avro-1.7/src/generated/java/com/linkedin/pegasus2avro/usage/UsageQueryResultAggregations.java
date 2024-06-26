/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.pegasus2avro.usage;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class UsageQueryResultAggregations extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"UsageQueryResultAggregations\",\"namespace\":\"com.linkedin.pegasus2avro.usage\",\"fields\":[{\"name\":\"uniqueUserCount\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"totalSqlQueries\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"users\",\"type\":[\"null\",{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"UserUsageCounts\",\"doc\":\" Records a single user's usage counts for a given resource \",\"fields\":[{\"name\":\"user\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"default\":null,\"java\":{\"class\":\"com.linkedin.pegasus2avro.common.urn.Urn\"}},{\"name\":\"count\",\"type\":\"int\"},{\"name\":\"userEmail\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"doc\":\" If user_email is set, we attempt to resolve the user's urn upon ingest \",\"default\":null}]}}],\"default\":null},{\"name\":\"fields\",\"type\":[\"null\",{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"FieldUsageCounts\",\"doc\":\" Records field-level usage counts for a given resource \",\"fields\":[{\"name\":\"fieldName\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"count\",\"type\":\"int\"}]}}],\"default\":null}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.Integer uniqueUserCount;
  @Deprecated public java.lang.Integer totalSqlQueries;
  @Deprecated public java.util.List<com.linkedin.pegasus2avro.usage.UserUsageCounts> users;
  @Deprecated public java.util.List<com.linkedin.pegasus2avro.usage.FieldUsageCounts> fields;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public UsageQueryResultAggregations() {}

  /**
   * All-args constructor.
   */
  public UsageQueryResultAggregations(java.lang.Integer uniqueUserCount, java.lang.Integer totalSqlQueries, java.util.List<com.linkedin.pegasus2avro.usage.UserUsageCounts> users, java.util.List<com.linkedin.pegasus2avro.usage.FieldUsageCounts> fields) {
    this.uniqueUserCount = uniqueUserCount;
    this.totalSqlQueries = totalSqlQueries;
    this.users = users;
    this.fields = fields;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return uniqueUserCount;
    case 1: return totalSqlQueries;
    case 2: return users;
    case 3: return fields;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: uniqueUserCount = (java.lang.Integer)value$; break;
    case 1: totalSqlQueries = (java.lang.Integer)value$; break;
    case 2: users = (java.util.List<com.linkedin.pegasus2avro.usage.UserUsageCounts>)value$; break;
    case 3: fields = (java.util.List<com.linkedin.pegasus2avro.usage.FieldUsageCounts>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'uniqueUserCount' field.
   */
  public java.lang.Integer getUniqueUserCount() {
    return uniqueUserCount;
  }

  /**
   * Sets the value of the 'uniqueUserCount' field.
   * @param value the value to set.
   */
  public void setUniqueUserCount(java.lang.Integer value) {
    this.uniqueUserCount = value;
  }

  /**
   * Gets the value of the 'totalSqlQueries' field.
   */
  public java.lang.Integer getTotalSqlQueries() {
    return totalSqlQueries;
  }

  /**
   * Sets the value of the 'totalSqlQueries' field.
   * @param value the value to set.
   */
  public void setTotalSqlQueries(java.lang.Integer value) {
    this.totalSqlQueries = value;
  }

  /**
   * Gets the value of the 'users' field.
   */
  public java.util.List<com.linkedin.pegasus2avro.usage.UserUsageCounts> getUsers() {
    return users;
  }

  /**
   * Sets the value of the 'users' field.
   * @param value the value to set.
   */
  public void setUsers(java.util.List<com.linkedin.pegasus2avro.usage.UserUsageCounts> value) {
    this.users = value;
  }

  /**
   * Gets the value of the 'fields' field.
   */
  public java.util.List<com.linkedin.pegasus2avro.usage.FieldUsageCounts> getFields() {
    return fields;
  }

  /**
   * Sets the value of the 'fields' field.
   * @param value the value to set.
   */
  public void setFields(java.util.List<com.linkedin.pegasus2avro.usage.FieldUsageCounts> value) {
    this.fields = value;
  }

  /** Creates a new UsageQueryResultAggregations RecordBuilder */
  public static com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.Builder newBuilder() {
    return new com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.Builder();
  }
  
  /** Creates a new UsageQueryResultAggregations RecordBuilder by copying an existing Builder */
  public static com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.Builder newBuilder(com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.Builder other) {
    return new com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.Builder(other);
  }
  
  /** Creates a new UsageQueryResultAggregations RecordBuilder by copying an existing UsageQueryResultAggregations instance */
  public static com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.Builder newBuilder(com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations other) {
    return new com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.Builder(other);
  }
  
  /**
   * RecordBuilder for UsageQueryResultAggregations instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<UsageQueryResultAggregations>
    implements org.apache.avro.data.RecordBuilder<UsageQueryResultAggregations> {

    private java.lang.Integer uniqueUserCount;
    private java.lang.Integer totalSqlQueries;
    private java.util.List<com.linkedin.pegasus2avro.usage.UserUsageCounts> users;
    private java.util.List<com.linkedin.pegasus2avro.usage.FieldUsageCounts> fields;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.uniqueUserCount)) {
        this.uniqueUserCount = data().deepCopy(fields()[0].schema(), other.uniqueUserCount);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.totalSqlQueries)) {
        this.totalSqlQueries = data().deepCopy(fields()[1].schema(), other.totalSqlQueries);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.users)) {
        this.users = data().deepCopy(fields()[2].schema(), other.users);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.fields)) {
        this.fields = data().deepCopy(fields()[3].schema(), other.fields);
        fieldSetFlags()[3] = true;
      }
    }
    
    /** Creates a Builder by copying an existing UsageQueryResultAggregations instance */
    private Builder(com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations other) {
            super(com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.SCHEMA$);
      if (isValidValue(fields()[0], other.uniqueUserCount)) {
        this.uniqueUserCount = data().deepCopy(fields()[0].schema(), other.uniqueUserCount);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.totalSqlQueries)) {
        this.totalSqlQueries = data().deepCopy(fields()[1].schema(), other.totalSqlQueries);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.users)) {
        this.users = data().deepCopy(fields()[2].schema(), other.users);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.fields)) {
        this.fields = data().deepCopy(fields()[3].schema(), other.fields);
        fieldSetFlags()[3] = true;
      }
    }

    /** Gets the value of the 'uniqueUserCount' field */
    public java.lang.Integer getUniqueUserCount() {
      return uniqueUserCount;
    }
    
    /** Sets the value of the 'uniqueUserCount' field */
    public com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.Builder setUniqueUserCount(java.lang.Integer value) {
      validate(fields()[0], value);
      this.uniqueUserCount = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'uniqueUserCount' field has been set */
    public boolean hasUniqueUserCount() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'uniqueUserCount' field */
    public com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.Builder clearUniqueUserCount() {
      uniqueUserCount = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'totalSqlQueries' field */
    public java.lang.Integer getTotalSqlQueries() {
      return totalSqlQueries;
    }
    
    /** Sets the value of the 'totalSqlQueries' field */
    public com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.Builder setTotalSqlQueries(java.lang.Integer value) {
      validate(fields()[1], value);
      this.totalSqlQueries = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'totalSqlQueries' field has been set */
    public boolean hasTotalSqlQueries() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'totalSqlQueries' field */
    public com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.Builder clearTotalSqlQueries() {
      totalSqlQueries = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'users' field */
    public java.util.List<com.linkedin.pegasus2avro.usage.UserUsageCounts> getUsers() {
      return users;
    }
    
    /** Sets the value of the 'users' field */
    public com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.Builder setUsers(java.util.List<com.linkedin.pegasus2avro.usage.UserUsageCounts> value) {
      validate(fields()[2], value);
      this.users = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'users' field has been set */
    public boolean hasUsers() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'users' field */
    public com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.Builder clearUsers() {
      users = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'fields' field */
    public java.util.List<com.linkedin.pegasus2avro.usage.FieldUsageCounts> getFields() {
      return fields;
    }
    
    /** Sets the value of the 'fields' field */
    public com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.Builder setFields(java.util.List<com.linkedin.pegasus2avro.usage.FieldUsageCounts> value) {
      validate(fields()[3], value);
      this.fields = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'fields' field has been set */
    public boolean hasFields() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'fields' field */
    public com.linkedin.pegasus2avro.usage.UsageQueryResultAggregations.Builder clearFields() {
      fields = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    public UsageQueryResultAggregations build() {
      try {
        UsageQueryResultAggregations record = new UsageQueryResultAggregations();
        record.uniqueUserCount = fieldSetFlags()[0] ? this.uniqueUserCount : (java.lang.Integer) defaultValue(fields()[0]);
        record.totalSqlQueries = fieldSetFlags()[1] ? this.totalSqlQueries : (java.lang.Integer) defaultValue(fields()[1]);
        record.users = fieldSetFlags()[2] ? this.users : (java.util.List<com.linkedin.pegasus2avro.usage.UserUsageCounts>) defaultValue(fields()[2]);
        record.fields = fieldSetFlags()[3] ? this.fields : (java.util.List<com.linkedin.pegasus2avro.usage.FieldUsageCounts>) defaultValue(fields()[3]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
