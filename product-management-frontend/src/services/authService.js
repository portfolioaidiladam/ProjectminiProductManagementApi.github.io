import axios from 'axios';

const API_URL = 'http://localhost:8070/api/auth';

class AuthService {
    login(username, password) {
        return axios
            .post(API_URL + '/login', { username, password })
            .then(response => {
                if (response.data.success && response.data.data.token) {
                    localStorage.setItem('user', JSON.stringify(response.data.data));
                }
                return response.data;
            });
    }

    logout() {
        localStorage.removeItem('user');
    }

    register(username, email, password) {
        return axios.post(API_URL + '/register', {
            username,
            email,
            password
        });
    }

    getCurrentUser() {
        return JSON.parse(localStorage.getItem('user'));
    }

    getAuthHeader() {
        const user = JSON.parse(localStorage.getItem('user'));
        if (user && user.token) {
            return { Authorization: 'Bearer ' + user.token };
        } else {
            return {};
        }
    }
}

export default new AuthService();