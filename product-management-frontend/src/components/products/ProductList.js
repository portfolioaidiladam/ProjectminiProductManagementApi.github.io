import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Button, Spinner, Alert } from 'react-bootstrap';
import ProductForm from './ProductForm';
import ProductSearch from './ProductSearch';
import productService from '../../services/productService';

function ProductList() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [showForm, setShowForm] = useState(false);
    const [editingProduct, setEditingProduct] = useState(null);

    useEffect(() => {
        loadProducts();
    }, []);

    const loadProducts = async () => {
        try {
            setLoading(true);
            const response = await productService.getAll();
            if (response.data.success) {
                setProducts(response.data.data);
            }
        } catch (error) {
            setError('Failed to load products');
            console.error('Error loading products:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this product?')) {
            try {
                const response = await productService.delete(id);
                if (response.data.success) {
                    loadProducts();
                }
            } catch (error) {
                alert('Failed to delete product');
            }
        }
    };

    const handleEdit = (product) => {
        setEditingProduct(product);
        setShowForm(true);
    };

    const handleFormClose = () => {
        setShowForm(false);
        setEditingProduct(null);
        loadProducts();
    };

    const handleSearch = async (searchParams) => {
        try {
            setLoading(true);
            const response = await productService.search(searchParams);
            if (response.data.success) {
                setProducts(response.data.data);
            }
        } catch (error) {
            setError('Search failed');
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <div className="loading-spinner">
                <Spinner animation="border" role="status">
                    <span className="visually-hidden">Loading...</span>
                </Spinner>
            </div>
        );
    }

    return (
        <Container>
            <Row className="mb-4">
                <Col>
                    <h2>Product Management</h2>
                </Col>
                <Col className="text-end">
                    <Button
                        variant="primary"
                        onClick={() => setShowForm(true)}
                    >
                        Add New Product
                    </Button>
                </Col>
            </Row>

            <ProductSearch onSearch={handleSearch} onReset={loadProducts} />

            {error && <Alert variant="danger">{error}</Alert>}

            <Row>
                {products.length === 0 ? (
                    <Col>
                        <Alert variant="info">No products found. Add your first product!</Alert>
                    </Col>
                ) : (
                    products.map(product => (
                        <Col key={product.id} md={4} className="mb-4">
                            <div className="card product-card h-100">
                                <div className="card-body">
                                    <h5 className="card-title">{product.name}</h5>
                                    <p className="card-text">{product.description || 'No description'}</p>
                                    <h6 className="text-primary">${product.price}</h6>
                                    <p className="text-muted small">
                                        Created: {new Date(product.createdAt).toLocaleDateString()}
                                    </p>
                                    <div className="mt-3">
                                        <Button
                                            variant="sm"
                                            size="sm"
                                            onClick={() => handleEdit(product)}
                                            className="me-2"
                                        >
                                            Edit
                                        </Button>
                                        <Button
                                            variant="danger"
                                            size="sm"
                                            onClick={() => handleDelete(product.id)}
                                        >
                                            Delete
                                        </Button>
                                    </div>
                                </div>
                            </div>
                        </Col>
                    ))
                )}
            </Row>

            <ProductForm
                show={showForm}
                onHide={handleFormClose}
                product={editingProduct}
            />
        </Container>
    );
}

export default ProductList;