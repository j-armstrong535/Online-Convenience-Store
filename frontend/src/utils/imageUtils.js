/**
 * Utility functions for handling product images
 */

// Default placeholder image URL
export const DEFAULT_PLACEHOLDER = 'https://i.imgur.com/EJLFNOwg.jpg';

/**
 * Get product image URL with fallback to placeholder
 * @param {Object} product - Product object
 * @returns {string} Image URL
 */
export const getProductImageUrl = (product) => {
    if (!product) return DEFAULT_PLACEHOLDER;

    // If product has imageUrl from backend, use it
    if (product.imageUrl) {
        return product.imageUrl;
    }

    // If product has id, construct Cloudinary URL
    if (product.id) {
        return `https://res.cloudinary.com/dtglrc8my/image/upload/v1760861224/${product.id}.jpg`;
    }

    // Fallback to placeholder
    return DEFAULT_PLACEHOLDER;
};

/**
 * Get placeholder URL for a specific product
 * @param {Object} product - Product object
 * @returns {string} Placeholder URL
 */
export const getProductPlaceholder = (product) => {
    if (!product || !product.name) {
        return DEFAULT_PLACEHOLDER;
    }

    const encodedName = encodeURIComponent(product.name);
    return `https://i.imgur.com/EJLFNOwg.jpg`;
};

/**
 * Handle image error by setting placeholder
 * @param {Event} e - Error event
 * @param {Object} product - Product object (optional)
 */
export const handleImageError = (e, product = null) => {
    // Prevent infinite loop if placeholder also fails
    if (e.target.src.includes('placeholder')) {
        return;
    }

    e.target.src = product ? getProductPlaceholder(product) : DEFAULT_PLACEHOLDER;
};
