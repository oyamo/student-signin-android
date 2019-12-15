package yasis.apps.sasi.OyamNet;

public class WifiState {
    ////Defines if wifi P2P is enabled on the user's phone
   private  boolean wifiIsP2PEnabled;
   private  Manager manager;
   private
    public WifiState() {
    }
    //getter
    public boolean isWifiIsP2PEnabled() {
        return wifiIsP2PEnabled;
    }
    //Getter
    public void setWifiIsP2PEnabled(boolean wifiIsP2PEnabled) {
        this.wifiIsP2PEnabled = wifiIsP2PEnabled;
    }
}
