package com.eltechs.axs.overlay;

import android.content.Context;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.app.AlertDialog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.nio.CharBuffer;

public class VirGL {
	public static String title = "VirGL (...)";
	private AlertDialog.Builder builder;
	private LinearLayout layout;
	private EditText socket_path;
	private CheckBox protocol_version_box, use_gles, use_threads, overlay_centered, dxtn_decompress_box, restart_box,
			no_overlay_box;
	//#define FL_GLX (1<<1)
	public static final int FL_GLES = (1 << 2);
	//#define FL_OVERLAY (1<<3)
	public static final int FL_MULTITHREAD = (1 << 4);
	public int centered = 0, restart_var = 0, protocol_version = 0, no_overlay_var = 0, dxtn_decompress = 0;
	Context context;

	VirGL(Context context) {
		this.context = context;
	}

	public void dialogSetup() {
		builder = new AlertDialog.Builder(context/*, style*/);
		layout = new LinearLayout(context);
		socket_path = new EditText(context);
		protocol_version_box = new CheckBox(context);
		use_gles = new CheckBox(context);
		use_threads = new CheckBox(context);
		overlay_centered = new CheckBox(context);
		dxtn_decompress_box = new CheckBox(context);
		restart_box = new CheckBox(context);
		no_overlay_box = new CheckBox(context);
		int flags = 0;

		builder.setMessage("VirGL [ tutorial and info on help session ]");
		layout.setOrientation(1);
		socket_path.setSingleLine();
		socket_path.setHint("path: /tmp/.virgl_test");
		protocol_version_box.setText("Use vtest protocol version 2 (new Mesa libGL needed)");
		use_gles.setText("Use GL ES 3.x instead of OpenGL");
		use_threads.setText("Use multi-thread egl access");
		overlay_centered.setText("Set overlay centered instead of top-left");
		dxtn_decompress_box.setText("DXTn (S3TC) decompress");
		restart_box.setText("Auto restart services");
		no_overlay_box.setText("Disable Overlay Resize*");

		/*
		//Проверка разрешения на показ поверх других приложений
		if (!Settings.canDrawOverlays(this)) {
			Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
					Uri.parse("package:" + getPackageName()));
			startActivityForResult(intent, 222);
		}
		*/

		try {
			CharBuffer buffer = CharBuffer.allocate(128);
			FileReader settings_reader = new FileReader(context.getFilesDir().getPath() + "/settings");
			BufferedReader reader = new BufferedReader(settings_reader);
			String[] parts = reader.readLine().split(" ");
			flags = Integer.parseInt(parts[0]);
			socket_path.setText(parts[1]);
			reader.close();
			settings_reader.close();
		} catch (Exception e) {
		}

		try {
			// Считываем
			FileReader settings_reader1 = new FileReader(context.getFilesDir().getPath() + "/settings2");
			BufferedReader reader1 = new BufferedReader(settings_reader1);
			String[] parts1 = reader1.readLine().split(" ");
			centered = Integer.parseInt(parts1[0]);
			restart_var = Integer.parseInt(parts1[1]);
			protocol_version = Integer.parseInt(parts1[2]);
			dxtn_decompress = Integer.parseInt(parts1[3]);
			no_overlay_var = Integer.parseInt(parts1[4]);
			reader1.close();
			settings_reader1.close();
		} catch (Exception e) {
		}

		protocol_version_box.setChecked(protocol_version != 0);
		use_gles.setChecked((flags & FL_GLES) != 0);
		use_threads.setChecked((flags & FL_MULTITHREAD) != 0);
		overlay_centered.setChecked(centered != 0);
		dxtn_decompress_box.setChecked(dxtn_decompress != 0);
		restart_box.setChecked(restart_var != 0);
		no_overlay_box.setChecked(no_overlay_var != 0);

		layout.addView(socket_path);
		layout.addView(protocol_version_box);
		layout.addView(use_gles);
		layout.addView(use_threads);
		layout.addView(overlay_centered);
		layout.addView(dxtn_decompress_box);
		layout.addView(restart_box);
		layout.addView(no_overlay_box);
		builder.setView(layout);

		builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				startVirGL();
				title = "VirGL (on)";
			}
		});

		builder.setNegativeButton("Clean", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				cleanVirGL();
				title = "VirGL (off)";
			}
		});

		builder.show();
	}

	public void cleanVirGL() {
		if (restart_var == 1) {
			writeStop(1);
		}
		for (int i = 1; i < 32; i++) {
			try {
				//stopService(new Intent().setClassName(context, getPackageName() + ".process.p" + i));
			} catch (Exception e) {
			}
		}

		if (restart_var == 1) {
			writeStop(0);
		}
	}

	public void writeStop(int stop) {
		try {
			FileWriter writer3 = new FileWriter(context.getFilesDir().getPath() + "/stop");
			writer3.write(String.valueOf(stop));
			writer3.close();
		} catch (IOException e) {
		}
	}

	private void startVirGL() {

		try {
			// Сохраняем
			if (overlay_centered.isChecked()) {
				centered = 1;
			} else {
				centered = 0;
			}

			if (restart_box.isChecked()) {
				restart_var = 1;
			} else {
				restart_var = 0;
			}
			if (no_overlay_box.isChecked()) {
				no_overlay_var = 1;
			} else {
				no_overlay_var = 0;
			}

			if (dxtn_decompress_box.isChecked()) {
				dxtn_decompress = 1;
			} else {
				dxtn_decompress = 0;
			}
			if (protocol_version_box.isChecked()) {
				protocol_version = 1;
			} else {
				protocol_version = 0;
			}

			FileWriter writer1 = new FileWriter(context.getFilesDir().getPath() + "/settings2");
			writer1.write(String.valueOf(centered));
			writer1.write(' ');
			writer1.write(String.valueOf(restart_var));
			writer1.write(' ');
			writer1.write(String.valueOf(protocol_version));
			writer1.write(' ');
			writer1.write(String.valueOf(dxtn_decompress));
			writer1.write(' ');
			writer1.write(String.valueOf(no_overlay_var));
			writer1.close();

			int flags = 0;
			if (use_gles.isChecked())
				flags |= FL_GLES;
			if (use_threads.isChecked())
				flags |= FL_MULTITHREAD;

			FileWriter writer = new FileWriter(context.getFilesDir().getPath() + "/settings");
			writer.write(String.valueOf(flags));
			writer.write(' ');
			writer.write(socket_path.getText().toString());
			writer.close();

			if (restart_var == 1) {
				writeStop(0);
			}

			//Intent intent = new Intent().setClassName(MainActivity.this, getPackageName() + ".process.p1");
			//startService(intent);
		} catch (Exception e) {
		}
	}

}