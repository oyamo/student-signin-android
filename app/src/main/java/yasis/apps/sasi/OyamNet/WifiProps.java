package yasis.apps.sasi.OyamNet;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;

import static android.os.Looper.getMainLooper;

public class WifiProps {
    ////Defines if wifi P2P is enabled on the user's phone
   private  boolean wifiIsP2PEnabled;
   private IntentFilter intentFilter;

    public WifiP2pManager.Channel getChannel() {
        return channel;
    }

    public void setChannel(WifiP2pManager.Channel channel) {
        this.channel = channel;
    }

    private WifiP2pManager.Channel channel;
    public IntentFilter getIntentFilter() {
        return intentFilter;
    }

    public void setIntentFilter(IntentFilter intentFilter) {
        this.intentFilter = intentFilter;
    }

    public WifiP2pManager getManager() {
        return manager;
    }

    public void setManager(WifiP2pManager manager) {
        this.manager = manager;
    }

    private WifiP2pManager manager;
    public WifiProps(Context ctx) {
        intentFilter = new IntentFilter();
        //Indicates a change in th wifi  p2p status
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        //Indicates a change in the list of available peers
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        //Indicates a change od Wi-fi p2p connectivity has changed
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        //Indicates this device's details changed
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        this.manager =(WifiP2pManager) ctx.getSystemService(Context.WIFI_P2P_SERVICE);
        this.channel = manager != null ? manager.initialize(ctx, getMainLooper(), null) : null;

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
