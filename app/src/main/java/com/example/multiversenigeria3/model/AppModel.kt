package com.example.multiversenigeria3.model

data class AppModel(
    var AppImageUri : String ?= null,
    var AppDocumentId :  String ? = null,
    var AppName : String ?= null,
    var AppVerseRate : String ?= null,
    var AppInformation : String ?= null,
    var AppUploadDate : String ?= null ,
    var AppCategory : String ?=null ,
    var AppPlayStoreLink : String ?= null ,
    var AppAlternativeLink  : String ?= null
)
{
}