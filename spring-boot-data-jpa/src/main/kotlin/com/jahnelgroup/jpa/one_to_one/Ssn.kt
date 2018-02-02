package com.jahnelgroup.jpa.one_to_one

import com.jahnelgroup.rest.common.data.AbstractEntity
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
class Ssn(
    var ssn : String
) : AbstractEntity(){

    /**
     * Bi-Directional:
     *
     *      Both sides will have a FK mapping. Ssn will have a mapping to Citizen
     *      with a column named citizen_id (the name can be changed).
     */
    @OneToOne

    /**
     * Uni-Directional:
     *
     *      Ssn will not have any FK mapping to Citizen. mappedBy here
     *      indicates that the mapping definition is on the property named
     *      ssn within the Citizen class.
     */
    //@OneToOne(fetch= FetchType.LAZY, mappedBy="ssn")

    var citizen : Citizen? = null

}