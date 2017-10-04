
package com.example.oli.scaleuser2.core;
import android.bluetooth.BluetoothAdapter;
import android.os.Handler;

/**
 * Diese abstrakte Klasse ist für die Bluetoothkommunikation verantwortlich.
 *
 * @author Oliver Dziedzic, Mamoudou Balde
 *
 * @version 1.0
 */
public abstract class BluetoothCommunication {
    public static final int BT_MI_SCALE = 0;
    //public static final int BT_OPEN_SCALE = 1;

    public static final int BT_RETRIEVE_SCALE_DATA = 0;
    public static final int BT_INIT_PROCESS = 1;
    public static final int BT_CONNECTION_ESTABLISHED = 2;
    public static final int BT_CONNECTION_LOST = 3;
    public static final int BT_NO_DEVICE_FOUND = 4;
    public static final int BT_UNEXPECTED_ERROR = 5;

    protected Handler callbackBtHandler;
    protected BluetoothAdapter btAdapter;

    /**
     * Konstruktor
     */
    public BluetoothCommunication()
    {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * Meldung des Status in der Bluetoothkommunikation.
     *
     * @param cbBtHandler der gemeldete Status
     *
     */
    public void registerCallbackHandler(Handler cbBtHandler) {
        callbackBtHandler = cbBtHandler;
    }

    /**
     * Sucht nach einem Bluetoothsgerät.
     *
     * @param deviceName Name des suchenden Bluetoothsgerätes
     *
     */
    abstract void startSearching(String deviceName);

    /**
     * Suche nach einem Bluetoothsgerät abbrechen.
     */
    abstract void stopSearching();
}