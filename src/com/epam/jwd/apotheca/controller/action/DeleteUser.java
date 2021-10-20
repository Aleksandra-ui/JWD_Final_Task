package com.epam.jwd.apotheca.controller.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.OrderManagerService;
import com.epam.jwd.apotheca.controller.RecipeManagerService;
import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.controller.validator.RoleAccessValidator;
import com.epam.jwd.apotheca.controller.validator.UserIdValidator;
import com.epam.jwd.apotheca.controller.validator.Validator;
import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.model.User;

public class DeleteUser implements RunCommand {

	private static final Logger logger = LoggerFactory.getLogger(DeleteUser.class);
	private Map<String, String[]> params;
	private User user;
	private boolean deleted;
	private User userToDelete;
	private List<User> users;
	private List<String> messages;
	private int totalCount;
	private int pageSize;
	private int currentPage;
	private int pagesCount;
	private Map<String, Validator> validators; 

	public DeleteUser() {
		messages = new ArrayList<String>();
		validators = new HashMap<String, Validator>();
		validators.put("user", new UserIdValidator("userId"));
		validators.put("role", new RoleAccessValidator(UserDAO.ROLE_NAME_ADMIN));
	}

	@Override
	public String run() {

		messages.clear();
		deleted = false;
		
		validators.get("user").setValue(params);
		validators.get("role").setValue(user);
		for (Validator validator : validators.values()) {
			if ( ! validator.validate() ) {
				messages.addAll( validator.getMessages() );
				validator.getMessages().stream().forEach(m -> logger.error(m));
			}
		}
		
		if ( messages.isEmpty() ) {
			Integer userId = Integer.valueOf(params.get("userId")[0]);
			userToDelete = UserManagerService.getInstance().getUser(userId);
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
		}

		totalCount = UserManagerService.getInstance().getTotalCount();
		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
		currentPage = params.get("currentPage") == null ? 1
				: Integer.valueOf(params.get("currentPage")[0]);
		pagesCount = totalCount / pageSize + ((totalCount % pageSize) == 0 ? 0 : 1);
		users = UserManagerService.getInstance().findUsersByRange(pageSize * (currentPage - 1), pageSize);

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
	
	public int getPagesCount() {
		return pagesCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

}
