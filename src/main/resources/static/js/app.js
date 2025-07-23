// API Configuration
const API_BASE_URL = 'http://localhost:8080/api';
let authToken = localStorage.getItem('authToken');
let currentUser = null;

// Axios interceptor for auth token
axios.interceptors.request.use(
    config => {
        if (authToken) {
            config.headers.Authorization = `Bearer ${authToken}`;
        }
        return config;
    },
    error => Promise.reject(error)
);

// DOM Elements
const loginSection = document.getElementById('loginSection');
const productSection = document.getElementById('productSection');
const loginForm = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');
const productForm = document.getElementById('productForm');
const searchForm = document.getElementById('searchForm');
const productsTableBody = document.getElementById('productsTableBody');
const usernameSpan = document.getElementById('username');
const logoutBtn = document.getElementById('logoutBtn');
const loginTab = document.getElementById('loginTab');
const registerTab = document.getElementById('registerTab');
const productModal = new bootstrap.Modal(document.getElementById('productModal'));

// Tab switching
loginTab.addEventListener('click', () => {
    loginTab.classList.add('active');
    registerTab.classList.remove('active');
    loginForm.style.display = 'block';
    registerForm.style.display = 'none';
});

registerTab.addEventListener('click', () => {
    registerTab.classList.add('active');
    loginTab.classList.remove('active');
    registerForm.style.display = 'block';
    loginForm.style.display = 'none';
});

// Authentication
loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    try {
        const response = await axios.post(`${API_BASE_URL}/auth/login`, {
            username: document.getElementById('loginUsername').value,
            password: document.getElementById('loginPassword').value
        });

        if (response.data.success) {
            authToken = response.data.data.token;
            localStorage.setItem('authToken', authToken);
            currentUser = response.data.data.username;
            showProductSection();
        }
    } catch (error) {
        alert('Login failed: ' + (error.response?.data?.message || 'Unknown error'));
    }
});

registerForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    try {
        const response = await axios.post(`${API_BASE_URL}/auth/register`, {
            username: document.getElementById('regUsername').value,
            email: document.getElementById('regEmail').value,
            password: document.getElementById('regPassword').value
        });

        if (response.data.success) {
            alert('Registration successful! Please login.');
            loginTab.click();
        }
    } catch (error) {
        alert('Registration failed: ' + (error.response?.data?.message || 'Unknown error'));
    }
});

logoutBtn.addEventListener('click', () => {
    localStorage.removeItem('authToken');
    authToken = null;
    currentUser = null;
    showLoginSection();
});

// Product Management
async function loadProducts() {
    try {
        const response = await axios.get(`${API_BASE_URL}/products`);
        if (response.data.success) {
            displayProducts(response.data.data);
        }
    } catch (error) {
        console.error('Failed to load products:', error);
    }
}

function displayProducts(products) {
    productsTableBody.innerHTML = '';
    products.forEach(product => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${product.id}</td>
            <td>${product.name}</td>
            <td>${product.description || ''}</td>
            <td>${product.price}</td>
            <td>${new Date(product.createdAt).toLocaleDateString()}</td>
            <td>
                <button class="btn btn-sm btn-primary btn-action" onclick="editProduct(${product.id})">Edit</button>
                <button class="btn btn-sm btn-danger btn-action" onclick="deleteProduct(${product.id})">Delete</button>
            </td>
        `;
        productsTableBody.appendChild(row);
    });
}

productForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const productId = document.getElementById('productId').value;
    const productData = {
        name: document.getElementById('productName').value,
        description: document.getElementById('productDescription').value,
        price: parseFloat(document.getElementById('productPrice').value)
    };

    try {
        let response;
        if (productId) {
            response = await axios.put(`${API_BASE_URL}/products/${productId}`, productData);
        } else {
            response = await axios.post(`${API_BASE_URL}/products`, productData);
        }

        if (response.data.success) {
            productModal.hide();
            loadProducts();
            productForm.reset();
        }
    } catch (error) {
        alert('Failed to save product: ' + (error.response?.data?.message || 'Unknown error'));
    }
});

searchForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const searchData = {
        name: document.getElementById('searchName').value,
        minPrice: document.getElementById('minPrice').value || null,
        maxPrice: document.getElementById('maxPrice').value || null
    };

    try {
        const response = await axios.post(`${API_BASE_URL}/products/search`, searchData);
        if (response.data.success) {
            displayProducts(response.data.data);
        }
    } catch (error) {
        console.error('Search failed:', error);
    }
});

async function editProduct(id) {
    try {
        const response = await axios.get(`${API_BASE_URL}/products/${id}`);
        if (response.data.success) {
            const product = response.data.data;
            document.getElementById('productId').value = product.id;
            document.getElementById('productName').value = product.name;
            document.getElementById('productDescription').value = product.description || '';
            document.getElementById('productPrice').value = product.price;
            document.getElementById('modalTitle').textContent = 'Edit Product';
            productModal.show();
        }
    } catch (error) {
        alert('Failed to load product details');
    }
}

async function deleteProduct(id) {
    if (confirm('Are you sure you want to delete this product?')) {
        try {
            const response = await axios.delete(`${API_BASE_URL}/products/${id}`);
            if (response.data.success) {
                loadProducts();
            }
        } catch (error) {
            alert('Failed to delete product');
        }
    }
}

// Modal reset
document.getElementById('productModal').addEventListener('hidden.bs.modal', () => {
    productForm.reset();
    document.getElementById('productId').value = '';
    document.getElementById('modalTitle').textContent = 'Add Product';
});

// View Management
function showLoginSection() {
    loginSection.style.display = 'block';
    productSection.style.display = 'none';
}

function showProductSection() {
    loginSection.style.display = 'none';
    productSection.style.display = 'block';
    usernameSpan.textContent = currentUser;
    loadProducts();
}

// Initialize
if (authToken) {
    // Verify token is still valid
    axios.get(`${API_BASE_URL}/products`)
        .then(() => {
            showProductSection();
        })
        .catch(() => {
            localStorage.removeItem('authToken');
            authToken = null;
            showLoginSection();
        });
} else {
    showLoginSection();
}