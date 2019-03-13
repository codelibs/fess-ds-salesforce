package org.codelibs.fess.ds.salesforce.api.sobject

import kotlin.reflect.KClass

enum class SObjects(val dataClass: KClass<out Searchable>) {
    User(SUser::class)
}