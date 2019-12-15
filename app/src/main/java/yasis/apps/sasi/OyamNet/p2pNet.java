package yasis.apps.sasi.OyamNet;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;

import java.util.HashMap;
import java.util.Map;

public class p2pNet extends NetBroadCastListener implements Callbacks {
    private Context context;
    private Callbacks callBacks;
    WifiProps wifiProps;
    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel channel;
    public p2pNet(Context ctx, IntentFilter intentFilter){
        this.context =ctx;
        this.wifiProps = new WifiProps(ctx);
        ctx.registerReceiver(this,intentFilter);
        this.wifiP2pManager = this.wifiProps.getManager();
        this.channel = this.wifiProps.getChannel();
    }
    @Override
    public void onPeersChanged() {

    }

    @Override
    public void onP2PConnectionChanged() {

    }

    @Override
    public void onP2PStateChanged(WifiProps wifiProps) {

    }

    @Override
    public void onThisDeviceChanged() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            //Check if P2P is enabled and show necessary action

            boolean isP2PEnabled = WifiP2pManager.WIFI_P2P_STATE_ENABLED == intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
            //Initialise the state here
            wifiProps.setWifiIsP2PEnabled(isP2PEnabled);
            callBacks.onP2PStateChanged(wifiProps);
        }
        if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
            callBacks.onPeersChanged();
        }
        if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            callBacks.onP2PConnectionChanged();
        }
        if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
            callBacks.onThisDeviceChanged();
        }
    }
    public void release(){
        context.unregisterReceiver(this);
    }
    public void startRegistration(){
        //Create a new map Containinginformation about my service
        Map record = new HashMap();
        record.put("listenport",String.valueOf(8392));
        record.put("buddyname","John Doe"+Math.random()*9000);
        record.put("available","visible");

        //Service INfomation
        WifiP2pDnsSdServiceInfo serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance("_test","_presence._tcp",record);
        wifiP2pManager.addLocalService(channel, serviceInfo,
                new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        //Alert the user if Broadcasting myself suceeded
                    }

                    @Override
                    public void onFailure(int reason) {

                    }
                });
        ///Register the discover service
        final HashMap<String,Peer> buddies = new HashMap<>();
        WifiP2pManager.DnsSdTxtRecordListener txtRecordListener = new WifiP2pManager.DnsSdTxtRecordListener() {
            @Override
            public void onDnsSdTxtRecordAvailable(String fullDomainName, Map<String, String> txtRecordMap, WifiP2pDevice srcDevice) {
                Peer peer = new Peer();
                peer.setAddress(srcDevice.deviceAddress);
                peer.setDeviceName(srcDevice.deviceName);
                peer.setDomainName(fullDomainName);
                buddies.put(srcDevice.deviceAddress,peer);
            }
        };
        WifiP2pManager.DnsSdServiceResponseListener responseListener = new WifiP2pManager.DnsSdServiceResponseListener() {
            @Override
            public void onDnsSdServiceAvailable(String instanceName, String registrationType, WifiP2pDevice srcDevice) {
                srcDevice.deviceName = (buddies.containsKey(srcDevice.deviceAddress)? ((Peer)buddies.get(srcDevice.deviceAddress)).getAddress()
                :srcDevice.deviceName);
            }
        };
        wifiP2pManager.setDnsSdResponseListeners(channel,responseListener,txtRecordListener);
        WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        wifiP2pManager.addLocalService(channel, serviceRequest,
                new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int reason) {

                    }
                });
        wifiP2pManager.discoverServices(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }
}
