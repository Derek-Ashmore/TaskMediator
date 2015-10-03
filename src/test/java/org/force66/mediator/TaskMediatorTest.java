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

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TaskMediatorTest {

	public static class MyCallable implements Callable<String> {

		private String response;
		private Exception exception;

		@Override
		public String call() throws Exception {
			if (exception != null) {
				throw exception;
			}
			return response;
		}

		public Exception getException() {
			return exception;
		}

		public String getResponse() {
			return response;
		}

		public void setException(Exception exception) {
			this.exception = exception;
		}

		public void setResponse(String response) {
			this.response = response;
		}

	}

	static final String TEST_RESPONSE = "myResponse";
	static final String TEST_CALLABLE_RESPONSE = "myCallableResponse";

	TaskMediator<String> taskMediator;
	MyCallable callable;

	@Mock
	TaskErrorHandler errorHandler;

	@Before
	public void setUp() throws Exception {
		taskMediator = new TaskMediator<String>(TEST_RESPONSE, errorHandler);
		callable = new MyCallable();
		callable.setResponse(TEST_CALLABLE_RESPONSE);
	}

	@Test
	public void testInvoke() throws Exception {
		// basic happy path
		Assert.assertEquals(TEST_CALLABLE_RESPONSE,
				taskMediator.invoke(callable));

		// exception test
		Exception testException = new RuntimeException("Crap");
		callable.setException(testException);
		Assert.assertEquals(TEST_RESPONSE, taskMediator.invoke(callable));
		Mockito.verify(errorHandler).handle(testException, callable);

		try {
			taskMediator.invoke(null);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains("not allowed"));
		}
	}

	@Test
	public void testTaskMediatorTTaskErrorHandler() throws Exception {
		// Response/Error handler constructor test
		Assert.assertEquals(TEST_RESPONSE, FieldUtils.readDeclaredField(
				taskMediator, "defaultResponse", true));
		Assert.assertEquals(errorHandler, FieldUtils.readDeclaredField(
				taskMediator, "taskErrorHandler", true));

		try {
			taskMediator = new TaskMediator<String>(null, errorHandler);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains("not allowed"));
		}
		try {
			taskMediator = new TaskMediator<String>(TEST_RESPONSE, null);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains("not allowed"));
		}

		// Response constructor test
		taskMediator = new TaskMediator<String>(TEST_RESPONSE);
		Assert.assertTrue(FieldUtils.readDeclaredField(taskMediator,
				"taskErrorHandler", true) instanceof Slf4jTaskErrorHandler);
	}

}
