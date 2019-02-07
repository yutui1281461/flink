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

package org.apache.flink.api.scala.typeutils;

import org.apache.flink.api.common.typeutils.TypeSerializerSnapshotMigrationTestBase;
import org.apache.flink.api.common.typeutils.base.IntSerializer;
import org.apache.flink.testutils.migration.MigrationVersion;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import scala.collection.TraversableOnce;

/**
 * {@link TraversableSerializer} migration test.
 */
@RunWith(Parameterized.class)
public class TraversableSerializerSnapshotMigrationTest
		extends TypeSerializerSnapshotMigrationTestBase<TraversableOnce<?>> {

	public TraversableSerializerSnapshotMigrationTest(
			TestSpecification<TraversableOnce<?>> testSpecification) {
		super(testSpecification);
	}

	@SuppressWarnings("unchecked")
	@Parameterized.Parameters(name = "Test Specification = {0}")
	public static Collection<TestSpecification<?>> testSpecifications() {

		final TestSpecifications testSpecifications =
				new TestSpecifications(MigrationVersion.v1_7);

		testSpecifications.add(
				"traversable-serializer-bitset",
				TraversableSerializer.class,
				TraversableSerializerSnapshot.class,
				() -> new TraversableSerializer(
						IntSerializer.INSTANCE,
						"implicitly[scala.collection.generic.CanBuildFrom[scala.collection.immutable.BitSet, Int, scala.collection.immutable.BitSet]]"),
				TRAVERSABLE_ONCE_MATCHER);

		return testSpecifications.get();
	}

	private static final Matcher<TraversableOnce<?>> TRAVERSABLE_ONCE_MATCHER =
			new TraversableOnceMatcher();

	private static final class TraversableOnceMatcher extends BaseMatcher<TraversableOnce<?>> {

		@Override
		public boolean matches(Object item) {
			return item instanceof TraversableOnce;
		}

		@Override
		public void describeTo(Description description) {
			description.appendText("a TraversableOnce");
		}
	}

}
