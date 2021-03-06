/**
 * Copyright 2012-2014 Appium Committers
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 **/

package settings.appium.com.settings;

import android.content.Context;
import settings.appium.com.settings.handlers.*;

public class ServicesFactory {
  public static Service getService(Context context, String name) {
    if (name.equalsIgnoreCase("wifi")) {
      return new WiFiService(context);
    } else if (name.equalsIgnoreCase("data")) {
      return new DataService(context);
    } else if (name.equalsIgnoreCase("airplane")) {
      return new AirplaneService(context);
    } else {
      return null;
    }
  }
}
