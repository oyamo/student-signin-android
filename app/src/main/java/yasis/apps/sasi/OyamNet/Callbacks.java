package yasis.apps.sasi.OyamNet;

public interface Callbacks {
    void onPeersChanged();
    void onP2PConnectionChanged();
    void onP2PStateChanged(WifiProps wifiProps);
    void onThisDeviceChanged();
}
