package com.jahnelgroup.acl.controller.account

import com.jahnelgroup.acl.domain.account.Account
import com.jahnelgroup.acl.domain.account.AccountRepo
import com.jahnelgroup.acl.domain.user.UserRepo
import com.jahnelgroup.acl.service.account.AccountService
import com.jahnelgroup.acl.service.context.UserContextService
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class AccountController(
        var accountService: AccountService,
        var userContextService: UserContextService,
        var accountRepo: AccountRepo,
        var userRepo: UserRepo
) {

    @ModelAttribute fun useRepo() = userRepo
    @ModelAttribute fun accountRepo() = accountRepo
    @ModelAttribute fun accountService() = accountService

    @GetMapping("/")
    fun get(model: Model): String{
        model.addAttribute("createAccountForm", CreateAccountForm())
        return "home"
    }

    @PostMapping("/")
    fun post(model: Model, @ModelAttribute(value="createAccountForm") createAccountForm: CreateAccountForm): String{

        var account = Account().apply {
            name = createAccountForm.name
            primaryOwner = userRepo.findByUsername(createAccountForm.primaryOwner!!).get()
            jointOwners = createAccountForm.jointOwners.asSequence().map { userRepo.findByUsername(it).get() }.toSet()
            readOnly = createAccountForm.readOnly.asSequence().map { userRepo.findByUsername(it).get() }.toSet()
        }

        userContextService.impersonateUser(account.primaryOwner!!) // pretend to be the account owner.
        accountService.save(account)

        model.addAttribute("accountService", accountService)
        return "home"
    }

    @ResponseBody
    @GetMapping("/accounts/{account}/read")
    @PostAuthorize("hasPermission(#account, 'read')")
    fun read(@PathVariable account: Account) = account

    @ResponseBody
    @GetMapping("/accounts/{account}/write")
    @PostAuthorize("hasPermission(#account, 'write')")
    fun write(@PathVariable account: Account) = account

    @ResponseBody
    @GetMapping("/accounts/{account}/admin")
    @PostAuthorize("hasPermission(#account, 'administration')")
    fun admin(@PathVariable account: Account) = account

}