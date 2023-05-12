package Controller;

import Model.Dao.UserDAO;
import Model.User;
import View.AdminView;

import java.util.ArrayList;


public class AdminController implements Observer {

    private AdminView adminView;
    private UserDAO userDAO;
    private Language language;

    public AdminController(Language language, int index) {
        this.adminView = new AdminView();
        this.language = language;
        this.language.attachObserver(this);
        language.setCurrentLanguage(index);
        this.userDAO =  new UserDAO();
        addActionListeners();
    }

    private void addActionListeners(){
        adminView.getBtnCreeaza().addActionListener(actionListener -> {
            String username = String.valueOf(adminView.getNameField().getText());
            String password = String.valueOf(adminView.getPasswordField().getPassword());
            String role = String.valueOf(adminView.getRolField().getText());

            ArrayList<User> users = (ArrayList<User>)userDAO.findAll();
            int id = users.size() + 1;

            if(!username.isEmpty() && !password.isEmpty() && !role.isEmpty()) {
                User user = new User(username, password, role, id);
                userDAO.insert(user);
            }
        });

        adminView.getBtnVizualizeazaListaUseri().addActionListener(actionListener -> {
            ArrayList<User> users = (ArrayList<User>)userDAO.findAll();
            String info = "";

            for(User u: users){
                info += u.toString();
            }

            adminView.setTextArea(info);
        });

        adminView.getBtnBack().addActionListener(action -> {
            System.out.println(language.getLanguage());
            new LogInController(language.getLanguage());
            AdminView.getFrame().dispose();
        });

        adminView.getBtnDelete().addActionListener(action -> {
            int id = Integer.parseInt(adminView.getIdField().getText());
            User user =  userDAO.findById(id);

            if(user != null){
                userDAO.delete(id);
            }
            else{
                adminView.showMessage("The user with this id doesn't exist!");
            }
        });

        adminView.getBtnUpdate().addActionListener(action -> {
            String username = String.valueOf(adminView.getNameField().getText());
            String password = String.valueOf(adminView.getPasswordField().getPassword());
            String role = String.valueOf(adminView.getRolField().getText());
            int id = Integer.parseInt(adminView.getIdField().getText());

            if(!username.isEmpty()){
                userDAO.update("nume", username, id);
            }
            if(!password.isEmpty()){
                userDAO.update("password", password, id);
            }
            if(!role.isEmpty()){
                userDAO.update("rol", role, id);
            }
        });

        adminView.getLanguageComboBox().addActionListener(actionListener -> {
            language.setCurrentLanguage(adminView.getLanguageComboBox().getSelectedIndex());
        });
    }

    @Override
    public void update() {
        adminView.getBtnUpdate().setText(language.getRb().getString("update"));
        adminView.getBtnDelete().setText(language.getRb().getString("delete"));
        adminView.getBtnCreeaza().setText(language.getRb().getString("insert"));
        adminView.getBtnVizualizeazaListaUseri().setText(language.getRb().getString("showUsers"));
        adminView.getBtnBack().setText(language.getRb().getString("back"));
        adminView.getLblName().setText(language.getRb().getString("userName"));
        adminView.getLblPassword().setText(language.getRb().getString("password"));
        adminView.getLblRol().setText(language.getRb().getString("role"));
        adminView.getLblUsersList().setText(language.getRb().getString("userList"));
        adminView.getLblLimba().setText(language.getRb().getString("language"));
    }
}
