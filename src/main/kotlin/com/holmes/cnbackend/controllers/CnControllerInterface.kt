package com.holmes.cnbackend.controllers

interface CnControllerInterface {
    fun destroyTable()
    fun createTable()
    fun executeAllInserts()
}