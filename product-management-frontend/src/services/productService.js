import axios from 'axios';
import authService from './authService';

const API_URL = 'http://localhost:8080/api/products';

class ProductService {
    getAll() {
        return axios.get(API_URL, { headers: authService.getAuthHeader() });
    }

    get(id) {
        return axios.get(`${API_URL}/${id}`, { headers: authService.getAuthHeader() });
    }

    create(data) {
        return axios.post(API_URL, data, { headers: authService.getAuthHeader() });
    }

    update(id, data) {
        return axios.put(`${API_URL}/${id}`, data, { headers: authService.getAuthHeader() });
    }

    delete(id) {
        return axios.delete(`${API_URL}/${id}`, { headers: authService.getAuthHeader() });
    }

    search(searchParams) {
        return axios.post(`${API_URL}/search`, searchParams, { headers: authService.getAuthHeader() });
    }
}

export default new ProductService();