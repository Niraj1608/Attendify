package com.project.gui.user;



import org.mindrot.jbcrypt.BCrypt;

public class PasswordManager {

    // Hash a password
    public static String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // Check a password against a hashed value
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

//    public static void main(String[] args) {
//        // Example usage
//        String originalPassword = "Vyom7482";
//        String hashedPassword = hashPassword(originalPassword);
//
//        System.out.println("Original Password: " + originalPassword);
//        System.out.println("Hashed Password: " + hashedPassword);
//
//        // Verifying the password
//        boolean isMatch = verifyPassword("", hashedPassword);
//        System.out.println("Password Match: " + isMatch);
//    }
}
