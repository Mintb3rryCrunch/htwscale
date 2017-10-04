package com.example.oli.scaleuser2.core;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import static com.example.oli.scaleuser2.core.BluetoothCommunication.BT_MI_SCALE;

/**
 * Hilfsklasse für die Bluetoothkommunikation
 *
 * @author Oliver Dziedzic, Mamoudou Balde
 *
 * @version 1.0
 */

public class UserBtHelp {

    private static UserBtHelp instance;

    private BluetoothCommunication btCom;
    private String btDeviceName;
    private String deviceName;
    private Context context;

    /**
     * 1.Konstruktor
     *
     * @param con der Kontext der Klasse
     *
     */
    private UserBtHelp(Context con) {
        context = con;
        btCom = null;

    }

    /**
     * 2.Konstruktor, der eine neue Instanz als Objekt erstellt und zurückgibt.
     *
     * @param con der Kontext der Klasse, in welchem die neue Instanz erstellt werden sollte
     *
     * @return das erstellte Objekt
     *
     */
    public static UserBtHelp getInstance(Context con) {
        if (instance == null) {
            instance = new UserBtHelp(con);
        }

        return instance;
    }

    /**
     * Sucht nach einem Bluetoothsgerät.
     *
     * @param btScales ID des Bluetoothsgerätes
     * @param deviceName Name des gesuchten Bluetoothsgerätes
     * @param callbackBtHandler der gemeldete Status
     *
     */
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

    /**
     * Suche nach einem Bluetoothsgerät abbrechen.
     */
    public void stopSearchingForBluetooth() {
        if (btCom != null) {
            btCom.stopSearching();
            Log.d("HTWScale", "Bluetooth Server explicit stopped!");
        }
    }

}


