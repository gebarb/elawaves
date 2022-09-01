package elawaves.Carrier;


import android.app.Activity;
import android.content.Context;

import org.elastos.carrier.Carrier;
import org.elastos.carrier.exceptions.CarrierException;

import java.util.HashMap;

import elawaves.Carrier.Friends.FriendManager;
import elawaves.Carrier.Messages.MessageManager;
import elawaves.MainActivity;

public class CarrierImplementation {

    private static CarrierImplementation instance;

    static boolean carrier_ready = false;

    private static Carrier carrier_instance;

    public static CarrierImplementation getInstance(Context context) throws CarrierException {
        if(instance == null)
            instance = new CarrierImplementation(context);

        return instance;
    }

    // Establishes connection to and function of app with Carrier Network
    private CarrierImplementation(Context context) throws CarrierException {
        Options options = new Options(context.getFilesDir().getParent());

        Carrier.initializeInstance(options,new CarrierHandler());

        Carrier carrier = Carrier.getInstance();
            carrier.start(0);

        MessageManager.setup(context);

        carrier_instance = carrier;
    }

    private static MainActivity activity;
    protected static void setReady(){
        carrier_ready = true;
        activity.carrierReady();
    }

    public static void onReady(MainActivity mainActivity){
        activity = mainActivity;
    }

    public static Carrier getCarrier(){
        return carrier_instance;
    }

    public static boolean isReady(){
        return carrier_ready;
    }
}
