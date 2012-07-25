package android.app;

import com.robomorphine.strictmode.DataProxy;

import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.ProcessErrorStateInfo;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.TaskThumbnails;
import android.app.ApplicationErrorReport.CrashInfo;
import android.content.ComponentName;
import android.content.IIntentReceiver;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.StrictMode.ViolationInfo;

import java.util.List;

@SuppressWarnings("all")
public class IActivityManagerProxyR15 implements IActivityManager {
    
    private final IActivityManager mTo;
    private final DataProxy<ViolationInfo> mViolationProxy;
        
    public IActivityManagerProxyR15(IActivityManager manager, DataProxy<ViolationInfo> violationProxy) {
        mTo = manager;
        mViolationProxy = violationProxy;
    }
    
    public void handleApplicationStrictModeViolation(IBinder binder, int mask, ViolationInfo info)
            throws RemoteException {
        mTo.handleApplicationStrictModeViolation(binder, mask, mViolationProxy.handle(info));
    }
    
    public void activityDestroyed(IBinder arg0) throws RemoteException {
        mTo.activityDestroyed(arg0);
    }

    public void activityIdle(IBinder arg0, Configuration arg1, boolean arg2) throws RemoteException {
        mTo.activityIdle(arg0, arg1, arg2);
    }

    public void activityPaused(IBinder arg0) throws RemoteException {
        mTo.activityPaused(arg0);
    }

    public void activitySlept(IBinder arg0) throws RemoteException {
        mTo.activitySlept(arg0);
    }

    public void activityStopped(IBinder arg0, Bundle arg1, Bitmap arg2, CharSequence arg3)
            throws RemoteException {
        mTo.activityStopped(arg0, arg1, arg2, arg3);
    }

    public IBinder asBinder() {
        return mTo.asBinder();
    }

    public void attachApplication(IApplicationThread arg0) throws RemoteException {
        mTo.attachApplication(arg0);
    }

    public void backupAgentCreated(String arg0, IBinder arg1) throws RemoteException {
        mTo.backupAgentCreated(arg0, arg1);
    }

    public boolean bindBackupAgent(ApplicationInfo arg0, int arg1) throws RemoteException {
        return mTo.bindBackupAgent(arg0, arg1);
    }

    public int bindService(IApplicationThread arg0, IBinder arg1, Intent arg2, String arg3,
            IServiceConnection arg4, int arg5) throws RemoteException {
        return mTo.bindService(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    public int broadcastIntent(IApplicationThread arg0, Intent arg1, String arg2,
            IIntentReceiver arg3, int arg4, String arg5, Bundle arg6, String arg7, boolean arg8,
            boolean arg9) throws RemoteException {
        return mTo.broadcastIntent(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    }

    public void cancelIntentSender(IIntentSender arg0) throws RemoteException {
        mTo.cancelIntentSender(arg0);
    }

    public int checkGrantUriPermission(int arg0, String arg1, Uri arg2, int arg3)
            throws RemoteException {
        return mTo.checkGrantUriPermission(arg0, arg1, arg2, arg3);
    }

    public int checkPermission(String arg0, int arg1, int arg2) throws RemoteException {
        return mTo.checkPermission(arg0, arg1, arg2);
    }

    public int checkUriPermission(Uri arg0, int arg1, int arg2, int arg3) throws RemoteException {
        return mTo.checkUriPermission(arg0, arg1, arg2, arg3);
    }

    public boolean clearApplicationUserData(String arg0, IPackageDataObserver arg1)
            throws RemoteException {
        return mTo.clearApplicationUserData(arg0, arg1);
    }

    public void closeSystemDialogs(String arg0) throws RemoteException {
        mTo.closeSystemDialogs(arg0);
    }

    public void crashApplication(int arg0, int arg1, String arg2, String arg3)
            throws RemoteException {
        mTo.crashApplication(arg0, arg1, arg2, arg3);
    }

    public void dismissKeyguardOnNextActivity() throws RemoteException {
        mTo.dismissKeyguardOnNextActivity();
    }

    public boolean dumpHeap(String arg0, boolean arg1, String arg2, ParcelFileDescriptor arg3)
            throws RemoteException {
        return mTo.dumpHeap(arg0, arg1, arg2, arg3);
    }

    public void enterSafeMode() throws RemoteException {
        mTo.enterSafeMode();
    }

    public boolean finishActivity(IBinder arg0, int arg1, Intent arg2) throws RemoteException {
        return mTo.finishActivity(arg0, arg1, arg2);
    }

    public void finishHeavyWeightApp() throws RemoteException {
        mTo.finishHeavyWeightApp();
    }

    public void finishInstrumentation(IApplicationThread arg0, int arg1, Bundle arg2)
            throws RemoteException {
        mTo.finishInstrumentation(arg0, arg1, arg2);
    }

    public void finishOtherInstances(IBinder arg0, ComponentName arg1) throws RemoteException {
        mTo.finishOtherInstances(arg0, arg1);
    }

    public void finishReceiver(IBinder arg0, int arg1, String arg2, Bundle arg3, boolean arg4)
            throws RemoteException {
        mTo.finishReceiver(arg0, arg1, arg2, arg3, arg4);
    }

    public void finishSubActivity(IBinder arg0, String arg1, int arg2) throws RemoteException {
        mTo.finishSubActivity(arg0, arg1, arg2);
    }

    public void forceStopPackage(String arg0) throws RemoteException {
        mTo.forceStopPackage(arg0);
    }

    public ComponentName getActivityClassForToken(IBinder arg0) throws RemoteException {
        return mTo.getActivityClassForToken(arg0);
    }

    public ComponentName getCallingActivity(IBinder arg0) throws RemoteException {
        return mTo.getCallingActivity(arg0);
    }

    public String getCallingPackage(IBinder arg0) throws RemoteException {
        return mTo.getCallingPackage(arg0);
    }

    public Configuration getConfiguration() throws RemoteException {
        return mTo.getConfiguration();
    }

    public ContentProviderHolder getContentProvider(IApplicationThread arg0, String arg1)
            throws RemoteException {
        return mTo.getContentProvider(arg0, arg1);
    }

    public ConfigurationInfo getDeviceConfigurationInfo() throws RemoteException {
        return mTo.getDeviceConfigurationInfo();
    }

    public int getFrontActivityScreenCompatMode() throws RemoteException {
        return mTo.getFrontActivityScreenCompatMode();
    }

    public IIntentSender getIntentSender(int arg0, String arg1, IBinder arg2, String arg3,
            int arg4, Intent[] arg5, String[] arg6, int arg7) throws RemoteException {
        return mTo.getIntentSender(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    }

    public void getMemoryInfo(MemoryInfo arg0) throws RemoteException {
        mTo.getMemoryInfo(arg0);
    }

    public boolean getPackageAskScreenCompat(String arg0) throws RemoteException {
        return mTo.getPackageAskScreenCompat(arg0);
    }

    public String getPackageForIntentSender(IIntentSender arg0) throws RemoteException {
        return mTo.getPackageForIntentSender(arg0);
    }

    public String getPackageForToken(IBinder arg0) throws RemoteException {
        return mTo.getPackageForToken(arg0);
    }

    public int getPackageScreenCompatMode(String arg0) throws RemoteException {
        return mTo.getPackageScreenCompatMode(arg0);
    }

    public int getProcessLimit() throws RemoteException {
        return mTo.getProcessLimit();
    }

    public android.os.Debug.MemoryInfo[] getProcessMemoryInfo(int[] arg0) throws RemoteException {
        return mTo.getProcessMemoryInfo(arg0);
    }

    public long[] getProcessPss(int[] arg0) throws RemoteException {
        return mTo.getProcessPss(arg0);
    }

    public List<ProcessErrorStateInfo> getProcessesInErrorState() throws RemoteException {
        return mTo.getProcessesInErrorState();
    }

    public String getProviderMimeType(Uri arg0) throws RemoteException {
        return mTo.getProviderMimeType(arg0);
    }

    public List<RecentTaskInfo> getRecentTasks(int arg0, int arg1) throws RemoteException {
        return mTo.getRecentTasks(arg0, arg1);
    }

    public int getRequestedOrientation(IBinder arg0) throws RemoteException {
        return mTo.getRequestedOrientation(arg0);
    }

    public List<RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException {
        return mTo.getRunningAppProcesses();
    }

    public List<ApplicationInfo> getRunningExternalApplications() throws RemoteException {
        return mTo.getRunningExternalApplications();
    }

    public PendingIntent getRunningServiceControlPanel(ComponentName arg0) throws RemoteException {
        return mTo.getRunningServiceControlPanel(arg0);
    }

    public List getServices(int arg0, int arg1) throws RemoteException {
        return mTo.getServices(arg0, arg1);
    }

    public int getTaskForActivity(IBinder arg0, boolean arg1) throws RemoteException {
        return mTo.getTaskForActivity(arg0, arg1);
    }

    public TaskThumbnails getTaskThumbnails(int arg0) throws RemoteException {
        return mTo.getTaskThumbnails(arg0);
    }

    public List getTasks(int arg0, int arg1, IThumbnailReceiver arg2) throws RemoteException {
        return mTo.getTasks(arg0, arg1, arg2);
    }

    public void goingToSleep() throws RemoteException {
        mTo.goingToSleep();
    }

    public void grantUriPermission(IApplicationThread arg0, String arg1, Uri arg2, int arg3)
            throws RemoteException {
        mTo.grantUriPermission(arg0, arg1, arg2, arg3);
    }

    public void grantUriPermissionFromOwner(IBinder arg0, int arg1, String arg2, Uri arg3, int arg4)
            throws RemoteException {
        mTo.grantUriPermissionFromOwner(arg0, arg1, arg2, arg3, arg4);
    }

    public void handleApplicationCrash(IBinder arg0, CrashInfo arg1) throws RemoteException {
        mTo.handleApplicationCrash(arg0, arg1);
    }   

    public boolean handleApplicationWtf(IBinder arg0, String arg1, CrashInfo arg2)
            throws RemoteException {
        return mTo.handleApplicationWtf(arg0, arg1, arg2);
    }

    public boolean isImmersive(IBinder arg0) throws RemoteException {
        return mTo.isImmersive(arg0);
    }

    public boolean isIntentSenderTargetedToPackage(IIntentSender arg0) throws RemoteException {
        return mTo.isIntentSenderTargetedToPackage(arg0);
    }

    public boolean isTopActivityImmersive() throws RemoteException {
        return mTo.isTopActivityImmersive();
    }

    public boolean isUserAMonkey() throws RemoteException {
        return mTo.isUserAMonkey();
    }

    public void killAllBackgroundProcesses() throws RemoteException {
        mTo.killAllBackgroundProcesses();
    }

    public void killApplicationProcess(String arg0, int arg1) throws RemoteException {
        mTo.killApplicationProcess(arg0, arg1);
    }

    public void killApplicationWithUid(String arg0, int arg1) throws RemoteException {
        mTo.killApplicationWithUid(arg0, arg1);
    }

    public void killBackgroundProcesses(String arg0) throws RemoteException {
        mTo.killBackgroundProcesses(arg0);
    }

    public boolean killPids(int[] arg0, String arg1, boolean arg2) throws RemoteException {
        return mTo.killPids(arg0, arg1, arg2);
    }

    public boolean moveActivityTaskToBack(IBinder arg0, boolean arg1) throws RemoteException {
        return mTo.moveActivityTaskToBack(arg0, arg1);
    }

    public void moveTaskBackwards(int arg0) throws RemoteException {
        mTo.moveTaskBackwards(arg0);
    }

    public void moveTaskToBack(int arg0) throws RemoteException {
        mTo.moveTaskToBack(arg0);
    }

    public void moveTaskToFront(int arg0, int arg1) throws RemoteException {
        mTo.moveTaskToFront(arg0, arg1);
    }

    public IBinder newUriPermissionOwner(String arg0) throws RemoteException {
        return mTo.newUriPermissionOwner(arg0);
    }

    public void noteWakeupAlarm(IIntentSender arg0) throws RemoteException {
        mTo.noteWakeupAlarm(arg0);
    }

    public ParcelFileDescriptor openContentUri(Uri arg0) throws RemoteException {
        return mTo.openContentUri(arg0);
    }

    public void overridePendingTransition(IBinder arg0, String arg1, int arg2, int arg3)
            throws RemoteException {
        mTo.overridePendingTransition(arg0, arg1, arg2, arg3);
    }

    public IBinder peekService(Intent arg0, String arg1) throws RemoteException {
        return mTo.peekService(arg0, arg1);
    }

    public boolean profileControl(String arg0, boolean arg1, String arg2,
            ParcelFileDescriptor arg3, int arg4) throws RemoteException {
        return mTo.profileControl(arg0, arg1, arg2, arg3, arg4);
    }

    public void publishContentProviders(IApplicationThread arg0, List<ContentProviderHolder> arg1)
            throws RemoteException {
        mTo.publishContentProviders(arg0, arg1);
    }

    public void publishService(IBinder arg0, Intent arg1, IBinder arg2) throws RemoteException {
        mTo.publishService(arg0, arg1, arg2);
    }

    public void registerActivityWatcher(IActivityWatcher arg0) throws RemoteException {
        mTo.registerActivityWatcher(arg0);
    }

    public void registerProcessObserver(IProcessObserver arg0) throws RemoteException {
        mTo.registerProcessObserver(arg0);
    }

    public Intent registerReceiver(IApplicationThread arg0, String arg1, IIntentReceiver arg2,
            IntentFilter arg3, String arg4) throws RemoteException {
        return mTo.registerReceiver(arg0, arg1, arg2, arg3, arg4);
    }

    public void removeContentProvider(IApplicationThread arg0, String arg1) throws RemoteException {
        mTo.removeContentProvider(arg0, arg1);
    }

    public boolean removeSubTask(int arg0, int arg1) throws RemoteException {
        return mTo.removeSubTask(arg0, arg1);
    }

    public boolean removeTask(int arg0, int arg1) throws RemoteException {
        return mTo.removeTask(arg0, arg1);
    }

    public void reportThumbnail(IBinder arg0, Bitmap arg1, CharSequence arg2)
            throws RemoteException {
        mTo.reportThumbnail(arg0, arg1, arg2);
    }

    public void resumeAppSwitches() throws RemoteException {
        mTo.resumeAppSwitches();
    }

    public void revokeUriPermission(IApplicationThread arg0, Uri arg1, int arg2)
            throws RemoteException {
        mTo.revokeUriPermission(arg0, arg1, arg2);
    }

    public void revokeUriPermissionFromOwner(IBinder arg0, Uri arg1, int arg2)
            throws RemoteException {
        mTo.revokeUriPermissionFromOwner(arg0, arg1, arg2);
    }

    public void serviceDoneExecuting(IBinder arg0, int arg1, int arg2, int arg3)
            throws RemoteException {
        mTo.serviceDoneExecuting(arg0, arg1, arg2, arg3);
    }

    public void setActivityController(IActivityController arg0) throws RemoteException {
        mTo.setActivityController(arg0);
    }

    public void setAlwaysFinish(boolean arg0) throws RemoteException {
        mTo.setAlwaysFinish(arg0);
    }

    public void setDebugApp(String arg0, boolean arg1, boolean arg2) throws RemoteException {
        mTo.setDebugApp(arg0, arg1, arg2);
    }

    public void setFrontActivityScreenCompatMode(int arg0) throws RemoteException {
        mTo.setFrontActivityScreenCompatMode(arg0);
    }

    public void setImmersive(IBinder arg0, boolean arg1) throws RemoteException {
        mTo.setImmersive(arg0, arg1);
    }

    public void setPackageAskScreenCompat(String arg0, boolean arg1) throws RemoteException {
        mTo.setPackageAskScreenCompat(arg0, arg1);
    }

    public void setPackageScreenCompatMode(String arg0, int arg1) throws RemoteException {
        mTo.setPackageScreenCompatMode(arg0, arg1);
    }

    public void setProcessForeground(IBinder arg0, int arg1, boolean arg2) throws RemoteException {
        mTo.setProcessForeground(arg0, arg1, arg2);
    }

    public void setProcessLimit(int arg0) throws RemoteException {
        mTo.setProcessLimit(arg0);
    }

    public void setRequestedOrientation(IBinder arg0, int arg1) throws RemoteException {
        mTo.setRequestedOrientation(arg0, arg1);
    }

    public void setServiceForeground(ComponentName arg0, IBinder arg1, int arg2, Notification arg3,
            boolean arg4) throws RemoteException {
        mTo.setServiceForeground(arg0, arg1, arg2, arg3, arg4);
    }

    public void showBootMessage(CharSequence arg0, boolean arg1) throws RemoteException {
        mTo.showBootMessage(arg0, arg1);
    }

    public void showWaitingForDebugger(IApplicationThread arg0, boolean arg1)
            throws RemoteException {
        mTo.showWaitingForDebugger(arg0, arg1);
    }

    public boolean shutdown(int arg0) throws RemoteException {
        return mTo.shutdown(arg0);
    }

    public void signalPersistentProcesses(int arg0) throws RemoteException {
        mTo.signalPersistentProcesses(arg0);
    }

    public int startActivities(IApplicationThread arg0, Intent[] arg1, String[] arg2, IBinder arg3)
            throws RemoteException {
        return mTo.startActivities(arg0, arg1, arg2, arg3);
    }

    public int startActivitiesInPackage(int arg0, Intent[] arg1, String[] arg2, IBinder arg3)
            throws RemoteException {
        return mTo.startActivitiesInPackage(arg0, arg1, arg2, arg3);
    }

    public int startActivity(IApplicationThread arg0, Intent arg1, String arg2, Uri[] arg3,
            int arg4, IBinder arg5, String arg6, int arg7, boolean arg8, boolean arg9,
            String arg10, ParcelFileDescriptor arg11, boolean arg12) throws RemoteException {
        return mTo.startActivity(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10,
                arg11, arg12);
    }

    public WaitResult startActivityAndWait(IApplicationThread arg0, Intent arg1, String arg2,
            Uri[] arg3, int arg4, IBinder arg5, String arg6, int arg7, boolean arg8, boolean arg9,
            String arg10, ParcelFileDescriptor arg11, boolean arg12) throws RemoteException {
        return mTo.startActivityAndWait(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9,
                arg10, arg11, arg12);
    }

    public int startActivityInPackage(int arg0, Intent arg1, String arg2, IBinder arg3,
            String arg4, int arg5, boolean arg6) throws RemoteException {
        return mTo.startActivityInPackage(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    public int startActivityIntentSender(IApplicationThread arg0, IntentSender arg1, Intent arg2,
            String arg3, IBinder arg4, String arg5, int arg6, int arg7, int arg8)
            throws RemoteException {
        return mTo.startActivityIntentSender(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }

    public int startActivityWithConfig(IApplicationThread arg0, Intent arg1, String arg2,
            Uri[] arg3, int arg4, IBinder arg5, String arg6, int arg7, boolean arg8, boolean arg9,
            Configuration arg10) throws RemoteException {
        return mTo.startActivityWithConfig(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8,
                arg9, arg10);
    }

    public boolean startInstrumentation(ComponentName arg0, String arg1, int arg2, Bundle arg3,
            IInstrumentationWatcher arg4) throws RemoteException {
        return mTo.startInstrumentation(arg0, arg1, arg2, arg3, arg4);
    }

    public boolean startNextMatchingActivity(IBinder arg0, Intent arg1) throws RemoteException {
        return mTo.startNextMatchingActivity(arg0, arg1);
    }

    public void startRunning(String arg0, String arg1, String arg2, String arg3)
            throws RemoteException {
        mTo.startRunning(arg0, arg1, arg2, arg3);
    }

    public ComponentName startService(IApplicationThread arg0, Intent arg1, String arg2)
            throws RemoteException {
        return mTo.startService(arg0, arg1, arg2);
    }

    public void stopAppSwitches() throws RemoteException {
        mTo.stopAppSwitches();
    }

    public int stopService(IApplicationThread arg0, Intent arg1, String arg2)
            throws RemoteException {
        return mTo.stopService(arg0, arg1, arg2);
    }

    public boolean stopServiceToken(ComponentName arg0, IBinder arg1, int arg2)
            throws RemoteException {
        return mTo.stopServiceToken(arg0, arg1, arg2);
    }

    public boolean switchUser(int arg0) throws RemoteException {
        return mTo.switchUser(arg0);
    }

    public boolean testIsSystemReady() {
        return mTo.testIsSystemReady();
    }

    public void unbindBackupAgent(ApplicationInfo arg0) throws RemoteException {
        mTo.unbindBackupAgent(arg0);
    }

    public void unbindFinished(IBinder arg0, Intent arg1, boolean arg2) throws RemoteException {
        mTo.unbindFinished(arg0, arg1, arg2);
    }

    public boolean unbindService(IServiceConnection arg0) throws RemoteException {
        return mTo.unbindService(arg0);
    }

    public void unbroadcastIntent(IApplicationThread arg0, Intent arg1) throws RemoteException {
        mTo.unbroadcastIntent(arg0, arg1);
    }

    public void unhandledBack() throws RemoteException {
        mTo.unhandledBack();
    }

    public void unregisterActivityWatcher(IActivityWatcher arg0) throws RemoteException {
        mTo.unregisterActivityWatcher(arg0);
    }

    public void unregisterProcessObserver(IProcessObserver arg0) throws RemoteException {
        mTo.unregisterProcessObserver(arg0);
    }

    public void unregisterReceiver(IIntentReceiver arg0) throws RemoteException {
        mTo.unregisterReceiver(arg0);
    }

    public void updateConfiguration(Configuration arg0) throws RemoteException {
        mTo.updateConfiguration(arg0);
    }

    public void updatePersistentConfiguration(Configuration arg0) throws RemoteException {
        mTo.updatePersistentConfiguration(arg0);
    }

    public void wakingUp() throws RemoteException {
        mTo.wakingUp();
    }

    public boolean willActivityBeVisible(IBinder arg0) throws RemoteException {
        return mTo.willActivityBeVisible(arg0);
    }
}
