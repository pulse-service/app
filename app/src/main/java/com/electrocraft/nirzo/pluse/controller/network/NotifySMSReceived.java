package com.electrocraft.nirzo.pluse.controller.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.electrocraft.nirzo.pluse.view.MainActivity;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DoctorHomeActivity;
import com.electrocraft.nirzo.pluse.view.util.Key;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by nirzo on 3/17/2018.
 */

public class NotifySMSReceived extends Activity {
    private static final String LOG_TAG = "SMSReceiver";


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
                            Intent intent = new Intent(NotifySMSReceived.this, MainActivity.class);
                            intent.putExtra(Key.KEY_IS_PATIENT_OR_DOCTOR, false);
                            startActivity(intent);
                        }
                    })
                    .setCancelButton("Reject", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            mMediaPlayer.stop();
                            sDialog.dismissWithAnimation();

                            Intent intent = new Intent(NotifySMSReceived.this, DoctorHomeActivity.class);

                            startActivity(intent);
                        }
                    })
                    .show();
        } catch (Exception e) {
        }



    }
}
