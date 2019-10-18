package org.khanacademy.theoskol;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

class GameCode {
    // Text vars
    private final static int CENTER = 0;
    private final static int BOTTOM = 2;
    private final static int LEFT = 3;
    private final static int RIGHT = 4;
    private final static int BASELINE = 5;
    private static float tl; // Text Leading: the vertical distance between adjacent lines of text
    private static float ta; // Text Ascent: the vertical distance between the "inner floor" and the "outer ceiling" of the text
    private static float td; // Text Descent: the vertical distance between the "inner floor" and the "outer floor" of the text
    private static int tah = LEFT; // Text Align Horizontal
    private static int tav = BOTTOM; // Text Align Vertical

    // Drawing vars
    private final static float width = 400; // Original Canvas Width
    private final static float height = 400; // Original Canvas Height
    private static Paint pf = new Paint(Paint.ANTI_ALIAS_FLAG); // Paint fill
    private static Paint ps = new Paint(Paint.ANTI_ALIAS_FLAG); // Paint stroke
    private static Paint pm = new Paint(Paint.ANTI_ALIAS_FLAG); // Paint miscellaneous
    private static Canvas canvas;

    // Mouse vars
    private static float mouseX, mouseY;
    private static boolean mouseJustReleased, mouseIsPressed;

    // Misc vars
    private static Path mp; // Miscellaneous Path
    private static long frameCount = 0;

    // PJS vars


    static void init() {
        ps.setStrokeCap(Paint.Cap.ROUND);
        pf.setStyle(Paint.Style.FILL);
        ps.setStyle(Paint.Style.STROKE);
        pm.setStyle(Paint.Style.FILL);
        pm.setColor(Color.BLACK);
    }
    static void draw(Canvas c) {
        frameCount ++;
        canvas = c;

        if (Screen.width > Screen.height) {
            canvas.save();
            canvas.translate((Screen.width - (Screen.height * width / height)) / 2f, 0f);
            canvas.scale(Screen.height / height, Screen.height / height, 0f, 0f);
            pjsCode();
            canvas.restore();
            canvas.drawRect(0f, 0f, (Screen.width - (Screen.height * width / height)) / 2f, Screen.height, pm);
            canvas.drawRect(Screen.height + (Screen.width - (Screen.height * width / height)) / 2f, 0, Screen.width, Screen.height, pm);
        } else {
            canvas.save();
            canvas.translate(0f, (Screen.height - (Screen.width * height / width)) / 2f);
            canvas.scale(Screen.width / width, Screen.width / width, 0f, 0f);
            pjsCode();
            canvas.restore();
            canvas.drawRect(0f, 0f, Screen.width, (Screen.height - (Screen.width * height / width)) / 2f, pm);
            canvas.drawRect(0f, Screen.width + (Screen.height - (Screen.width * height / width)) / 2f, Screen.width, Screen.height, pm);
        }

        mouseJustReleased = false;
    }
    private static void background() {
        Paint paint = new Paint();
        paint.set(pf);
        pf.setColor(Color.rgb(107, 72, 1));
        canvas.drawRect(0f, 0f, 400f, 400f, pf);
        pf.set(paint);
    }
    private static void noFill() {
        pf.setColor(Color.TRANSPARENT);
    }
    private static void fill(int r, int g, int b, float a) {
        pf.setColor(Color.argb((int) a, r, g, b));
    }
    private static void fill(int r, int g, int b) {
        pf.setColor(Color.rgb(r, g, b));
    }
    private static void fill(int shade) {
        pf.setColor(Color.rgb(shade, shade, shade));
    }
    private static void noStroke() {
        ps.setColor(Color.TRANSPARENT);
    }
    private static void stroke(int r, int g, int b, float a) {
        ps.setColor(Color.argb((int) (a > 0 ? (a < 255 ? a : 255) : 0), r, g, b));
    }
    private static void stroke(int r, int g, int b) {
        ps.setColor(Color.rgb(r, g, b));
    }
    private static void stroke(int shade) {
        ps.setColor(Color.rgb(shade, shade, shade));
    }
    private static void strokeWeight(float w) {
        ps.setStrokeWidth(w);
    }
    private static void line(float x1, float y1, float x2, float y2) {
        canvas.drawLine(x1, y1, x2, y2, ps);
    }
    private static void rect(float x, float y, float w, float h) {
        canvas.drawRect(x, y, x + w, y + h, pf);
        canvas.drawRect(x, y, x + w, y + h, ps);
    }
    private static void ellipse(float x, float y, float w, float h) {
        RectF r = new RectF(x - (w / 2f), y - (h / 2f), x + (w / 2f), y + (h / 2f));
        canvas.drawOval(r, pf);
        canvas.drawOval(r, ps);
    }
    private static void text(String txt, float x, float y) {
        drawMultilineText(txt, x, y);
    }
    private static void text(int txt, float x, float y) {
        drawMultilineText(String.valueOf(txt), x, y);
    }
    private static void drawMultilineText(String str, float x, float y) {
        String[] lines = str.split("\n");

        pushMatrix();
        if (tav == BOTTOM) {
            translate(0, y - tl * (lines.length - 1) - td);
        } else if (tav == BASELINE) {
            translate(0, y);
        } else if (tav == CENTER) {
            translate(0, y - tl * (lines.length - 1) / 2 + ta / 2);
        } else { // Text Align Vertical = TOP
            translate(0, y + ta);
        }

        if (tah == LEFT) {
            for (int i = 0; i < lines.length; i++) {
                canvas.drawText(lines[i], x, i * tl, pf);
            }
        } else if (tah == CENTER) {
            for (int i = 0; i < lines.length; i++) {
                canvas.drawText(lines[i], x - textWidth(lines[i]) / 2, i * tl, pf);
            }
        } else { // Text Align Horizontal = RIGHT
            for (int i = 0; i < lines.length; i++) {
                canvas.drawText(lines[i], x - textWidth(lines[i]), i * tl, pf);
            }
        }
        popMatrix();
    }
    private static void textAlign(int h, int v) { // Use ints CENTER, LEFT, RIGHT, TOP or BOTTOM.
        tah = h;
        tav = v;
    }
    private static void textSize(float size) {
        if (size < 0) {
            return;
        }
        pf.setTextSize(size);
        ta = pf.getTextSize() * 0.716f;
        td = pf.getTextSize() * 0.211f;
        tl = pf.getTextSize() * 1.149f;
    }
    private static float textWidth(String txt) {
        float w = 0;
        String[] lines = txt.split("\n");
        for (String line : lines) {
            w = pf.measureText(line) > w ? pf.measureText(line) : w;
        }
        return w;
    }
    private static void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        mp = new Path();
        mp.moveTo(x1, y1);
        mp.lineTo(x2, y2);
        mp.lineTo(x3, y3);
        mp.close();
        canvas.drawPath(mp, pf);
        canvas.drawPath(mp, ps);
    }
    private static void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        mp = new Path();
        mp.moveTo(x1, y1);
        mp.lineTo(x2, y2);
        mp.lineTo(x3, y3);
        mp.lineTo(x4, y4);
        mp.close();
        canvas.drawPath(mp, pf);
        canvas.drawPath(mp, ps);
    }
    private static void arc(float x, float y, float w, float h, float start, float stop) {
        RectF r = new RectF(x - w / 2, y - h / 2, x + w / 2, y + h / 2);
        canvas.drawArc(r, start, stop - start, false, ps);
        r = new RectF(x - w / 2 + ps.getStrokeWidth() / 2, y - h / 2 + ps.getStrokeWidth() / 2, x + w / 2 - ps.getStrokeWidth() / 2, y + h / 2 - ps.getStrokeWidth() / 2);
        canvas.drawArc(r, start, stop - start, true, pf);
    }
    private static double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
    private static double sin(double degrees) {
        return Math.sin(degrees * 2 * Math.PI / 360);
    }
    private static double cos(double degrees) {
        return Math.cos(degrees * 2 * Math.PI / 360);
    }
    private static void beginShape() {
        mp = new Path();
    }
    private static void vertex(float x, float y) {
        if (mp.isEmpty()) {
            mp.moveTo(x, y);
        } else {
            mp.lineTo(x, y);
        }
    }
    private static void endShape() {
        mp.close();
        canvas.drawPath(mp, pf);
        canvas.drawPath(mp, ps);
    }
    private static double random(double low, double high) {
        return (high - low) * Math.random() + low;
    }
    private static int round(double num) {
        return (int) num;
    }
    private static void point(float x, float y) {
        canvas.drawPoint(x, y, ps);
    }
    private static void pushMatrix() {
        canvas.save();
    }
    private static void popMatrix() {
        canvas.restore();
    }
    private static void translate(float x, float y) {
        canvas.translate(x, y);
    }
    private static void rotate() {
        canvas.rotate(-90, 0, 0);
    }
    private static void pjsCode() {

    }
}
