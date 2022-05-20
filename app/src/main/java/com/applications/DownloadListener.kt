package com.applications

interface DownloadListener {
    fun onSuccess(path: String)
    fun onFailure(error: String)
}