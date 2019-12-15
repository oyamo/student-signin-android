package yasis.apps.sasi.OyamNet;

public interface NetCallBacks {
    void onPeersChanged();
    void onP2PConnectionChanged();
    void onP2PStateChanged(WifiState wifiState);
    void onThisDeviceChanged();
}
