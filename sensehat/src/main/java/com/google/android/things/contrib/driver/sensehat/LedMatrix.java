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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

/**
 * LedMatrix implements a subset of the SurfaceHolder interface to allow to draw on the LED matrix.
 */
public class LedMatrix implements AutoCloseable {
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    private static final int BUFFER_SIZE = WIDTH * HEIGHT * 3 + 1;

    private Bitmap mBitmap = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888);
    private I2cDevice mDevice;

    /**
     * Create a new LED matrix driver connected on the given I2C bus.
     * @param bus I2C bus the sensor is connected to.
     * @throws IOException
     */
    public LedMatrix(String bus) throws IOException {
        PeripheralManagerService pioService = new PeripheralManagerService();
        mDevice = pioService.openI2cDevice(bus, SenseHat.I2C_ADDRESS);
    }

    /* package */ LedMatrix(I2cDevice device) {
        mDevice = device;
    }

    /**
     * Close the driver and the underlying device.
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        if (mDevice != null) {
            try {
                mDevice.close();
            } finally {
                mDevice = null;
            }
        }
    }

    /**
     * Start editing the pixels in the surface.
     * @return The Canvas to draw into the surface.
     */
    public Canvas lockCanvas() {
        return new Canvas(mBitmap);
    }

    /**
     * Finish editing pixels in the surface.
     * After this call, the surface's current pixels will be shown on the LED matrix.
     * @param canvas The Canvas previously returned by lockCanvas().
     * @throws IOException
     */
    public void unlockCanvasAndPost(Canvas canvas) throws IOException {
        byte[] colorBytes = new byte[BUFFER_SIZE];
        colorBytes[0] = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int p = mBitmap.getPixel(x, y);
                double a = Color.alpha(p) / 255.0;
                colorBytes[1+x+24*y] = (byte)((int)(Color.red(p)*a)>>3);
                colorBytes[1+x+8+24*y] = (byte)((int)(Color.green(p)*a)>>3);
                colorBytes[1+x+16+24*y] = (byte)((int)(Color.blue(p)*a)>>3);
            }
        }
        mDevice.write(colorBytes, colorBytes.length);
    }
}
