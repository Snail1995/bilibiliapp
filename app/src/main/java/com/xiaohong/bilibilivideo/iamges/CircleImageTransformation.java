package com.xiaohong.bilibilivideo.iamges;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

/**
 * Created by XIAOHONG.
 * on 2016/10/15.
 * and BilibiliSuperApp
 */

/**
 *  使用Transformation, 在picasso使用,让图片变圆
 */
public  class CircleImageTransformation implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap ret = null;

        int sw = source.getWidth();
        int sh = source.getHeight();

        int bw = Math.min(sw, sh);


        // 1. 找到原始图片的中心位置

        int scx = sw >> 1;
        int hbw = bw >> 1;

        ret = Bitmap.createBitmap(source, scx - hbw, 0, bw, bw);
        Canvas canvas = new Canvas(ret);
        BitmapShader shader = new BitmapShader(source,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);

        Paint paint = new Paint();
        paint.setShader(shader);

        // 生成新的内容
        ret = Bitmap.createBitmap(bw, bw, Bitmap.Config.RGB_565);

        int half = bw >> 1;
        canvas.drawCircle(half, half, half, paint);
        canvas = null;
        paint = null;
        // 原始图像就不需使用了
        source.recycle();
        return ret;
    }

    /**
     * 名称 唯一标识
     *
     * @return
     */
    @Override
    public String key() {
        return "";
    }
}
