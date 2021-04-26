package kz.seasky.tms.utils

const val MIN_USERNAME_LENGTH = 4
const val MAX_USERNAME_LENGTH = 32
const val MIN_PASSWORD_LENGTH = 8
const val MAX_PASSWORD_LENGTH = 32

const val JWT_NAME_STANDARD = "jwt-token"
const val JWT_CLAIM_ID = "jwt-id"

const val FILE_ROOT_DIR = "./.files"

/** Equals 26214400 bytes or 25MiB */
const val FILE_DEFAULT_SIZE: Long = 25 * 1024 * 1024

const val SESSION_ROOT_DIR = "./.sessions"
const val COOKIE_STATISTIC_NAME = "cookie-statistic-name"