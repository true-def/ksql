/*
 * Copyright 2020 Confluent Inc.
 *
 * Licensed under the Confluent Community License (the "License"); you may not use
 * this file except in compliance with the License.  You may obtain a copy of the
 * License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.confluent.ksql.api.client.impl;

import io.confluent.ksql.api.client.ColumnType;
import io.confluent.ksql.api.client.KsqlArray;
import io.confluent.ksql.api.client.KsqlObject;
import io.confluent.ksql.api.client.Row;
import io.vertx.core.json.JsonArray;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RowImpl implements Row {

  private final List<String> columnNames;
  private final List<ColumnType> columnTypes;
  private final KsqlArray values;
  private final Map<String, Integer> columnNameToIndex;

  @SuppressWarnings("unchecked")
  public RowImpl(
      final List<String> columnNames,
      final List<ColumnType> columnTypes,
      final JsonArray values,
      final Map<String, Integer> columnNameToIndex) {
    this.columnNames = Objects.requireNonNull(columnNames);
    this.columnTypes = Objects.requireNonNull(columnTypes);
    this.values = new KsqlArray(Objects.requireNonNull(values).getList());
    this.columnNameToIndex = Objects.requireNonNull(columnNameToIndex);
  }

  @Override
  public List<String> columnNames() {
    return columnNames;
  }

  @Override
  public List<ColumnType> columnTypes() {
    return columnTypes;
  }

  @Override
  public KsqlArray values() {
    return values;
  }

  @Override
  public KsqlObject asObject() {
    return KsqlObject.fromArray(columnNames, values);
  }

  @Override
  public boolean isNull(final int columnIndex) {
    return getValue(columnIndex) == null;
  }

  @Override
  public boolean isNull(final String columnName) {
    return isNull(indexFromName(columnName));
  }

  @Override
  public Object getValue(final int columnIndex) {
    return values.getValue(columnIndex - 1);
  }

  @Override
  public Object getValue(final String columnName) {
    return getValue(indexFromName(columnName));
  }

  @Override
  public String getString(final int columnIndex) {
    return values.getString(columnIndex - 1);
  }

  @Override
  public String getString(final String columnName) {
    return getString(indexFromName(columnName));
  }

  @Override
  public Integer getInteger(final int columnIndex) {
    return values.getInteger(columnIndex - 1);
  }

  @Override
  public Integer getInteger(final String columnName) {
    return getInteger(indexFromName(columnName));
  }

  @Override
  public Long getLong(final int columnIndex) {
    return values.getLong(columnIndex - 1);
  }

  @Override
  public Long getLong(final String columnName) {
    return getLong(indexFromName(columnName));
  }

  @Override
  public Double getDouble(final int columnIndex) {
    return values.getDouble(columnIndex - 1);
  }

  @Override
  public Double getDouble(final String columnName) {
    return getDouble(indexFromName(columnName));
  }

  @Override
  public Boolean getBoolean(final int columnIndex) {
    return values.getBoolean(columnIndex - 1);
  }

  @Override
  public Boolean getBoolean(final String columnName) {
    return getBoolean(indexFromName(columnName));
  }

  @Override
  public BigDecimal getDecimal(final int columnIndex) {
    return values.getDecimal(columnIndex - 1);
  }

  @Override
  public BigDecimal getDecimal(final String columnName) {
    return getDecimal(indexFromName(columnName));
  }

  @Override
  public KsqlObject getKsqlObject(final int columnIndex) {
    return values.getKsqlObject(columnIndex - 1);
  }

  @Override
  public KsqlObject getKsqlObject(final String columnName) {
    return getKsqlObject(indexFromName(columnName));
  }

  @Override
  public KsqlArray getKsqlArray(final int columnIndex) {
    return values.getKsqlArray(columnIndex - 1);
  }

  @Override
  public KsqlArray getKsqlArray(final String columnName) {
    return getKsqlArray(indexFromName(columnName));
  }

  private int indexFromName(final String columnName) {
    final Integer index = columnNameToIndex.get(columnName);
    if (index == null) {
      throw new IllegalArgumentException("No column exists with name: " + columnName);
    }
    return index;
  }
}