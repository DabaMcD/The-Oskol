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

    // Misc vars
    private static Path mp; // Miscellaneous Path
    private static int frameCount = 0;
    private static String angleMode = "degrees";
    private static RectF mrf = new RectF(); // Miscellaneous Rect Float

    // PJS vars
    private static int sheetPlus = 0;

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
    }
    private static void background(int r, int g, int b) {
        pm.set(pf);
        pf.setColor(Color.rgb(r, g, b));
        canvas.drawRect(0f, 0f, 400f, 400f, pf);
        pf.set(pm);
        pm.setColor(Color.BLACK);
    }
    private static void background(int shade) {
        pm.set(pf);
        pm.setColor(Color.rgb(shade, shade, shade));
        canvas.drawRect(0f, 0f, 400f, 400f, pf);
        pf.set(pm);
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
    private static void rect(float x, float y, float w, float h, float r) {
        mrf = new RectF(x, y, x + w, y + h);
        canvas.drawRoundRect(mrf, r, r, pf);
        canvas.drawRoundRect(mrf, r, r, ps);
    }
    private static void ellipse(float x, float y, float w, float h) {
        mrf = new RectF(x - (w / 2f), y - (h / 2f), x + (w / 2f), y + (h / 2f));
        canvas.drawOval(mrf, pf);
        canvas.drawOval(mrf, ps);
    }
    private static void textLeading(float dist) {
        tl = dist;
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
        mrf = new RectF(x - w / 2, y - h / 2, x + w / 2, y + h / 2);
        canvas.drawArc(mrf, start, stop - start, false, ps);
        mrf = new RectF(x - w / 2 + ps.getStrokeWidth() / 2, y - h / 2 + ps.getStrokeWidth() / 2, x + w / 2 - ps.getStrokeWidth() / 2, y + h / 2 - ps.getStrokeWidth() / 2);
        canvas.drawArc(mrf, start, stop - start, true, pf);
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
    private static double tan(double degrees) {
        return Math.tan(degrees * 2 * Math.PI / 360);
    }
    private static double asin(double degrees) {
        return Math.asin(degrees * 2 * Math.PI / 360);
    }
    private static double acos(double degrees) {
        return Math.acos(degrees * 2 * Math.PI / 360);
    }
    private static double atan(double degrees) {
        return Math.atan(degrees * 2 * Math.PI / 360);
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
    private static double random(double high) {
        return high * Math.random();
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
    private static void rotate(float angle) {
        canvas.rotate((angleMode.equals("degrees")) ? angle : (float) (angle * 360 / (2 * Math.PI)), 0, 0);
    }
    private static void scale(float sx, float sy) {
        canvas.scale(sx, sy, 0, 0);
    }
    private static void scale(float s) {
        canvas.scale(s, s, 0, 0);
    }
    private static void bezierVertex(float x1, float y1, float x2, float y2, float x3, float y3) {
        mp.cubicTo(x1, y1, x2, y2, x3, y3);
    }
    private static void bezier(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        mp = new Path();
        mp.moveTo(x1, y1);
        mp.cubicTo(x2, y2, x3, y3, x4, y4);
        canvas.drawPath(mp, ps);
        canvas.drawPath(mp, pf);
    }

    // PJS functions
    private static void sheet(float x, float y, float w, float h, float r, boolean end) {
        pushMatrix();
        translate(x, y);
        rotate(r);
        scale(w / 100, h / 100);
        noStroke();
        fill(100 + sheetPlus, 20 + sheetPlus, (int) (10 + sheetPlus / 1.5));
        sheetPlus += 20;
        beginShape();
        vertex(0, 5);
        bezierVertex(40, -5, 75, -5, 100, 0);
        if (end) {
            vertex(110, -5);
            vertex(105, 5);
            vertex(115, 5);
            vertex(105, 10);
            vertex(110, 15);
        }
        vertex(100, 12);
        bezierVertex(90, 20, 80, 30, 70, 30);
        bezierVertex(50, 30, 30, 35, 25, 30);
        bezierVertex(30, 20, 15, 5, 0, 5);
        endShape();
        fill(255, 255, 245, 50);
        beginShape();
        vertex(20, 5);
        bezierVertex(20, 0, 60, 0, 90, 5);
        bezierVertex(70, 15, 50, 3, 20, 5);
        endShape();
        stroke(0, 0, 50, 100);
        strokeWeight(2);
        noFill();
        bezier(25, 10, 32, 20, 30, 25, 30, 25);
        line(30, 15, 60, 15);
        line(35, 20, 80, 20);
        arc(40, 10, 4, 4, 20, 230);
        arc(30, 8, 3, 3, 20, 230);
        popMatrix();
    }

    private static void spike(float x, float y, float w, float h, float r) {
        pushMatrix();
        translate(x, y);
        rotate(r);
        scale(w / 100, h / 100);
        fill(240, 230, 190);
        ellipse(0, 0, 40, 30);
        triangle(-20, 0, 20, 0, 0, -100);
        fill(0, 0, 50, 50);
        triangle(-20, 0, -5, -5, 0, -100);
        popMatrix();
    }

    private static void toe(float x, float y, float w, float h, float r) {
        pushMatrix();
        translate(x, y);
        rotate(r);
        scale(w / 100, h / 100);
        noStroke();
        fill(240, 230, 190);
        beginShape();
        vertex(5, 0);
        bezierVertex(10, 10, 5, 35, 0, 40);
        bezierVertex(-5, 35, -10, 10, -5, 0);
        endShape();
        popMatrix();
    }

    private static void head(float x, float y, float w, float h, float r) {
        pushMatrix();
        translate(x, y);
        rotate(r);
        translate(-50, 0);
        scale(w / 100, h / 100);
        noStroke();

        // Blade
        fill(130, 110, 125);
        beginShape();
        vertex(60, 30);
        vertex(80, 30);
        vertex(85, 55);
        bezierVertex(100, 60, 130, 55, 150, 45);
        bezierVertex(130, 65, 100, 70, 60, 70);
        vertex(65, 50);
        endShape();
        fill(255, 255, 245, 50);
        beginShape();
        vertex(150, 45);
        bezierVertex(130, 65, 100, 70, 60, 70);
        vertex(62, 60);
        bezierVertex(100, 65, 130, 60, 150, 45);
        endShape();

        // Jets
        stroke(180, 120, 70);
        noFill();
        strokeWeight(15);
        arc(60, 40, 100, 100, 111, 160);
        strokeWeight(10);
        arc(-40, 0, 80, 80, 81, 130);
        strokeWeight(8);
        stroke(0, 0, 50, 50);
        arc(60, 40, 107, 107, 111, 160);
        strokeWeight(5);
        arc(-40, 0, 84, 84, 81, 130);
        noStroke();
        fill(0, 0, 50, 60);
        ellipse(43, 87, 15, 15);
        ellipse(43, 87, 10, 8);
        noStroke();

        // Mouth
        pushMatrix();
        rotate(30);
        translate(0, 20);
        stroke(200, 190, 170);
        fill(0, 0, 80, 100);
        strokeWeight(3.5f);
        arc(0f, 35f, 45f, (float) (103f + sin(frameCount * 8f) * 5f), 0f, 90f);
        arc(0f, 35f, 35f, (float) (103f + sin(frameCount * 8f) * 5f), 91f, 180f);
        strokeWeight(7);
        arc(0, 35, 40, 55, 0, 90);
        arc(0, 35, 30, 55, 91, 180);
        noStroke();
        fill(240, 230, 190);
        beginShape();
        vertex(-12, 20);
        vertex(12, 20);
        vertex(7, 65);
        vertex(15, 75);
        vertex(3, 70);
        vertex(5, 75);
        vertex(-2, 70);
        vertex(-10, 75);
        vertex(-7, 70);
        vertex(-15, 72);
        vertex(-8, 65);
        endShape();
        pushMatrix();
        translate(0, (float) (2 + sin(frameCount * 8) * 2.5));
        beginShape();
        vertex(10, 75);
        vertex(8, 85);
        vertex(2, 87);
        vertex(-8, 85);
        vertex(-10, 75);
        vertex(-5, 80);
        vertex(-3, 77);
        vertex(5, 80);
        endShape();
        popMatrix();
        strokeWeight(2);
        stroke(0, 0, 50, 30);
        line(5, 0, 5, 60);
        line(-2, 50, -2, 65);
        line(-6, 0, -6, 58);
        popMatrix();

        // Skin
        fill(60, 0, 20);
        beginShape();
        vertex(-10, 20);
        vertex(50, 0);
        vertex(150, 20);
        vertex(150, 30);
        vertex(100, 40);
        bezierVertex(95, 50, 80, 40, 70, 40);
        bezierVertex(45, 35, 50, 50, 50, 60);
        vertex(35, 55);
        vertex(35, 60);
        vertex(30, 60);
        bezierVertex(32, 65, 35, 65, 35, 65);
        bezierVertex(35, 70, 20, 70, 15, 65);
        bezierVertex(15, 75, 20, 80, 25, 75);
        bezierVertex(25, 90, 10, 80, 5, 70);
        bezierVertex(5, 90, -10, 90, -10, 80);
        bezierVertex(-10, 70, 0, 65, -10, 60);
        vertex(-15, 55);
        endShape();
        pushMatrix();
        translate(-7, 5);
        beginShape();
        vertex(-20, 10);
        vertex(-35, 40);
        bezierVertex(-40, 65, -50, 50, -50, 50);
        bezierVertex(-55, 50, -60, 45, -60, 40);
        bezierVertex(-55, 45, -50, 45, -45, 45);
        bezierVertex(-40, 40, -45, 35, -40, 30);
        vertex(-45, 30);
        vertex(-40, 25);
        bezierVertex(-50, 15, -25, 25, -35, 15);
        endShape();
        popMatrix();

        // Face
        pushMatrix();
        rotate(35);
        translate(5, 20);
        fill(80, 40, 40);
        beginShape();
        vertex(-20, 10);
        vertex(20, 10);
        vertex(15, 40);
        vertex(20, 45);
        vertex(10, 40);
        vertex(10, 45);
        vertex(5, 40);
        vertex(0, 50);
        vertex(0, 40);
        vertex(-5, 40);
        vertex(-2, 50);
        vertex(-15, 40);
        endShape();
        fill(0, 0, 50, 50);
        triangle(0, 25, 10, 30, -5, 40);
        triangle(-15, 25, -5, 30, -8, 40);
        rotate(-10);
        fill(0, 0, 50, 100);
        ellipse(0, 15, 45, 20);

        // Eyes
        fill(210, 230, 50);
        pushMatrix();
        translate(-20, 20);
        rotate(30);
        ellipse(0, 0, 17, (float) (4 + sin(frameCount * 6)));
        popMatrix();
        pushMatrix();
        translate(5, 24);
        rotate(-5);
        ellipse(0, 0, 19, (float) (6 + sin(frameCount * 6)));
        popMatrix();
        popMatrix();

        // Shell
        sheet(-40, 15, 90, 120, (float) (-10 + sin(r) * 2), false);
        sheet(0, -3, 100, 100, (float) (sin(r * 15 + 60) * 2), false);
        sheet(50, -12, 80, 105, (float) (8 + sin(r * 20 + 120) * 2), false);
        sheet(90, -16, 80, 110, (float) (15 + sin(r * 30 + 190) * 2), true);

        // Particles
        pushMatrix();
        rotate(20);
        fill(230, 230, 60, 100);
        stroke(210, 230, 50, 50);
        strokeWeight(2);
        ellipse(8, 42, (float) (10 + sin(frameCount * 130)), 5);
        ellipse(20, 37, (float) (11 + sin(frameCount * 130)), 3);
        rotate(30);
        ellipse(-8, 40, (float) (8 + sin(frameCount * 130)), 4);
        ellipse(-14, 33, (float) (6 + sin(frameCount * 130)), 3);
        popMatrix();
        popMatrix();
    }

    private static void neck(float x, float y, float w, float h, float r) {
        pushMatrix();
        translate(x, y);
        rotate(r);
        scale(w / 100, h / 100);
        noStroke();

        // Main neck
        fill(130, 70, 50);
        beginShape();
        vertex(0, 0);
        vertex(30, -40);
        vertex(100, -10);
        vertex(90, 40);
        vertex(60, 50);
        endShape();

        fill(255, 255, 245, 50);
        quad(0, 0, 10, -10, 90, 40, 60, 50);
        fill(130, 70, 50);
        ellipse(50, 25, 4, 4);
        ellipse(55, 35, 6, 6);
        strokeWeight(2);
        stroke(130, 70, 50);
        line(60, 30, 60, 10);
        line(65, 40, 68, 10);
        line(75, 35, 72, 10);

        noFill();
        stroke(255, 255, 245, 50);
        strokeWeight(5);
        bezier(0, 0, 40, -5, 50, 50, 70, 45);

        sheet(60, -30, 70, 100, 30, false);
        sheet(30, -50, 70, 100, 28, false);
        popMatrix();
    }

    private static void body(float x, float y, float w, float h, float r) {
        pushMatrix();
        translate(x, y);
        rotate(r);
        scale(w / 100, h / 100);
        noStroke();
        fill(130, 110, 125);
        beginShape();
        vertex(50, -30);
        bezierVertex(60, -60, 70, 10, 90, -10);
        vertex(85, 5);
        vertex(95, 0);
        vertex(85, 10);
        vertex(90, 15);
        vertex(85, 20);
        vertex(80, 25);
        endShape();
        fill(255, 255, 245, 50);
        beginShape();
        vertex(50, -30);
        bezierVertex(60, -60, 70, 10, 90, -10);
        bezierVertex(70, 20, 60, -50, 50, -30);
        endShape();

        fill(80, 30, 25);
        beginShape();
        vertex(0, 0);
        vertex(50, -30);
        vertex(70, -10);
        bezierVertex(100, 20, 70, 70, 60, 100);
        vertex(70, 120);
        vertex(60, 115);
        vertex(20, 120);
        vertex(15, 70);
        bezierVertex(10, 50, -10, 30, 0, 0);
        endShape();

        fill(0, 0, 50, 50);
        beginShape();
        vertex(0, 0);
        vertex(30, -30);
        bezierVertex(70, 30, 40, 60, 40, 117);
        vertex(20, 120);
        vertex(15, 70);
        bezierVertex(10, 50, -10, 30, 0, 0);
        endShape();
        beginShape();
        vertex(0, 0);
        vertex(30, -30);
        vertex(50, -25);
        vertex(50, -10);
        bezierVertex(70, 30, 40, 60, 47, 65);
        vertex(43, 65);
        vertex(45, 75);
        vertex(35, 65);
        vertex(30, 80);
        vertex(25, 70);
        vertex(15, 80);
        vertex(15, 70);
        bezierVertex(10, 50, -10, 30, 0, 0);
        endShape();
        noFill();
        strokeWeight(2);
        stroke(70, 30, 35);
        beginShape();
        vertex(10, 50);
        vertex(15, 45);
        vertex(30, 44);
        vertex(37, 49);
        vertex(50, 48);
        endShape();
        line(40, 20, 50, 15);
        line(35, 30, 55, 20);
        arc(10, 15, 4, 4, 20, 230);
        arc(15, 25, 6, 6, 20, 230);
        arc(35, 10, 5, 5, 20, 230);
        strokeWeight(10);
        stroke(255, 255, 245, 20);
        bezier(5, 20, 5, 35, 30, 55, 25, 90);
        sheet(70, 60, 60, 70, 90, true);
        sheet(90, 20, 80, 80, 100, false);
        popMatrix();
    }

    private static void arm(float x, float y, float size, float shoulder, float elbow, float hand, boolean flip) {
        pushMatrix();
        translate(x, y);
        rotate(shoulder);
        scale(size / 100 * (flip ? -1 : 1), size / 100);
        noStroke();

        // Second Arm Segment
        pushMatrix();
        translate(50, 10);
        rotate(elbow);
        translate(0, -5);
        fill(160, 100, 60);
        beginShape();
        vertex(0, 0);
        bezierVertex(20, 5, 40, 5, 60, 0);
        vertex(60, 25);
        bezierVertex(50, 23, 35, 23, 25, 30);
        vertex(20, 28);
        bezierVertex(15, 20, 5, 18, 0, 15);
        endShape();
        fill(0, 0, 50, 50);
        beginShape();
        vertex(60, 15);
        vertex(60, 25);
        bezierVertex(50, 23, 35, 23, 25, 30);
        vertex(20, 28);
        bezierVertex(15, 20, 5, 18, 0, 15);
        endShape();
        noFill();
        strokeWeight(2);
        stroke(70, 30, 35, 150);
        arc(35, 5, 3, 3, 20, 230);
        bezier(0, 10, 30, 15, 40, 10, 60, 20);
        bezier(0, 5, 30, 40, 40, 0, 60, 10);

        // Hand
        translate(55, 5);
        rotate(hand);
        noStroke();
        fill(180, 120, 70);
        beginShape();
        vertex(0, -10);
        bezierVertex(5, 0, 10, 0, 15, 0);
        vertex(20, -3);
        vertex(40, -3);
        vertex(40, 5);
        bezierVertex(38, 10, 38, 15, 40, 20);
        vertex(25, 25);
        bezierVertex(20, 20, 15, 20, 15, 20);
        vertex(5, 20);
        bezierVertex(-5, 15, -5, 0, 0, -5);
        endShape();

        fill(0, 0, 50, 30);
        ellipse(25, 10, 15, 18);

        fill(0, 0, 50, 50);
        pushMatrix();
        translate(20, 10);
        rotate(45);
        rect(-4f, -4f, 8f, 8f, 2f);
        rotate(-5);
        rect(10, -3, 3, 3);
        rotate(20);
        rect(0, -13, 5, 5);
        popMatrix();

        fill(240, 230, 190);

        // Thumb
        beginShape();
        vertex(21, -3);
        vertex(28, -3);
        bezierVertex(23, -5, 23, -10, 25, -17);
        vertex(35, -20);
        bezierVertex(30, -23, 25, -23, 20, -18);
        bezierVertex(19, -15, 19, -8, 21, -3);
        endShape();

        // First Finger
        beginShape();
        vertex(40, -3);
        vertex(40, 5);
        bezierVertex(45, 2, 50, 6, 55, 5);
        bezierVertex(58, 0, 58, -5, 55, -14);
        bezierVertex(53, -5, 53, -3, 50, 0);
        bezierVertex(45, -2, 40, -3, 40, -3);
        endShape();

        // Second Finger
        beginShape();
        vertex(40, 18);
        vertex(35, 22);
        vertex(50, 25);
        bezierVertex(55, 22, 56, 18, 55, 13);
        vertex(50, 20);
        endShape();
        popMatrix();

        // First Arm Segment
        fill(130, 70, 50);
        noStroke();
        beginShape();
        vertex(0, 0);
        vertex(50, 0);
        bezierVertex(50, 10, 60, 25, 70, 30);
        vertex(0, 25);
        endShape();
        for (private static void i = 4; i >= 0; i--) {
            fill(130 - i * 15, 70 - i * 15, 50 - i * 8);
            ellipse(20 - i * 5, 13, 15, 26);
        }
        fill(0, 0, 50, 50);
        triangle(0, 15, 70, 30, 0, 25);
        noFill();
        strokeWeight(2);
        stroke(70, 30, 35, 150);
        arc(35, 10, 5, 5, 20, 230);
        arc(45, 15, 4, 4, 20, 230);
        bezier(0, 10, 30, 10, 40, 30, 60, 30);
        popMatrix();
    }

    private static void leg = function(x, y, size, hip, knee, foot, flip) {
        pushMatrix();
        translate(x, y);
        rotate(hip);
        scale(size / 100 * (flip ? -1 : 1), size / 100);
        noStroke();

        // Second Leg Segment
        pushMatrix();
        translate(0, 50);
        rotate(knee);
        translate(-11, 0);
        fill(160, 100, 60);
        quad(0, -5, 25, 0, 35, 55, 0, 50);
        fill(0, 0, 50, 50);
        triangle(0, -5, 0, 50, 20, 52);
        noFill();
        strokeWeight(2);
        stroke(70, 30, 35, 150);
        arc(5, 10, 4, 4, 20, 230);
        arc(25, 20, 3, 3, 20, 230);
        bezier(10, 0, 5, 15, 30, 35, 25, 53);
        bezier(20, 0, 25, 20, 5, 40, 5, 50);

        // Foot
        translate(15, 50);
        rotate(foot);
        translate(-15, 0);
        noStroke();
        fill(180, 120, 70);
        beginShape();
        vertex(5, -10);
        bezierVertex(0, 10, -15, 20, -15, 30);
        bezierVertex(0, 30, 20, 35, 40, 35);
        bezierVertex(30, 25, 38, 10, 35, -5);
        vertex(25, 0);
        endShape();
        strokeWeight(6);
        stroke(255, 255, 245, 50);
        bezier(4, 8, 8, 10, -2, 15, -7, 25);
        strokeWeight(2);
        stroke(0, 0, 50, 50);
        line(20, 20, 30, 30);
        line(15, 23, 25, 31);
        line(27, 21, 32, 25);
        stroke(70, 30, 35, 150);
        bezier(20, 0, 20, 10, 10, 20, 10, 32);
        noStroke();

        // Toes
        toe(-10, 25, 40, 45, 15);
        toe(0, 27, 40, 40, 5);
        toe(10, 30, 30, 35, -10);
        toe(20, 32, 45, 40, -30);
        toe(30, 31, 40, 30, -55);
        popMatrix();

        // First Leg Segment
        fill(130, 70, 50);
        noStroke();
        beginShape();
        vertex(-12, 0);
        vertex(12, 0);
        bezierVertex(13, 20, 12, 30, 10, 50);
        vertex(-10, 50);
        bezierVertex(-12, 30, -13, 20, -12, 0);
        endShape();
        fill(0, 0, 50, 50);
        beginShape();
        vertex(-12, 0);
        vertex(3, 0);
        bezierVertex(-3, 20, -3, 30, 0, 50);
        vertex(-10, 50);
        bezierVertex(-12, 30, -13, 20, -12, 0);
        endShape();
        spike(3, 45, 10, 15, 175);
        spike(-5, 40, 15, 10, 195);
        spike(5, 30, 20, 15, 170);
        spike(-6, 20, 35, 40, 200);
        spike(5, 17, 30, 25, 130);
        spike(-3, 15, 20, 20, 150);
        spike(-5, 5, 30, 25, 215);
        spike(7, 3, 20, 25, 140);
        spike(0, 0, 30, 20, 160);
        popMatrix();
    }
    private static void pjsCode() {

    }
}
