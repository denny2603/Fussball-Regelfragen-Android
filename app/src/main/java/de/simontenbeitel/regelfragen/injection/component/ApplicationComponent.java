package de.simontenbeitel.regelfragen.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import de.simontenbeitel.regelfragen.threading.MainThreadImpl;

@Singleton
@Component
public interface ApplicationComponent {

    MainThreadImpl mainThreadImpl();

}
