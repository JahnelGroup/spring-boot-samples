package com.jahnelgroup.jpa

import com.jahnelgroup.jpa.one_to_one.Citizen
import com.jahnelgroup.jpa.one_to_one.CitizenRepo
import com.jahnelgroup.jpa.one_to_one.Ssn
import com.jahnelgroup.jpa.one_to_one.SsnRepo
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class UserTests : AbstractTest() {

    @Autowired lateinit var citizenRepo: CitizenRepo
    @Autowired lateinit var ssnRepo: SsnRepo

    @Test
    fun `cascading save of citizen to ssn`(){
        var citizen = Citizen("Steven")
        var ssn = Ssn("123-45-6789")

        citizen.ssn = ssn
        ssn.citizen = citizen

        // Saving a Citizen will cascade the save to Ssn because we've configured
        // the annotation's Citizen to do so. Also since we've established the relationship
        // both ways before saving then Ssn will have a reference back to Citizen.
        var citizenId = citizenRepo.save(citizen).id

        var savedCitizen = citizenRepo.findOne(citizenId)
        assertThat(savedCitizen).isNotNull()
        assertThat(savedCitizen.id).isNotNull()
        assertThat(savedCitizen.ssn).isNotNull()
        assertThat(savedCitizen.ssn!!.id).isNotNull()

        var savedSsn = ssnRepo.findOne(savedCitizen.ssn!!.id)
        assertThat(savedSsn).isNotNull()
        assertThat(savedSsn.id).isNotNull()
        assertThat(savedSsn.citizen).isNotNull()
        assertThat(savedSsn.citizen!!.id).isNotNull()
    }

    @Test
    fun `cascading save of citizen to ssn without a back-reference remains null`(){
        var citizen = Citizen("Steven")
        var ssn = Ssn("123-45-6789")

        citizen.ssn = ssn

        // Warning: If you don't set directions both ways then
        // JPA will NOT auto-detect nor assign it for you.
        //
        // ssn.citizen = citizen

        var citizenId = citizenRepo.save(citizen).id

        var savedCitizen = citizenRepo.findOne(citizenId)
        assertThat(savedCitizen).isNotNull()
        assertThat(savedCitizen.id).isNotNull()
        assertThat(savedCitizen.ssn).isNotNull()
        assertThat(savedCitizen.ssn!!.id).isNotNull()

        var savedSsn = ssnRepo.findOne(savedCitizen.ssn!!.id)
        assertThat(savedSsn).isNotNull()
        assertThat(savedSsn.id).isNotNull()
        // This will remain null! JPA will not auto-detect this for you.
        assertThat(savedSsn.citizen).isNull() // Warning Null !
    }

    @Test
    fun `cascading save of ssn to citizen not configured`(){
        var citizen = Citizen("Steven")
        var ssn = Ssn("123-45-6789")

        citizen.ssn = ssn
        ssn.citizen = citizen

        // Saving a Ssn will NOT cascade the save to Citizen because we've never configured
        // the annotation's in Ssn to do so. As a result, since the mapping is Bi-Directional
        // we must manually save both of the Entitiy's or else we'll get Hibernate Exceptions.
        var ssnId = ssnRepo.save(ssn).id

        var savedSsn = ssnRepo.findOne(ssnId)
        assertThat(savedSsn).isNotNull()
        assertThat(savedSsn.id).isNotNull()
        assertThat(savedSsn.citizen).isNotNull()
        // JPA did not save the Citizen automatically for us
        assertThat(savedSsn.citizen!!.id).isNull() // Warning NULL !

        // We must save the Citizen manually because we didn't configure Cascading, or
        // else we would get a Hibernate Exception like this:
        //
        // org.hibernate.TransientPropertyValueException: object references an unsaved transient instance -
        //  save the transient instance before flushing :
        //  com.jahnelgroup.jpa.one_to_one.bidirectional.Ssn.citizen -> com.jahnelgroup.jpa.one_to_one.bidirectional.Citizen
        var citizenId = citizenRepo.save(citizen).id

        var savedCitizen = citizenRepo.findOne(citizenId)
        assertThat(savedCitizen).isNotNull()
        assertThat(savedCitizen.id).isNotNull()
        assertThat(savedCitizen.ssn).isNotNull()
        assertThat(savedCitizen.ssn!!.id).isNotNull()
    }

}