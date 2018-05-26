package codeu.controller;
import codeu.model.store.persistence.PersistentDataStore;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;

import java.util.*;
import java.util.List;
import java.util.ArrayList;

// DATA STORE IMPORTS
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.store.persistence.PersistentDataStoreException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.time.Instant;



public class AdminServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  // Store class that stores datastore objects
  private PersistentDataStore dataStore;

  // Stores hard-coded list of names
  private List<String> names;

  // Site statistics variable
  private List<User> users;


  /**
   * Set up state for handling login-related requests. This method is only called when running in a
   * server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    names = new ArrayList<String>();
    names.add("annepham");
    names.add("jeanette");
    names.add("jorge");

    users = new ArrayList<User>();
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * This function fires when a user requests the /admin URL. It simply forwards the request to
   * login.jsp.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException, PersistentDataStoreException {

        String username = (String) request.getSession().getAttribute("user");
        if (username == null || !names.contains(username)) {
          // user is not logged in, don't let them see the admin page
          response.sendRedirect("/login");
        } else {
          response.sendRedirect("/admin.jsp");
        }

        User user = userStore.getUser(username);
        if (user == null) {
          // user was not found, don't let them see the admin page
          System.out.println("Access Denied: does this work" + username);
          response.sendRedirect("/login");
        } else {
          request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
        }


        // after login ?
        users = dataStore.loadUsers();
        int length = users.size();
        request.setAttribute("numUsers", length);
        request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the login form. It gets the username and password from
   * the submitted form data, checks for validity and if correct adds the username to the session so
   * we know the user is logged in.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException, PersistentDataStoreException {
      request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }
}
