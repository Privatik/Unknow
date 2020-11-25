package com.io.unknow.model

import android.os.Parcelable
import java.io.Serializable

data class Search(
    var sex: Int = 2,
    var ageStart: Int? = null,
    var ageEnd: Int? = null,
  var heightStart: Int? = null,
    var heightEnd: Int? = null,
  var weightStart: Int? = null,
  var weightEnd: Int? = null,
  var local: String? = null
):Serializable