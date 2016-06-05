package source.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Query;
import org.hibernate.Session;
import source.entity.Users;
import util.HibernateUtil;

/**
 * Created by NKostya on 06.06.2016.
 */
public class EditUserInfoController {

    public PasswordField userpasswordField;
    public TextField usernameField;
    public TextField created_atField;
    public TextField usergroupField;
    public Button nextButton;
    public Button cancelButton;

    private Stage editStage;
    private Users users;
    private boolean nextClicked = false;
    private int userId;

    public void setEditStage(Stage stage) {
        this.editStage = stage;
    }

    public void setUser(Users user) {
        this.users = user;
        usernameField.setText(user.getUsername());
        userpasswordField.setText(user.getUserPassword());
        created_atField.setText(user.getCreated_at());
        usergroupField.setText(user.getUserGroup());
    }

    public boolean isNextClicked() {
        return this.nextClicked;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void nextAction(ActionEvent actionEvent) {
        if(!usernameField.getText().isEmpty() && !userpasswordField.getText().isEmpty() && !usergroupField.getText().isEmpty() && !created_atField.getText().isEmpty()) {
            this.users.setUsername(usernameField.getText());
            this.users.setUserPassword(userpasswordField.getText());
            this.users.setCreated_at(created_atField.getText());
            this.users.setUserGroup(usergroupField.getText());
        }
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery("update  Users set username = :username, userPassword = :userPassword, created_at = :created_at, userGroup = :userGroup where userId = :userId")
                    .setParameter("username", usernameField.getText())
                    .setParameter("userPassword", userpasswordField.getText())
                    .setParameter("created_at", created_atField.getText())
                    .setParameter("userGroup", usergroupField.getText())
                    .setParameter("userId", this.userId);
            query.executeUpdate();
            session.getTransaction().commit();
            this.nextClicked = true;
            editStage.close();
        } catch (Exception e) {
//            UsersAlert.throwsException(e);
            e.printStackTrace();
        } finally {
            if(session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void cancelAction(ActionEvent actionEvent) {
    }
}
