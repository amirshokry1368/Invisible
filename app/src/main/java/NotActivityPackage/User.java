package NotActivityPackage;

public class User {
    private String Name;
    private String Email;

    public User(String name, String email) {
        Name = name;
        Email = email;
    }

    public User() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
