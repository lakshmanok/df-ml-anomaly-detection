/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.solutions.df.log.aggregations.common;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoValue
public abstract class CentroidVector implements Serializable {
  private static final Logger LOG = LoggerFactory.getLogger(CentroidVector.class);

  public abstract Integer centroidId();

  public abstract Double normalizedDistance();

  public abstract ImmutableList<Double> featureVectors();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setCentroidId(Integer value);

    public abstract Builder setNormalizedDistance(Double value);

    public abstract Builder setFeatureVectors(ImmutableList<Double> values);

    public abstract CentroidVector build();
  }

  public static Builder newBuilder() {
    return new AutoValue_CentroidVector.Builder();
  }
}
