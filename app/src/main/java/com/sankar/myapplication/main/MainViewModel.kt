package com.sankar.myapplication.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sankar.myapplication.util.DispatcherProvider
import com.sankar.myapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class CurrencyEvent {
        class Success(val resultText: String): CurrencyEvent()
        class Failure(val errorText: String): CurrencyEvent()
        object Loading: CurrencyEvent()
        object Empty: CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(
        amount: String,
        fromCurrency: String,
        toCurrency: String
    ) {
        val amountValue = amount.toFloatOrNull()

        if(amountValue == null) {
            _conversion.value = CurrencyEvent.Failure("Not a valid amount!")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading
            when(val response = repository.getRates(fromCurrency, toCurrency, amount)) {
                is Resource.Error -> _conversion.value = CurrencyEvent.Failure(response.message!!)
                is Resource.Success -> {
                    val finalAmount = response.data!!.result
                    //val rate = getRateForCurrency(fromCurrency, rates)

                    if(response.data!!.success) {
                        val convertedAmount = round(finalAmount * 100)/100
                        _conversion.value = CurrencyEvent.Success(
                            "$amount $fromCurrency = $convertedAmount $toCurrency"
                        )
                    } else {
                        _conversion.value = CurrencyEvent.Failure("Unexpected error!")
                    }
                }
            }
        }
    }
}