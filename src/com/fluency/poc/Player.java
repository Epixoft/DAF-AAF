package com.fluency.poc;

import android.media.AudioTrack;

public class Player
{
  private static final String TAG = "Daf";
  private AudioTrack atrack;
  
  public void flush()
  {
    this.atrack.flush();
  }
  
  public void init(int paramInt, boolean paramBoolean)
    throws Exception
  {
    int i = AudioTrack.getMinBufferSize(paramInt, 2, 2);
    int j = 3;
    if (paramBoolean) {
      j = 0;
    }
    this.atrack = new AudioTrack(j, paramInt, 2, 2, i * 1, AudioTrack.MODE_STREAM);
    this.atrack.setPlaybackRate(paramInt);
    if (this.atrack.getState() != 1) {
      throw new Exception("Could not initialize player.");
    }
  }
  
  public void play(short[] paramArrayOfShort, int paramInt)
  {
    this.atrack.write(paramArrayOfShort, 0, paramInt);
  }
  
  public void start()
  {
    this.atrack.play();
  }
  
  public void stop()
  {
    this.atrack.stop();
    this.atrack.release();
  }
}