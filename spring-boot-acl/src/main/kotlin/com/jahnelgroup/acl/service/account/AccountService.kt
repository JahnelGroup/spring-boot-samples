package com.jahnelgroup.acl.service.account

import com.jahnelgroup.acl.domain.account.Account
import com.jahnelgroup.acl.domain.account.AccountRepo
import com.jahnelgroup.acl.domain.user.UserRepo
import com.jahnelgroup.acl.service.context.UserContextService
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.access.prepost.PostFilter
import org.springframework.security.acls.domain.AclFormattingUtils
import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.acls.domain.ObjectIdentityImpl
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.model.MutableAcl
import org.springframework.security.acls.model.MutableAclService
import org.springframework.security.acls.model.NotFoundException
import org.springframework.security.acls.model.Permission
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AccountService(
        private var accountRepo: AccountRepo,
        private var userRepo: UserRepo,
        private var aclService: MutableAclService,
        private var permissionEvaluator: PermissionEvaluator,
        private var userContextService: UserContextService
) {

    //@PostFilter("principal.username == filterObject.createdBy")
    fun findAll(): Iterable<Account> = accountRepo.findAll()

    @Transactional
    fun save(account: Account): Account {
        var e = accountRepo.save(account)

        // Object Instance
        val oi = ObjectIdentityImpl(e::class.java, e.id)

        // ACL
        var acl: MutableAcl? = null
        try {
            acl = aclService.readAclById(oi) as MutableAcl
        } catch (nfe: NotFoundException) {
            acl = aclService.createAcl(oi)
        }

        //
        // ACE: Primary Owner
        //
        val sid = PrincipalSid(account.primaryOwner!!.username)
        acl!!.let {
            it.insertAce(it.entries.size, BasePermission.ADMINISTRATION, sid, true)
        }

        //
        // ACE: Joint Owners
        //
        account.jointOwners.forEach {
            val sid = PrincipalSid(it.username)

            acl.let {
                it.insertAce(it.entries.size, object : Permission {
                    override fun getPattern() =  AclFormattingUtils.printBinary(mask, '*')
                    override fun getMask() = BasePermission.WRITE.mask or BasePermission.CREATE.mask
                }, sid, true)
            }
        }

        //
        // ACE: Joint Owners
        //
        account.readOnly.forEach {
            val sid = PrincipalSid(it.username)
            acl.let {
                it.insertAce(it.entries.size, BasePermission.READ, sid, true)
            }
        }

        aclService.updateAcl(acl)

        return e
    }

    fun getReadAcl(id: Long, permission: String): List<String> {
        var account = accountRepo.findById(id)

        val oi = ObjectIdentityImpl(Account::class.java, id)
        var acl = aclService.readAclById(oi)

        return acl.entries.asSequence().map {
            var sidname: String = "FALSE"

            var sid = it.sid
            if (sid is PrincipalSid){
                sidname = sid.principal
                var siduser = userRepo.findByUsername(sidname)

            }

            userContextService.impersonateUser(userRepo.findByUsername(sidname).get())
            if( !permissionEvaluator.hasPermission(SecurityContextHolder.getContext().authentication, account.get(), permission))
                "NO"
            else
                sidname
        }.filter { it != "NO" }.toList()
    }

}

