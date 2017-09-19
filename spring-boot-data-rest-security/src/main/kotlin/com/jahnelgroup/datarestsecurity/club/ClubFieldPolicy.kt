package com.jahnelgroup.datarestsecurity.club

import com.fasterxml.jackson.databind.ser.PropertyWriter
import com.jahnelgroup.datarestsecurity.securefield.policy.FieldSecurityPolicy

class ClubFieldPolicy : FieldSecurityPolicy {

    override fun permitAccess(writer: PropertyWriter, target: Any, createdBy: String?, currentPrincipal: String?): Boolean {
        if( target is Club && target.members != null ){

            // not a member but I created the club.
            if( target.createdBy == currentPrincipal )
                return true

            return target.members.firstOrNull {
                it.createdBy == currentPrincipal
            } != null
        }
        return false
    }

}