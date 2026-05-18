package com.cellfootball.app

import android.content.Context

object AssetTextLoader {
    fun load(context: Context, assetName: String): String {
        return runCatching {
            context.assets.open(assetName).bufferedReader().use { it.readText() }
        }.getOrElse {
            "Не удалось загрузить правила ($assetName)."
        }
    }
}