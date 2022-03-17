class ProxyConfig {
  late bool isEnable;
  late String host;
  late String port;
  late String proxyUrl;

  ProxyConfig(String proxyUrl) {
    _init(proxyUrl);
  }

  void _init(String proxyUrl){
    print("_init: $proxyUrl");
    isEnable = proxyUrl.isNotEmpty;

    if(isEnable) {
      this.proxyUrl = proxyUrl;
      final proxyRaw = proxyUrl.split(':');
      this.host = proxyRaw[0];
      this.port = proxyRaw[1];
    }
  }
}