package de.simontenbeitel.regelfragen.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import de.simontenbeitel.regelfragen.domain.interactor.base.AbstractInteractor;

@Singleton
@Component
public interface ApplicationComponent {

    void inject(AbstractInteractor interactor);

}
