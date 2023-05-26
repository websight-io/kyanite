package pl.ds.bulma.components.services;

import javax.inject.Inject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import pl.ds.bulma.components.configurations.RecaptchaConfiguration;

@Component(service = RecaptchaConfigurationService.class)
@Designate(ocd = RecaptchaConfiguration.class)
public class RecaptchaConfigurationService {

  @Inject
  private RecaptchaConfiguration config;

  @Activate
  protected void activate(RecaptchaConfiguration config) {
    this.config = config;
  }

  public String getCaptchaPublicKey() {
    return config.captchaPublicKey();
  }
}
