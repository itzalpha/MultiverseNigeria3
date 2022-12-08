package com.example.multiversenigeria3.model

data class ChannelModel(
    var ChannelImageUri : String ?= null,
    var ChannelDocumentId :  String ? = null,
    var ChannelCategory : String ?= null,
    var ChannelTitle : String ?= null,
    var ChannelAuthor : String ?= null,
    var ChannelAuthorImageUri : String ? = null ,
    var ChannelInformation : String ?= null ,
    var ChannelLink : String ?= null,
    var ChannelAlternativeLink : String ?= null ,
    var ChannelUploadDate : String ?= null

)
{
}