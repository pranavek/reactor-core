/*
 * Copyright (c) 2011-2018 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package reactor.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;

import reactor.util.function.TriFunction;
import reactor.util.function.Tuple2;

import static io.micrometer.core.instrument.Metrics.globalRegistry;

/**
 * Utilities around instrumentation and metrics with Micrometer.
 *
 * @author Simon Baslé
 */
public class Metrics {

	static final boolean isMicrometerAvailable;

	static {
		boolean micrometer;
		try {
			globalRegistry.getRegistries();
			micrometer = true;
		}
		catch (Throwable t) {
			micrometer = false;
		}
		isMicrometerAvailable = micrometer;
	}

	/**
	 * Check if the current runtime supports metrics / instrumentation, by
	 * verifying if Micrometer is on the classpath.
	 *
	 * @return true if the Micrometer instrumentation facade is available
	 */
	public static final boolean isInstrumentationAvailable() {
		return isMicrometerAvailable;
	}


	/**
	 * If the Micrometer instrumentation facade is available, return a {@link BiFunction}
	 * that takes a {@link Tuple2} of {@link String} (category and description) and a {@link ScheduledExecutorService} to instrument.
	 * It can be applied to instruments {@link ExecutorService}-based schedulers (as
	 * supported by Micrometer, ie. it instruments state of queues but not timing of tasks).
	 * <p>
	 * This factory sends instrumentation data to the Micrometer Global Registry.
	 *
	 * @implNote Note that this is added as a decorator via Schedulers when enabling metrics for schedulers, which doesn't change the Factory.
	 *
	 * @return a {@link BiFunction} that decorates {@link ScheduledExecutorService} to instrument their internal state,
	 * or is the identity function if Micrometer isn't available.
	 */
	public static TriFunction<String, String, ScheduledExecutorService, ScheduledExecutorService> instrumentedExecutorService() {
		if (isMicrometerAvailable) {
			return new MicrometerSchedulersDecorator();
		}
		return (type, desc, service) -> service;
	}

	static final class MicrometerSchedulersDecorator
			implements TriFunction<String, String, ScheduledExecutorService, ScheduledExecutorService> {

		private Map<String, Long> seenSchedulers = new HashMap<>();

		@Override
		public ScheduledExecutorService apply(String schedulerType, String schedulerDescription,
				ScheduledExecutorService service) {
			Long number = seenSchedulers.compute(schedulerType, (it, key) -> key == null ? 1 : key + 1);
			String executorNumber = schedulerType + "-exec" + number;

			ExecutorServiceMetrics.monitor(globalRegistry, service, schedulerType,
					Tag.of("scheduler", schedulerDescription),
					Tag.of("executorId", executorNumber));
			return service;
		}
	}
}
