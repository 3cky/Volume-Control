package com.twenk11k.sideproject.volumecontrol;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.twenk11k.sideproject.volumecontrol.listener.OnAudioVolumeChangedListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnAudioVolumeChangedListener {

    private SeekBar seekBarVolume = null;
    private AudioManager audioManager = null;
    private int sbarProgress = 0;
    private AudioVolumeObserver mAudioVolumeObserver;
    private Drawable musicNoteD,musicOffD;
    // Butterknife bindings..
    @BindView(R.id.music_note)
    ImageView musicNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setSeekBar();
        musicNoteD = getResources().getDrawable(R.drawable.baseline_music_note_white_24);
        musicOffD = getResources().getDrawable(R.drawable.baseline_music_off_white_24);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAudioVolumeObserver != null) {
            mAudioVolumeObserver.unregister();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(seekBarVolume!=null){
            if(audioManager!=null){
                int currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                seekBarVolume.setProgress(currentVol);
                handleIcon(currentVol);
            }
        }
        if (mAudioVolumeObserver == null) {
            mAudioVolumeObserver = new AudioVolumeObserver(this);
        }
        mAudioVolumeObserver.register(AudioManager.STREAM_MUSIC, this);

    }

    private void setSeekBar() {
        try
        {
            seekBarVolume = (SeekBar)findViewById(R.id.seekbar);
            seekBarVolume.setSecondaryProgress(100);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            seekBarVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            seekBarVolume.setProgress(volume);
            seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            sbarProgress, AudioManager.FLAG_PLAY_SOUND);
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    Log.d("progres_changed",String.valueOf(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)));
                    sbarProgress = progress;
                    handleIcon(progress);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onAudioVolumeChanged(int currentVolume, int maxVolume) {
        if(seekBarVolume!=null){
                seekBarVolume.setProgress(currentVolume);
        }
        handleIcon(currentVolume);
    }
    private void handleIcon(int val){
        Log.d("theval",String.valueOf(val));
        if(val==0){
                musicNote.setImageDrawable(musicOffD);
        } else {
                musicNote.setImageDrawable(musicNoteD);
        }
    }
}
