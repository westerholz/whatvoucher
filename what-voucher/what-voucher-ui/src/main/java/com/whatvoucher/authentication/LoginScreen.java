package com.whatvoucher.authentication;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.whatvoucher.AdminView;
import com.whatvoucher.MainLayout;

/**
 * UI content when the user is not logged in yet.
 */
@Route("Login")
@PageTitle("Login")
@HtmlImport("css/shared-styles.html")
public class LoginScreen extends FlexLayout {

    private AccessControl accessControl;

    public LoginScreen() {
        accessControl = AccessControlFactory.getInstance().createAccessControl();
        buildUI();
    }

    private void buildUI() {
        setSizeFull();
        setClassName("login-screen");

        // login form, centered in the available part of the screen
        LoginForm loginForm = new LoginForm();
        loginForm.addLoginListener(this::login);
        loginForm.addForgotPasswordListener(
                event -> Notification.show("Hint: same as username"));

        // layout to center login form when there is sufficient screen space
        FlexLayout centeringLayout = new FlexLayout();
        centeringLayout.setSizeFull();
        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeringLayout.setAlignItems(Alignment.CENTER);
        centeringLayout.add(loginForm);

        // information text about logging in
        Component loginInformation = buildLoginInformation();

        add(loginInformation);
        add(centeringLayout);
    }

    private Component buildLoginInformation() {
        VerticalLayout loginInformation = new VerticalLayout();
        loginInformation.setClassName("login-information");

        H1 loginInfoHeader = new H1("What Voucher?");
        Span loginInfoText = new Span(
                "How often do you find a voucher that you just forgot about? " +
                        "This is over now. Manage all your voucher here and " +
                        "get regular reminders before it is too late!");
        loginInformation.add(loginInfoHeader);
        loginInformation.add(loginInfoText);

        return loginInformation;
    }

    private void login(LoginForm.LoginEvent event) {
        if (accessControl.signIn(event.getUsername(), event.getPassword())) {
            registerAdminViewIfApplicable();
            getUI().get().navigate("");
        } else {
            event.getSource().setError(true);
        }
    }

    private void registerAdminViewIfApplicable() {
        // register the admin view dynamically only for any admin user logged in
        if (accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            RouteConfiguration.forSessionScope().setRoute(AdminView.VIEW_NAME,
                    AdminView.class, MainLayout.class);
            // as logout will purge the session route registry, no need to
            // unregister the view on logout
        }
    }

}
