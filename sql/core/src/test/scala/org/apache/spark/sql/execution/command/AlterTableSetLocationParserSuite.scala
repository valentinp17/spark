/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.execution.command

import org.apache.spark.sql.catalyst.analysis.{AnalysisTest, UnresolvedTable}
import org.apache.spark.sql.catalyst.parser.CatalystSqlParser.parsePlan
import org.apache.spark.sql.catalyst.plans.logical.SetTableLocation
import org.apache.spark.sql.test.SharedSparkSession

class AlterTableSetLocationParserSuite extends AnalysisTest with SharedSparkSession {

  test("alter table: set location") {
    val sql1 = "ALTER TABLE a.b.c SET LOCATION 'new location'"
    val parsed1 = parsePlan(sql1)
    val expected1 = SetTableLocation(
      UnresolvedTable(Seq("a", "b", "c"), "ALTER TABLE ... SET LOCATION ...", true),
      None,
      "new location")
    comparePlans(parsed1, expected1)

    val sql2 = "ALTER TABLE a.b.c PARTITION(ds='2017-06-10') SET LOCATION 'new location'"
    val parsed2 = parsePlan(sql2)
    val expected2 = SetTableLocation(
      UnresolvedTable(Seq("a", "b", "c"), "ALTER TABLE ... SET LOCATION ...", true),
      Some(Map("ds" -> "2017-06-10")),
      "new location")
    comparePlans(parsed2, expected2)
  }
}
