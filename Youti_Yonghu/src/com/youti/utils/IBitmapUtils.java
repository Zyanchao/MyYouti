package com.youti.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;

import com.youti.appConfig.YoutiApplication;

public class IBitmapUtils {
	public IBitmapUtils() {

	}

	
	/* 圆角处理 */
	public static Bitmap toRoundCorner(Bitmap bitmap, float pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);

		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		System.out.println("pixels+++++++" + pixels);

		return output;
	}

	/* 高斯模糊处理 */
	public static Bitmap blurFilter(Bitmap bmp, int hRadius, int vRadius,
			int iterations) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		int[] inPixels = new int[width * height];
		int[] outPixels = new int[width * height];
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bmp.getPixels(inPixels, 0, width, 0, 0, width, height);
		for (int i = 0; i < iterations; i++) {
			blur(inPixels, outPixels, width, height, hRadius);
			blur(outPixels, inPixels, height, width, vRadius);
		}
		blurFractional(inPixels, outPixels, width, height, hRadius);
		blurFractional(outPixels, inPixels, height, width, vRadius);
		bitmap.setPixels(inPixels, 0, width, 0, 0, width, height);
		// Drawable drawable = new BitmapDrawable(bitmap);
		return bitmap;
	}

	public static void blur(int[] in, int[] out, int width, int height,
			float radius) {
		int widthMinus1 = width - 1;
		int r = (int) radius;
		int tableSize = 2 * r + 1;
		int divide[] = new int[256 * tableSize];

		for (int i = 0; i < 256 * tableSize; i++)
			divide[i] = i / tableSize;

		int inIndex = 0;

		for (int y = 0; y < height; y++) {
			int outIndex = y;
			int ta = 0, tr = 0, tg = 0, tb = 0;

			for (int i = -r; i <= r; i++) {
				int rgb = in[inIndex + clamp(i, 0, width - 1)];
				ta += (rgb >> 24) & 0xff;
				tr += (rgb >> 16) & 0xff;
				tg += (rgb >> 8) & 0xff;
				tb += rgb & 0xff;
			}

			for (int x = 0; x < width; x++) {
				out[outIndex] = (divide[ta] << 24) | (divide[tr] << 16)
						| (divide[tg] << 8) | divide[tb];

				int i1 = x + r + 1;
				if (i1 > widthMinus1)
					i1 = widthMinus1;
				int i2 = x - r;
				if (i2 < 0)
					i2 = 0;
				int rgb1 = in[inIndex + i1];
				int rgb2 = in[inIndex + i2];

				ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
				tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
				tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
				tb += (rgb1 & 0xff) - (rgb2 & 0xff);
				outIndex += height;
			}
			inIndex += width;
		}
	}

	public static void blurFractional(int[] in, int[] out, int width,
			int height, float radius) {
		radius -= (int) radius;
		float f = 1.0f / (1 + 2 * radius);
		int inIndex = 0;

		for (int y = 0; y < height; y++) {
			int outIndex = y;

			out[outIndex] = in[0];
			outIndex += height;
			for (int x = 1; x < width - 1; x++) {
				int i = inIndex + x;
				int rgb1 = in[i - 1];
				int rgb2 = in[i];
				int rgb3 = in[i + 1];

				int a1 = (rgb1 >> 24) & 0xff;
				int r1 = (rgb1 >> 16) & 0xff;
				int g1 = (rgb1 >> 8) & 0xff;
				int b1 = rgb1 & 0xff;
				int a2 = (rgb2 >> 24) & 0xff;
				int r2 = (rgb2 >> 16) & 0xff;
				int g2 = (rgb2 >> 8) & 0xff;
				int b2 = rgb2 & 0xff;
				int a3 = (rgb3 >> 24) & 0xff;
				int r3 = (rgb3 >> 16) & 0xff;
				int g3 = (rgb3 >> 8) & 0xff;
				int b3 = rgb3 & 0xff;
				a1 = a2 + (int) ((a1 + a3) * radius);
				r1 = r2 + (int) ((r1 + r3) * radius);
				g1 = g2 + (int) ((g1 + g3) * radius);
				b1 = b2 + (int) ((b1 + b3) * radius);
				a1 *= f;
				r1 *= f;
				g1 *= f;
				b1 *= f;
				out[outIndex] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
				outIndex += height;
			}
			out[outIndex] = in[width - 1];
			inIndex += width;
		}
	}

	public static int clamp(int x, int a, int b) {
		return (x < a) ? a : (x > b) ? b : x;
	}

	public static int dip2px(float dipValue) {
		final float scale = YoutiApplication.getInstance().getResources()
				.getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(float pxValue) {
		final float scale = YoutiApplication.getInstance().getResources()
				.getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 从服务器取图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmapFromUrl(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;

		try {
			myFileUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setConnectTimeout(0);
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * 通过图片路径，获取压缩的bitmap
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static Bitmap getBitmapFromPath(String imgFile, int minSideLength,
			int maxNumOfPixels) {
		if (imgFile == null || imgFile.length() == 0)
			return null;

		try {
			FileDescriptor fd = new FileInputStream(imgFile).getFD();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			// BitmapFactory.decodeFile(imgFile, options);
			BitmapFactory.decodeFileDescriptor(fd, null, options);

			options.inSampleSize = computeSampleSize(options, minSideLength,
					maxNumOfPixels);
			try {
				// 这里一定要将其设置回false，因为之前我们将其设置成了true
				// 设置inJustDecodeBounds为true后，decodeFile并不分配空间，即，BitmapFactory解码出来的Bitmap为Null,但可计算出原始图片的长度和宽度
				options.inJustDecodeBounds = false;

				Bitmap bmp = BitmapFactory.decodeFile(imgFile, options);
				return bmp == null ? null : bmp;
			} catch (OutOfMemoryError err) {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * compute Sample Size
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	/**
	 * compute Initial Sample Size
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		// 上下限范围
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 缩放Bitmap
	 * 
	 * @param oldWidth
	 * @param oldHeight
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitMap, int width, int height) {
		float scale = IBitmapUtils.reckonThumbnail(bitMap.getWidth(),
				bitMap.getHeight(), width, height);
		return IBitmapUtils.PicZoom(bitMap, (int) (bitMap.getWidth() / scale),
				(int) (bitMap.getHeight() / scale));
	}

	/**
	 * 计算缩放比
	 * 
	 * @param oldWidth
	 * @param oldHeight
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	private static float reckonThumbnail(int oldWidth, int oldHeight,
			int newWidth, int newHeight) {
		
		float be = Math.max(oldWidth/newWidth, oldHeight/newHeight);
		if (be <= 1)be=1;
		return be;
//		if ((oldHeight > newHeight && oldWidth > newWidth)
//				|| (oldHeight <= newHeight && oldWidth > newWidth)) {
//			float be = oldWidth / (float) newWidth;
//			if (be <= 1)
//				be = 1;
//			return be;
//		} else if (oldHeight > newHeight && oldWidth <= newWidth) {
//			float be = oldHeight / (float) newHeight;
//			if (be <= 1)
//				be = 1;
//			return be;
//		}

		//return 1;
	}

	/**
	 * 缩放图片
	 * 
	 * @param bmp
	 * @param width
	 * @param height
	 * @return
	 */
	private static Bitmap PicZoom(Bitmap bmp, int width, int height) {
		int bmpWidth = bmp.getWidth();
		int bmpHeght = bmp.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale((float) width / bmpWidth, (float) height / bmpHeght);

		return Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeght, matrix, true);
	}

	/**
	 * 从URI获取bitmap
	 * 
	 * @param cr
	 * @param uri
	 * @return bitmap
	 */
	public static Bitmap getBitmapFromUri(ContentResolver cr, Uri uri) {
		try {
			// 读取uri所在的图片
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 从URI获取path
	 * 
	 * @param activity
	 * @param uri
	 * @return path
	 */
	public static String getPathFromUri(Activity activity, Uri uri) {
		// ((ImageView)findViewById(R.id.testImg)).setImageURI(uri);
		String[] proj = { MediaStore.Images.Media.DATA };
		// 好像是android多媒体数据库的封装接口，具体的看Android文档
		Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}

	/**
	 * 从Bitmap转换成Byte[]
	 * 
	 * @param activity
	 * @param uri
	 * @return path
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	/**
	 * 从Byte[]转换成Bitmap
	 * 
	 * @param activity
	 * @param uri
	 * @return path
	 */
	public static Bitmap Bytes2Bitmap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

}

