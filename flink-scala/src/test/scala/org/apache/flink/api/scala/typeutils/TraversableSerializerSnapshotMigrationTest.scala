/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.api.scala.typeutils

import java.util
import java.util.function.Supplier

import org.apache.flink.api.common.ExecutionConfig
import org.apache.flink.api.common.typeutils.{TypeSerializer, TypeSerializerSnapshotMigrationTestBase}
import org.apache.flink.testutils.migration.MigrationVersion
import org.apache.flink.api.scala.createTypeInformation
import org.hamcrest.{BaseMatcher, Description, Matcher}
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

import scala.collection.BitSet

/**
  * [[TraversableSerializer]] migration test.
  */
@RunWith(classOf[Parameterized])
class TraversableSerializerSnapshotMigrationTest(testSpecification: TypeSerializerSnapshotMigrationTestBase.TestSpecification[TraversableOnce[_]])
  extends TypeSerializerSnapshotMigrationTestBase[TraversableOnce[_]](testSpecification) {


}

object TraversableSerializerSnapshotMigrationTest {

  @SuppressWarnings(Array("unchecked"))
  @Parameterized.Parameters(name = "Test Specification = {0}")
  def testSpecifications: util.Collection[TypeSerializerSnapshotMigrationTestBase.TestSpecification[_]] = {
    val testSpecifications: TypeSerializerSnapshotMigrationTestBase.TestSpecifications =
      new TypeSerializerSnapshotMigrationTestBase.TestSpecifications(MigrationVersion.v1_7)


    testSpecifications.add(
      "traversable-serializer-bitset",
      classOf[TraversableSerializer[BitSet, Int]],
      classOf[TraversableSerializerSnapshot[BitSet, Int]],
      new Supplier[TypeSerializer[TraversableOnce[_]]] {
        override def get(): TypeSerializer[TraversableOnce[_]] = {
          val ser = createTypeInformation[BitSet].createSerializer(new ExecutionConfig).asInstanceOf[TypeSerializer[TraversableOnce[_]]]
          ser
        }
      },
      TRAVERSABLE_ONCE_MATCHER)

    testSpecifications.get
  }

  private val TRAVERSABLE_ONCE_MATCHER: Matcher[TraversableOnce[_]] = new TraversableOnceMatcher

  final private class TraversableOnceMatcher extends BaseMatcher[TraversableOnce[_]] {
    override def matches(item: Any): Boolean = item.isInstanceOf[TraversableOnce[_]]

    override def describeTo(description: Description): Unit = {
      description.appendText("a TraversableOnce")
    }
  }

}