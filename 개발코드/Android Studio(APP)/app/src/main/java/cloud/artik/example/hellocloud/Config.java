/*
 * Copyright (C) 2017 Samsung Electronics Co., Ltd.
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

package cloud.artik.example.hellocloud;

class Config {


    //static final String CLIENT_ID = "284a2e64219b4e98a3558ea7c8692ccf"; // AKA application ID
    static final String CLIENT_ID = "284a2e64219b4e98a3558ea7c8692ccf"; // AKA application ID       서브어플
    static final String mAccessToken = "52304eca8da34cf38b31753075649846";
    static final String DEVICE_ID = "bfff16ca56be4c5896a0cfd8c3ce7e66";  // Device ID

    /* 이전 규혁이 053
    static final String CLIENT_ID = "745583dbc4d946d5a28be0769e718ab0"; // AKA application ID
    static final String DEVICE_ID = "87aaf3aad0414bdca02c046bc932f663";  // Device ID
       */
    // MUST be consistent with "AUTH REDIRECT URL" of your application set up at the developer.artik.cloud
    static final String REDIRECT_URI = "cloud.artik.example.hellocloud://oauth2callback";

}
