package yasis.apps.sasi.OyamNet;

import android.net.wifi.p2p.WifiP2pManager;

public class DiscoverPeerService {
    WifiProps wifiProps;
    DiscoverPeerService(WifiProps props){

    }

    void scan(){
        wifiProps.getManager().discoverPeers(wifiProps.getChannel(),
                new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int reason) {

                    }
                });
    }


}
