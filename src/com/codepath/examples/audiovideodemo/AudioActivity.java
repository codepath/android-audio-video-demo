package com.codepath.examples.audiovideodemo;

import java.io.IOException;

import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AudioActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);
	}
	
	public void onPlayLocalAudio(View v) {
		MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sample_audio);
		mediaPlayer.start();
	}
	
	public void onStreamAudio(View v) {
		String url = "https://dl.dropboxusercontent.com/u/10281242/sample_audio.mp3"; // your URL here
		final MediaPlayer mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		// Listen for if the audio file can't be prepared
		mediaPlayer.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// ... react appropriately ...
		        // The MediaPlayer has moved to the Error state, must be reset!
				return false;
			}
		});
		// Attach to when audio file is prepared for playing
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mediaPlayer.start();
			}
		});
		// Set the data source to the remote URL
		// Trigger an async preparation which will file listener when completed
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	MediaRecorder mediaRecorder;
	String mFileName;
	public void onRecordAudio(View v) {
		// Verify that the device has a mic
		PackageManager pmanager = this.getPackageManager();
		if (!pmanager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
			Toast.makeText(this, "This device doesn't have a mic!", Toast.LENGTH_LONG).show();
			return;
		}
		
		
		Button btnRecord = (Button) v;
		// Start the recording
		if (v.getTag() == "start" || v.getTag() == null) {
			mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
	        mFileName += "/audiorecordtest.3gp";
	        mediaRecorder = new MediaRecorder();

	        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	        mediaRecorder.setOutputFile(mFileName);
			v.setTag("stop");
			btnRecord.setText("Stop");			
			
						
			try {
				mediaRecorder.prepare();
				mediaRecorder.start();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (v.getTag() == "stop") {
			mediaRecorder.stop();
			mediaRecorder.reset();
			mediaRecorder.release();
			v.setTag("start");
			btnRecord.setText("Record Audio");	
			MediaPlayer mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			try {
				mediaPlayer.setDataSource(mFileName);
				mediaPlayer.prepare(); // must call prepare first 
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mediaPlayer.start(); // then start
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.audio, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
