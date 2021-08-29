package com.holmes.cnbackend.config

const val SERV_PORT = 23567

const val HOST_PROP = "HEROKU_POSTGRESQL_KOTLIN_HOST"
const val USER_PROP = "HEROKU_POSTGRESQL_KOTLIN_USER"
const val PASS_PROP = "HEROKU_POSTGRESQL_KOTLIN_PASS"
const val OPER_PROP = "HEROKU_POSTGRESQL_KOTLIN_OPERATION"

const val DRIVER_PROP = "org.postgresql.Driver"
const val PARAMS_PROP = "?sslmode=require&reWriteBatchedInserts=true"

const val PROD_OPER = "prod"
const val TERM_OPER = "terminate"
const val NOIN_OPER = "run"

const val ROUTE_VOCAB = "vocab"
const val ROUTE_CHARS = "chars"

const val DB_VOCAB = "CnVocabT"