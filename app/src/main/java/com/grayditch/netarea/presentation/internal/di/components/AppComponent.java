/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.grayditch.netarea.presentation.internal.di.components;

import com.grayditch.netarea.presentation.views.mainactivity.activity.MainActivity;
import com.grayditch.netarea.presentation.views.mainactivity.fragments.login.LoginFragment;
import com.grayditch.netarea.presentation.views.mainactivity.fragments.qualifications.QualificationsFragment;
import com.grayditch.netarea.presentation.internal.di.modules.AppModule;
import com.grayditch.netarea.presentation.internal.di.modules.DBModule;
import com.grayditch.netarea.presentation.internal.di.modules.DependenciesModule;
import com.grayditch.netarea.presentation.internal.di.modules.NetworkModule;
import com.grayditch.netarea.presentation.internal.di.modules.PresentersModule;
import com.grayditch.netarea.presentation.internal.di.modules.RepositoriesModule;
import com.grayditch.netarea.presentation.internal.di.modules.UseCasesModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = {PresentersModule.class, AppModule.class, NetworkModule.class,
        UseCasesModule.class, RepositoriesModule.class, DependenciesModule.class, DBModule.class})
public interface AppComponent {
    void inject(LoginFragment loginFragment);

    void inject(MainActivity mainActivity);

    void inject(QualificationsFragment qualificationsFragment);
}
