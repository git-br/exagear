package com.eltechs.axs.overlay;

import android.app.Activity;
import android.os.Bundle;
import android.app.Dialog;
import android.view.MenuItem;
import android.view.Menu;
import com.eltechs.axs.overlay.VirGL;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 1, VirGL.title);
		menu.getItem(0).setShowAsAction(1);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getOrder() == 1) {
			item.setTitle("VirGL (...)");
			new VirGL(this).dialogSetup();
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

}