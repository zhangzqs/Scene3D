package cn.zzq.Scene3D.Main;

import java.util.HashSet;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.google.appinventor.components.runtime.collect.Sets;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.HVArrangement;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.util.YailList;
import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Logger;
import com.threed.jpct.RGBColor;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView.Renderer;
import android.view.ViewGroup;

@DesignerComponent(version = 1, description = "", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH4gQWAwE6yvvoAAAAAyFJREFUOMt9k9trHFUcx79nzplrZnZnN7vZ7WbsplHrpbTKtpBqURQRFK1SqATxQfH64IPiqw8tedGKffCloBJRKxT0D6hPFl8ktTaWtlRTaZLmNtnLJLvZ7uzOnJlzfAiIgu33/fO7wOdLcKe8frFKdfoh0YxCGkeXZdS/BIVcAw+X8M2TAwAgt4Xf/k0lBB9ly6MfmG6eUBFKGff7cdhb7wRbP8ZRPIXpR+rsDvtfHS4V3yrsLBJdpzANjZh61rIJH790xX9neaE1D+Ak/V/0zZnHCuXc8Z33lKq5nI6cA2RtoOIAN5oplrpE0dIoxx9449x/LzgGDdH7nsOtd8fGhx8qVyzoGmBoQNaQuL6cYHaFAwqDZZk1pdt7fHuABMEJHGEGDuvpuT1Of3J/sexi2JZwZRM0N4L1psDsEoeEgKoIQIotkZBg+wUL791V1U48fF/xkOP5o2F3lCz4NQz5F3D45hTcaAO/tsdwLWAAEXCSnsRG+4sokacoPsbBQoV8cqR2wHtl93P4K/wDW3ETg8Yh7Akv4qhyBmX/J6T+HNpcQ5rxoHa7M0ErPo4v969T9ixeLpTIS7opSDBo43pnEcRYg9jM4InGHA6W5pH3XNxLrqLGf0EmWBzE/vLpxenJ7wGAaZrq6SrI/OYaFjbX/jHDHZ3GeEBAiQR1yrBVF3t3JKi2zxpH7f7zwWtsI42Tb1mSyKYYaFKAk39rlY9b8NgQVMuBQgXYDg+8V0e+yODuLtS0jLF3ddYPlZgnM7yvt3K0AB4BPAKSGNjVdzBi6WAWA5IO2JAJlndBMxZU1wRR6Srv8zmKR7HU4QOnYAxPVLMV5tlljGd2YaKlY5+RwsrroDqF6mSh5EYgWYTO8i3pn1859cx0/DXFzxDYh/P1ZGujG3Kz3eVYqQc3H1yomxN3u4ae0UANBoUmYG4Fgqaoz65eaFxZn7r/KlrbIp1ED8BnjWOd0w27k31xDfZTQ9mvmJ05IDQDUE0IYoDcUhHcCHn9d/+7F87gz9u28ewkxhRb/dSp5EtCsggSoRAilBK9dDCYS1c3P3/6B3QA4G+fl0r2glUAJwAAAABJRU5ErkJggg==")

@SimpleObject(external = true)
public class Scene3D extends AndroidNonvisibleComponent implements Renderer {
	public static final String LOG_TAG = "MyJPCT";

	public static RGBColor TRANSPARENT = new RGBColor(0, 0, 0, 0);
	private final HashSet<OnDrawFrameListener> onDrawFrameListeners = Sets.newHashSet();
	private final HashSet<OnSurfaceChangedListener> onSurfaceChangedListeners = Sets.newHashSet();




	public HVArrangement hvArrangement = null;
	public GL10 gl10 = null;
	public int width, height;
	public FrameBuffer fb;

	public MyGLView glView = null;

	private long time = System.currentTimeMillis();
	private int fps = 0;

	public Scene3D(ComponentContainer container) {
		super(container.$form());
		form.Sizing("responsive");
		if (glView == null) {
			glView = new MyGLView(form);
			glView.setEGLContextClientVersion(2);
			glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
			glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
			glView.setZOrderOnTop(true);
			glView.setRenderer(this);
		}
	}
	private void setHVArrangement(final HVArrangement hvArrangement) {
		this.hvArrangement = hvArrangement;
		((ViewGroup) hvArrangement.getView()).addView(glView);
	}
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT
			+ ":com.google.appinventor.components.runtime.HorizontalArrangement")
	@SimpleProperty(description = "绑定一个水平布局作为显示器/" + "Bind a layout as a monitor")
	public void HorizontalArrangement(
			final com.google.appinventor.components.runtime.HorizontalArrangement horizontalArrangement) {
		setHVArrangement(horizontalArrangement);
	}
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT
			+ ":com.google.appinventor.components.runtime.HorizontalScrollArrangement")
	@SimpleProperty(description = "绑定一个水平滚动布局作为显示器/" + "Bind a layout as a monitor")

	public void HorizontalScrollArrangement(
			final com.google.appinventor.components.runtime.HorizontalScrollArrangement horizontalScrollArrangement) {
		setHVArrangement(horizontalScrollArrangement);
	}
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT
			+ ":com.google.appinventor.components.runtime.VerticalArrangement")
	@SimpleProperty(description = "绑定一个垂直布局作为显示器/" + "Bind a layout as a monitor")
	public void VerticalArrangement(
			final com.google.appinventor.components.runtime.VerticalArrangement verticalArrangement) {
		setHVArrangement(verticalArrangement);
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT
			+ ":com.google.appinventor.components.runtime.VerticalScrollArrangement")
	@SimpleProperty(description = "绑定一个垂直滚动布局作为显示器/" + "Bind a layout as a monitor")
	public void VerticalScrollArrangement(
			final com.google.appinventor.components.runtime.VerticalScrollArrangement verticalScrollArrangement) {
		setHVArrangement(verticalScrollArrangement);
	}


	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = Component.DEFAULT_VALUE_COLOR_WHITE)
	@SimpleProperty(description = "")
	public void BackgroundColor(int color) {
		TRANSPARENT = Utils.int2rgba(color);
	}


	/*---------------------------------------------------------------------------*/
	@SimpleEvent(description = "获取当前的FPS")
	public void OnFPSChanged(final int FPS) {
		form.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				EventDispatcher.dispatchEvent(Scene3D.this, "OnFPSChanged", FPS);
			}
		});
	}

	public World world = new World();
	public Camera camera = world.getCamera();

	// 线框渲染信息
	private boolean wireframe = false;
	private RGBColor wireframeColor = RGBColor.WHITE;
	private int wireframeSize = 1;
	private boolean wireframePointMode = true;

	@SimpleFunction(description = "Draws the current scene as a single-colored wireframe.\n" + "将当前场景绘制为单色线框。")
	public void UseWireFrame(int color, int size, boolean pointMode) {
		this.wireframe = true;
		this.wireframeColor = Utils.int2rgba(color);
		this.wireframeSize = size;
		this.wireframePointMode = pointMode;
	}

	@SimpleFunction(description = "关闭线框模式")
	public void CloseWireFrame() {
		this.wireframe = false;
	}

	@SimpleEvent(description = "动画渲染，理想情况下每秒60帧")
	public void OnDrawFrame() {
		form.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				EventDispatcher.dispatchEvent(Scene3D.this, "OnDrawFrame");
			}
		});
		world.renderScene(fb);
		// 判断渲染模式
		if (wireframe) {
			world.drawWireframe(fb, wireframeColor, wireframeSize, wireframePointMode);
		} else {
			world.draw(fb);
		}

	}

	@SimpleEvent(description = "当视口改变时，如屏幕旋转时")
	public void OnSurfaceChanged(final int width, final int height) {
		form.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				EventDispatcher.dispatchEvent(Scene3D.this, "OnSurfaceChanged", width, height);
			}
		});
	}

	@SimpleEvent(description = "当3d舞台被创建后的初始化工作")
	public void OnSurfaceCreated() {
		form.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				EventDispatcher.dispatchEvent(Scene3D.this, "OnSurfaceCreated");
			}
		});
	}

	/*-----------------------------------Save Picture----------------------------------------*/
	Bitmap bitmap = null;
	boolean save = false;

	@SimpleFunction(description = "")
	public void SaveAsPicture() {
		save = true;
	}

	@SimpleEvent(description = "")
	public void OnPictureSaved(String filepath) {
		EventDispatcher.dispatchEvent(this, "OnPictureSaved", filepath);
	}
	public interface OnDrawFrameListener {
		public void onDrawFrame(GL10 gl10);
	}

	public void registerForOnDrawFrame(OnDrawFrameListener onDrawFrameListener) {
		onDrawFrameListeners.add(onDrawFrameListener);
	}

	public void unregisterForOnDrawFrame(OnDrawFrameListener onDrawFrameListener) {
		onDrawFrameListeners.remove(onDrawFrameListener);
	}

	/*-----------------------------------OPENGL渲染----------------------------------------*/
	@Override
	public void onDrawFrame(GL10 gl) {
		for (OnDrawFrameListener onDrawFrameListener : onDrawFrameListeners) {
			onDrawFrameListener.onDrawFrame(gl);
		}

		fb.clear(TRANSPARENT);
		OnDrawFrame();
		fb.display();

		if (save) {
			form.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					bitmap = Bitmap.createBitmap(fb.getPixels(), fb.getWidth(), fb.getHeight(), Config.ARGB_8888);
					String path = Save(bitmap);
					OnPictureSaved(path);
				}
			});
			save = false;
		}
		// fps
		if (System.currentTimeMillis() - time >= 1000) {
			Logger.log(fps + "fps");
			OnFPSChanged(fps);
			fps = 0;
			time = System.currentTimeMillis();
		}
		fps++;
	}
	public interface OnSurfaceChangedListener {
		public void onSurfaceChanged(GL10 gl, int width, int height);
	}

	public void registerForOnSurfaceChangedListener(OnSurfaceChangedListener onSurfaceChangedListener) {
		onSurfaceChangedListeners.add(onSurfaceChangedListener);
	}

	public void unregisterForOnSurfaceChangedListener(OnSurfaceChangedListener onSurfaceChangedListener) {
		onSurfaceChangedListeners.remove(onSurfaceChangedListener);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int w, int h) {
		this.width = w;
		this.height = h;
		if (fb == null) {
			fb = new FrameBuffer(w, h);
		} else {
			fb.resize(w, h);
		}
		OnSurfaceChanged(w, h);

		for (OnSurfaceChangedListener onSurfaceChangedListener : onSurfaceChangedListeners) {
			onSurfaceChangedListener.onSurfaceChanged(gl, w, h);
		}
	}

	private boolean first = true;

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig egc) {
		this.gl10 = gl;
		world = new World();
		camera = world.getCamera();
		if (first) {
			first = false;
			OnSurfaceCreated();
		}
	}

	/*-----------------------------------Utils(非静态工具)-----------------------------------*/
	// 保存图片工具
	public String Save(Bitmap bitmap) {
		return Utils.Save(this, form, bitmap);
	}

	public String SaveAs(Bitmap bitmap, String fileName) {
		return Utils.SaveAs(this, form, fileName, bitmap);
	}

	/*--------------------------------FrameBuffer-------------------------------------------*/
	@SimpleFunction(description = "Should be called before this FrameBuffer won't be used anymore to do some clean up work.")
	public void Dispose() {
		fb.dispose();
	}

	@SimpleFunction(description = "Frees some native memory used by the gl context.")
	public void FreeMemory() {
		fb.freeMemory();
	}

	@SimpleFunction(description = "Does a resize of the FrameBuffer in case that the output window's size has changed.")
	public void Resize(int width, int height) {
		fb.resize(width, height);
	}

	@SimpleProperty(description = "")
	public YailList GetPixels() {
		return Utils.IArray2List(fb.getPixels());
	}

	@SimpleFunction(description = "Synchronizes GPU and CPU and flushes the render pipeline..")
	public void Sync() {
		fb.sync();
	}

	@SimpleFunction(description = "")
	public void InitPlugin(IScene3DPlugin plugin) {
		plugin.init(this);
	}

	@SimpleFunction(description = "Tries to free some memory by forcing gc and finalization. Write current memory usage to the log. This can be used to trigger some gc work that might otherwise interrupt the application causing an animation to stutter or similar. However, this method can't avoid that...it just increases the chance that won't happen or at least not that often.\n"
			+ "尝试通过强制gc和最终化来释放一些内存。将当前内存使用情况写入日志。这可以用于触发某些gc工作，否则可能会中断应用程序，导致动画造成口吃或类似情况。然而，这种方法无法避免......这只会增加不会发生的机会，或者至少不会经常发生。 ")
	public void compact() {
		MemoryHelper.compact();
	}

	@SimpleFunction(description = " 	printMemory()\n"
			+ "          Prints current memory usage into the log and return it;.\n" + "将当前内存使用情况打印到日志中并返回。")
	public String GetMemory() {
		MemoryHelper.printMemory();
		return "Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024L
				+ " KB used out of " + Runtime.getRuntime().totalMemory() / 1024L
				+ " KB. Max. memory available to the VM is " + Runtime.getRuntime().maxMemory() / 1024L + " KB.";
	}
}
