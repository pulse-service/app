package com.electrocraft.nirzo.pluse.view.activity.doctor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.electrocraft.nirzo.pluse.view.MainActivity;
import com.electrocraft.nirzo.pluse.view.util.Key;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 *
 * @author Faisal
 * @since 3/17/2018.
 */

public class DocNotifyCallReceivedAlertActivity extends Activity {
    private static final String LOG_TAG = "DocNotifyCallReceivedAlertActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        displayAlert();
    }

    private void displayAlert() {


        try {
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            final MediaPlayer mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(this, alert);
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }

            new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("Calling")
                    .setContentText("You have a call !")
                    .setConfirmText("Accept")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            mMediaPlayer.stop();
                            sDialog.dismissWithAnimation();
                            Intent intent = new Intent(DocNotifyCallReceivedAlertActivity.this, MainActivity.class);
                            intent.putExtra(Key.KEY_IS_PATIENT_OR_DOCTOR, false);
                            startActivity(intent);
                        }
                    })
                    .setCancelButton("Reject", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            mMediaPlayer.stop();
                            sDialog.dismissWithAnimation();

                            Intent intent = new Intent(DocNotifyCallReceivedAlertActivity.this, DoctorHomeActivity.class);

                            startActivity(intent);
                        }
                    })
                    .show();
        } catch (Exception e) {
        }



    }
}
