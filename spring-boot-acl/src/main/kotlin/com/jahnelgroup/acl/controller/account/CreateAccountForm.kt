package com.jahnelgroup.acl.controller.account

data class CreateAccountForm(
        var name: String? = null,
        var primaryOwner: String?  = null,
        var jointOwners: List<String> = emptyList(),
        var readOnly: List<String> = emptyList()
)