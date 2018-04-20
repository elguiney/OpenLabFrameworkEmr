package org.openlab.webservice

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.collections.Predicate
import org.openlab.main.DataObject
import org.openlab.security.AppAccessToken

class RestfulController {

    def grailsApplication
    def searchableService

    def search(){

        def responseData

        if(!params.token) {
            responseData = [
                    'total': 0,
                    'results': null,
                    'data': null,
                    'status': "Access token missing"
            ]
        }
        else if(!isAccessTokenValid()){
            responseData = [
                    'total': 0,
                    'result': null,
                    'data': null,
                    'status': "Access token invalid"
            ]
        }
        else {
            def resultMap
            def domainClass

            if (params.type)
                domainClass = grailsApplication.getArtefactByLogicalPropertyName("Domain", params.type)?.getClazz()
            else
                domainClass = searchableService //searches all domain classes
            resultMap = domainClass.search(params.query, params, escape: true)

            responseData = [
                    'total': resultMap.total,
                    'results': resultMap.results.collect{[id : it.id, label: it.toString()]},
                    'data': resultMap.hits.datas,
                    'status': resultMap ? "OK" : "Nothing found"
            ]
        }

        render responseData as JSON
    }

    def get(){
        def responseData

        if(!params.token) {
            responseData = [
                    'result': null,
                    'status': "Access token missing"
            ]
        }
        else if(!isAccessTokenValid()){
            responseData = [
                    'result': null,
                    'status': "Access token invalid"
            ]
        }
        else {
            def type = DataObject.get(params.id).getType()
            def result = grailsApplication.getArtefactByLogicalPropertyName("Domain", type)?.getClazz().get(params.id)
            responseData = [
                    'result': result,
                    'status': result ? "OK" : "Nothing found"
            ]
        }
        render responseData as JSON
    }

    def isAccessTokenValid = {
        def appAccessTokenInstance = AppAccessToken.findByToken(params.token)
        if(appAccessTokenInstance){
            if(appAccessTokenInstance.expires) return(appAccessTokenInstance.expiryDate.after(new Date()))
            else return(true)
        }
        else return(false)
    }
}
