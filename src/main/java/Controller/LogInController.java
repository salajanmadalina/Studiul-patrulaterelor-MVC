package Controller;

import Model.Dao.UserDAO;
import Model.User;
import View.LogInView;
import java.util.ArrayList;

public class LogInController implements Observer{
    private LogInView logInView;
    private Language language;
    private UserDAO userDAO;

    public LogInController(int index) {
        this.logInView = new LogInView();
        this.language = new Language();
        language.attachObserver(this);
        language.setCurrentLanguage(index);
        addActionListeners();
    }

    private void addActionListeners(){
        logInView.getLogIn().addActionListener(actionListener -> {
            String userName = String.valueOf(logInView.getNameField().getText());
            String password = String.valueOf(logInView.getPasswordField().getPassword());

            userDAO =  new UserDAO();
            ArrayList<User> users = (ArrayList<User>)userDAO.findAll();
            User user = getRegisteredUser(users, userName, password);
            if( user!= null){
                showUserInterface(user);
            }

        });

        logInView.getGuestMode().addActionListener(actionListener -> {
            new GuestController(language, logInView.getLanguageComboBox().getSelectedIndex());
            logInView.getFrame().dispose();
        });

        logInView.getBtnRegister().addActionListener(actionListener -> {
            String userName = String.valueOf(logInView.getNameField().getText());
            String password = String.valueOf(logInView.getPasswordField().getPassword());

            userDAO =  new UserDAO();
            int id = userDAO.findAll().size() + 2;
            if(!userName.isEmpty() && !password.isEmpty()) {
                User user = new User(userName, password, "ELEV", id);
                userDAO.insert(user);
            }
        });

        logInView.getLanguageComboBox().addActionListener(actionListener -> {
            language.setCurrentLanguage(logInView.getLanguageComboBox().getSelectedIndex());
        });
    }

    @Override
    public void update() {
        logInView.getLblUsername().setText(language.getRb().getString("userName"));
        logInView.getLblPassword().setText(language.getRb().getString("password"));
        logInView.getGuestMode().setText(language.getRb().getString("guestMode"));
        logInView.getLogIn().setText(language.getRb().getString("logIn"));
        logInView.getBtnRegister().setText(language.getRb().getString("register"));
        logInView.getLblStudiulPatrulaterelor().setText(language.getRb().getString("titleLogIn"));

    }

    private User getRegisteredUser( ArrayList<User> users, String userName, String password){
        for(User user: users){
            if(user.getNume().equals(userName) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }

    private void showUserInterface(User user){
        switch (user.getRol()){
            case "ADMIN":
                new AdminController(language, language.getLanguage());
                logInView.getFrame().dispose();
                break;
            case "ELEV":
                new UserController(language, language.getLanguage(), user.getId());
                logInView.getFrame().dispose();
                break;
        }
    }

}
