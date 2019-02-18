package org.codelibs.fess.ds.salesforce

import org.codelibs.fess.exception.DataStoreException

class SalesforceDataStoreException : DataStoreException {
    constructor(message: String, throwable: Throwable) : super(message, throwable)
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)
}