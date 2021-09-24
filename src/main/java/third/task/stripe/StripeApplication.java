package third.task.stripe;

import com.stripe.model.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StripeApplication extends Application {

    public static void main(String[] args) {

        SpringApplication.run(StripeApplication.class, args);
    }

}
