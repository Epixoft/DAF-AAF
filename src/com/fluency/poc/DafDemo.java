package com.fluency.poc;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import java.util.Date;
import java.util.TimerTask;

public class DafDemo
{
  private static long DEMO_RUN_MSEC = 60000L;
  private static long DEMO_WAIT_MSEC = 60000L;
  private Daf daf;
  private Handler demoHandler;
  private Date demoResumeAt = new Date();
  private Date demoStartedAt = null;
  private TimerTask demoTimerTask;
  
  public DafDemo(final Daf paramDaf)
  {
    this.daf = paramDaf;
    this.demoHandler = new Handler();
    this.demoTimerTask = new TimerTask()
    {
      public void run()
      {
//        paramDaf.stop();
//        DafDemo.this.demoStartedAt = null;
//        DafDemo.this.demoResumeAt = new Date();
//        DafDemo.this.demoResumeAt.setTime(DafDemo.this.demoResumeAt.getTime() + DafDemo.DEMO_WAIT_MSEC);
//        DafDemo.this.showDemoDialog();
      }
    };
  }
  
  public boolean canStartDemo()
  {
//    if (this.demoStartedAt == null)
//    {
//      if (new Date().before(this.demoResumeAt)) {
//        return false;
//      }
//      this.demoStartedAt = new Date();
//      this.demoHandler.removeCallbacks(this.demoTimerTask);
//      this.demoHandler.postDelayed(this.demoTimerTask, DEMO_RUN_MSEC);
//    }
    return true;
  }
  
  public void showDemoDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.daf);
    localBuilder.setTitle("DAF Assistant Lite");
    localBuilder.setMessage("This is demo application of DAF Assistant. Please wait and continue after one minute. If you like the app please consider purchasing the full version of DAF Assistant.");
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
}
