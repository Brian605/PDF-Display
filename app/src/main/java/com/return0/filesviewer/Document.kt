package com.return0.filesviewer

import java.io.Serializable

//Dont forget to add this class to proguard
class Document : Serializable {
    var documentId:String? = null
    var documentUrl: String? = null
    var documentName:String?=null

    constructor(documentId:String, documentUrl: String?,documentName:String?) {
        this.documentId = documentId
        this.documentUrl = documentUrl
        this.documentName=documentName
    }

    constructor() {}
}