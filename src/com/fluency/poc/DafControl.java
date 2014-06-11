package com.fluency.poc;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import java.util.Set;

public class DafControl
{
  private static final String TAG = "Daf";
  private boolean autoMute;
  private Context context;
  private int delay;
  private int freq;
  private int muteAfterSec;
 // private MuteSensitivity muteSensitivity;
  private Player player;
  private Thread recThread;
  private Recorder recorder;
  private boolean running = false;
  private boolean useBt=false;
  
  public DafControl(Context paramContext)
  {
    this.context = paramContext;
    this.player = new Player();
    this.recorder = new Recorder(paramContext, this.player, this);
  }
  
  private boolean isBluetoothEnabled()
  {
    BluetoothAdapter localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    boolean bool = false;
    if (localBluetoothAdapter != null) {
      bool = localBluetoothAdapter.isEnabled();
    }
    if ((bool) && (localBluetoothAdapter.getBondedDevices().size() == 0)) {
      bool = false;
    }
    return bool;
  }
  
  public int getDelay()
  {
    return this.delay;
  }
  
  public int getFreq()
  {
    return this.freq;
  }
  
  public int getMuteAfterSec()
  {
    return this.muteAfterSec;
  }
  
//  public MuteSensitivity getMuteSensitivity()
//  {
//    return this.muteSensitivity;
//  }
  
  public void init()
    throws Exception
  {
    if (this.useBt)
    {
//      if (!isBluetoothEnabled()) {
//        throw new Exception("Please turn on Bluetooth on your device and pair your headset.");
//      }
//      ((AudioManager)this.context.getSystemService("audio")).startBluetoothSco();
    }
    this.recorder.init();
    this.recThread = new Thread(this.recorder);
    this.player.init(this.recorder.getSampleRate(), this.useBt);
  }
  
  public boolean isAutoMute()
  {
    return this.autoMute;
  }
  
  public boolean isRunning()
  {
    return this.running;
  }
  
  public boolean isUseBt()
  {
    return this.useBt;
  }
  
  public void resetDelay()
  {
    this.recorder.adjustDelay();
  }
  
  public void setAutoMute(boolean paramBoolean)
  {
    this.autoMute = paramBoolean;
  }
  
  public void setDelay(int paramInt)
  {
    this.delay = paramInt;
  }
  
  public void setFreq(int paramInt)
  {
    this.freq = paramInt;
  }
  
  public void setMuteAfterSec(int paramInt)
  {
    this.muteAfterSec = paramInt;
  }
  
//  public void setMuteSensitivity(MuteSensitivity paramMuteSensitivity)
//  {
//    this.muteSensitivity = paramMuteSensitivity;
//  }
  
  public void setUseBt(boolean paramBoolean)
  {
    this.useBt = paramBoolean;
  }
  
  public void start()
  {
    this.running = true;
    this.player.start();
    this.recThread.start();
  }
  
  public void stop()
  {
    this.recorder.stop();
    this.player.stop();
    try
    {
      this.recThread.join();
      this.running = false;
      if (this.useBt) {
        ((AudioManager)this.recorder.getContext().getSystemService("audio")).stopBluetoothSco();
      }
      return;
    }
    catch (InterruptedException localInterruptedException)
    {
      for (;;)
      {
        localInterruptedException.printStackTrace();
      }
    }
  }
  
//  public static enum MuteSensitivity
//  {
//    private short threshold;
//    
//    static
//    {
//      MuteSensitivity[] arrayOfMuteSensitivity = new MuteSensitivity[3];
//      arrayOfMuteSensitivity[0] = quietVoice;
//      arrayOfMuteSensitivity[1] = normalVoice;
//      arrayOfMuteSensitivity[2] = loudVoice;
//      ENUM$VALUES = arrayOfMuteSensitivity;
//    }
//    
//    private MuteSensitivity(int paramInt)
//    {
//      this.threshold = ((short)paramInt);
//    }
//    
//    public static MuteSensitivity fromString(String paramString)
//    {
//      MuteSensitivity[] arrayOfMuteSensitivity = values();
//      int i = arrayOfMuteSensitivity.length;
//      for (int j = 0;; j++)
//      {
//        MuteSensitivity localMuteSensitivity;
//        if (j >= i) {
//          localMuteSensitivity = normalVoice;
//        }
//        do
//        {
//          return localMuteSensitivity;
//          localMuteSensitivity = arrayOfMuteSensitivity[j];
//        } while (localMuteSensitivity.toString().equalsIgnoreCase(paramString));
//      }
//    }
//    
//    public short getThreshold()
//    {
//      return this.threshold;
//    }
//  }
}