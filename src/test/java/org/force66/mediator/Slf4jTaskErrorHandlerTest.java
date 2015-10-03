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

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class Slf4jTaskErrorHandlerTest {

	@Mock
	Logger logger;

	Slf4jTaskErrorHandler taskErrorHandler;

	@Before
	public void setUp() throws Exception {
		taskErrorHandler = new Slf4jTaskErrorHandler(logger);
	}

	@Test
	public void testGetLogLevel() throws Exception {
		Assert.assertEquals(Slf4jTaskErrorHandler.DEFAULT_LOG_LEVEL.ERROR,
				taskErrorHandler.getLogLevel());
	}

	@Test
	public void testHandle() throws Exception {
		String errorMessage = (String) FieldUtils.readDeclaredField(
				taskErrorHandler, "message", true);
		Exception exception = new RuntimeException("crap");

		// Test ERROR logging
		taskErrorHandler.handle(exception, null);
		Mockito.verify(logger).error(errorMessage, exception);

		// Test WARNING logging
		taskErrorHandler = new Slf4jTaskErrorHandler(logger, errorMessage,
				Slf4jTaskErrorHandler.Slf4jLogLevel.WARNING);
		taskErrorHandler.handle(exception, null);
		Mockito.verify(logger).warn(errorMessage, exception);

		// Test INFO logging
		taskErrorHandler = new Slf4jTaskErrorHandler(logger, errorMessage,
				Slf4jTaskErrorHandler.Slf4jLogLevel.INFO);
		taskErrorHandler.handle(exception, null);
		Mockito.verify(logger).info(errorMessage, exception);

		// Test TRACE logging
		taskErrorHandler = new Slf4jTaskErrorHandler(logger, errorMessage,
				Slf4jTaskErrorHandler.Slf4jLogLevel.TRACE);
		taskErrorHandler.handle(exception, null);
		Mockito.verify(logger).trace(errorMessage, exception);
	}

	@Test
	public void testSlf4jTaskErrorHandler() throws Exception {
		// Logger constructor test
		Assert.assertEquals(logger,
				FieldUtils.readDeclaredField(taskErrorHandler, "logger", true));
		Assert.assertEquals(Slf4jTaskErrorHandler.DEFAULT_MESSAGE,
				FieldUtils.readDeclaredField(taskErrorHandler, "message", true));
		Assert.assertEquals(Slf4jTaskErrorHandler.DEFAULT_LOG_LEVEL.ERROR,
				FieldUtils
						.readDeclaredField(taskErrorHandler, "logLevel", true));

		// Logger/Message constructor test
		taskErrorHandler = new Slf4jTaskErrorHandler(logger, "MyMessage");
		Assert.assertEquals(logger,
				FieldUtils.readDeclaredField(taskErrorHandler, "logger", true));
		Assert.assertEquals("MyMessage",
				FieldUtils.readDeclaredField(taskErrorHandler, "message", true));
		Assert.assertEquals(Slf4jTaskErrorHandler.DEFAULT_LOG_LEVEL.ERROR,
				FieldUtils
						.readDeclaredField(taskErrorHandler, "logLevel", true));

		// Logger/Message/Error level constructor test
		taskErrorHandler = new Slf4jTaskErrorHandler(logger, "MyMessage",
				Slf4jTaskErrorHandler.Slf4jLogLevel.WARNING);
		Assert.assertEquals(logger,
				FieldUtils.readDeclaredField(taskErrorHandler, "logger", true));
		Assert.assertEquals("MyMessage",
				FieldUtils.readDeclaredField(taskErrorHandler, "message", true));
		Assert.assertEquals(Slf4jTaskErrorHandler.Slf4jLogLevel.WARNING,
				FieldUtils
						.readDeclaredField(taskErrorHandler, "logLevel", true));

		// Null Constructor test
		taskErrorHandler = new Slf4jTaskErrorHandler();
		Assert.assertNotNull(FieldUtils.readDeclaredField(taskErrorHandler,
				"logger", true));
		Assert.assertEquals(Slf4jTaskErrorHandler.DEFAULT_MESSAGE,
				FieldUtils.readDeclaredField(taskErrorHandler, "message", true));
		Assert.assertEquals(Slf4jTaskErrorHandler.DEFAULT_LOG_LEVEL.ERROR,
				FieldUtils
						.readDeclaredField(taskErrorHandler, "logLevel", true));
	}

}
