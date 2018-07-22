package cn.zzq.Scene3D.Main;

import java.util.HashSet;

import com.google.appinventor.components.runtime.collect.Sets;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class MyGLView extends GLSurfaceView {

	public MyGLView(Context context) {
		super(context);
		this.setFocusable(true);
		this.requestFocus();
	}

	private final HashSet<ExtensionGestureDetector> extensionGestureDetectors = Sets.newHashSet();

	public interface ExtensionGestureDetector {
		boolean onTouchEvent(MotionEvent event);
	};

	public void registerCustomGestureDetector(ExtensionGestureDetector detector) {
		extensionGestureDetectors.add(detector);
	}

	public void removeCustomGestureDetector(Object detector) {
		extensionGestureDetectors.remove(detector);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		for (ExtensionGestureDetector extensionGestureDetector : extensionGestureDetectors) {
			extensionGestureDetector.onTouchEvent(event);
		}
		return true;
	}

	private final HashSet<ExtensionKeyDetector> extensionKeyDetectors = Sets.newHashSet();

	public interface ExtensionKeyDetector {
		boolean onKey(KeyEvent event,String action);

		boolean onKeyDown(int keyCode, KeyEvent event);

		boolean onKeyLongPress(int keyCode, KeyEvent event);

		boolean onKeyUp(int keyCode, KeyEvent event);

		boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event);

		boolean onKeyShortcut(int keyCode, KeyEvent event);

	}

	public abstract class ExtensionKeyAdaptor implements ExtensionKeyDetector {
	}

	public void registerCustomKeyDetector(ExtensionKeyDetector detector) {
		extensionKeyDetectors.add(detector);
	}

	public void removeCustomKeyDetector(Object detector) {
		extensionKeyDetectors.remove(detector);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		for (ExtensionKeyDetector extensionKeyDetector : extensionKeyDetectors) {
			extensionKeyDetector.onKeyDown(keyCode, event);
			extensionKeyDetector.onKey(event,"onKeyDown");
		}
		return true;
	}
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		for (ExtensionKeyDetector extensionKeyDetector : extensionKeyDetectors) {
			extensionKeyDetector.onKeyLongPress(keyCode, event);
			extensionKeyDetector.onKey(event,"onKeyLongPress");
		}
		return true;
	}
	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		for (ExtensionKeyDetector extensionKeyDetector : extensionKeyDetectors) {
			extensionKeyDetector.onKeyMultiple(keyCode, repeatCount, event);
			extensionKeyDetector.onKey(event,"onKeyMultiple");
		}
		return true;
	}
	
	@Override
	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
		for (ExtensionKeyDetector extensionKeyDetector : extensionKeyDetectors) {
			extensionKeyDetector.onKeyShortcut(keyCode, event);
			extensionKeyDetector.onKey(event,"onKeyShortcut");
		}
		return true;
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		for (ExtensionKeyDetector extensionKeyDetector : extensionKeyDetectors) {
			extensionKeyDetector.onKeyUp(keyCode, event);
			extensionKeyDetector.onKey(event,"onKeyUp");
		}
		return true;
	}

}
