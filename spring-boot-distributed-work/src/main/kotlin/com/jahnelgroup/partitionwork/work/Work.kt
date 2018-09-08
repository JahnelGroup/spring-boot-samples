package com.jahnelgroup.partitionwork.work

import java.io.Serializable

data class Work  (
    var partition: String,
    var group: String
) : Serializable