package com.jahnelgroup.integration.model

import lombok.Data
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "")
@Data
class WordProcessingStats {

    @Id
    var id: String? = null

    var wordCount: Int = 0
    var capitalCount: Long = 0
    var vowelCount: Long = 0

}