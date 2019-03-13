package org.codelibs.fess.ds.salesforce.api.sobject

import java.util.*

data class SUser(
        override val Id: String,
        val Username: String,
        val Name: String,
        val CompanyName: String?,
        val Division: String?,
        val Department: String?,
        val Title: String?,
        val Email: String,
        val Phone: String?,
        val Fax: String?,
        val CreatedDate: Date,
        val LastModifiedDate: Date,
        val AboutMe: String?,
        val SmallPhotoUrl: String?
) : Searchable {
    override fun title(): String = "$Name ($Username)"
    override fun content(): String = listOfNotNull(CompanyName, Division, Department, Title, Email, Phone, Fax, AboutMe).joinToString("\n")
    override fun created(): Date = CreatedDate
    override fun lastModified(): Date = LastModifiedDate
    override fun thumbnail(): String? = SmallPhotoUrl
}