package org.codelibs.fess.ds.salesforce.api.sobject

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
interface Searchable {
    val Id: String
    fun title(): String
    fun content(): String
    fun digest(): String = content()
    fun urlPath(): String = "/$Id"
    fun created(): Date? = null
    fun lastModified(): Date? = null
    fun thumbnail(): String? = null
}