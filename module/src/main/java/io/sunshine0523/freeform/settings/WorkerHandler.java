/*
 * This file is part of Sui.
 *
 * Sui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sui.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2021 Sui Contributors
 */

package io.sunshine0523.freeform.settings;

import android.os.Handler;
import android.os.HandlerThread;

public class WorkerHandler {

    private static final HandlerThread HANDLER_THREAD;
    private static final Handler HANDLER;

    static {
        HANDLER_THREAD = new HandlerThread("MF");
        HANDLER_THREAD.start();
        HANDLER = new Handler(HANDLER_THREAD.getLooper());
    }

    public static Handler get() {
        return HANDLER;
    }
}
