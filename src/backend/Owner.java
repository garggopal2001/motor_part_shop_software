package backend;
import java.util.*;

public class Owner {
private
    String username;
    String password;

public
    Owner() {
        username = "Not Set";
        password = "Not Set";
    }

    String getusername() {
        return username;
    }

    String getpassword() {
        return password;
    }

    int setusername(String UserName) {
        if(UserName.matches("[a-zA-Z]+") == true) {
            this.username = UserName;
            return 1;
        }
        return 0;
    }

    int setpassword(String Password) {
        if(Password.length() < 5) return 0;
        this.password = Password;
        return 1;
    }

    int validate(String Username, String Password) {
        if(this.username != Username || this.password != Password) return 0;
        return 1;
    }
}

