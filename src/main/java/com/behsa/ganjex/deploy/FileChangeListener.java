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

package com.behsa.ganjex.deploy;

import java.io.File;

/**
 * this listener is responsible to handle the change in a directory which {@link JarWatcher} watch
 * it take action when a file modified or removed. modified file include adding new files
 *
 * @author Esa Hekmatizadeh
 * @version 1.0
 */
public interface FileChangeListener {

	/**
	 * listener which executed when a file added into or modified in a specific path
	 *
	 * @param file the new file
	 */
	void fileAdd(File file);

	/**
	 * listener which executed when a file removed in a specific path
	 *
	 * @param file file which be removed
	 */
	void fileRemoved(File file);
}
