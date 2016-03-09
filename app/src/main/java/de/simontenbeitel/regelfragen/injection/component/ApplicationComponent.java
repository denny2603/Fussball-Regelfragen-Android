package de.simontenbeitel.regelfragen.injection.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import de.simontenbeitel.regelfragen.injection.ApplicationContext;
import de.simontenbeitel.regelfragen.injection.module.ApplicationModule;
import de.simontenbeitel.regelfragen.domain.interactor.base.AbstractInteractor;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    void inject(AbstractInteractor interactor);

}
