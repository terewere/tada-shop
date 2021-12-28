package com.kasa.auth

/**
 * Data validation state of the form.
 */
data class LoginFormState(val numberError: Int? = null,
                          val nameError: Int? = null,
                          val lastNameError: Int? = null,
                          val isDataValid: Boolean = false)
