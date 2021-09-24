package third.task.stripe.command;


public class LoginCommand {
    String email;
    String password;

    public LoginCommand(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginCommand{" +
                "Email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
