package com.empeegee.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";

    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssets;
    private SoundPool mSoundPool;

    private float speedOfSound = 1.0f;

    private List<Sound> mSounds = new ArrayList<>();

    public BeatBox(Context context){
        mAssets = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);

        loadSounds();
    }

    private void loadSounds(){
        String[] soundNames;

        try{
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.d(TAG, "Found " + soundNames[3] + " sound!");
        } catch (IOException ioe){
            Log.d(TAG, "Could not list assets");
            return;
        }

        for (String filename : soundNames){
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            } catch (IOException ioe){
                Log.e(TAG, "Could not load"+ filename, ioe);
            }

        }
    }


    private void load(Sound sound) throws IOException {
        AssetFileDescriptor assetFileDescriptor = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(assetFileDescriptor, 1);
        sound.setSoundId(soundId);
    }

    public void play(Sound sound){
        Integer soundId = sound.getSoundId();
        if (soundId == null){
            return;
        }

        mSoundPool.play(soundId, 1.0f, 1.0f, 1,0, speedOfSound);
    }

    public void release(){
        mSoundPool.release();
    }

    public void setSpeedOfSound(float speed){
        speedOfSound = speed;
    }

    public List<Sound> getSounds(){
        return mSounds;
    }
}