/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.things.contrib.driver.sensehat;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class SenseHatDeviceTest {
    @Test
    public void senseHat_DisplayColor() throws IOException {
        // Color the LED matrix.
        LedMatrix display = SenseHat.openDisplay();
        Canvas canvas = display.lockCanvas();
        canvas.drawColor(Color.MAGENTA);
        display.unlockCanvasAndPost(canvas);
        // Close the display when done.
        display.close();
    }

    @Test
    public void senseHat_DisplayGradient() throws IOException {
        // Display a gradient on the LED matrix.
        LedMatrix display = SenseHat.openDisplay();
        Canvas canvas = display.lockCanvas();
        Paint paint = new Paint();
        paint.setShader(new RadialGradient(4, 4, 4, Color.RED, Color.BLUE, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 0, 8, 8, paint);
        display.unlockCanvasAndPost(canvas);
        // Close the display when done.
        display.close();
    }
}
