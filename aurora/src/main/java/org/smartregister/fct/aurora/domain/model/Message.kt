package org.smartregister.fct.aurora.domain.model

sealed class Message(val text: String) {

    class Info(text: String) : Message(text)
    class Error(text: String) : Message(text)
}