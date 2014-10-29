package com.planetgeekz;

import java.util.List;

import orbotix.robot.base.Robot;
import orbotix.robot.base.RobotProvider;
import orbotix.sphero.ConnectionListener;
import orbotix.sphero.DiscoveryListener;
import orbotix.sphero.Sphero;
import orbotix.view.connection.SpheroConnectionView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.planetgeekz.socialmedianotificationlistener.R;

public class MainActivity extends Activity{

	protected static final String TAG="Planetgeekz-Receiver";

    protected SMReceiver mReceiver =null;

	protected Sphero mRobot;
	
	protected MainActivity main;
	
    private SpheroConnectionView mSpheroConnectionView;
    
    private TextView mSpheroState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		mSpheroState=(TextView)findViewById(R.id.sphero_state);

		 mSpheroConnectionView = (SpheroConnectionView) findViewById(R.id.sphero_connection_view);
	        mSpheroConnectionView.addConnectionListener(new ConnectionListener() {

	            @Override
	            public void onConnected(Robot robot) {
	                Log.i(TAG, "Connection Successful " + robot);
	                mRobot=(Sphero) robot;
	                
	                mSpheroState.setText("Sphero Notifier Active");
	                
	                main=MainActivity.this;
	                
	            }

	            @Override
	            public void onConnectionFailed(Robot sphero) {
	                Log.e(TAG, "Connection Failed: " + sphero);

	            }

	            @Override
	            public void onDisconnected(Robot sphero) {
	                Log.w(TAG, "Sphero Disconnected");
	                mSpheroConnectionView.startDiscovery();
	            }
	        });
	        
	        
	        RobotProvider.getDefaultProvider().addDiscoveryListener(new DiscoveryListener() {
	            @Override
	            public void onBluetoothDisabled() {
	                Log.e(TAG, "Bluetooth Disabled");
	                Toast.makeText(MainActivity.this, "Bluetooth Disabled - Enable Bluetooth First", Toast.LENGTH_SHORT).show();
	            }

	            @Override
	            public void discoveryComplete(List<Sphero> spheros) {
	            }

	            @Override
	            public void onFound(List<Sphero> sphero) {

	            }
	        });
		
	        
	}
	
	
	@Override
    protected void onResume() {
        super.onResume();
        mSpheroConnectionView.startDiscovery();

        if (mReceiver == null) 
        	mReceiver = new SMReceiver();
        
        registerReceiver(mReceiver, new IntentFilter(Constants.PLANETGEEKZ_NOTIFICATION));
        
    }
	
	@Override
	protected void onDestroy() {
	 super.onDestroy();
      RobotProvider.getDefaultProvider().disconnectControlledRobots();
     
      unregisterReceiver(mReceiver);
	}
	


    @Override
    protected void onPause() {
        super.onPause();

    }
	
	
    
	public class SMReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent != null) {

				Sphero sphero = main.mRobot;

				if (sphero == null) {
					Log.e(TAG, "\t" + "Robot is null");
					return;
				} else {
					Bundle extras = intent.getExtras();

					boolean isNotificationRemoved = extras
							.getBoolean(Constants.INTENT_EXTRAS_NOTIFICATION_REMOVED);

					if (isNotificationRemoved) {
						// yellow
						Log.e(TAG, "\t" + "Notification removed");

						sphero.setColor(255, 255, 0);

					}

					else {

						String notificationPackage = extras
								.getString("packageName");

						Log.i(TAG, "\t" + notificationPackage);

						if (notificationPackage
								.equals(Constants.NOTIFICATION_GMAIL)
								|| notificationPackage
										.equals(Constants.NOTIFICATION_GTALK)) {
							// red
							sphero.setColor(255, 0, 0);

						} else if (notificationPackage
								.equals(Constants.NOTIFICATION_WHATSAPP)) {
							// whatsapp green
							sphero.setColor(33, 158, 0);

						} else if (notificationPackage
								.equals(Constants.NOTIFICATION_FACEBOOK)) {
							// facebook navy
							sphero.setColor(56, 77, 144);

						} else if (notificationPackage
								.equals(Constants.NOTIFICATION_TWITTER)) {

							// twitter blue
							sphero.setColor(0, 109, 164);

						} else {
							// unknown notification, black
							sphero.setColor(0, 0, 0);

						}

					}
				}
			}
		}
	}
}


