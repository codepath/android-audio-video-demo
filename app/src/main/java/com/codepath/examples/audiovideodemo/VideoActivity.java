package com.codepath.examples.audiovideodemo;

import java.io.File;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
	}

	public void onPlayLocalVideo(View v) {
		VideoView mVideoView = (VideoView) findViewById(R.id.video_view);
		mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/"
				+ R.raw.small_video));
		mVideoView.setMediaController(new MediaController(this));
		mVideoView.requestFocus();
		mVideoView.start();
	}

	public void onStreamVideo(View v) {
		final VideoView mVideoView = (VideoView) findViewById(R.id.video_view);
		mVideoView.setVideoPath("http://techslides.com/demos/sample-videos/small.mp4");
		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(mVideoView);
		mVideoView.setMediaController(mediaController);
		mVideoView.requestFocus();
		mVideoView.setOnPreparedListener(new OnPreparedListener() {
			// Close the progress bar and play the video
			public void onPrepared(MediaPlayer mp) {
				mVideoView.start();
			}
		});
	}

	private static final int VIDEO_CAPTURE = 101;
	Uri videoUri;
	public void onRecordVideo(View v) {
		if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvideo.mp4");
			videoUri = Uri.fromFile(mediaFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
			startActivityForResult(intent, VIDEO_CAPTURE);
		} else {
			Toast.makeText(this, "No camera on device", Toast.LENGTH_LONG).show();
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == VIDEO_CAPTURE) {
		  if (resultCode == RESULT_OK) {
			  VideoView mVideoView = (VideoView) findViewById(R.id.video_view);
			  mVideoView.setVideoURI(videoUri);
			  mVideoView.setMediaController(new MediaController(this));
			  mVideoView.requestFocus();
			  mVideoView.start(); 
		  } else if (resultCode == RESULT_CANCELED) {
		    	Toast.makeText(this, "Video recording cancelled.",  Toast.LENGTH_LONG).show();
		  } else {
		     Toast.makeText(this, "Failed to record video",  Toast.LENGTH_LONG).show();
	        }
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video, menu);
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
