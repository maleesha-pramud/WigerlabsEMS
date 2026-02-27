const USER_KEY = "user";

const UserStore = {

    // CREATE / UPDATE: Save user object
    save(userData) {
        try {
            const stringData = JSON.stringify(userData);
            localStorage.setItem(USER_KEY, stringData);
        } catch (error) {
            console.error("Error saving to localStorage", error);
        }
    },

    // READ: Get the full user object
    get() {
        const data = localStorage.getItem(USER_KEY);
        try {
            return data ? JSON.parse(data) : null;
        } catch (error) {
            console.error("Error parsing user data", error);
            return null;
        }
    },

    // READ: Specific fields (Helper methods)
    getRole() {
        const user = this.get();
        return user ? user.userRoleName : null;
    },

    getUserId() {
        const user = this.get();
        return user ? user.id : null;
    },

    // UPDATE: Update a specific property without losing others
    update(updates) {
        const currentUser = this.get();
        if (currentUser) {
            const updatedUser = { ...currentUser, ...updates };
            this.save(updatedUser);
        }
    },

    // DELETE: Clear user data (Logout)
    clear() {
        localStorage.removeItem(USER_KEY);
    },

    // CHECK: Is the user logged in?
    isLoggedIn() {
        return this.get() !== null;
    }
};

export default UserStore;