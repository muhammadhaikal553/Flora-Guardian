package com.example.florapedia.data.response

import com.google.gson.annotations.SerializedName

data class PlantDetailResponse(

	@field:SerializedName("data")
	val data: List<Data>,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("attachment")
	val attachment: String,

	@field:SerializedName("plantFamily")
	val plantFamily: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("wateringFrequence")
	val wateringFrequence: String,

	@field:SerializedName("id")
	val id: Long,

	@field:SerializedName("waterLevel")
	val waterLevel: String
)
