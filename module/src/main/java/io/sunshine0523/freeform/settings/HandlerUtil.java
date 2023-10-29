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

import android.annotation.SuppressLint;
import android.os.Handler;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;

@SuppressWarnings("JavaReflectionMemberAccess")
@SuppressLint("DiscouragedPrivateApi")
public class HandlerUtil {

    private static Field callbackField;

    public static void init() throws ReflectiveOperationException {
            callbackField = Handler.class.getDeclaredField("mCallback");
            callbackField.setAccessible(true);
    }

    public static Handler.Callback getCallback(@NonNull Handler handler) {
        try {
            return (Handler.Callback) callbackField.get(handler);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static void setCallback(@NonNull Handler handler, Handler.Callback callback){
        try {
            callbackField.set(handler, callback);
        } catch (IllegalAccessException ignored) {
        }
    }
}
