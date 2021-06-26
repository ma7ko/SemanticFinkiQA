import axios from "../custom-axios/axios";


const authService = {

    register(username, email, password) {
        return axios.post("/auth/signup", {
            username,
            email,
            password
        });
    },

    logIn(username, password) {
        return axios.post( "/auth/signin", {
                username,
                password
            })
    },

    logOut() {
        localStorage.removeItem("user");
    }
}

export default authService;