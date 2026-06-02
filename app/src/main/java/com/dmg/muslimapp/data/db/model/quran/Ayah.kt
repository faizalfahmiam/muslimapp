package com.dmg.muslimapp.data.db.model.quran

class Ayah {
    var ID: Int
    var SuraID: Int
    var VerseID: Int
    var AyahText: String
    var Translation: String
    //var ind: String
    //var en: String

    constructor(ID: Int, SuraID: Int, VerseID: Int, AyahText: String, Translation: String) {
        this.ID = ID
        this.SuraID = SuraID
        this.VerseID = VerseID
        this.AyahText = AyahText
        this.Translation = Translation
    }
}