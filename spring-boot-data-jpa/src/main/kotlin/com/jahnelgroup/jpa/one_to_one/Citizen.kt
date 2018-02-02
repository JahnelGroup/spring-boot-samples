package com.jahnelgroup.jpa.one_to_one

import com.jahnelgroup.rest.common.data.AbstractEntity
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
class Citizen(
    var name : String
) : AbstractEntity() {

    /**
     * Bi-Directional:
     *
     *      Both sides will have a FK mapping. Citizen will have a mapping to Ssn
     *      with a column named ssn_id (the name can be changed).
     */
    @OneToOne(cascade = arrayOf(CascadeType.ALL))

    /**
     * Uni-Directional:
     *
     *      Citizen will have a FK mapping to Ssn with a column named ssn_id
     */
    //@OneToOne(optional = false, cascade = arrayOf(CascadeType.ALL))
    //@JoinColumn(name="ssn_id")

    var ssn: Ssn? = null

}