package org.openlab.security

class AppAccessToken {

    String applicationName
    String token
    Date dateCreated
    Date lastUpdated
    boolean expires
    Date expiryDate

    def beforeValidate(){
        token = UUID.randomUUID()
    }

    static constraints = {
        applicationName()
        expires()
        expiryDate nullable: true
        token editable: false
    }
}
