/*
 * Copyright 2012-2019 CodeLibs Project and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.codelibs.fess.ds.salesforce

import org.codelibs.fess.ds.AbstractDataStore
import org.codelibs.fess.ds.callback.IndexUpdateCallback
import org.codelibs.fess.es.config.exentity.DataConfig
import org.slf4j.LoggerFactory

class SalesforceDataStore : AbstractDataStore() {
    companion object {
        private val logger = LoggerFactory.getLogger(SalesforceDataStore::class.java)
    }

    override fun getName(): String = "Salesforce"

    override fun storeData(dataConfig: DataConfig, callback: IndexUpdateCallback, paramMap: Map<String, String>,
                           scriptMap: Map<String, String>, defaultDataMap: Map<String, Any>) {
    }
}
