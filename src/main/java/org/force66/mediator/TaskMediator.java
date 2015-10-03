/*
 * This software is licensed under the Apache License, Version 2.0
 * (the "License") agreement; you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.force66.mediator;

import java.util.concurrent.Callable;

import org.apache.commons.lang3.Validate;

/**
 * Will handle task errors and provide a default response. This is used to
 * provide partial functionality in the event of some type of outage.
 *
 * @author D. Ashmore
 *
 * @param <T>
 */
public class TaskMediator<T> {

	private final T defaultResponse;
	private final TaskErrorHandler taskErrorHandler;

	public TaskMediator(T defaultResponse) {
		this(defaultResponse, new Slf4jTaskErrorHandler());
	}

	public TaskMediator(T defaultResponse, TaskErrorHandler errorHandler) {
		Validate.notNull(defaultResponse, "Null defaultResponse not allowed.");
		Validate.notNull(errorHandler, "Null errorHandler not allowed.");
		this.defaultResponse = defaultResponse;
		this.taskErrorHandler = errorHandler;
	}

	public T invoke(Callable<T> operation) {
		Validate.notNull(operation, "Null operation not allowed.");
		try {
			T output = operation.call();
			return output;
		} catch (Exception e) {
			taskErrorHandler.handle(e, operation);
			return defaultResponse;
		}
	}

}
