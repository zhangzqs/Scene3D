package cn.zzq.Scene3D.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.FileUtil;
import com.google.appinventor.components.runtime.util.YailList;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import com.threed.jpct.DepthBuffer;
import gnu.math.Complex;
import gnu.math.DFloNum;
import gnu.math.IntNum;

/**
 * @author root
 *
 */
public final class Utils {
	public static Bitmap LoadBitmapFromView(View v) {
		int w = v.getWidth();
		int h = v.getHeight();

		Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmp);

		c.drawColor(Color.WHITE);
		/** 如果不设置canvas画布为白色，则生成透明 */

		v.layout(0, 0, w, h);
		v.draw(c);

		return bmp;
	}

	public static void DeleteComponent(Form form,AndroidViewComponent androidViewComponent) {
		View view = androidViewComponent.getView();
		ViewGroup vg = (ViewGroup) view.getParent();
		vg.removeView(view);
		form.deleteComponent(androidViewComponent);
	}
	
	/**
	 * @param Name
	 * @param Format
	 * @param Content
	 * @return
	 */
	public static String CreateTempFile(String Name, String Format, String Content) {
		File file;
		try {
			file = File.createTempFile(Name, Format);
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(Content);
			fileWriter.close();
			return file.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}
//
//
//	/*------------------------------------向量工具---------------------------------------*/
//	// 列表转三维向量
//	/**
//	 * JPCT的向量转appinventor的向量
//	 * 
//	 * @param list
//	 *            appinventor的向量
//	 * @return JPCT的向量
//	 */
//	public static SimpleVector List2Vector(YailList list) {
//		float x = ((Complex) list.getObject(0)).floatValue();
//		float y = ((Complex) list.getObject(1)).floatValue();
//		float z = ((Complex) list.getObject(2)).floatValue();
//		return new SimpleVector(x, y, z);
//	}
//
//	public static SimpleVector[] Lists2Vectors(YailList lists) {
//		SimpleVector[] simpleVectors = new SimpleVector[lists.size()];
//		for (int i = 0; i < lists.size(); i++) {
//			YailList list = (YailList) lists.get(i);
//			SimpleVector simpleVector = List2Vector(list);
//			simpleVectors[i] = simpleVector;
//		}
//		return simpleVectors;
//	}
//
//	public static YailList Vectors2Lists(SimpleVector[] simpleVectors) {
//		ArrayList<YailList> lists = new ArrayList<YailList>();
//		for (int i = 0; i < simpleVectors.length; i++) {
//			SimpleVector simpleVector = simpleVectors[i];
//			YailList list = Vector2List(simpleVector);
//			lists.add(list);
//		}
//		return YailList.makeList(lists);
//	}
//
//	/**
//	 * JPCT的向量转appinventor的向量
//	 * 
//	 * @param vector
//	 *            JPCT的向量
//	 * @return appinventor的向量
//	 */
//	public static YailList Vector2List(SimpleVector vector) {
//		ArrayList<Complex> arrayList = new ArrayList<Complex>();
//		arrayList.add(new DFloNum(vector.x));
//		arrayList.add(new DFloNum(vector.y));
//		arrayList.add(new DFloNum(vector.z));
//		return YailList.makeList(arrayList);
//	}
//
//	/**
//	 * 创建四维向量,appinventor中向量的数据结构采用长度为向量维度的列表存储
//	 * 
//	 * @param x
//	 *            向量的x分量
//	 * @param y
//	 *            向量的y分量
//	 * @param z
//	 *            向量的z分量
//	 * @param w
//	 *            向量的w分量
//	 * @return 四维向量
//	 */
//	public static YailList CreateVector4d(float x, float y, float z, float w) {
//		ArrayList<Complex> arrayList = new ArrayList<Complex>();
//		arrayList.add(new DFloNum(x));
//		arrayList.add(new DFloNum(y));
//		arrayList.add(new DFloNum(z));
//		arrayList.add(new DFloNum(w));
//		return YailList.makeList(arrayList);
//	}


	// float数组转列表
	/**
	 * float数组转数值列表
	 * 
	 * @param array
	 *            float数组
	 * @return 数值列表
	 */
	public static YailList FArray2List(float[] array) {
		ArrayList<Complex> arrayList = new ArrayList<Complex>(array.length);
		for (int i = 0; i < array.length; i++) {
			arrayList.add(new DFloNum(array[i]));
		}
		return YailList.makeList(arrayList);
	}

	public static YailList IArray2List(int[] array) {
		ArrayList<Complex> arrayList = new ArrayList<Complex>(array.length);
		for (int i = 0; i < array.length; i++) {
			arrayList.add(new IntNum(array[i]));
		}
		return YailList.makeList(arrayList);
	}
	
	// 列表转数组
	/**
	 * 数值列表转float数组，一般用于向量转换与矩阵数据提取
	 * 
	 * @param list
	 *            数值列表
	 * @return float数组
	 */
	public static float[] List2FArray(YailList list) {
		Object[] list1=list.toArray();
		float[] array = new float[list.size()];
		for (int i = 0; i < list1.length; i++) {
			array[i]=((Complex) list1[i]).floatValue();
		}
		return array;
	}

	
	
	public static int[] List2IArray(YailList list) {
		Object[] list1=list.toArray();
		int[] array = new int[list.size()];
		for (int i = 0; i < list1.length; i++) {
			array[i]=((Complex) list1[i]).intValue();
		}
		return array;
	}



	public static Object3D[] List2Object3Ds(YailList list) {
		Object3D[] object3ds=new Object3D[list.size()];
		for (int i = 0; i < object3ds.length; i++) {
			object3ds[i]=(Object3D) list.get(i);
		}
		return object3ds;
	}

	/**
	 * 用于将Map集合转换为appinventor中的列表键值对
	 * 
	 * @param map
	 *            Map集合
	 * @return 列表
	 */
	public static YailList Map2List(HashMap<String, ?> map) {
		Set<String> set = map.keySet();
		ArrayList<YailList> list = new ArrayList<YailList>();
		for (String key : set) {
			ArrayList<Object> object = new ArrayList<Object>();
			object.add(key);
			object.add(map.get(key));
			list.add(YailList.makeList(object));
		}
		return YailList.makeList(list);
	}


	public static YailList DepthBuffer2List(DepthBuffer depthBuffer) {
		int height=depthBuffer.getHeight();
		int width=depthBuffer.getWidth();
		HashMap<String, IntNum> map=new HashMap<String, IntNum>();
		map.put("height", new IntNum(height));
		map.put("width", new IntNum(width));
		return Map2List(map);
	}
	
	/*------------------------------------颜色转换类---------------------------------------*/

	public static int rgba2int(RGBColor color) {

		int A = color.getAlpha() & 0xFF, R = color.getRed() & 0xFF, G = color.getGreen() & 0xFF,
				B = color.getBlue() & 0xFF;
		return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 16 | (B & 0xff);
	}

	/**
	 * 用于解析appinventor中的颜色数据为RGBColor对象
	 * 
	 * @param color
	 *            appinventor中的颜色转RGBColor对象
	 * @return
	 */
	public static RGBColor int2rgba(int color) {
		int a = (color >> 24) & 0xff;
		int r = (color >> 16) & 0xff;
		int g = (color >> 8) & 0xff;
		int b = (color) & 0xff;
		return new RGBColor(r, g, b, a);
	}

	/**
	 * 用于将hsv颜色转换为rgb颜色
	 * 
	 * @param h
	 *            色调(0~360)
	 * @param s
	 *            饱和度(0~1)
	 * @param v
	 *            明度(0~1)
	 * @param a
	 *            透明度(0~1)
	 * @return RGBColor颜色对象
	 */
	public static RGBColor hsva2rgba(float h, float s, float v, float a) {
		h = h % 360;
		float f, p, q, t;
		if (s == 0) { // achromatic (grey)
			return new RGBColor((int) v * 255, (int) v * 255, (int) v * 255, (int) a * 255);
		}

		h /= 60; // sector 0 to 5
		int i = (int) Math.floor(h);
		f = h - i; // factorial part of h
		p = v * (1 - s);
		q = v * (1 - s * f);
		t = v * (1 - s * (1 - f));
		p = p * 255;
		q = q * 255;
		t = t * 255;
		v = v * 255;
		int vi = (int) v, ti = (int) t, pi = (int) p, ai = (int) a * 255, qi = (int) q;
		switch (i) {
		case 0:
			return new RGBColor(vi, ti, pi, ai);
		case 1:
			return new RGBColor(qi, vi, pi, ai);
		case 2:
			return new RGBColor(pi, vi, ti, ai);
		case 3:
			return new RGBColor(pi, qi, vi, ai);
		case 4:
			return new RGBColor(ti, pi, vi, ai);
		default: // case 5:
			return new RGBColor(vi, pi, qi, ai);
		}
	}
	
	public static String Save(Component component,Form form,Bitmap bitmap) {
		try {
			File file = FileUtil.getPictureFile("png");
			return saveFile(component,form,bitmap, file, Bitmap.CompressFormat.PNG, "Save");
		} catch (IOException e) {
			form.dispatchErrorOccurredEvent(component, "Save", ErrorMessages.ERROR_MEDIA_FILE_ERROR, e.getMessage());
		} catch (FileUtil.FileException e) {
			form.dispatchErrorOccurredEvent(component, "Save", e.getErrorMessageNumber());
		}
		return "";
	}

	// Helper method for Save and SaveAs
	public static String saveFile(Component component,Form form,Bitmap bitmap, File file, Bitmap.CompressFormat format, String method) {
		try {
			boolean success = false;
			FileOutputStream fos = new FileOutputStream(file);
			// Don't cache, in order to save memory. It seems unlikely to be used again
			// soon.
			try {
				success = bitmap.compress(format, 100, // quality: ignored for png
						fos);
			} finally {
				fos.close();
			}
			if (success) {
				return file.getAbsolutePath();
			} else {
				form.dispatchErrorOccurredEvent(component, method, ErrorMessages.ERROR_CANVAS_BITMAP_ERROR);
			}
		} catch (FileNotFoundException e) {
			form.dispatchErrorOccurredEvent(component, method, ErrorMessages.ERROR_MEDIA_CANNOT_OPEN,
					file.getAbsolutePath());
		} catch (IOException e) {
			form.dispatchErrorOccurredEvent(component, method, ErrorMessages.ERROR_MEDIA_FILE_ERROR, e.getMessage());
		}
		return "";
	}

	public static String SaveAs(Component component,Form form,String fileName, Bitmap bitmap) {
		// Figure out desired file format
		Bitmap.CompressFormat format;
		if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
			format = Bitmap.CompressFormat.JPEG;
		} else if (fileName.endsWith(".png")) {
			format = Bitmap.CompressFormat.PNG;
		} else if (!fileName.contains(".")) { // make PNG the default to match Save behavior
			fileName = fileName + ".png";
			format = Bitmap.CompressFormat.PNG;
		} else {
			form.dispatchErrorOccurredEvent(component, "SaveAs", ErrorMessages.ERROR_MEDIA_IMAGE_FILE_FORMAT);
			return "";
		}
		try {
			File file = FileUtil.getExternalFile(fileName);
			return saveFile(component,form,bitmap, file, format, "SaveAs");
		} catch (IOException e) {
			form.dispatchErrorOccurredEvent(component, "SaveAs", ErrorMessages.ERROR_MEDIA_FILE_ERROR, e.getMessage());
		} catch (FileUtil.FileException e) {
			form.dispatchErrorOccurredEvent(component, "SaveAs", e.getErrorMessageNumber());
		}
		return "";
	}
}
