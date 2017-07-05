package com.example.oli.scaleuser2;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import static com.example.oli.scaleuser2.BluetoothCommunication.BT_MI_SCALE;

/**
 * Created by Oli on 24.05.2017.
 */

public class UserBtHelp {

    private static UserBtHelp instance;

    private BluetoothCommunication btCom;
    private String btDeviceName;
    private String deviceName;
    private Context context;

    private UserBtHelp(Context con) {
        context = con;
        btCom = null;

    }

    public static UserBtHelp getInstance(Context con) {
        if (instance == null) {
            instance = new UserBtHelp(con);
        }

        return instance;
    }

    public void startSearchingForBluetooth(int btScales, String deviceName, Handler callbackBtHandler) {
        Log.d("HTWScale", "Bluetooth Server started! I am searching for device ...");

        if (btScales == BT_MI_SCALE)
        {
            btCom = new BluetoothMiScale(context);

        }
        btCom.registerCallbackHandler(callbackBtHandler);
        btDeviceName = deviceName;

        btCom.startSearching(btDeviceName);


    }

    public void stopSearchingForBluetooth() {
        if (btCom != null) {
            btCom.stopSearching();
            Log.d("HTWScale", "Bluetooth Server explicit stopped!");
        }
    }

}


