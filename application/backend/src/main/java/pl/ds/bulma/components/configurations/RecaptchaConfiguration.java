package pl.ds.bulma.components.configurations;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Recaptcha Configuration",
    description = "Reads the data for recaptcha config")
public @interface RecaptchaConfiguration {

  @AttributeDefinition(name = "Recaptcha public key",
      description = "Enter the recaptcha public key to be used for the google recaptcha service")
  String captchaPublicKey();
}
