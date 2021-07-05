package com.ajit.pingplacepicker.inject

import com.ajit.pingplacepicker.viewmodel.PlaceConfirmDialogViewModel
import com.ajit.pingplacepicker.viewmodel.PlacePickerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { PlacePickerViewModel(get()) }

    viewModel { PlaceConfirmDialogViewModel(get()) }

}