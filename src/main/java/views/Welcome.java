package views;

import dao.userDAO;
import model.User;
import service.GenerateOTP;
import service.SendOTPService;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

public class Welcome {
    public void welcomeScreen(){
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("welcome to the app");
        System.out.println("press 1 to login");
        System.out.println("press 2 to signup");
        System.out.println("press 0 to exit");
        int choice =0;
        try{
            choice = Integer.parseInt(br.readLine());
        }
        catch (IOException ex ){
            ex.printStackTrace();
        }
        switch(choice) {
            case 1 -> login();
            case 2 -> singUp();
            case 0 -> System.exit(0);
        }

    }
    private void login() {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter Email");
        String email = sc.nextLine();
        try{
            if(userDAO.isExists(email)){
                String genOTP = GenerateOTP.getOTP();
                SendOTPService.sendOTP(email, genOTP);
                System.out.println("Enter the OTP");
                String otp = sc.nextLine();
                if(otp.equals(genOTP)){
                    new UserView(email).home();
                }
                else{
                    System.out.println("Wrong OTP");
                }
            }
            else{
                System.out.println("User not found");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }


    }

    private void singUp() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter name");
        String name= sc.nextLine();
        System.out.println("Enter Email");
        String email = sc.nextLine();
        String genOTP = GenerateOTP.getOTP();
        SendOTPService.sendOTP(email, genOTP);
        System.out.println("Enter the OTP");
        String otp = sc.nextLine();
        if(otp.equals(genOTP)){
            User user = new User(name,email);
            int response = UserService.saveUser(user);
            switch(response){
                case 0 -> System.out.println("User registered");
                case 1 -> System.out.println("User already exists");
            }
        }
        else{
            System.out.println("Wrong OTP");
        }


    }
}
