package com.eltechs.ed.smali1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity {
	AlertDialog.Builder alert;
	RadioGroup group;
	RadioButton size1, size2, size3, size4, size5;

	public void toast() {
		Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		toast();

		alert = new AlertDialog.Builder(this);
		group = new RadioGroup(this);
		size1 = new RadioButton(this);
		size2 = new RadioButton(this);
		size3 = new RadioButton(this);
		size4 = new RadioButton(this);
		size5 = new RadioButton(this);

		alert.setTitle("Screen size (width X height)");
		size1.setText("Default");
		size2.setText("480x640 (3:4)");
		size3.setText("600x800 (3:4)");
		size4.setText("640x480 (4:3)");
		size5.setText("800x600 (4:3)");

		group.addView(size1);
		group.addView(size2);
		group.addView(size3);
		group.addView(size4);
		group.addView(size5);

		alert.setView(group);
		alert.setPositiveButton("Cancel", null);

		alert.setNeutralButton("Custom", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				customSize(alert);
			}
		});

		alert.show();
	}

	private void customSize(AlertDialog.Builder dialog) {
		EditText custom = new EditText(this);
		custom.setKeyListener(DigitsKeyListener.getInstance("0123456789,"));
		custom.setFilters(new InputFilter[] { new InputFilter.LengthFilter(9) });
		custom.setBackground(null);
		int s1 = getDefaultScreenSize()[0];
		int s2 = getDefaultScreenSize()[1];
		custom.setText(s1 + "," + s2);
		dialog.setView(custom);
		dialog.setNeutralButton("Confirm", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				if (custom.getText().toString().contains(",") && custom.getText().toString().length() > 5) {
					Toast.makeText(dialog.getContext(), custom.getText(), Toast.LENGTH_LONG).show();
				}
			}
		});
		dialog.show();
	}

	private static int[] getDefaultScreenSize() {
		int i = (int) (((float) 720.0) / 32);
		int i2 = (int) (((float) 1600.0) / 32);
		int max = Math.max(i, i2);
		i2 = Math.min(i, i2);
		i = Math.max(max, 1136); //1136 	× 	640 
		i2 = Math.max(i2, 640);

		return new int[] { i, i2 };
	}

}
