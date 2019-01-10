package com.jahnelgroup.acl.service.account

import com.jahnelgroup.acl.domain.account.Account
import com.jahnelgroup.acl.domain.account.AccountRepo
import com.jahnelgroup.acl.domain.user.UserRepo
import com.jahnelgroup.acl.service.context.UserContextService
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.acls.domain.*
import org.springframework.security.acls.model.*
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AccountService(
        private var accountRepo: AccountRepo,
        private var userRepo: UserRepo,
        private var aclService: MutableAclService
) {

    private var permissionFactory: PermissionFactory = DefaultPermissionFactory()

    //@PostFilter("principal.username == filterObject.createdBy")
    fun findAll(): Iterable<Account> = accountRepo.findAll()

    @Transactional
    fun save(account: Account): Account {
        var e = accountRepo.save(account)

//        // Object Instance
//        val oi = ObjectIdentityImpl(e::class.java, e.id)
//
//        // ACL
//        var acl: MutableAcl? = null
//        try {
//            acl = aclService.readAclById(oi) as MutableAcl
//        } catch (nfe: NotFoundException) {
//            acl = aclService.createAcl(oi)
//        }
//
//        //
//        // ACE: Primary Owner
//        //
//        val sid = PrincipalSid(account.primaryOwner!!.username)
//        acl!!.let {
//            it.insertAce(it.entries.size, BasePermission.READ, sid, true)
//            it.insertAce(it.entries.size, BasePermission.WRITE, sid, true)
//            it.insertAce(it.entries.size, BasePermission.CREATE, sid, true)
//            it.insertAce(it.entries.size, BasePermission.DELETE, sid, true)
//            it.insertAce(it.entries.size, BasePermission.ADMINISTRATION, sid, true)
//        }
//
//        //
//        // ACE: Joint Owners
//        //
//        account.jointOwners.forEach {
//            val sid = PrincipalSid(it.username)
//
//            acl.let {
//                it.insertAce(it.entries.size, BasePermission.READ, sid, true)
//                it.insertAce(it.entries.size, BasePermission.WRITE, sid, true)
//            }
//        }
//
//        //
//        // ACE: Read Only
//        //
//        account.readOnly.forEach {
//            val sid = PrincipalSid(it.username)
//            acl.let {
//                it.insertAce(it.entries.size, BasePermission.READ, sid, true)
//            }
//        }
//
//        aclService.updateAcl(acl)

        return e
    }

    fun getUsersByPermission(id: Long, permission: String): Set<String> {
        var users = mutableSetOf<String>()
        val oi = ObjectIdentityImpl(Account::class.java, id)

        userRepo.findAll().forEach { user ->
            var acl = aclService.readAclById(oi)
            if( acl.entries.any {
                        user.username == (it.sid as PrincipalSid).principal &&
                        it.permission.mask == permissionFactory.buildFromName(permission.toUpperCase()).mask
                    } ){
                users.add(user.username)
            }
        }
        return users
    }

}

