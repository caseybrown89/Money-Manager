package com.caseynbrown.moneymanager;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ModalDelete extends Dialog {
	public interface ReadyListener{
		public void ready(boolean delete);
	}

	private Button yes, no;
	private TextView message;
	private ReadyListener readyListener;
	private String name;

	public ModalDelete(Context c, String name, ReadyListener readyListener){
		super(c);
		this.readyListener = readyListener;
		this.name = name;
	}

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.deleteview);
		setTitle("Delete");

		this.yes = ((Button) findViewById(R.id.del_yes));
		this.no = ((Button) findViewById(R.id.del_no));
		this.message = ((TextView) findViewById(R.id.del_title));

		this.message.setText("Are you sure you want to delete "+name+"?");

		// Click listeners Owe options
		this.yes.setOnClickListener(new ModalListener(true));
		this.no.setOnClickListener(new ModalListener(false));
	}

	// Nested Class for dialog
	protected class ModalListener implements android.view.View.OnClickListener{
		boolean delete;

		ModalListener(boolean delete){
			this.delete = delete;
		}

		@Override
		public void onClick(View v) {
			readyListener.ready(this.delete);
			ModalDelete.this.dismiss();
		}
	}
}
