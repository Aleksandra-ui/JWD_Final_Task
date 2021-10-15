package com.epam.jwd.apotheca.controller.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.controller.OrderManagerService;
import com.epam.jwd.apotheca.controller.RecipeManagerService;
import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.model.User;

public class DeleteUser implements RunCommand {

	private Map<String, String[]> params;
	private User user;
	private boolean deleted;
	private User userToDelete;
	private List<User> users;
	private List<String> messages;

	public DeleteUser() {
		messages = new ArrayList<String>();
	}

	@Override
	public String run() {

		messages.clear();
		deleted = false;

		if ("admin".equalsIgnoreCase(user.getRole().getName())) {

			Integer userId = params.get("userId") != null ? Integer.valueOf(params.get("userId")[0]) : null;

			if (userId != null) {
				userToDelete = UserManagerService.getInstance().getUser(userId);
				if (userToDelete != null) {
					if (OrderManagerService.getInstance().deleteUserOrders(userToDelete)) {
						if (RecipeManagerService.getInstance().deleteUserRecipes(userToDelete)) {
							boolean success = true;
							if (UserManagerService.getInstance().canPrescribe(userToDelete)) {
								success = RecipeManagerService.getInstance().switchToSuperDoc(userToDelete);
							}
							if (success) {
								deleted = UserManagerService.getInstance().deleteUser(userId);
								if (deleted) {
									messages.add("User " + userToDelete.getName() + " was successfully deleted.");
								} else {
									messages.add("Unable to delete user.");
									messages.add("Internal error while deleting user " + userToDelete.getName() + ".");
								}
							} else {
								messages.add("Unable to delete user.");
								messages.add("Internal error while updating recipes of user " + userToDelete.getName()
										+ ".");
							}
						} else {
							messages.add("Unable to delete user.");
							messages.add(
									"Internal error while deleting recipes of user " + userToDelete.getName() + ".");
						}
					} else {
						messages.add("Unable to delete user.");
						messages.add("Internal error while deleting orders of user " + userToDelete.getName() + ".");
					}
				} else {
					messages.add("User with id " + userId + " doesn't exist.");
				}
			}

		} else {

			messages.add("You have no permission to delete users!");

		}

		users = UserManagerService.getInstance().getUsers();

		return null;
	}

	@Override
	public String getView() {
		// return "secure/deleteUser.jsp";
		return "logonPage1.jsp";
	}

	@Override
	public void setParams(Map<String, String[]> params) {
		this.params = params;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public boolean isSecure() {
		return true;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public User getUserToDelete() {
		return userToDelete;
	}

	public List<User> getUsers() {
		return users;
	}

	public List<String> getMessages() {
		return messages;
	}

}
