package org.codelibs.fess.ds.salesforce.api.sobject

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
interface SObject : Searchable {
    val id: String
    val createdDate: Date
    val lastModifiedDate: Date
    val objectType: SObjects
    override fun title(): String = "[${objectType.name}]"
    override fun urlPath(): String = "/$id"
    override fun created(): Date = createdDate
    override fun lastModified(): Date = lastModifiedDate
}