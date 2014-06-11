package com.fluency.poc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class Daf
  extends Activity
{
  private static final boolean DEMO_MODE = true;
  private static final String PREFS_NAME = "DafPrefsFile";
  private static final String TAG = "Daf";
  private DafControl ctrl = new DafControl(this);
  private TextView delayLabel;
  private SeekBar delaySlider;
  private DafDemo demo;
  private TextView freqLabel;
  private SeekBar freqSlider;
  private Button startBtn;
  private Button stopBtn;
  
  private void adjustScreen()
  {
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
    int i = localDisplayMetrics.heightPixels;
    int j = localDisplayMetrics.widthPixels;
    if ((i == 960) && (j == 540))
    {
      ((ViewGroup.MarginLayoutParams)this.delaySlider.getLayoutParams()).setMargins(100, 300, 20, 0);
      ((ViewGroup.MarginLayoutParams)this.freqSlider.getLayoutParams()).setMargins(40, 240, 0, 0);
      ((ViewGroup.MarginLayoutParams)this.delayLabel.getLayoutParams()).setMargins(120, 170, 0, 0);
      ((ViewGroup.MarginLayoutParams)this.freqLabel.getLayoutParams()).setMargins(20, 460, 0, 0);
    }
    while ((i != 1184) || (j != 720)) {
      return;
    }
    ((ViewGroup.MarginLayoutParams)this.delaySlider.getLayoutParams()).setMargins(140, 350, 20, 0);
    ((ViewGroup.MarginLayoutParams)this.freqSlider.getLayoutParams()).setMargins(40, 275, 0, 0);
    ((ViewGroup.MarginLayoutParams)this.delayLabel.getLayoutParams()).setMargins(150, 200, 0, 0);
    ((ViewGroup.MarginLayoutParams)this.freqLabel.getLayoutParams()).setMargins(30, 550, 0, 0);
    this.delayLabel.setWidth(100);
  }
  
  private void refreshLabels()
  {
    this.delayLabel.setText(String.valueOf(1 + this.ctrl.getDelay()));
    this.freqLabel.setText(String.valueOf(-10 + this.ctrl.getFreq()));
  }
  
  private void restart()
  {
    if (this.ctrl.isRunning())
    {
      stop();
      start();
    }
  }
  
  private void restoreConfigurablePreferences()
  {
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    boolean bool1 = localSharedPreferences.getBoolean("btEnabled", false);
    this.ctrl.setUseBt(bool1);
    updateBtIcon();
    boolean bool2 = localSharedPreferences.getBoolean("autoMuteEnabled", false);
    this.ctrl.setAutoMute(bool2);
//    try
//    {
//      int j = Integer.valueOf(localSharedPreferences.getString("muteAfterSec", "5")).intValue();
//      i = j;
//    }
//    catch (Exception localException)
//    {
//      for (;;)
//      {
//        String str;
//        int i = 5;
//      }
//    }
//    this.ctrl.setMuteAfterSec(i);
//    str = localSharedPreferences.getString("muteSensitivity", DafControl.MuteSensitivity.normalVoice.toString());
//    this.ctrl.setMuteSensitivity(DafControl.MuteSensitivity.fromString(str));
  }
  
  private void restoreSettings()
  {
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    int i = localSharedPreferences.getInt("delay", 0);
    int j = localSharedPreferences.getInt("freq", 10);
    this.ctrl.setDelay(i);
    this.ctrl.setFreq(j);
    restoreConfigurablePreferences();
  }
  
  private void saveSettings()
  {
    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
    localEditor.putInt("delay", this.ctrl.getDelay());
    localEditor.putInt("freq", this.ctrl.getFreq());
    localEditor.putBoolean("btEnabled", this.ctrl.isUseBt());
    localEditor.putBoolean("autoMuteEnabled", false);
//    localEditor.putString("muteAfterSec", String.valueOf(this.ctrl.getMuteAfterSec()));
//    localEditor.putString("muteSensitivity", String.valueOf(this.ctrl.getMuteSensitivity()));
    localEditor.commit();
  }
  
  private void start()
  {
    try
    {
      if (!this.ctrl.isRunning())
      {
        if (!this.demo.canStartDemo())
        {
          this.demo.showDemoDialog();
          return;
        }
        this.ctrl.init();
        this.startBtn.setEnabled(false);
        this.stopBtn.setEnabled(true);
        this.ctrl.start();
        return;
      }
    }
    catch (Exception localException)
    {
      showDialog(localException.getMessage());
    }
  }
  
  private void updateBtIcon()
  {
//    ImageView localImageView = (ImageView)findViewById(2131296267);
//    if (this.ctrl.isUseBt())
//    {
//      localImageView.setVisibility(0);
//      return;
//    }
//    localImageView.setVisibility(4);
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_main);
    this.delayLabel = ((TextView)findViewById(R.id.delayLabel));
    this.freqLabel = ((TextView)findViewById(R.id.freqLabel));
    this.delaySlider = ((SeekBar)findViewById(R.id.delaySlider));
    this.delaySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
    {
      public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        Daf.this.ctrl.setDelay(paramAnonymousInt);
        Daf.this.saveSettings();
        Daf.this.refreshLabels();
      }
      
      public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {}
      
      public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
        Daf.this.ctrl.resetDelay();
      }
    });
//    ((Button)findViewById(2131296260)).setOnClickListener(new View.OnClickListener()
//    {
//      public void onClick(View paramAnonymousView)
//      {
//        Daf.this.delaySlider.incrementProgressBy(1);
//        Daf.this.ctrl.resetDelay();
//      }
//    });
//    ((Button)findViewById(2131296261)).setOnClickListener(new View.OnClickListener()
//    {
//      public void onClick(View paramAnonymousView)
//      {
//        Daf.this.delaySlider.incrementProgressBy(-1);
//        Daf.this.ctrl.resetDelay();
//      }
//    });
    this.freqSlider = ((SeekBar)findViewById(R.id.freqSlider));
    this.freqSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
    {
      public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        Daf.this.ctrl.setFreq(paramAnonymousInt);
        Daf.this.saveSettings();
        Daf.this.refreshLabels();
      }
      
      public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {}
      
      public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {}
    });
//    ((Button)findViewById(2131296258)).setOnClickListener(new View.OnClickListener()
//    {
//      public void onClick(View paramAnonymousView)
//      {
//        Daf.this.freqSlider.incrementProgressBy(1);
//      }
//    });
//    ((Button)findViewById(2131296259)).setOnClickListener(new View.OnClickListener()
//    {
//      public void onClick(View paramAnonymousView)
//      {
//        Daf.this.freqSlider.incrementProgressBy(-1);
//      }
//    });
    this.startBtn = ((Button)findViewById(R.id.startBtn));
    this.startBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Daf.this.start();
      }
    });
    this.stopBtn = ((Button)findViewById(R.id.stopBtn));
    this.stopBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Daf.this.stop();
      }
    });
    adjustScreen();
    restoreSettings();
    this.delaySlider.setProgress(this.ctrl.getDelay());
    this.freqSlider.setProgress(this.ctrl.getFreq());
    this.demo = new DafDemo(this);
  }
  
//  public boolean onCreateOptionsMenu(Menu paramMenu)
//  {
//    getMenuInflater().inflate(2131230720, paramMenu);
//    return true;
//  }
  
  protected void onDestroy()
  {
    stop();
    super.onDestroy();
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    }
    for (;;)
    {
      return true;
    }
  }
  
  protected void onResume()
  {
    restoreConfigurablePreferences();
    super.onResume();
  }
  
  public void showDialog(String paramString)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle("DAF Assistant");
    localBuilder.setMessage(paramString);
    localBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        paramAnonymousDialogInterface.dismiss();
      }
    });
    localBuilder.setCancelable(false);
    localBuilder.create().show();
  }
  
  public void stop()
  {
    try
    {
      if (this.ctrl.isRunning())
      {
        this.startBtn.setEnabled(true);
        this.stopBtn.setEnabled(false);
        this.ctrl.stop();
      }
      return;
    }
    catch (Exception localException) {}
  }
}
