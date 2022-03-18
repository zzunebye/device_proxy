package com.intechlab.device_proxy

import android.content.Context
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import android.app.Activity
import android.util.Log


class DeviceProxyPlugin(val activity: Activity?) : MethodCallHandler {

  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "com.intechlab/device_proxy")

      channel.setMethodCallHandler(DeviceProxyPlugin(registrar.activity()))
    }
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    when(call.method) {
      "getProxySetting" -> {
        result.success("${getProxySetting(this.activity)}")
      }
      "getProxySettingTest" -> {
        result.success("${getProxySettingTest(this.activity)}")
      }
      else -> {
        result.notImplemented()
      }
    }
  }

  fun getProxySettingTest(context: Context?): String? {
    try {
      Log.d("TAG", "sdk ver " + android.os.Build.VERSION.SDK_INT)
      Log.d("TAG", "sdk ver of ics " + android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)
      if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        Log.d("TAG", "SDK_INT is lower than ICE_CREAM_SANDWICH")
        val proxyAddress = android.net.Proxy.getHost(context)
        Log.d("TAG", "proxyAddress" + proxyAddress)
        if (proxyAddress == null || proxyAddress == "") {
          return proxyAddress
        }
        val port = android.net.Proxy.getPort(context)
        return "$proxyAddress:$port"
      }
      else {
        val address = System.getProperty("http.proxyHost")
        val port = System.getProperty("http.proxyPort")
        Log.d("TAG", "address " + address + ", port " +port)
        if(address.isNotEmpty() && port.isNotEmpty()){
          return "$address:$port"
        }
      }
    } catch (ex: Exception) {
      //ignore
    }

    return ""
  }

  fun getProxySetting(context: Context?): String? {
    try {
      if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        val proxyAddress = android.net.Proxy.getHost(context)
        if (proxyAddress == null || proxyAddress == "") {
          return proxyAddress
        }
        val port = android.net.Proxy.getPort(context)
        return "$proxyAddress:$port"
      }
      else {
        val address = System.getProperty("http.proxyHost")
        val port = System.getProperty("http.proxyPort")

        if(address.isNotEmpty() && port.isNotEmpty()){
          return "$address:$port"
        }
      }
    } catch (ex: Exception) {
      //ignore
    }

    return ""
  }
}
