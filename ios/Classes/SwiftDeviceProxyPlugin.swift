import Flutter
import UIKit

public class SwiftDeviceProxyPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "com.intechlab/device_proxy", binaryMessenger: registrar.messenger())
    let instance = SwiftDeviceProxyPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
        case "getProxySetting":
            result(getProxyUrl())
            break
        case "getProxySettingTest":
            result(getProxyUrlTest())
            break
        default:
            result(FlutterMethodNotImplemented)
            break
    }
  }

  func getProxyUrlTest() -> String {
          guard let proxySettings = CFNetworkCopySystemProxySettings()?.takeUnretainedValue(),
              let url = URL(string: "https://www.bing.com/") else {
                  return "error1:"
          }
          let proxies = CFNetworkCopyProxiesForURL((url as CFURL), proxySettings).takeUnretainedValue() as NSArray
          guard let settings = proxies.firstObject as? NSDictionary,
              let _ = settings.object(forKey: (kCFProxyTypeKey as String)) as? String else {
                  return "error2:"
          }

          if let hostName = settings.object(forKey: (kCFProxyHostNameKey as String)), let port = settings.object(forKey: (kCFProxyPortNumberKey as String)) {
              return "\(settings) .. \(hostName):\(port)"
          }

          return "error3: proxies - \(proxies) settings - \(settings)"
      }
    
    func getProxyUrl() -> String {
        guard let proxySettings = CFNetworkCopySystemProxySettings()?.takeUnretainedValue(),
            let url = URL(string: "https://www.bing.com/") else {
                return ""
        }
        let proxies = CFNetworkCopyProxiesForURL((url as CFURL), proxySettings).takeUnretainedValue() as NSArray
        guard let settings = proxies.firstObject as? NSDictionary,
            let _ = settings.object(forKey: (kCFProxyTypeKey as String)) as? String else {
                return ""
        }
        
        if let hostName = settings.object(forKey: (kCFProxyHostNameKey as String)), let port = settings.object(forKey: (kCFProxyPortNumberKey as String)) {
            return "\(hostName):\(port)"
        }
        
        return ""
    }
}
