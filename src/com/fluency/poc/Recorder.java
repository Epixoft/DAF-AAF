package com.fluency.poc;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Process;
import android.util.Log;

import java.util.Date;

public class Recorder
  implements Runnable
{
  public static final int AUDIO_ENCODING = 2;
  public static final int[] BT_DEFAULT_SAMPLE_RATE = { 8000 };
  public static final int CHANNEL_CONFIGURATION = 2;
  public static final int[] DEFAULT_SAMPLE_RATE = { 44100, 22050, 11025, 8000 };
  public static final int SAMPLES_TO_ANALYZE = 512;
  private static final String TAG = "RecordClass:";
  private int bufferSizeBytes = 0;
  private Context context;
  private DafControl ctrl;
  private boolean delayAdjusting = false;
  private int delayAdjustingCount = 0;
  private volatile boolean isRecording;
  private Date lastTimeAboveThreshold = new Date();
  private Player player;
  private AudioRecord recordInstance;
  private int sampleRate;
  
  public Recorder(Context paramContext, Player paramPlayer, DafControl paramDafControl)
  {
    this.context = paramContext;
    this.player = paramPlayer;
    this.ctrl = paramDafControl;
    setSampleRate(DEFAULT_SAMPLE_RATE[0]);
    this.lastTimeAboveThreshold.setTime(3000L + this.lastTimeAboveThreshold.getTime());
  }
  
  private void performPitchShiftTD(short[] paramArrayOfShort, int paramInt, float paramFloat)
  {
	  if (paramFloat == 0.0F)
	 	return;
	 else
	 {
    while (true)
    {
    	
      float f1 = 0.0F;
      if (paramFloat > 1.0D)
      {
        int j = 1;
        if (paramInt > 1024)
          j = 2;
        int k = paramInt / j;
        int m = 0;
        int n=0;
        while (true)
          if (m < j)
          {
            n = m * k;
            if (n >= k + m * k)
            {
              m++;
              continue;
            }
          }
          else
          {
            break;
          }
        paramArrayOfShort[n] = paramArrayOfShort[(int)f1];
        if (1 + (int)(f1 + paramFloat) >= k + m * k)
          paramArrayOfShort[n] = paramArrayOfShort[(-1 + (k + m * k))];
        while (true)
        {
          n++;
          f1 += paramFloat;
          break;
         
        }
      }
      float f2 = 1.0F;
      float f3 = 0.0F;
      float f4 = paramFloat * paramInt;
      for (int i = paramInt - 1; i >= 0; i--)
      {
        if (f4 < 0.0F)
          f4 = 0.0F;
        paramArrayOfShort[i] = paramArrayOfShort[(int)f4];
        if (i < 20)
        {
          f2 -= 0.05F;
          if (f2 < 0.0D)
            f2 = 0.0F;
          paramArrayOfShort[i] = (short)(int)(f2 * paramArrayOfShort[i]);
        }
        if (paramInt - i < 20)
        {
          f3 += 0.05F;
          if (f3 > 1.0F)
            f3 = 1.0F;
          paramArrayOfShort[i] = (short)(int)(f3 * paramArrayOfShort[i]);
        }
        f4 -= paramFloat;
      }
    }
	 }
  }
  
  private void preprocess(short[] paramArrayOfShort, int paramInt)
  {
//    int i;
//    long l;
//    int j;
//    if (this.ctrl.isAutoMute())
//    {
//      i = 0;
//      l = 0L;
//      j = 0;
//      if (j < paramInt)
//        break label99;
//     }
//    for (int k = 0; ; k++)
//    {
//      if (k >= paramInt)
//      {
//        return;
//        label99: if (paramArrayOfShort[j] > 0)
//        {
//          l += paramArrayOfShort[j];
//          i++;
//        }
//        j++;
//        break;
//      }
//      paramArrayOfShort[k] = 0;
//    }
  }
  
  public void adjustDelay()
  {
    this.delayAdjustingCount = 0;
    this.delayAdjusting = true;
  }
  
  public Context getContext()
  {
    return this.context;
  }
  
  public int getSampleRate()
  {
    return this.sampleRate;
  }
  
  public void init()
    throws Exception
  {
    this.recordInstance= findAudioRecord();
    
  }
  
  
  private static int[] mSampleRates = new int[] { 44100,  22050, 11025,8000  };
  public AudioRecord findAudioRecord() {
      for (int rate : mSampleRates) {
          for (short audioFormat : new short[] { AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT }) {
              for (short channelConfig : new short[] { AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO }) {
                  try {
                      Log.d(TAG, "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                              + channelConfig);
                      int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                      if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                          // check if we can instantiate and have a success
                          AudioRecord recorder = new AudioRecord(AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);

                          if (recorder.getState() == AudioRecord.STATE_INITIALIZED)
                          {
                        	  setSampleRate(rate);
                              this.bufferSizeBytes = AudioRecord.getMinBufferSize(getSampleRate(), 2, 2);
                              return recorder;
                          }
                      }
                  } catch (Exception e) {
                      Log.e(TAG, rate + "Exception, keep trying.",e);
                  }
              }
          }
      }
      return null;
  }

 
  public void playDelay()
  {
    int i = this.ctrl.getDelay();
    int j = 0;
    short[] arrayOfShort=null;
    if (i > 0)
    {
      j = 1 * (i * 10 * getSampleRate()) / 1000;
      arrayOfShort = new short[j];
    
      try
      {
    	  this.player.play(arrayOfShort, j);
    	  return;
      }
      catch (Exception localException) {}
    }
  }
  
  public void run()
  {
    int i = 0;
    Process.setThreadPriority(-19);
    short[] arrayOfShort = new short[this.bufferSizeBytes / 8];
    this.recordInstance.startRecording();
    this.isRecording = true;
    
//    if (!this.isRecording);
//	{
//		this.recordInstance.stop();
//		this.recordInstance.release();
//		return;
//	}
	
    
    while (true)
    {
      int k,j;
      float f;
      try
      {
        while (true)
        {
        	
          j = this.recordInstance.read(arrayOfShort, 0, arrayOfShort.length);
          if (j == -3)
            throw new IllegalStateException("read() returned AudioRecord.ERROR_INVALID_OPERATION");
          if (j == -2)
            throw new IllegalStateException("read() returned AudioRecord.ERROR_BAD_VALUE");
          if (i == 0);
          try
          {
            playDelay();
            i = 1;
            k = -10 + this.ctrl.getFreq();
            if (k <= 0)
            {
              f = 1.0F - -0.015F * k;
              if (k != 0)
                performPitchShiftTD(arrayOfShort, j, f);
              preprocess(arrayOfShort, j);
              if (this.delayAdjusting)
              {
                if (j <= 0)
                  break;
                this.delayAdjustingCount = (1 + this.delayAdjustingCount);
                if (this.delayAdjustingCount <= 16)
                  break;
                this.delayAdjustingCount = 0;
                this.delayAdjusting = false;
                i = 0;
                break;
              }
              if (j <= 0)
                break;
              this.player.play(arrayOfShort, j);
            }
          }
          catch (Exception localException1)
          {
          }
        }
      }
      catch (Exception localException2)
      {
        return;
      }
      f = 1.0F + 0.05F * k;
    }
	
  }
  
  public void setContext(Context paramContext)
  {
    this.context = paramContext;
  }
  
  public void setSampleRate(int paramInt)
  {
    this.sampleRate = paramInt;
  }
  
  public void stop()
  {
    this.isRecording = false;
  }
}
