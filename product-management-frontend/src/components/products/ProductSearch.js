import React, { useState } from 'react';
import { Form, Button, Row, Col } from 'react-bootstrap';

function ProductSearch({ onSearch, onReset }) {
    const [searchParams, setSearchParams] = useState({
        name: '',
        minPrice: '',
        maxPrice: ''
    });

    const handleChange = (e) => {
        setSearchParams({
            ...searchParams,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const params = {
            name: searchParams.name || null,
            minPrice: searchParams.minPrice ? parseFloat(searchParams.minPrice) : null,
            maxPrice: searchParams.maxPrice ? parseFloat(searchParams.maxPrice) : null
        };
        onSearch(params);
    };

    const handleReset = () => {
        setSearchParams({
            name: '',
            minPrice: '',
            maxPrice: ''
        });
        onReset();
    };

    return (
        <div className="search-section">
            <Form onSubmit={handleSubmit}>
                <Row>
                    <Col md={4}>
                        <Form.Group className="mb-3">
                            <Form.Label>Product Name</Form.Label>
                            <Form.Control
                                type="text"
                                name="name"
                                value={searchParams.name}
                                onChange={handleChange}
                                placeholder="Search by name"
                            />
                        </Form.Group>
                    </Col>
                    <Col md={3}>
                        <Form.Group className="mb-3">
                            <Form.Label>Min Price</Form.Label>
                            <Form.Control
                                type="number"
                                name="minPrice"
                                value={searchParams.minPrice}
                                onChange={handleChange}
                                placeholder="0.00"
                                min="0"
                                step="0.01"
                            />
                        </Form.Group>
                    </Col>
                    <Col md={3}>
                        <Form.Group className="mb-3">
                            <Form.Label>Max Price</Form.Label>
                            <Form.Control
                                type="number"
                                name="maxPrice"
                                value={searchParams.maxPrice}
                                onChange={handleChange}
                                placeholder="1000.00"
                                min="0"
                                step="0.01"
                            />
                        </Form.Group>
                    </Col>
                    <Col md={2} className="d-flex align-items-end">
                        <Form.Group className="mb-3 w-100">
                            <Button variant="primary" type="submit" className="w-100 mb-2">
                                Search
                            </Button>
                            <Button variant="secondary" onClick={handleReset} className="w-100">
                                Reset
                            </Button>
                        </Form.Group>
                    </Col>
                </Row>
            </Form>
        </div>
    );
}

export default ProductSearch;