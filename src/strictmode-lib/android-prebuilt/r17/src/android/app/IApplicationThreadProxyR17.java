
package android.app;

import java.io.FileDescriptor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ComponentName;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Debug.MemoryInfo;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

import com.robomorphine.strictmode.DataProxy;
import com.robomorphine.strictmode.MethodLogger;

public class IApplicationThreadProxyR17 implements IApplicationThread {
	
	private final IApplicationThread mTo;
	private final DataProxy<Intent> mIntentProxy;
	private final DataProxy<IBinder> mBinderProxy;
	private final boolean mPrintMethodCalls;
	private final boolean mPrintMethodArgs = false;
	   
	
	public IApplicationThreadProxyR17(IApplicationThread to, 
			DataProxy<Intent> intentProxy,
			DataProxy<IBinder> binderProxy,
			boolean logMethodCalls) {
		mTo = to;
		mIntentProxy = intentProxy;
		mBinderProxy = binderProxy;
		mPrintMethodCalls = logMethodCalls; 
	}
	
	public IBinder asBinder() {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mBinderProxy.handle(mTo.asBinder());
	}
	
	public void bindApplication(String arg0, ApplicationInfo arg1,
			List<ProviderInfo> arg2, ComponentName arg3, String arg4,
			ParcelFileDescriptor arg5, boolean arg6, Bundle arg7,
			IInstrumentationWatcher arg8, int arg9, boolean arg10,
			boolean arg11, boolean arg12, Configuration arg13,
			CompatibilityInfo arg14, Map<String, IBinder> arg15, Bundle arg16)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		HashMap<String, IBinder> binders = new HashMap<String, IBinder>();
		for (Map.Entry<String, IBinder> entry : arg15.entrySet()) {
			binders.put(entry.getKey(), mBinderProxy.handle(entry.getValue()));
		}
		
		mTo.bindApplication(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7,
				arg8, arg9, arg10, arg11, arg12, arg13, arg14, binders, arg16);
	}
	
	public void clearDnsCache() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.clearDnsCache();
	}
	public void dispatchPackageBroadcast(int arg0, String[] arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.dispatchPackageBroadcast(arg0, arg1);
	}
	public void dumpActivity(FileDescriptor arg0, IBinder arg1, String arg2,
			String[] arg3) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.dumpActivity(arg0, mBinderProxy.handle(arg1), arg2, arg3);
	}
	public void dumpDbInfo(FileDescriptor arg0, String[] arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.dumpDbInfo(arg0, arg1);
	}
	public void dumpGfxInfo(FileDescriptor arg0, String[] arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.dumpGfxInfo(arg0, arg1);
	}
	public void dumpHeap(boolean arg0, String arg1, ParcelFileDescriptor arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.dumpHeap(arg0, arg1, arg2);
	}
	public MemoryInfo dumpMemInfo(FileDescriptor arg0, boolean arg1,
			boolean arg2, String[] arg3) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		return mTo.dumpMemInfo(arg0, arg1, arg2, arg3);
	}
	public void dumpProvider(FileDescriptor arg0, IBinder arg1, String[] arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.dumpProvider(arg0, mBinderProxy.handle(arg1), arg2);
	}
	public void dumpService(FileDescriptor arg0, IBinder arg1, String[] arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.dumpService(arg0, mBinderProxy.handle(arg1), arg2);
	}
	public void getMemoryInfo(MemoryInfo arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.getMemoryInfo(arg0);
	}
	public void processInBackground() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.processInBackground();
	}
	public void profilerControl(boolean arg0, String arg1,
			ParcelFileDescriptor arg2, int arg3) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.profilerControl(arg0, arg1, arg2, arg3);
	}
	public void requestThumbnail(IBinder arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.requestThumbnail(mBinderProxy.handle(arg0));
	}
	public void scheduleActivityConfigurationChanged(IBinder arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleActivityConfigurationChanged(mBinderProxy.handle(arg0));
	}
	public void scheduleBindService(IBinder arg0, Intent arg1, boolean arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleBindService(mBinderProxy.handle(arg0), mIntentProxy.handle(arg1), arg2);
	}
	public void scheduleConfigurationChanged(Configuration arg0)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleConfigurationChanged(arg0);
	}
	public void scheduleCrash(String arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleCrash(arg0);
	}
	public void scheduleCreateBackupAgent(ApplicationInfo arg0,
			CompatibilityInfo arg1, int arg2) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleCreateBackupAgent(arg0, arg1, arg2);
	}
	public void scheduleCreateService(IBinder arg0, ServiceInfo arg1,
			CompatibilityInfo arg2) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleCreateService(mBinderProxy.handle(arg0), arg1, arg2);
	}
	public void scheduleDestroyActivity(IBinder arg0, boolean arg1, int arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleDestroyActivity(mBinderProxy.handle(arg0), arg1, arg2);
	}
	public void scheduleDestroyBackupAgent(ApplicationInfo arg0,
			CompatibilityInfo arg1) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleDestroyBackupAgent(arg0, arg1);
	}
	public void scheduleExit() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleExit();
	}
	public void scheduleLaunchActivity(Intent arg0, IBinder arg1, int arg2,
			ActivityInfo arg3, Configuration arg4, CompatibilityInfo arg5,
			Bundle arg6, List<ResultInfo> arg7, List<Intent> arg8,
			boolean arg9, boolean arg10, String arg11,
			ParcelFileDescriptor arg12, boolean arg13) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleLaunchActivity(mIntentProxy.handle(arg0), 
				mBinderProxy.handle(arg1), arg2, arg3, arg4, arg5, arg6,
				arg7, arg8, arg9, arg10, arg11, arg12, arg13);
	}
	public void scheduleLowMemory() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleLowMemory();
	}
	public void scheduleNewIntent(List<Intent> arg0, IBinder arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleNewIntent(arg0, mBinderProxy.handle(arg1));
	}
	public void schedulePauseActivity(IBinder arg0, boolean arg1, boolean arg2,
			int arg3) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.schedulePauseActivity(mBinderProxy.handle(arg0), arg1, arg2, arg3);
	}
	public void scheduleReceiver(Intent arg0, ActivityInfo arg1,
			CompatibilityInfo arg2, int arg3, String arg4, Bundle arg5,
			boolean arg6, int arg7) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleReceiver(mIntentProxy.handle(arg0), arg1, arg2, 
				arg3, arg4, arg5, arg6, arg7);
	}
	public void scheduleRegisteredReceiver(IIntentReceiver arg0, Intent arg1,
			int arg2, String arg3, Bundle arg4, boolean arg5, boolean arg6,
			int arg7) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleRegisteredReceiver(arg0, mIntentProxy.handle(arg1), arg2, 
				arg3, arg4, arg5,
				arg6, arg7);
	}
	public void scheduleRelaunchActivity(IBinder arg0, List<ResultInfo> arg1,
			List<Intent> arg2, int arg3, boolean arg4, Configuration arg5)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleRelaunchActivity(mBinderProxy.handle(arg0), arg1, arg2, arg3, arg4, arg5);
	}
	public void scheduleResumeActivity(IBinder arg0, boolean arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleResumeActivity(mBinderProxy.handle(arg0), arg1);
	}
	public void scheduleSendResult(IBinder arg0, List<ResultInfo> arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleSendResult(mBinderProxy.handle(arg0), arg1);
	}
	public void scheduleServiceArgs(IBinder arg0, boolean arg1, int arg2,
			int arg3, Intent arg4) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleServiceArgs(mBinderProxy.handle(arg0), arg1, arg2,
				arg3, mIntentProxy.handle(arg4));
	}
	public void scheduleSleeping(IBinder arg0, boolean arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleSleeping(mBinderProxy.handle(arg0), arg1);
	}
	public void scheduleStopActivity(IBinder arg0, boolean arg1, int arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleStopActivity(mBinderProxy.handle(arg0), arg1, arg2);
	}
	public void scheduleStopService(IBinder arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleStopService(mBinderProxy.handle(arg0));
	}
	public void scheduleSuicide() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleSuicide();
	}
	public void scheduleTrimMemory(int arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleTrimMemory(arg0);
	}
	public void scheduleUnbindService(IBinder arg0, Intent arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleUnbindService(mBinderProxy.handle(arg0), mIntentProxy.handle(arg1));
	}
	public void scheduleWindowVisibility(IBinder arg0, boolean arg1)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.scheduleWindowVisibility(mBinderProxy.handle(arg0), arg1);
	}
	public void setCoreSettings(Bundle arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.setCoreSettings(arg0);
	}
	public void setHttpProxy(String arg0, String arg1, String arg2)
			throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.setHttpProxy(arg0, arg1, arg2);
	}
	public void setSchedulingGroup(int arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.setSchedulingGroup(arg0);
	}
	public void unstableProviderDied(IBinder arg0) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.unstableProviderDied(mBinderProxy.handle(arg0));
	}
	public void updatePackageCompatibilityInfo(String arg0,
			CompatibilityInfo arg1) throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.updatePackageCompatibilityInfo(arg0, arg1);
	}
	public void updateTimeZone() throws RemoteException {
		if (mPrintMethodCalls) MethodLogger.logMethod(mPrintMethodArgs);
		mTo.updateTimeZone();
	}
}
