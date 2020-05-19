/*
 * Copyright 2020 Google LLC
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
package com.google.solutions.df.log.aggregations;

import com.google.solutions.df.log.aggregations.common.fraud.detection.FraudDetectionFinServTranPipelineOptions;
import com.google.solutions.df.log.aggregations.common.fraud.detection.PredictTransform;
import com.google.solutions.df.log.aggregations.common.fraud.detection.ReadTransactionTransform;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.PCollection;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FraudDetectionFinServTranPipeline {
  public static final Logger LOG = LoggerFactory.getLogger(FraudDetectionFinServTranPipeline.class);
  private static final Duration DEFAULT_POLL_INTERVAL = Duration.standardSeconds(30);

  public static void main(String args[]) {

    FraudDetectionFinServTranPipelineOptions options =
        PipelineOptionsFactory.fromArgs(args)
            .withValidation()
            .as(FraudDetectionFinServTranPipelineOptions.class);
    run(options);
  }

  public static PipelineResult run(FraudDetectionFinServTranPipelineOptions options) {

    Pipeline p = Pipeline.create(options);
    PCollection<String> transaction =
        p.apply(
            "ReadTransactionTransform",
            ReadTransactionTransform.newBuilder()
                .setFilePattern(options.getInputFilePattern())
                .setPollInterval(DEFAULT_POLL_INTERVAL)
                .setSubscriber(options.getSubscriberId())
                .build());
    //            .apply(
    //                "Fixed Window",
    //                Window.<String>into(
    //
    // FixedWindows.of(Duration.standardSeconds(options.getWindowInterval())))
    //                    .triggering(AfterWatermark.pastEndOfWindow())
    //                    .discardingFiredPanes()
    //                    .withAllowedLateness(Duration.ZERO));

    transaction.apply(
        "Predict",
        PredictTransform.newBuilder()
            .setBatchSize(options.getBatchSize())
            .setModelId(options.getModelId())
            .setVersionId(options.getVersionId())
            .setProjectId(options.getProject())
            .setRandomKey(options.getKeyRange())
            .build());

    return p.run();
  }
}