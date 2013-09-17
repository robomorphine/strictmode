package android.app;

import android.content.ComponentName;
import android.content.ContentProviderNative;
import android.content.IContentProvider;
import android.content.IIntentReceiver;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.ProviderInfo;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug.MemoryInfo;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.StrictMode.ViolationInfo;
import java.util.List;

public abstract interface IActivityManager extends IInterface
{
  public static final String descriptor = "android.app.IActivityManager";
  public static final int START_RUNNING_TRANSACTION = 1;
  public static final int HANDLE_APPLICATION_CRASH_TRANSACTION = 2;
  public static final int START_ACTIVITY_TRANSACTION = 3;
  public static final int UNHANDLED_BACK_TRANSACTION = 4;
  public static final int OPEN_CONTENT_URI_TRANSACTION = 5;
  public static final int FINISH_ACTIVITY_TRANSACTION = 11;
  public static final int REGISTER_RECEIVER_TRANSACTION = 12;
  public static final int UNREGISTER_RECEIVER_TRANSACTION = 13;
  public static final int BROADCAST_INTENT_TRANSACTION = 14;
  public static final int UNBROADCAST_INTENT_TRANSACTION = 15;
  public static final int FINISH_RECEIVER_TRANSACTION = 16;
  public static final int ATTACH_APPLICATION_TRANSACTION = 17;
  public static final int ACTIVITY_IDLE_TRANSACTION = 18;
  public static final int ACTIVITY_PAUSED_TRANSACTION = 19;
  public static final int ACTIVITY_STOPPED_TRANSACTION = 20;
  public static final int GET_CALLING_PACKAGE_TRANSACTION = 21;
  public static final int GET_CALLING_ACTIVITY_TRANSACTION = 22;
  public static final int GET_TASKS_TRANSACTION = 23;
  public static final int MOVE_TASK_TO_FRONT_TRANSACTION = 24;
  public static final int MOVE_TASK_TO_BACK_TRANSACTION = 25;
  public static final int MOVE_TASK_BACKWARDS_TRANSACTION = 26;
  public static final int GET_TASK_FOR_ACTIVITY_TRANSACTION = 27;
  public static final int REPORT_THUMBNAIL_TRANSACTION = 28;
  public static final int GET_CONTENT_PROVIDER_TRANSACTION = 29;
  public static final int PUBLISH_CONTENT_PROVIDERS_TRANSACTION = 30;
  public static final int REF_CONTENT_PROVIDER_TRANSACTION = 31;
  public static final int FINISH_SUB_ACTIVITY_TRANSACTION = 32;
  public static final int GET_RUNNING_SERVICE_CONTROL_PANEL_TRANSACTION = 33;
  public static final int START_SERVICE_TRANSACTION = 34;
  public static final int STOP_SERVICE_TRANSACTION = 35;
  public static final int BIND_SERVICE_TRANSACTION = 36;
  public static final int UNBIND_SERVICE_TRANSACTION = 37;
  public static final int PUBLISH_SERVICE_TRANSACTION = 38;
  public static final int ACTIVITY_RESUMED_TRANSACTION = 39;
  public static final int GOING_TO_SLEEP_TRANSACTION = 40;
  public static final int WAKING_UP_TRANSACTION = 41;
  public static final int SET_DEBUG_APP_TRANSACTION = 42;
  public static final int SET_ALWAYS_FINISH_TRANSACTION = 43;
  public static final int START_INSTRUMENTATION_TRANSACTION = 44;
  public static final int FINISH_INSTRUMENTATION_TRANSACTION = 45;
  public static final int GET_CONFIGURATION_TRANSACTION = 46;
  public static final int UPDATE_CONFIGURATION_TRANSACTION = 47;
  public static final int STOP_SERVICE_TOKEN_TRANSACTION = 48;
  public static final int GET_ACTIVITY_CLASS_FOR_TOKEN_TRANSACTION = 49;
  public static final int GET_PACKAGE_FOR_TOKEN_TRANSACTION = 50;
  public static final int SET_PROCESS_LIMIT_TRANSACTION = 51;
  public static final int GET_PROCESS_LIMIT_TRANSACTION = 52;
  public static final int CHECK_PERMISSION_TRANSACTION = 53;
  public static final int CHECK_URI_PERMISSION_TRANSACTION = 54;
  public static final int GRANT_URI_PERMISSION_TRANSACTION = 55;
  public static final int REVOKE_URI_PERMISSION_TRANSACTION = 56;
  public static final int SET_ACTIVITY_CONTROLLER_TRANSACTION = 57;
  public static final int SHOW_WAITING_FOR_DEBUGGER_TRANSACTION = 58;
  public static final int SIGNAL_PERSISTENT_PROCESSES_TRANSACTION = 59;
  public static final int GET_RECENT_TASKS_TRANSACTION = 60;
  public static final int SERVICE_DONE_EXECUTING_TRANSACTION = 61;
  public static final int ACTIVITY_DESTROYED_TRANSACTION = 62;
  public static final int GET_INTENT_SENDER_TRANSACTION = 63;
  public static final int CANCEL_INTENT_SENDER_TRANSACTION = 64;
  public static final int GET_PACKAGE_FOR_INTENT_SENDER_TRANSACTION = 65;
  public static final int ENTER_SAFE_MODE_TRANSACTION = 66;
  public static final int START_NEXT_MATCHING_ACTIVITY_TRANSACTION = 67;
  public static final int NOTE_WAKEUP_ALARM_TRANSACTION = 68;
  public static final int REMOVE_CONTENT_PROVIDER_TRANSACTION = 69;
  public static final int SET_REQUESTED_ORIENTATION_TRANSACTION = 70;
  public static final int GET_REQUESTED_ORIENTATION_TRANSACTION = 71;
  public static final int UNBIND_FINISHED_TRANSACTION = 72;
  public static final int SET_PROCESS_FOREGROUND_TRANSACTION = 73;
  public static final int SET_SERVICE_FOREGROUND_TRANSACTION = 74;
  public static final int MOVE_ACTIVITY_TASK_TO_BACK_TRANSACTION = 75;
  public static final int GET_MEMORY_INFO_TRANSACTION = 76;
  public static final int GET_PROCESSES_IN_ERROR_STATE_TRANSACTION = 77;
  public static final int CLEAR_APP_DATA_TRANSACTION = 78;
  public static final int FORCE_STOP_PACKAGE_TRANSACTION = 79;
  public static final int KILL_PIDS_TRANSACTION = 80;
  public static final int GET_SERVICES_TRANSACTION = 81;
  public static final int GET_TASK_THUMBNAILS_TRANSACTION = 82;
  public static final int GET_RUNNING_APP_PROCESSES_TRANSACTION = 83;
  public static final int GET_DEVICE_CONFIGURATION_TRANSACTION = 84;
  public static final int PEEK_SERVICE_TRANSACTION = 85;
  public static final int PROFILE_CONTROL_TRANSACTION = 86;
  public static final int SHUTDOWN_TRANSACTION = 87;
  public static final int STOP_APP_SWITCHES_TRANSACTION = 88;
  public static final int RESUME_APP_SWITCHES_TRANSACTION = 89;
  public static final int START_BACKUP_AGENT_TRANSACTION = 90;
  public static final int BACKUP_AGENT_CREATED_TRANSACTION = 91;
  public static final int UNBIND_BACKUP_AGENT_TRANSACTION = 92;
  public static final int GET_UID_FOR_INTENT_SENDER_TRANSACTION = 93;
  public static final int HANDLE_INCOMING_USER_TRANSACTION = 94;
  public static final int GET_TASK_TOP_THUMBNAIL_TRANSACTION = 95;
  public static final int KILL_APPLICATION_WITH_APPID_TRANSACTION = 96;
  public static final int CLOSE_SYSTEM_DIALOGS_TRANSACTION = 97;
  public static final int GET_PROCESS_MEMORY_INFO_TRANSACTION = 98;
  public static final int KILL_APPLICATION_PROCESS_TRANSACTION = 99;
  public static final int START_ACTIVITY_INTENT_SENDER_TRANSACTION = 100;
  public static final int OVERRIDE_PENDING_TRANSITION_TRANSACTION = 101;
  public static final int HANDLE_APPLICATION_WTF_TRANSACTION = 102;
  public static final int KILL_BACKGROUND_PROCESSES_TRANSACTION = 103;
  public static final int IS_USER_A_MONKEY_TRANSACTION = 104;
  public static final int START_ACTIVITY_AND_WAIT_TRANSACTION = 105;
  public static final int WILL_ACTIVITY_BE_VISIBLE_TRANSACTION = 106;
  public static final int START_ACTIVITY_WITH_CONFIG_TRANSACTION = 107;
  public static final int GET_RUNNING_EXTERNAL_APPLICATIONS_TRANSACTION = 108;
  public static final int FINISH_HEAVY_WEIGHT_APP_TRANSACTION = 109;
  public static final int HANDLE_APPLICATION_STRICT_MODE_VIOLATION_TRANSACTION = 110;
  public static final int IS_IMMERSIVE_TRANSACTION = 111;
  public static final int SET_IMMERSIVE_TRANSACTION = 112;
  public static final int IS_TOP_ACTIVITY_IMMERSIVE_TRANSACTION = 113;
  public static final int CRASH_APPLICATION_TRANSACTION = 114;
  public static final int GET_PROVIDER_MIME_TYPE_TRANSACTION = 115;
  public static final int NEW_URI_PERMISSION_OWNER_TRANSACTION = 116;
  public static final int GRANT_URI_PERMISSION_FROM_OWNER_TRANSACTION = 117;
  public static final int REVOKE_URI_PERMISSION_FROM_OWNER_TRANSACTION = 118;
  public static final int CHECK_GRANT_URI_PERMISSION_TRANSACTION = 119;
  public static final int DUMP_HEAP_TRANSACTION = 120;
  public static final int START_ACTIVITIES_TRANSACTION = 121;
  public static final int IS_USER_RUNNING_TRANSACTION = 122;
  public static final int ACTIVITY_SLEPT_TRANSACTION = 123;
  public static final int GET_FRONT_ACTIVITY_SCREEN_COMPAT_MODE_TRANSACTION = 124;
  public static final int SET_FRONT_ACTIVITY_SCREEN_COMPAT_MODE_TRANSACTION = 125;
  public static final int GET_PACKAGE_SCREEN_COMPAT_MODE_TRANSACTION = 126;
  public static final int SET_PACKAGE_SCREEN_COMPAT_MODE_TRANSACTION = 127;
  public static final int GET_PACKAGE_ASK_SCREEN_COMPAT_TRANSACTION = 128;
  public static final int SET_PACKAGE_ASK_SCREEN_COMPAT_TRANSACTION = 129;
  public static final int SWITCH_USER_TRANSACTION = 130;
  public static final int REMOVE_SUB_TASK_TRANSACTION = 131;
  public static final int REMOVE_TASK_TRANSACTION = 132;
  public static final int REGISTER_PROCESS_OBSERVER_TRANSACTION = 133;
  public static final int UNREGISTER_PROCESS_OBSERVER_TRANSACTION = 134;
  public static final int IS_INTENT_SENDER_TARGETED_TO_PACKAGE_TRANSACTION = 135;
  public static final int UPDATE_PERSISTENT_CONFIGURATION_TRANSACTION = 136;
  public static final int GET_PROCESS_PSS_TRANSACTION = 137;
  public static final int SHOW_BOOT_MESSAGE_TRANSACTION = 138;
  public static final int DISMISS_KEYGUARD_ON_NEXT_ACTIVITY_TRANSACTION = 139;
  public static final int KILL_ALL_BACKGROUND_PROCESSES_TRANSACTION = 140;
  public static final int GET_CONTENT_PROVIDER_EXTERNAL_TRANSACTION = 141;
  public static final int REMOVE_CONTENT_PROVIDER_EXTERNAL_TRANSACTION = 142;
  public static final int GET_MY_MEMORY_STATE_TRANSACTION = 143;
  public static final int KILL_PROCESSES_BELOW_FOREGROUND_TRANSACTION = 144;
  public static final int GET_CURRENT_USER_TRANSACTION = 145;
  public static final int TARGET_TASK_AFFINITY_MATCHES_ACTIVITY_TRANSACTION = 146;
  public static final int NAVIGATE_UP_TO_TRANSACTION = 147;
  public static final int SET_LOCK_SCREEN_SHOWN_TRANSACTION = 148;
  public static final int FINISH_ACTIVITY_AFFINITY_TRANSACTION = 149;
  public static final int GET_LAUNCHED_FROM_UID_TRANSACTION = 150;
  public static final int UNSTABLE_PROVIDER_DIED_TRANSACTION = 151;
  public static final int IS_INTENT_SENDER_AN_ACTIVITY_TRANSACTION = 152;
  public static final int START_ACTIVITY_AS_USER_TRANSACTION = 153;
  public static final int STOP_USER_TRANSACTION = 154;
  public static final int REGISTER_USER_SWITCH_OBSERVER_TRANSACTION = 155;
  public static final int UNREGISTER_USER_SWITCH_OBSERVER_TRANSACTION = 156;
  public static final int GET_RUNNING_USER_IDS_TRANSACTION = 157;
  public static final int REQUEST_BUG_REPORT_TRANSACTION = 158;
  public static final int INPUT_DISPATCHING_TIMED_OUT_TRANSACTION = 159;
  public static final int CLEAR_PENDING_BACKUP_TRANSACTION = 160;
  public static final int GET_INTENT_FOR_INTENT_SENDER_TRANSACTION = 161;
  public static final int GET_TOP_ACTIVITY_EXTRAS_TRANSACTION = 162;
  public static final int REPORT_TOP_ACTIVITY_EXTRAS_TRANSACTION = 163;
  public static final int GET_LAUNCHED_FROM_PACKAGE_TRANSACTION = 164;
  public static final int KILL_UID_TRANSACTION = 165;
  public static final int SET_USER_IS_MONKEY_TRANSACTION = 166;
  public static final int HANG_TRANSACTION = 167;

  public abstract int startActivity(IApplicationThread paramIApplicationThread, String paramString1, Intent paramIntent, String paramString2, IBinder paramIBinder, String paramString3, int paramInt1, int paramInt2, String paramString4, ParcelFileDescriptor paramParcelFileDescriptor, Bundle paramBundle)
      throws RemoteException;

  public abstract int startActivityAsUser(IApplicationThread paramIApplicationThread, String paramString1, Intent paramIntent, String paramString2, IBinder paramIBinder, String paramString3, int paramInt1, int paramInt2, String paramString4, ParcelFileDescriptor paramParcelFileDescriptor, Bundle paramBundle, int paramInt3)
      throws RemoteException;

  public abstract WaitResult startActivityAndWait(IApplicationThread paramIApplicationThread, String paramString1, Intent paramIntent, String paramString2, IBinder paramIBinder, String paramString3, int paramInt1, int paramInt2, String paramString4, ParcelFileDescriptor paramParcelFileDescriptor, Bundle paramBundle, int paramInt3)
      throws RemoteException;

  public abstract int startActivityWithConfig(IApplicationThread paramIApplicationThread, String paramString1, Intent paramIntent, String paramString2, IBinder paramIBinder, String paramString3, int paramInt1, int paramInt2, Configuration paramConfiguration, Bundle paramBundle, int paramInt3)
      throws RemoteException;

  public abstract int startActivityIntentSender(IApplicationThread paramIApplicationThread, IntentSender paramIntentSender, Intent paramIntent, String paramString1, IBinder paramIBinder, String paramString2, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle)
      throws RemoteException;

  public abstract boolean startNextMatchingActivity(IBinder paramIBinder, Intent paramIntent, Bundle paramBundle)
      throws RemoteException;

  public abstract boolean finishActivity(IBinder paramIBinder, int paramInt, Intent paramIntent)
      throws RemoteException;

  public abstract void finishSubActivity(IBinder paramIBinder, String paramString, int paramInt)
      throws RemoteException;

  public abstract boolean finishActivityAffinity(IBinder paramIBinder)
      throws RemoteException;

  public abstract boolean willActivityBeVisible(IBinder paramIBinder)
      throws RemoteException;

  public abstract Intent registerReceiver(IApplicationThread paramIApplicationThread, String paramString1, IIntentReceiver paramIIntentReceiver, IntentFilter paramIntentFilter, String paramString2, int paramInt)
      throws RemoteException;

  public abstract void unregisterReceiver(IIntentReceiver paramIIntentReceiver)
      throws RemoteException;

  public abstract int broadcastIntent(IApplicationThread paramIApplicationThread, Intent paramIntent, String paramString1, IIntentReceiver paramIIntentReceiver, int paramInt1, String paramString2, Bundle paramBundle, String paramString3, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, int paramInt3)
      throws RemoteException;

  public abstract void unbroadcastIntent(IApplicationThread paramIApplicationThread, Intent paramIntent, int paramInt)
      throws RemoteException;

  public abstract void finishReceiver(IBinder paramIBinder, int paramInt, String paramString, Bundle paramBundle, boolean paramBoolean)
      throws RemoteException;

  public abstract void attachApplication(IApplicationThread paramIApplicationThread)
      throws RemoteException;

  public abstract void activityResumed(IBinder paramIBinder)
      throws RemoteException;

  public abstract void activityIdle(IBinder paramIBinder, Configuration paramConfiguration, boolean paramBoolean)
      throws RemoteException;

  public abstract void activityPaused(IBinder paramIBinder)
      throws RemoteException;

  public abstract void activityStopped(IBinder paramIBinder, Bundle paramBundle, Bitmap paramBitmap, CharSequence paramCharSequence)
      throws RemoteException;

  public abstract void activitySlept(IBinder paramIBinder)
      throws RemoteException;

  public abstract void activityDestroyed(IBinder paramIBinder)
      throws RemoteException;

  public abstract String getCallingPackage(IBinder paramIBinder)
      throws RemoteException;

  public abstract ComponentName getCallingActivity(IBinder paramIBinder)
      throws RemoteException;

  public abstract List getTasks(int paramInt1, int paramInt2, IThumbnailReceiver paramIThumbnailReceiver)
      throws RemoteException;

  public abstract List<ActivityManager.RecentTaskInfo> getRecentTasks(int paramInt1, int paramInt2, int paramInt3)
      throws RemoteException;

  public abstract ActivityManager.TaskThumbnails getTaskThumbnails(int paramInt)
      throws RemoteException;

  public abstract Bitmap getTaskTopThumbnail(int paramInt)
      throws RemoteException;

  public abstract List getServices(int paramInt1, int paramInt2)
      throws RemoteException;

  public abstract List<ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState()
      throws RemoteException;

  public abstract void moveTaskToFront(int paramInt1, int paramInt2, Bundle paramBundle)
      throws RemoteException;

  public abstract void moveTaskToBack(int paramInt)
      throws RemoteException;

  public abstract boolean moveActivityTaskToBack(IBinder paramIBinder, boolean paramBoolean)
      throws RemoteException;

  public abstract void moveTaskBackwards(int paramInt)
      throws RemoteException;

  public abstract int getTaskForActivity(IBinder paramIBinder, boolean paramBoolean)
      throws RemoteException;

  public abstract void reportThumbnail(IBinder paramIBinder, Bitmap paramBitmap, CharSequence paramCharSequence)
      throws RemoteException;

  public abstract ContentProviderHolder getContentProvider(IApplicationThread paramIApplicationThread, String paramString, int paramInt, boolean paramBoolean)
      throws RemoteException;

  public abstract ContentProviderHolder getContentProviderExternal(String paramString, int paramInt, IBinder paramIBinder)
      throws RemoteException;

  public abstract void removeContentProvider(IBinder paramIBinder, boolean paramBoolean)
      throws RemoteException;

  public abstract void removeContentProviderExternal(String paramString, IBinder paramIBinder)
      throws RemoteException;

  public abstract void publishContentProviders(IApplicationThread paramIApplicationThread, List<ContentProviderHolder> paramList)
      throws RemoteException;

  public abstract boolean refContentProvider(IBinder paramIBinder, int paramInt1, int paramInt2)
      throws RemoteException;

  public abstract void unstableProviderDied(IBinder paramIBinder)
      throws RemoteException;

  public abstract PendingIntent getRunningServiceControlPanel(ComponentName paramComponentName)
      throws RemoteException;

  public abstract ComponentName startService(IApplicationThread paramIApplicationThread, Intent paramIntent, String paramString, int paramInt)
      throws RemoteException;

  public abstract int stopService(IApplicationThread paramIApplicationThread, Intent paramIntent, String paramString, int paramInt)
      throws RemoteException;

  public abstract boolean stopServiceToken(ComponentName paramComponentName, IBinder paramIBinder, int paramInt)
      throws RemoteException;

  public abstract void setServiceForeground(ComponentName paramComponentName, IBinder paramIBinder, int paramInt, Notification paramNotification, boolean paramBoolean)
      throws RemoteException;

  public abstract int bindService(IApplicationThread paramIApplicationThread, IBinder paramIBinder, Intent paramIntent, String paramString, IServiceConnection paramIServiceConnection, int paramInt1, int paramInt2)
      throws RemoteException;

  public abstract boolean unbindService(IServiceConnection paramIServiceConnection)
      throws RemoteException;

  public abstract void publishService(IBinder paramIBinder1, Intent paramIntent, IBinder paramIBinder2)
      throws RemoteException;

  public abstract void unbindFinished(IBinder paramIBinder, Intent paramIntent, boolean paramBoolean)
      throws RemoteException;

  public abstract void serviceDoneExecuting(IBinder paramIBinder, int paramInt1, int paramInt2, int paramInt3)
      throws RemoteException;

  public abstract IBinder peekService(Intent paramIntent, String paramString)
      throws RemoteException;

  public abstract boolean bindBackupAgent(ApplicationInfo paramApplicationInfo, int paramInt)
      throws RemoteException;

  public abstract void clearPendingBackup()
      throws RemoteException;

  public abstract void backupAgentCreated(String paramString, IBinder paramIBinder)
      throws RemoteException;

  public abstract void unbindBackupAgent(ApplicationInfo paramApplicationInfo)
      throws RemoteException;

  public abstract void killApplicationProcess(String paramString, int paramInt)
      throws RemoteException;

  public abstract boolean startInstrumentation(ComponentName paramComponentName, String paramString, int paramInt1, Bundle paramBundle, IInstrumentationWatcher paramIInstrumentationWatcher, IUiAutomationConnection paramIUiAutomationConnection, int paramInt2)
      throws RemoteException;

  public abstract void finishInstrumentation(IApplicationThread paramIApplicationThread, int paramInt, Bundle paramBundle)
      throws RemoteException;

  public abstract Configuration getConfiguration()
      throws RemoteException;

  public abstract void updateConfiguration(Configuration paramConfiguration)
      throws RemoteException;

  public abstract void setRequestedOrientation(IBinder paramIBinder, int paramInt)
      throws RemoteException;

  public abstract int getRequestedOrientation(IBinder paramIBinder)
      throws RemoteException;

  public abstract ComponentName getActivityClassForToken(IBinder paramIBinder)
      throws RemoteException;

  public abstract String getPackageForToken(IBinder paramIBinder)
      throws RemoteException;

  public abstract IIntentSender getIntentSender(int paramInt1, String paramString1, IBinder paramIBinder, String paramString2, int paramInt2, Intent[] paramArrayOfIntent, String[] paramArrayOfString, int paramInt3, Bundle paramBundle, int paramInt4)
      throws RemoteException;

  public abstract void cancelIntentSender(IIntentSender paramIIntentSender)
      throws RemoteException;

  public abstract boolean clearApplicationUserData(String paramString, IPackageDataObserver paramIPackageDataObserver, int paramInt)
      throws RemoteException;

  public abstract String getPackageForIntentSender(IIntentSender paramIIntentSender)
      throws RemoteException;

  public abstract int getUidForIntentSender(IIntentSender paramIIntentSender)
      throws RemoteException;

  public abstract int handleIncomingUser(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, String paramString1, String paramString2)
      throws RemoteException;

  public abstract void setProcessLimit(int paramInt)
      throws RemoteException;

  public abstract int getProcessLimit()
      throws RemoteException;

  public abstract void setProcessForeground(IBinder paramIBinder, int paramInt, boolean paramBoolean)
      throws RemoteException;

  public abstract int checkPermission(String paramString, int paramInt1, int paramInt2)
      throws RemoteException;

  public abstract int checkUriPermission(Uri paramUri, int paramInt1, int paramInt2, int paramInt3)
      throws RemoteException;

  public abstract void grantUriPermission(IApplicationThread paramIApplicationThread, String paramString, Uri paramUri, int paramInt)
      throws RemoteException;

  public abstract void revokeUriPermission(IApplicationThread paramIApplicationThread, Uri paramUri, int paramInt)
      throws RemoteException;

  public abstract void showWaitingForDebugger(IApplicationThread paramIApplicationThread, boolean paramBoolean)
      throws RemoteException;

  public abstract void getMemoryInfo(ActivityManager.MemoryInfo paramMemoryInfo)
      throws RemoteException;

  public abstract void killBackgroundProcesses(String paramString, int paramInt)
      throws RemoteException;

  public abstract void killAllBackgroundProcesses()
      throws RemoteException;

  public abstract void forceStopPackage(String paramString, int paramInt)
      throws RemoteException;

  public abstract void goingToSleep()
      throws RemoteException;

  public abstract void wakingUp()
      throws RemoteException;

  public abstract void setLockScreenShown(boolean paramBoolean)
      throws RemoteException;

  public abstract void unhandledBack()
      throws RemoteException;

  public abstract ParcelFileDescriptor openContentUri(Uri paramUri)
      throws RemoteException;

  public abstract void setDebugApp(String paramString, boolean paramBoolean1, boolean paramBoolean2)
      throws RemoteException;

  public abstract void setAlwaysFinish(boolean paramBoolean)
      throws RemoteException;

  public abstract void setActivityController(IActivityController paramIActivityController)
      throws RemoteException;

  public abstract void enterSafeMode()
      throws RemoteException;

  public abstract void noteWakeupAlarm(IIntentSender paramIIntentSender)
      throws RemoteException;

  public abstract boolean killPids(int[] paramArrayOfInt, String paramString, boolean paramBoolean)
      throws RemoteException;

  public abstract boolean killProcessesBelowForeground(String paramString)
      throws RemoteException;

  public abstract void startRunning(String paramString1, String paramString2, String paramString3, String paramString4)
      throws RemoteException;

  public abstract void handleApplicationCrash(IBinder paramIBinder, ApplicationErrorReport.CrashInfo paramCrashInfo)
      throws RemoteException;

  public abstract boolean handleApplicationWtf(IBinder paramIBinder, String paramString, ApplicationErrorReport.CrashInfo paramCrashInfo)
      throws RemoteException;

  public abstract void handleApplicationStrictModeViolation(IBinder paramIBinder, int paramInt, StrictMode.ViolationInfo paramViolationInfo)
      throws RemoteException;

  public abstract void signalPersistentProcesses(int paramInt)
      throws RemoteException;

  public abstract List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses()
      throws RemoteException;

  public abstract List<ApplicationInfo> getRunningExternalApplications()
      throws RemoteException;

  public abstract void getMyMemoryState(ActivityManager.RunningAppProcessInfo paramRunningAppProcessInfo)
      throws RemoteException;

  public abstract ConfigurationInfo getDeviceConfigurationInfo()
      throws RemoteException;

  public abstract boolean profileControl(String paramString1, int paramInt1, boolean paramBoolean, String paramString2, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt2)
      throws RemoteException;

  public abstract boolean shutdown(int paramInt)
      throws RemoteException;

  public abstract void stopAppSwitches()
      throws RemoteException;

  public abstract void resumeAppSwitches()
      throws RemoteException;

  public abstract void killApplicationWithAppId(String paramString, int paramInt)
      throws RemoteException;

  public abstract void closeSystemDialogs(String paramString)
      throws RemoteException;

  public abstract Debug.MemoryInfo[] getProcessMemoryInfo(int[] paramArrayOfInt)
      throws RemoteException;

  public abstract void overridePendingTransition(IBinder paramIBinder, String paramString, int paramInt1, int paramInt2)
      throws RemoteException;

  public abstract boolean isUserAMonkey()
      throws RemoteException;

  public abstract void setUserIsMonkey(boolean paramBoolean)
      throws RemoteException;

  public abstract void finishHeavyWeightApp()
      throws RemoteException;

  public abstract void setImmersive(IBinder paramIBinder, boolean paramBoolean)
      throws RemoteException;

  public abstract boolean isImmersive(IBinder paramIBinder)
      throws RemoteException;

  public abstract boolean isTopActivityImmersive()
      throws RemoteException;

  public abstract void crashApplication(int paramInt1, int paramInt2, String paramString1, String paramString2)
      throws RemoteException;

  public abstract String getProviderMimeType(Uri paramUri, int paramInt)
      throws RemoteException;

  public abstract IBinder newUriPermissionOwner(String paramString)
      throws RemoteException;

  public abstract void grantUriPermissionFromOwner(IBinder paramIBinder, int paramInt1, String paramString, Uri paramUri, int paramInt2)
      throws RemoteException;

  public abstract void revokeUriPermissionFromOwner(IBinder paramIBinder, Uri paramUri, int paramInt)
      throws RemoteException;

  public abstract int checkGrantUriPermission(int paramInt1, String paramString, Uri paramUri, int paramInt2)
      throws RemoteException;

  public abstract boolean dumpHeap(String paramString1, int paramInt, boolean paramBoolean, String paramString2, ParcelFileDescriptor paramParcelFileDescriptor)
      throws RemoteException;

  public abstract int startActivities(IApplicationThread paramIApplicationThread, String paramString, Intent[] paramArrayOfIntent, String[] paramArrayOfString, IBinder paramIBinder, Bundle paramBundle, int paramInt)
      throws RemoteException;

  public abstract int getFrontActivityScreenCompatMode()
      throws RemoteException;

  public abstract void setFrontActivityScreenCompatMode(int paramInt)
      throws RemoteException;

  public abstract int getPackageScreenCompatMode(String paramString)
      throws RemoteException;

  public abstract void setPackageScreenCompatMode(String paramString, int paramInt)
      throws RemoteException;

  public abstract boolean getPackageAskScreenCompat(String paramString)
      throws RemoteException;

  public abstract void setPackageAskScreenCompat(String paramString, boolean paramBoolean)
      throws RemoteException;

  public abstract boolean switchUser(int paramInt)
      throws RemoteException;

  public abstract int stopUser(int paramInt, IStopUserCallback paramIStopUserCallback)
      throws RemoteException;

  public abstract UserInfo getCurrentUser()
      throws RemoteException;

  public abstract boolean isUserRunning(int paramInt, boolean paramBoolean)
      throws RemoteException;

  public abstract int[] getRunningUserIds()
      throws RemoteException;

  public abstract boolean removeSubTask(int paramInt1, int paramInt2)
      throws RemoteException;

  public abstract boolean removeTask(int paramInt1, int paramInt2)
      throws RemoteException;

  public abstract void registerProcessObserver(IProcessObserver paramIProcessObserver)
      throws RemoteException;

  public abstract void unregisterProcessObserver(IProcessObserver paramIProcessObserver)
      throws RemoteException;

  public abstract boolean isIntentSenderTargetedToPackage(IIntentSender paramIIntentSender)
      throws RemoteException;

  public abstract boolean isIntentSenderAnActivity(IIntentSender paramIIntentSender)
      throws RemoteException;

  public abstract Intent getIntentForIntentSender(IIntentSender paramIIntentSender)
      throws RemoteException;

  public abstract void updatePersistentConfiguration(Configuration paramConfiguration)
      throws RemoteException;

  public abstract long[] getProcessPss(int[] paramArrayOfInt)
      throws RemoteException;

  public abstract void showBootMessage(CharSequence paramCharSequence, boolean paramBoolean)
      throws RemoteException;

  public abstract void dismissKeyguardOnNextActivity()
      throws RemoteException;

  public abstract boolean targetTaskAffinityMatchesActivity(IBinder paramIBinder, String paramString)
      throws RemoteException;

  public abstract boolean navigateUpTo(IBinder paramIBinder, Intent paramIntent1, int paramInt, Intent paramIntent2)
      throws RemoteException;

  public abstract int getLaunchedFromUid(IBinder paramIBinder)
      throws RemoteException;

  public abstract String getLaunchedFromPackage(IBinder paramIBinder)
      throws RemoteException;

  public abstract void registerUserSwitchObserver(IUserSwitchObserver paramIUserSwitchObserver)
      throws RemoteException;

  public abstract void unregisterUserSwitchObserver(IUserSwitchObserver paramIUserSwitchObserver)
      throws RemoteException;

  public abstract void requestBugReport()
      throws RemoteException;

  public abstract long inputDispatchingTimedOut(int paramInt, boolean paramBoolean)
      throws RemoteException;

  public abstract Bundle getTopActivityExtras(int paramInt)
      throws RemoteException;

  public abstract void reportTopActivityExtras(IBinder paramIBinder, Bundle paramBundle)
      throws RemoteException;

  public abstract void killUid(int paramInt, String paramString)
      throws RemoteException;

  public abstract void hang(IBinder paramIBinder, boolean paramBoolean)
      throws RemoteException;

  public abstract boolean testIsSystemReady();

  public static class WaitResult
      implements Parcelable
  {
    public int result;
    public boolean timeout;
    public ComponentName who;
    public long thisTime;
    public long totalTime;
    public static final Parcelable.Creator<WaitResult> CREATOR = new Parcelable.Creator()
    {
      public IActivityManager.WaitResult createFromParcel(Parcel source) {
        return new IActivityManager.WaitResult(source, null);
      }

      public IActivityManager.WaitResult[] newArray(int size) {
        return new IActivityManager.WaitResult[size];
      }
    };

    public WaitResult()
    {
    }

    public int describeContents()
    {
      return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.result);
      dest.writeInt(this.timeout ? 1 : 0);
      ComponentName.writeToParcel(this.who, dest);
      dest.writeLong(this.thisTime);
      dest.writeLong(this.totalTime);
    }

    private WaitResult(Parcel source)
    {
      this.result = source.readInt();
      this.timeout = (source.readInt() != 0);
      this.who = ComponentName.readFromParcel(source);
      this.thisTime = source.readLong();
      this.totalTime = source.readLong();
    }
  }

  public static class ContentProviderHolder
      implements Parcelable
  {
    public final ProviderInfo info;
    public IContentProvider provider;
    public IBinder connection;
    public boolean noReleaseNeeded;
    public static final Parcelable.Creator<ContentProviderHolder> CREATOR = new Parcelable.Creator()
    {
      public IActivityManager.ContentProviderHolder createFromParcel(Parcel source) {
        return new IActivityManager.ContentProviderHolder(source, null);
      }

      public IActivityManager.ContentProviderHolder[] newArray(int size) {
        return new IActivityManager.ContentProviderHolder[size];
      }
    };

    public ContentProviderHolder(ProviderInfo _info)
    {
      this.info = _info;
    }

    public int describeContents() {
      return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
      this.info.writeToParcel(dest, 0);
      if (this.provider != null)
        dest.writeStrongBinder(this.provider.asBinder());
      else {
        dest.writeStrongBinder(null);
      }
      dest.writeStrongBinder(this.connection);
      dest.writeInt(this.noReleaseNeeded ? 1 : 0);
    }

    private ContentProviderHolder(Parcel source)
    {
      this.info = ((ProviderInfo)ProviderInfo.CREATOR.createFromParcel(source));
      this.provider = ContentProviderNative.asInterface(source.readStrongBinder());

      this.connection = source.readStrongBinder();
      this.noReleaseNeeded = (source.readInt() != 0);
    }
  }
}