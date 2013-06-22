package com.grayditch.netarea;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent startServiceIntent = new Intent(context,
					NetareaService.class);
			context.startService(startServiceIntent);
		}
	}
}