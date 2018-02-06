/*
 * Copyright 2018 Behsa Corporation
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

package com.behsa.ganjex.integration.hooksExecution;

import com.behsa.ganjex.bootstrap.Bootstrap;
import com.behsa.ganjex.integration.TestUtil;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static com.behsa.ganjex.integration.TestUtil.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * @author Esa Hekmatizadeh
 */
@Test(sequential = true)
public class HooksExecutionIT {

	@Test
	public void testJustOneObjectWithDifferentHooks() throws IOException, InterruptedException,
					ClassNotFoundException,
					NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		TestUtil.clean();
		deployLib(TEST_PATH + "hooksExecution/libWithDifferentHooks/", "simple-lib");
		Bootstrap.main(new String[]{TEST_CONFIG_PATH});
		waitToBootstrap();
		invokeStaticMethod("com.behsa.LibWithDifferentHooks", "clean", Void.class,
						new Class[0]);
		deployService(TEST_PATH + "hooksExecution/simpleService/", "simple-service");
		Thread.sleep(TIMEOUT);
		int runTime = invokeStaticMethod("com.behsa.LibWithDifferentHooks",
						"getRunTime", Integer.class, new Class<?>[0]);
		assertEquals(1, runTime);
		Bootstrap.destroy();
	}

	@Test
	public void testEveryHooksRunOnce() throws IOException, InterruptedException,
					ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		TestUtil.clean();
		deployLib(TEST_PATH + "hooksExecution/libWithDifferentHooks/", "simple-lib");
		Bootstrap.main(new String[]{TEST_CONFIG_PATH});
		waitToBootstrap();
		invokeStaticMethod("com.behsa.LibWithDifferentHooks", "clean", Void.class,
						new Class[0]);
		int shutdownHookRunTime = invokeStaticMethod("com.behsa.LibWithDifferentHooks",
						"getShutdownHookRunTime", Integer.class, new Class<?>[0]);
		assertEquals(0, shutdownHookRunTime);
		deployService(TEST_PATH + "hooksExecution/simpleService/", "simple-service");
		Thread.sleep(TIMEOUT);
		int startHookRunTime = invokeStaticMethod("com.behsa.LibWithDifferentHooks",
						"getStartHookRunTime", Integer.class, new Class<?>[0]);
		int startHook2RunTime = invokeStaticMethod("com.behsa.LibWithDifferentHooks",
						"getStartHook2RunTime", Integer.class, new Class<?>[0]);
		assertEquals(startHookRunTime, 1);
		assertEquals(startHook2RunTime, 1);
		unDeployService("simple-service");
		Thread.sleep(TIMEOUT);
		shutdownHookRunTime = invokeStaticMethod("com.behsa.LibWithDifferentHooks",
						"getShutdownHookRunTime", Integer.class, new Class<?>[0]);
		assertNull(Bootstrap.lifecycleManagement().findContext("simple-service"));
		assertEquals(1, shutdownHookRunTime);
		Bootstrap.destroy();
	}
}
