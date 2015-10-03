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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Will log exceptions via SLF4J.
 *
 * @author D. Ashmore
 *
 */
public class Slf4jTaskErrorHandler implements TaskErrorHandler {

	public static enum Slf4jLogLevel {
		WARNING, TRACE, ERROR, INFO
	}

	public static final Slf4jLogLevel DEFAULT_LOG_LEVEL = Slf4jLogLevel.ERROR;
	public static final String DEFAULT_MESSAGE = "Mediated Task failure";
	private final Slf4jLogLevel logLevel;
	private final Logger logger;
	private final String message;

	public Slf4jTaskErrorHandler() {
		this(LoggerFactory.getLogger(TaskMediator.class), DEFAULT_MESSAGE,
				DEFAULT_LOG_LEVEL);
	}

	public Slf4jTaskErrorHandler(Logger slf4jLogger) {
		this(slf4jLogger, DEFAULT_MESSAGE, DEFAULT_LOG_LEVEL);
	}

	public Slf4jTaskErrorHandler(Logger slf4jLogger, String slf4jMessage) {
		this(slf4jLogger, slf4jMessage, DEFAULT_LOG_LEVEL);
	}

	public Slf4jTaskErrorHandler(Logger slf4jLogger, String slf4jMessage,
			Slf4jLogLevel slf4jLogLevel) {
		Validate.notNull(slf4jLogger, "Null slf4jLogger not allowed.");
		Validate.notEmpty(slf4jMessage, "Blank slf4jMessage not allowed.");
		Validate.notNull(slf4jLogLevel, "Null slf4jLogLevel not allowed.");
		logger = slf4jLogger;
		logLevel = slf4jLogLevel;
		message = slf4jMessage;
	}

	public Slf4jLogLevel getLogLevel() {
		return logLevel;
	}

	@Override
	public void handle(Exception ex, Callable<?> exceptingCallable) {
		if (Slf4jLogLevel.INFO.equals(logLevel)) {
			logger.info(message, ex);
		} else if (Slf4jLogLevel.WARNING.equals(logLevel)) {
			logger.warn(message, ex);
		} else if (Slf4jLogLevel.TRACE.equals(logLevel)) {
			logger.trace(message, ex);
		} else {
			logger.error(message, ex);
		}

	}

}
