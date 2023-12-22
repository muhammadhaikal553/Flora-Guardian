package com.example.florapedia.data.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("prediction")
	val prediction: String
)
