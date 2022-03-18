package NotActivityPackage;

public class Post {
    private String email;
    private String message;

    public Post(String email, String message) {
        this.email = email;
        this.message = message;
    }

    public Post() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
