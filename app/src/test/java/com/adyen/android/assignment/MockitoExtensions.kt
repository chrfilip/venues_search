package com.adyen.android.assignment

import org.mockito.Mockito

inline fun <reified T> anyNonNull(): T = Mockito.any(T::class.java)
