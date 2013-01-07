package android.app;

import java.util.List;

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
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.StrictMode.ViolationInfo;

import com.robomorphine.strictmode.DataProxy;
import com.robomorphine.strictmode.MethodLogger;

@SuppressWarnings("all")
public class IActivityManagerProxyR17 implements IActivityManager {
    
    private final IActivityManager mTo;
    private final DataProxy<Intent> mIntentProxy;
    private final DataProxy<IBinder> mBinderProxy;
    private final boolean mPrintMethodCalls;
    private final boolean mPrintMethodArgs = false;
        
    public IActivityManagerProxyR17(IActivityManager manager, 
    			DataProxy<Intent> intentProxy,
    			DataProxy<IBinder> binderProxy,
    			boolean logMethodCalls) {
        mTo = manager;
        mIntentProxy = intentProxy;
        mBinderProxy = binderProxy;
        mPrintMethodCalls = logMethodCalls;
    }
    
    public void handleApplicationStrictModeViolation(IBinder binder, int mask, ViolationInfo info)
            throws RemoteException {
    	if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
        mTo.handleApplicationStrictModeViolation(mBinderProxy.handle(binder), mask, info);
    }

	public void activityDestroyed(IBinder arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.activityDestroyed(mBinderProxy.handle(arg0));
	}

	public void activityIdle(IBinder arg0, Configuration arg1, boolean arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.activityIdle(mBinderProxy.handle(arg0), arg1, arg2);
	}

	public void activityPaused(IBinder arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.activityPaused(mBinderProxy.handle(arg0));
	}

	public void activityResumed(IBinder arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.activityResumed(mBinderProxy.handle(arg0));
	}

	public void activitySlept(IBinder arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.activitySlept(mBinderProxy.handle(arg0));
	}

	public void activityStopped(IBinder arg0, Bundle arg1, Bitmap arg2,
			CharSequence arg3) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.activityStopped(mBinderProxy.handle(arg0), arg1, arg2, arg3);
	}

	public IBinder asBinder() {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mBinderProxy.handle(mTo.asBinder());
	}

	public void attachApplication(IApplicationThread arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.attachApplication(arg0);
	}

	public void backupAgentCreated(String arg0, IBinder arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.backupAgentCreated(arg0, mBinderProxy.handle(arg1));
	}

	public boolean bindBackupAgent(ApplicationInfo arg0, int arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.bindBackupAgent(arg0, arg1);
	}

	public int bindService(IApplicationThread arg0, IBinder arg1, Intent arg2,
			String arg3, IServiceConnection arg4, int arg5, int arg6)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.bindService(arg0, mBinderProxy.handle(arg1),
				mIntentProxy.handle(arg2), arg3, arg4, arg5, arg6);
	}

	public int broadcastIntent(IApplicationThread arg0, Intent arg1,
			String arg2, IIntentReceiver arg3, int arg4, String arg5,
			Bundle arg6, String arg7, boolean arg8, boolean arg9, int arg10)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.broadcastIntent(arg0, mIntentProxy.handle(arg1), arg2, arg3, arg4, arg5, arg6,
				arg7, arg8, arg9, arg10);
	}

	public void cancelIntentSender(IIntentSender arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.cancelIntentSender(arg0);
	}

	public int checkGrantUriPermission(int arg0, String arg1, Uri arg2, int arg3)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.checkGrantUriPermission(arg0, arg1, arg2, arg3);
	}

	public int checkPermission(String arg0, int arg1, int arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.checkPermission(arg0, arg1, arg2);
	}

	public int checkUriPermission(Uri arg0, int arg1, int arg2, int arg3)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.checkUriPermission(arg0, arg1, arg2, arg3);
	}

	public boolean clearApplicationUserData(String arg0,
			IPackageDataObserver arg1, int arg2) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.clearApplicationUserData(arg0, arg1, arg2);
	}

	public void clearPendingBackup() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.clearPendingBackup();
	}

	public void closeSystemDialogs(String arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.closeSystemDialogs(arg0);
	}

	public void crashApplication(int arg0, int arg1, String arg2, String arg3)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.crashApplication(arg0, arg1, arg2, arg3);
	}

	public void dismissKeyguardOnNextActivity() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.dismissKeyguardOnNextActivity();
	}

	public boolean dumpHeap(String arg0, int arg1, boolean arg2, String arg3,
			ParcelFileDescriptor arg4) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.dumpHeap(arg0, arg1, arg2, arg3, arg4);
	}

	public void enterSafeMode() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.enterSafeMode();
	}

	public boolean finishActivity(IBinder arg0, int arg1, Intent arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.finishActivity(mBinderProxy.handle(arg0), arg1, mIntentProxy.handle(arg2));
	}

	public boolean finishActivityAffinity(IBinder arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.finishActivityAffinity(mBinderProxy.handle(arg0));
	}

	public void finishHeavyWeightApp() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.finishHeavyWeightApp();
	}

	public void finishInstrumentation(IApplicationThread arg0, int arg1,
			Bundle arg2) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.finishInstrumentation(arg0, arg1, arg2);
	}

	public void finishReceiver(IBinder arg0, int arg1, String arg2,
			Bundle arg3, boolean arg4) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.finishReceiver(mBinderProxy.handle(arg0), arg1, arg2, arg3, arg4);
	}

	public void finishSubActivity(IBinder arg0, String arg1, int arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.finishSubActivity(mBinderProxy.handle(arg0), arg1, arg2);
	}

	public void forceStopPackage(String arg0, int arg1) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.forceStopPackage(arg0, arg1);
	}

	public ComponentName getActivityClassForToken(IBinder arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getActivityClassForToken(mBinderProxy.handle(arg0));
	}

	public ComponentName getCallingActivity(IBinder arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getCallingActivity(mBinderProxy.handle(arg0));
	}

	public String getCallingPackage(IBinder arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getCallingPackage(mBinderProxy.handle(arg0));
	}

	public Configuration getConfiguration() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getConfiguration();
	}

	public ContentProviderHolder getContentProvider(IApplicationThread arg0,
			String arg1, int arg2, boolean arg3) throws RemoteException 
			{
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getContentProvider(arg0, arg1, arg2, arg3);
	}

	public ContentProviderHolder getContentProviderExternal(String arg0,
			int arg1, IBinder arg2) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getContentProviderExternal(arg0, arg1, mBinderProxy.handle(arg2));
	}

	public UserInfo getCurrentUser() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getCurrentUser();
	}

	public ConfigurationInfo getDeviceConfigurationInfo()
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getDeviceConfigurationInfo();
	}

	public int getFrontActivityScreenCompatMode() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getFrontActivityScreenCompatMode();
	}

	public IIntentSender getIntentSender(int arg0, String arg1, IBinder arg2,
			String arg3, int arg4, Intent[] arg5, String[] arg6, int arg7,
			Bundle arg8, int arg9) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getIntentSender(arg0, arg1, mBinderProxy.handle(arg2),
				arg3, arg4, arg5, arg6,
				arg7, arg8, arg9);
	}

	public int getLaunchedFromUid(IBinder arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getLaunchedFromUid(mBinderProxy.handle(arg0));
	}

	public void getMemoryInfo(MemoryInfo arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.getMemoryInfo(arg0);
	}

	public void getMyMemoryState(RunningAppProcessInfo arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.getMyMemoryState(arg0);
	}

	public boolean getPackageAskScreenCompat(String arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getPackageAskScreenCompat(arg0);
	}

	public String getPackageForIntentSender(IIntentSender arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getPackageForIntentSender(arg0);
	}

	public String getPackageForToken(IBinder arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getPackageForToken(mBinderProxy.handle(arg0));
	}

	public int getPackageScreenCompatMode(String arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getPackageScreenCompatMode(arg0);
	}

	public int getProcessLimit() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getProcessLimit();
	}

	public android.os.Debug.MemoryInfo[] getProcessMemoryInfo(int[] arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getProcessMemoryInfo(arg0);
	}

	public long[] getProcessPss(int[] arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getProcessPss(arg0);
	}

	public List<ProcessErrorStateInfo> getProcessesInErrorState()
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getProcessesInErrorState();
	}

	public String getProviderMimeType(Uri arg0, int arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getProviderMimeType(arg0, arg1);
	}

	public List<RecentTaskInfo> getRecentTasks(int arg0, int arg1, int arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getRecentTasks(arg0, arg1, arg2);
	}

	public int getRequestedOrientation(IBinder arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getRequestedOrientation(mBinderProxy.handle(arg0));
	}

	public List<RunningAppProcessInfo> getRunningAppProcesses()
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getRunningAppProcesses();
	}

	public List<ApplicationInfo> getRunningExternalApplications()
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getRunningExternalApplications();
	}

	public PendingIntent getRunningServiceControlPanel(ComponentName arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getRunningServiceControlPanel(arg0);
	}

	public int[] getRunningUserIds() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getRunningUserIds();
	}

	public List getServices(int arg0, int arg1) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getServices(arg0, arg1);
	}

	public int getTaskForActivity(IBinder arg0, boolean arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getTaskForActivity(mBinderProxy.handle(arg0), arg1);
	}

	public TaskThumbnails getTaskThumbnails(int arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getTaskThumbnails(arg0);
	}

	public Bitmap getTaskTopThumbnail(int arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getTaskTopThumbnail(arg0);
	}

	public List getTasks(int arg0, int arg1, IThumbnailReceiver arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getTasks(arg0, arg1, arg2);
	}

	public int getUidForIntentSender(IIntentSender arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.getUidForIntentSender(arg0);
	}

	public void goingToSleep() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.goingToSleep();
	}

	public void grantUriPermission(IApplicationThread arg0, String arg1,
			Uri arg2, int arg3) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.grantUriPermission(arg0, arg1, arg2, arg3);
	}

	public void grantUriPermissionFromOwner(IBinder arg0, int arg1,
			String arg2, Uri arg3, int arg4) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.grantUriPermissionFromOwner(mBinderProxy.handle(arg0), arg1, arg2, arg3, arg4);
	}

	public void handleApplicationCrash(IBinder arg0, CrashInfo arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.handleApplicationCrash(mBinderProxy.handle(arg0), arg1);
	}

	public boolean handleApplicationWtf(IBinder arg0, String arg1,
			CrashInfo arg2) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.handleApplicationWtf(mBinderProxy.handle(arg0), arg1, arg2);
	}

	public int handleIncomingUser(int arg0, int arg1, int arg2, boolean arg3,
			boolean arg4, String arg5, String arg6) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.handleIncomingUser(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	}

	public long inputDispatchingTimedOut(int arg0, boolean arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.inputDispatchingTimedOut(arg0, arg1);
	}

	public boolean isImmersive(IBinder arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.isImmersive(mBinderProxy.handle(arg0));
	}

	public boolean isIntentSenderAnActivity(IIntentSender arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.isIntentSenderAnActivity(arg0);
	}

	public boolean isIntentSenderTargetedToPackage(IIntentSender arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.isIntentSenderTargetedToPackage(arg0);
	}

	public boolean isTopActivityImmersive() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.isTopActivityImmersive();
	}

	public boolean isUserAMonkey() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.isUserAMonkey();
	}

	public boolean isUserRunning(int arg0, boolean arg1) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.isUserRunning(arg0, arg1);
	}

	public void killAllBackgroundProcesses() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.killAllBackgroundProcesses();
	}

	public void killApplicationProcess(String arg0, int arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.killApplicationProcess(arg0, arg1);
	}

	public void killApplicationWithAppId(String arg0, int arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.killApplicationWithAppId(arg0, arg1);
	}

	public void killBackgroundProcesses(String arg0, int arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.killBackgroundProcesses(arg0, arg1);
	}

	public boolean killPids(int[] arg0, String arg1, boolean arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.killPids(arg0, arg1, arg2);
	}

	public boolean killProcessesBelowForeground(String arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.killProcessesBelowForeground(arg0);
	}

	public boolean moveActivityTaskToBack(IBinder arg0, boolean arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.moveActivityTaskToBack(mBinderProxy.handle(arg0), arg1);
	}

	public void moveTaskBackwards(int arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.moveTaskBackwards(arg0);
	}

	public void moveTaskToBack(int arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.moveTaskToBack(arg0);
	}

	public void moveTaskToFront(int arg0, int arg1, Bundle arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.moveTaskToFront(arg0, arg1, arg2);
	}

	public boolean navigateUpTo(IBinder arg0, Intent arg1, int arg2, Intent arg3)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.navigateUpTo(mBinderProxy.handle(arg0),
				mIntentProxy.handle(arg1), arg2, mIntentProxy.handle(arg3));
	}

	public IBinder newUriPermissionOwner(String arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mBinderProxy.handle(mTo.newUriPermissionOwner(arg0));
	}

	public void noteWakeupAlarm(IIntentSender arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.noteWakeupAlarm(arg0);
	}

	public ParcelFileDescriptor openContentUri(Uri arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.openContentUri(arg0);
	}

	public void overridePendingTransition(IBinder arg0, String arg1, int arg2,
			int arg3) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.overridePendingTransition(mBinderProxy.handle(arg0), arg1, arg2, arg3);
	}

	public IBinder peekService(Intent arg0, String arg1) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mBinderProxy.handle(mTo.peekService(mIntentProxy.handle(arg0), arg1));
	}

	public boolean profileControl(String arg0, int arg1, boolean arg2,
			String arg3, ParcelFileDescriptor arg4, int arg5)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.profileControl(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	public void publishContentProviders(IApplicationThread arg0,
			List<ContentProviderHolder> arg1) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.publishContentProviders(arg0, arg1);
	}

	public void publishService(IBinder arg0, Intent arg1, IBinder arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.publishService(mBinderProxy.handle(arg0),
				mIntentProxy.handle(arg1),
				mBinderProxy.handle(arg2));
	}

	public boolean refContentProvider(IBinder arg0, int arg1, int arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.refContentProvider(mBinderProxy.handle(arg0), arg1,
				arg2);
	}

	public void registerProcessObserver(IProcessObserver arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.registerProcessObserver(arg0);
	}

	public Intent registerReceiver(IApplicationThread arg0, String arg1,
			IIntentReceiver arg2, IntentFilter arg3, String arg4, int arg5)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.registerReceiver(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	public void registerUserSwitchObserver(IUserSwitchObserver arg0)
			throws RemoteException {
		mTo.registerUserSwitchObserver(arg0);
	}

	public void removeContentProvider(IBinder arg0, boolean arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.removeContentProvider(mBinderProxy.handle(arg0), arg1);
	}

	public void removeContentProviderExternal(String arg0, IBinder arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.removeContentProviderExternal(arg0, mBinderProxy.handle(arg1));
	}

	public boolean removeSubTask(int arg0, int arg1) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.removeSubTask(arg0, arg1);
	}

	public boolean removeTask(int arg0, int arg1) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.removeTask(arg0, arg1);
	}

	public void reportThumbnail(IBinder arg0, Bitmap arg1, CharSequence arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.reportThumbnail(mBinderProxy.handle(arg0), arg1, arg2);
	}

	public void requestBugReport() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.requestBugReport();
	}

	public void resumeAppSwitches() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.resumeAppSwitches();
	}

	public void revokeUriPermission(IApplicationThread arg0, Uri arg1, int arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.revokeUriPermission(arg0, arg1, arg2);
	}

	public void revokeUriPermissionFromOwner(IBinder arg0, Uri arg1, int arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.revokeUriPermissionFromOwner(mBinderProxy.handle(arg0), arg1, arg2);
	}

	public void serviceDoneExecuting(IBinder arg0, int arg1, int arg2, int arg3)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.serviceDoneExecuting(mBinderProxy.handle(arg0), arg1, arg2, arg3);
	}

	public void setActivityController(IActivityController arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.setActivityController(arg0);
	}

	public void setAlwaysFinish(boolean arg0) throws RemoteException {
		mTo.setAlwaysFinish(arg0);
	}

	public void setDebugApp(String arg0, boolean arg1, boolean arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.setDebugApp(arg0, arg1, arg2);
	}

	public void setFrontActivityScreenCompatMode(int arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.setFrontActivityScreenCompatMode(arg0);
	}

	public void setImmersive(IBinder arg0, boolean arg1) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.setImmersive(mBinderProxy.handle(arg0), arg1);
	}

	public void setLockScreenShown(boolean arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.setLockScreenShown(arg0);
	}

	public void setPackageAskScreenCompat(String arg0, boolean arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.setPackageAskScreenCompat(arg0, arg1);
	}

	public void setPackageScreenCompatMode(String arg0, int arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.setPackageScreenCompatMode(arg0, arg1);
	}

	public void setProcessForeground(IBinder arg0, int arg1, boolean arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.setProcessForeground(mBinderProxy.handle(arg0), arg1, arg2);
	}

	public void setProcessLimit(int arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.setProcessLimit(arg0);
	}

	public void setRequestedOrientation(IBinder arg0, int arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.setRequestedOrientation(mBinderProxy.handle(arg0), arg1);
	}

	public void setServiceForeground(ComponentName arg0, IBinder arg1,
			int arg2, Notification arg3, boolean arg4) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.setServiceForeground(arg0, mBinderProxy.handle(arg1), arg2, arg3, arg4);
	}

	public void showBootMessage(CharSequence arg0, boolean arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.showBootMessage(arg0, arg1);
	}

	public void showWaitingForDebugger(IApplicationThread arg0, boolean arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.showWaitingForDebugger(arg0, arg1);
	}

	public boolean shutdown(int arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.shutdown(arg0);
	}

	public void signalPersistentProcesses(int arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.signalPersistentProcesses(arg0);
	}

	public int startActivities(IApplicationThread arg0, Intent[] arg1,
			String[] arg2, IBinder arg3, Bundle arg4, int arg5)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.startActivities(arg0, arg1, arg2, mBinderProxy.handle(arg3), arg4, arg5);
	}

	public int startActivity(IApplicationThread arg0, Intent arg1, String arg2,
			IBinder arg3, String arg4, int arg5, int arg6, String arg7,
			ParcelFileDescriptor arg8, Bundle arg9) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.startActivity(arg0, mIntentProxy.handle(arg1), arg2, 
				mBinderProxy.handle(arg3), arg4, arg5, arg6,
				arg7, arg8, arg9);
	}

	public WaitResult startActivityAndWait(IApplicationThread arg0,
			Intent arg1, String arg2, IBinder arg3, String arg4, int arg5,
			int arg6, String arg7, ParcelFileDescriptor arg8, Bundle arg9,
			int arg10) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.startActivityAndWait(arg0, mIntentProxy.handle(arg1), arg2, 
				mBinderProxy.handle(arg3), 
				arg4, arg5,
				arg6, arg7, arg8, arg9, arg10);
	}

	public int startActivityAsUser(IApplicationThread arg0, Intent arg1,
			String arg2, IBinder arg3, String arg4, int arg5, int arg6,
			String arg7, ParcelFileDescriptor arg8, Bundle arg9, int arg10)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.startActivityAsUser(arg0, mIntentProxy.handle(arg1), arg2, 
				mBinderProxy.handle(arg3), arg4, arg5,
				arg6, arg7, arg8, arg9, arg10);
	}

	public int startActivityIntentSender(IApplicationThread arg0,
			IntentSender arg1, Intent arg2, String arg3, IBinder arg4,
			String arg5, int arg6, int arg7, int arg8, Bundle arg9)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.startActivityIntentSender(arg0, arg1, mIntentProxy.handle(arg2), arg3, 
				mBinderProxy.handle(arg4),
				arg5, arg6, arg7, arg8, arg9);
	}

	public int startActivityWithConfig(IApplicationThread arg0, Intent arg1,
			String arg2, IBinder arg3, String arg4, int arg5, int arg6,
			Configuration arg7, Bundle arg8, int arg9) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.startActivityWithConfig(arg0, mIntentProxy.handle(arg1), arg2, 
				mBinderProxy.handle(arg3), arg4, arg5,
				arg6, arg7, arg8, arg9);
	}

	public boolean startInstrumentation(ComponentName arg0, String arg1,
			int arg2, Bundle arg3, IInstrumentationWatcher arg4, int arg5)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.startInstrumentation(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	public boolean startNextMatchingActivity(IBinder arg0, Intent arg1,
			Bundle arg2) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.startNextMatchingActivity(mBinderProxy.handle(arg0),
				mIntentProxy.handle(arg1), arg2);
	}

	public void startRunning(String arg0, String arg1, String arg2, String arg3)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.startRunning(arg0, arg1, arg2, arg3);
	}

	public ComponentName startService(IApplicationThread arg0, Intent arg1,
			String arg2, int arg3) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.startService(arg0, mIntentProxy.handle(arg1), arg2, arg3);
	}

	public void stopAppSwitches() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.stopAppSwitches();
	}

	public int stopService(IApplicationThread arg0, Intent arg1, String arg2,
			int arg3) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.stopService(arg0, mIntentProxy.handle(arg1), arg2, arg3);
	}

	public boolean stopServiceToken(ComponentName arg0, IBinder arg1, int arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.stopServiceToken(arg0, mBinderProxy.handle(arg1), arg2);
	}

	public int stopUser(int arg0, IStopUserCallback arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.stopUser(arg0, arg1);
	}

	public boolean switchUser(int arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.switchUser(arg0);
	}

	public boolean targetTaskAffinityMatchesActivity(IBinder arg0, String arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.targetTaskAffinityMatchesActivity(mBinderProxy.handle(arg0), arg1);
	}

	public boolean testIsSystemReady() {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.testIsSystemReady();
	}

	public void unbindBackupAgent(ApplicationInfo arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.unbindBackupAgent(arg0);
	}

	public void unbindFinished(IBinder arg0, Intent arg1, boolean arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.unbindFinished(mBinderProxy.handle(arg0), mIntentProxy.handle(arg1), arg2);
	}

	public boolean unbindService(IServiceConnection arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.unbindService(arg0);
	}

	public void unbroadcastIntent(IApplicationThread arg0, Intent arg1, int arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.unbroadcastIntent(arg0, mIntentProxy.handle(arg1), arg2);
	}

	public void unhandledBack() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.unhandledBack();
	}

	public void unregisterProcessObserver(IProcessObserver arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.unregisterProcessObserver(arg0);
	}

	public void unregisterReceiver(IIntentReceiver arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.unregisterReceiver(arg0);
	}

	public void unregisterUserSwitchObserver(IUserSwitchObserver arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.unregisterUserSwitchObserver(arg0);
	}

	public void unstableProviderDied(IBinder arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.unstableProviderDied(mBinderProxy.handle(arg0));
	}

	public void updateConfiguration(Configuration arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.updateConfiguration(arg0);
	}

	public void updatePersistentConfiguration(Configuration arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.updatePersistentConfiguration(arg0);
	}

	public void wakingUp() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.wakingUp();
	}

	public boolean willActivityBeVisible(IBinder arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.willActivityBeVisible(mBinderProxy.handle(arg0));
	}   
}
